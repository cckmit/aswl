package com.aswl.as.ibrs.api.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 设备Dto
 *
 * @author dingfei
 * @date 2019-09-27 14:12
 */
@ApiModel(value = "DeviceDto",description = "设备Dto")
@Data
public class DeviceDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @ApiModelProperty(value = "主键",name = "id")
    private String id;
    /**
     * 编码
     */
    @ApiModelProperty(value = "编码",name = "deviceCode")
    private String deviceCode;
    /**
     * 设备名称
     */
    @ApiModelProperty(value = "设备名称",name = "deviceName")
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

    @ApiModelProperty(value = "维度",name = "longitude")
    private Double longitude;
    /**
     * 维度A
     */
    @ApiModelProperty(value = "维度A",name = "longitudeA")
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
     * 下级设备ID字符串
     */

    @ApiModelProperty(value = "下级设备ID字符串",name = "childrenDeviceIds")
    private String childrenDeviceIds;
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
     * 修改者
     */
    @ApiModelProperty(value = "修改者",name = "modifier")
    private String modifier;
    /**
     * 最后修改时间
     */
    @ApiModelProperty(value = "最后修改时间",name = "modifyDate")
    private Date modifyDate;

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
     * 修改终端
     */
    @ApiModelProperty(value = "修改终端",name = "modifyTerminal")
    private String modifyTerminal;
    /**
     * 创建着
     */
    @ApiModelProperty(value = "创建着",name = "creator")
    private String creator;
    /**
     * 添加时间
     */
    @ApiModelProperty(value = "添加时间",name = "createDate")
    private Date createDate;
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
     * 设备型号名
     */
    @ApiModelProperty(value = "设备型号名",name = "deviceModelName")
    private String deviceModelName;

    /**
     * 上级设备编码
     */
    @ApiModelProperty(value = "上级设备编码",name = "parentDeviceCode")
    private String parentDeviceCode;

    /**
     * 项目Id
     */
    @ApiModelProperty(value = "项目ID",name = "projectId")
    private String projectId;

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
     * 是否傲视云平台运营端传递过来的
     * （该字段不会保存到数据库，只作标记使用，null或者为0就是不是运营端传递过来，只有1才是运营端传递过来）
     * （isAsOs 和 randomStr 这两个属性一般不用的）
     */
    @ApiModelProperty(value = "傲视运营端传递过来的数据",name = "isAsOs",example= "0")
    private String isAsOs;

    @ApiModelProperty(value = "傲视运营端传递过来的数据校验",name = "randomStr",example= "")
    private String randomStr;

    /**
     * 设备发现ID
     */
    @ApiModelProperty(value = "设备发现ID",name = "discoverId")
    private String discoverId;

    @ApiModelProperty(value = "搜索关键字",name = "keywords")
    private String keywords;

    @ApiModelProperty(value = "出厂编码",name = "factoryCode")
    private String factoryCode;

    /**
     * 子设备:添加下级时用来传输电源口网络口光纤口
     */
    @ApiModelProperty(value = "子设备",name = "chilDevice")
    private List<Map> chilDevice;

    /**
     * 父设备:添加上级时用来传输电源口网络口光纤口
     */
    @ApiModelProperty(value = "父设备",name = "parentDevice")
    private List<Map> parentDevice;

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


    /**
     * 设备类型过滤参数
     */
    @ApiModelProperty(value = "设备类型过滤参数",name = "type")
    private String type;
    
    /**
     * 设备型号过滤参数
     */
    @ApiModelProperty(value = "设备型号过滤参数",name = "model")
    private String model;

    /**
     * 设备种类过滤参数
     */
    @ApiModelProperty(value = "设备种类过滤参数(2、智能箱 3、摄像机)",name = "kind")
    private String  kind;

    /**
     * 多条件查询参数
     */
    @ApiModelProperty(value = "多条件查询参数",name = "query")
    private String  query;

    /**
     * ID集（,号分隔）
     */
    @ApiModelProperty(value = "ID集（,号分隔）",name = "ids")
    private String ids;
}
