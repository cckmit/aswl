package com.aswl.as.metadata.api.dto;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
*
* 事件统一ID自增Dto
* @author zgl
* @date 2019-11-14 14:59
*/

@ApiModel(value = "EventUcIdDto",description = "事件统一ID自增Dto")
@Data
public class EventUcIdDto implements Serializable {

private static final long serialVersionUID = 1L;

    /**
    * 
    */
    @ApiModelProperty(value = "",name="id")
    private String id;
}
