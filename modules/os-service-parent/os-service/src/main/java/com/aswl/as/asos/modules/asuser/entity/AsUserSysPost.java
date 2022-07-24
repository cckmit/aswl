package com.aswl.as.asos.modules.asuser.entity;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
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
 * 工作岗位
 * </p>
 *
 * @author hfx
 * @since 2019-11-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="SysPost对象", description="工作岗位")
@TableName(value = "sys_post")
public class AsUserSysPost implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "唯一标识")
    @TableId(type = IdType.INPUT)
    private String postId;

    @ApiModelProperty(value = "岗位名称")
    private String postName;

    @ApiModelProperty(value = "月工作天数")
    private Integer workingDays;

    @ApiModelProperty(value = "降级标准")
    private Integer standardDown;

    @ApiModelProperty(value = "升级考评标准")
    private Integer standardUp;

    @ApiModelProperty(value = "提成比")
    private BigDecimal commission;

    @ApiModelProperty(value = "创建人")
    private String creator;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createDate;

    @ApiModelProperty(value = "修改人")
    private String modifier;

    @ApiModelProperty(value = "修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date modifyDate;

    @ApiModelProperty(value = "删除标记 0:正常;1:删除")
    private Integer delFlag;

    @ApiModelProperty(value = "应用编码")
    private String applicationCode;

    @ApiModelProperty(value = "租户编码")
    private String tenantCode;


}
