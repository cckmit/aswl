package com.aswl.as.ibrs.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * 供电单位VO
 * @author zgl
 * @date 2021/07/18
 */

@ApiModel(value = "ElectricSupplyUnitVo",description = "供电单位VO")
@Data
public class ElectricSupplyUnitVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @ApiModelProperty(value = "主键ID",name="id")
    private String id;
    /**
     * 单位名称
     */
    @ApiModelProperty(value = "单位名称",name="name")
    private String name;
    /**
     * 联系人
     */
    @ApiModelProperty(value = "联系人",name="contact")
    private String contact;
    /**
     * 联系电话
     */
    @ApiModelProperty(value = "联系电话",name="phone")
    private String phone;

    /**
     * 电价（每千瓦时）
     */
    @ApiModelProperty(value = "电价（每千瓦时）",name="phone")
    private Double electricPrice;

    /**
     * 点位数
     */
    @ApiModelProperty(value = "点位数",name="deviceNum")
    private int deviceNum;

    /**
     * 点位ID集（,号分隔）
     */
    @ApiModelProperty(value = "点位ID集（,号分隔）",name="deviceNum")
    private String deviceIds;

    /**
     * 描述
     */
    @ApiModelProperty(value = "描述",name="description")
    private String description;
    /**
     * 项目ID
     */
    @ApiModelProperty(value = "项目ID",name="projectId")
    private String projectId;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间",name="createTime")
    private Date createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间",name="modifyDate")
    private Date modifyDate;
    /**
     * 系统编号
     */
    @ApiModelProperty(value = "系统编号",name="applicationCode")
    private String applicationCode;
    /**
     * SAAS租户编码
     */
    @ApiModelProperty(value = "SAAS租户编码",name="tenantCode")
    private String tenantCode;
}
