package com.aswl.as.ibrs.service;
import com.aswl.as.common.core.constant.CommonConstant;
import com.aswl.as.common.core.enums.RoleEnum;
import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.common.core.utils.CopySheetUtil;
import com.aswl.as.common.core.utils.DateUtils;
import com.aswl.as.common.core.utils.SysUtil;
import com.aswl.as.common.core.wrapper.CellRangeAddressWrapper;
import com.aswl.as.common.security.constant.SecurityConstant;
import com.aswl.as.ibrs.api.dto.ElectricStatisticsDto;
import com.aswl.as.ibrs.api.module.ElectricStatistics;
import com.aswl.as.ibrs.api.vo.UnitElectricStatisticsVo;
import com.aswl.as.ibrs.filter.RegionCodeContextHolder;
import com.aswl.as.ibrs.filter.RoleContextHolder;
import com.aswl.as.ibrs.mapper.ElectricStatisticsMapper;
import com.aswl.as.user.api.feign.UserServiceClient;
import com.aswl.as.user.api.module.Config;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

@AllArgsConstructor
@Slf4j
@Service
public class ElectricStatisticsService extends CrudService<ElectricStatisticsMapper, ElectricStatistics> {
    private final ElectricStatisticsMapper electricStatisticsMapper;
    private final UserServiceClient userServiceClient;

    /**
     * ?????????????????????
     *
     * @param electricStatistics
     * @return int
     */
    @Transactional
    @Override
    public int insert(ElectricStatistics electricStatistics) {
        return electricStatisticsMapper.insert(electricStatistics);
    }

    /**
     * ?????????????????????
     *
     * @param electricStatistics
     * @return int
     */
    @Transactional
    @Override
    public int delete(ElectricStatistics electricStatistics) {
        return electricStatisticsMapper.delete(electricStatistics);
    }
	
    /**
     * ?????????????????????????????????
     * @param deviceId
     * @return
     */
    public ElectricStatistics findLastRecord(String deviceId) {
        return electricStatisticsMapper.findLastRecord(deviceId);
    }

    /**
     * ???????????????
     * @param deviceId
     * @param date
     * @return
     */
    public ElectricStatistics findTodayRecord(String deviceId, Date date) {
        return electricStatisticsMapper.findTodayRecord(deviceId,date);
    }

    /**
     * ??????--???????????????
     * @return map
     */
    public Map getElectricNum(String unitId){
        Map map=new LinkedHashMap();
        String roles = RoleContextHolder.getRole();
        String regionCode ="";
        String tenantCode = SysUtil.getTenantCode();
        String projectId = SysUtil.getProjectId();
        if (SysUtil.isAdmin() || roles.contains(SecurityConstant.ROLE_ADMIN)) { //???????????????
            //??????????????????
            tenantCode = null;
        } else if (roles != null && !"".equals(roles) &&
                (roles.contains(RoleEnum.ROLE_SYS_ADMIN.getCode()) || roles.contains(RoleEnum.ROLE_SYS_WATCHER.getCode()))) {  //?????????????????????????????????????????????
            //???????????????????????????????????????
        } else if (roles != null && !"".equals(roles) &&
                (roles.contains(RoleEnum.ROLE_PROJECT_ADMIN.getCode()) || roles.contains(RoleEnum.ROLE_PROJECT_WATCHER.getCode()))) {   //?????????????????????????????????
            //???????????????????????????????????????SysUtil.getProjectId()?????????
        } else {
            if (regionCode == null || "".equals(regionCode)) {
                String userRegionCode = RegionCodeContextHolder.getRegionCode();
                if (userRegionCode == null || "".equals(userRegionCode)) {
                    return null;
                }
                regionCode = userRegionCode;
            }
        }
        // ????????????
        Map dayMap =electricStatisticsMapper.getElectricNum(regionCode,projectId,tenantCode,unitId,"day");
        // ????????????
        Map monthMap  = electricStatisticsMapper.getElectricNum(regionCode,projectId,tenantCode,unitId,"month");
        //???????????????
        Map quarterMap = electricStatisticsMapper.getElectricNum(regionCode,projectId,tenantCode,unitId,"quarter");
        //????????????
        Map yearMap = electricStatisticsMapper.getElectricNum(regionCode,projectId,tenantCode,unitId,"year");
        map.put("dayNum",dayMap.get("counts"));
        map.put("monthNum",monthMap.get("counts"));
        map.put("quarterNum",quarterMap.get("counts"));
        map.put("yearNum",yearMap.get("counts"));
        return map;

    }


    /**
     * ???????????????--????????????
     * @param dto
     * @return list
     */
    public List<Object> getElectricContrast(ElectricStatisticsDto dto) {
        String timeUnit = dto.getTimeUnit();
        String startTime = dto.getStartTime();
        String endTime = dto.getEndTime();
        String roles = RoleContextHolder.getRole();
        String regionCode = dto.getRegionCode();
        String tenantCode = SysUtil.getTenantCode();
        String projectId = SysUtil.getProjectId();
        if (SysUtil.isAdmin() || roles.contains(SecurityConstant.ROLE_ADMIN)) { //???????????????
            //??????????????????
            tenantCode = null;
        } else if (roles != null && !"".equals(roles) &&
                (roles.contains(RoleEnum.ROLE_SYS_ADMIN.getCode()) || roles.contains(RoleEnum.ROLE_SYS_WATCHER.getCode()))) {  //?????????????????????????????????????????????
            //???????????????????????????????????????
        } else if (roles != null && !"".equals(roles) &&
                (roles.contains(RoleEnum.ROLE_PROJECT_ADMIN.getCode()) || roles.contains(RoleEnum.ROLE_PROJECT_WATCHER.getCode()))) {   //?????????????????????????????????
            //???????????????????????????????????????SysUtil.getProjectId()?????????
        } else {
            if (regionCode == null || "".equals(regionCode)) {
                String userRegionCode = RegionCodeContextHolder.getRegionCode();
                if (userRegionCode == null || "".equals(userRegionCode)) {
                    return null;
                }
                regionCode = userRegionCode;
            }
        }
        dto.setProjectId(projectId);
        dto.setTenantCode(tenantCode);
        dto.setRegionCode(regionCode);
        List<Map> mapList =electricStatisticsMapper.getElectricContrast(dto);
        Map<String,List<Map>> resultData = new LinkedHashMap<>();
        if("day".equals(timeUnit)) {
            //?????????????????????????????????
            List<String> dayList = DateUtils.getDayBetweenDates(startTime, endTime);
            for (int i = 0; i < dayList.size(); i++) {
                List<Map> putList = new ArrayList<>();
                Map map = new HashMap();
                map.put("total", 0L);
                putList.add(map);
                resultData.put(dayList.get(i).substring(5,dayList.get(i).length()) + "???", putList);
            }
            for (int i = 0; i < mapList.size(); i++) {
                Map map = new HashMap();
                List<Map> putList = new ArrayList<>();
                map.put("total", mapList.get(i).get("electricNum"));
                putList.add(map);
                resultData.put(mapList.get(i).get("date").toString().substring(5,mapList.get(i).get("date").toString().length()), putList);
            }
        }
        else{
            //?????????????????????????????????
            List<String> monthList = DateUtils.getMonthBetween(startTime, endTime);
            for (int i = 0; i < monthList.size(); i++) {
                List<Map> putList=new ArrayList<>();
                Map map =new HashMap();
                map.put("total",0L);
                putList.add(map);
                resultData.put(monthList.get(i) + "???", putList);
            }
            for (int i = 0; i < mapList.size(); i++) {
                List<Map> putList=new ArrayList<>();
                Map map =new HashMap();
                map.put("total",mapList.get(i).get("electricNum"));
                putList.add(map);
                resultData.put(mapList.get(i).get("date") + "",putList);
            }
        }
        ArrayList<Object> result = new ArrayList<>();
        result.add(resultData);
        return result;
    }

    /**
     * ???????????????--?????????????????????
     * @param dto
     * @return list
     */
    public List<UnitElectricStatisticsVo> getUnitElectricContrast(ElectricStatisticsDto dto) {
        String startTime = dto.getStartTime();
        String endTime = dto.getEndTime();
        String roles = RoleContextHolder.getRole();
        String regionCode = dto.getRegionCode();
        String tenantCode = SysUtil.getTenantCode();
        String projectId = SysUtil.getProjectId();
        if (SysUtil.isAdmin() || roles.contains(SecurityConstant.ROLE_ADMIN)) { //???????????????
            //??????????????????
            tenantCode = null;
        } else if (roles != null && !"".equals(roles) &&
                (roles.contains(RoleEnum.ROLE_SYS_ADMIN.getCode()) || roles.contains(RoleEnum.ROLE_SYS_WATCHER.getCode()))) {  //?????????????????????????????????????????????
            //???????????????????????????????????????
        } else if (roles != null && !"".equals(roles) &&
                (roles.contains(RoleEnum.ROLE_PROJECT_ADMIN.getCode()) || roles.contains(RoleEnum.ROLE_PROJECT_WATCHER.getCode()))) {   //?????????????????????????????????
            //???????????????????????????????????????SysUtil.getProjectId()?????????
        } else {
            if (regionCode == null || "".equals(regionCode)) {
                String userRegionCode = RegionCodeContextHolder.getRegionCode();
                if (userRegionCode == null || "".equals(userRegionCode)) {
                    return null;
                }
                regionCode = userRegionCode;
            }
        }
        dto.setProjectId(projectId);
        String queryProjectId = dto.getQueryProjectId();
        if(queryProjectId != null && !"".equals(queryProjectId)){
            dto.setProjectId(queryProjectId);
        }
        dto.setTenantCode(tenantCode);
        dto.setRegionCode(regionCode);
        dto.setStartTime(startTime);
        dto.setEndTime(endTime);
        return electricStatisticsMapper.getUnitElectricContrast(dto);
    }


    /**
     * ???????????????????????????
     *
     * @param dto
     * @return ResponseEntity
     * @throws Exception
     */

    public ResponseEntity<byte[]> export(ElectricStatisticsDto dto) throws Exception {
        String roles = RoleContextHolder.getRole();
        String regionCode = dto.getRegionCode();
        String tenantCode = SysUtil.getTenantCode();
        String projectId = SysUtil.getProjectId();
        if (SysUtil.isAdmin() || roles.contains(SecurityConstant.ROLE_ADMIN)) { //???????????????
            //??????????????????
            tenantCode = null;
        } else if (roles != null && !"".equals(roles) &&
                (roles.contains(RoleEnum.ROLE_SYS_ADMIN.getCode()) || roles.contains(RoleEnum.ROLE_SYS_WATCHER.getCode()))) {  //?????????????????????????????????????????????
            //???????????????????????????????????????
        } else if (roles != null && !"".equals(roles) &&
                (roles.contains(RoleEnum.ROLE_PROJECT_ADMIN.getCode()) || roles.contains(RoleEnum.ROLE_PROJECT_WATCHER.getCode()))) {   //?????????????????????????????????
            //???????????????????????????????????????SysUtil.getProjectId()?????????
        } else {
            if (regionCode == null || "".equals(regionCode)) {
                String userRegionCode = RegionCodeContextHolder.getRegionCode();
                if (userRegionCode == null || "".equals(userRegionCode)) {
                    return null;
                }
                regionCode = userRegionCode;
            }
        }
        dto.setProjectId(projectId);
        dto.setTenantCode(tenantCode);
        dto.setRegionCode(regionCode);
        //??????????????????
        String path = "electricStatistics.xlsx";
        //?????????????????????
        String fileName = dto.getQueryYear() +"???????????????.xlsx";
        String gFileName = URLEncoder.encode(fileName, "UTF-8");
        InputStream in = null;
        HSSFWorkbook exl;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {

            in = getClass().getClassLoader().getResourceAsStream("temp/" + path);
            exl = new HSSFWorkbook(in);
            String queryYear = dto.getQueryYear();
            String [] years =queryYear.split(",");
            for (int sheetIndex = 1; sheetIndex<years.length ; sheetIndex++) {
                HSSFSheet srcSheet = exl.getSheetAt(0);
                HSSFSheet newSheet = exl.createSheet(years[sheetIndex] +"???");
                CopySheetUtil.copySheets(newSheet,srcSheet);
            }
            for (int sheetIndex = 0; sheetIndex<years.length ; sheetIndex++) {
                //??????sheet
                Sheet sheet1 = exl.getSheetAt(sheetIndex);
                exl.setSheetName(sheetIndex,years[sheetIndex] +"???");
                // ????????????
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                Row row1 = sheet1.getRow(0);
                row1.getCell(0).setCellValue(years[sheetIndex] +"???????????????");
                Row row2 = sheet1.getRow(1);
                row2.getCell(0).setCellValue("????????????:" + years[sheetIndex]+ "???" +  "           ??????:" + dto.getProjectName()  + "          ???????????? :"  + format.format( new Date()));
                String[] exportData= getExportData();
                String report=exportData[0];
                String reportCopy=exportData[1];
                //???????????????????????????
                Row row3 = sheet1.getRow(2);
                row3.getCell(2).setCellValue(report);
                Row row4 = sheet1.getRow(3);
                row4.getCell(2).setCellValue(reportCopy);
                // ????????????
                dto.setQueryYear(years[sheetIndex]);
                List<UnitElectricStatisticsVo> list = electricStatisticsMapper.exportUnitElectric(dto);
                Map<String, Integer> unitRowIndexMap = new HashMap();   //???????????????????????????Map
                if (list!= null && list.size() > 0) {
                    int unitRowIndex = 5;   //?????????????????????
                    for (int i = 0; i < list.size(); i++) {
                        String unitName = list.get(i).getName();
                        if (!unitRowIndexMap.containsKey(unitName)) {
                            unitRowIndex++;
                            unitRowIndexMap.put(unitName, unitRowIndex);
                            Row rowInstance = sheet1.getRow(unitRowIndex);
                            for(int k=1; k<=24;k++){
                                rowInstance.getCell(k).setCellValue(0.00D);
                            }
                            //???25?????????????????????????????????
                            if (unitRowIndex== 25){
                                sheet1.createRow(unitRowIndex);
                            }

                        } else {
                            unitRowIndex = unitRowIndexMap.get(unitName);
                        }
                        Row row = sheet1.getRow(unitRowIndex);
                        row.getCell(0).setCellValue(unitName);
                        if ("01".equals(list.get(i).getMonth())) {
                            row.getCell(1).setCellValue(list.get(i).getElectricNum());
                            row.getCell(2).setCellValue(list.get(i).getElectricFee());
                        } else if ("02".equals(list.get(i).getMonth())) {
                            row.getCell(3).setCellValue(list.get(i).getElectricNum());
                            row.getCell(4).setCellValue(list.get(i).getElectricFee());
                        } else if ("03".equals(list.get(i).getMonth())) {
                            row.getCell(5).setCellValue(list.get(i).getElectricNum());
                            row.getCell(6).setCellValue(list.get(i).getElectricFee());
                        } else if ("04".equals(list.get(i).getMonth())) {
                            row.getCell(7).setCellValue(list.get(i).getElectricNum());
                            row.getCell(8).setCellValue(list.get(i).getElectricFee());
                        } else if ("05".equals(list.get(i).getMonth())) {
                            row.getCell(9).setCellValue(list.get(i).getElectricNum());
                            row.getCell(10).setCellValue(list.get(i).getElectricFee());
                        } else if ("06".equals(list.get(i).getMonth())) {
                            row.getCell(11).setCellValue(list.get(i).getElectricNum());
                            row.getCell(12).setCellValue(list.get(i).getElectricFee());
                        } else if ("07".equals(list.get(i).getMonth())) {
                            row.getCell(13).setCellValue(list.get(i).getElectricNum());
                            row.getCell(14).setCellValue(list.get(i).getElectricFee());
                        } else if ("08".equals(list.get(i).getMonth())) {
                            row.getCell(15).setCellValue(list.get(i).getElectricNum());
                            row.getCell(16).setCellValue(list.get(i).getElectricFee());
                        } else if ("09".equals(list.get(i).getMonth())) {
                            row.getCell(17).setCellValue(list.get(i).getElectricNum());
                            row.getCell(18).setCellValue(list.get(i).getElectricFee());
                        } else if ("10".equals(list.get(i).getMonth())) {
                            row.getCell(19).setCellValue(list.get(i).getElectricNum());
                            row.getCell(20).setCellValue(list.get(i).getElectricFee());
                        } else if ("11".equals(list.get(i).getMonth())) {
                            row.getCell(21).setCellValue(list.get(i).getElectricNum());
                            row.getCell(22).setCellValue(list.get(i).getElectricFee());
                        } else if ("12".equals(list.get(i).getMonth())) {
                            row.getCell(23).setCellValue(list.get(i).getElectricNum());
                            row.getCell(24).setCellValue(list.get(i).getElectricFee());
                        }
                    }
                }
            }
            
            exl.setForceFormulaRecalculation(true);// ???????????????
            exl.write(out);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment;filename=\"" + gFileName + "\"");
            return ResponseEntity.ok().headers(headers)
                    .contentType(MediaType.parseMediaType("application/x-msdownload")).body(out.toByteArray());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    }


    /**
     * ??????????????????
     * [0] ????????????
     * [1] ????????????
     * [2] ????????????????????????
     * @return
     */
    public String[] getExportData()
    {
        String report="";
        String reportCopy="";
        String printType="1"; //?????????????????????
        Config config=new Config();
        ResponseBean<List<Config>> r=userServiceClient.findConfigList(config);
        if(r!=null && ResponseBean.SUCCESS==r.getCode())
        {
            for(Config c:r.getData())
            {
                if(CommonConstant.CONFIG_PARAM_KEY_REPORT_PERSON.equals(c.getParamKey()))
                {
                    report=c.getParamValue();
                }
                if(CommonConstant.CONFIG_PARAM_KEY_REPORT_COPY_PERSON.equals(c.getParamKey()))
                {
                    reportCopy=c.getParamValue();
                }
                if(CommonConstant.CONFIG_PARAM_KEY_REPORT_TOTAL_PRINT_TYPE.equals(c.getParamKey()))
                {
                    printType=c.getParamValue();
                }
            }
        }
        return new String[]{report,reportCopy,printType};
    }
}
