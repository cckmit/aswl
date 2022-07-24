package com.aswl.as.ibrs.task;

import com.aswl.as.common.core.utils.DateUtils;
import com.aswl.as.common.core.utils.IdGen;
import com.aswl.as.ibrs.api.module.*;
import com.aswl.as.ibrs.api.vo.FlowRunVo;
import com.aswl.as.ibrs.api.vo.PatrolHisNoRecordVo;
import com.aswl.as.ibrs.service.*;
import com.aswl.as.ibrs.utils.CityPlatformUtil;
import lombok.AllArgsConstructor;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
@AllArgsConstructor
public class ExamineStatisticsTask {

    private final ExamineStandardService examineStandardService;
    private final ExamineItemService examineItemService;
    private final ExamineBaseLibService examineBaseLibService;
    private final ExamineStatisticsService examineStatisticsService;
    private final ExamineTimePartConfigService examineTimePartConfigService;
    private final ExamineDeductRuleService examineDeductRuleService;
    private final FlowRunService flowRunService;
    private final PatrolHisNoRecordService patrolHisNoRecordService;
    private final ExamineStatisticsDeductService examineStatisticsDeductService;
    private final ExamineRegionService examineRegionService;
    private final ExamineStatisticsRecordService examineStatisticsRecordService;
    private CityPlatformUtil cityPlatformUtil;
    /**
     * 考勤数据生成
     * 每月1号1点统计
     */
    @Async
    @Scheduled(cron = "0 0 1 1 * ?")
    public void examineStatistics() {
        //批量总表
//        List<ExamineStatistics> batchInsertStatistics = new ArrayList<>();
        //批量统计扣分表
//        List<ExamineStatisticsDeduct> batchInsertStatisticsDeduct = new ArrayList<>();
        //批量扣分详细记录表
//        List<ExamineStatisticsRecord> batchInsertStatisticsRecord = new ArrayList<>();
        //
//        List<ExamineStatisticsRecord> recordList = new ArrayList<>();
        if(cityPlatformUtil.getEnable() && cityPlatformUtil.getIsCityPlatform()){
            return;
        }
        Calendar instance = Calendar.getInstance();
        instance.setTime(new Date());
        instance.add(Calendar.MONTH, -1);
        int year = instance.get(Calendar.YEAR);
        int month = instance.get(Calendar.MONTH) + 1;
        int days = instance.getActualMaximum(Calendar.DAY_OF_MONTH);
        //所有的考核标准
        List<ExamineStandard> examineStandards = examineStandardService.findAllList(new ExamineStandard());
        //当前标准下的考核项
        for (ExamineStandard examineStandard : examineStandards) {

            //考核标准Id
            String standardId = examineStandard.getId();
            //考核标准下的区域
            List<ExamineRegion> regions = examineRegionService.getListByStandardId(standardId);
            //考核周期
            Integer examinePeriod = examineStandard.getExaminePeriod();
            //确定上个月属于哪个周期(周期开始时间和结束时间)
            String periodStart = null;
            String periodEnd = null;
            String periodMonthStart = null;
            String periodMonthEnd = null;
//            int i = examinePeriod;
//            for (;;) {
//                if (month <= examinePeriod) {
//                    periodStart = year + "-" + ((examinePeriod - i + 1) > 10 ? (examinePeriod - i + 1) : "0" + (examinePeriod - i + 1)) + "-01 00:00:00";
//                    periodEnd = year + "-" + (examinePeriod > 10 ? examinePeriod : "0" + examinePeriod) + "-31 23:59:59";
//                    break;
//                }
//                examinePeriod += i;
//            }
            int times = 12/examinePeriod;
            for (int i = 1; i <= times; i++) {
                if(month >= 1+(i-1)*examinePeriod && month <= i*examinePeriod){
                    periodStart = year + "-" + (1+(i-1)*examinePeriod > 10 ? 1+(i-1)*examinePeriod : "0"+1+(i-1)*examinePeriod) + "-01 00:00:00";
                    periodEnd = year + "-" + (i*examinePeriod > 10 ? i*examinePeriod : "0"+i*examinePeriod) + "-31 23:59:59";
                    periodMonthStart = 1+(i-1)*examinePeriod+"";
                    periodMonthEnd = i*examinePeriod+"";
                }
            }
            ExamineStatistics statistics = examineStatisticsService.findByStandardId(standardId, year + "", periodStart, periodEnd);
            String statisticsId;
            if (statistics == null) {
                ExamineStatistics statistics_db = new ExamineStatistics();
                statisticsId = IdGen.snowflakeId();
                statistics_db.setId(statisticsId);
                statistics_db.setExamineStandardId(standardId);
                statistics_db.setExamineYear(year + "");
                statistics_db.setStoreTime(new Date());
                statistics_db.setProjectId(examineStandard.getProjectId());
                statistics_db.setApplicationCode(examineStandard.getApplicationCode());
                statistics_db.setTenantCode(examineStandard.getTenantCode());
                statistics_db.setExamineMonthPeriod(examinePeriod == 1 ? periodMonthStart : periodMonthStart + "-" + periodMonthEnd);
//                batchInsertStatistics.add(statistics_db);
                examineStatisticsService.insert(statistics_db);
            }else {
                statisticsId = statistics.getId();
            }

            //根据考核标准Id获取考核项
            List<ExamineItem> examineItems = examineItemService.getItemListByStandardId(standardId);
            for (ExamineItem examineItem : examineItems) {
                Integer flowCount;
                long noRecord;
                Double totalScope = 0.00d;
                Double totalFee = 0.00d;
                //考核基础库ID
                String baseLibId = examineItem.getExamineBaseLibId();
                //考核项Id
                String examineItemId = examineItem.getId();
                //考核基础指标
                ExamineBaseLib examineBaseLib = examineBaseLibService.getBy(baseLibId);
                //一个考核项对应多个考核配置
                List<ExamineTimePartConfig> configList = examineTimePartConfigService.getConfigListExamineItemId(examineItemId);
                //一个考核项对应多个扣分规则
                List<ExamineDeductRule> deductRules = examineDeductRuleService.getByExamineItemId(examineItemId);
                   String baseLibCode = examineBaseLib.getCode();
                if (baseLibCode.contains("PATROL")) { //说明这个考核项是巡检的
                    String tableName = "as_patrol_his_no_record_" + year + (month > 10 ? month : "0" + month);
                    //上个月有多少个未巡更的记录
                    List<PatrolHisNoRecordVo> patrolHisNoRecords = patrolHisNoRecordService.getNoRecordExamineTime(tableName, year + "-" + month + "-01 00:00:00", year + "-" + month + "-" + days + " 23:59:59");
                    noRecord = patrolHisNoRecords.size();
                    if (noRecord > 0) {
                        for (ExamineDeductRule deductRule : deductRules) {
                            boolean flag = false;
                            String expression = deductRule.getExpression();
                            switch (expression) {
                                case "--":
                                    if (noRecord >= deductRule.getMinNum() && noRecord <= deductRule.getMaxNum()) {
                                        totalScope += deductRule.getDeductScore();
                                        totalFee += deductRule.getDeductFee();
                                    }
                                    flag = true;
                                    break;
                                case ">":
                                    if (noRecord > deductRule.getMaxNum()) {
                                        totalScope += deductRule.getDeductScore();
                                        totalFee += deductRule.getDeductFee();
                                    }
                                    flag = true;
                                    break;
                                case "<":
                                    if (noRecord < deductRule.getMaxNum()) {
                                        totalScope += deductRule.getDeductScore();
                                        totalFee += deductRule.getDeductFee();
                                    }
                                    flag = true;
                                    break;
                                case "<=":
                                    if (noRecord <= deductRule.getMaxNum()) {
                                        totalScope += deductRule.getDeductScore();
                                        totalFee += deductRule.getDeductFee();
                                    }
                                    flag = true;
                                    break;
                                case ">=":
                                    if (noRecord >= deductRule.getMaxNum()) {
                                        totalScope += deductRule.getDeductScore();
                                        totalFee += deductRule.getDeductFee();
                                    }
                                    flag = true;
                                    break;
                                case "=":
                                    if (noRecord == deductRule.getMaxNum()) {
                                        totalScope += deductRule.getDeductScore();
                                        totalFee += deductRule.getDeductFee();
                                    }
                                    flag = true;
                                    break;
                            }
                            if(flag){
                                break;
                            }
                        }
                    }
                    for (PatrolHisNoRecordVo patrolHisNoRecord : patrolHisNoRecords) {
                        ExamineStatisticsRecord record = new ExamineStatisticsRecord();
                        //ID主键
                        record.setId(IdGen.snowflakeId());
                        //考核统计ID
                        record.setExamineStatisticsId(statisticsId);
                        //考核指标ID
                        record.setExamineBaseLibId(baseLibId);
                        //区域编码
                        record.setRegionNo(patrolHisNoRecord.getRegionCode());
                        //区域名称
                        record.setRegionName(patrolHisNoRecord.getRegionName());
                        //关联ID
                        record.setRelateId(patrolHisNoRecord.getId());
                        //存储时间
                        record.setStoreTime(new Date());
                        //系统编码
                        record.setApplicationCode(patrolHisNoRecord.getApplicationCode());
                        //租户编码
                        record.setTenantCode(patrolHisNoRecord.getTenantCode());
//                        recordList.add(record);
//                        examineStatisticsRecordService.batchInsert(recordList);
                        examineStatisticsRecordService.insert(record);
                    }
                } else {//工单表
                    for (ExamineTimePartConfig partConfig : configList) {
                        if (examineItem.getExamineTimePart() == 1) {
                            //考核开始时段
                            Date beginTime = partConfig.getBeginTime();
                            String beginTimeStr = DateUtils.dateToString(beginTime);
                            //考核结束时段
                            Date endTime = partConfig.getEndTime();
                            String endTimeStr = DateUtils.dateToString(endTime);
                            //响应时长
                            Integer respondTime = partConfig.getRespondTime();
                            //某个考核配置下的工单超时数
                            List<FlowRunVo> flowRuns = flowRunService.findCountByExamineTime(beginTimeStr, endTimeStr, respondTime, year + "-" + (month > 10 ? month : "0"+month) + "-01 00:00:00", year + "-" + month + "-" + days + " 23:59:59");
                            flowCount = flowRuns.size();
                            if (flowCount > 0) {
                                for (ExamineDeductRule deductRule : deductRules) {
                                    boolean flag = false;
                                    String expression = deductRule.getExpression();
                                    switch (expression) {
                                        case "--":
                                            if (flowCount >= deductRule.getMinNum() && flowCount <= deductRule.getMaxNum()) {
                                                totalScope += deductRule.getDeductScore();
                                                totalFee += deductRule.getDeductFee();
                                            }
                                            flag = true;
                                            break;
                                        case ">":
                                            if (flowCount > deductRule.getMaxNum()) {
                                                totalScope += deductRule.getDeductScore();
                                                totalFee += deductRule.getDeductFee();
                                            }
                                            flag = true;
                                            break;
                                        case "<":
                                            if (flowCount < deductRule.getMaxNum()) {
                                                totalScope += deductRule.getDeductScore();
                                                totalFee += deductRule.getDeductFee();
                                            }
                                            flag = true;
                                            break;
                                        case "<=":
                                            if (flowCount <= deductRule.getMaxNum()) {
                                                totalScope += deductRule.getDeductScore();
                                                totalFee += deductRule.getDeductFee();
                                            }
                                            flag = true;
                                            break;
                                        case ">=":
                                            if (flowCount >= deductRule.getMaxNum()) {
                                                totalScope += deductRule.getDeductScore();
                                                totalFee += deductRule.getDeductFee();
                                            }
                                            flag = true;
                                            break;
                                        case "=":
                                            if (flowCount.equals(deductRule.getMaxNum())) {
                                                totalScope += deductRule.getDeductScore();
                                                totalFee += deductRule.getDeductFee();
                                            }
                                            flag = true;
                                            break;
                                    }
                                    if(flag){
                                        break;
                                    }
                                }
                            }
                            for (FlowRunVo flowRunVo : flowRuns) {
                                ExamineStatisticsRecord record = new ExamineStatisticsRecord();
                                //ID主键
                                record.setId(IdGen.snowflakeId());
                                //考核统计ID
                                record.setExamineStatisticsId(statisticsId);
                                //考核指标ID
                                record.setExamineBaseLibId(baseLibId);
                                //区域编码
                                record.setRegionNo(flowRunVo.getRegionCode());
                                //区域名称
                                record.setRegionName(flowRunVo.getRegionName());
                                //关联ID
                                record.setRelateId(flowRunVo.getId());
                                //存储时间
                                record.setStoreTime(new Date());
                                //系统编码
                                record.setApplicationCode(flowRunVo.getApplicationCode());
                                //租户编码
                                record.setTenantCode(flowRunVo.getTenantCode());
//                                recordList.add(record);
//                                examineStatisticsRecordService.batchInsert(recordList);
                                examineStatisticsRecordService.insert(record);
                            }
                        }
                    }
                }
                //插入每个考核项的扣分总计
                ExamineStatisticsDeduct deduct = new ExamineStatisticsDeduct();
                   //ID主键
                deduct.setId(IdGen.snowflakeId());
                   //考核统计ID
                deduct.setExamineStatisticsId(statisticsId);
                   //考核指标ID
                deduct.setExamineBaseLibId(baseLibId);
                   //扣分
                deduct.setDeductScoreTotal(BigDecimal.valueOf(totalScope));
                //扣费
                deduct.setDeductFeeTotal(totalFee);
                   //存储时间
                deduct.setStoreTime(new Date());
                   //系统编码
                deduct.setApplicationCode(examineStandard.getApplicationCode());
                   //租户编码
                deduct.setTenantCode(examineStandard.getTenantCode());
//                batchInsertStatisticsDeduct.add(deduct);
//                examineStatisticsDeductService.batchInsert(batchInsertStatisticsDeduct);
                examineStatisticsDeductService.insert(deduct);
            }
//            if(batchInsertStatisticsDeduct.size() > 0){
//                //批量插入扣分统计
//                examineStatisticsDeductService.batchInsert(batchInsertStatisticsDeduct);
//            }
        }
//        if(batchInsertStatistics.size() > 0){
//            //批量插入
//            examineStatisticsService.batchInsert(batchInsertStatistics);
//        }
    }

    public static void main(String[] args) throws IOException, InvalidFormatException {
        Calendar instance = Calendar.getInstance();
        instance.setTime(new Date());
        instance.add(Calendar.MONTH, 6);
        int days = instance.getActualMaximum(Calendar.DAY_OF_MONTH);
        int year = instance.get(Calendar.YEAR);
        int month = instance.get(Calendar.MONTH) + 1;
        System.out.println(year + "" + month + "" + days + "");
        String periodStart;
        String periodEnd;
        int examinePeriod = 3;
        int i = examinePeriod;
        for (; ; ) {
            if (month <= examinePeriod) {
                periodStart = year + "-" + ((examinePeriod - i + 1) > 10 ? (examinePeriod - i + 1) : "0" + (examinePeriod - i + 1)) + "-01 00:00:00";
                periodEnd = year + "-" + (examinePeriod > 10 ? examinePeriod : "0" + examinePeriod) + "-31 23:59:59";
                break;
            }
            examinePeriod += i;
        }
        System.out.println(periodStart);
        System.out.println(periodEnd);
        double totalScope = 23.58d;
        BigDecimal bigDecimal = BigDecimal.valueOf(totalScope);
        System.out.println(bigDecimal);
        InputStream inputStream = new FileInputStream("C:\\Users\\Administrator.PB7U89UTTKIIX98\\Desktop\\ceshi.xlsx");
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        Sheet sheet = workbook.getSheetAt(0);  //示意访问sheet
//        XSSFSheet sheet = workbook.createSheet();
        int dynamicRowNum = 10;
        for (int x = 1; x <= dynamicRowNum; x++) {
            //循环一次就创建一行
            Row row = sheet.createRow(x + 4);
            //创建单元格
            for (int j = 0; j < 8; j++) {
                Cell cell = row.createCell(j);
                cell.setCellValue("你好a ");
            }
        }
        OutputStream outputStream = new FileOutputStream("C:\\Users\\Administrator.PB7U89UTTKIIX98\\Desktop\\ceshi.xlsx");
        workbook.write(outputStream);
        inputStream.close();
        outputStream.close();
    }
}
