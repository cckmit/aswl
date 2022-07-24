package com.aswl.as.asos.modules.asuser.entity;

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
 * 职位表
 * </p>
 *
 * @author hfx
 * @since 2019-11-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="SysPosition对象", description="职位表")
@TableName(value = "sys_position")
public class AsUserSysPosition implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "职位ID")
    @TableId(type = IdType.INPUT)
    private String positionId;

    @ApiModelProperty(value = "职位父ID")
    private String positionParentId;

    @ApiModelProperty(value = "职位名称")
    private String positionName;

    @ApiModelProperty(value = "职位描述")
    private String positionDes;

    @ApiModelProperty(value = "岗位级别")
    private String postId;

    private Integer sort;

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
