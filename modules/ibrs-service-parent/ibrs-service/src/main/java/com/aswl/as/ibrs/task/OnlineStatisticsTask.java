package com.aswl.as.ibrs.task;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.aswl.as.common.core.config.CityPlatformSender;
import com.aswl.as.common.core.constant.MqConstant;
import com.aswl.as.common.core.dto.CityPlatformDto;
import com.aswl.as.common.core.enums.DeviceKindType;
import com.aswl.as.common.core.utils.IdGen;
import com.aswl.as.ibrs.api.module.OnlineStatistics;
import com.aswl.as.ibrs.api.module.Region;
import com.aswl.as.ibrs.service.OnlineStatisticsService;
import com.aswl.as.ibrs.service.RegionService;
import com.aswl.as.ibrs.utils.CityPlatformUtil;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class OnlineStatisticsTask {


    /*
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RedisTemplate redisTemplate;

    @Resource(name = "redisTemplateJson")
    private RedisTemplate redisTemplateJson;

    @Autowired
    private RegionService regionService;

    @Autowired
    private DeviceService deviceService;
    */
    @Autowired
    private RegionService regionService;

    @Autowired
    private OnlineStatisticsService onlineStatisticsService;

    @Autowired
    private CityPlatformUtil cityPlatformUtil;

    @Autowired
    private CityPlatformSender cityPlatformSender;

    private static final SimpleDateFormat formatDay = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat formatMonth = new SimpleDateFormat("yyyyMM");

    @Async
  @Scheduled(cron = "0 50 23 * * ?")
    //@Scheduled(cron = "0 */2 * * * ?")
    public void regionDeviceOnlineStatistics() {
        try{
            if(cityPlatformUtil.getEnable() && cityPlatformUtil.getIsCityPlatform()){
                return;
            }
            // ??????????????????
            Date statisticsDate = new Date();
            String day = formatDay.format(statisticsDate);
            Date thisDate = formatDay.parse(day);
            String hisTable = "as_event_his_network_"+formatMonth.format(thisDate);
            //?????????????????????????????????
            onlineStatisticsService.deleteByCreateDate(day);
            //???????????????
            List<Region> allRegions = regionService.findAllList(new Region());
            //????????????
            List<OnlineStatistics> onlineStatisticList = new ArrayList<>();
            for (Region region : allRegions) {
                //?????????deviceKind : 2
                List<OnlineStatistics> boxStatisticsList = onlineStatisticsService.getByRegionCodeAndDeviceKind(region.getRegionCode(), DeviceKindType.BOX.getType(), day, hisTable);
                int boxNum = region.getBoxNum() != null ? region.getBoxNum() : 0;    //??????????????????????????????
                int realBoxNum = 0;
                if(boxStatisticsList != null && boxStatisticsList.size() > 0) {
                    for (OnlineStatistics boxStatistics : boxStatisticsList) {    //???????????????????????????????????????
                        boxStatistics.setId(IdGen.snowflakeId());
                        boxStatistics.setCreateDate(statisticsDate);
                    }
                    realBoxNum = boxStatisticsList.stream().mapToInt(OnlineStatistics::getDeviceNum).sum(); //??????????????????????????????
                }
                if(boxNum > realBoxNum){    //???????????????????????????????????? ?????? ??????????????????????????????
                    OnlineStatistics boxStatistics = new OnlineStatistics();
                    boxStatistics.setId(IdGen.snowflakeId());
                    boxStatistics.setCreateDate(statisticsDate);
                    boxStatistics.setRegionNo(region.getRegionCode());
                    boxStatistics.setDeviceKind(DeviceKindType.BOX.getType());
                    boxStatistics.setOnlineNum(0);
                    boxStatistics.setIntactNum(0);
                    boxStatistics.setDeviceNum(boxNum - realBoxNum);    //?????????????????????????????????????????????????????????
                    boxStatistics.setProjectId(region.getProjectId());
                    if(boxStatisticsList == null){
                        boxStatisticsList = new ArrayList<>();
                    }
                    boxStatisticsList.add(boxStatistics);
                }

                //?????????deviceKind : 3
                List<OnlineStatistics> camStatisticsList = onlineStatisticsService.getByRegionCodeAndDeviceKind(region.getRegionCode(),DeviceKindType.CAMERA.getType(), day, hisTable);
                int camNum = region.getCameraNum() != null ? region.getCameraNum() : 0;    //??????????????????????????????
                int realCamNum = 0; //??????????????????????????????
                if(camStatisticsList != null && camStatisticsList.size() > 0) {
                    for (OnlineStatistics camStatistics : camStatisticsList) {
                        camStatistics.setId(IdGen.snowflakeId());
                        camStatistics.setCreateDate(statisticsDate);
                    }
                    realCamNum = camStatisticsList.stream().mapToInt(OnlineStatistics::getDeviceNum).sum(); //??????????????????????????????
                }
                if(camNum > realCamNum){    //???????????????????????????????????? ?????? ??????????????????????????????
                    OnlineStatistics camStatistics = new OnlineStatistics();
                    camStatistics.setId(IdGen.snowflakeId());
                    camStatistics.setCreateDate(statisticsDate);
                    camStatistics.setRegionNo(region.getRegionCode());
                    camStatistics.setDeviceKind(DeviceKindType.CAMERA.getType());
                    camStatistics.setOnlineNum(0);
                    camStatistics.setIntactNum(0);
                    camStatistics.setDeviceNum(camNum - realCamNum);    //?????????????????????????????????????????????????????????
                    camStatistics.setProjectId(region.getProjectId());
                    if(camStatisticsList == null){
                        camStatisticsList = new ArrayList<>();
                    }
                    camStatisticsList.add(camStatistics);
                }

                onlineStatisticList.addAll(boxStatisticsList);
                onlineStatisticList.addAll(camStatisticsList);
            }
            onlineStatisticsService.bathInsert(onlineStatisticList);

            if(cityPlatformUtil.isCityPlatform()) {
                Map cityMap = new HashMap();
                Date date = new Date();
                String today = formatDay.format(date);
                CityPlatformDto cityPlatformDto = new CityPlatformDto();
                cityPlatformDto.setCityCode(cityPlatformUtil.getCityCode());
                cityPlatformDto.setCityName(cityPlatformUtil.getCityName());
                cityPlatformDto.setProjectCode(cityPlatformUtil.getProjectCode());
                cityPlatformDto.setProjectName(cityPlatformUtil.getProjectName());
                List<OnlineStatistics> onlineStatisticsList = new ArrayList<>();
                OnlineStatistics cityBoxtatistics = onlineStatisticsService.getByDeviceKind(DeviceKindType.BOX.getType());
                if(cityBoxtatistics != null){
                    cityBoxtatistics.setCreateDate(formatDay.parse(today));
                    onlineStatisticsList.add(cityBoxtatistics);
                }
                OnlineStatistics cityCamtatistics = onlineStatisticsService.getByDeviceKind(DeviceKindType.CAMERA.getType());
                if(cityCamtatistics != null){
                    cityCamtatistics.setCreateDate(formatDay.parse(today));
                    onlineStatisticsList.add(cityCamtatistics);
                }
                if(onlineStatisticsList.size()>0){
                    cityPlatformDto.setData(onlineStatisticsList);
                    byte[] bytes = JSON.toJSONString(cityPlatformDto, SerializerFeature.WriteDateUseDateFormat).getBytes(StandardCharsets.UTF_8);
                    cityPlatformSender.sender(MqConstant.CITY_PLATFORM_EXCHANGE,MqConstant.CITY_PLATFORM_ONLINE_QUEUE, bytes);
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
