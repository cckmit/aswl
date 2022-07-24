package com.aswl.as.ibrs.api.vo;

import com.aswl.as.ibrs.api.module.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dingfei
 * @Description
 * @date 2019-10-25 19:50
 * @Version V1
 */
@Data
public class DeviceStautsOverallVo implements java.io.Serializable {

    /**
     * 非组显示数据
     */
    @ApiModelProperty(value = "非组显示数据",name = "deviceStatusNotGroup")
    private List<DeviceStatusVo> deviceStatusNotGroup=new ArrayList<>();

    /**
     * 组显示数据
     */

    @ApiModelProperty(value = "组显示数据",name = "deviceStatusGroup")
    private List<DeviceStatusGroupVo> deviceStatusGroup;


    /**
     * 设备分控板-电源
     */

    @ApiModelProperty(value = "设备分控板-电源",name = "eventSecCtlPower")
    private EventSecCtlPower eventSecCtlPower;


    /**
     * 设备分控板-电源输出
     */

    @ApiModelProperty(value = "组显示数据",name = "eventSecCtlPowerOutput")
    private EventSecCtlPowerOutput eventSecCtlPowerOutput;


    /**
     * 设备分控板-电源设置
     */

    @ApiModelProperty(value = "设备分控板-电源设置",name = "eventSecCtlPowerSet")
    private EventSecCtlPowerSet eventSecCtlPowerSet;

    /**
     * SFP信息
     */

    @ApiModelProperty(value = "SFP信息",name = "eventSfpInfo")
    private EventSfpInfo eventSfpInfo;

    /**
     * 姿态信息
     */

    @ApiModelProperty(value = "姿态信息",name = "eventPosture")
    private EventPosture eventPosture;

    /**
     * 设备事件-输入
     */
    @ApiModelProperty(value = "设备事件-输入",name = "eventInput")
    private EventInput eventInput;

}
