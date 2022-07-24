package com.aswl.as.ibrs.api.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 设备Vo
 *
 * @author dingfei
 * @date 2019-09-27 14:12
 */
@Data
public class DeviceVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @ApiModelProperty(value = "主键ID",name = "id")
    private String id;

    /**
     * 设备种类ID
     */

    @ApiModelProperty(value = "设备种类ID",name = "deviceKindId")
    private String deviceKindId;


    /**
     * 设备种类名称
     */

    @ApiModelProperty(value = "设备种类名称",name = "deviceKindName")
    private String deviceKindName;

    /**
     * 设备类型ID
     */

    @ApiModelProperty(value = "设备类型ID",name = "deviceTypeId")
    private String deviceTypeId;


    /**
     * 设备类型
     */

    @ApiModelProperty(value = "设备类型",name = "deviceType")
    private String deviceType;

    /**
     * 设备类型名称
     */

    @ApiModelProperty(value = "设备类型名称",name = "deviceTypeName")
    private String deviceTypeName;

    /**
     * 连接类型
     */

    @ApiModelProperty(value = "连接类型",name = "connectType")
    private String connectType;

    /**
     * 设备型号
     */

    @ApiModelProperty(value = "设备型号",name = "deviceModelId")
    private String deviceModelId;

    /**
     * 设备型号名称
     */

    @ApiModelProperty(value = "设备型号名称",name = "deviceModelName")
    private String deviceModelName;

    /**
     * 编码
     */

    @ApiModelProperty(value = "编码",name = "deviceCode")
    private String deviceCode;
    /**
     * 名称
     */

    @ApiModelProperty(value = "名称",name = "deviceName")
    private String deviceName;
    /**
     * 地址
     */
    @ApiModelProperty(value = "地址",name = "address")
    private String address;
    /**
     * 网络口
     */

    @ApiModelProperty(value = "网络口",name = "rj45No")
    private Integer rj45No;
    /**
     * 光纤口
     */

    @ApiModelProperty(value = "",name = "")
    private Integer fibreOpticalNo;
    /**
     * IP
     */

    @ApiModelProperty(value = "IP",name = "ip")
    private String ip;
    /**
     * 端口
     */

    @ApiModelProperty(value = "端口",name = "port")
    private Integer port;
    /**
     * MAC地址
     */
    @ApiModelProperty(value = "MAC地址",name = "mac")
    private String mac;
    /**
     * 维度
     */
    @ApiModelProperty(value = "维度",name = "latitude")
    private Double latitude;
    /**
     * 维度A
     */

    @ApiModelProperty(value = "维度A",name = "latitudeA")
    private Double latitudeA;
    /**
     * 经度
     */

    @ApiModelProperty(value = "经度",name = "longitude")
    private Double longitude;
    /**
     * 经度A
     */

    @ApiModelProperty(value = "经度A",name = "longitudeA")
    private Double longitudeA;
    /**
     * 上级220V电源口
     */

    @ApiModelProperty(value = "上级220V电源口",name = "parentAcpowerNo")
    private Integer parentAcpowerNo;
    /**
     * 上级直流电源口
     */

    @ApiModelProperty(value = "上级直流电源口",name = "parentDcpowerNo")
    private Integer parentDcpowerNo;
    /**
     * 上级光纤口
     */

    @ApiModelProperty(value = "上级光纤口",name = "parentFibreOpticalNo")
    private Integer parentFibreOpticalNo;
    /**
     * 上级网络口
     */

    @ApiModelProperty(value = "上级网络口",name = "parentRj45No")
    private Integer parentRj45No;
    /**
     * 上级槽位
     */

    @ApiModelProperty(value = "上级槽位",name = "parentSlotO")
    private Integer parentSlotO;
    /**
     * 上级设备ID
     */

    @ApiModelProperty(value = "上级设备ID",name = "parentDeviceId")
    private String parentDeviceId;
    /**
     * 地区ID
     */

    @ApiModelProperty(value = "地区ID",name = "regionId")
    private String regionId;

    /**
     * 地区编码
     */

    @ApiModelProperty(value = "地区编码",name = "regionCode")
    private String regionCode;

    /**
     * 地区名称
     */

    @ApiModelProperty(value = "地区名称",name = "regionName")
    private String regionName;

    /**
     * 区域全称
     */
    @ApiModelProperty(value = "区域全称",name = "fullName")
    private String fullName;

    /**
     * 告警级别
     */

    @ApiModelProperty(value = "告警级别",name = "alarmLevel")
    private Integer alarmLevel;

    /**
     * 报警类型集合
     */

    @ApiModelProperty(value = "报警类型集合",name = "alarmTypes")
    private String alarmTypes;

    /**
     * 报警类型中文描述
     */

    @ApiModelProperty(value = "报警类型中文描述",name = "alarmTypesDes")
    private String alarmTypesDes;


    /**
     * 用户名
     */

    @ApiModelProperty(value = "用户名",name = "userName")
    private String userName;
    /**
     * 密码
     */

    @ApiModelProperty(value = "密码",name = "password")
    private String password;
    /**
     * 购买日期
     */

    @ApiModelProperty(value = "购买日期",name = "purchaseDate")
    private String purchaseDate;

    /**
     * 出产日期
     */
    @ApiModelProperty(value = "出产日期",name = "produceDate")
    private String produceDate;

    /**
     * 保修期
     */

    @ApiModelProperty(value = "保修期",name = "guaranteeTime")
    private Integer guaranteeTime;
    /**
     * 修改人
     */

    @ApiModelProperty(value = "修改人",name = "modifier")
    private String modifier;
    /**
     * 修改时间
     */

    @ApiModelProperty(value = "修改时间",name = "modifyDate")
    private Date modifyDate;
    /**
     * 修改终端
     */

    @ApiModelProperty(value = "修改终端",name = "modifyTerminal")
    private String modifyTerminal;
    /**
     * 创建人
     */

    @ApiModelProperty(value = "创建人",name = "creator")
    private String creator;
    /**
     * 添加时间
     */

    @ApiModelProperty(value = "添加时间",name = "createDate")
    private String createDate;
    /**
     * 删除标记 0:正常;1:删除
     */
    @ApiModelProperty(value = "删除标记 0:正常;1:删除",name = "delFlag")
    private Integer delFlag;
    /**
     * 创建终端
     */

    @ApiModelProperty(value = "创建终端",name = "createTerminal")
    private String createTerminal;
    /**
     * 备注
     */

    @ApiModelProperty(value = "备注",name = "remark")
    private String remark;

    /**
     * 项目Id
     */
    @ApiModelProperty(value = "项目ID",name = "projectId")
    private String projectId;

    /**
     * 项目编码
     */
    @ApiModelProperty(value = "项目编码",name = "projectCode")
    private String projectCode;

    /**
     * 系统编号
     */
    @ApiModelProperty(value = "系统编号",name = "applicationCode")
    private String applicationCode;
    /**
     * SAAS租户编码
     */
    @ApiModelProperty(value = "SAAS租户编码",name = "tenantCode")
    private String tenantCode;

    /**
     * 在线状态
     */
    @ApiModelProperty(value = "在线状态(0,null 离线,1,在线)",name = "networkState")
    private Integer networkState;

    /**
     * 区域负责人
     */

    @ApiModelProperty(value = "区域负责人",name = "regionLeaderName")
    private String regionLeaderName;

    /**
     * 区域负责人电话
     */

    @ApiModelProperty(value = "区域负责人电话",name = "userMobile")
    private String userMobile;

    /**
     * 图片路径
     */

    @ApiModelProperty(value = "图片路径",name = "picUrl")
    private String picUrl;

    /**
     * 型号描述
     */

    @ApiModelProperty(value = "型号描述",name = "description")
    private String description;

    /**
     * 是否报警
     */

    @ApiModelProperty(value = "是否报警",name = "isAlarm")
    private Integer isAlarm;

    /**
     * 设备类型
     */
    @ApiModelProperty(value = "设备类型",name = "type")
    private int type;


    /**
     * 是否有在线视频
     */
    @ApiModelProperty(value = "是否有在线视频",name = "isVideo")
    private Integer isVideo;

    /**
     * 距离
     */
    @ApiModelProperty(value = "距离",name = "juli")
    private String juli;



    //--------------- 下面字段只作查询使用
    /**
     * 项目名称
     */
    @ApiModelProperty(value = "项目名称",name = "projectName")
    private String projectName;

    /**
     * 租户名称
     */
    @ApiModelProperty(value = "租户名称",name = "projectName")
    private String tenantName;

    /**
     * acpowerNumber 交流电源口
     */
    @ApiModelProperty(value = "交流电源口",name = "acpowerNumber")
    private Integer acpowerNumber;

    /**
     * dcpowerNumber 直流电源口
     */
    @ApiModelProperty(value = "直流电源口",name = "dcpowerNumber")
    private Integer dcpowerNumber;

    /**
     * fibreOpticalNumber 光纤口
     */
    @ApiModelProperty(value = "光纤口",name = "fibreOpticalNumber")
    private Integer fibreOpticalNumber;

    /**
     * rj45Number 网络口
     */
    @ApiModelProperty(value = "网络口",name = "rj45Number")
    private Integer rj45Number;
    /**
     * 调试模式(0:非调试,1:调试模式)
     */
    @ApiModelProperty(value = "调试模式(0:非调试,1:调试模式)",name = "debug")
    private Integer debug;


    /**
     * 调试时长(单位:天)
     */
    @ApiModelProperty(value = "调试时长",name = "debugDuration")
    private Integer debugDuration;

    /**
     * 调试截至日期
     */
    @ApiModelProperty(value = "调试截至日期",name = "debugDeadline")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date debugDeadline;
}
