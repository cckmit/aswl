package com.aswl.as.asos.modules.ibrs.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 区域表
 * </p>
 *
 * @author hfx
 * @since 2019-12-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="AsRegion对象", description="区域表")
@TableName(value = "as_region")
public class AsRegion implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键", name = "id")
    @TableId(type = IdType.INPUT)
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
    private LocalDateTime createDate;

    @ApiModelProperty(value = "更新人账号")
    private String modifier;

    @ApiModelProperty(value = "更新人名称")
    private String updateName;

    @ApiModelProperty(value = "更新日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private LocalDateTime modifyDate;

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

    @ApiModelProperty(value = "租户编码")
    private String tenantCode;

    @ApiModelProperty(value = "IP段起始")
    private String ipPartBegin;

    @ApiModelProperty(value = "IP段结束")
    private String ipPartEnd;

    // ---- 以下字段是不在表里，仅在查询的时候作为传递

    /**
     * 父区域名称
     */
    @ApiModelProperty(value = "父区域名称",name = "parentName")
    @TableField(exist=false)
    private String parentName;


}
