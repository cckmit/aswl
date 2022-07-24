package com.aswl.as.ibrs.api.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 设备型号元数据VO
 * @author dingfei
 * @Description
 * @date 2019-10-25 14:55
 * @Version V1
 */
@Data
public class DeviceModelMetadataVo implements java.io.Serializable {

    /**
     * 设备ID
     */
    @ApiModelProperty(value ="设备ID" ,name = "deviceId")
    private String deviceId;

    /**
     * 设备名称
     */

    @ApiModelProperty(value ="设备名称" ,name = "deviceName")
    private String deviceName;

    /**
     * 设备型号ID
     */


    @ApiModelProperty(value ="设备型号ID" ,name = "deviceModelId")
    private String deviceModelId;

    /**
     * 设备型号名称
     */

    @ApiModelProperty(value ="设备型号名称" ,name = "deviceModelName")
    private String deviceModelName;

    /**
     * 事件元数据ID
     */

    @ApiModelProperty(value ="事件元数据ID" ,name = "eventMetadataId")
    private String eventMetadataId;

    /**
     * 状态组ID
     */

    @ApiModelProperty(value ="状态组ID" ,name = "statusGroupId")
    private String statusGroupId;

    /**
     * 事件元数据表编码
     */

    @ApiModelProperty(value ="事件元数据表编码" ,name = "tabCode")
    private String tabCode;

    /**
     * 事件元数据字段编码
     */

    @ApiModelProperty(value ="事件元数据字段编码" ,name = "fldCode")
    private String fldCode;
}
