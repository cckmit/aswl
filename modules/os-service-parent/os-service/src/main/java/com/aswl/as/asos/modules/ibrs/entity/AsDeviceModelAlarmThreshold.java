package com.aswl.as.asos.modules.ibrs.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 设备型号区间报警
 * </p>
 *
 * @author hfx
 * @since 2020-01-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="AsDeviceModelAlarmThreshold对象", description="设备型号区间报警")
@TableName(value = "as_device_model_alarm_threshold")
public class AsDeviceModelAlarmThreshold implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "唯一标识")
    @TableId(type = IdType.INPUT)
    private String id;

    @ApiModelProperty(value = "设备型号属性ID")
    private String eventMetadataModelId;

    @ApiModelProperty(value = "最大值")
    private Double maxValue;

    @ApiModelProperty(value = "最小值")
    private Double minValue;

    @ApiModelProperty(value = "报警类型值")
    private Integer statusValue;


}
