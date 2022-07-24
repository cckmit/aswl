package com.aswl.as.ibrs.controller;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.utils.IdGen;
import com.aswl.as.common.core.utils.SysUtil;
import com.aswl.as.common.core.web.BaseController;
import com.aswl.as.ibrs.api.module.*;
import com.aswl.as.ibrs.api.vo.*;
import com.aswl.as.ibrs.service.*;
import com.aswl.as.ibrs.utils.JsonUtil;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * 设备型号JSON controller
 *
 * @author df
 * @date 2022/06/22 16:09
 */
@Slf4j
@AllArgsConstructor
@Api(value = "/api/v1/deviceModelJson", tags = "设备型号JSON")
@RestController
@RequestMapping("/v1/deviceModelJson")
public class JsonDeviceModelController extends BaseController {
    private final DeviceModelService deviceModelService;
    private final EventUcMetadataService eventUcMetadataService;
    private final EventUcStatusGroupService eventUcStatusGroupService;
    private final DeviceModelAlarmThresholdService deviceModelAlarmThresholdService;
    private  final EventUcMetadataModelService eventUcMetadataModelService;
    private final EventUcMetadataModelOperationService  eventUcMetadataModelOperationService;
    private final EventUcStatusGroupModelService eventUcStatusGroupModelService;

    /* 使⽤Json导出
     * @param response response
     * @param id id
     * @param name name
     */
    @GetMapping(value = "/export")
    public void exportJson(HttpServletResponse response,
                           @RequestParam(name = "id", required = false) String id,
                           @RequestParam(name = "name", required = false) String name) throws Exception {
        // 返回数据
        DeviceModelJsonVo deviceModelJsonVo = new DeviceModelJsonVo();
        //非组扩展状态
        List<ExtendStatusJsonVo> extendStatusJsonVos = new ArrayList<>();
        //组扩展状态
        List<GroupExtendStatusJsonVo> groupExtendStatusJsonVos = new ArrayList<>();
        //事件元数据
        List<EventUcMetadataVo> eventUcMetadataVoList = new ArrayList<>();
        List<EventUcStatusGroupModelVo> eventUcStatusGroupModelVoList = new ArrayList<>();
        ExtendStatusJsonVo extendStatusJsonVo = null;
        GroupExtendStatusJsonVo groupExtendStatusJsonVo = null;
        final DeviceModel model = deviceModelService.get(id);
        if (model != null) {
            eventUcMetadataVoList = eventUcMetadataService.findEventUcMetadataByDeviceModelId(id);
            eventUcStatusGroupModelVoList = eventUcStatusGroupService.getExtendStatusGroup(id);
        }
        for (EventUcMetadataVo vo : eventUcMetadataVoList) {
            DeviceModelAlarmThreshold deviceModelAlarmThreshold = new DeviceModelAlarmThreshold();
            extendStatusJsonVo = new ExtendStatusJsonVo();
            extendStatusJsonVo.setId(vo.getId());
            extendStatusJsonVo.setFldName(vo.getFldName());
            extendStatusJsonVo.setStatusName(vo.getStatusName());
            extendStatusJsonVo.setEventMetadataId(vo.getEventUcMetadataId());
            extendStatusJsonVos.add(extendStatusJsonVo);
            deviceModelAlarmThreshold.setEventMetadataModelId(vo.getId());
            List<DeviceModelAlarmThreshold> allList = deviceModelAlarmThresholdService.findAllList(deviceModelAlarmThreshold);
            List<DeviceModelAlarmThreshold> deviceModelAlarmThresholds = new ArrayList<>();
            for (DeviceModelAlarmThreshold v : allList) {
                DeviceModelAlarmThreshold deviceModelAlarmThreshold1 = new DeviceModelAlarmThreshold();
                deviceModelAlarmThreshold1.setMinValue(v.getMinValue());
                deviceModelAlarmThreshold1.setMaxValue(v.getMaxValue());
                deviceModelAlarmThreshold1.setStatusValue(v.getStatusValue());
                deviceModelAlarmThresholds.add(deviceModelAlarmThreshold1);
            }
            extendStatusJsonVo.setDeviceModelAlarmThresholds(deviceModelAlarmThresholds);

            //状态操作
            List<EventUcStatusOperationVo> extendStatusGroupOperationList = eventUcStatusGroupService.getSelectExtendStatusGroupOperationList(vo.getMetadataModel());
            extendStatusJsonVo.setEventUcStatusOperationVos(extendStatusGroupOperationList);
        }

        // 事件状态组-设备型号
        for (EventUcStatusGroupModelVo vo1 : eventUcStatusGroupModelVoList) {
            groupExtendStatusJsonVo = new GroupExtendStatusJsonVo();
            groupExtendStatusJsonVo.setId(vo1.getId());
            groupExtendStatusJsonVo.setName(vo1.getName());
            groupExtendStatusJsonVo.setPortNum(vo1.getPortNum());
            groupExtendStatusJsonVo.setGroupId(vo1.getGroupId());
            groupExtendStatusJsonVos.add(groupExtendStatusJsonVo);
            
            //根据设备型号ID和状态组ID查询组扩展属性
            eventUcMetadataVoList = eventUcStatusGroupService.getExtendStatusGroupAttribute(id, groupExtendStatusJsonVo.getGroupId());
            List<ExtendStatusJsonVo> extendStatusJsonVos_group = new ArrayList<>();
            for (EventUcMetadataVo vo : eventUcMetadataVoList) {
                extendStatusJsonVo = new ExtendStatusJsonVo();
                extendStatusJsonVo.setId(vo.getId());
                extendStatusJsonVo.setFldName(vo.getFldName());
                extendStatusJsonVo.setStatusName(vo.getStatusName());
                extendStatusJsonVo.setEventMetadataId(vo.getEventUcMetadataId());
                extendStatusJsonVo.setPortSerial(vo.getPortSerial());
                extendStatusJsonVos_group.add(extendStatusJsonVo);
                
                // 获取报警区间
                DeviceModelAlarmThreshold deviceModelAlarmThreshold = new DeviceModelAlarmThreshold();
                deviceModelAlarmThreshold.setEventMetadataModelId(vo.getMetadataModel());
                List<DeviceModelAlarmThreshold> allList = deviceModelAlarmThresholdService.findAllList(deviceModelAlarmThreshold);
                List<DeviceModelAlarmThreshold> deviceModelAlarmThresholds = new ArrayList<>();
                for (DeviceModelAlarmThreshold v : allList) {
                    DeviceModelAlarmThreshold deviceModelAlarmThreshold1 = new DeviceModelAlarmThreshold();
                    deviceModelAlarmThreshold1.setMinValue(v.getMinValue());
                    deviceModelAlarmThreshold1.setMaxValue(v.getMaxValue());
                    deviceModelAlarmThreshold1.setStatusValue(v.getStatusValue());
                    deviceModelAlarmThresholds.add(deviceModelAlarmThreshold1);
                }
                extendStatusJsonVo.setDeviceModelAlarmThresholds(deviceModelAlarmThresholds);
                //状态操作
                List<EventUcStatusOperationVo> extendStatusGroupOperationList = eventUcStatusGroupService.getSelectExtendStatusGroupOperationList(vo.getMetadataModel());
                extendStatusJsonVo.setEventUcStatusOperationVos(extendStatusGroupOperationList);
            }
            groupExtendStatusJsonVo.setExtendStatusJsonVos(extendStatusJsonVos_group);
        }

        //导出
        deviceModelJsonVo.setDeviceModel(model);
        deviceModelJsonVo.setExtendStatusJsonVos(extendStatusJsonVos);
        deviceModelJsonVo.setGroupExtendStatusJsonVos(groupExtendStatusJsonVos);
        JsonUtil.exportJson(response, deviceModelJsonVo, name + " -" + UUID.randomUUID() + ".json");
    }


    /* 使⽤Json导⼊
     * @param multipartFile multipartFile
     * @return Object
     */
    @PostMapping(value = "/import")
    public ResponseBean<cn.hutool.json.JSONObject> importJson(HttpServletRequest request) {
        ResponseBean responseBean = new ResponseBean();
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            try{
            MultipartFile file = entity.getValue();// 获取上传文件对象
            Map<String, String> result = JsonUtil.readJson(file);
            if ("200".equals(result.get("code"))) {
                String jsonString = result.get("result");
                JSONObject jsonObject = JSONObject.parseObject(jsonString);
                String jsonStr = jsonObject.get("deviceModel").toString();
                DeviceModel model = com.alibaba.fastjson.JSON.parseObject(jsonStr, DeviceModel.class);
                //根据设备型号名称查询型号ID
                DeviceModel deviceModel_db = deviceModelService.findByDeviceModelName(model.getDeviceModelName());
                //根据型号ID查询事件元数据-设备型号表数据
                if (deviceModel_db != null){
                List<EventUcMetadataModel> list = eventUcMetadataModelService.findByUcMetadataByModelId(deviceModel_db.getId());
                    for (EventUcMetadataModel metadataModel : list) {
                        //删除设备型号事件元数据-状态操作
                        eventUcMetadataModelOperationService.deleteByEventMetadataModelId(metadataModel.getId());
                        //删除设备型号区间报警数据
                        deviceModelAlarmThresholdService.deleteByEventMetadataModelId(metadataModel.getId());
                    }
                    //删除事件元数据-设备型号
                    eventUcMetadataModelService.deleteByDeviceModelId(deviceModel_db.getId());

                    //删除事件状态组-设备型号
                    eventUcStatusGroupModelService.deleteByDeviceModelId(deviceModel_db.getId());

                    // 通过型号ID删除型号表数据
                    DeviceModel deviceModel = new DeviceModel();
                    deviceModel.setId(deviceModel_db.getId());
                    deviceModelService.delete(deviceModel);
                }
               
                // 保存设备型号
                deviceModelService.insert(model);


                // 获取非组扩展状态
                JSONArray extendStatusJsonArray = jsonObject.getJSONArray("extendStatusJsonVos");
                for (int i = 0; i < extendStatusJsonArray.size(); i++) {
                    //将获取的单个json字符串翻译成JSONObject
                    JSONObject extendStatusJsonObject = JSONObject.parseObject(extendStatusJsonArray.get(i).toString());
                    //将json对象翻译成Student对象
                    ExtendStatusJsonVo extendStatusJsonVo = (ExtendStatusJsonVo) JSONObject.toJavaObject(extendStatusJsonObject, ExtendStatusJsonVo.class);
                    //保存非组扩展状态
                    EventUcMetadataModel eventUcMetadataModel = new EventUcMetadataModel();
                    eventUcMetadataModel.setId(IdGen.snowflakeId());
                    eventUcMetadataModel.setDeviceModelId(model.getId());
                    eventUcMetadataModel.setEventMetadataId(extendStatusJsonVo.getEventMetadataId());
                    eventUcMetadataModel.setCreateDate(new Date());
                    eventUcMetadataModelService.insert(eventUcMetadataModel);

                    // 获取报警区间值
                    JSONArray deviceModelAlarmThresholdsJsonArray = extendStatusJsonObject.getJSONArray("deviceModelAlarmThresholds");
                    if (!deviceModelAlarmThresholdsJsonArray.isEmpty() || deviceModelAlarmThresholdsJsonArray.size() >= 1) {
                        for (int j = 0; j <deviceModelAlarmThresholdsJsonArray.size(); j++) {
                            //将获取的单个json字符串翻译成JSONObject
                            JSONObject deviceModelAlarmThresholdsJsonObject = JSONObject.parseObject(deviceModelAlarmThresholdsJsonArray.get(j).toString());
                            //将json对象翻译成DeviceModelAlarmThreshold对象
                            DeviceModelAlarmThreshold deviceModelAlarmThreshold =  JSONObject.toJavaObject(deviceModelAlarmThresholdsJsonObject, DeviceModelAlarmThreshold.class);
                            //保存报警区间
                            deviceModelAlarmThreshold.setId(IdGen.snowflakeId());
                            deviceModelAlarmThreshold.setEventMetadataModelId(eventUcMetadataModel.getId());
                            deviceModelAlarmThresholdService.insert(deviceModelAlarmThreshold);
                        }
                    }
                    // 获取状态操作
                    JSONArray eventUcStatusOperationJsonArray = extendStatusJsonObject.getJSONArray("eventUcStatusOperationVos");
                    if (!eventUcStatusOperationJsonArray.isEmpty() || eventUcStatusOperationJsonArray.size() >= 1) {
                        for (int j = 0; j < eventUcStatusOperationJsonArray.size(); j++) {
                            //将获取的单个json字符串翻译成JSONObject
                            JSONObject eventUcStatusOperationJsonObject = JSONObject.parseObject(eventUcStatusOperationJsonArray.get(j).toString());
                            //将json对象翻译成DeviceModelAlarmThreshold对象
                            EventUcStatusOperationVo eventUcStatusOperationVo = (EventUcStatusOperationVo) JSONObject.toJavaObject(eventUcStatusOperationJsonObject, EventUcStatusOperationVo.class);
                            //保存状态操作
                            EventUcMetadataModelOperation eventUcMetadataModelOperation = new EventUcMetadataModelOperation();
                            eventUcMetadataModelOperation.setId(IdGen.snowflakeId());
                            eventUcMetadataModelOperation.setEventMetadataModelId(eventUcMetadataModel.getId());
                            eventUcMetadataModelOperation.setStatusOperationId(eventUcStatusOperationVo.getStatusOperationId());
                            eventUcMetadataModelOperationService.insert(eventUcMetadataModelOperation);
                        }
                    }
                }
                // 获取组扩展状态

                JSONArray groupExtendStatusJsonArray = jsonObject.getJSONArray("groupExtendStatusJsonVos");
                for (int i = 0; i < groupExtendStatusJsonArray.size(); i++) {
                    JSONObject groupExtendStatusJsonObject = JSONObject.parseObject(groupExtendStatusJsonArray.get(i).toString());
                    GroupExtendStatusJsonVo groupExtendStatusJsonVo = JSONObject.toJavaObject(groupExtendStatusJsonObject, GroupExtendStatusJsonVo.class);
                    //保存组扩展状态
                    EventUcStatusGroupModel eventUcStatusGroupModel = new EventUcStatusGroupModel();
                    eventUcStatusGroupModel.setId(IdGen.snowflakeId());
                    eventUcStatusGroupModel.setEventStatusGroupId(groupExtendStatusJsonVo.getGroupId());
                    eventUcStatusGroupModel.setDeviceModelId(model.getId());
                    eventUcStatusGroupModel.setPortNum(groupExtendStatusJsonVo.getPortNum());
                    eventUcStatusGroupModel.setCreateDate(new Date());
                    eventUcStatusGroupModelService.insert(eventUcStatusGroupModel);

                    JSONArray jsonArray = groupExtendStatusJsonObject.getJSONArray("extendStatusJsonVos");
                    for (int j = 0; j < jsonArray.size(); j++) {
                        //将获取的单个json字符串翻译成JSONObject
                        JSONObject extendStatusJsonObject = JSONObject.parseObject(jsonArray.get(j).toString());
                        //将json对象翻译成Student对象
                        ExtendStatusJsonVo extendStatusJsonVo = JSONObject.toJavaObject(extendStatusJsonObject, ExtendStatusJsonVo.class);
                        //保存组扩展状态
                        EventUcMetadataModel eventUcMetadataModel = new EventUcMetadataModel();
                        eventUcMetadataModel.setId(IdGen.snowflakeId());
                        eventUcMetadataModel.setDeviceModelId(model.getId());
                        eventUcMetadataModel.setEventMetadataId(extendStatusJsonVo.getEventMetadataId());
                        eventUcMetadataModel.setEventStatusGroupModelId(eventUcStatusGroupModel.getId());
                        eventUcMetadataModel.setPortSerial(extendStatusJsonVo.getPortSerial());
                        eventUcMetadataModel.setCreateDate(new Date());
                        eventUcMetadataModelService.insert(eventUcMetadataModel);

                        // 获取报警区间值
                        JSONArray deviceModelAlarmThresholdsJsonArray = extendStatusJsonObject.getJSONArray("deviceModelAlarmThresholds");
                        if (!deviceModelAlarmThresholdsJsonArray.isEmpty() || deviceModelAlarmThresholdsJsonArray.size() >= 1) {
                            for (int k = 0; k < deviceModelAlarmThresholdsJsonArray.size(); k++) {
                                //将获取的单个json字符串翻译成JSONObject
                                JSONObject deviceModelAlarmThresholdsJsonObject = JSONObject.parseObject(deviceModelAlarmThresholdsJsonArray.get(k).toString());
                                //将json对象翻译成DeviceModelAlarmThreshold对象
                                DeviceModelAlarmThreshold deviceModelAlarmThreshold =  JSONObject.toJavaObject(deviceModelAlarmThresholdsJsonObject, DeviceModelAlarmThreshold.class);
                                //保存报警区间

                                deviceModelAlarmThreshold.setId(IdGen.snowflakeId());
                                deviceModelAlarmThreshold.setEventMetadataModelId(eventUcMetadataModel.getId());
                                deviceModelAlarmThresholdService.insert(deviceModelAlarmThreshold);
                            }
                        }
                        // 获取状态操作
                        JSONArray eventUcStatusOperationJsonArray = extendStatusJsonObject.getJSONArray("eventUcStatusOperationVos");
                        if (!eventUcStatusOperationJsonArray.isEmpty() || eventUcStatusOperationJsonArray.size() >= 1) {
                            for (int u = 0; u < eventUcStatusOperationJsonArray.size(); u++) {
                                //将获取的单个json字符串翻译成JSONObject
                                JSONObject eventUcStatusOperationJsonObject = JSONObject.parseObject(eventUcStatusOperationJsonArray.get(u).toString());
                                //将json对象翻译成DeviceModelAlarmThreshold对象
                                EventUcStatusOperationVo eventUcStatusOperationVo = JSONObject.toJavaObject(eventUcStatusOperationJsonObject, EventUcStatusOperationVo.class);
                                //保存状态操作
                                EventUcMetadataModelOperation eventUcMetadataModelOperation = new EventUcMetadataModelOperation();
                                eventUcMetadataModelOperation.setId(IdGen.snowflakeId());
                                eventUcMetadataModelOperation.setEventMetadataModelId(eventUcMetadataModel.getId());
                                eventUcMetadataModelOperation.setStatusOperationId(eventUcStatusOperationVo.getStatusOperationId());
                                eventUcMetadataModelOperationService.insert(eventUcMetadataModelOperation);
                            }
                        }
                    }
                }
            }
        }catch (Exception e){
                log.error(e.getMessage(), e);
                responseBean.setData("文件导入失败:" + e.getMessage());
            }
        }
    return responseBean;
}
    
}
