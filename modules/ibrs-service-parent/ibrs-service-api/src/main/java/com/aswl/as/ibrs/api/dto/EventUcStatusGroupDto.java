package com.aswl.as.ibrs.api.dto;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
*
* 事件状态组Dto
* @author dingfei
* @date 2019-10-22 11:45
*/

@ApiModel(value = "EventUcStatusGroupDto",description = "事件状态组Dto")
@Data
public class EventUcStatusGroupDto implements Serializable {

private static final long serialVersionUID = 1L;

    /**
    * ID
    */
    @ApiModelProperty(value = "ID",name="id")
    private String id;
    /**
    * 名称
    */
    @ApiModelProperty(value = "名称",name="name")
    private String name;
    /**
    * StatusGroupName
    */
    @ApiModelProperty(value = "StatusGroupName",name="statusGroupName")
    private String statusGroupName;
    /**
    * 最大端口数量
    */
    @ApiModelProperty(value = "最大端口数量",name="maxPortNum")
    private Integer maxPortNum;
}
