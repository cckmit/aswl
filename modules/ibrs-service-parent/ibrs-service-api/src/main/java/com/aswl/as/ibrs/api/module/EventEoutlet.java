package com.aswl.as.ibrs.api.module;
import java.util.Date;
import com.aswl.as.common.core.persistence.BaseEntity;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
*
* 设备事件-电源电口Entity
* @author zgl
* @date 2019-11-01 14:01
*/

@ApiModel(value = "EventEoutlet",description = "设备事件-电源电口Entity")
@Data
public class EventEoutlet extends BaseEntity<EventEoutlet> {

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
    * 接收时间(从1970-01-01 08-00-00到现在的秒)
    */

    @ApiModelProperty(value = "接收时间(从1970-01-01 08-00-00到现在的秒)",name="recTime")
    private Integer recTime;
    /**
    * 通道有效位1
    */

    @ApiModelProperty(value = "通道有效位1",name="passway1")
    private String passway1;
    /**
    * 通道有效位2
    */

    @ApiModelProperty(value = "通道有效位2",name="passway2")
    private String passway2;
    /**
    * 预留
    */

    @ApiModelProperty(value = "预留",name="passway3")
    private String passway3;
    /**
    * 电口1
    */

    @ApiModelProperty(value = "电口1",name="fld01")
    private Integer fld01 = -9;
    /**
    * 电口2
    */

    @ApiModelProperty(value = "电口2",name="fld02")
    private Integer fld02 = -9;
    /**
    * 电口3
    */

    @ApiModelProperty(value = "电口3",name="fld03")
    private Integer fld03 = -9;
    /**
    * 电口4
    */

    @ApiModelProperty(value = "电口4",name="fld04")
    private Integer fld04 = -9;
    /**
    * 电口5
    */

    @ApiModelProperty(value = "电口5",name="fld05")
    private Integer fld05 = -9;
    /**
    * 电口6
    */

    @ApiModelProperty(value = "电口6",name="fld06")
    private Integer fld06 = -9;
    /**
    * 电口7
    */

    @ApiModelProperty(value = "电口7",name="fld07")
    private Integer fld07 = -9;
    /**
    * 电口8
    */

    @ApiModelProperty(value = "电口8",name="fld08")
    private Integer fld08 = -9;
    /**
    * 电口9
    */

    @ApiModelProperty(value = "电口9",name="fld09")
    private Integer fld09 = -9;
    /**
    * 电口10
    */

    @ApiModelProperty(value = "电口10",name="fld10")
    private Integer fld10 = -9;
    /**
    * 电口11
    */

    @ApiModelProperty(value = "电口11",name="fld11")
    private Integer fld11 = -9;
    /**
    * 电口12
    */

    @ApiModelProperty(value = "电口12",name="fld12")
    private Integer fld12 = -9;
    /**
    * 电口13
    */

    @ApiModelProperty(value = "电口13",name="fld13")
    private Integer fld13 = -9;
    /**
    * 电口14
    */

    @ApiModelProperty(value = "电口14",name="fld14")
    private Integer fld14 = -9;
    /**
    * 电口15
    */

    @ApiModelProperty(value = "电口15",name="fld15")
    private Integer fld15 = -9;
    /**
    * 电口16
    */

    @ApiModelProperty(value = "电口16",name="fld16")
    private Integer fld16 = -9;
    /**
    * 预留17
    */

    @ApiModelProperty(value = "预留17",name="fld17")
    private Integer fld17 = -9;
    /**
    * 预留18
    */

    @ApiModelProperty(value = "预留18",name="fld18")
    private Integer fld18 = -9;
    /**
    * 预留19
    */

    @ApiModelProperty(value = "预留19",name="fld19")
    private Integer fld19 = -9;
    /**
    * 预留20
    */

    @ApiModelProperty(value = "预留20",name="fld20")
    private Integer fld20 = -9;
    /**
    * 预留21
    */

    @ApiModelProperty(value = "预留21",name="fld21")
    private Integer fld21 = -9;
    /**
    * 预留22
    */

    @ApiModelProperty(value = "预留22",name="fld22")
    private Integer fld22 = -9;
    /**
    * 预留23
    */

    @ApiModelProperty(value = "预留23",name="fld23")
    private Integer fld23 = -9;
    /**
    * 预留24
    */

    @ApiModelProperty(value = "预留24",name="fld24")
    private Integer fld24 = -9;
    /**
    * 存储时间
    */

    @ApiModelProperty(value = "存储时间",name="storeTime")
    private Date storeTime;
}
