package com.aswl.as.ibrs.api.module;
import java.util.Date;
import com.aswl.as.common.core.persistence.BaseEntity;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
*
* 设备事件数据扩展Entity
* @author zgl
* @date 2021-08-16 13:57
*/

@ApiModel(value = "EventDataExt",description = "设备事件数据扩展Entity")
@Data
public class EventDataExt extends BaseEntity<EventDataExt> {

    /**
    * ID主键
    */

    @ApiModelProperty(value = "ID主键",name="id")
    private String id;
    /**
    * 设备ID
    */

    @ApiModelProperty(value = "设备ID",name="deviceId")
    private String deviceId;
    /**
    * 数据
    */

    @ApiModelProperty(value = "数据",name="data")
    private String data;
}
