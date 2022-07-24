package com.aswl.as.ibrs.api.dto;

import java.io.Serializable;

import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 设备型号事件元数据-状态操作Dto
 *
 * @author dingfei
 * @date 2019-12-02 10:44
 */

@ApiModel(value = "EventUcMetadataModelOperationDto", description = "设备型号事件元数据-状态操作Dto")
@Data
public class EventUcMetadataModelOperationDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @ApiModelProperty(value = "ID", name = "id")
    private String id;
    /**
     * 事件元数据-设备型号ID
     */
    @ApiModelProperty(value = "事件元数据-设备型号ID", name = "eventMetadataModelId")
    private String eventMetadataModelId;
    /**
     * 状态操作ID
     */
    @ApiModelProperty(value = "状态操作ID", name = "statusOperationId")
    private String statusOperationId;
}
