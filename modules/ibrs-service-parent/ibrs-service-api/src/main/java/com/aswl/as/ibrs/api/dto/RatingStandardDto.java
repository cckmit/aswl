package com.aswl.as.ibrs.api.dto;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
*
* 告警评级设置Dto
* @author hzj
* @date 2021/01/11 16:03
*/

@ApiModel(value = "RatingStandardDto",description = "告警评级设置Dto")
@Data
public class RatingStandardDto implements Serializable {

private static final long serialVersionUID = 1L;

    /**
    * ID主键
    */
    @ApiModelProperty(value = "ID主键",name="id")
    private String id;
    /**
    * 评级名称
    */
    @ApiModelProperty(value = "评级名称",name="name")
    private String name;
    /**
    * 在线率上限
    */
    @ApiModelProperty(value = "在线率上限",name="onlineRateMax")
    private Double onlineRateMax;
    /**
    * 在线率下限
    */
    @ApiModelProperty(value = "在线率下限",name="onlineRateMin")
    private Double onlineRateMin;
    /**
    * 完好率上限
    */
    @ApiModelProperty(value = "完好率上限",name="normalRateMax")
    private Double normalRateMax;
    /**
    * 完好率下限
    */
    @ApiModelProperty(value = "完好率下限",name="normalRateMin")
    private Double normalRateMin;
    /**
    * 系统编码
    */
    @ApiModelProperty(value = "系统编码",name="applicationCode")
    private String applicationCode;
    /**
    * 租户编码
    */
    @ApiModelProperty(value = "租户编码",name="tenantCode")
    private String tenantCode;

}
