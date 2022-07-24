package com.aswl.as.asos.dto;

import com.aswl.as.asos.modules.ibrs.entity.AsRegion;
import com.aswl.as.common.core.persistence.TreeEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class OsRegionDto extends TreeEntity<OsRegionDto> {

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键", name = "id")
    private String id;

    @ApiModelProperty(value = "区域名称")
    private String regionName;

    @ApiModelProperty(value = "父区域ID")
    private String parentId;

    @ApiModelProperty(value = "区域全称")
    private String fullName;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "区域编码")
    private String regionCode;

    @ApiModelProperty(value = "维度")
    private BigDecimal latitude;

    @ApiModelProperty(value = "经度")
    private BigDecimal longitude;

    @ApiModelProperty(value = "创建人账号")
    private String creator;

    @ApiModelProperty(value = "创建人名称")
    private String createName;

    @ApiModelProperty(value = "创建日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createDate;

    @ApiModelProperty(value = "更新人账号")
    private String modifier;

    @ApiModelProperty(value = "更新人名称")
    private String updateName;

    @ApiModelProperty(value = "更新日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date modifyDate;

    @ApiModelProperty(value = "所属机构")
    private String sysOrgCode;

    @ApiModelProperty(value = "所属区域")
    private String sysRegionCode;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "删除标记 0:正常;1:删除")
    private Integer delFlag;

    @ApiModelProperty(value = "项目ID")
    private String projectId;

    @ApiModelProperty(value = "系统编号")
    private String applicationCode;

    private String tenantCode;


    // ---- 以下字段是不在表里，仅在查询的时候作为传递

    /**
     * 父区域名称
     */
    @ApiModelProperty(value = "父区域名称",name = "parentName")
    @TableField(exist=false)
    private String parentName;

    @ApiModelProperty(value = "节点类型，1为租户，2为项目，3为区域")
    @TableField(exist=false)
    private String type;

    public OsRegionDto(AsRegion r)
    {
        // 初始化字段
        this.id = r.getId();
        this.regionName = r.getRegionName();

        this.fullName = r.getFullName();
        this.description = r.getDescription();
        this.longitude = r.getLongitude();
        this.latitude = r.getLatitude();
        this.regionCode = r.getRegionCode();
        this.createName = r.getCreateName();
        this.updateName = r.getUpdateName();
        this.sysOrgCode = r.getSysOrgCode();

        this.projectId=r.getProjectId();
        this.tenantCode=r.getTenantCode();

        this.parentId = r.getParentId();
        this.sort =r.getSort()!= null ?r.getSort() :0;
        this.sysRegionCode = r.getSysRegionCode();

        this.type="3";
    }

}
