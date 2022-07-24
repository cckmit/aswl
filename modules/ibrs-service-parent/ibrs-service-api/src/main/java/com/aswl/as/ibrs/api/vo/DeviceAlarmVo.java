package com.aswl.as.ibrs.api.vo;

import com.aswl.as.common.core.persistence.TreeEntity;
import com.aswl.as.ibrs.api.module.AlarmCapture;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author jk
 * @version 1.0.0
 * @create 2019/10/24 14:12
 */
@ApiModel(value = "DeviceAlarmVo",description = "设备报警Vo")
@Getter@Setter
public class DeviceAlarmVo implements Serializable{

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "设备id",name = "id")
    private String id;
    @ApiModelProperty(value = "设备上级id",name = "parentId")
    private String parentId;
    @ApiModelProperty(value = "设备名称",name = "deviceName")
    private String deviceName;
    @ApiModelProperty(value = "区域编码",name = "deviceCode")
    private String deviceCode;
    @ApiModelProperty(value = "区域ID",name = "regionId")
    private String regionId;
    @ApiModelProperty(value = "设备编码",name = "regionCode")
    private String regionCode;
    @ApiModelProperty(value = "设备ip",name = "ip")
    private String ip;
    @ApiModelProperty(value = "区域名称",name = "regionName")
    private String regionName;
    @ApiModelProperty(value = "经度",name = "latitude")
    private BigDecimal latitude;
    @ApiModelProperty(value = "纬度",name = "longitude")
    private BigDecimal longitude;
    @ApiModelProperty(value = "设备种类/类型/型号名称",name = "deviceModelKind")
    private String deviceModelKind;
    @ApiModelProperty(value = "设备类型",name = "type")
    private Integer type;
    @ApiModelProperty(value = "设备报警时间",name = "alarmTime")
    private String alarmTime;
    @ApiModelProperty(value = "设备报警时间",name = "alarmTime")
    private String recTime;
    @ApiModelProperty(value = "报警间隔时长",name = "intervalTime")
    private String intervalTime;
    @ApiModelProperty(value = "区域负责人",name = "regionLeader")
    private String regionLeader;
    @ApiModelProperty(value = "设备报警级别",name = "alarmLevel")
    private String alarmLevel;
    @ApiModelProperty(value = "设备报警级别名称",name = "alarmLevelName")
    private String alarmLevelName;
    @ApiModelProperty(value = "设备报警级别英文名称",name = "alarmLevelNameEn")
    private String alarmLevelNameEn;
    @ApiModelProperty(value = "报警类型名称",name = "alarmTypeName")
    private String alarmTypeName;
    @ApiModelProperty(value = "报警类型英文",name = "alarmTypeNameEn")
    private String alarmTypeNameEn;
    @ApiModelProperty(value = "报警类型",name = "alarmTypes")
    private String alarmTypes;
    @ApiModelProperty(value = "报警类型",name = "alarmTypes")
    private Integer isManual;
    @ApiModelProperty(value = "序号",name = "num")
    private Integer num;
    @ApiModelProperty(value = "地址",name = "address")
    private String address;
    @ApiModelProperty(value = "网络状态",name = "isOnline")
    private Integer isOnline;
    @ApiModelProperty(value = "网络状态",name = "isOnline")
    private Integer isVideo;
    @ApiModelProperty(value = "事件唯一id",name = "eventId")
    private String eventId;
    @ApiModelProperty(value = "工单id",name = "runId")
    private String runId;
    @ApiModelProperty(value = "工单号",name = "runNo")
    private String runNo;
    @ApiModelProperty(value = "工单状态",name = "status")
    private Integer status;
    private List<DeviceAlarmVo> children = new ArrayList<>();
    @ApiModelProperty(value = "告警级别集",name = "alarmLevelNames")
    private String alarmLevelNames;
    @ApiModelProperty(value = "告警时间集",name = "alarmTimes")
    private String alarmTimes;
    @ApiModelProperty(value = "正常的类型集",name = "promptTypes")
    private String promptTypes;
    @ApiModelProperty(value = "型号图片",name = "picUrl")
    private String picUrl;
    @ApiModelProperty(value = "设备型号名",name = "deviceModelName")
    private String deviceModelName;
    @ApiModelProperty(value = "设备种类名",name = "deviceKindName")
    private String deviceKindName;
    @ApiModelProperty(value = "设备类型名",name = "deviceTypeName")
    private String deviceTypeName;
    @ApiModelProperty(value = "0:非调试,1:调试中",name = "debug")
    private Integer debug;
    @ApiModelProperty(value = "告警次数",name = "alarmlCount")
    private Integer alarmlCount;
    @ApiModelProperty(value = "故障类型",name = "faultType")
    private String  faultType;
    @ApiModelProperty(value = "报警类型中文描述",name = "alarmTypesDes")
    private String  alarmTypesDes;
    @ApiModelProperty(value = "报警时间集合",name = "alarmDates")
    private String  alarmDates;
    @ApiModelProperty(value = "派单状态/0:未派单 1:已派单 2:已恢复",name = "dispatchOrderStatus")
    private String  dispatchOrderStatus;
    @ApiModelProperty(value = "项目名称",name = "projectName")
    private String projectName;
    @ApiModelProperty(value = "项目ID",name = "projectId")
    private String projectId;
    @ApiModelProperty(value = "元数据关联ID",name = "relatedId")
    private String relatedId;

    /**
     * 设备告警抓拍集
     */
    private List<AlarmCapture> alarmCaptures;

    @ApiModelProperty(value = "统一事件Id",name = "uEventId")
    private String uEventId;
    @ApiModelProperty(value = "距离",name = "juli")
    private String juli;
    @ApiModelProperty(value = "电源口",name = "parentDcpowerNo")
    private String parentDcpowerNo;
    @ApiModelProperty(value = "网络口",name = "parentRj45No")
    private String parentRj45No;
    @ApiModelProperty(value = "光纤口",name = "parentFibreOpticalNo")
    private String parentFibreOpticalNo;
    @ApiModelProperty(value = "设备种类",name = "deviceKind")
    private String deviceKind;
    @ApiModelProperty(value = "调试天数",name = "debugDuration")
    private Integer debugDuration;
    @ApiModelProperty(value = "调试截止日期",name = "debugDeadline")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date debugDeadline;
    @ApiModelProperty(value = "网络口数量",name = "rj45Number")
    private Integer rj45Number;
    @ApiModelProperty(value = "网络口信息",name = "deviceRJ45InfoVo")
    private DeviceRJ45InfoVo deviceRJ45InfoVo;
}
