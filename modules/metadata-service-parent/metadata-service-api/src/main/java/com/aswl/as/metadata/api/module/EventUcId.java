package com.aswl.as.metadata.api.module;
import java.util.Date;
import com.aswl.as.common.core.persistence.BaseEntity;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
*
* 事件统一ID自增Entity
* @author zgl
* @date 2019-11-14 14:59
*/

@ApiModel(value = "EventUcId",description = "事件统一ID自增Entity")
@Data
public class EventUcId extends BaseEntity<EventUcId> {

}
