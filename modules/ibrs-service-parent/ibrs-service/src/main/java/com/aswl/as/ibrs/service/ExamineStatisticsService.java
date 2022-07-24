package com.aswl.as.ibrs.service;


import com.aswl.as.common.core.constant.CommonConstant;
import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.common.core.utils.DateUtils;
import com.aswl.as.common.core.utils.SysUtil;
import com.aswl.as.ibrs.api.dto.DeductScopeDto;
import com.aswl.as.ibrs.api.dto.LabelScopeDto;
import com.aswl.as.ibrs.api.dto.PeriodScopeDto;
import com.aswl.as.ibrs.api.module.*;
import com.aswl.as.ibrs.api.vo.ExamineStatisticsVo;
import com.aswl.as.ibrs.mapper.AsEventHisAlarmMapper;
import com.aswl.as.ibrs.mapper.ExamineStatisticsMapper;
import com.aswl.as.ibrs.utils.MonthUtils;
import com.aswl.as.user.api.feign.UserServiceClient;
import com.aswl.as.user.api.module.Config;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import okhttp3.Headers;
import org.apache.commons.collections4.map.LinkedMap;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.io.File;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
@Slf4j
@Service
public class ExamineStatisticsService extends CrudService<ExamineStatisticsMapper, ExamineStatistics> {
    private final ExamineStatisticsMapper examineStatisticsMapper;
    private final ExamineStandardService examineStandardService;
    private final ExamineBaseLibService examineBaseLibService;
    private final ExamineStatisticsRecordService examineStatisticsRecordService;
    private final ProjectService projectService;
    private final UserServiceClient userServiceClient;
    private final ExamineItemService examineItemService;
    private final AsEventHisAlarmMapper asEventHisAlarmMapper;
    private final ExamineStatisticsDeductService statisticsDeductService;
    private final ExamineTimePartConfigService timePartConfigService;


    public ExamineStatistics findByStandardId(String standardId, String year, String periodStart, String periodEnd) {
        return examineStatisticsMapper.findByStandardId(standardId, year, periodStart, periodEnd);
    }


    /**
     * 批量新增考核统计
     * 每个周期每个考核标准新增一条
     *
     * @param batchInsert
     */
    public void batchInsert(List<ExamineStatistics> batchInsert) {
        examineStatisticsMapper.batchInsert(batchInsert);
    }

    /**
     * 考核统计总表明细
     *
     * @param standardId
     * @param year
     * @param regionCode
     * @return
     */
    public List<Map<String, PeriodScopeDto>> examineSummary(String standardId, String year,String regionCode) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat fmt = new SimpleDateFormat("HH:mm:ss");
        List<Map<String, PeriodScopeDto>> list = new ArrayList<>();
        Calendar instance = Calendar.getInstance();
        Date date = new Date();
        instance.setTime(date);
       // int currentYear = instance.get(Calendar.YEAR);
        //当前月份
       // int currentMonth = instance.get(Calendar.MONTH) + 1;
        //考核周期
//        ExamineStandard standard = examineStandardService.getById(standardId);
//        Integer period = standard.getExaminePeriod();
        String periodStart;
        String periodEnd;
//        if (Integer.parseInt(year) < currentYear) {
//            currentMonth = 12;
//        }
//        for (int i = 1; i <= currentMonth; i += period) {
//            periodStart = year + "-" + (i < 10 ? "0" + i : i) + "-01 00:00:00";
//            periodEnd = year + "-" + ((i + (period - 1)) < 10 ? "0" + (i + (period - 1)) : (i + (period - 1))) + "-31 23:59:59";
        //年度和standardID查询考核周期
        List<ExamineStatistics> examineStatistics = examineStatisticsMapper.getByStandardIDAndYear(standardId, year,regionCode);
        if (examineStatistics != null && examineStatistics.size() > 0) {
            for (ExamineStatistics examineStatistic : examineStatistics) {
                String statisticId = examineStatistic.getId();
                String examineMonthPeriod = examineStatistic.getExamineMonthPeriod();
                int begin;
                int end;
                if(examineMonthPeriod.contains("-")){
                    begin = Integer.parseInt(examineMonthPeriod.substring(0, examineMonthPeriod.indexOf("-")));
                    end = Integer.parseInt(examineMonthPeriod.substring(examineMonthPeriod.indexOf("-")+1));
                    periodStart = year + "-" + (begin > 10 ? begin : "0"+begin) + "-01 00:00:00";
                    periodEnd = year + "-" + (end > 10 ? end : "0"+end) + "-31 23:59:59";
                }else {
                    periodStart = year + "-" + (Integer.parseInt(examineMonthPeriod) > 10 ? examineMonthPeriod : "0"+examineMonthPeriod) + "-01 00:00:00";
                    periodEnd = year + "-" + (Integer.parseInt(examineMonthPeriod) > 10 ? examineMonthPeriod : "0"+examineMonthPeriod) + "-31 23:59:59";
                    begin = Integer.parseInt(examineMonthPeriod);
                    end = Integer.parseInt(examineMonthPeriod);
                }
                //此周期的总分
//              PeriodScopeDto periodScopeDto = statisticsDeductService.periodTotalScope(standardId, periodStart, periodEnd);
                PeriodScopeDto periodScopeDto = statisticsDeductService.totalScope(statisticId);
//              PeriodScopeDto periodScopeDto = examineStatisticsMapper.periodTotalScope(standardId, periodStart, periodEnd);
                //此周期的每个指标的分数
//                List<LabelScopeDto> labelScopeDtos = examineStatisticsMapper.periodLabelScope(standardId, periodStart, periodEnd);
                List<LabelScopeDto> labelScopeDtos = statisticsDeductService.LabelScope(statisticId);
                //当前周期所有的扣分记录
//                List<ExamineStatisticsRecord> recordList = examineStatisticsRecordService.getByPeriodAndStandardId(standardId, periodStart, periodEnd);
                List<ExamineStatisticsRecord> recordList = examineStatisticsRecordService.detailsRecord(statisticId);
                //扣分详细记录
                List<DeductScopeDto> deductScopeDtoList = new ArrayList<>();
                for (ExamineStatisticsRecord record : recordList) {
                    String relateId = record.getRelateId();
                    ExamineBaseLib baseLib = new ExamineBaseLib();
                    baseLib.setId(record.getExamineBaseLibId());
                    ExamineBaseLib examineBaseLib = examineBaseLibService.get(baseLib);
                    //找到具体的考核项item
                    ExamineItem item = examineItemService.getItemByStandardIdAndBaseLibId(standardId, record.getExamineBaseLibId());
                    String itemId = item.getId();
                    if (examineBaseLib.getCode().contains("PATROL")) { //关联Id为未巡更表
                        //未巡更历史表名
                        List<String> hisTables = new ArrayList<>();
                        List<String> monthList = MonthUtils.getMonthBetween(periodStart, periodEnd);
                        //周期确定查哪几张历史表
                        for (String month : monthList) {
                            hisTables.add("as_patrol_his_no_record_" + month.replaceAll("-", ""));
                        }
                        //查询表是否存在
                        hisTables = asEventHisAlarmMapper.findTables(hisTables);
//                        List<DeductScopeDto> deductDetailsWithNoRecord = examineStatisticsRecordService.findDeductDetailsWithNoRecord(relateId, hisTables, periodStart, periodEnd, standardId);
                        List<DeductScopeDto> deductDetailsWithNoRecord = examineStatisticsRecordService.findDeductDetailsNoRecord(relateId, hisTables);
                        if(deductDetailsWithNoRecord != null && deductDetailsWithNoRecord.size() > 0){
                            for (DeductScopeDto deductScopeDto : deductDetailsWithNoRecord) {
                                deductScopeDto.setTitle(examineBaseLib.getTitle());
                            }
                            deductScopeDtoList.addAll(deductDetailsWithNoRecord);
                        }
                    } else {  //关联ID为工单表
//                        List<DeductScopeDto> deductDetailsWithFlowRun = examineStatisticsRecordService.findDeductDetailsWithFlowRun(relateId, periodStart, periodEnd, standardId, itemId);
                        List<DeductScopeDto> deductDetailsWithFlowRun = examineStatisticsRecordService.findDeductDetailsFlowRun(relateId);
                        List<ExamineTimePartConfig> configs = timePartConfigService.getConfigListExamineItemId(itemId);
                        if (deductDetailsWithFlowRun.size() > 0) {
                            for (DeductScopeDto deductScopeDto : deductDetailsWithFlowRun) {
//                                deductScopeDto.setDelayTime(DateUtils.formatDateTime(deductScopeDto.getDelayTime()));
                                deductScopeDto.setTitle(examineBaseLib.getTitle());
                                deductScopeDto.setTime(format.format(new Date()));
                                for (ExamineTimePartConfig config : configs) {
                                    Date beginTime = config.getBeginTime();
                                    Date endTime = config.getEndTime();
                                    Date alarmTime = fmt.parse(deductScopeDto.getAlarmTime().substring(10));
                                    if(DateUtils.isEffectiveDate(alarmTime,beginTime,endTime)){
                                        deductScopeDto.setResponseTime(config.getRespondTime());
                                        long delay = (format.parse(deductScopeDto.getHandleTime()).getTime() / 1000) - (format.parse(deductScopeDto.getAlarmTime()).getTime() / 1000) - config.getRespondTime();
                                        deductScopeDto.setDelayTime(DateUtils.formatDateTime(delay+""));
                                        break;
                                    }
                                }
                            }
                        }
                        deductScopeDtoList.addAll(deductDetailsWithFlowRun);
                    }
                }
                if (periodScopeDto == null) {
                    periodScopeDto = new PeriodScopeDto();
//                    periodScopeDto.setPeriod(periodStart.substring(0, 10) + "至" + periodEnd.substring(0, 10));
                    periodScopeDto.setPeriod(begin == end ? begin + "月" : begin + "月-" + end + "月");
                    periodScopeDto.setTotalScope("总分:100");
                    periodScopeDto.setTotalFee("总扣费:0");
                }else {
                    periodScopeDto.setPeriod(begin == end ? begin + "月" : begin + "月-" + end + "月");
                }
                List<String> baseTitles = examineBaseLibService.findAllTitleByStandardId(standardId);
                List<LabelScopeDto> dtoList = new ArrayList<>();
                if (labelScopeDtos != null && labelScopeDtos.size() > 0) {
                    List<String> titles = labelScopeDtos.stream().map(LabelScopeDto::getLabelTitle).collect(Collectors.toList());
                    List<String> intersection = baseTitles.stream().filter(item -> !titles.contains(item)).collect(Collectors.toList());
                    if (intersection != null && intersection.size() > 0) {
                        for (String title : intersection) {
                            LabelScopeDto dto = new LabelScopeDto();
                            dto.setLabelFee("0");
                            dto.setLabelScope("0");
                            dto.setLabelTitle(title);
                            labelScopeDtos.add(dto);
                        }
                        periodScopeDto.setLabelScopeDtoList(labelScopeDtos);
                    }else {
                        periodScopeDto.setLabelScopeDtoList(labelScopeDtos);
                    }
                } else {
                    for (String title : baseTitles) {
                        LabelScopeDto dto = new LabelScopeDto();
                        dto.setLabelFee("0");
                        dto.setLabelScope("0");
                        dto.setLabelTitle(title);
                        dtoList.add(dto);
                    }
                    periodScopeDto.setLabelScopeDtoList(dtoList);
                }
                if (deductScopeDtoList != null) {
                    periodScopeDto.setDeductScopeDtoList(deductScopeDtoList);
                }
                periodScopeDto.setPeriodId(statisticId);
                Map<String, PeriodScopeDto> map = new HashMap<>();
//                map.put(periodStart.substring(0, 10) + "至" + periodEnd.substring(0, 10), periodScopeDto);
                map.put(begin == end ? begin + "月" : begin + "月-" + end + "月",periodScopeDto);
                list.add(map);
            }
        }
        return list;
    }
    public static void main(String[] args) {
        Calendar instance = Calendar.getInstance();
        Date date = new Date();
        instance.setTime(date);
        int currentYear = instance.get(Calendar.YEAR);
        //当前月份
        int currentMonth = instance.get(Calendar.MONTH) + 1;
        int period = 1;
        String year = "2020";
        String periodStart;
        String periodEnd;
        System.out.println(currentMonth);
        for (int i = 1; i + (period - 1) <= currentMonth; i += period) {
            periodStart = year + "-" + i + "-01 00:00:00";
            periodEnd = year + "-" + (i + (period - 1)) + "-31 23:59:59";
            System.out.println(periodStart);
            System.out.println(periodEnd);
        }

        String s = "1198505";
        System.out.println(DateUtils.formatDateTime(s));
    }

    /**
     * 导出
     *
     * @param response
     * @return
     */
    public ResponseEntity<byte[]> export(List<Map<String,PeriodScopeDto>> list, HttpServletResponse response,String periodId,String standardId) throws UnsupportedEncodingException {

        String p = Thread.currentThread().getContextClassLoader().getResource("").getPath() + File.separator + "temp";
        String path = "reportTemplate3.xlsx";
        String fileName = "考核统计报表.xlsx";
        String encoderName = URLEncoder.encode(fileName, "UTF-8");
        InputStream in = null;
        //创建工作簿
        Workbook wb;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
//            in = new FileInputStream(p + File.separator + path);
//            wb = WorkbookFactory.create(in);
//            in = new FileInputStream("C:\\asms\\reportTemplate3.xlsx");
            in = getClass().getClassLoader().getResourceAsStream("temp/"+path);
            wb = new XSSFWorkbook(in);
            //将数据写到excel中
            //第一张sheet
            sheet1(wb.getSheetAt(0), list,standardId);
            //第二张sheet
            sheet2(wb.getSheetAt(1),periodId,standardId);
            wb.write(out);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment;filename="+encoderName);
            return ResponseEntity.ok().headers(headers).contentType(MediaType.parseMediaType("application/x-msdownload")).body(out.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }


    private void sheet1(Sheet sheet,List<Map<String,PeriodScopeDto>> list,String standardId) {
        //考核标题
        ExamineStandard examineStandard = new ExamineStandard();
        examineStandard.setId(standardId);
        ExamineStandard standard = examineStandardService.get(examineStandard);
        setCellValue(sheet, 0, 0, standard.getTitle());
        //统计时间,项目,下载时间
        Project pro = new Project();
        pro.setProjectId(standard.getProjectId());
        Project project = projectService.get(pro);
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
//        setCellValue(sheet, 1, 0, "统计时间:" + date + "   项目:" + project.getProjectName() + "  下载时间:" + date);
        sheet.getRow(2).getCell(0).setCellValue("统计时间:" + date + "   项目:" + project.getProjectName() + "  下载时间:" + date);
        Config config = new Config();
        config.setDelFlag(CommonConstant.DEL_FLAG_NORMAL);
        ResponseBean<List<Config>> configList = userServiceClient.findConfigList(config);
        String report = "";
        String reportCopy = "";
        if (configList.getData() != null && configList.getData().size() > 0) {
            for (Config c : configList.getData()) {
                if (c.getParamKey().equals(CommonConstant.CONFIG_PARAM_KEY_REPORT_PERSON)) {
                    report = c.getParamValue();
                }
                if (c.getParamKey().equals(CommonConstant.CONFIG_PARAM_KEY_REPORT_COPY_PERSON)) {
                    reportCopy = c.getParamValue();
                }
            }
        }
        setCellValue(sheet, 3, 2, report);
        setCellValue(sheet, 4, 2, reportCopy);
        Workbook workbook = sheet.getWorkbook();
        XSSFFont font = (XSSFFont) workbook.createFont();
        font.setFontHeightInPoints((short) 10);
        XSSFCellStyle cellStyle = (XSSFCellStyle) workbook.createCellStyle();
        cellStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN); //下边框
        cellStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);//左边框
        cellStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);//上边框
        cellStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);//右边框
        cellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        cellStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        cellStyle.setFont(font);
        //动态生成excel的行数
        int dynamicRowNum = list.size();
        for (Map<String, PeriodScopeDto> map : list) {
            Collection<PeriodScopeDto> values = map.values();
            Iterator<PeriodScopeDto> iterator = values.iterator();
            if (iterator.hasNext()) {
                List<LabelScopeDto> labelScopeDtoList = iterator.next().getLabelScopeDtoList();
                if (labelScopeDtoList != null) {
                    dynamicRowNum += labelScopeDtoList.size();
                }
            }
        }
        for (int i = 0; i < dynamicRowNum; i++) {
            //循环一次就创建一行
            Row row = sheet.createRow(i + 5);
            //创建单元格
            for (int j = 0; j < 8; j++) {
                Cell cell = row.createCell(j);
                cell.setCellStyle(cellStyle);
            }
            CellRangeAddress region = new CellRangeAddress(i + 5, i + 5, 0, 2);
            CellRangeAddress region2 = new CellRangeAddress(i + 5, i + 5, 3, 5);
            CellRangeAddress region3 = new CellRangeAddress(i + 5, i + 5, 6, 7);
            sheet.addMergedRegion(region);
            sheet.addMergedRegion(region2);
            sheet.addMergedRegion(region3);
        }
        List<String> column1 = new LinkedList<>();
        List<String> column2 = new LinkedList<>();
        List<String> column3 = new LinkedList<>();
        for (Map<String, PeriodScopeDto> map : list) {
            Set<String> strings = map.keySet();
            Iterator<String> iterator = strings.iterator();
            String next = null;
            if (iterator.hasNext()) {
                next = iterator.next();
                column1.add(next);
            }
            PeriodScopeDto periodScopeDto = map.get(next);
            String totalScope = periodScopeDto.getTotalScope();
            String totalFee = periodScopeDto.getTotalFee();
            column2.add(totalScope);
            column3.add(totalFee);
            List<LabelScopeDto> labelScopeDtoList = periodScopeDto.getLabelScopeDtoList();
            if(labelScopeDtoList != null && labelScopeDtoList.size() > 0){
                for (LabelScopeDto labelScopeDto : labelScopeDtoList) {
                    column1.add(labelScopeDto.getLabelTitle());
                    column2.add(labelScopeDto.getLabelScope());
                    column3.add(labelScopeDto.getLabelFee());
                }
            }
        }
        for(int i = 5;i <= dynamicRowNum+4;i++){
            Row row = sheet.getRow(i);
            row.getCell(0).setCellValue(column1.get(i-5));
            row.getCell(3).setCellValue(column2.get(i-5));
            row.getCell(6).setCellValue(column3.get(i-5));
        }
        for (int i = 1; i <= 6; i++) {
            Row row = sheet.createRow(4 + dynamicRowNum + i);
            for (int j = 0; j < 8; j++) {
                Cell cell = row.createCell(j);
                cell.setCellStyle(cellStyle);
            }
        }
        sheet.getRow(4 + dynamicRowNum + 1).getCell(0).setCellValue("报告说明");
        sheet.getRow(4 + dynamicRowNum + 5).getCell(0).setCellValue("报送单位");
        sheet.getRow(4 + dynamicRowNum + 6).getCell(5).setCellValue("(盖章)");
        sheet.getRow(4 + dynamicRowNum + 5).getCell(6).setCellValue("报送人");
        sheet.getRow(4 + dynamicRowNum + 5).getCell(7).setCellValue("(签字)");
        CellRangeAddress region = new CellRangeAddress(4 + dynamicRowNum + 1 , 4 + dynamicRowNum + 4, 0, 1);
        CellRangeAddress region2 = new CellRangeAddress(4 + dynamicRowNum + 1 , 4 + dynamicRowNum + 4, 2, 7);
        CellRangeAddress region3 = new CellRangeAddress(4 + dynamicRowNum + 5, 4 + dynamicRowNum + 6, 0, 1);
        CellRangeAddress region4 = new CellRangeAddress(4 + dynamicRowNum + 5, 4 + dynamicRowNum + 6, 2, 5);
        sheet.addMergedRegion(region);
        sheet.addMergedRegion(region2);
        sheet.addMergedRegion(region3);
        sheet.addMergedRegion(region4);


//        OutputStream out = new FileOutputStream("C:\\Users\\Administrator.PB7U89UTTKIIX98\\Desktop\\ceshi2.xlsx");
//        sheet.getWorkbook().write(out);
//        out.close();
//        int lastRowNum = sheet.getLastRowNum();
//        sheet.getRow(lastRowNum-1).getCell(0).setCellValue("报送单位");
//        sheet.getRow(lastRowNum-1).getCell(4).setCellValue("(盖章)");
//        sheet.getRow(lastRowNum-1).getCell(6).setCellValue("报送人");
//        sheet.getRow(lastRowNum-1).getCell(7).setCellValue("(签字)");
//        sheet.getRow(lastRowNum - 4).getCell(0).setCellValue("报告说明");
//        Row row = sheet.createRow(lastRowNum);
//        for (int i = 0; i < 8; i++) {
//            Cell cell = row.createCell(i);
//        }
//        CellRangeAddress address = new CellRangeAddress(lastRowNum+1,lastRowNum+1,0,7);
//        sheet.addMergedRegion(address);
//        row.createCell(0).setCellValue("下载人:"+SysUtil.getUser()+" 制表:广州傲视物联网信息技术有限公司");
    }


    private void sheet2(Sheet sheet,String periodId,String standardId) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ExamineStatistics examineStatistics = examineStatisticsMapper.findById(periodId);
        String year = examineStatistics.getExamineYear();
        String examineMonthPeriod = examineStatistics.getExamineMonthPeriod();
        String periodStart;
        String periodEnd;
        int begin;
        int end;
        if(examineMonthPeriod.contains("-")){
            begin = Integer.parseInt(examineMonthPeriod.substring(0, examineMonthPeriod.indexOf("-")));
            end = Integer.parseInt(examineMonthPeriod.substring(examineMonthPeriod.indexOf("-")+1));
            periodStart = year + "-" + (begin > 10 ? begin : "0"+begin) + "-01 00:00:00";
            periodEnd = year + "-" + (end > 10 ? end : "0"+end) + "-31 23:59:59";
        }else {
            periodStart = year + "-" + (Integer.parseInt(examineMonthPeriod) > 10 ? examineMonthPeriod : "0"+examineMonthPeriod) + "-01 00:00:00";
            periodEnd = year + "-" + (Integer.parseInt(examineMonthPeriod) > 10 ? examineMonthPeriod : "0"+examineMonthPeriod) + "-31 23:59:59";
            begin = Integer.parseInt(examineMonthPeriod);
            end = Integer.parseInt(examineMonthPeriod);
        }
        //此周期的总分
//        PeriodScopeDto periodScopeDto = examineStatisticsMapper.periodTotalScope(standardId, periodStart, periodEnd);
        PeriodScopeDto periodScopeDto = statisticsDeductService.totalScope(periodId);
        //此周期的每个指标的分数
//        List<LabelScopeDto> labelScopeDtos = examineStatisticsMapper.periodLabelScope(standardId, periodStart, periodEnd);
        List<LabelScopeDto> labelScopeDtos = statisticsDeductService.LabelScope(periodId);
        //当前周期所有的扣分记录
//        List<ExamineStatisticsRecord> recordList = examineStatisticsRecordService.getByPeriodAndStandardId(standardId, periodStart, periodEnd);
        List<ExamineStatisticsRecord> recordList = examineStatisticsRecordService.detailsRecord(periodId);
        //扣分详细记录
        List<DeductScopeDto> deductScopeDtoList = new ArrayList<>();
        for (ExamineStatisticsRecord record : recordList) {
            String relateId = record.getRelateId();
            ExamineBaseLib baseLib = new ExamineBaseLib();
            baseLib.setId(record.getExamineBaseLibId());
            ExamineBaseLib examineBaseLib = examineBaseLibService.get(baseLib);
            //找到具体的考核项item
            ExamineItem item = examineItemService.getItemByStandardIdAndBaseLibId(standardId,record.getExamineBaseLibId());
            String itemId = item.getId();
            if (examineBaseLib.getCode().contains("PATROL")) { //关联Id为未巡更表
                //未巡更历史表名
                List<String> hisTables = new ArrayList<>();
                List<String> monthList = MonthUtils.getMonthBetween(periodStart, periodEnd);
                //周期确定查哪几张历史表
                for (String month : monthList) {
                    hisTables.add("as_patrol_his_no_record_" + month.replaceAll("-", ""));
                }
                //查询表是否存在
                hisTables = asEventHisAlarmMapper.findTables(hisTables);
//                List<DeductScopeDto> deductDetailsWithNoRecord = examineStatisticsRecordService.findDeductDetailsWithNoRecord(relateId, hisTables, periodStart, periodEnd, standardId);
                List<DeductScopeDto> deductDetailsWithNoRecord = examineStatisticsRecordService.findDeductDetailsNoRecord(relateId, hisTables);
                if (deductDetailsWithNoRecord != null && deductDetailsWithNoRecord.size() > 0) {
                    for (DeductScopeDto deductScopeDto : deductDetailsWithNoRecord) {
                        deductScopeDto.setTitle(examineBaseLib.getTitle());
                    }
                    deductScopeDtoList.addAll(deductDetailsWithNoRecord);
                }
            } else {  //关联ID为工单表
//                List<DeductScopeDto> deductDetailsWithFlowRun = examineStatisticsRecordService.findDeductDetailsWithFlowRun(relateId, periodStart, periodEnd, standardId,itemId);
                List<DeductScopeDto> deductDetailsWithFlowRun = examineStatisticsRecordService.findDeductDetailsFlowRun(relateId);
                if(deductDetailsWithFlowRun.size() > 0){
                    for (DeductScopeDto deductScopeDto : deductDetailsWithFlowRun) {
//                        deductScopeDto.setDelayTime(DateUtils.formatDateTime(deductScopeDto.getDelayTime()));
//                        deductScopeDto.setTime(format.format(new Date()));
                        deductScopeDto.setTitle(examineBaseLib.getTitle());
                    }
                }
                deductScopeDtoList.addAll(deductDetailsWithFlowRun);
            }
        }
        if (periodScopeDto == null) {
            periodScopeDto = new PeriodScopeDto();
//            periodScopeDto.setPeriod(periodStart.substring(0,10)+"至"+periodEnd.substring(0,10));
            periodScopeDto.setPeriod(begin == end ? begin + "月" : begin + "月-" + end + "月");
            periodScopeDto.setTotalScope("总分:100");
            periodScopeDto.setTotalFee("总扣费:0");
        }else {
            periodScopeDto.setPeriod(begin == end ? begin + "月" : begin + "月-" + end + "月");
        }
        if(labelScopeDtos != null && labelScopeDtos.size() > 0){
            periodScopeDto.setLabelScopeDtoList(labelScopeDtos);

        }
        if(deductScopeDtoList != null){
            periodScopeDto.setDeductScopeDtoList(deductScopeDtoList);
        }
        //考核标题
        ExamineStandard examineStandard = new ExamineStandard();
        examineStandard.setId(standardId);
        ExamineStandard standard = examineStandardService.get(examineStandard);
        setCellValue(sheet, 0, 0, standard.getTitle());
        //统计时间,项目,下载时间
        Project pro = new Project();
        pro.setProjectId(standard.getProjectId());
        Project project = projectService.get(pro);
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        //        setCellValue(sheet, 1, 0, "统计时间:" + date + "   项目:" + project.getProjectName() + "  下载时间:" + date);
        sheet.getRow(2).getCell(0).setCellValue("统计时间:" + date + "   项目:" + project.getProjectName() + "  下载时间:" + date);
        Config config = new Config();
        config.setDelFlag(CommonConstant.DEL_FLAG_NORMAL);
        ResponseBean<List<Config>> configList = userServiceClient.findConfigList(config);
        String report = "";
        String reportCopy = "";
        if (configList.getData() != null && configList.getData().size() > 0) {
            for (Config c : configList.getData()) {
                if (c.getParamKey().equals(CommonConstant.CONFIG_PARAM_KEY_REPORT_PERSON)) {
                    report = c.getParamValue();
                }
                if (c.getParamKey().equals(CommonConstant.CONFIG_PARAM_KEY_REPORT_COPY_PERSON)) {
                    reportCopy = c.getParamValue();
                }
            }
        }
        setCellValue(sheet, 3, 2, report);
        setCellValue(sheet, 4, 2, reportCopy);
        Workbook workbook = sheet.getWorkbook();
        XSSFFont font = (XSSFFont) workbook.createFont();
        font.setFontHeightInPoints((short) 10);
        XSSFCellStyle cellStyle = (XSSFCellStyle) workbook.createCellStyle();
        cellStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN); //下边框
        cellStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);//左边框
        cellStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);//上边框
        cellStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);//右边框
        cellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        cellStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        cellStyle.setFont(font);
        //创建行
        Row row = sheet.createRow(5);
        for (int i = 0; i < 8; i++) {
            Cell cell = row.createCell(i);
            cell.setCellStyle(cellStyle);
        }
        CellRangeAddress cellRangeAddress1 = new CellRangeAddress(5, 5, 0, 2);
        CellRangeAddress cellRangeAddress2 = new CellRangeAddress(5, 5, 3, 5);
        CellRangeAddress cellRangeAddress3 = new CellRangeAddress(5, 5, 6, 7);
        sheet.addMergedRegion(cellRangeAddress1);
        sheet.addMergedRegion(cellRangeAddress2);
        sheet.addMergedRegion(cellRangeAddress3);
//        sheet.getRow(5).getCell(0).setCellValue(periodStart.substring(0,10)+"至"+periodEnd.substring(0,10));
//        sheet.getRow(5).getCell(0).setCellValue(periodStart.substring(0,10)+"至"+periodEnd.substring(0,10));
        sheet.getRow(5).getCell(0).setCellValue(begin == end ? begin + "月扣分" : begin + "月-" + end + "月扣分");
        sheet.getRow(5).getCell(3).setCellValue(periodScopeDto.getTotalScope());
        sheet.getRow(5).getCell(6).setCellValue(periodScopeDto.getTotalFee());
        if(labelScopeDtos != null && labelScopeDtos.size() > 0){
            for (int i = 0; i < labelScopeDtos.size(); i++) {
                LabelScopeDto labelScopeDto = labelScopeDtos.get(i);
                Row row1 = sheet.createRow(6 + i);
                for (int j = 0; j < 8; j++) {
                    Cell cell = row1.createCell(j);
                    cell.setCellStyle(cellStyle);
                }
                CellRangeAddress cellRangeAddress4 = new CellRangeAddress(6+i, 6+i, 0, 2);
                CellRangeAddress cellRangeAddress5 = new CellRangeAddress(6+i, 6+i, 3, 5);
                CellRangeAddress cellRangeAddress6 = new CellRangeAddress(6+i, 6+i, 6, 7);
                sheet.addMergedRegion(cellRangeAddress4);
                sheet.addMergedRegion(cellRangeAddress5);
                sheet.addMergedRegion(cellRangeAddress6);
//                    Row row2 = sheet.getRow(6 + i);
                row1.getCell(0).setCellValue(labelScopeDto.getLabelTitle());
                row1.getCell(3).setCellValue("扣分:"+labelScopeDto.getLabelScope());
                row1.getCell(6).setCellValue("扣费:"+labelScopeDto.getLabelFee());
            }
        }
        Row row1 = sheet.createRow(labelScopeDtos.size() + 6);
        for (int i = 0; i < 8; i++) {
            Cell cell = row1.createCell(i);
            cell.setCellStyle(cellStyle);
        }
//        row1.createCell(0).setCellValue(periodStart.substring(0,10)+"至"+periodEnd.substring(0,10)+"扣分明细");
        row1.createCell(0).setCellValue(begin == end ? begin + "月扣分明细" : begin + "月-" + end + "月扣分明细");
        CellRangeAddress address0 = new CellRangeAddress(labelScopeDtos.size() + 6,labelScopeDtos.size() + 6,0,7);
        sheet.addMergedRegion(address0);
        if(deductScopeDtoList != null && deductScopeDtoList.size() > 0){
            for (int i = 0; i < deductScopeDtoList.size(); i++) {
                DeductScopeDto deductScopeDto;
                deductScopeDto = deductScopeDtoList.get(i);
                Row row2 = sheet.createRow(labelScopeDtos.size() + 7 + i);
                for (int j = 0; j < 8; j++) {
                    Cell cell = row2.createCell(j);
                    cell.setCellStyle(cellStyle);
                }
                CellRangeAddress address = new CellRangeAddress(labelScopeDtos.size() + 7 + i,labelScopeDtos.size() + 7 + i,1,2);
                CellRangeAddress address2 = new CellRangeAddress(labelScopeDtos.size() + 7 + i,labelScopeDtos.size() + 7 + i,4,7);
                sheet.addMergedRegion(address);
                sheet.addMergedRegion(address2);
                if(i == 0){
                    Row row3 = sheet.getRow(labelScopeDtos.size() + 7 + i);
                    row3.getCell(0).setCellValue("序号");
                    row3.getCell(1).setCellValue("考核指标");
                    row3.getCell(3).setCellValue("区域");
                    row3.getCell(4).setCellValue("事件");
                }else {
                    Row row3 = sheet.getRow(labelScopeDtos.size() + 7 + i);
                    row3.getCell(0).setCellValue(i);
                    row3.getCell(1).setCellValue(deductScopeDto.getTitle());
                    row3.getCell(3).setCellValue(deductScopeDto.getRegionName());
                    row3.getCell(4).setCellValue(deductScopeDto.getEvent());
                }
            }

        }
        int lastRowNum = sheet.getLastRowNum();
        for (int i = 1; i <= 6; i++) {
            Row row2 = sheet.createRow(lastRowNum + i);
            for (int j = 0; j < 8; j++) {
                Cell cell = row2.createCell(j);
                cell.setCellStyle(cellStyle);
            }
        }
        sheet.getRow(lastRowNum+1).getCell(0).setCellValue("报告说明");
        sheet.getRow(lastRowNum+5).getCell(0).setCellValue("报送单位");
        sheet.getRow(lastRowNum+5).getCell(6).setCellValue("报送人");
        sheet.getRow(lastRowNum+5).getCell(7).setCellValue("(签字)");
        sheet.getRow(lastRowNum+6).getCell(5).setCellValue("(盖章)");
        CellRangeAddress address = new CellRangeAddress(lastRowNum+1,lastRowNum+4,0,1);
        CellRangeAddress address2 = new CellRangeAddress(lastRowNum+1,lastRowNum+4,2,7);
        CellRangeAddress address3 = new CellRangeAddress(lastRowNum+5,lastRowNum+6,0,1);
        CellRangeAddress address4 = new CellRangeAddress(lastRowNum+5,lastRowNum+6,2,5);
        sheet.addMergedRegion(address);
        sheet.addMergedRegion(address2);
        sheet.addMergedRegion(address3);
        sheet.addMergedRegion(address4);
        Row row2 = sheet.createRow(lastRowNum + 7);
        for (int i = 0; i < 8; i++) {
            Cell cell = row2.createCell(i);
            cell.setCellStyle(cellStyle);
        }
        CellRangeAddress address7 = new CellRangeAddress(lastRowNum + 7,lastRowNum + 7,0,7);
        sheet.addMergedRegion(address7);
        row2.getCell(0).setCellValue("下载人:"+SysUtil.getUser()+" 制表人:广州傲视物联网信息技术有限公司");

    }

    private void setCellValue(Sheet sheet, int rowNum, int cellNum, Object value) {
        // 判断对应的类型，然后设置对应的值，重复获取row完全没问题，因为看源码，row的对象是放在treeMap里面的，重复获取可以忽略该时间
        Cell c = sheet.getRow(rowNum).getCell(cellNum);
        // 判断对应数据框的类型，然后设置对应的值，只要设置几个常用的类型就可以了
        if (c == null || value == null) //这里可以写成多加一个判断，如果为空是否需要创建一个Cell
        {
            return;
        }
        if (value instanceof String) {
            c.setCellValue((String) value);
        } else if (value instanceof Integer) {
            c.setCellValue((Integer) value);
        } else if (value instanceof Double) {
            c.setCellValue((Double) value);
        } else if (value instanceof Long) {
            c.setCellValue((Long) value);
        } else if (value instanceof Short) {
            c.setCellValue((Short) value);
        } else if (value instanceof Number) {
            c.setCellValue(((Number) value).doubleValue());
        } else if (value instanceof Date) {
            c.setCellValue((Date) value);
        } else if (value instanceof Calendar) {
            c.setCellValue((Calendar) value);
        } else if (value instanceof RichTextString) {
            c.setCellValue((RichTextString) value);
        } else if (value instanceof Boolean) {
            c.setCellValue((Boolean) value);
        } else {
            c.setCellValue(value.toString());
        }
    }

}
