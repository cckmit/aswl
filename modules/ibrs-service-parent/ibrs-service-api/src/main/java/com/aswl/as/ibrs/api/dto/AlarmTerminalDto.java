package com.aswl.as.ibrs.api.dto;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 报警终端设备Dto
 *
 * @author dingfei
 * @date 2019-11-09 10:13
 */

@ApiModel(value = "AlarmTerminalDto", description = "报警终端设备Dto")
@Data
public class AlarmTerminalDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @ApiModelProperty(value = "ID", name = "id")
    private String id;
    /**
     * 名称
     */
    @ApiModelProperty(value = "名称", name = "name")
    private String name;
    /**
     * 编码
     */
    @ApiModelProperty(value = "编码", name = "code")
    private String code;
    /**
     * 地址
     */
    @ApiModelProperty(value = "地址", name = "address")
    private String address;
    /**
     * IP
     */
    @ApiModelProperty(value = "IP", name = "ip")
    private String ip;
    /**
     * 端口
     */
    @ApiModelProperty(value = "端口", name = "port")
    private Integer port;
    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名", name = "userName")
    private String userName;
    /**
     * 密码
     */
    @ApiModelProperty(value = "密码", name = "password")
    private String password;
    /**
     * 开关状态
     */
    @ApiModelProperty(value = "开关状态", name = "status")
    private Integer status;
    /**
     * 即时连接状态
     */
    @ApiModelProperty(value = "即时连接状态", name = "connectStatus")
    private Integer connectStatus;
    /**
     * 区域ID
     */
    @ApiModelProperty(value = "区域ID", name = "regionId")
    private String regionId;
    /**
     * 购买日期
     */
    @ApiModelProperty(value = "购买日期", name = "purchaseDate")
    private Date purchaseDate;
    /**
     * 保修期
     */
    @ApiModelProperty(value = "保修期", name = "guaranteeTime")
    private Integer guaranteeTime;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注", name = "remark")
    private String remark;
    /**
     * 创建日期
     */
    @ApiModelProperty(value = "创建日期", name = "createDate")
    private Date createDate;
    /**
     * 创建人账号
     */
    @ApiModelProperty(value = "创建人账号", name = "createBy")
    private String createBy;
    /**
     * 创建人名称
     */
    @ApiModelProperty(value = "创建人名称", name = "createName")
    private String createName;
    /**
     * 更新日期
     */
    @ApiModelProperty(value = "更新日期", name = "updateDate")
    private Date updateDate;
    /**
     * 更新人账号
     */
    @ApiModelProperty(value = "更新人账号", name = "updateBy")
    private String updateBy;
    /**
     * 更新人名称
     */
    @ApiModelProperty(value = "更新人名称", name = "updateName")
    private String updateName;
    /**
     * 所属机构
     */
    @ApiModelProperty(value = "所属机构", name = "orgCode")
    private String orgCode;
    /**
     * 所属区域
     */
    @ApiModelProperty(value = "所属区域", name = "regionCode")
    private String regionCode;
    /**
     * 删除标记
     */
    @ApiModelProperty(value = "删除标记", name = "delFlag")
    private Integer delFlag;
    /**
     * 删除日期
     */
    @ApiModelProperty(value = "删除日期", name = "delDate")
    private Date delDate;
    /**
     * 系统编号
     */
    @ApiModelProperty(value = "系统编号", name = "applicationCode")
    private String applicationCode;
    /**
     * SAAS租户编码
     */
    @ApiModelProperty(value = "SAAS租户编码", name = "tenantCode")
    private String tenantCode;
}
