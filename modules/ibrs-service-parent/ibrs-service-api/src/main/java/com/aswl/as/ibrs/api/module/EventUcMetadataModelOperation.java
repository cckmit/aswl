package com.aswl.as.ibrs.api.module;
import java.util.Date;
import com.aswl.as.common.core.persistence.BaseEntity;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
*
* 设备型号事件元数据-状态操作Entity
* @author dingfei
* @date 2019-12-02 10:44
*/

@ApiModel(value = "EventUcMetadataModelOperation",description = "设备型号事件元数据-状态操作Entity")
@Data
public class EventUcMetadataModelOperation extends BaseEntity<EventUcMetadataModelOperation> {
    /**
    * 事件元数据-设备型号ID
    */

    @ApiModelProperty(value = "事件元数据-设备型号ID",name="eventMetadataModelId")
    private String eventMetadataModelId;
    /**
    * 状态操作ID
    */

    @ApiModelProperty(value = "状态操作ID",name="statusOperationId")
    private String statusOperationId;
}
