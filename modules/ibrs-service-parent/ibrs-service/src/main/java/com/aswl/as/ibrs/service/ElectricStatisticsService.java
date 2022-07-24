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
     * 新增电量统计表
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
     * 删除电量统计表
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
     * 今天之前的最近一条记录
     * @param deviceId
     * @return
     */
    public ElectricStatistics findLastRecord(String deviceId) {
        return electricStatisticsMapper.findLastRecord(deviceId);
    }

    /**
     * 今天的记录
     * @param deviceId
     * @param date
     * @return
     */
    public ElectricStatistics findTodayRecord(String deviceId, Date date) {
        return electricStatisticsMapper.findTodayRecord(deviceId,date);
    }

    /**
     * 首页--用电量统计
     * @return map
     */
    public Map getElectricNum(String unitId){
        Map map=new LinkedHashMap();
        String roles = RoleContextHolder.getRole();
        String regionCode ="";
        String tenantCode = SysUtil.getTenantCode();
        String projectId = SysUtil.getProjectId();
        if (SysUtil.isAdmin() || roles.contains(SecurityConstant.ROLE_ADMIN)) { //超级管理员
            //加载所有项目
            tenantCode = null;
        } else if (roles != null && !"".equals(roles) &&
                (roles.contains(RoleEnum.ROLE_SYS_ADMIN.getCode()) || roles.contains(RoleEnum.ROLE_SYS_WATCHER.getCode()))) {  //租户系统管理员或租户系统值班员
            //加载用户所在租户的所有项目
        } else if (roles != null && !"".equals(roles) &&
                (roles.contains(RoleEnum.ROLE_PROJECT_ADMIN.getCode()) || roles.contains(RoleEnum.ROLE_PROJECT_WATCHER.getCode()))) {   //项目管理员或项目值班员
            //只加载用户关联的项目（已在SysUtil.getProjectId()获取）
        } else {
            if (regionCode == null || "".equals(regionCode)) {
                String userRegionCode = RegionCodeContextHolder.getRegionCode();
                if (userRegionCode == null || "".equals(userRegionCode)) {
                    return null;
                }
                regionCode = userRegionCode;
            }
        }
        // 当天电量
        Map dayMap =electricStatisticsMapper.getElectricNum(regionCode,projectId,tenantCode,unitId,"day");
        // 本月电量
        Map monthMap  = electricStatisticsMapper.getElectricNum(regionCode,projectId,tenantCode,unitId,"month");
        //本季度电量
        Map quarterMap = electricStatisticsMapper.getElectricNum(regionCode,projectId,tenantCode,unitId,"quarter");
        //本年电量
        Map yearMap = electricStatisticsMapper.getElectricNum(regionCode,projectId,tenantCode,unitId,"year");
        map.put("dayNum",dayMap.get("counts"));
        map.put("monthNum",monthMap.get("counts"));
        map.put("quarterNum",quarterMap.get("counts"));
        map.put("yearNum",yearMap.get("counts"));
        return map;

    }


    /**
     * 用电量统计--用电对比
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
        if (SysUtil.isAdmin() || roles.contains(SecurityConstant.ROLE_ADMIN)) { //超级管理员
            //加载所有项目
            tenantCode = null;
        } else if (roles != null && !"".equals(roles) &&
                (roles.contains(RoleEnum.ROLE_SYS_ADMIN.getCode()) || roles.contains(RoleEnum.ROLE_SYS_WATCHER.getCode()))) {  //租户系统管理员或租户系统值班员
            //加载用户所在租户的所有项目
        } else if (roles != null && !"".equals(roles) &&
                (roles.contains(RoleEnum.ROLE_PROJECT_ADMIN.getCode()) || roles.contains(RoleEnum.ROLE_PROJECT_WATCHER.getCode()))) {   //项目管理员或项目值班员
            //只加载用户关联的项目（已在SysUtil.getProjectId()获取）
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
            //返回日期之间所有的日期
            List<String> dayList = DateUtils.getDayBetweenDates(startTime, endTime);
            for (int i = 0; i < dayList.size(); i++) {
                List<Map> putList = new ArrayList<>();
                Map map = new HashMap();
                map.put("total", 0L);
                putList.add(map);
                resultData.put(dayList.get(i).substring(5,dayList.get(i).length()) + "日", putList);
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
            //返回日期之间所有的月份
            List<String> monthList = DateUtils.getMonthBetween(startTime, endTime);
            for (int i = 0; i < monthList.size(); i++) {
                List<Map> putList=new ArrayList<>();
                Map map =new HashMap();
                map.put("total",0L);
                putList.add(map);
                resultData.put(monthList.get(i) + "月", putList);
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
     * 用电量统计--各单位用电对比
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
        if (SysUtil.isAdmin() || roles.contains(SecurityConstant.ROLE_ADMIN)) { //超级管理员
            //加载所有项目
            tenantCode = null;
        } else if (roles != null && !"".equals(roles) &&
                (roles.contains(RoleEnum.ROLE_SYS_ADMIN.getCode()) || roles.contains(RoleEnum.ROLE_SYS_WATCHER.getCode()))) {  //租户系统管理员或租户系统值班员
            //加载用户所在租户的所有项目
        } else if (roles != null && !"".equals(roles) &&
                (roles.contains(RoleEnum.ROLE_PROJECT_ADMIN.getCode()) || roles.contains(RoleEnum.ROLE_PROJECT_WATCHER.getCode()))) {   //项目管理员或项目值班员
            //只加载用户关联的项目（已在SysUtil.getProjectId()获取）
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
     * 用电量报表统计导出
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
        if (SysUtil.isAdmin() || roles.contains(SecurityConstant.ROLE_ADMIN)) { //超级管理员
            //加载所有项目
            tenantCode = null;
        } else if (roles != null && !"".equals(roles) &&
                (roles.contains(RoleEnum.ROLE_SYS_ADMIN.getCode()) || roles.contains(RoleEnum.ROLE_SYS_WATCHER.getCode()))) {  //租户系统管理员或租户系统值班员
            //加载用户所在租户的所有项目
        } else if (roles != null && !"".equals(roles) &&
                (roles.contains(RoleEnum.ROLE_PROJECT_ADMIN.getCode()) || roles.contains(RoleEnum.ROLE_PROJECT_WATCHER.getCode()))) {   //项目管理员或项目值班员
            //只加载用户关联的项目（已在SysUtil.getProjectId()获取）
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
        //获取文件路径
        String path = "electricStatistics.xlsx";
        //更改文件名编码
        String fileName = dto.getQueryYear() +"年用电统计.xlsx";
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
                HSSFSheet newSheet = exl.createSheet(years[sheetIndex] +"年");
                CopySheetUtil.copySheets(newSheet,srcSheet);
            }
            for (int sheetIndex = 0; sheetIndex<years.length ; sheetIndex++) {
                //获取sheet
                Sheet sheet1 = exl.getSheetAt(sheetIndex);
                exl.setSheetName(sheetIndex,years[sheetIndex] +"年");
                // 填充数据
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                Row row1 = sheet1.getRow(0);
                row1.getCell(0).setCellValue(years[sheetIndex] +"年用电统计");
                Row row2 = sheet1.getRow(1);
                row2.getCell(0).setCellValue("统计时间:" + years[sheetIndex]+ "年" +  "           项目:" + dto.getProjectName()  + "          下载时间 :"  + format.format( new Date()));
                String[] exportData= getExportData();
                String report=exportData[0];
                String reportCopy=exportData[1];
                //设置报送人、抄送人
                Row row3 = sheet1.getRow(2);
                row3.getCell(2).setCellValue(report);
                Row row4 = sheet1.getRow(3);
                row4.getCell(2).setCellValue(reportCopy);
                // 获取数据
                dto.setQueryYear(years[sheetIndex]);
                List<UnitElectricStatisticsVo> list = electricStatisticsMapper.exportUnitElectric(dto);
                Map<String, Integer> unitRowIndexMap = new HashMap();   //供电单位对应行下标Map
                if (list!= null && list.size() > 0) {
                    int unitRowIndex = 5;   //供电单位行下标
                    for (int i = 0; i < list.size(); i++) {
                        String unitName = list.get(i).getName();
                        if (!unitRowIndexMap.containsKey(unitName)) {
                            unitRowIndex++;
                            unitRowIndexMap.put(unitName, unitRowIndex);
                            Row rowInstance = sheet1.getRow(unitRowIndex);
                            for(int k=1; k<=24;k++){
                                rowInstance.getCell(k).setCellValue(0.00D);
                            }
                            //第25行起需要插入新的数据行
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
            
            exl.setForceFormulaRecalculation(true);// 执行公式。
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
     * 返回一个数组
     * [0] 是报送人
     * [1] 是抄送人
     * [2] 是导出报表的类型
     * @return
     */
    public String[] getExportData()
    {
        String report="";
        String reportCopy="";
        String printType="1"; //默认打印第一种
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
