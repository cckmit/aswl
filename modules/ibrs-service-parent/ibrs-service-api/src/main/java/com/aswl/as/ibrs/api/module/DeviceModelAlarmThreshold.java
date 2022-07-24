package com.aswl.as.ibrs.api.module;
import com.aswl.as.common.core.persistence.BaseEntity;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
*
* 设备型号区间报警Entity
* @author dingfei
* @date 2019-10-26 14:24
*/

@ApiModel(value = "DeviceModelAlarmThreshold",description = "设备型号区间报警Entity")
@Data
public class DeviceModelAlarmThreshold extends BaseEntity<DeviceModelAlarmThreshold> {
    /**
    * 设备型号属性ID
    */

    @ApiModelProperty(value = "设备型号属性ID",name="eventMetadataModelId")
    private String eventMetadataModelId;
    /**
    * 最大值
    */

    @ApiModelProperty(value = "最大值",name="maxValue")
    private Double maxValue;
    /**
    * 最小值
    */

    @ApiModelProperty(value = "最小值",name="minValue")
    private Double minValue;
    /**
    * 报警类型值
    */

    @ApiModelProperty(value = "报警类型值",name="statusValue")
    private Integer statusValue;
}
