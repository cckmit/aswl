package com.aswl.as.ibrs.api.module;

import java.util.Date;

import com.aswl.as.common.core.persistence.BaseEntity;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 设备分控板-电源设置Entity
 *
 * @author df
 * @date 2021/07/26 19:34
 */

@ApiModel(value = "EventSecCtlPowerSet", description = "设备分控板-电源设置Entity")
@Data
public class EventSecCtlPowerSet extends BaseEntity<EventSecCtlPowerSet> {
    
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
     * 电源1过压设定
     */

    @ApiModelProperty(value = "电源1过压设定", name = "overVol01")
    private Double overVol01;
    /**
     * 电源2过压设定
     */

    @ApiModelProperty(value = "电源2过压设定", name = "overVol02")
    private Double overVol02;
    /**
     * 电源3过压设定
     */

    @ApiModelProperty(value = "电源3过压设定", name = "overVol03")
    private Double overVol03;
    /**
     * 电源4过压设定
     */

    @ApiModelProperty(value = "电源4过压设定", name = "overVol04")
    private Double overVol04;
    /**
     * 电源5过压设定
     */

    @ApiModelProperty(value = "电源5过压设定", name = "overVol05")
    private Double overVol05;
    /**
     * 电源6过压设定
     */

    @ApiModelProperty(value = "电源6过压设定", name = "overVol06")
    private Double overVol06;
    /**
     * 电源7过压设定
     */

    @ApiModelProperty(value = "电源7过压设定", name = "overVol07")
    private Double overVol07;
    /**
     * 电源8过压设定
     */

    @ApiModelProperty(value = "电源8过压设定", name = "overVol08")
    private Double overVol08;
    /**
     * 电源9过压设定
     */

    @ApiModelProperty(value = "电源9过压设定", name = "overVol09")
    private Double overVol09;
    /**
     * 电源10过压设定
     */

    @ApiModelProperty(value = "电源10过压设定", name = "overVol10")
    private Double overVol10;
    /**
     * 电源11过压设定
     */

    @ApiModelProperty(value = "电源11过压设定", name = "overVol11")
    private Double overVol11;
    /**
     * 电源12过压设定
     */

    @ApiModelProperty(value = "电源12过压设定", name = "overVol12")
    private Double overVol12;
    /**
     * 电源13过压设定
     */

    @ApiModelProperty(value = "电源13过压设定", name = "overVol13")
    private Double overVol13;
    /**
     * 电源14过压设定
     */

    @ApiModelProperty(value = "电源14过压设定", name = "overVol14")
    private Double overVol14;
    /**
     * 电源15过压设定
     */

    @ApiModelProperty(value = "电源15过压设定", name = "overVol15")
    private Double overVol15;
    /**
     * 电源16过压设定
     */

    @ApiModelProperty(value = "电源16过压设定", name = "overVol16")
    private Double overVol16;
    /**
     * 电源1欠压设定
     */

    @ApiModelProperty(value = "电源1欠压设定", name = "underVol01")
    private Double underVol01;
    /**
     * 电源2欠压设定
     */

    @ApiModelProperty(value = "电源2欠压设定", name = "underVol02")
    private Double underVol02;
    /**
     * 电源3欠压设定
     */

    @ApiModelProperty(value = "电源3欠压设定", name = "underVol03")
    private Double underVol03;
    /**
     * 电源4欠压设定
     */

    @ApiModelProperty(value = "电源4欠压设定", name = "underVol04")
    private Double underVol04;
    /**
     * 电源5欠压设定
     */

    @ApiModelProperty(value = "电源5欠压设定", name = "underVol05")
    private Double underVol05;
    /**
     * 电源6欠压设定
     */

    @ApiModelProperty(value = "电源6欠压设定", name = "underVol06")
    private Double underVol06;
    /**
     * 电源7欠压设定
     */

    @ApiModelProperty(value = "电源7欠压设定", name = "underVol07")
    private Double underVol07;
    /**
     * 电源8欠压设定
     */

    @ApiModelProperty(value = "电源8欠压设定", name = "underVol08")
    private Double underVol08;
    /**
     * 电源9欠压设定
     */

    @ApiModelProperty(value = "电源9欠压设定", name = "underVol09")
    private Double underVol09;
    /**
     * 电源10欠压设定
     */

    @ApiModelProperty(value = "电源10欠压设定", name = "underVol10")
    private Double underVol10;
    /**
     * 电源11欠压设定
     */

    @ApiModelProperty(value = "电源11欠压设定", name = "underVol11")
    private Double underVol11;
    /**
     * 电源12欠压设定
     */

    @ApiModelProperty(value = "电源12欠压设定", name = "underVol12")
    private Double underVol12;
    /**
     * 电源13欠压设定
     */

    @ApiModelProperty(value = "电源13欠压设定", name = "underVol13")
    private Double underVol13;
    /**
     * 电源14欠压设定
     */

    @ApiModelProperty(value = "电源14欠压设定", name = "underVol14")
    private Double underVol14;
    /**
     * 电源15欠压设定
     */

    @ApiModelProperty(value = "电源15欠压设定", name = "underVol15")
    private Double underVol15;
    /**
     * 电源16欠压设定
     */

    @ApiModelProperty(value = "电源16欠压设定", name = "underVol16")
    private Double underVol16;
    /**
     * 电源1过流设定
     */

    @ApiModelProperty(value = "电源1过流设定", name = "overElec01")
    private Double overElec01;
    /**
     * 电源2过流设定
     */

    @ApiModelProperty(value = "电源2过流设定", name = "overElec02")
    private Double overElec02;
    /**
     * 电源3过流设定
     */

    @ApiModelProperty(value = "电源3过流设定", name = "overElec03")
    private Double overElec03;
    /**
     * 电源4过流设定
     */

    @ApiModelProperty(value = "电源4过流设定", name = "overElec04")
    private Double overElec04;
    /**
     * 电源5过流设定
     */

    @ApiModelProperty(value = "电源5过流设定", name = "overElec05")
    private Double overElec05;
    /**
     * 电源6过流设定
     */

    @ApiModelProperty(value = "电源6过流设定", name = "overElec06")
    private Double overElec06;
    /**
     * 电源7过流设定
     */

    @ApiModelProperty(value = "电源7过流设定", name = "overElec07")
    private Double overElec07;
    /**
     * 电源8过流设定
     */

    @ApiModelProperty(value = "电源8过流设定", name = "overElec08")
    private Double overElec08;
    /**
     * 电源9过流设定
     */

    @ApiModelProperty(value = "电源9过流设定", name = "overElec09")
    private Double overElec09;
    /**
     * 电源10过流设定
     */

    @ApiModelProperty(value = "电源10过流设定", name = "overElec10")
    private Double overElec10;
    /**
     * 电源11过流设定
     */

    @ApiModelProperty(value = "电源11过流设定", name = "overElec11")
    private Double overElec11;
    /**
     * 电源12过流设定
     */

    @ApiModelProperty(value = "电源12过流设定", name = "overElec12")
    private Double overElec12;
    /**
     * 电源13过流设定
     */

    @ApiModelProperty(value = "电源13过流设定", name = "overElec13")
    private Double overElec13;
    /**
     * 电源14过流设定
     */

    @ApiModelProperty(value = "电源14过流设定", name = "overElec14")
    private Double overElec14;
    /**
     * 电源15过流设定
     */

    @ApiModelProperty(value = "电源15过流设定", name = "overElec15")
    private Double overElec15;
    /**
     * 电源16过流设定
     */

    @ApiModelProperty(value = "电源16过流设定", name = "overElec16")
    private Double overElec16;
    /**
     * 存储时间
     */

    @ApiModelProperty(value = "存储时间", name = "storeTime")
    private Date storeTime;
}
