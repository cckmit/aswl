package com.aswl.as.ibrs.api.module;
import java.util.Date;

import com.aswl.as.common.core.persistence.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * 设备Entity
 *
 * @author dingfei
 * @date 2019-09-27 14:17
 */
@ApiModel(value = "Device",description = "设备Entity")
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class Device extends BaseEntity<Device> {
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
    @ApiModelProperty(value = "光纤口",name = "fibreOpticalNo")
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
     * 经度
     */
    @ApiModelProperty(value = "经度",name = "latitude")
    private Double latitude;
    /**
     * 经度A
     */

    @ApiModelProperty(value = "经度A",name = "latitudeA")
    private Double latitudeA;
    /**
     * 维度
     */

    @ApiModelProperty(value = "维度",name ="longitude")
    private Double longitude;
    /**
     * 维度A
     */

    @ApiModelProperty(value = "维度A",name  ="longitudeA")
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

    @ApiModelProperty(value = "上级槽位",name  ="parentSlotO")
    private Integer parentSlotO;

    /**
     * 设备种类id
     */
    @ApiModelProperty(value = "设备种类id",name = "deviceKindId")
    private String deviceKindId;

    /**
     * 设备类型
     */
    @ApiModelProperty(value = "设备类型",name = "deviceType")
    private String deviceType;
    /**
     * 设备型号
     */

    @ApiModelProperty(value = "设备型号",name = "deviceModelId")
    private String deviceModelId;
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
     * 区域编码
     */

    @ApiModelProperty(value = "区域编码",name = "regionCode")
    private String regionCode;
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
    private Date purchaseDate;

    /**
     * 出产日期
     */
    @ApiModelProperty(value = "出产日期",name = "produceDate")
    private Date produceDate;

    /**
     * 保修期
     */

    @ApiModelProperty(value = "保修期",name = "guaranteeTime")
    private Integer guaranteeTime;
    /**
     * 修改终端
     */

    @ApiModelProperty(value = "修改终端",name = "modifyTerminal")
    private String modifyTerminal;
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
     * 随机码
     */
    @ApiModelProperty(value = "运营端使用的字段，一般不用管，默认就为null",name = "randomStr")
    private String randomStr;

    /**
     * 项目编码
     */
    @ApiModelProperty(value = "项目编码",name = "projectCode")
    private String projectCode;

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
