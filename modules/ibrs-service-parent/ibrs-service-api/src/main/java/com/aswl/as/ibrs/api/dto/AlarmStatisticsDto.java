package com.aswl.as.ibrs.api.dto;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
*
* 设备故障统计Dto
* @author dingfei
* @date 2019-10-22 19:01
*/

@ApiModel(value = "AlarmStatisticsDto",description = "设备故障统计Dto")
@Data
public class AlarmStatisticsDto implements Serializable {

private static final long serialVersionUID = 1L;

    /**
    * 主键
    */
    @ApiModelProperty(value = "主键",name="id")
    private String id;
    /**
    * 类型(1:报障箱,2:摄像头)
    */
    @ApiModelProperty(value = "类型(1:报障箱,2:摄像头)",name="type")
    private Integer type;
    /**
    * 区域编码
    */
    @ApiModelProperty(value = "区域编码",name="regionCode")
    private String regionCode;
    /**
    * 时间
    */
    @ApiModelProperty(value = "时间",name="createDate")
    private Date createDate;
    /**
    * 数量
    */
    @ApiModelProperty(value = "数量",name="counts")
    private Integer counts;
}
