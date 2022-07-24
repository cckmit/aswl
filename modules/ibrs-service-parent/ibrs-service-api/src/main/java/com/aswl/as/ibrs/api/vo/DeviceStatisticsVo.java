package com.aswl.as.ibrs.api.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 *  在线率--》健康率-->统计VO
 * @author df
 * @date 2019-12-31 15:28
 * @Version v5.1.0
 */
@Data
public class DeviceStatisticsVo implements Serializable {

    @ApiModelProperty(value = "在线设备数",name="onlineDevice")
    private Integer onlineDevice;

    @ApiModelProperty(value = "离线设备数",name="offDevice")
    private Integer offDevice;

    @ApiModelProperty(value = "设备列表",name="deviceList")
    private List<DeviceVo> deviceList;

}
