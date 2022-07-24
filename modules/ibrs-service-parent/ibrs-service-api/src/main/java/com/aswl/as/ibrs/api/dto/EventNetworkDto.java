package com.aswl.as.ibrs.api.dto;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
*
* 设备事件-网络Dto
* @author zgl
* @date 2019-11-01 14:06
*/

@ApiModel(value = "EventNetworkDto",description = "设备事件-网络Dto")
@Data
public class EventNetworkDto implements Serializable {

private static final long serialVersionUID = 1L;

    /**
    * 主键
    */
    @ApiModelProperty(value = "主键",name="id")
    private String id;
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
    * 网络状态
    */
    @ApiModelProperty(value = "网络状态",name="networkState")
    private Integer networkState;
    /**
    * 存储时间
    */
    @ApiModelProperty(value = "存储时间",name="storeTime")
    private Date storeTime;
    /**
    * 系统编号
    */
    @ApiModelProperty(value = "系统编号",name="applicationCode")
    private String applicationCode;
    /**
    * 租户编码
    */
    @ApiModelProperty(value = "租户编码",name="tenantCode")
    private String tenantCode;
}
