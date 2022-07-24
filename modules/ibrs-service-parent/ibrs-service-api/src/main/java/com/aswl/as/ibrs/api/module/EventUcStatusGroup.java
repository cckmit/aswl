package com.aswl.as.ibrs.api.module;
import java.util.Date;
import com.aswl.as.common.core.persistence.BaseEntity;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
*
* 事件状态组Entity
* @author dingfei
* @date 2019-10-22 11:45
*/

@ApiModel(value = "EventUcStatusGroup",description = "事件状态组Entity")
@Data
public class EventUcStatusGroup extends BaseEntity<EventUcStatusGroup> {
    /**
    * 名称
    */

    @ApiModelProperty(value = "名称",name="name")
    private String name;

    /**
     * 英文名称
     */
    @ApiModelProperty(value = "英文名称",name="nameEn")
    private String nameEn;
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
