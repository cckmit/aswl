package com.aswl.as.asos.modules.asuser.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 系统配置信息表Entity
 *
 * @author dingfei
 * @date 2019-12-18 10:57
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="SysConfig对象", description="系统配置信息表")
@TableName(value = "sys_config")
public class SysConfig  implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "唯一ID")
    @TableId(type = IdType.INPUT)
    private String id;
    
    @ApiModelProperty(value = "key", name = "paramKey")
    private String paramKey;
    
    @ApiModelProperty(value = "value", name = "paramValue")
    private String paramValue;

    @ApiModelProperty(value = "状态   0：隐藏   1：显示", name = "status")
    private Integer status;

    @ApiModelProperty(value = "备注", name = "remark")
    private String remark;

    @ApiModelProperty(value = "系统编号")
    private String applicationCode;

    @ApiModelProperty(value = "租户编号")
    private String tenantCode;
}
