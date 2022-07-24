package com.aswl.as.ibrs.api.dto;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
*
* 市级平台在线统计表Dto
* @author hzj
* @date 2021/01/23 17:26
*/

@ApiModel(value = "CityOnlineStatisticsDto",description = "市级平台在线统计表Dto")
@Data
public class CityOnlineStatisticsDto implements Serializable {

private static final long serialVersionUID = 1L;

    /**
    * ID主键
    */
    @ApiModelProperty(value = "ID主键",name="id")
    private String id;
    /**
    * 城市编码
    */
    @ApiModelProperty(value = "城市编码",name="cityCode")
    private String cityCode;
    /**
    * 统计日期
    */
    @ApiModelProperty(value = "统计日期",name="statisticsDate")
    private Date statisticsDate;
    /**
    * 设备种类
    */
    @ApiModelProperty(value = "设备种类",name="deviceKind")
    private String deviceKind;
    /**
    * 设备型号
    */
    @ApiModelProperty(value = "设备型号",name="deviceModelId")
    private String deviceModelId;
    /**
    * 在线数
    */
    @ApiModelProperty(value = "在线数",name="onlineNum")
    private Integer onlineNum;
    /**
    * 设备数量
    */
    @ApiModelProperty(value = "设备数量",name="deviceNum")
    private Integer deviceNum;
    /**
    * 项目ID
    */
    @ApiModelProperty(value = "项目ID",name="projectId")
    private String projectId;

    /**
     * 完好数
     */
    @ApiModelProperty(value = "完好数",name = "intactNum")
    private Integer intactNum;
}
