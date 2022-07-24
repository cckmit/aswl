package com.aswl.as.asos.modules.ibrs.entity;

import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 事件元数据
 * </p>
 *
 * @author hfx
 * @since 2020-01-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="AsEventUcMetadata对象", description="事件元数据")
@TableName(value = "as_event_uc_metadata")
public class AsEventUcMetadata implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "唯一标识")
    @TableId(type = IdType.INPUT)
    private String id;

    @ApiModelProperty(value = "表编码")
    private String tabCode;

    @ApiModelProperty(value = "表名")
    private String tabName;

    @ApiModelProperty(value = "字段编码")
    private String fldCode;

    @ApiModelProperty(value = "字段名称")
    private String fldName;

    @ApiModelProperty(value = "字段类型")
    private String fldType;

    @ApiModelProperty(value = "字段单位")
    private String fldUnit;

    @ApiModelProperty(value = "状态名")
    private String statusName;

    @ApiModelProperty(value = "优先权")
    private Integer priority;

    @ApiModelProperty(value = "是否启用")
    private Boolean enable;

    @ApiModelProperty(value = "是否报警")
    private Boolean isAlarm;

    @ApiModelProperty(value = "矢量类型 0 模拟量 1开关量")
    private Integer vectorType;

    @ApiModelProperty(value = "图标URL")
    private String iconPath;

    @ApiModelProperty(value = "是否状态组")
    private Integer isStatusGroup;

    @ApiModelProperty(value = "状态组ID")
    private String eventStatusGroupId;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createDate;

    @ApiModelProperty(value = "创建人账号")
    private String createBy;

    @ApiModelProperty(value = "创建人名称")
    private String createName;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateDate;

    @ApiModelProperty(value = "更新人账号")
    private String updateBy;

    @ApiModelProperty(value = "更新人名称")
    private String updateName;

    //-----  下面字段不是数据库里的，仅作为查询使用---------
    @ApiModelProperty(value = "metadataModel的id")
    @TableField(exist=false)
    private String metadataModel;



}
