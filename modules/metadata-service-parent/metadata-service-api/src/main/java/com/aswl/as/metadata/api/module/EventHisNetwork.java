package com.aswl.as.metadata.api.module;
import com.aswl.as.common.core.persistence.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
*
* 设备历史事件-网络Entity
* @author houzejun
* @date 2019-11-14 11:16
*/

@ApiModel(value = "EventHisNetwork",description = "设备历史事件-网络Entity")
@Data
public class EventHisNetwork extends BaseEntity<EventHisNetwork> {

    /**
     * 统一事件ID
     */

    @ApiModelProperty(value = "统一事件ID",name="uEventId")
    private String uEventId;
    /**
    * 设备ID
    */

    @ApiModelProperty(value = "设备ID",name="deviceId")
    private String deviceId;
    /**
    * 区域编码
    */

    @ApiModelProperty(value = "区域编码",name="regionNo")
    private String regionNo;
    /**
    * 记录日期
    */

    @ApiModelProperty(value = "记录日期",name="recDate")
    private Date recDate;
    /**
    * 网络状态
    */

    @ApiModelProperty(value = "网络状态",name="networkState")
    private Integer networkState;
    /**
    * 存储时间
    */

    @ApiModelProperty(value = "存储时间",name="storeTime")
    private Date storeTime;
}
