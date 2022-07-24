package com.aswl.as.ibrs.api.dto;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
*
* 设备在线统计Dto
* @author dingfei
* @date 2019-12-16 16:01
*/

@ApiModel(value = "OnlineStatisticsDto",description = "设备在线统计Dto")
@Data
public class OnlineStatisticsDto implements Serializable {

private static final long serialVersionUID = 1L;

    /**
    * ID
    */
    @ApiModelProperty(value = "ID",name="id")
    private String id;
    /**
    * 日期
    */
    @ApiModelProperty(value = "日期",name="createDate")
    private Date createDate;
    /**
    * 区域编码
    */
    @ApiModelProperty(value = "区域编码",name="regionNo")
    private String regionNo;
    /**
    * 在线数量
    */
    @ApiModelProperty(value = "在线数量",name="onlineNum")
    private Integer onlineNum;
    /**
    * 设备数量
    */
    @ApiModelProperty(value = "设备数量",name="deviceNum")
    private Integer deviceNum;
}
