package com.aswl.as.ibrs.api.module;
import com.aswl.as.common.core.persistence.BaseEntity;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 事件元数据-状态操作Entity
 *
 * @author dingfei
 * @date 2019-11-13 11:50
 */

@ApiModel(value = "EventUcMetadataStatusOperation", description = "事件元数据-状态操作Entity")
@Data
public class EventUcMetadataStatusOperation extends BaseEntity<EventUcMetadataStatusOperation> {
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
