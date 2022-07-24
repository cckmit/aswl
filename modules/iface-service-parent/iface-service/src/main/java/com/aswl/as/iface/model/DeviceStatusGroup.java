package com.aswl.as.iface.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jk
 * @version 1.0.0
 * @create 2019/12/30 16:00
 */
@Data
@ApiModel(value ="状态组",description = "设备状态组")
public class DeviceStatusGroup implements java.io.Serializable{
    /**
     * 状态组显示名称
     */
    @ApiModelProperty(value = "状态组显示名称",name = "name")
    private String name;

    /**
     * 状态组名称
     */

    @ApiModelProperty(value = "状态组名称",name = "statusGroupName")
    private String statusGroupName;

    /**
     * 组内状态位list
     */

    @ApiModelProperty(value = "组内状态位list",name = "deviceStatusVoList")
    private List<DeviceStatus> deviceStatusVoList = new ArrayList();
}
