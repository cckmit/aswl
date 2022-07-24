package com.aswl.as.asos.modules.asuser.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @author df
 * @date 2020/10/19 11:13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "SysTenantRole对象", description = "租户角色表")
public class SysTenantRole implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "唯一ID")
    @TableId(type = IdType.INPUT)
    private String id;

    @ApiModelProperty(value = "租户角色名称")
    private String tenantRoleName;

    @ApiModelProperty(value = "描述")
    private String remark;

    @ApiModelProperty(value = "创建人")
    private String creator;

    @ApiModelProperty(value = "创建时间")
    private Date createDate;

    @ApiModelProperty(value = "菜单ID字符串")
    private String menuIds;
}
