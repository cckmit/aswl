package com.aswl.as.ibrs.api.module;

import com.aswl.as.common.core.persistence.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 设备事件-网络
 *
 * @author liuliepan
 * @date 2019-09-26 15:29
 */

@ApiModel(value = "AsEventNetwork",description = "设备事件-网络Entity")
@Data
public class Eventstatu extends BaseEntity<Eventstatu> {

    @ApiModelProperty(value = "设备ID",name = "deviceId")
    private Integer deviceId;

    @ApiModelProperty(value = "区域编码",name = "regionNo")
    private String regionNo;

    @ApiModelProperty(value = "网络状态",name = "networkState")
    private String networkState;

    @ApiModelProperty(value = "存储时间",name = "storeTime")
    private String storeTime;


}
