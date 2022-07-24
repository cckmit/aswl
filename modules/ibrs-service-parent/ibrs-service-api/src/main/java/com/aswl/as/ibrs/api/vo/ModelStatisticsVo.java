package com.aswl.as.ibrs.api.vo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author dingfei
 * @Description
 * @date 2020-11-23 11:29
 * @Version V1
 */
@Data
public class ModelStatisticsVo implements Serializable {
    /**
     * 设备型号
     */
    @ApiModelProperty(value = "设备型号", name = "modelName")
    private String deviceModelName;
    /**
     * 设备数量
     */
    @ApiModelProperty(value = "设备数量", name = "deviceCount")
    private Integer deviceCount;

    /**
     * 出产时间
     */
    @ApiModelProperty(value = "出产时间", name = "produceDate")
    private String produceDate;
}
