package com.aswl.as.ibrs.task;

import com.aswl.as.common.core.utils.IdGen;
import com.aswl.as.common.core.utils.SysUtil;
import com.aswl.as.ibrs.api.module.Device;
import com.aswl.as.ibrs.api.module.PatrolHisNoRecord;
import com.aswl.as.ibrs.api.module.Region;
import com.aswl.as.ibrs.api.module.RegionLeader;
import com.aswl.as.ibrs.api.vo.DeviceVo;
import com.aswl.as.ibrs.mapper.RegionLeaderMapper;
import com.aswl.as.ibrs.service.*;
import com.aswl.as.ibrs.utils.CityPlatformUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 巡更统计定时任务
 */
@Component
public class PatrolStatisticsTask {

    @Autowired
    private PatrolHisNoRecordService patrolHisNoRecordService;

    @Autowired
    private RegionLeaderService regionLeaderService;

    @Autowired
    private EventPatrolService eventPatrolService;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private RegionService regionService;

    @Autowired
    private CityPlatformUtil cityPlatformUtil;

    private static final String DAY = "day";
    private static final String MONTH = "month";
    private static final String YEAR = "year";

    /**
     * 生成未巡更记录
     */
    @Async
    @Scheduled(cron = "59 59 22 * * ?")
    public void patrolStatistics() throws Exception {

        if(cityPlatformUtil.getEnable() && cityPlatformUtil.getIsCityPlatform()){
            return;
        }
        //获取所有的区域责任人
        //List<RegionLeader> regionLeaders = regionLeaderService.findAllList(new RegionLeader());
        Collection<RegionLeader> regionLeaders = findAllRegionLeader().values();
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (RegionLeader regionLeader : regionLeaders) {
            //此责任人的巡更开始时间
            Date patrolPeriodBeginTime = regionLeader.getPatrolPeriodBeginTime();
            //此责任人的巡更周期
            Integer patrolPeriod = regionLeader.getPatrolPeriod();
            //当前时间属于哪个巡更周期
            if (patrolPeriodBeginTime != null && patrolPeriod != null) {
                calendar.setTime(patrolPeriodBeginTime);
                calendar.add(Calendar.SECOND, patrolPeriod);
                for (;;) {
                    if (calendar.getTimeInMillis() > new Date().getTime()) {
                        break;
                    }
                    calendar.add(Calendar.SECOND, patrolPeriod);
                }
                //当前周期的巡更结束时间
                String periodEndTime = format.format(calendar.getTime());
                //当前周期的开始时间
                calendar.add(Calendar.SECOND, -patrolPeriod);
                String periodBeginTime = format.format(calendar.getTime());
                //钥匙Id
                String patrolKeyId = regionLeader.getPatrolKeyId();
                //返回当前周期的开始时间到当前时间的月份
                List<String> monthBetween = getMonthBetween(periodBeginTime, format.format(new Date()));
                List<String> tables = new ArrayList<>();
                String tableName = "as_patrol_his_no_record_";
                for (String month : monthBetween) {
                    tables.add(tableName + month.replaceAll("-", ""));
                }
                //查询未巡更历史表
                List<PatrolHisNoRecord> patrolHisNoRecords = patrolHisNoRecordService.findByPatrolKeyIdAndPeriod(regionLeader.getRegionCode(),patrolKeyId, periodBeginTime, periodEndTime, tables);
                if (patrolHisNoRecords.size() == 0) { //未巡更表无记录
                    //查询当前巡更表,授权时间在此负责人巡更周期内的设备
                    List<String> patrolDeviceIds = eventPatrolService.findByPeriod(regionLeader.getPatrolKeyId(), periodBeginTime, periodEndTime);
                    //根据区域责任人的区域Id查询设备
                    List<String> regionIds = new ArrayList<>();
                    if (regionLeader.getRegionId().indexOf(",") == -1) {
                        regionIds.add(regionLeader.getRegionId());
                    } else {
                        for (String s : regionLeader.getRegionId().split(",")) {
                            regionIds.add(s);
                        }
                    }
                    //当前负责人的所有设备
                    List<String> deviceIds = deviceService.findByRegionIds(regionIds);
                    //当前负责人未巡更的设备
                    List<String> noRecordDeviceIds = deviceIds.stream().filter(item -> !patrolDeviceIds.contains(item)).collect(Collectors.toList());
                    String noRecordHisTable = "as_patrol_his_no_record_" + format.format(new Date()).substring(0, 7).replaceAll("-", "");
                    List<PatrolHisNoRecord> patrolHisNoRecordList = new ArrayList<>();
                    if(noRecordDeviceIds != null && noRecordDeviceIds.size() > 0){
                        for (String deviceId : noRecordDeviceIds) {
                            PatrolHisNoRecord patrolHisNoRecord = new PatrolHisNoRecord();
                            patrolHisNoRecord.setId(IdGen.snowflakeId());
                            patrolHisNoRecord.setDeviceId(deviceId);
                            Device device = new Device();
                            device.setId(deviceId);
//                            device = deviceService.get(device);
//                            patrolHisNoRecord.setRegionNo(device.getRegionCode());
                            patrolHisNoRecord.setRegionNo(regionLeader.getRegionCode());
                            patrolHisNoRecord.setCipherId(regionLeader.getPatrolKeyId());
                            patrolHisNoRecord.setTenantCode(regionLeader.getTenantCode());
                            patrolHisNoRecord.setApplicationCode(regionLeader.getApplicationCode());
                            try {
                                patrolHisNoRecord.setPeriodBeginDate(format.parse(periodBeginTime));
                                patrolHisNoRecord.setPeriodEndDate(format.parse(periodEndTime));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            patrolHisNoRecord.setStoreTime(new Date());
                            patrolHisNoRecordList.add(patrolHisNoRecord);
                        }
                        patrolHisNoRecordService.batchInsert(patrolHisNoRecordList, noRecordHisTable);
                    }
                }
            }
        }
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
     * 查询所有的区域的负责人,没有区域负责人的区域将此区域的负责人设置为上级区域的负责人
     */
    private Map<String,RegionLeader> findAllRegionLeader(){
        List<RegionLeader> regionLeaderList = regionLeaderService.findAllList(new RegionLeader());
        Map<String,RegionLeader> regionLeaderMap = new HashMap<>();
        for (RegionLeader regionLeader : regionLeaderList) {
            regionLeaderMap.put(regionLeader.getRegionCode(),regionLeader);
        }
        //所有的区域
        List<Region> regions = regionService.findList(new Region());
        for (Region region : regions) {
            //根据区域id查询区域负责人
            RegionLeader regionLeader = regionLeaderService.findByRegionId(region.getId());
            if(regionLeader == null){ //说明此区域没有区域负责人,找上级区域的负责人
                String regionCode = region.getRegionCode();
                for (int i = regionCode.length(); i >= 3; i = i - 3) {
                    String code = regionCode.substring(0, i);
                    RegionLeader byRegionCode = regionLeaderService.findByRegionCode(code);
                    if(byRegionCode != null){
                        RegionLeader leader = regionLeaderMap.get(code);
                        leader.setRegionId(leader.getRegionId()+","+region.getId());
                        regionLeaderMap.put(code,leader);
                        break;
                    }
                }
            }
        }
        return regionLeaderMap;
    }

    public static void main(String[] args) {
        List<String> list1 = new ArrayList();
        list1.add("1111");
        list1.add("2222");
        list1.add("3333");

        List<String> list2 = new ArrayList();
//        list2.add("3333");
//        list2.add("4444");
//        list2.add("5555");

        // 差集 (list1 - list2)
        List<String> reduce1 = list1.stream().filter(item -> !list2.contains(item)).collect(Collectors.toList());
        System.out.println("---得到差集 reduce1 (list1 - list2)---");
        reduce1.parallelStream().forEach(System.out :: println);

        // 差集 (list2 - list1)
        List<String> reduce2 = list2.stream().filter(item -> !list1.contains(item)).collect(Collectors.toList());
        System.out.println("---得到差集 reduce2 (list2 - list1)---");
        reduce2.parallelStream().forEach(System.out :: println);

    }
}
