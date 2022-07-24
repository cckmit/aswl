package com.aswl.as.iface.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jk
 * @version 1.0.0
 * @create 2019/12/30 15:58
 */
@Data
@ApiModel(value = "设备状态汇总",description = "设备状态汇总")
public class DeviceStautsOverall implements java.io.Serializable{
    /**
     * 非组显示数据
     */
    @ApiModelProperty(value = "非组显示数据",name = "deviceStatusNotGroup")
    private List<DeviceStatus> deviceStatusNotGroup=new ArrayList<>();

    /**
     * 组显示数据
     */

    @ApiModelProperty(value = "组显示数据",name = "deviceStatusGroup")
    private List<DeviceStatusGroup> deviceStatusGroup;
}
