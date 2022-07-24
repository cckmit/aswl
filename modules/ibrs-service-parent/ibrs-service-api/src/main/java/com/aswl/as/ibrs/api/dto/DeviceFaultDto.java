package com.aswl.as.ibrs.api.dto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author jk
 * @version 1.0.0
 * @create 2019/10/26 20:01
 */
@Getter@Setter
public class DeviceFaultDto {
    @ApiModelProperty(value = "工单id",name="id")
    private String id;
    @ApiModelProperty(value = "设备id",name="deviceId")
    private String deviceId;
    @ApiModelProperty(value = "工单标题",name="title")
    private String title;
    @ApiModelProperty(value = "工单流水号",name="runNo")
    private String runNo;
    @ApiModelProperty(value = "维修人员",name="repairer")
    private String repairer;
    @ApiModelProperty(value = "区域名称",name="regionName")
    private String regionName;
    @ApiModelProperty(value = "设备名称",name="deviceName")
    private String deviceName;
    @ApiModelProperty(value = "设备种类",name="kind")
    private String kind;
    @ApiModelProperty(value = "开始时间",name="startTime")
    private String startTime;
    @ApiModelProperty(value = "结束时间",name="endTime")
    private String endTime;
    @ApiModelProperty(value = "报警级别",name="alarmLevel")
    private String alarmLevel;
    private String[] alarmLevels;
    @ApiModelProperty(value = "区域编码",name="regionCode")
    private String regionCode;
    @ApiModelProperty(value = "设备报警类型",name="alarmType")
    private String alarmType;
    @ApiModelProperty(value = "工单类型",name="type")
    private Integer type;
    @ApiModelProperty(value = "模糊查询条件",name="query")
    private String query;
    @ApiModelProperty(value = "事件id",name="eventId")
    private String eventId;
    /**
     * 0 --> 未处理
     * 1 --> 已修复
     * 2 --> 未修复
     */
    @ApiModelProperty(value = "工单状态",name="status")
    private String status;
    private String[] statuses;

    //------------------------ 下面是只用来查询和传输 ----------------------
    @ApiModelProperty(value = "租户编码",name="tenantCode")
    private String tenantCode;

    @ApiModelProperty(value = "项目ID",name="projectId")
    private String projectId;

    @ApiModelProperty(value = "是否手机端传过来的",name="isApp")
    private String isApp;

    @ApiModelProperty(value = "手机端特殊的列表排序（手机端说包含 FlowRunStatus.REVIEW_PENDING(6) 和 FlowRunStatus.REVIEW_PASSED(8) 状态的，都要 6排前面 ）",name="isAppOrderType1")
    private String isAppOrderType1;

    @ApiModelProperty(value = "网站专门的条件,这个是想如果勾选多个，就只能显示选中多个的东西",name="htmlSelectType1")
    private String htmlSelectType1;

    @ApiModelProperty(value = "项目过滤",name="queryProjectId")
    private String queryProjectId;
    
    @ApiModelProperty(value = "维护人员ID")
    private String maintainUserId;

    @ApiModelProperty(value = "离线标识")
    private String offlineFlag;

    @ApiModelProperty(value = "经度",name = "longitude")
    private String longitude;

    @ApiModelProperty(value = "维度",name = "latitude")
    private String latitude;

    @ApiModelProperty(value = "排序标识")
    private String order;

    @ApiModelProperty(value = "设备状态筛选条件")
    private String deviceStatus;

    @ApiModelProperty(value = "全部工单还是我的工单,1:全部工单,0:我的工单")
    private String allOrder;

}
