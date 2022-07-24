package com.aswl.as.iface.controller;

import com.alibaba.fastjson.JSON;
import com.aswl.as.common.core.constant.CommonConstant;
import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.web.BaseController;
import com.aswl.as.ibrs.api.dto.DeviceDto;
import com.aswl.as.ibrs.api.dto.DeviceSettingsDto;
import com.aswl.as.ibrs.api.dto.DeviceSettingsListDto;
import com.aswl.as.ibrs.api.feign.RegionServiceClient;
import com.aswl.as.ibrs.api.vo.DeviceStautsOverallVo;
import com.aswl.as.ibrs.api.vo.OpenDeviceDetailsVo;
import com.aswl.as.iface.model.*;
import com.aswl.as.iface.model.consumer.ControlDeviceDto;
import com.aswl.as.iface.model.request.ControlDevice;
import com.aswl.as.iface.model.request.RequestBean;
import com.aswl.as.iface.properties.SocketConfig;
import com.aswl.as.iface.utils.Client;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author aswl
 * @date 2019-09-11
 */
@Slf4j
@AllArgsConstructor
@Api(value = "/api/v1/device", tags = "设备信息")
@RestController
@RequestMapping(value = "/v1/device")
public class DeviceController extends BaseController {

    private final RegionServiceClient regionServiceClient;
    private final SocketConfig socketConfig;

    /**
     * 根据设备id获取状态信息
     *
     * @param id id
     * @return ResponseBean
     */
    @GetMapping("/deviceStatus/{id}")
    @ApiOperation(value = "获取设备状态", notes = "根据设备id获取设备状态")
    @ApiImplicitParam(name = "id", value = "设备ID", required = true, dataType = "String", paramType = "path")
    public ResponseBean<DeviceStautsOverall> deviceStatus(@PathVariable String id) {
        DeviceStautsOverall deviceStautsOverall = new DeviceStautsOverall();
        ResponseBean<DeviceStautsOverallVo> stautsData = regionServiceClient.getDeviceStautsData(id);
        //TODO 处理组数据
        if (stautsData.getData().getDeviceStatusGroup() != null && stautsData.getData().getDeviceStatusGroup().size() > 0) {
            List<DeviceStatusGroup> groups = stautsData.getData().getDeviceStatusGroup().stream()
                    .map(group -> {
                        DeviceStatusGroup statusGroup = new DeviceStatusGroup();
                        statusGroup.setName(group.getName());
                        statusGroup.setStatusGroupName(group.getStatusGroupName());
                        statusGroup.setDeviceStatusVoList(group.getDeviceStatusVoList().stream()
                                // TODO 过滤状态集合
                                .map(vo -> {
                                    DeviceStatus deviceStatus = new DeviceStatus();
                                    deviceStatus.setCode(vo.getCode());
                                    deviceStatus.setCodeCN(vo.getCodeCN());
                                    deviceStatus.setName(vo.getFldName());
                                    deviceStatus.setValue(vo.getValue());
                                    deviceStatus.setUnit(vo.getFldUnit());
                                    deviceStatus.setOperationList(vo.getOperationList() != null ? vo.getOperationList().stream()
                                            // TODO 过滤操作集合
                                            .map(operationVo -> {
                                                StatusOperation statusOperation = new StatusOperation();
                                                BeanUtils.copyProperties(operationVo, statusOperation);
                                                return statusOperation;
                                            }).collect(Collectors.toList()) : null);
                                    return deviceStatus;
                                }).collect(Collectors.toList()));
                        return statusGroup;
                    }).collect(Collectors.toList());
            deviceStautsOverall.setDeviceStatusGroup(groups);
        }
        //TODO 处理非组数据
        if (stautsData.getData().getDeviceStatusNotGroup() != null && stautsData.getData().getDeviceStatusNotGroup().size() > 0) {
            List<DeviceStatus> unGroups = stautsData.getData().getDeviceStatusNotGroup().stream()
                    .map(unGroup -> {
                        DeviceStatus deviceStatus = new DeviceStatus();
                        deviceStatus.setUnit(unGroup.getFldUnit());
                        deviceStatus.setValue(unGroup.getValue());
                        deviceStatus.setName(unGroup.getFldName());
                        deviceStatus.setCodeCN(unGroup.getCodeCN());
                        deviceStatus.setCode(unGroup.getCode());
                        deviceStatus.setOperationList(unGroup.getOperationList() != null ? unGroup.getOperationList().stream()
                                // TODO 过滤操作集合
                                .map(operationVo -> {
                                    StatusOperation statusOperation = new StatusOperation();
                                    statusOperation.setOperCode(operationVo.getOperCode());
                                    statusOperation.setOperName(operationVo.getOperName());
                                    statusOperation.setOperSort(operationVo.getOperSort());
                                    statusOperation.setPortSerial(operationVo.getPortSerial());
                                    statusOperation.setTitle(operationVo.getTitle());
                                    return statusOperation;
                                }).collect(Collectors.toList()) : null);
                        return deviceStatus;
                    }).collect(Collectors.toList());
            deviceStautsOverall.setDeviceStatusNotGroup(unGroups);
        }
        return new ResponseBean<>(deviceStautsOverall);
    }

    /**
     * 根据设备id获取在线状态信息
     *
     * @return ResponseBean
     * @Param id 设备id
     */
    @GetMapping("/onlineStatus/{id}")
    @ApiOperation(value = "获取设备在线状态", notes = "根据设备id获取设备在线状态")
    @ApiImplicitParam(name = "id", value = "设备id", required = true, dataType = "String", paramType = "path")
    public ResponseBean<Map> getOnlineStatusByDeviceCode(@PathVariable("id") String id) {

        return regionServiceClient.getOnlineStatusByDeviceId(id);
    }

    /**
     * 电源设置接口
     *
     * @param
     * @return
     */
    @PostMapping("/powerSetting")
    @ApiOperation(value = "电源设置接口")
    public ResponseBean<Boolean> powerSetting(@RequestBody ControlDeviceDto deviceDto) {

        return operatorCommand(deviceDto);
    }

    /**
     * 温度设置接口
     *
     * @param
     * @return
     */
    @PostMapping("/temperatureSetting")
    @ApiOperation(value = "温度设置接口")
    public ResponseBean<Boolean> temperatureSetting(@RequestBody ControlDeviceDto deviceDto) {
        deviceDto.setOperCode("FanTempValSet");
        return operatorCommand(deviceDto);
    }

    /**
     * 布防设置接口
     *
     * @param
     * @return
     */
    @PostMapping("/odSetting")
    @ApiOperation(value = "布防设置接口")
    public ResponseBean<Boolean> odSetting(@RequestBody SettingDto settingDto) {
        if (settingDto != null) {
            DeviceSettingsListDto dto = new DeviceSettingsListDto();
            //通过设备编码去找设备id
            ResponseBean<Map> status = regionServiceClient.getOnlineStatusByDeviceId(settingDto.getDeviceCode());
            String id = (String) status.getData().get("deviceId");
            dto.setType("DOOR");
            DeviceSettingsDto deviceSettingsDto = new DeviceSettingsDto();
            deviceSettingsDto.setDeviceId(id);
            deviceSettingsDto.setSetType("DOOR");
            deviceSettingsDto.setMode(settingDto.getMode());
            List<DeviceSettingsDto> list = new ArrayList<>();
            list.add(deviceSettingsDto);
            dto.setDeviceSettingsDtoList(list);
            return regionServiceClient.updateBath(dto);
        }
        return new ResponseBean<>(false);
    }

    /**
     * 设备复位
     *
     * @param
     * @return
     */
    @PostMapping("/reset")
    @ApiOperation(value = "设备复位")
    public ResponseBean<Boolean> reset(@RequestBody ControlDeviceDto deviceDto) {

        return operatorCommand(deviceDto);
    }

    /**
     * 分页查询设备列表
     *
     * @param pageNum
     * @param pageSize
     * @param regionCode
     * @param deviceDto
     * @return
     */
    @GetMapping("deviceList")
    @ApiOperation(value = "分页查询设备列表")
    public ResponseBean<PageInfo<LinkedHashMap>> deviceList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                                            @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                                            @RequestParam(value = "regionCode", required = true) String regionCode,
                                                            DeviceDto deviceDto) {

        if (regionCode != null) {
            deviceDto.setRegionCode(regionCode);
        }
        return regionServiceClient.openDeviceList(pageNum, pageSize, deviceDto);
    }

    /**
     * 查询设备详情
     *
     * @param id
     * @return
     */
    @GetMapping("deviceDetails/{id}")
    @ApiOperation(value = "查询设备详情")
    public ResponseBean<OpenDeviceDetailsVo> deviceDetails(@PathVariable("id") String id) {

        return regionServiceClient.findDeviceDetails(id);
    }

    /**
     * 查询区域列表
     *
     * @param regionName
     * @return
     */
    @GetMapping("regionList")
    @ApiOperation(value = "查询区域列表")
    public ResponseBean<List<LinkedHashMap>> regionList(@RequestParam(value = "regionName", required = false) String regionName) {

        return regionServiceClient.findRegionList(regionName);
    }

    //--------------------------------------------------以下为共有方法-------------------------------------------------//
    //抽取websocket
    public void sendMsg(String msg) {
        Client myClient = new Client();
        //默认参数连接到客户端
        try {
            myClient.runStompClient(myClient,
                    socketConfig.getUrl(),
                    "/queue/rspMsg", socketConfig.getSend(), msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            myClient.getTtask().destroy();
            myClient.getSockJsClient().stop();
            myClient.getClient().stop();
            myClient.getSession().disconnect();
        }
    }

    //抽取公用业务逻辑
    public ResponseBean<Boolean> operatorCommand(ControlDeviceDto deviceDto) {
        if (deviceDto != null) {
            RequestBean<ControlDevice> rb = new RequestBean<>();
            ControlDevice device = new ControlDevice();
            ResponseBean<Map> status = regionServiceClient.getOnlineStatusByDeviceId(deviceDto.getDeviceCode());
            String id = (String) status.getData().get("deviceId");
            device.setDeviceId(id);
            device.setOperCode(deviceDto.getOperCode());
            if (deviceDto.getPortSerial() != null) {
                device.setPortSerial(deviceDto.getPortSerial());
            }
            if (deviceDto.getFanTempVal1() != null) {
                device.setFanTempVal1(deviceDto.getFanTempVal1());
            }
            if (deviceDto.getFanTempVal2() != null) {
                device.setFanTempVal2(deviceDto.getFanTempVal2());
            }
            rb.setData(device);
            rb.setEventNo(String.valueOf(System.currentTimeMillis()));
            if (rb.getData() != null) {
                String deviceId = rb.getData().getDeviceId();
                ResponseBean<Map> map = regionServiceClient.getOnlineStatusByDeviceId(deviceId);
                if (map.getCode() == 200 && map.getData() != null) {
                    int networkStatus = (int) map.getData().get("networkStatus");
                    if (networkStatus != 1) {
                        ResponseBean<Boolean> responseBean = new ResponseBean();
                        responseBean.setCode(0);
                        responseBean.setMsg("failure");
                        responseBean.setStatus(0);
                        responseBean.setData(false);
                        return responseBean;
                    }
                    String sendMsg = JSON.toJSONString(rb);
                    sendMsg(sendMsg);
                    ResponseBean<Boolean> responseBean = new ResponseBean();
                    responseBean.setCode(1);
                    responseBean.setMsg("success");
                    responseBean.setStatus(1);
                    responseBean.setData(true);
                    return responseBean;
                }
            }
        }
        ResponseBean<Boolean> responseBean = new ResponseBean();
        responseBean.setCode(0);
        responseBean.setMsg("failure");
        responseBean.setStatus(0);
        responseBean.setData(false);
        return responseBean;
    }
    //----------------------------------------------以上为共有方法-----------------------------------------------------//
    /**
     *
     * @param id  设备id
     * @param position 220v电源端口，从0开始，0表示第一个端口
     * @param onOff 打开或者关闭 open开 close关闭
     * @return
     */
   /* @GetMapping("/switchBoxACPower/{id}/{position}/{onOff}")
    @ApiOperation(value = "开关报障箱220v电源的操作")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "设备id",required = true,dataType = "String",paramType = "path"),
            @ApiImplicitParam(name = "position",value = "220v电源端口",required = true,dataType = "Integer",paramType = "path"),
            @ApiImplicitParam(name = "onOff",value = "打开或者关闭",required = true,dataType = "String",paramType = "path")
    })
    public ResponseBean<Object> switchBoxACPower(@PathVariable String id, @PathVariable Integer position, @PathVariable String onOff){
        WsResultMessage wsResultMessage = iBzptService.switchBoxACPower(initWsBaseParam(), id, position, onOff);
        return new ResponseBean<>(wsResultMessage.getData());
    }*/

    /**
     * 描述：重启设备的操作
     * @param id 设备Id
     * @return
     */
    /*@GetMapping("/resetDevice/{id}")
    @ApiOperation(value = "重启设备的操作", notes = "根据设备id获取设备状态")
    @ApiImplicitParam(name = "id", value = "设备id", required = true, dataType = "String", paramType = "path")
    public ResponseBean<Object> resetDevice(@PathVariable String id) {
        WsBaseParam baseParam = new WsBaseParam();
        WsResultMessage wsResultMessage = iBzptService.resetDevice(baseParam, id);

        return new ResponseBean<>(wsResultMessage);
    }*/
    //初始化基础参数,从token中拿到token,解析获取serviceId,先暂时这样写
    //其实可以过滤器解析token后,将serviceId,放到Request域中,这样这里就不用解析token了
  /*  private WsBaseParam initWsBaseParam(){
        String serviceId = (String) request.getAttribute("serviceId");
        WsBaseParam wsBaseParam = new WsBaseParam();
        SuperPlatform superPlatform = superPlatformService.findByServiceId(serviceId);
        wsBaseParam.setServiceId(serviceId);
        wsBaseParam.setKey(CustomMd5.MD5(serviceId+"_"+superPlatform.getSrcret()+"_"+new SimpleDateFormat("yyyyMMddHHmm").format(new Date())));
        return wsBaseParam;
    }*/
}
