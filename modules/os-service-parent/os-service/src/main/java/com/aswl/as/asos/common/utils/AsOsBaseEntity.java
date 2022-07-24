package com.aswl.as.asos.common.utils;

import com.aswl.as.common.core.constant.CommonConstant;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * Entity基类
 *
 * @author aswl.com
 * @date 2018-08-24 18:58
 */
@Data
@NoArgsConstructor
@ApiModel(value = "AsUserBaseEntity", description = "Entity基类")
public class AsOsBaseEntity<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 创建者
     */
    @ApiModelProperty(value = "创建者", name = "creator")
    protected String creator;

    /**
     * 创建日期
     */

    @ApiModelProperty(value = "创建日期", name = "createDate")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    protected Date createDate;

    /**
     * 更新者
     */
    @ApiModelProperty(value = "更新者", name = "modifier")
    protected String modifier;

    /**
     * 更新日期
     */

    @ApiModelProperty(value = "更新日期", name = "modifyDate")
    protected Date modifyDate;

    /**
     * 删除标记 0:正常，1-删除
     */

    @ApiModelProperty(value = "删除标记 0:正常，1-删除", name = "delFlag")
    protected Integer delFlag = CommonConstant.DEL_FLAG_NORMAL;

    /**
     * 系统编号
     */
    @ApiModelProperty(value = "系统编号", name = "applicationCode", example = "AS")
    protected String applicationCode;

    /**
     * 租户编号
     */
    @ApiModelProperty(value = "租户编号", name = "tenantCode", example = "aswl")
    protected String tenantCode;

    /**
     * 是否为新记录
     */
    @ApiModelProperty(value = "是否为新记录", name = "isNewRecord")
    @TableField(exist=false)
    protected boolean isNewRecord;

    /**
     * ID数组
     */

    @ApiModelProperty(value = "ID数组", name = "ids")
    @TableField(exist=false)
    protected String[] ids;

    /**
     * ID字符串，多个用逗号隔开
     */

    @ApiModelProperty(value = "ID字符串，多个用逗号隔开", name = "idString")
    @TableField(exist=false)
    protected String idString;

}

