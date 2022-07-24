package com.aswl.as.ibrs.api.dto;
import java.io.Serializable;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 事件元数据-状态操作Dto
 *
 * @author dingfei
 * @date 2019-11-13 11:50
 */

@ApiModel(value = "EventUcMetadataStatusOperationDto", description = "事件元数据-状态操作Dto")
@Data
public class EventUcMetadataStatusOperationDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @ApiModelProperty(value = "ID", name = "id")
    private String id;
    /**
     * 事件元数据ID
     */
    @ApiModelProperty(value = "事件元数据ID", name = "eventMetadataId")
    private String eventMetadataId;
    /**
     * 状态操作ID
     */
    @ApiModelProperty(value = "状态操作ID", name = "statusOperationId")
    private String statusOperationId;
}
