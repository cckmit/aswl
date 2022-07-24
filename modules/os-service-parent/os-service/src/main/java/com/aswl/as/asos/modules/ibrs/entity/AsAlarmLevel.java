package com.aswl.as.asos.modules.ibrs.entity;

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
 * 报警级别
 * </p>
 *
 * @author hfx
 * @since 2020-03-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="AsAlarmLevel对象", description="报警级别")
public class AsAlarmLevel implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "唯一标识")
    @TableId(type = IdType.INPUT)
    private String id;

    @ApiModelProperty(value = "级别")
    private Integer alarmLevel;

    private String alarmLevelName;

    private String alarmLevelNameEn;

    private String remark;
    
    private String remarkEn;

    @ApiModelProperty(value = "工单生成方式（0：自动；1：手动）")
    private Integer orderGenType;

    @ApiModelProperty(value = "SAAS租户编码")
    private String tenantCode;

}
