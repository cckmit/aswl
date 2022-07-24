package com.aswl.as.ibrs.controller;

import com.aswl.as.common.core.constant.CommonConstant;
import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.log.annotation.Log;
import com.aswl.as.ibrs.api.dto.DeviceDiscoverDto;
import com.aswl.as.ibrs.api.dto.DeviceDto;
import com.aswl.as.ibrs.api.module.*;
import com.aswl.as.ibrs.api.vo.DeviceDiscoverVo;
import com.aswl.as.ibrs.service.*;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@Api(value = "/api/v1/deviceDiscover", tags = "设备发现")
@RestController
@RequestMapping(value = "/v1/deviceDiscover")
@Slf4j
@AllArgsConstructor
public class DeviceDiscoverController {
    private final DeviceDiscoverService deviceDiscoverService;
    private final DeviceService deviceService;
    private final DeviceKindService deviceKindService;
    private final DeviceTypeService deviceTypeService;
    private final DeviceModelService deviceModelService;
    private final RegionService regionService;

    /*@GetMapping(value = "devices")
    @ApiOperation(value = "查询所有设备发现")
    @Log("查询所有设备发现")
    public ResponseBean<List<DeviceDiscover>> findAll(){
        return new ResponseBean<>(deviceDiscoverService.findAll());
    }*/

    @GetMapping(value = "{id}")
    @ApiOperation(value = "根据Id设查询备发现")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "设备发现Id", paramType = "path", required = true, dataType = "String"))
    @Log("根据Id设查询备发现")
    public ResponseBean<DeviceDiscover> findById(@PathVariable String id){
        return new ResponseBean<DeviceDiscover>(deviceDiscoverService.findById(id));
    }
    @PostMapping("insert")
    @ApiOperation("发现设备新增到设备表")
    @ApiImplicitParams(@ApiImplicitParam(name = "DeviceDiscoverDto", value = "设备发现dto", required = true, paramType = "body", dataType = "DeviceDiscoverDto"))
    @Log("发现设备新增到设备表")
    public ResponseBean<Boolean> bathInsertToDevice(@RequestBody @Valid List<DeviceDiscoverDto> deviceDiscoverDtoList){
        int num = 0;
        for (DeviceDiscoverDto deviceDiscoverDto : deviceDiscoverDtoList) {
            String deviceModelId = deviceDiscoverDto.getDeviceModelId();
            //根据deviceModelId查询设备类型
            DeviceModel deviceModel = new DeviceModel();
            deviceModel.setId(deviceModelId);
            DeviceModel dm = deviceModelService.get(deviceModel);
            //查询设备类型
            DeviceType dt = deviceTypeService.findByDeviceType(dm.getDeviceType());
            //设备种类
            DeviceKind deviceKind = new DeviceKind();
            deviceKind.setId(dt.getDeviceKindId());
            DeviceKind dk = deviceKindService.get(deviceKind);
            DeviceDto deviceDto = new DeviceDto();
            //deviceDto.setId(deviceDiscoverDto.getId());
            deviceDto.setDeviceCode(deviceDiscoverDto.getDeviceFactoryCode());
            deviceDto.setDeviceName(deviceDiscoverDto.getDeviceName());
            deviceDto.setIp(deviceDiscoverDto.getIp());
            deviceDto.setPort(Integer.parseInt(deviceDiscoverDto.getPort()));
            deviceDto.setDeviceModelId(deviceDiscoverDto.getDeviceModelId());
            deviceDto.setCreateDate(new Date());
            deviceDto.setApplicationCode(deviceDiscoverDto.getApplicationCode());
            deviceDto.setTenantCode(deviceDiscoverDto.getTenantCode());
            deviceDto.setDeviceKindId(dk.getId());
            deviceDto.setUserName("admin");
            deviceDto.setPassword("admin");
            deviceDto.setDeviceType(dt.getDeviceType());
            deviceDto.setDeviceModelName(dm.getDeviceModelName());
            deviceDto.setDiscoverId(deviceDiscoverDto.getId());
            deviceDto.setProjectId("1");
            if ((deviceDiscoverDto.getRegionCode() == null||deviceDiscoverDto.getRegionCode().equals("")) ||
                    (deviceDiscoverDto.getRegionId() == null||deviceDiscoverDto.getRegionId().equals(""))){
                List<Region> regions = regionService.findRegionByParentId("-1");
                deviceDto.setRegionId(regions.get(0).getId());
                deviceDto.setRegionCode(regions.get(0).getRegionCode());
            }else {
                deviceDto.setRegionId(deviceDiscoverDto.getRegionId());
                deviceDto.setRegionCode(deviceDiscoverDto.getRegionCode());
            }
            num += deviceService.insert(deviceDto);
             //deviceService.insert(deviceDto);
        }
        return new ResponseBean<>(num == deviceDiscoverDtoList.size());
    }

    /**
     * 分页查询设备发现列表
     *
     * @param pageNum
     * @param pageSize
     * @return PageInfo
     */
    @GetMapping("devices")
    @ApiOperation(value = "分页查询设备种类列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = "deviceDiscover", value = "发现设备", dataType = "deviceDiscover")
    })
    public ResponseBean<PageInfo<DeviceDiscoverVo>> deviceDiscoverList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                                                       @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                                                       DeviceDiscover deviceDiscover) {
        return new ResponseBean<>(deviceDiscoverService.findPage(pageNum,pageSize,deviceDiscover));
    }
}
