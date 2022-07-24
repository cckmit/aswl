package com.aswl.as.ibrs.api.module;

import java.util.Date;
import com.aswl.as.common.core.persistence.BaseEntity;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 设备分控板-电源输出Entity
 *
 * @author df
 * @date 2021/07/26 19:31
 */

@ApiModel(value = "EventSecCtlPowerOutput", description = "设备分控板-电源输出Entity")
@Data
public class EventSecCtlPowerOutput extends BaseEntity<EventSecCtlPowerOutput> {
    /**
     * 统一事件ID
     */

    @ApiModelProperty(value = "统一事件ID", name = "uEventId")
    private String uEventId;
    /**
     * 设备ID
     */

    @ApiModelProperty(value = "设备ID", name = "deviceId")
    private String deviceId;
    /**
     * 区域编码
     */

    @ApiModelProperty(value = "区域编码", name = "regionNo")
    private String regionNo;
    /**
     * 接收时间(从1970-01-01 08-00-00到现在的秒)
     */

    @ApiModelProperty(value = "接收时间(从1970-01-01 08-00-00到现在的秒)", name = "recTime")
    private Integer recTime;
    /**
     * 电源1电压
     */

    @ApiModelProperty(value = "电源1电压", name = "vol01")
    private Double vol01;
    /**
     * 电源2电压
     */

    @ApiModelProperty(value = "电源2电压", name = "vol02")
    private Double vol02;
    /**
     * 电源3电压
     */

    @ApiModelProperty(value = "电源3电压", name = "vol03")
    private Double vol03;
    /**
     * 电源4电压
     */

    @ApiModelProperty(value = "电源4电压", name = "vol04")
    private Double vol04;
    /**
     * 电源5电压
     */

    @ApiModelProperty(value = "电源5电压", name = "vol05")
    private Double vol05;
    /**
     * 电源6电压
     */

    @ApiModelProperty(value = "电源6电压", name = "vol06")
    private Double vol06;
    /**
     * 电源7电压
     */

    @ApiModelProperty(value = "电源7电压", name = "vol07")
    private Double vol07;
    /**
     * 电源8电压
     */

    @ApiModelProperty(value = "电源8电压", name = "vol08")
    private Double vol08;
    /**
     * 电源9电压
     */

    @ApiModelProperty(value = "电源9电压", name = "vol09")
    private Double vol09;
    /**
     * 电源10电压
     */

    @ApiModelProperty(value = "电源10电压", name = "vol10")
    private Double vol10;
    /**
     * 电源11电压
     */

    @ApiModelProperty(value = "电源11电压", name = "vol11")
    private Double vol11;
    /**
     * 电源12电压
     */

    @ApiModelProperty(value = "电源12电压", name = "vol12")
    private Double vol12;
    /**
     * 电源13电压
     */

    @ApiModelProperty(value = "电源13电压", name = "vol13")
    private Double vol13;
    /**
     * 电源14电压
     */

    @ApiModelProperty(value = "电源14电压", name = "vol14")
    private Double vol14;
    /**
     * 电源15电压
     */

    @ApiModelProperty(value = "电源15电压", name = "vol15")
    private Double vol15;
    /**
     * 电源16电压
     */

    @ApiModelProperty(value = "电源16电压", name = "vol16")
    private Double vol16;
    /**
     * 电源1功率
     */

    @ApiModelProperty(value = "电源1功率", name = "rate01")
    private Double rate01;
    /**
     * 电源2功率
     */

    @ApiModelProperty(value = "电源2功率", name = "rate02")
    private Double rate02;
    /**
     * 电源3功率
     */

    @ApiModelProperty(value = "电源3功率", name = "rate03")
    private Double rate03;
    /**
     * 电源4功率
     */

    @ApiModelProperty(value = "电源4功率", name = "rate04")
    private Double rate04;
    /**
     * 电源5功率
     */

    @ApiModelProperty(value = "电源5功率", name = "rate05")
    private Double rate05;
    /**
     * 电源6功率
     */

    @ApiModelProperty(value = "电源6功率", name = "rate06")
    private Double rate06;
    /**
     * 电源7功率
     */

    @ApiModelProperty(value = "电源7功率", name = "rate07")
    private Double rate07;
    /**
     * 电源8功率
     */

    @ApiModelProperty(value = "电源8功率", name = "rate08")
    private Double rate08;
    /**
     * 电源9功率
     */

    @ApiModelProperty(value = "电源9功率", name = "rate09")
    private Double rate09;
    /**
     * 电源10功率
     */

    @ApiModelProperty(value = "电源10功率", name = "rate10")
    private Double rate10;
    /**
     * 电源11功率
     */

    @ApiModelProperty(value = "电源11功率", name = "rate11")
    private Double rate11;
    /**
     * 电源12功率
     */

    @ApiModelProperty(value = "电源12功率", name = "rate12")
    private Double rate12;
    /**
     * 电源13功率
     */

    @ApiModelProperty(value = "电源13功率", name = "rate13")
    private Double rate13;
    /**
     * 电源14功率
     */

    @ApiModelProperty(value = "电源14功率", name = "rate14")
    private Double rate14;
    /**
     * 电源15功率
     */

    @ApiModelProperty(value = "电源15功率", name = "rate15")
    private Double rate15;
    /**
     * 电源16功率
     */

    @ApiModelProperty(value = "电源16功率", name = "rate16")
    private Double rate16;
    /**
     * 电源1电量
     */

    @ApiModelProperty(value = "电源1电量", name = "elec01")
    private Double elec01;
    /**
     * 电源2电量
     */

    @ApiModelProperty(value = "电源2电量", name = "elec02")
    private Double elec02;
    /**
     * 电源3电量
     */

    @ApiModelProperty(value = "电源3电量", name = "elec03")
    private Double elec03;
    /**
     * 电源4电量
     */

    @ApiModelProperty(value = "电源4电量", name = "elec04")
    private Double elec04;
    /**
     * 电源5电量
     */

    @ApiModelProperty(value = "电源5电量", name = "elec05")
    private Double elec05;
    /**
     * 电源6电量
     */

    @ApiModelProperty(value = "电源6电量", name = "elec06")
    private Double elec06;
    /**
     * 电源7电量
     */

    @ApiModelProperty(value = "电源7电量", name = "elec07")
    private Double elec07;
    /**
     * 电源8电量
     */

    @ApiModelProperty(value = "电源8电量", name = "elec08")
    private Double elec08;
    /**
     * 电源9电量
     */

    @ApiModelProperty(value = "电源9电量", name = "elec09")
    private Double elec09;
    /**
     * 电源10电量
     */

    @ApiModelProperty(value = "电源10电量", name = "elec10")
    private Double elec10;
    /**
     * 电源11电量
     */

    @ApiModelProperty(value = "电源11电量", name = "elec11")
    private Double elec11;
    /**
     * 电源12电量
     */

    @ApiModelProperty(value = "电源12电量", name = "elec12")
    private Double elec12;
    /**
     * 电源13电量
     */

    @ApiModelProperty(value = "电源13电量", name = "elec13")
    private Double elec13;
    /**
     * 电源14电量
     */

    @ApiModelProperty(value = "电源14电量", name = "elec14")
    private Double elec14;
    /**
     * 电源15电量
     */

    @ApiModelProperty(value = "电源15电量", name = "elec15")
    private Double elec15;
    /**
     * 电源16电量
     */

    @ApiModelProperty(value = "电源16电量", name = "elec16")
    private Double elec16;
    /**
     * 存储时间
     */

    @ApiModelProperty(value = "存储时间", name = "storeTime")
    private Date storeTime;
}
