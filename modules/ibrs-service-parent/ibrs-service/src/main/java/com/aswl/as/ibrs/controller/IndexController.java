package com.aswl.as.ibrs.controller;

import com.aswl.as.common.core.constant.CommonConstant;
import com.aswl.as.common.core.exceptions.CommonException;
import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.utils.DateUtils;
import com.aswl.as.common.core.vo.OsVo;
import com.aswl.as.common.core.web.BaseController;
import com.aswl.as.ibrs.api.dto.DeviceAlarmDto;
import com.aswl.as.ibrs.api.dto.DeviceDto;
import com.aswl.as.ibrs.api.module.Device;
import com.aswl.as.ibrs.api.module.DynamicMessage;
import com.aswl.as.ibrs.api.vo.AlarmTypeStatisticsVo;
import com.aswl.as.ibrs.api.vo.DeviceVo;
import com.aswl.as.ibrs.api.vo.RegionDeviceListVo;
import com.aswl.as.ibrs.service.AlarmStatisticsService;
import com.aswl.as.ibrs.service.DeviceEventAlarmService;
import com.aswl.as.ibrs.service.DeviceService;
import com.aswl.as.ibrs.service.RegionService;
import com.aswl.as.ibrs.task.DebugDeviceTask;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author dingfei
 * @date 2019-10-17 11:02
 */
@Slf4j
@AllArgsConstructor
@Api(value = "/api/v1/index", tags = "首页")
@RestController
@RequestMapping("/v1/index")
public class IndexController extends BaseController {

    private final DeviceService deviceService;
    private final AlarmStatisticsService statisticsService;
    private final DeviceEventAlarmService eventAlarmService;
    private  final RegionService regionService;
    @Autowired
    @Qualifier(value = "redisTemplateJson")
    private RedisTemplate redisTemplate;
    @Autowired
    private ThreadPoolTaskScheduler scheduler;


    /**
     * 首页设备故障趋势统计
     *
     * @param queryType  day 近7天、week 近10周、month 近12个月
     * @param deviceKind 设备类型 1:光纤收发器 2:传输箱 3:摄像头
     * @return ResponseBean
     */
    @ApiOperation(value = "首页设备故障趋势统计")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "queryType", value = "日期类型(day 近7天、week 近10周、month 近12个月)", defaultValue = "mongth", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "deviceKind", value = "设备类型", defaultValue = "1", paramType = "query", dataType = "String")
    })
    @GetMapping(value = "queryAlarmCount")
    public ResponseBean<Object> getDeviceAlarmCount(@RequestParam(value = "queryType", defaultValue = "month") String queryType, @RequestParam(value = "deviceKind", defaultValue = "1") int deviceKind, @Param("deviceId") String deviceId) {
        List<Object> resultList = null;
        int week = 7; //周天数
        int month = 30;//月天数
        int year = 12; //年(12个月)

        if ("week".equals(queryType)) {
            resultList = statisticsService.queryByDate(week, queryType, deviceId, deviceKind, "%Y-%m-%d", CommonConstant.IS_ASOS_FALSE);
        } else if ("month".equals(queryType)) {
            resultList = statisticsService.queryByDate(month, queryType, deviceId, deviceKind, "%Y-%m-%d", CommonConstant.IS_ASOS_FALSE);
        } else {
            resultList = statisticsService.queryByDate(year, queryType, deviceId, deviceKind, "%Y-%m", CommonConstant.IS_ASOS_FALSE);
        }
        return new ResponseBean<>(resultList);
    }

    /**
     * 根据经纬度查询区域设备信息
     *
     * @param longMin  经度最小值
     * @param longMax  经度最大值
     * @param latMin   维度最小值
     * @param latMax   维度最大值
     * @param devType  设备类型
     * @param query 关键字
     * @return
     */

    @ApiOperation(value = "根据经纬度查询设备信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "longMin", value = "经度最小值", paramType = "query", dataType = "double"),
            @ApiImplicitParam(name = "longMax", value = "经度最大值", paramType = "query", dataType = "double"),
            @ApiImplicitParam(name = "latMin", value = "维度最小值", paramType = "query", dataType = "double"),
            @ApiImplicitParam(name = "latMax", value = "维度最大值", paramType = "query", dataType = "double"),
            @ApiImplicitParam(name = "devType", value = "设备类型", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "query", value = "关键字", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "alarmLevels", value = "告警标识", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "offlineFlag", value = "离线标识", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "kind", value = "设备种类(2,报障箱,3,摄像机)", paramType = "query", dataType = "String")
    })
    @GetMapping(value = "getDeviceLocation")
    public ResponseBean<List<Object>> getDeviceLocation( @RequestParam(value = "regionCode",required = false) String regionCode, @RequestParam("longMin") double longMin, @RequestParam("longMax") double longMax,
                                                          @RequestParam("latMin") double latMin, @RequestParam("latMax") double latMax,
                                                          @RequestParam(value = "devType",required = false) String devType, @RequestParam(value = "query",required = false) String query,
                                                          @RequestParam(value = "alarmLevels",required = false) String[] alarmLevels, @RequestParam(value = "offlineFlag",required = false) String offlineFlag,
                                                          @RequestParam(value = "queryDebug",required = false) String queryDebug,
                                                          @RequestParam(value = "kind",required = false) String kind,
                                                           @RequestParam(value = "queryProjectId",required = false) String queryProjectId,
                                                           @RequestParam(value = "loadType",required = false) String loadType
                                                           )
    {
        return new ResponseBean<>(deviceService.getDevicesByLonLat(regionCode,longMin, longMax, latMin, latMax, devType, query,alarmLevels,queryDebug,offlineFlag,kind,queryProjectId, CommonConstant.IS_ASOS_FALSE,loadType));
    }

    /**
     * 首页告警分级统计
     *
     * @return ResponseBean
     */
    @ApiOperation(value = "首页告警分级统计")
    @GetMapping(value = "getAlarmLevel")
    public ResponseBean<Map> getAlarmLevelCounts() {
        return new ResponseBean<>(eventAlarmService.getAlarmLevelCounts(CommonConstant.IS_ASOS_FALSE));
    }

    /**
     * 首页告警分类统计
     *
     * @return ResponseBean
     */
    @ApiOperation(value = "首页告警分类统计")
    @GetMapping(value = "getAlarmTypes")
    public ResponseBean<List<Map>> getAlarmTypesCount() {
        return new ResponseBean<>(eventAlarmService.getAlarmTypesCount(CommonConstant.IS_ASOS_FALSE));
    }


    /**
     * 首页总体健康率统计
     *
     * @return ResponseBean
     */
    @ApiOperation(value = "首页总体健康率统计")
    @GetMapping(value = "getHealthy")
    public ResponseBean<Map> getHealthyData() {
        return new ResponseBean<>(eventAlarmService.getHealthyData(CommonConstant.IS_ASOS_FALSE,"","", null));
    }


    /**
     * 地图上设备数统计
     *
     * @return
     */
    @ApiOperation(value = "地图上设备数统计")
    @GetMapping(value = "queryMap")
    public ResponseBean<Map> getMapDeviceData() {
        return new ResponseBean<>(statisticsService.getMapDeviceData(CommonConstant.IS_ASOS_FALSE));
    }


    /**
     * 首页报障箱和摄像头统计
     *
     * @return
     */
    @ApiOperation(value = "首页报障箱和摄像头统计(2报障箱 3摄像机)")
    @GetMapping(value = "queryOnLine")
    public ResponseBean<Map> getOnLineDeviceData(@RequestParam(value = "type", defaultValue = "2") String type) {

        return new ResponseBean<>(statisticsService.getOnLineDeviceData(type, CommonConstant.IS_ASOS_FALSE));
    }


    /**
     * 首页近30天告警数据TOP5
     *
     * @return
     */
    @ApiOperation(value = "首页近30天告警数据TOP5")
    @GetMapping(value = "historyTop5")
    public ResponseBean<List<AlarmTypeStatisticsVo>> getHistoryTop5(DeviceAlarmDto deviceAlarmDto) {

        //历史记录就是以当前时间往后推7天
        Date date = new Date();//取时间
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date); //需要将date数据转移到Calender对象中操作
        if (deviceAlarmDto.getStartTime() == null) {
            calendar.add(calendar.DATE, -(deviceAlarmDto.getDay() - 1));//把日期往后增加n天.正数往后推,负数往前移动
        }
        date = calendar.getTime();   //这个时间就是日期往后推一天的结果
        if (deviceAlarmDto.getStartTime() == null || "".equals(deviceAlarmDto.getStartTime())) {
            deviceAlarmDto.setStartTime(DateUtils.asLocalDateTime(date).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            deviceAlarmDto.setEndTime(DateUtils.asLocalDateTime(new Date()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        }
        return new ResponseBean<>(statisticsService.getHistoryTop5(deviceAlarmDto));
    }

    // 暴露接口提供给 运营端调用


    //-----------------------------------------------------------------------------------------------
    @ApiOperation(value = "首页设备故障趋势统计")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "queryType", value = "日期类型(day 近7天、week 近10周、month 近12个月)", defaultValue = "mongth", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "deviceKind", value = "设备类型", defaultValue = "1", paramType = "query", dataType = "String")
    })
    @GetMapping(value = "/os/getDeviceBreakDown")
    public ResponseBean<Object> osIndex1(@RequestParam(value = "queryType", defaultValue = "month") String queryType, @RequestParam(value = "deviceKind", defaultValue = "1") Integer deviceKind,
                                         @RequestParam(value = "deviceId", required = false) String deviceId, @RequestParam(value = OsVo.CHECK_STRING, required = false, defaultValue = "") String randomStr) {

        if (OsVo.isWrongRandomStr(randomStr)) return OsVo.ERR_RESPONSE_BEAN;

        List<Object> resultList = null;
        int week = 7; //周
        int month = 30;//月
        int year = 12; //年

        if ("week".equals(queryType)) {
            resultList = statisticsService.queryByDate(week, queryType, deviceId, deviceKind, "%Y-%m-%d", CommonConstant.IS_ASOS_TRUE);
        } else if ("month".equals(queryType)) {
            resultList = statisticsService.queryByDate(month, queryType, deviceId, deviceKind, "%Y-%m-%d", CommonConstant.IS_ASOS_TRUE);
        } else {
            resultList = statisticsService.queryByDate(year, queryType, deviceId, deviceKind, "%Y-%m", CommonConstant.IS_ASOS_TRUE);
        }
        return new ResponseBean<>(resultList);
    }

    /**
     * 根据经纬度查询区域设备信息
     *
     * @param longMin  经度最小值
     * @param longMax  经度最大值
     * @param latMin   维度最小值
     * @param latMax   维度最大值
     * @param devType  设备类型
     * @param keyWords 关键字
     * @return
     */

    @ApiOperation(value = "根据经纬度查询设备信息（专门提供给给运营端调用）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "longMin", value = "经度最小值", paramType = "query", dataType = "double"),
            @ApiImplicitParam(name = "longMax", value = "经度最大值", paramType = "query", dataType = "double"),
            @ApiImplicitParam(name = "latMin", value = "维度最小值", paramType = "query", dataType = "double"),
            @ApiImplicitParam(name = "latMax", value = "维度最大值", paramType = "query", dataType = "double"),
            @ApiImplicitParam(name = "devType", value = "设备类型", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "keyWords", value = "关键字", paramType = "query", dataType = "String")
    })
    @GetMapping(value = "/os/getDeviceLocation")
    public ResponseBean<List<Object>> osIndex2(@RequestParam("longMin") double longMin, @RequestParam("longMax") double longMax,
                                                 @RequestParam("latMin") double latMin, @RequestParam("latMax") double latMax,
                                                 @RequestParam("devType") String devType, @RequestParam("keyWords") String keyWords,
                                                 @RequestParam(value = OsVo.CHECK_STRING, required = false, defaultValue = "") String randomStr) {

        if (OsVo.isWrongRandomStr(randomStr)) return OsVo.ERR_RESPONSE_BEAN;

        return new ResponseBean<>(deviceService.getDevicesByLonLat(null,longMin, longMax, latMin, latMax, devType, keyWords,null,null,null,null,null,CommonConstant.IS_ASOS_TRUE,null));
    }

    /**
     * 首页告警分级统计
     *
     * @return ResponseBean
     */
    @ApiOperation(value = "首页告警分级统计（专门提供给给运营端调用）")
    @GetMapping(value = "/os/getAlarmLevel")
    public ResponseBean<Map> osIndex3(@RequestParam(value = OsVo.CHECK_STRING, required = false, defaultValue = "") String randomStr) {

        if (OsVo.isWrongRandomStr(randomStr)) return OsVo.ERR_RESPONSE_BEAN;

        return new ResponseBean<>(eventAlarmService.getAlarmLevelCounts(CommonConstant.IS_ASOS_TRUE));
    }

    /**
     * 首页告警分类统计
     *
     * @return ResponseBean
     */
    @ApiOperation(value = "首页告警分类统计（专门提供给给运营端调用）")
    @GetMapping(value = "/os/getAlarmTypes")
    public ResponseBean<List<Map>> osIndex4(@RequestParam(value = OsVo.CHECK_STRING, required = false, defaultValue = "") String randomStr) {

        if (OsVo.isWrongRandomStr(randomStr)) return OsVo.ERR_RESPONSE_BEAN;

        return new ResponseBean<>(eventAlarmService.getAlarmTypesCount(CommonConstant.IS_ASOS_TRUE));
    }

    /**
     * 首页总体健康率统计
     *
     * @return ResponseBean
     */
    @ApiOperation(value = "首页总体健康率统计（专门提供给给运营端调用）")
    @GetMapping(value = "/os/getHealthy")
    public ResponseBean<Integer> osIndex5(@RequestParam(value = OsVo.CHECK_STRING, required = false, defaultValue = "") String randomStr) {

        if (OsVo.isWrongRandomStr(randomStr)) return OsVo.ERR_RESPONSE_BEAN;

        return new ResponseBean<>(eventAlarmService.getHealthyDataOs(CommonConstant.IS_ASOS_TRUE));
    }

    /**
     * 地图上设备数统计
     *
     * @return
     */
    @ApiOperation(value = "地图上设备数统计（专门提供给给运营端调用）")
    @GetMapping(value = "/os/queryMap")
    public ResponseBean<Map> osIndex6(@RequestParam(value = OsVo.CHECK_STRING, required = false, defaultValue = "") String randomStr) {

        if (OsVo.isWrongRandomStr(randomStr)) return OsVo.ERR_RESPONSE_BEAN;

        return new ResponseBean<>(statisticsService.getMapDeviceData(CommonConstant.IS_ASOS_TRUE));
    }

    /**
     * 首页报障箱和摄像头统计
     *
     * @return
     */
    @ApiOperation(value = "首页报障箱和摄像头统计(2报障箱 3摄像机)（专门提供给给运营端调用）")
    @GetMapping(value = "/os/queryOnLine")
    public ResponseBean<Map> osIndex7(@RequestParam(value = "type", defaultValue = "2") String type, @RequestParam(value = OsVo.CHECK_STRING, required = false, defaultValue = "") String randomStr) {

        if (OsVo.isWrongRandomStr(randomStr)) return OsVo.ERR_RESPONSE_BEAN;

        return new ResponseBean<>(statisticsService.getOnLineDeviceData(type, CommonConstant.IS_ASOS_TRUE));
    }

    /**
     * 首页设备在线数
     *
     * @param type
     * @return
     */
    @ApiOperation(value = "首页设备在线数")
    @GetMapping(value = "indexOnlineCount")
    public ResponseBean<Map> indexOnlineCount(@RequestParam(value = "type", defaultValue = "2") String type,@RequestParam(value = "queryProjectId",required = false) String queryProjectId,
                                              @RequestParam(value = "regionCode", required = false) String regionCode) {
        return new ResponseBean<>(statisticsService.indexOnlineCount(type, queryProjectId, regionCode, CommonConstant.IS_ASOS_FALSE));
    }


    /**
     * APP首页设备统计
     *
     * @return
     */
    @ApiOperation(value = "APP首页设备统计")
    @GetMapping(value = "app/queryMap")
    public ResponseBean<Map> getAppMapDeviceData() {
        return new ResponseBean<>(statisticsService.getMapDeviceData(CommonConstant.IS_ASOS_FALSE));
    }

    /**
     * APP首页各区智能箱/摄像机在线数完好数和数量
     *
     * @return
     */
    @ApiOperation(value = "APP首页各区智能箱/摄像机在线数完好数和数量")
    @GetMapping(value = "app/getRegionList")
    public ResponseBean<List<RegionDeviceListVo>> getAppOnlineAndIntactList(@RequestParam(value = "type", defaultValue = "2") String type) {

        return new ResponseBean<>(regionService.getAppOnlineAndIntactList(type));
    }

    /**
     * APP首页报障箱/摄像头在线率
     *
     * @param type
     * @return
     */
    @ApiOperation(value = "APP首页报障箱/摄像头在线率")
    @GetMapping(value = "app/onlineCount")
    public ResponseBean<Map> appIndexOnlineCount(@RequestParam(value = "type", defaultValue = "2") String type,@RequestParam("queryProjectId") String queryProjectId,
                                                 @RequestParam(value = "regionCode", required = false) String regionCode) {
        return new ResponseBean<>(statisticsService.indexOnlineCount(type,queryProjectId, regionCode, CommonConstant.IS_ASOS_FALSE));
    }


    /**
     * APP首页报障箱/摄像头完好率
     *
     * @param type
     * @return
     */
    @ApiOperation(value = "APP首页报障箱/摄像头完好率")
    @GetMapping(value = "app/intactCount")
    public ResponseBean<Map> appIndexIntactCount(@RequestParam(value = "type", defaultValue = "2") String type,@RequestParam("queryProjectId") String queryProjectId,
                                                 @RequestParam(value = "regionCode", required = false) String regionCode) {

       return new  ResponseBean<>(eventAlarmService.getHealthyData(CommonConstant.IS_ASOS_FALSE, type, queryProjectId, regionCode));


    }

    /**
     * 标记调试设备,设备动态移到调试选项中
     */
    @PutMapping("setDebugMode")
    @ApiOperation("设置调试模式")
    public ResponseBean<Boolean> setDebugMode(@RequestBody DeviceDto deviceDto){
        String deviceId = deviceDto.getId();
        Integer day = deviceDto.getDebugDuration();
        Device device = new Device();
        device.setId(deviceId);
        Device dbDevice = deviceService.get(device);
        if(dbDevice != null){
            //删除故障预警内该设备的动态,移到调试key中
            List<DynamicMessage> debug = new ArrayList<>();
            BoundListOperations faultListOps = redisTemplate.boundListOps("fault");
            if (faultListOps != null && faultListOps.size()>0) {
                for (int i = 0; i < faultListOps.size(); i++) {
                    DynamicMessage dynamicMessage = (DynamicMessage) faultListOps.index(i);
                    if(deviceId.equals(dynamicMessage.getDeviceId())){
                        debug.add(dynamicMessage);
                    }
                }
            }
            BoundListOperations warnListOps = redisTemplate.boundListOps("warn");
            if (warnListOps != null && warnListOps.size()>0) {
                for (int i = 0; i < warnListOps.size(); i++) {
                    DynamicMessage dynamicMessage = (DynamicMessage) warnListOps.index(i);
                    if(deviceId.equals(dynamicMessage.getDeviceId())){
                        debug.add(dynamicMessage);
                    }
                }
            }
            if(debug.size()>0){
                for (DynamicMessage message : debug) {
                    faultListOps.remove(0,message);
                    warnListOps.remove(0,message);
                    redisTemplate.boundListOps("debugging").leftPush(message);
                }
            }
            dbDevice.setDebug(1);
            dbDevice.setDebugDuration(day);
            Calendar instance = Calendar.getInstance();
            instance.add(Calendar.DAY_OF_MONTH,day);
            dbDevice.setDebugDeadline(instance.getTime());
            scheduler.getScheduledThreadPoolExecutor().schedule(new DebugDeviceTask(deviceService,dbDevice),day, TimeUnit.DAYS);
           return new ResponseBean<>(deviceService.update(dbDevice)>0);
        }
        throw new CommonException("设备不存在");
    }

    /**
     * 首页健康指数
     * @param deviceAlarmDto
     * @return
     */
    @GetMapping("indexHealth")
    @ApiOperation("首页健康指数")
    public ResponseBean<Map> indexHealth(DeviceAlarmDto deviceAlarmDto){
        return statisticsService.indexHealth(deviceAlarmDto);
    }
}
