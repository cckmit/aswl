package com.aswl.as.ibrs.controller;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.aswl.as.common.core.constant.CommonConstant;
import com.aswl.as.common.core.enums.BusinessType;
import com.aswl.as.common.core.enums.EventTableEnum;
import com.aswl.as.common.core.exceptions.CommonException;
import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.utils.JPushUtils;
import com.aswl.as.common.core.utils.PageUtil;
import com.aswl.as.common.core.utils.SysUtil;
import com.aswl.as.common.core.vo.OsVo;
import com.aswl.as.common.core.web.BaseController;
import com.aswl.as.common.log.annotation.Log;
import com.aswl.as.ibrs.api.dto.DeviceAlarmDto;
import com.aswl.as.ibrs.api.dto.DeviceDto;
import com.aswl.as.ibrs.api.dto.ProjectDto;
import com.aswl.as.ibrs.api.module.Attachment;
import com.aswl.as.ibrs.api.module.Device;
import com.aswl.as.ibrs.api.module.Project;
import com.aswl.as.ibrs.api.vo.*;
import com.aswl.as.ibrs.common.Globals;
import com.aswl.as.ibrs.mq.MQSender;
import com.aswl.as.ibrs.service.*;
import com.aswl.as.ibrs.utils.StringUtils;
import com.aswl.iot.dto.QueryDeviceDataDto;
import com.aswl.iot.dto.constant.MQConstants;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Stopwatch;
import feign.Param;
import feign.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import jdk.nashorn.internal.runtime.GlobalConstants;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 设备controller
 *
 * @author dingfei
 * @date 2019-09-27 14:17
 */
@Slf4j
@AllArgsConstructor
@Api(value = "/api/v1/device", tags = "设备")
@RestController
@RequestMapping("/v1/device")
public class DeviceController extends BaseController {

    private final DeviceService deviceService;
    private final ProjectService projectService;
    private final AlarmStatisticsService alarmStatisticsService;
    private final RegionService regionService;
    private final FileController fileController;
    private final AttachmentService attachmentService;
    private final MQSender mqSender;

    /**
     * 根据ID获取设备
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "根据设备ID查询设备")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "设备ID", paramType = "path", required = true, dataType = "String"))
    @GetMapping(value = "/{id}")
    public ResponseBean<DeviceVo> findById(@PathVariable("id") String id) {
        return new ResponseBean<>(deviceService.findById(id));
    }

    /**
     * 查询所有设备
     *
     * @param device
     * @return ResponseBean
     */
    @GetMapping(value = "devices")
    @ApiOperation(value = "查询所有设备列表")
    @ApiImplicitParams(@ApiImplicitParam(name = "device", value = "设备对象", paramType = "body", required = true, dataType = "json"))
    public ResponseBean
            <List<Device>> findAll(Device device) {
        Stopwatch watch = Stopwatch.createStarted();
        device.setTenantCode(SysUtil.getTenantCode());
        List<Device> allList = deviceService.findAllList(device);
        watch.stop();
        log.info("查询设备列表耗时: {}ms",
                watch.elapsed(TimeUnit.MILLISECONDS));
        return new ResponseBean<>(allList);
    }

    /**
     * 分页查询设备列表
     *
     * @param pageNum
     * @param pageSize
     * @param sort
     * @param order
     * @param deviceDto
     * @return PageInfo
     */
    @GetMapping("deviceList")
    @ApiOperation(value = "分页查询设备列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "deviceDto", value = "设备信息", dataType = "DeviceDto")
    })
    public ResponseBean<PageInfo<DeviceVo>> deviceList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                                       @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                                       @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                                       @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
                                                       @RequestParam(value = "regionId", defaultValue = "") String regionId,
                                                       @RequestParam(value = "isAsos", defaultValue = "0", required = false) String isAsos,
                                                       DeviceDto deviceDto) {
        deviceDto.setRegionId(regionId);
        if (!CommonConstant.IS_ASOS_TRUE.equals(isAsos) && !SysUtil.isAdmin()) {
            deviceDto.setTenantCode(SysUtil.getTenantCode());


            if (StringUtils.isEmpty(deviceDto.getProjectId())) {
                deviceDto.setProjectId(SysUtil.getProjectId());
            } else {
                String projectId = SysUtil.getProjectId();
                if (!StringUtils.isEmpty(projectId))
                {
                    String[] arr = projectId.split(",");
                    boolean hasProjectId = false;
                    for (String temp : arr) {
                        if (deviceDto.getProjectId().equals(temp)) {
                            hasProjectId = true;
                            break;
                        }
                    }
                    if (!hasProjectId) {
                        return new ResponseBean(new PageInfo());
                    }
                }
            }
        }

        return new ResponseBean<>(deviceService.findPage(PageUtil.pageInfo(pageNum, pageSize, "", ""), deviceDto,isAsos));
    }

    /**
     * 新增设备
     *
     * @param deviceDto
     * @return ResponseBean
     */
    @PostMapping
    @ApiOperation(value = "新增设备")
    @ApiImplicitParams(@ApiImplicitParam(name = "deviceDto", value = "设备dto", required = true, paramType = "body", dataType = "deviceDto"))
    @Log("新增设备")
    public ResponseBean
            <Boolean> insertDevice(@RequestBody @Valid DeviceDto deviceDto) {
        return new ResponseBean<>(deviceService.insert(deviceDto) > 0);
    }

    /**
     * 修改设备
     *
     * @param deviceDto
     * @return ResponseBean
     */
    @PutMapping
    @ApiOperation(value = "更新设备信息", notes = "根据设备ID更新职位信息")
    @ApiImplicitParams(@ApiImplicitParam(name = "deviceDto", value = "设备dto", required = true, paramType = "body", dataType = "deviceDto"))
    @Log("修改设备")
    public ResponseBean
            <Boolean> updateDevice(@RequestBody @Valid DeviceDto deviceDto) {
        return new ResponseBean<>(deviceService.update(deviceDto) > 0);
    }


    /**
     * 根据设备ID删除设备信息
     *
     * @param id
     * @return ResponseBean
     */
    @DeleteMapping(value = "/{id}")
    @ApiOperation(value = "删除设备信息", notes = "根据设备ID删除设备信息")
    public ResponseBean
            <Boolean> deleteDeviceById(@PathVariable("id") String id) {
        Device device = new Device();
        device.setId(id);
        device.setNewRecord(false);
        device.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(deviceService.delete(device) > 0);
    }

    /**
     * 批量删除设备信息
     *
     * @param device
     * @return ResponseBean
     */
    @PostMapping("deleteAll")
    @ApiOperation(value = "批量删除设备", notes = "根据设备ID批量删除设备")
    @ApiImplicitParam(name = "device", value = "设备信息", dataType = "device")
    @Log("批量删除设备")
    public ResponseBean<Boolean> deleteAllDevice(@RequestBody Device device) {
        boolean success = false;
        try {
            int count = deviceService.deleteAllDevice(device);
            if (count > 0) {
                success = true;
            }
        } catch (Exception e) {
            log.error("删除设备失败！", e);
        }
        return new ResponseBean<>(success);
    }

    /**
     * 获取上级设备信息
     *
     * @param parentId
     * @return ResponseBean
     */
    @GetMapping(value = "/getSuperiorDevice")
    @ApiOperation(value = "获取上级设备信息", notes = "根据父级ID获取上级设备信息")
    @ApiImplicitParam(name = "parentId", value = "父ID", required = true)
    public ResponseBean<DeviceVo> getSuperiorDevice(@RequestParam("parentId") String parentId) {
        return new ResponseBean<>(deviceService.getSuperiorDevice(parentId));
    }

    /**
     * 获取下级设备信息
     *
     * @param id
     * @return ResponseBean
     */
    @GetMapping(value = "/getSubordinateDevice")
    @ApiOperation(value = "获取下级设备信息", notes = "根据设备ID获取下级设备信息")
    @ApiImplicitParam(name = "id", value = "设备ID", required = true)
    public ResponseBean<List<DeviceVo>> getSubordinateDevice(@RequestParam("id") String id) {
        return new ResponseBean<>(deviceService.getSubordinateDevice(id));
    }

    /**
     * 通过excel导入设备数据
     *
     * @param request
     * @param projectId
     * @param tag  1:智能箱 2:摄像机
     * @return ResponseBean
     */
    @RequestMapping(value = "/importDevice", method = RequestMethod.POST)
    public ResponseBean importDevice(HttpServletRequest request, @RequestParam("projectId") String projectId ,@RequestParam("tag") String tag) {
        ResponseBean responseBean = new ResponseBean();
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            MultipartFile file = entity.getValue();// 获取上传文件对象
            try {
                return deviceService.importDevice(file,projectId,tag);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                responseBean.setData("文件导入失败:" + e.getMessage());

            } finally {
                try {
                    file.getInputStream().close();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
        return responseBean;
    }

    /**
     * 通过excel导入设备数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/importOsDevice", method = RequestMethod.POST)
    public ResponseBean<JSONObject> importOsDevice(HttpServletRequest request, HttpServletResponse response) {
        ResponseBean responseBean = new ResponseBean();
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            MultipartFile file = entity.getValue();// 获取上传文件对象
            try {
                return deviceService.importOsDevice(file);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                responseBean.setData("文件导入失败:" + e.getMessage());

            } finally {
                try {
                    file.getInputStream().close();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
        return responseBean;
    }


    /**
     * 导出设备数据
     *
     * @param deviceDto deviceDto
     */
    @PostMapping("export")
    @ApiOperation(value = "导出设备信息", notes = "导出设备信息")
    @ApiImplicitParam(name = "deviceDto", value = "设备信息", required = true, dataType = "deviceDto")
    @Log(value = "导出设备信息", businessType = BusinessType.EXPORT)
    public ResponseBean<Boolean> exportDevice(@RequestBody DeviceDto deviceDto, HttpServletRequest request, HttpServletResponse response) {
        return deviceService.exportDevice(deviceDto, request, response);
    }

    /**
     * 根据设备id更新上级设备ID为空
     *
     * @param id
     * @return ResponseBean
     */
    @GetMapping(value = "/updateParentDeviceId/{id}")
    @ApiOperation(value = "根据设备id更新上级设备ID为空", notes = "根据设备id更新上级设备ID为空")
    @ApiImplicitParam(name = "id", value = "设备ID", required = true)
    public ResponseBean<Boolean> updateParentDeviceId(@PathVariable("id") String id) {
        boolean success = false;
        try {
            int count = deviceService.updateParentDeviceId(id);
            if (count > 0) {
                success = true;
            }
        } catch (Exception e) {
            log.error("删除设备失败！", e);
        }
        return new ResponseBean<>(success);
    }

    //----------------------- 下面是运营端使用的接口 ------------------------------------------------------------------------

    //根据租户和项目查询设备

    /**
     * 获取下级设备信息
     *
     * @param device
     * @return ResponseBean
     */
    @PostMapping(value = "/os/getDeviceList")
    @ApiOperation(value = "根据租户和项目获取设备", notes = "根据租户和项目获取设备信息")
    @ApiImplicitParams(@ApiImplicitParam(name = "device", value = "设备对象", paramType = "body", required = true, dataType = "json"))
    public ResponseBean<List<Device>> osDevice1(@RequestBody Device device) {

        if (OsVo.isWrongRandomStr(device.getRandomStr())) return OsVo.ERR_RESPONSE_BEAN;

        return new ResponseBean<List<Device>>(deviceService.findList(device));
    }

    /**
     * 根据ID获取设备(专门提供给运营端使用，不ignore原来的接口)
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "运营端根据设备ID查询设备")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "设备ID", paramType = "path", required = true, dataType = "String"))
    @GetMapping(value = "/os/findById/{id}")
    public ResponseBean<DeviceVo> osDevice3(@PathVariable("id") String id, @RequestParam(value = OsVo.CHECK_STRING, required = false, defaultValue = "") String randomStr) {

        if (OsVo.isWrongRandomStr(randomStr)) return OsVo.ERR_RESPONSE_BEAN;

        return findById(id);
    }

    /**
     * 新增设备(专门提供给运营端使用，不ignore原来的接口)
     *
     * @param deviceDto
     * @return ResponseBean
     */
    @PostMapping("/os/test")
    @ApiOperation(value = "测试接口")
    public ResponseBean<Boolean> test(DeviceDto deviceDto) {

        Map<String, String> map = new HashMap<>();
        map.put("a", "123");

        JPushUtils.sendAlias("123", null, map, null, "596078038307966976");


        return new ResponseBean(Boolean.TRUE);
    }

    /**
     * 新增设备(专门提供给运营端使用，不ignore原来的接口)
     *
     * @param deviceDto
     * @return ResponseBean
     */
    @PostMapping("/os/insertDevice")
    @ApiOperation(value = "运营端新增设备")
    @ApiImplicitParams(@ApiImplicitParam(name = "deviceDto", value = "设备dto", required = true, paramType = "body", dataType = "deviceDto"))
    @Log("运营端新增设备")
    public ResponseBean<Boolean> osDevice2(@RequestBody DeviceDto deviceDto) {

        if (OsVo.isWrongRandomStr(deviceDto.getRandomStr())) return OsVo.ERR_RESPONSE_BEAN;

        ResponseBean<Boolean> r;
        try {
            r = insertDevice(deviceDto);
        } catch (Exception e) {
            r = new ResponseBean<>(e);
        }

        return r;
    }

    /**
     * 修改设备(专门提供给运营端使用，不ignore原来的接口)
     *
     * @param deviceDto
     * @return ResponseBean
     */
    @PostMapping("/os/updateDevice")
    @ApiOperation(value = "运营端更新设备信息", notes = "运营端根据设备ID更新职位信息")
    @ApiImplicitParams(@ApiImplicitParam(name = "deviceDto", value = "设备dto", required = true, paramType = "body", dataType = "deviceDto"))
    @Log("运营端修改设备")
    public ResponseBean<Boolean> osDevice4(@RequestBody @Valid DeviceDto deviceDto) {

        if (OsVo.isWrongRandomStr(deviceDto.getRandomStr())) return OsVo.ERR_RESPONSE_BEAN;

        return updateDevice(deviceDto);
    }

    /**
     * 批量删除设备信息(专门提供给运营端使用，不ignore原来的接口)
     *
     * @param device
     * @return ResponseBean
     */
    @PostMapping("/os/deleteAll")
    @ApiOperation(value = "运营端批量删除设备", notes = "运营端根据设备ID批量删除设备")
    @ApiImplicitParam(name = "device", value = "设备信息", dataType = "device")
    @Log("运营端批量删除设备")
    public ResponseBean<Boolean> osDevice5(@RequestBody Device device) {

        if (OsVo.isWrongRandomStr(device.getRandomStr())) return OsVo.ERR_RESPONSE_BEAN;

        return deleteAllDevice(device);
    }

    /**
     * 分页查询设备列表
     *
     * @param pageNum
     * @param pageSize
     * @param sort
     * @param order
     * @param deviceDto
     * @return PageInfo
     */
    @PostMapping("/os/deviceList")
    @ApiOperation(value = "分页查询设备列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "Device", value = "设备信息", dataType = "Device")
    })
    public ResponseBean<PageInfo<DeviceVo>> osDevice6(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                                      @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                                      @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                                      @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
                                                      @RequestParam(value = "regionId", defaultValue = "") String regionId,
                                                      @RequestBody DeviceDto deviceDto) {

        if (OsVo.isWrongRandomStr(deviceDto.getRandomStr())) return OsVo.ERR_RESPONSE_BEAN;
        deviceDto.setRegionId(regionId);
        return deviceList(pageNum, pageSize, sort, order, regionId, CommonConstant.IS_ASOS_TRUE, deviceDto);
    }

    /**
     * 导入设备数据(专门提供给运营端使用，不ignore原来的接口)
     *
     * @param deviceDtos
     * @return
     */
    @PostMapping("/os/import")
    @ApiOperation(value = "运营端导入数据", notes = "运营端导入数据")
    @Log("运营端导入设备")
    public ResponseBean osDevice7(@RequestBody List<DeviceDto> deviceDtos) {

        // 修改导入端传递过来的数据

        try {

            if (CollectionUtils.isEmpty(deviceDtos))
                throw new CommonException("无设备信息数据导入.");

            if (OsVo.isWrongRandomStr(deviceDtos.get(0).getRandomStr())) return OsVo.ERR_RESPONSE_BEAN;
            return new ResponseBean<>(deviceService.importDeviceByDeviceDtoList(deviceDtos,null,null));

        } catch (Exception e) {
            log.error("导入设备数据失败！", e);
            //throw new CommonException("导入设备数据失败！");
            return new ResponseBean<>(e);
        }
    }

    /**
     * 导出设备数据(专门提供给运营端使用，不ignore原来的接口)
     *
     * @param deviceDto deviceDto
     */
    @PostMapping("/os/export")
    @ApiOperation(value = "运营端导出设备信息", notes = "运营端导出设备信息")
    @ApiImplicitParam(name = "deviceDto", value = "设备信息", required = true, dataType = "deviceDto")
    @Log(value = "运营端导出设备信息", businessType = BusinessType.EXPORT)
    public ResponseBean<List<Device>> osDevice8(@RequestBody DeviceDto deviceDto) {

        //获取导出设备，之后到运营端才解析 后面可能需要修改条件

        if (OsVo.isWrongRandomStr(deviceDto.getRandomStr())) return OsVo.ERR_RESPONSE_BEAN;

        try {
            Device device = new Device();
            BeanUtils.copyProperties(deviceDto, device);
            List<Device> devices = deviceService.findList(device);
            if (CollectionUtils.isEmpty(devices)) {
                throw new CommonException("无设备信息数据.");
            }
            return new ResponseBean<List<Device>>(devices);
        } catch (Exception e) {
            return new ResponseBean<List<Device>>(e);
        }
        //return exportDevice(deviceDto,request,response);
    }


    /**
     * 获取上级设备信息(专门提供给运营端使用，不ignore原来的接口)
     *
     * @param parentId
     * @return ResponseBean
     */
    @GetMapping(value = "/os/getSuperiorDevice")
    @ApiOperation(value = "运营端获取上级设备信息", notes = "运营端根据父级ID获取上级设备信息")
    @ApiImplicitParam(name = "parentId", value = "父ID", required = true)
    public ResponseBean<DeviceVo> osDevice9(@RequestParam("parentId") String parentId, @RequestParam(value = OsVo.CHECK_STRING, required = false, defaultValue = "") String randomStr) {

        if (OsVo.isWrongRandomStr(randomStr)) return OsVo.ERR_RESPONSE_BEAN;

        return this.getSuperiorDevice(parentId);
    }

    /**
     * 获取下级设备信息(专门提供给运营端使用，不ignore原来的接口)
     *
     * @param id
     * @return ResponseBean
     */
    @GetMapping(value = "/os/getSubordinateDevice")
    @ApiOperation(value = "运营端获取下级设备信息", notes = "运营端根据设备ID获取下级设备信息")
    @ApiImplicitParam(name = "id", value = "设备ID", required = true)
    public ResponseBean<List<DeviceVo>> osDevice10(@RequestParam("id") String id, @RequestParam(value = OsVo.CHECK_STRING, required = false, defaultValue = "") String randomStr) {

        if (OsVo.isWrongRandomStr(randomStr)) return OsVo.ERR_RESPONSE_BEAN;

        return this.getSubordinateDevice(id);
    }

    /**
     * 根据设备id更新上级设备ID为空
     *
     * @param id
     * @return ResponseBean
     */
    @GetMapping(value = "/os/updateParentDeviceId/{id}")
    @ApiOperation(value = "运营端根据设备id更新上级设备ID为空", notes = "运营端根据设备id更新上级设备ID为空")
    @ApiImplicitParam(name = "id", value = "设备ID", required = true)
    public ResponseBean<Boolean> osDevice11(@PathVariable("id") String id, @RequestParam(value = OsVo.CHECK_STRING, required = false, defaultValue = "") String randomStr) {

        if (OsVo.isWrongRandomStr(randomStr)) return OsVo.ERR_RESPONSE_BEAN;

        return this.updateParentDeviceId(id);
    }

    //------------------------运营端远程调用项目管理档案管理接口-----------------------------------------

    @ApiOperation(value = "分页查询项目表列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "project", value = "项目表信息", dataType = "project")
    })

    @GetMapping("/os/projectList")
    public ResponseBean<PageInfo<Project>> projectList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                                       @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                                       @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                                       @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
                                                       Project project) {
        if (CommonConstant.PAGE_SORT_DEFAULT.equals(sort)) {
            sort = "create_at";
        }
        return new ResponseBean<>(projectService.findPage(PageUtil.pageInfo(pageNum, pageSize, sort, order), project));
    }

    @ApiOperation(value = "新增项目表", notes = "新增项目表")
    @PostMapping("/os/insertProject")
    @Log("新增项目表")
    public ResponseBean<Boolean> insertProject(@RequestBody @Valid ProjectDto projectDto) {
        Project project = new Project();
       // BeanUtils.copyProperties(projectDto, project);
        project.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), projectDto.getTenantCode());
        return new ResponseBean<>(projectService.insert(projectDto) > 0);
    }

    @ApiOperation(value = "更新项目表信息", notes = "根据项目表ID更新项目表信息")
    @Log("修改项目表")
    @PostMapping(value = "/os/updateProject")
    public ResponseBean<Boolean> updateProject(@RequestBody @Valid ProjectDto projectDto) {
        Project project = new Project();
        BeanUtils.copyProperties(projectDto, project);
        project.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), projectDto.getTenantCode(), projectDto.getProjectId());
        return new ResponseBean<>(projectService.update(project) > 0);
    }

    @ApiOperation(value = "项目维护统计")
    @PostMapping(value = "/os/projectStatistics")
    public ResponseBean<List<StatisticsVo>> getProjectMaintainStatistics(@RequestBody DeviceAlarmDto deviceAlarmDto) {
        Stopwatch watch = Stopwatch.createStarted();
        String year = deviceAlarmDto.getYear();
        if (year != null && !"".equals(year)) {
            deviceAlarmDto.setStartTime(year + "-01-01 00:00:00");
            deviceAlarmDto.setEndTime(year + "-12-31 23:59:59");
        }
        List<StatisticsVo> statistics = alarmStatisticsService.getProjectMaintainStatistics(deviceAlarmDto);
        watch.stop();
        log.info("Query projectStatistics Cost: {}ms",
                watch.elapsed(TimeUnit.MILLISECONDS));
        return new ResponseBean<>(statistics);
    }

    @PostMapping(value = "/os/findDeviceModelKind")
    @ApiOperation(value = "获取设备型号,类型,种类")
    public ResponseBean<List<KindVo>> findDeviceModelKind() {
        return new ResponseBean<>(regionService.findDeviceModelKind1());
    }

    @ApiOperation(value = "获取项目设备清单")
    @PostMapping(value = "/os/projectDeviceList")
    public ResponseBean<List<ProjectDeviceVo>> getProjectDeviceList(@RequestBody DeviceAlarmDto deviceAlarmDto) {
        Stopwatch watch = Stopwatch.createStarted();
        List<ProjectDeviceVo> result = projectService.getProjectDeviceList(deviceAlarmDto);
        if (result == null) {
            result = new ArrayList<>();
        }
        watch.stop();
        log.info("Query projectDeviceList Cost: {}ms",
                watch.elapsed(TimeUnit.MILLISECONDS));
        return new ResponseBean<>(result);
    }

    @ApiOperation(value = "查询指定项目的附件表列表")
    @ApiImplicitParams(@ApiImplicitParam(name = "attachment", value = "附件表对象", paramType = "path", required = true, dataType = "attachment"))
    @GetMapping(value = "/os/attachments")
    public ResponseBean
            <List<Attachment>> findAttachments(@RequestParam("projectId") String projectId) {
        Attachment attachment = new Attachment();
        attachment.setProjectId(projectId);
        return new ResponseBean<>(attachmentService.findAllList(attachment));
    }

    @PostMapping(value = "/os/download")
    public ResponseEntity<byte[]> download(@RequestParam("filePath") String filePath) {
        try {
            return fileController.download(filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    @ApiOperation(value = "获取项目设备清单型号信息")
    @GetMapping(value = "/os/modelStatistics")
    public ResponseBean
            <List<ModelStatisticsVo>> findModelStatistics(@RequestParam("projectId") String projectId) {
        return new ResponseBean<>(deviceService.findModelStatistics(projectId));
    }


    @ApiOperation(value = "手机端关联设备")
    @PostMapping(value = "/os/relationDevice")
    public ResponseBean
            <Boolean> relationDevice(@RequestBody DeviceDto dto) {
        return new ResponseBean<>(deviceService.relationDevice(dto) > 0);
    }


    /**
     *返回所有的调试设备列表
     */
    @ApiOperation(value = "返回所有的调试设备列表")
    @GetMapping(value = "debugDeviceList")
    public ResponseBean<List<DeviceVo>> debugDeviceList(DeviceDto deviceDto) {
        return new ResponseBean<>(deviceService.debugDeviceList(deviceDto));
    }

    /**
     * 刷新设备状态
     */
    @ApiOperation(value = "刷新设备数据")
    @GetMapping(value = "flushDevice")
    public ResponseBean<Boolean> flushDevice(@RequestParam("deviceId") String deviceId, @RequestParam("flag") String flag) {
        ResponseBean<Boolean> result = new ResponseBean<>();
        try {
            Device device = deviceService.getDeviceVo(deviceId);
            if (device != null) {
                if (flag == null || "".equals(flag)) {
                    flag = EventTableEnum.SFP.getTableName() + Globals.EventAlarmSeparateConsts.COMMA +
                            EventTableEnum.OUTLET.getTableName() + Globals.EventAlarmSeparateConsts.COMMA +
                            EventTableEnum.ALARM.getTableName() + Globals.EventAlarmSeparateConsts.COMMA +
                            EventTableEnum.CURRENT.getTableName() + Globals.EventAlarmSeparateConsts.COMMA +
                            EventTableEnum.SWITCH.getTableName() + Globals.EventAlarmSeparateConsts.COMMA +
                            EventTableEnum.VOLTAGE.getTableName();
                }
                QueryDeviceDataDto deviceDataDto = new QueryDeviceDataDto();
                deviceDataDto.setDeviceId(deviceId);
                deviceDataDto.setFlag(flag);
                deviceDataDto.setProjectCode(device.getProjectCode());
                deviceDataDto.setTenantCode(device.getTenantCode());
                mqSender.send(MQConstants.DEVICE_STATUS_EXCHANGE,MQConstants.QUERY_DEVICE_DATA, JSONUtil.toJsonStr(deviceDataDto));
                return new ResponseBean<>(true);
            }
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        return new ResponseBean<>(false);
    }
}
