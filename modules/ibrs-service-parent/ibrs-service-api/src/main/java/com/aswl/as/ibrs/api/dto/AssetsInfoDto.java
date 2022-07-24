package com.aswl.as.ibrs.api.dto;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 资产信息Dto
 *
 * @author df
 * @date 2022/01/14 15:54
 */

@ApiModel(value = "AssetsInfoDto", description = "资产信息Dto")
@Data
public class AssetsInfoDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @ApiModelProperty(value = "主键ID", name = "id")
    private String id;
    /**
     * 资产名称
     */
    @ApiModelProperty(value = "资产名称", name = "name")
    private String name;
    /**
     * 资产编号
     */
    @ApiModelProperty(value = "资产编号", name = "basicNo")
    private String basicNo;
    /**
     * 自定义编号
     */
    @ApiModelProperty(value = "自定义编号", name = "customNo")
    private Integer customNo;
    /**
     * 供应商
     */
    @ApiModelProperty(value = "供应商", name = "supplier")
    private String supplier;

    /**
     * 供应商电话
     */

    @ApiModelProperty(value = "供应商电话", name = "supplierTel")
    private String supplierTel;


    /**
     * 厂家
     */

    @ApiModelProperty(value = "厂家", name = "manufacturers")
    private String manufacturers;

    /**
     * 厂家电话
     */

    @ApiModelProperty(value = "厂家电话", name = "manufacturersTel")
    private String manufacturersTel;


    /**
     * 集成商
     */

    @ApiModelProperty(value = "集成商", name = "integrator")
    private String integrator;


    /**
     * 集成商电话
     */

    @ApiModelProperty(value = "集成商电话", name = "integratorTel")
    private String integratorTel;

    /**
     * 负责人
     */

    @ApiModelProperty(value = "负责人", name = "principal")
    private String principal;

    /**
     * 负责人电话
     */

    @ApiModelProperty(value = "负责人电话", name = "principalTel")
    private String principalTel;
    
    /**
     * 质保期
     */
    @ApiModelProperty(value = "质保期", name = "guaranteePeriod")
    private String guaranteePeriod;
    /**
     * 计量单位
     */
    @ApiModelProperty(value = "计量单位", name = "measureUnit")
    private String measureUnit;
    /**
     * 数量
     */
    @ApiModelProperty(value = "数量", name = "quantity")
    private Integer quantity;
    /**
     * 资产状态（0：空闲；1：使用中；2：维护；3：报废；4：外借）
     */
    @ApiModelProperty(value = "资产状态（0：空闲；1：使用中；2：维护；3：报废；4：外借）", name = "status")
    private Integer status;
    /**
     * 分类
     */
    @ApiModelProperty(value = "分类", name = "categoryId")
    private String categoryId;
    /**
     * 分类名称
     */
    @ApiModelProperty(value = "分类名称", name = "categoryName")
    private String categoryName;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注", name = "remark")
    private String remark;
    /**
     * 录入日期
     */
    @ApiModelProperty(value = "录入日期", name = "createDate")
    private Date createDate;
    /**
     * 录入人
     */
    @ApiModelProperty(value = "录入人", name = "creator")
    private String creator;
    /**
     * 系统编码
     */
    @ApiModelProperty(value = "系统编码", name = "applicationCode")
    private String applicationCode;
    /**
     * 租户编码
     */
    @ApiModelProperty(value = "租户编码", name = "tenantCode")
    private String tenantCode;
}
