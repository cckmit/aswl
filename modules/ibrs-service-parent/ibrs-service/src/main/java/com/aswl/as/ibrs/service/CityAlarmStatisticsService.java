package com.aswl.as.ibrs.service;

import com.aswl.as.common.core.enums.DeviceKindType;
import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.common.core.utils.DateUtils;
import com.aswl.as.ibrs.api.dto.CityAlarmStatisticsDto;
import com.aswl.as.ibrs.api.dto.DeviceAlarmDto;
import com.aswl.as.ibrs.api.module.CityAlarmStatistics;
import com.aswl.as.ibrs.api.vo.MaintenanceStatisticsVo;
import com.aswl.as.ibrs.mapper.CityAlarmStatisticsMapper;
import com.aswl.as.ibrs.mapper.CityOnlineStatisticsMapper;
import com.aswl.as.ibrs.mapper.DeviceMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@AllArgsConstructor
@Slf4j
@Service
public class CityAlarmStatisticsService extends CrudService<CityAlarmStatisticsMapper, CityAlarmStatistics> {
    private final CityAlarmStatisticsMapper cityAlarmStatisticsMapper;
    private final DeviceMapper deviceMapper;
    private final CityOnlineStatisticsMapper cityOnlineStatisticsMapper;
    /**
     * 新增故障统计表
     *
     * @param cityAlarmStatistics
     * @return int
     */
    @Transactional
    @Override
    public int insert(CityAlarmStatistics cityAlarmStatistics) {
        return cityAlarmStatisticsMapper.insert(cityAlarmStatistics);
    }

    /**
     * 删除故障统计表
     *
     * @param cityAlarmStatistics
     * @return int
     */
    @Transactional
    @Override
    public int delete(CityAlarmStatistics cityAlarmStatistics) {
        return cityAlarmStatisticsMapper.delete(cityAlarmStatistics);
    }

    /**
     * 多条件查询
     * @param statisticsDate
     * @param deviceKind
     * @param cityCode
     * @param projectId
     * @return
     */
    public List<CityAlarmStatistics> findByCondition(Date statisticsDate, String deviceKind, String cityCode, String projectId,String alarmType,String deviceModelId) {
        return cityAlarmStatisticsMapper.findByCondition(statisticsDate,deviceKind,cityCode,projectId,alarmType,deviceModelId);
    }

    /**
     * 市级平台派单率
     * @param cityAlarmStatisticsDto
     * @return
     */
    public CityAlarmStatistics cityPlatDistrRate(CityAlarmStatisticsDto cityAlarmStatisticsDto) {
        return cityAlarmStatisticsMapper.cityPlatDistrRate(cityAlarmStatisticsDto);
    }

    /**
     * 告警类型派单占比
     * @param cityAlarmStatisticsDto
     * @return
     */
    public List<CityAlarmStatistics> cityPlatAlarmTypeDistr(CityAlarmStatisticsDto cityAlarmStatisticsDto) {
        return cityAlarmStatisticsMapper.cityPlatAlarmTypeDistr(cityAlarmStatisticsDto);
    }

    /**
     * 区域派单占比
     * @param cityAlarmStatisticsDto
     * @return
     */
    public List<CityAlarmStatisticsDto> cityPlatCityDistr(CityAlarmStatisticsDto cityAlarmStatisticsDto) {
        return cityAlarmStatisticsMapper.cityPlatCityDistr(cityAlarmStatisticsDto);
    }

    /**
     * 各月份派单修复率
     */
    public Map cityPlatMonthlyRepairReta(CityAlarmStatisticsDto cityAlarmStatisticsDto) {
        Map resultMap = new HashMap();
        String timeUnit = cityAlarmStatisticsDto.getTimeUnit();
        String startTime = cityAlarmStatisticsDto.getStartTime();
        String endTime = cityAlarmStatisticsDto.getEndTime();
        List<String> dates = null;
        if("day".equals(timeUnit)){
            dates = DateUtils.getDayBetweenDates(startTime, endTime);
        }
        if("month".equals(timeUnit)){
            dates = getMonthBetween(startTime, endTime);
        }
        for (String date : dates) {
            resultMap.put(date,new CityAlarmStatisticsDto());
        }
        List<CityAlarmStatisticsDto> cityAlarmStatisticsDtos = cityAlarmStatisticsMapper.cityPlatMonthlyRepairReta(cityAlarmStatisticsDto);
        if(cityAlarmStatisticsDtos.size()>0){
            for (CityAlarmStatisticsDto alarmStatisticsDto : cityAlarmStatisticsDtos) {
                resultMap.put(alarmStatisticsDto.getDate(),alarmStatisticsDto);
            }
        }
        return resultMap;
    }


    private static List<String> getMonthBetween(String minDate, String maxDate) {
        ArrayList<String> result = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");//格式化为年月

        Calendar min = Calendar.getInstance();
        Calendar max = Calendar.getInstance();
        try {
            min.setTime(sdf.parse(minDate));
            min.set(min.get(Calendar.YEAR), min.get(Calendar.MONTH), 1);
            max.setTime(sdf.parse(maxDate));
            max.set(max.get(Calendar.YEAR), max.get(Calendar.MONTH), 2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        while (min.before(max)) {
            result.add(sdf.format(min.getTime()));
            min.add(Calendar.MONTH, 1);
        }
        return result;
    }

    /**
     * 各区域修复率
     * @param cityAlarmStatisticsDto
     * @return
     */
    public List<CityAlarmStatisticsDto> cityPlatCityRepair(CityAlarmStatisticsDto cityAlarmStatisticsDto) {
        return cityAlarmStatisticsMapper.cityPlatCityRepair(cityAlarmStatisticsDto);
    }

    /**
     * 运维统计
     * @param cityAlarmStatisticsDto
     * @return
     */
    public MaintenanceStatisticsVo maintenanStatist(CityAlarmStatisticsDto cityAlarmStatisticsDto) {
        MaintenanceStatisticsVo maintenanceStatisticsVo = new MaintenanceStatisticsVo();
        String cityCode = cityAlarmStatisticsDto.getCityCode();
        String projectId = cityAlarmStatisticsDto.getProjectId();
        //箱子数量和摄像机数量
        Long deviceCount = deviceMapper.getDeviceCount(DeviceKindType.BOX.getType(), cityCode, null, projectId,null);
        Long camCount = deviceMapper.getDeviceCount(DeviceKindType.CAMERA.getType(), cityCode, null, projectId,null);
        maintenanceStatisticsVo.setDeviceBoxCount(deviceCount);
        maintenanceStatisticsVo.setCamCount(camCount);
        //设备箱在线率
        Map onlineMap = cityOnlineStatisticsMapper.onlineRate(cityAlarmStatisticsDto);
        Number boxTotalNum = (Number) onlineMap.get("boxTotalNum");
        maintenanceStatisticsVo.setBoxTotalNum(boxTotalNum.longValue());
        Number camTotalNum = (Number) onlineMap.get("camTotalNum");
        maintenanceStatisticsVo.setCamTotalNum(camTotalNum.longValue());
        Number boxOnlineNum = (Number) onlineMap.get("boxOnlineNum");
        maintenanceStatisticsVo.setBoxOnlineNum(boxOnlineNum.longValue());
        Number camOnlineNum = (Number) onlineMap.get("camOnlineNum");
        maintenanceStatisticsVo.setCamOnlineNum(camOnlineNum.longValue());
        Number boxIntactNum = (Number) onlineMap.get("boxIntactNum");
        maintenanceStatisticsVo.setBoxIntactNum(boxIntactNum.longValue());
        Number camIntactNum = (Number) onlineMap.get("camIntactNum");
        maintenanceStatisticsVo.setCamIntactNum(camIntactNum.longValue());
        Map wrongMap = cityAlarmStatisticsMapper.wrongRate(cityAlarmStatisticsDto);
        Number boxRunNum = (Number) wrongMap.get("boxRunNum");
        maintenanceStatisticsVo.setBoxRunNum(boxRunNum.longValue());
        Number camRunNum = (Number) wrongMap.get("camRunNum");
        maintenanceStatisticsVo.setCamRunNum(camRunNum.longValue());
        Number boxRepairNum = (Number) wrongMap.get("boxRepairNum");
        maintenanceStatisticsVo.setBoxRepairNum(boxRepairNum.longValue());
        Number camRepairNum = (Number) wrongMap.get("camRepairNum");
        maintenanceStatisticsVo.setCamRepairNum(camRepairNum.longValue());
        Number boxRepairAvgTime = (Number) wrongMap.get("boxRepairAvgTime");
        maintenanceStatisticsVo.setDeviceBoxRepairDuration(formatSeconds(boxRepairAvgTime.longValue()));
        Number camRepairAvgTime = (Number) wrongMap.get("camRepairAvgTime");
        maintenanceStatisticsVo.setCamRepairDuration(formatSeconds(camRepairAvgTime.longValue()));
        return maintenanceStatisticsVo;
    }


    private String formatSeconds(long seconds) {
        String timeStr = seconds + "秒";
        if (seconds > 60) {
            long second = seconds % 60;
            long min = seconds / 60;
            timeStr = min + "分" + second + "秒";
            if (min > 60) {
                min = (seconds / 60) % 60;
                long hour = (seconds / 60) / 60;
                timeStr = hour + "小时" + min + "分" + second + "秒";
                if (hour > 24) {
                    hour = ((seconds / 60) / 60) % 24;
                    long day = (((seconds / 60) / 60) / 24);
                    timeStr = day + "天" + hour + "小时" + min + "分" + second + "秒";
                }
            }
        }
        return timeStr;
    }
}