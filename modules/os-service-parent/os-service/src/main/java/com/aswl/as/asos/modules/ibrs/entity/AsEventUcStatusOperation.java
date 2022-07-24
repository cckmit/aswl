package com.aswl.as.asos.modules.ibrs.entity;

import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 事件状态操作
 * </p>
 *
 * @author hfx
 * @since 2020-01-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="AsEventUcStatusOperation对象", description="事件状态操作")
public class AsEventUcStatusOperation implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "唯一标识")
    @TableId(type = IdType.INPUT)
    private String id;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "操作名称")
    private String operName;

    @ApiModelProperty(value = "操作编码")
    private String operCode;

    @ApiModelProperty(value = "排序")
    private Integer operSort;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createDate;

    @ApiModelProperty(value = "创建人账号")
    private String createBy;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateDate;

    @ApiModelProperty(value = "更新人账号")
    private String updateBy;

    @ApiModelProperty(value = "所属机构")
    private String sysOrgCode;

    @ApiModelProperty(value = "所属区域")
    private String sysRegionCode;


}
