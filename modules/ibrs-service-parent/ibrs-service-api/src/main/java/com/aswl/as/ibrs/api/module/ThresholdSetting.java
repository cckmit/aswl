package com.aswl.as.ibrs.api.module;
import java.util.Date;
import com.aswl.as.common.core.persistence.BaseEntity;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
*
* 阈值设置表Entity
* @author df
* @date 2021/09/28 09:47
*/

@ApiModel(value = "ThresholdSetting",description = "阈值设置表Entity")
@Data
public class ThresholdSetting extends BaseEntity<ThresholdSetting> {
    /**
    * 温度值
    */

    @ApiModelProperty(value = "温度值",name="temperatureValue")
    private String temperatureValue;
    /**
    * 电流
    */

    @ApiModelProperty(value = "电流",name="electricCurrentValue")
    private String electricCurrentValue;
    /**
    * 电量
    */

    @ApiModelProperty(value = "电量",name="electricQuantityValue")
    private String electricQuantityValue;
    /**
    * 电压1
    */

    @ApiModelProperty(value = "电压1",name="voltageFld01")
    private String voltageFld01;
    /**
    * 电压2
    */

    @ApiModelProperty(value = "电压2",name="voltageFld02")
    private String voltageFld02;
    /**
    * 电压3
    */

    @ApiModelProperty(value = "电压3",name="voltageFld03")
    private String voltageFld03;
    /**
    * 电压4
    */

    @ApiModelProperty(value = "电压4",name="voltageFld04")
    private String voltageFld04;
    /**
    * 电压5
    */

    @ApiModelProperty(value = "电压5",name="voltageFld05")
    private String voltageFld05;
    /**
    * 电压6
    */

    @ApiModelProperty(value = "电压6",name="voltageFld06")
    private String voltageFld06;
    /**
    * 电压7
    */

    @ApiModelProperty(value = "电压7",name="voltageFld07")
    private String voltageFld07;
    /**
    * 电压8
    */

    @ApiModelProperty(value = "电压8",name="voltageFld08")
    private String voltageFld08;
    /**
    * 用户ID
    */

    @ApiModelProperty(value = "用户ID",name="userId")
    private String userId;
    /**
    * 项目ID
    */

    @ApiModelProperty(value = "项目ID",name="projectId")
    private String projectId;
}
