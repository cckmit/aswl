package com.aswl.as.ibrs.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.aswl.as.common.core.constant.CommonConstant;
import com.aswl.as.common.core.constant.DtuPushConstant;
import com.aswl.as.common.core.enums.*;
import com.aswl.as.common.core.exceptions.CommonException;
import com.aswl.as.common.core.model.DtuBodyBean;
import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.common.core.utils.IdGen;
import com.aswl.as.common.core.utils.JPushUtils;
import com.aswl.as.common.core.utils.SysUtil;
import com.aswl.as.common.core.vo.UserVo;
import com.aswl.as.common.security.constant.SecurityConstant;
import com.aswl.as.common.security.project.CloudCommon;
import com.aswl.as.ibrs.api.dto.DeviceEventAlarmDto;
import com.aswl.as.ibrs.api.dto.DeviceFaultDto;
import com.aswl.as.ibrs.api.dto.FlowRunDto;
import com.aswl.as.ibrs.api.dto.WorkOrderDto;
import com.aswl.as.ibrs.api.module.*;
import com.aswl.as.ibrs.api.vo.*;
import com.aswl.as.ibrs.filter.RegionCodeContextHolder;
import com.aswl.as.ibrs.filter.RoleContextHolder;
import com.aswl.as.ibrs.mapper.*;
import com.aswl.as.user.api.dto.SmsDto;
import com.aswl.as.user.api.dto.UserInfoDto;
import com.aswl.as.user.api.feign.UserServiceClient;
import com.aswl.as.user.api.module.SmsResponse;
import com.aswl.as.user.api.module.User;
import com.aswl.iot.dto.constant.MQConstants;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service
public class FlowRunService extends CrudService<FlowRunMapper, FlowRun> {
    private final FlowRunMapper flowRunMapper;
    private final FlowRunPrcsMapper flowRunPrcsMapper;
    private final FlowRunLogMapper flowRunLogMapper;
    private final UserServiceClient userServiceClient;
    private final DeviceEventAlarmMapper deviceEventAlarmMapper;
    private final AlarmRevokeMapper alarmRevokeMapper;
    private final EventUcMetadataMapper eventUcMetadataMapper;
    private final AlarmOrderHandleService alarmOrderHandleService;
    private final DeviceService deviceService;
    private final AlarmTypeService alarmTypeService;
    private final RegionLeaderMapper regionLeaderMapper;
    private  final MailService mailService;

    @Autowired
    @Qualifier(value = "rabbitTemplate")
    private AmqpTemplate amqpTemplate;
    @Value("${sys.projectCode}")
    private String projectCode;
    @Value("${sms.templateCode}")
    private String templateCode;
    @Value("${sms.orderTemplateCode}")
    private String orderTemplateCode;

    //APP推送，如果在5分钟内，不上线，就收不到这条信息
    @Getter
    private int timeToLive = 300;

    /**
     * 新增流程实例
     *
     * @param flowRun
     * @return int
     */
    @Transactional
    @Override
    public int insert(FlowRun flowRun) {
        return super.insert(flowRun);
    }

    /**
     * 删除流程实例
     *
     * @param flowRun
     * @return int
     */
    @Transactional
    @Override
    public int delete(FlowRun flowRun) {
        return super.delete(flowRun);
    }

    /**
     * 获取故障维护待处理列表
     *
     * @return
     */
    public PageInfo<DeviceFaultVo> findUndoneList(PageInfo<DeviceFaultDto> pageInfo, DeviceFaultDto deviceFaultDto) {
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
                if(userRegionCode == null || "".equals(userRegionCode)){
                    throw new CommonException("当前用户暂无数据");
                }
                regionCode = userRegionCode;
            }
        }
        deviceFaultDto.setProjectId(projectId);
        String queryProjectId =deviceFaultDto.getQueryProjectId();
        if (queryProjectId!= null){
            deviceFaultDto.setProjectId(queryProjectId);
        }
        deviceFaultDto.setTenantCode(tenantCode);
        deviceFaultDto.setRegionCode(regionCode);
        //报警级别
        if (deviceFaultDto.getAlarmLevel() != null && !"".equals(deviceFaultDto.getAlarmLevel())) {

            String[] AlarmLevels = deviceFaultDto.getAlarmLevel().split(",");
            deviceFaultDto.setAlarmLevels(AlarmLevels);
        }
        //处理状态
        if (deviceFaultDto.getStatus() != null) {

            String[] statuses = deviceFaultDto.getStatus().split(",");
            deviceFaultDto.setStatuses(statuses);
        }

        // APP想要的排序
        if("1".equals(deviceFaultDto.getIsApp()) && deviceFaultDto.getStatuses()!=null)
        {
            //包含 6 和 8 的话，按照排序来排
            List<String> list=Arrays.asList(deviceFaultDto.getStatuses());
            if(list.contains(FlowRunStatus.REVIEW_PENDING.getValue().toString())&&list.contains(FlowRunStatus.REVIEW_PASSED.getValue().toString()))
            {
                deviceFaultDto.setIsAppOrderType1("1");
            }
        }
        PageHelper.startPage(pageInfo.getPageNum(), pageInfo.getPageSize());
        List<DeviceFaultVo> list = flowRunMapper.findUndoneList(deviceFaultDto);
        PageInfo<DeviceFaultVo> info = new PageInfo<>(list);
        List<DeviceFaultVo> voList = new ArrayList<>();
        for (DeviceFaultVo vo : list) {
            if (vo != null) {
                if (vo.getAssigner() != null && !"".equals(vo.getAssigner())) {
                    ResponseBean<User> user1 = userServiceClient.user(vo.getAssigner());
                    if (user1.getCode() == 200 && user1.getData()!= null) {
                        vo.setAssigner(user1.getData().getName());
                    }
                }
                //设置等待时长
                vo.setIntervalTime(formatDateTime(vo.getIntervalTime()));
                //设置工单处理时长
                vo.setRepairTime(formatDateTime(vo.getRepairTime()));
            }
            //设置处理人以及处理时间
            FlowLogVo logVos = flowRunLogMapper.findUserByRunId(vo.getId());
            if (logVos != null && logVos.getUserId() != null) {
                ResponseBean<User> user1 = userServiceClient.user(logVos.getUserId());
                if (user1.getCode() == 200 && user1.getData()!=null) {
                    logVos.setHandler(user1.getData().getName());
                }
                vo.setChildren(logVos);
            }

            voList.add(vo);
        }

        info.setList(voList);
        return info;
    }



    /**
     * 获取维护流程状态
     *
     * @param id
     * @return
     */
    public Map findFlowStatus(String id) {
        return flowRunMapper.findFlowStatus(id);
    }

    /**
     * 工单处理
     *
     * @param workOrderDto
     */
    @Transactional
    public void handleWorkOrder(WorkOrderDto workOrderDto) {
        ResponseBean<UserInfoDto> info = userServiceClient.info();
        String userId = "";
        if (info.getCode() == 200) {
            userId = info.getData().getId();
        } else {
            throw new CommonException("获取不到用户信息，请重试");
        }

        //加载工单
        FlowRun flowRun = null;
        try {
            flowRun = this.get(workOrderDto.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(flowRun == null){
            throw new CommonException("无该工单。");
        }

        // 校验工单是否可审核
        String roles = RoleContextHolder.getRole();
        if((workOrderDto.getStatus() == FlowRunStatus.REVIEW_NOT_PASS.getValue() || workOrderDto.getStatus() == FlowRunStatus.REVIEW_PASSED.getValue())) {  //审核处理
            if(flowRun.getRunStatus() != FlowRunStatus.REVIEW_PENDING.getValue()){  //若工单不是待审核状态，则不可审核
                throw new CommonException("该工单不在待审核状态。");
            }else if(!roles.contains(RoleEnum.ROLE_SYS_ADMIN.getCode()) && !roles.contains(RoleEnum.ROLE_PROJECT_ADMIN.getCode())){ //若不是 租户系统管理员或项目管理员，则无限权审核
                throw new CommonException("没有权限审核。");
            }
        }

        //修改工单实例对象
        flowRun.setRunStatus(workOrderDto.getStatus());
        flowRun.setEstimatedTime(workOrderDto.getEstimatedTime());
//        if (workOrderDto.getStatus() == 1) {
        if (workOrderDto.getStatus() == FlowRunStatus.MAINTENANCE_COMPLETED.getValue() || workOrderDto.getStatus() == FlowRunStatus.REVIEW_PASSED.getValue() ) {

            flowRun.setEndTime((int) (System.currentTimeMillis() / 1000));
            DeviceEventAlarmVo alarm = deviceEventAlarmMapper.findAlarmByDeviceId(workOrderDto.getDeviceId());

            if (alarm.getAlarmTypes().contains(workOrderDto.getAlarmType())) {
                DeviceEventAlarmDto eventAlarmDto = new DeviceEventAlarmDto();
                // TODO 处理报警类型
                String[] alarms = alarm.getAlarmTypes().split(",");
                int i = getIndex(alarms, workOrderDto.getAlarmType());
                List<String> list1 = Arrays.asList(alarms);
                List<String> arrList1 = new ArrayList<String>(list1);
                arrList1.remove(i);
                String newAlarms = StringUtils.join(arrList1, ",");

                // TODO 处理报警类型中文描述
                String[] alarmTypesDes = alarm.getAlarmTypesDes().split(";");
                List<String> list2 = Arrays.asList(alarmTypesDes);
                List<String> arrList2 = new ArrayList<String>(list2);
                arrList2.remove(i);
                String newAlarmDes = StringUtils.join(arrList2, ";");

                // TODO 处理报警级别
                String[] alarmLevels = alarm.getAlarmLevels().split(";");
                List<String> list3 = Arrays.asList(alarmLevels);
                List<String> arrList3 = new ArrayList<String>(list3);
                arrList3.remove(i);
                String newAlarmLevels = StringUtils.join(arrList3, ";");

                // TODO 处理报警时间
                String[] alarmDates = alarm.getAlarmDates().split(";");
                List<String> list4 = Arrays.asList(alarmDates);
                List<String> arrList4 = new ArrayList<String>(list4);
                arrList4.remove(i);
                String newAlarmDates = StringUtils.join(arrList4, ";");

                // TODO 处理事件id
                String[] eventIds = alarm.getEventIds().split(";");
                List<String> list5 = Arrays.asList(eventIds);
                List<String> arrList5 = new ArrayList<String>(list5);
                arrList5.remove(i);
                String newEventIds = StringUtils.join(arrList5, ";");


                eventAlarmDto.setDeviceId(workOrderDto.getDeviceId());
                eventAlarmDto.setEventIds(newEventIds);
                eventAlarmDto.setAlarmTypes(newAlarms);
                eventAlarmDto.setAlarmTypeDes(newAlarmDes);
                eventAlarmDto.setAlarmLevels(newAlarmLevels);
                eventAlarmDto.setAlarmDates(newAlarmDates);

                //更新级别
                Integer level = alarmTypeService.loadMinAlarmLevel(arrList1);
                int alarmLevel = level == null ? AlarmLevelType.NORMAL.getType() : level;   //更新后的级别
                eventAlarmDto.setAlarmLevel(alarmLevel);
                eventAlarmDto.setIsAlarm(StringUtils.isEmpty(eventAlarmDto.getAlarmTypes()) ? 0 : 1);
                deviceEventAlarmMapper.updateByDeviceId(eventAlarmDto);
            }
        }
        flowRunMapper.update(flowRun);
        //修改步骤信息
        FlowRunPrcs flowRunPrcs = new FlowRunPrcs();
        flowRunPrcs.setId(workOrderDto.getPrcsId());
        flowRunPrcs.setComment(workOrderDto.getComment());
        flowRunPrcs.setRemark(workOrderDto.getRemark());
        flowRunPrcsMapper.update(flowRunPrcs);

        //添加日志
        if(workOrderDto.getStatus() != FlowRunStatus.CONFIRMED.getValue() && workOrderDto.getStatus()!= FlowRunStatus.MAINTENANCE.getValue() )
        {
            //如果 已确认 和 维修中，就不用加日志
            FlowRunLog flowRunLog = new FlowRunLog();
            flowRunLog.setId(IdGen.snowflakeId());
            flowRunLog.setRunId(workOrderDto.getId());
            flowRunLog.setRunStatus(workOrderDto.getStatus());
            flowRunLog.setUserId(userId);
            flowRunLog.setContent(workOrderDto.getComment());
            flowRunLog.setRemark(workOrderDto.getRemark());
            flowRunLog.setLogTime(new Date());
            flowRunLogMapper.insert(flowRunLog);
        }

        //发送 推送 通知
        //需要手动审核才推送
        if(SendOrderSettingEnum.ORDER_HANDLE_TYPE_MANUAL.getValue() == flowRun.getType().intValue()) {
            Map<String,String> extras=new HashMap<>();
            extras.put(CommonConstant.PushMobilePhoneConstant.BUSINESS_CODE, CommonConstant.PushMobilePhoneConstant.BusinessCodeEnum.WORK_AUDIT.getCode());
            extras.put("workOrderId",workOrderDto.getId());

            // 根据日志 用 run_id 获取最后一个处理的人把状态设为6的人 , 如果没有，就找现在的区域负责人
            FlowLogVo flowLog = flowRunLogMapper.findUserByRunIdAndRunStatus(workOrderDto.getId(),FlowRunStatus.REVIEW_PENDING.getValue());

            // 审核通过或者不通过，都发给区域负责人
            if(flowLog!=null&&StringUtils.isNotEmpty(flowLog.getUserId()))
            {
                if(workOrderDto.getStatus() == FlowRunStatus.REVIEW_PASSED.getValue() )
                {
                    JPushUtils.JPushResult jPushResult = JPushUtils.sendAlias(workOrderDto.getRemark(),"工单处理 已审核通过",extras,timeToLive,flowLog.getUserId());
                    log.info("jPushResult: " + JSON.toJSONString(jPushResult));
                }

                if(workOrderDto.getStatus() == FlowRunStatus.REVIEW_NOT_PASS.getValue())
                {
                    JPushUtils.JPushResult jPushResult = JPushUtils.sendAlias(workOrderDto.getRemark(),"工单处理 审核未通过",extras,timeToLive,flowLog.getUserId());
                    log.info("jPushResult: " + JSON.toJSONString(jPushResult));
                }
            }

            //REVIEW_PENDING 准备给审核人审核的话，就发送通知给审核人
            if(workOrderDto.getStatus() == FlowRunStatus.REVIEW_PENDING.getValue())
            {
                Device device = deviceService.getDeviceVo(flowRun.getBeginDeviceId());
                if(device != null){
                    ResponseBean<List<UserVo>> approveUsers = userServiceClient.getApproveUsers(device.getProjectId());
                    if (approveUsers != null && approveUsers.getStatus() == 200  && approveUsers.getData() != null){
                        List<UserVo> userVoList  = approveUsers.getData();
                        List<String> userIds = userVoList.stream().map(UserVo::getId).collect(Collectors.toList());
                        JPushUtils.JPushResult jPushResult = JPushUtils.sendAlias(workOrderDto.getRemark(), "工单处理 待审核", extras,timeToLive, userIds);
                        log.info("jPushResult: " + JSON.toJSONString(jPushResult));
                    }
                }
            }
        }

    }

    @Transactional
    public int manualCreateWorkOrder(FlowRunDto flowRunDto) {
        List<String> userIds = new ArrayList<>();
        List<String> userPhones = new ArrayList<>();
        Device device = deviceService.getDeviceVo(flowRunDto.getBeginDeviceId());
        if(device == null){
            throw new CommonException("找不到对应设备。");
        }

        AlarmOrderHandleVo alarmOrderHandleVo = alarmOrderHandleService.findByProjectId(device.getProjectId()); //加载指定项目的派单配置
        ResponseBean<List<User>> u = userServiceClient.findSendNoticeUsers(device.getProjectId()); // 获取发送通知的用户
        if (ResponseBean.SUCCESS == u.getCode()){
            List<User> data = u.getData();
            for (User user :data){
                if ((user.getAppNotice() == null && alarmOrderHandleVo.getAppNotice() == 1) || (user.getAppNotice() != null && user.getAppNotice() == 1)){
                    userIds.add(user.getId());
                }
                if ((user.getSmsNotice() == null && alarmOrderHandleVo.getSmsNotice() == 1) || (user.getSmsNotice() != null && user.getSmsNotice() == 1)){
                    userPhones.add(user.getPhone());
                }
            }
        }
        FlowRun flowRun = new FlowRun();
        flowRun.setRunName(flowRunDto.getRunName());
        flowRun.setPriority(flowRunDto.getPriority());
        try {
            flowRun.setAlarmTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(flowRunDto.getAlarmTime()));
        } catch (ParseException e) {
            log.error("报警时间转换异常error --> {}", e.getMessage());
            e.printStackTrace();
        }
        flowRun.setType(alarmOrderHandleVo != null ? alarmOrderHandleVo.getOrderHandleType() : SendOrderSettingEnum.ORDER_HANDLE_TYPE_MANUAL.getValue());
        flowRun.setRunNo(IdGen.getFlowRunNum(flowRunDto.getAlarmType()));
        flowRun.setBeginUserId(flowRunDto.getBeginUserId());
        flowRun.setBeginDeviceId(flowRunDto.getBeginDeviceId());
        flowRun.setBeginUEventId(flowRunDto.getBeginUEventId());
        flowRun.setBeginTime((int) (System.currentTimeMillis() / 1000));
//        flowRun.setRunStatus(0);
        flowRun.setRunStatus(FlowRunStatus.INIT.getValue());
        flowRun.setAlarmLevel(flowRunDto.getAlarmLevel());
        flowRun.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        flowRun.setAlarmType(flowRunDto.getAlarmType());
        flowRun.setMaintainUserId(flowRunDto.getMaintainUserId());
        flowRun.setRemark(flowRunDto.getRemark());
        flowRun.setAppNotice(flowRunDto.getAppNotice());
        flowRun.setSmsNotice(flowRunDto.getSmsNotice());
        flowRun.setEmailNotice(flowRunDto.getEmailNotice());
        int row = flowRunMapper.insert(flowRun);
        if (row > 0) {
            addFlowRunPrcs(flowRun);
        }

//        TODO 2021-07-11工单通知
        //发送APP通知、短信通知、邮件通知
        if(alarmOrderHandleVo != null){
            int appNotice = alarmOrderHandleVo.getAppNotice();
            int smsNotice = alarmOrderHandleVo.getSmsNotice();
            int emailNotice = alarmOrderHandleVo.getEmailNotice();
            if(appNotice == 1){ //APP通知
                List flowRuns = new ArrayList<>();
                flowRuns.add(flowRun);
                pushOrderAppNotice(device, flowRuns,userIds);
            }
            if(smsNotice == 1){ //短信通知
                List flowRuns = new ArrayList<>();
                flowRuns.add(flowRun);
                pushOrderSmsNotice(device, flowRuns,userPhones);
            }
            if(emailNotice == 1){ //邮件通知

            }
        }
       // TODO 2021-07-23 工单日志
        FlowRunLog flowRunLog = new FlowRunLog();
        flowRunLog.setId(IdGen.snowflakeId());
        flowRunLog.setRunId(flowRun.getId());
        flowRunLog.setRunStatus(flowRun.getRunStatus());
        flowRunLog.setUserId(flowRunDto.getBeginUserId());
        flowRunLog.setContent("派单");
        flowRunLog.setRemark(flowRunDto.getRemark());
        flowRunLog.setLogTime(new Date());
        flowRunLogMapper.insert(flowRunLog);

        return row;
    }

    /**
     * 新增实例步骤批注
     *
     * @param flowRun
     */
    @Transactional
    public void addFlowRunPrcs(FlowRun flowRun) {
        FlowRunPrcs flowRunPrcs = new FlowRunPrcs();
        flowRunPrcs.setCommonValue("admin", SysUtil.getSysCode(), "aswl");
        flowRunPrcs.setRunId(flowRun.getId());
        flowRunPrcs.setTenantCode("aswl");
        flowRunPrcs.setCreateTime(Integer.parseInt(System.currentTimeMillis() / 1000 + ""));
        flowRunPrcsMapper.insert(flowRunPrcs);
    }

    //时长转换
    public String formatDateTime(String time) {
        if (time == null) {
            return "";
        }
        long ss = Long.valueOf(time);
        String intervalTime = null;
        long days = ss / (60 * 60 * 24);
        long hours = (ss % (60 * 60 * 24)) / (60 * 60);
        long minutes = (ss % (60 * 60)) / 60;
        long seconds = ss % 60;
        if (days > 0) {
            intervalTime = days + "天" + hours + "小时" + minutes + "分钟";
        } else if (hours > 0) {
            intervalTime = hours + "小时" + minutes + "分钟";
        } else if (minutes > 0) {
            intervalTime = minutes + "分钟";
        } else {
            intervalTime = "1分钟内";
        }
        return intervalTime;
    }

    @Transactional
    public int batchCreateWorkOrder(List<FlowRunDto> flowRunDtos) {
        List<FlowRun> flowRuns = new ArrayList<>();
        List<String> userIds = new ArrayList();
        List<String> userEmailList = new ArrayList<>();
        List<String> userPhones = new ArrayList<>();
        for (FlowRunDto flowRunDto : flowRunDtos) {
            Device device = deviceService.getDeviceVo(flowRunDto.getBeginDeviceId());
            if(device == null){
                throw new CommonException("找不到对应设备。");
            }
            AlarmOrderHandleVo alarmOrderHandleVo = alarmOrderHandleService.findByProjectId(device.getProjectId()); //加载指定项目的派单配置
            ResponseBean<List<User>> u = userServiceClient.findSendNoticeUsers(device.getProjectId()); // 获取发送通知的用户
            ResponseBean<List<User>> sendEmailUsers = userServiceClient.findSendEmailUsers(device.getProjectId());// 获取项目下邮箱地址不为空的用户
            if (ResponseBean.SUCCESS == u.getCode()){
                List<User> data = u.getData();
                for (User user :data){
                    if ((user.getAppNotice() == null && alarmOrderHandleVo.getAppNotice() == 1) || (user.getAppNotice() != null && user.getAppNotice() == 1)){
                        userIds.add(user.getId());
                    }
                    if ((user.getSmsNotice() == null && alarmOrderHandleVo.getSmsNotice() == 1) || (user.getSmsNotice() != null && user.getSmsNotice() == 1)){
                        userPhones.add(user.getPhone());
                    }
                }
            }
            if (ResponseBean.SUCCESS == sendEmailUsers.getCode()){
                List<User> data = sendEmailUsers.getData();
                for (User user:data) {
                    if (user.getEmail() != null){
                        userEmailList.add(user.getEmail());
                    }
                }
            }
            FlowRun flowRun = new FlowRun();
            flowRun.setRunName(flowRunDto.getRunName());
            flowRun.setPriority(flowRunDto.getPriority());
            try {
                flowRun.setAlarmTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(flowRunDto.getAlarmTime()));
            } catch (ParseException e) {
                log.error("报警时间转换异常error --> {}", e.getMessage());
            }
            flowRun.setType(alarmOrderHandleVo != null ? alarmOrderHandleVo.getOrderHandleType() : SendOrderSettingEnum.ORDER_HANDLE_TYPE_MANUAL.getValue());
            flowRun.setRunNo(IdGen.getFlowRunNum(flowRunDto.getAlarmType()));
            flowRun.setBeginUserId(flowRunDto.getBeginUserId());
            flowRun.setBeginDeviceId(flowRunDto.getBeginDeviceId());
            flowRun.setBeginUEventId(flowRunDto.getBeginUEventId());
            flowRun.setBeginTime((int) (System.currentTimeMillis() / 1000));
//            flowRun.setRunStatus(0);
            flowRun.setRunStatus(FlowRunStatus.INIT.getValue());
            flowRun.setAlarmLevel(flowRunDto.getAlarmLevel());
            flowRun.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
            flowRun.setAlarmType(flowRunDto.getAlarmType());
            flowRun.setMaintainUserId(flowRunDto.getMaintainUserId());
            flowRun.setRemark(flowRunDto.getRemark());
            flowRun.setAppNotice(flowRunDto.getAppNotice());
            flowRun.setSmsNotice(flowRunDto.getSmsNotice());
            flowRun.setEmailNotice(flowRunDto.getEmailNotice());
            flowRuns.add(flowRun);

            // TODO 2021-07-23 工单日志
            FlowRunLog flowRunLog = new FlowRunLog();
            flowRunLog.setId(IdGen.snowflakeId());
            flowRunLog.setRunId(flowRun.getId());
            flowRunLog.setRunStatus(flowRun.getRunStatus());
            flowRunLog.setUserId(flowRunDto.getBeginUserId());
            flowRunLog.setContent("派单");
            flowRunLog.setRemark(flowRunDto.getRemark());
            flowRunLog.setLogTime(new Date());
            flowRunLogMapper.insert(flowRunLog);

           // TODO 2021-11-01工单通知
            //发送APP通知、短信通知、邮件通知
            if(alarmOrderHandleVo != null){
                int appNotice = alarmOrderHandleVo.getAppNotice();
                int smsNotice = alarmOrderHandleVo.getSmsNotice();
                int emailNotice = alarmOrderHandleVo.getEmailNotice();
                if(appNotice == 1){ //APP通知
                    List runList = new ArrayList<>();
                    runList.add(flowRun);
                    if (userIds != null && userIds.size() >0) {
                        pushOrderAppNotice(device, runList, userIds);
                    }
                }
                if(smsNotice == 1){ //短信通知
                    List runList = new ArrayList<>();
                    runList.add(flowRun);
                    if (userPhones != null && userPhones.size() > 0) {
                        pushOrderSmsNotice(device, runList, userPhones);
                    }
                }
                if(emailNotice == 1){ //邮件通知
                    List runList = new ArrayList<>();
                    runList.add(flowRun);
                    if (userEmailList != null && userEmailList.size() > 0) {
                        pushOrderEmailNotice(runList, userEmailList);
                    }
                }
            }
        }
        int row = flowRunMapper.batchCreateWorkOrder(flowRuns);
        if (row > 0) {
            for (FlowRun flowRun : flowRuns) {
                addFlowRunPrcs(flowRun);
            }
        }

        return row;
    }

    /**
     * 撤销报警
     *
     * @param flowRunDtos
     */
    @Transactional
    public int revokeAlarm(List<FlowRunDto> flowRunDtos) {
        int row=0;
        for (int j = 0; j < flowRunDtos.size(); j++) {
            for (FlowRunDto dto: flowRunDtos) {
                DeviceEventAlarmVo alarm = deviceEventAlarmMapper.findAlarmByDeviceId(dto.getBeginDeviceId());
                if (alarm.getAlarmTypes().contains(dto.getAlarmType())) {
                    DeviceEventAlarmDto eventAlarmDto = new DeviceEventAlarmDto();
                    // TODO 处理报警类型
                    String[] alarms = alarm.getAlarmTypes().split(",");
                    int i = getIndex(alarms, dto.getAlarmType());
                    List<String> list1 = Arrays.asList(alarms);
                    List<String> arrList1 = new ArrayList<String>(list1);
                    arrList1.remove(i);
                    String newAlarms = StringUtils.join(arrList1, ",");

                    // TODO 处理报警类型中文描述
                    String[] alarmTypesDes = alarm.getAlarmTypesDes().split(";");
                    List<String> list2 = Arrays.asList(alarmTypesDes);
                    List<String> arrList2 = new ArrayList<String>(list2);
                    arrList2.remove(i);
                    String newAlarmDes = StringUtils.join(arrList2, ";");

                    // TODO 处理报警级别
                    String[] alarmLevels = alarm.getAlarmLevels().split(";");
                    List<String> list3 = Arrays.asList(alarmLevels);
                    List<String> arrList3 = new ArrayList<String>(list3);
                    arrList3.remove(i);
                    String newAlarmLevels = StringUtils.join(arrList3, ";");

                    // TODO 处理报警时间
                    String[] alarmDates = alarm.getAlarmDates().split(";");
                    List<String> list4 = Arrays.asList(alarmDates);
                    List<String> arrList4 = new ArrayList<String>(list4);
                    arrList4.remove(i);
                    String newAlarmDates = StringUtils.join(arrList4, ";");

                    // TODO 处理事件id
                    String[] eventIds = alarm.getEventIds().split(";");
                    List<String> list5 = Arrays.asList(eventIds);
                    List<String> arrList5 = new ArrayList<String>(list5);
                    arrList5.remove(i);
                    String newEventIds = StringUtils.join(arrList5, ";");

                    eventAlarmDto.setDeviceId(dto.getBeginDeviceId());
                    eventAlarmDto.setEventIds(newEventIds);
                    eventAlarmDto.setAlarmTypes(newAlarms);
                    eventAlarmDto.setAlarmTypeDes(newAlarmDes);
                    eventAlarmDto.setAlarmLevels(newAlarmLevels);
                    eventAlarmDto.setAlarmDates(newAlarmDates);

                    //更新级别
                    Integer level = alarmTypeService.loadMinAlarmLevel(arrList1);
                    int alarmLevel = level == null ? AlarmLevelType.NORMAL.getType() : level;   //更新后的级别
                    eventAlarmDto.setAlarmLevel(alarmLevel);
                    eventAlarmDto.setIsAlarm(StringUtils.isEmpty(eventAlarmDto.getAlarmTypes()) ? 0 : 1);
                    deviceEventAlarmMapper.updateByDeviceId(eventAlarmDto);
                    // 修改历史表描述信息
                    DeviceEventAlarm deviceEventAlarm = null;
                    String alarmTime=dto.getAlarmTime().substring(0,7).replace("-","");
                    DeviceEventAlarmVo alarmVo = deviceEventAlarmMapper.findByEventId(dto.getBeginUEventId(),Integer.parseInt(alarmTime));
                    if (alarmVo != null) {
                        deviceEventAlarm = new DeviceEventAlarm();
                        deviceEventAlarm.setAlarmTypesDes(alarmVo.getAlarmTypesDes().replace(dto.getRunName(),dto.getRunName()+"(被撤销)"));
                        deviceEventAlarm.setUEventId(dto.getBeginUEventId());
                        deviceEventAlarm.setRecTime(Integer.parseInt(alarmTime));
                        deviceEventAlarmMapper.updateDeviceEventHisAlarm(deviceEventAlarm);
                        // 写入报警撤销记录表
                        AlarmRevoke revoke = new AlarmRevoke();
                        revoke.setId(IdGen.snowflakeId());
                        revoke.setUEventId(dto.getBeginUEventId());
                        revoke.setDeviceId(dto.getBeginDeviceId());
                        revoke.setAlarmTypes(dto.getAlarmType());
                        revoke.setAlarmTypesDes(dto.getRunName());
                        revoke.setCreator(SysUtil.getUser());
                        revoke.setCreateDate(new Date());
                        revoke.setRemarks(dto.getRemark());
                        alarmRevokeMapper.insert(revoke);
                        row++;
                    }

                    //若为电口、光纤口
                    if(dto.getAlarmType() != null && (dto.getAlarmType().startsWith("RJ45Ext") || dto.getAlarmType().startsWith("FibreOpticalExt"))){
                        EventUcMetadata metadata = eventUcMetadataMapper.findByAlarmType(dto.getAlarmType());
                        if(metadata != null){
                            AlarmType alarmType = eventUcMetadataMapper.findAlarmTypeByMetadataAndDeviceId(metadata, dto.getBeginDeviceId());
                            if(alarmType != null && AlarmKindEnum.Alarm.getCode().equals(alarmType.getKind())){  //若当前为告警，则重置为可用
                                eventUcMetadataMapper.updateDynamicTableValByDeviceId(metadata, dto.getBeginDeviceId(), "-1");
                            }
                        }
                    }
                }
            }
        }
        return row;
    }



    public int getIndex(String[] array, String s) {
        for (int i = 0; i < array.length; i++) {
            if (s.equalsIgnoreCase(array[i])) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 根据考核时段和响应时长查询工单数
     * @param beginTimeStr 考核开始时段
     * @param endTimeStr 考核结束时段
     * @param respondTime 响应时长
     * @param previousMonthStart 上个月的开始时间
     * @param previousMonthEnd 上个月的结束时间
     * @return
     */
    public List<FlowRunVo> findCountByExamineTime(String beginTimeStr, String endTimeStr, Integer respondTime, String previousMonthStart, String previousMonthEnd) {
        return flowRunMapper.findCountByExamineTime(beginTimeStr,endTimeStr,respondTime,previousMonthStart,previousMonthEnd);
    }

    /**
     * 当天某个设备种类产生的工单
     * @param today
     * @param type
     * @return
     */
    public int findTodayCount(String today, String type,String alarmType,String deviceModelId) {
        return flowRunMapper.findTodayCount(today,type,alarmType,deviceModelId);
    }

    /**
     * 当天某个设备种类完成的工单
     * @param today
     * @param type
     * @return
     */
    public List<Map> findTodayFinish(String today, String type) {
        return flowRunMapper.findTodayFinish(today,type);
    }

    /**
     * 各种类时段工单数
     * @param today
     * @param type
     * @return
     */
    public CityRunTimeStatistics findPeriodRun(String today, String type) {
        return flowRunMapper.findPeriodRun(today,type);
    }

    public CityAlarmStatistics getByDateAlarmTypeKind(String beginTime, String type, String alarmType, String deviceModelId) {
        return flowRunMapper.getByDateAlarmTypeKind(beginTime,type,alarmType,deviceModelId);
    }

    /**
     *  APP消息推送
     * @param device
     * @param flowRuns
     * @param userIds
     */
    public void pushOrderAppNotice(Device device, List<FlowRun> flowRuns,List<String> userIds){
        if(CloudCommon.isCloud()){  //云平台
            Map extrasMap = new HashMap();
            extrasMap.put(CommonConstant.PushMobilePhoneConstant.BUSINESS_CODE, CommonConstant.PushMobilePhoneConstant.BusinessCodeEnum.WORK_DETAIL_NOTICE.getCode());
            for (FlowRun flowRun : flowRuns) {
                extrasMap.put("workOrderId",flowRun.getId());
                RegionLeader regionLeader = new RegionLeader();
                regionLeader.setId(flowRun.getMaintainUserId());
                RegionLeader regionLeaderEntity= regionLeaderMapper.get(regionLeader);
                JPushUtils.JPushResult jPushResult = JPushUtils.sendAlias("待处理工单", flowRun.getRunName(), extrasMap, timeToLive,regionLeaderEntity.getUserId());
                log.info("工单详情推送" + (jPushResult.isSuccess() ? "成功！" : "失败！"));
                JPushUtils.JPushResult jPushResult1 = JPushUtils.sendAlias("待处理工单", flowRun.getRunName(), extrasMap, timeToLive,userIds);
                log.info("工单详情推送" + (jPushResult1.isSuccess() ? "成功！" : "失败！"));
            }
        }else{  //内网
            DtuBodyBean dtuBodyBean = new DtuBodyBean();
            dtuBodyBean.setFlag(DtuPushConstant.DtuFlagEnum.PUSH_APP_WORK_ORDER.getFlag());
            dtuBodyBean.setData(flowRuns);
            dtuBodyBean.setIp(device.getIp());
            dtuBodyBean.setProjectCode(projectCode);
            amqpTemplate.convertAndSend(MQConstants.MESSAGE_EXCHANGE,MQConstants.SEND_DTU_QUEUE, JSON.toJSONString(dtuBodyBean, SerializerFeature.WriteDateUseDateFormat));
        }
    }

    /**
     * 发送短信
     * @param device
     * @param flowRuns
     * @param userPhone
     */
    public void pushOrderSmsNotice(Device device, List<FlowRun> flowRuns,List<String> userPhone){
        if(CloudCommon.isCloud()){  //云平台
            for (FlowRun flowRun : flowRuns) {
                SmsDto smsDto = new SmsDto();
                smsDto.setReceiver(StringUtils.join(userPhone,","));
                smsDto.setContent(flowRun.getRunNo());
                smsDto.setSignName("傲视物联");
                smsDto.setTemplateCode(orderTemplateCode);
                ResponseBean<SmsResponse> s = userServiceClient.sendSms(smsDto);
                if (ResponseBean.SUCCESS == s.getCode()){
                    log.info("发送短信成功，返回内容：{}", s.getData());
                }
            }
        }else{  //内网
           
        }
    }


    /**
     * 发送邮件
     * @param flowRuns
     * @param emailList
     */
    public void pushOrderEmailNotice(List<FlowRun> flowRuns, List<String> emailList) {
        if (CloudCommon.isCloud()) {  //云平台
            for (FlowRun flowRun : flowRuns) {
                String content = "尊敬的用户，您有一个工单，工单号："+ flowRun.getRunNo() + "，故障类型为："+flowRun.getRunName()+ "，" +"请尽快处理";
                mailService.sendMail(emailList,"工单信息",content,false);
            }
            
        } else{  //内网

        }
    }


    /**
     * 获取工单管理--(待处理/处理完成列表)
     *
     * @return PageInfo
     */
    public PageInfo<DeviceFaultVo> findWorkOrderList(PageInfo<DeviceFaultDto> pageInfo, DeviceFaultDto deviceFaultDto) {
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
            if (regionCode == null || "".equals(regionCode)) {
                String userRegionCode = RegionCodeContextHolder.getRegionCode();
                if (userRegionCode == null || "".equals(userRegionCode)) {
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
        //报警级别
        if (deviceFaultDto.getAlarmLevel() != null && !"".equals(deviceFaultDto.getAlarmLevel())) {

            String[] AlarmLevels = deviceFaultDto.getAlarmLevel().split(",");
            deviceFaultDto.setAlarmLevels(AlarmLevels);
        }
        //处理状态
        if (deviceFaultDto.getStatus() != null) {

            String[] statuses = deviceFaultDto.getStatus().split(",");
            deviceFaultDto.setStatuses(statuses);
        }
        PageHelper.startPage(pageInfo.getPageNum(), pageInfo.getPageSize());
        List<DeviceFaultVo> list = flowRunMapper.findWorkOrderList(deviceFaultDto);
        PageInfo<DeviceFaultVo> info = new PageInfo<>(list);
        List<DeviceFaultVo> voList = new ArrayList<>();
        for (DeviceFaultVo vo : list) {
            if (vo != null) {
                if (vo.getAssigner() != null && !"".equals(vo.getAssigner())) {
                    ResponseBean<User> user1 = userServiceClient.user(vo.getAssigner());
                    if (user1.getCode() == 200 && user1.getData()!= null) {
                        vo.setAssigner(user1.getData().getName());
                    }
                }
                //设置等待时长
                vo.setIntervalTime(formatDateTime(vo.getIntervalTime()));
                //设置工单处理时长
                vo.setRepairTime(formatDateTime(vo.getRepairTime()));
            }
            //设置处理人以及处理时间
            FlowLogVo logVos = flowRunLogMapper.findUserByRunId(vo.getId());
            if (logVos != null && logVos.getUserId() != null) {
                ResponseBean<User> user1 = userServiceClient.user(logVos.getUserId());
                if (user1.getCode() == 200 && user1.getData()!=null) {
                    logVos.setHandler(user1.getData().getName());
                }
                vo.setChildren(logVos);
            }

            voList.add(vo);
        }

        info.setList(voList);
        return info;
    }

    /**
     * 根据状态查询工单数量
     * @param deviceFaultDto
     * @return list
     */
    public Map findWorkOrderCount(DeviceFaultDto deviceFaultDto){
        String roles = RoleContextHolder.getRole();
        String regionCode = deviceFaultDto.getRegionCode();
        String tenantCode = SysUtil.getTenantCode();
        String projectId = SysUtil.getProjectId();
        String queryProjectId = deviceFaultDto.getQueryProjectId();
        if (StringUtils.isNotEmpty(queryProjectId)){
            projectId = queryProjectId;
        }
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
        Map map =new HashMap();
        String  maintainUserId =deviceFaultDto.getMaintainUserId();
        String allOrder = deviceFaultDto.getAllOrder();
        Map notFinishCount = null;
        Map confirmCount=null;
        Map noRepairCount = null;
        Map waitApprovalCount = null;
        Map finishCount= null;
        if ("1".equals(allOrder)) {  //所有的工单
            //未完成
            notFinishCount = flowRunMapper.findWorkOrderCount(maintainUserId,projectId, regionCode, "0,2,3,4,6,7", tenantCode);
            //已确认
            confirmCount =flowRunMapper.findWorkOrderCount(maintainUserId,projectId, regionCode, "2", tenantCode);
            //未修复
            noRepairCount = flowRunMapper.findWorkOrderCount(maintainUserId,projectId, regionCode, "4", tenantCode);
            //待审核
            waitApprovalCount = flowRunMapper.findWorkOrderCount(maintainUserId,projectId, regionCode, "6", tenantCode);
            //已完成
            finishCount = flowRunMapper.findWorkOrderCount(maintainUserId,projectId, regionCode, "1,8", tenantCode);
        }else if("0".equals(allOrder)) {//我的工单数

            notFinishCount = flowRunMapper.myWorkOrderCount(maintainUserId,projectId, regionCode, "0,2,3,4,6,7", tenantCode);

            confirmCount =flowRunMapper.myWorkOrderCount(maintainUserId,projectId, regionCode, "2", tenantCode);
            //未修复
            noRepairCount = flowRunMapper.myWorkOrderCount(maintainUserId,projectId, regionCode, "4", tenantCode);
            //待审核
            waitApprovalCount = flowRunMapper.myWorkOrderCount(maintainUserId,projectId, regionCode, "6", tenantCode);
            //已完成
            finishCount = flowRunMapper.myWorkOrderCount(maintainUserId,projectId, regionCode, "1,8", tenantCode);

        }else{
            notFinishCount = flowRunMapper.myWorkOrderCount(maintainUserId,projectId, regionCode, "0,2,3,4,6,7", tenantCode);

            confirmCount =flowRunMapper.myWorkOrderCount(maintainUserId,projectId, regionCode, "2", tenantCode);
            //未修复
            noRepairCount = flowRunMapper.myWorkOrderCount(maintainUserId,projectId, regionCode, "4", tenantCode);
            //待审核
            waitApprovalCount = flowRunMapper.myWorkOrderCount(maintainUserId,projectId, regionCode, "6", tenantCode);
            //已完成
            finishCount = flowRunMapper.myWorkOrderCount(maintainUserId,projectId, regionCode, "1,8", tenantCode);
        }
        map.put("notFinishCount",notFinishCount.get("count"));
        map.put("confirmCount",confirmCount.get("count"));
        map.put("noRepairCount",noRepairCount.get("count"));
        map.put("waitApprovalCount",waitApprovalCount.get("count"));
        map.put("finishCount",finishCount.get("count"));
        return map;
    }

    /**
     * 根据工单ID获取工单详情
     * @param id 
     * @return FlowRunVo
     */
    @Transactional
    public FlowRunVo findWorkOrderDetails(String id) {
        FlowRunVo flowRunVo = flowRunMapper.findFlowRunById(id);
        if (flowRunVo != null) {
            if (flowRunVo.getBeginUserId() != null && !"".equals(flowRunVo.getBeginUserId())) {
                ResponseBean<User> user = userServiceClient.user(flowRunVo.getBeginUserId());
                if (user.getCode() == 200 && user.getData() != null) {
                    // 设置派单人员名称/电话
                    flowRunVo.setDispatchUserName(user.getData().getName());
                    flowRunVo.setDispatchUserPhone(user.getData().getPhone());
                }
                // 审核人员
                ResponseBean<List<UserVo>> approveUsers = userServiceClient.getApproveUsers(flowRunVo.getProjectId());
                if (approveUsers.getStatus() == 200  && approveUsers.getData() != null){
                    flowRunVo.setApprovalUserList(approveUsers.getData());
                }
            }
        }
        return flowRunVo;
    }

    /**
     *  已完成工单，同时被处理的工单接口
     * @param beginUserId  
     * @param endTime
     * @param id
     * @return list
     */
    public List<FlowRunVo> getHandledOrder(String beginUserId,Integer endTime,String id){
        return flowRunMapper.getHandledOrder(beginUserId,endTime,id);
    }

    /**
     * 根据状态查询工单数量
     * @param deviceFaultDto
     * @return list
     */
    public Map findTabOrderCount(DeviceFaultDto deviceFaultDto){
        // 获取登录用户信息
        ResponseBean<UserInfoDto> info = userServiceClient.info();
        String userId;
        if (info.getCode() == 200) {
            userId = info.getData().getMaintainUserId();
        }
        else
        {
            throw new CommonException("获取不到用户信息，请重试");
        }
        Map map =new HashMap();
        String projectId =deviceFaultDto.getProjectId();
        String regionCode =deviceFaultDto.getRegionCode();
        String tenantCode =deviceFaultDto.getTenantCode();
        //全部工单数量(未完成)
        Map pendingCount = flowRunMapper.findWorkOrderCount("",projectId, regionCode, "0,2,3,4,6,7", tenantCode);
        //我的工单数
        Map myOrderCount = flowRunMapper.myWorkOrderCount(userId,projectId, regionCode, "0,2,3,4,6,7", tenantCode);
        //待我审核
        Map examineCount = flowRunMapper.getExamineOrderCount(projectId, regionCode, "6", tenantCode);
        map.put("pendingCount",pendingCount.get("count"));
        map.put("myOrderCount",myOrderCount.get("count"));
        map.put("examineCount",examineCount.get("count"));
        return map;
    }

    /**
     * 故障上报
     * @param flowRunDto
     * @return int
     */
    public int insertFaultReport(FlowRunDto flowRunDto){
        FlowRun flowRun = new FlowRun();
        flowRun.setRunName(flowRunDto.getRunName());
        flowRun.setPriority(flowRunDto.getPriority());
        try {
            flowRun.setAlarmTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(flowRunDto.getAlarmTime()));
        } catch (ParseException e) {
            log.error("报警时间转换异常error --> {}", e.getMessage());
            e.printStackTrace();
        }
        flowRun.setType(SendOrderSettingEnum.ORDER_HANDLE_TYPE_MANUAL.getValue());
        flowRun.setRunNo(IdGen.getFlowRunNum("OTHER"));
        flowRun.setBeginUserId(flowRunDto.getBeginUserId());
        flowRun.setBeginDeviceId(flowRunDto.getBeginDeviceId());
        flowRun.setBeginUEventId(flowRunDto.getBeginUEventId());
        flowRun.setBeginTime((int) (System.currentTimeMillis() / 1000));
        flowRun.setRunStatus(FlowRunStatus.INIT.getValue());
        flowRun.setAlarmLevel(flowRunDto.getAlarmLevel());
        flowRun.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        flowRun.setAlarmType(flowRunDto.getAlarmType());
        flowRun.setMaintainUserId(flowRunDto.getMaintainUserId());
        flowRun.setRemark(flowRunDto.getRemark());
        flowRun.setAppNotice(flowRunDto.getAppNotice());
        flowRun.setSmsNotice(flowRunDto.getSmsNotice());
        flowRun.setEmailNotice(flowRunDto.getEmailNotice());
        int row = flowRunMapper.insert(flowRun);
        if (row > 0) {
            addFlowRunPrcs(flowRun);
        }
        // 工单日志
        FlowRunLog flowRunLog = new FlowRunLog();
        flowRunLog.setId(IdGen.snowflakeId());
        flowRunLog.setRunId(flowRun.getId());
        flowRunLog.setRunStatus(flowRun.getRunStatus());
        flowRunLog.setUserId(flowRunDto.getBeginUserId());
        flowRunLog.setContent("上报");
        flowRunLog.setRemark(flowRunDto.getRemark());
        flowRunLog.setLogTime(new Date());
        flowRunLogMapper.insert(flowRunLog);
        return row;
    }
 }