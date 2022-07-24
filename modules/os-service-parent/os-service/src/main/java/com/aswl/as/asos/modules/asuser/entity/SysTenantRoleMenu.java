package com.aswl.as.asos.modules.asuser.entity;

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
 * 租户角色菜单表
 * </p>
 *
 * @author df
 * @since 2020-11-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="SysTenantRoleMenu对象", description="租户角色菜单表")
public class SysTenantRoleMenu implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "唯一标识")
    @TableId(type = IdType.INPUT)
    private String id;

    @ApiModelProperty(value = "租户角色ID")
    private String tenantRoleId;

    @ApiModelProperty(value = "菜单ID")
    private String menuId;


}
