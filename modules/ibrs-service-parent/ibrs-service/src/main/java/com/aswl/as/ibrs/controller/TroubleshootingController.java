package com.aswl.as.ibrs.controller;

import com.aswl.as.common.core.constant.CommonConstant;
import com.aswl.as.common.core.enums.BusinessType;
import com.aswl.as.common.core.enums.FlowRunStatus;
import com.aswl.as.common.core.enums.RoleEnum;
import com.aswl.as.common.core.exceptions.CommonException;
import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.utils.DateUtils;
import com.aswl.as.common.core.utils.IdGen;
import com.aswl.as.common.core.utils.PageUtil;
import com.aswl.as.common.core.utils.SysUtil;
import com.aswl.as.common.log.annotation.Log;
import com.aswl.as.common.security.constant.SecurityConstant;
import com.aswl.as.ibrs.api.dto.AlarmTypeDeviceFavoriteDto;
import com.aswl.as.ibrs.api.dto.DeviceFaultDto;
import com.aswl.as.ibrs.api.dto.FlowRunDto;
import com.aswl.as.ibrs.api.dto.WorkOrderDto;
import com.aswl.as.ibrs.api.module.AlarmTypeDeviceFavorite;
import com.aswl.as.ibrs.api.vo.DeviceFaultVo;
import com.aswl.as.ibrs.filter.RegionCodeContextHolder;
import com.aswl.as.ibrs.filter.RoleContextHolder;
import com.aswl.as.ibrs.service.FlowRunLogService;
import com.aswl.as.ibrs.service.FlowRunService;
import com.aswl.as.ibrs.utils.StringUtils;
import com.aswl.as.user.api.dto.UserInfoDto;
import com.aswl.as.user.api.feign.UserServiceClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 故障维护Controller
 *
 * @author jk
 * @version 1.0.0
 * @create 2019/10/23 10:06
 */
@Slf4j
@AllArgsConstructor
@Api(value = "/api/v1/troubleshooting", tags = "故障维护")
@RestController
@RequestMapping("/v1/troubleshooting")
public class TroubleshootingController {
    private final FlowRunService flowRunService;
    private final FlowRunLogService flowRunLogService;

    /**
     * 获取维护流程状态
     *
     * @return
     */
    @GetMapping(value = "findFlowStatus/{id}")
    @ApiOperation(value = "获取维护流程状态")
    public ResponseBean<Map> findFlowStatus(@PathVariable("id") String id) {
        return new ResponseBean<>(flowRunService.findFlowStatus(id));
    }


    /**
     * 获取故障维护待处理列表
     *
     * @return
     */
    @GetMapping(value = "findUndoneList")
    @ApiOperation(value = "获取故障维护待处理列表")
    public ResponseBean<PageInfo<DeviceFaultVo>> findTroubleshootingUndoneList(@RequestParam(value = "pageNum", required = false) String pageNum,
                                                                 @RequestParam(value = "pageSize", required = false) String pageSize,
                                                                 DeviceFaultDto deviceFaultDto) {

        /*if (deviceFaultDto.getStartTime() != null && !deviceFaultDto.getStartTime().equals("")) {
            Date date = new Date();//取时间
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date); //需要将date数据转移到Calender对象中操作
            calendar.add(calendar.DATE, -(deviceFaultDto.getStartTime().equals("0") ? 0 : Integer.parseInt(deviceFaultDto.getStartTime())));//把日期往后增加n天.正数往后推,负数往前移动
            date = calendar.getTime();   //这个时间就是日期往后推一天的结果
            deviceFaultDto.setStartTime(DateUtils.asLocalDateTime(date).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        }*/
        return new ResponseBean<>(flowRunService.findUndoneList(PageUtil.pageInfo(pageNum, pageSize, "", ""), deviceFaultDto));
    }


    @PostMapping(value = "manual")
    @ApiOperation(value = "手动派单")
    public ResponseBean<Boolean> manualCreateWorkOrder(@RequestBody FlowRunDto flowRunDto) {
        return new ResponseBean<>(flowRunService.manualCreateWorkOrder(flowRunDto) > 0);
    }

    @PostMapping(value = "batchManual")
    @ApiOperation(value = "批量手动派单", notes = "批量手动派单")
    @Log(value = "批量手动派单",businessType = BusinessType.INSERT)
    public ResponseBean
            <Boolean> batchInsertUserWatchDevice(@RequestBody @Valid String orders) {
        ObjectMapper mapper = new ObjectMapper();
        CollectionType listType = mapper.getTypeFactory().constructCollectionType(ArrayList.class, FlowRunDto.class);
        List<FlowRunDto> flowRunDtos = null;
        try {
            flowRunDtos = mapper.readValue(orders, listType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ResponseBean<>(flowRunService.batchCreateWorkOrder(flowRunDtos ) > 0);
    }

    /**
     * 工单处理
     *
     * @return
     */
    @PostMapping(value = "handleWorkOrder")
    @ApiOperation(value = "工单处理")
    public ResponseBean<Boolean> handleWorkOrder(@RequestBody WorkOrderDto workOrderDto) {

            flowRunService.handleWorkOrder(workOrderDto);
            return new ResponseBean<>(true);
    }

    /**
     * 工单处理历史记录
     *
     * @return
     */
    @GetMapping(value = "findWorkOrderHistory/{id}")
    @ApiOperation(value = "工单处理历史记录")
    public ResponseBean<PageInfo<DeviceFaultVo>> findWorkOrderHistory(@RequestParam(value = "pageNum",required = false) String pageNum,
                                                    @RequestParam(value = "pageSize",required = false) String pageSize,
                                                    @PathVariable("id") String id) {
        return new ResponseBean<>(flowRunLogService.findWorkOrderHistory(PageUtil.pageInfo(pageNum, pageSize, "", ""), id));
    }



    @PostMapping(value = "revokeAlarm")
    @ApiOperation(value = "撤消告警", notes = "撤消告警")
    @Log(value = "撤消告警",businessType = BusinessType.INSERT)
        public ResponseBean<Boolean> revokeAlarm(@RequestBody @Valid String orders) {

        ObjectMapper mapper = new ObjectMapper();
        CollectionType listType = mapper.getTypeFactory().constructCollectionType(ArrayList.class, FlowRunDto.class);
        List<FlowRunDto> flowRunDtos = null;
        try {
            flowRunDtos = mapper.readValue(orders, listType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ResponseBean<>(flowRunService.revokeAlarm(flowRunDtos) > 0);
    }

    /**
     * APP端获取全部工单数和我的工单数量
     *
     * @return ResponseBean
     */
    @GetMapping(value = "/app/tabOrderCount")
    @ApiOperation(value = "APP端获取全部工单数和我的工单数量")
    public ResponseBean<Map> findTabOrderCount(DeviceFaultDto deviceFaultDto){

        String roles = RoleContextHolder.getRole();
        String regionCode = deviceFaultDto.getRegionCode();
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
            if(regionCode == null || "".equals(regionCode)){
                String userRegionCode = RegionCodeContextHolder.getRegionCode();
                if(regionCode == null || "".equals(regionCode)){
                    return null;
                }
                regionCode = userRegionCode;
            }
        }
        deviceFaultDto.setTenantCode(tenantCode);
        deviceFaultDto.setProjectId(projectId);
        String queryProjectId = deviceFaultDto.getQueryProjectId();
        if (StringUtils.isNotEmpty(queryProjectId)){
            deviceFaultDto.setProjectId(queryProjectId);
        }
        deviceFaultDto.setRegionCode(regionCode);
        return new ResponseBean<>(flowRunService.findTabOrderCount(deviceFaultDto));
    }

    /**
     * 故障上报
     * @param flowRunDto
     * @return ResponseBean
     */
    @PostMapping(value = "faultReport")
    @ApiOperation(value = "故障上报", notes = "故障上报")
    @Log(value = "故障上报",businessType = BusinessType.INSERT)
    public ResponseBean
            <Boolean> insertFaultReport(@RequestBody FlowRunDto flowRunDto) {
        return new ResponseBean<>(flowRunService.insertFaultReport(flowRunDto) > 0);
    }
    
}
