package com.aswl.as.ibrs.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author jk
 * @version 1.0.0
 * @create 2020/1/9 11:37
 */
@Data
@ApiModel(value = "DeviceDetailsVo",description = "报警明细")
public class OpenDeviceDetailsVo implements Serializable {
    /**
     * 设备基本信息
     */
    @ApiModelProperty(value ="设备基本信息" ,name = "device")
    private  OpenDeviceVo device;

    /**
     * 上级设备信息
     */
    @ApiModelProperty(value = "上级设备信息",name = "parentDevice")
    private OpenDeviceVo parentDevice;

    /**
     * 下级设备信息list
     */
    @ApiModelProperty(value = "下级设备信息list",name = "childrenDeviceList")
    private List<OpenDeviceVo> childrenDeviceList;
}
