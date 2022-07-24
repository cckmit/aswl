package com.aswl.as.ibrs.api.vo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;

/**
 * @author dingfei
 * @Description
 * @date 2019-10-24 16:44
 * @Version V1
 */
@Data
@ApiModel(value = "DeviceDetailsVo",description = "报警明细")
public class DeviceDetailsVo implements java.io.Serializable{

    /**
     * 设备基本信息
     */
    @ApiModelProperty(value ="设备基本信息" ,name = "device")
    private  DeviceVo device;

    /**
     * 上级设备信息
     */
    @ApiModelProperty(value = "上级设备信息",name = "parentDevice")
    private DeviceVo parentDevice;

    /**
     * 下级设备信息list
     */
    @ApiModelProperty(value = "下级设备信息list",name = "childrenDeviceList")
    private List<DeviceVo> childrenDeviceList;

}
