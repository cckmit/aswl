package com.aswl.as.user.api.module;
import com.aswl.as.common.core.persistence.BaseEntity;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 系统配置信息表Entity
 *
 * @author dingfei
 * @date 2019-12-18 10:57
 */

@ApiModel(value = "Config", description = "系统配置信息表Entity")
@Data
public class Config extends BaseEntity<Config> {
    /**
     * key
     */

    @ApiModelProperty(value = "key", name = "paramKey")
    private String paramKey;
    /**
     * value
     */

    @ApiModelProperty(value = "value", name = "paramValue")
    private String paramValue;
    /**
     * 状态   0：隐藏   1：显示
     */

    @ApiModelProperty(value = "状态   0：隐藏   1：显示", name = "status")
    private Integer status;
    /**
     * 备注
     */

    @ApiModelProperty(value = "备注", name = "remark")
    private String remark;
}
