package com.aswl.as.asos.modules.asuser.entity;

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
 * APP菜单表(现在实际上没那么复杂，但是后面可能会很复杂，所以直接参考sys_menu表来弄，不用的列，清空就是)
 * </p>
 *
 * @author hfx
 * @since 2020-03-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="SysAppMenu对象", description="APP菜单表(现在实际上没那么复杂，但是后面可能会很复杂，所以直接参考sys_menu表来弄，不用的列，清空就是)")
public class SysAppMenu implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "唯一ID")
    @TableId(type = IdType.INPUT)
    private String id;

    @ApiModelProperty(value = "菜单名称")
    private String name;

    @ApiModelProperty(value = "权限标识")
    private String permission;

    @ApiModelProperty(value = "url")
    private String url;

    @ApiModelProperty(value = "父菜单id")
    private String parentId;

    @ApiModelProperty(value = "图标")
    private String icon;

    @ApiModelProperty(value = "排序")
    private String sort;

    @ApiModelProperty(value = "类型")
    private String type;

    @ApiModelProperty(value = "创建人")
    private String creator;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createDate;

    @ApiModelProperty(value = "修改人")
    private String modifier;

    @ApiModelProperty(value = "修改时间")
    private LocalDateTime modifyDate;

    @ApiModelProperty(value = "删除标记 0:正常;1:删除")
    private String delFlag;

    @ApiModelProperty(value = "模块")
    private String component;

    @ApiModelProperty(value = "路径")
    private String path;

    @ApiModelProperty(value = "重定向url")
    private String redirect;

    @ApiModelProperty(value = "是否隐藏 0 显示 1隐藏")
    private Boolean isHidden;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "系统编号")
    private String applicationCode;

    @ApiModelProperty(value = "租户编号")
    private String tenantCode;


}
