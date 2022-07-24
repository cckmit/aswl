package com.aswl.as.ibrs.api.module;
import java.util.Date;
import com.aswl.as.common.core.persistence.BaseEntity;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
*
* 设备事件-光口Entity
* @author zgl
* @date 2019-11-01 14:04
*/

@ApiModel(value = "EventSfp",description = "设备事件-光口Entity")
@Data
public class EventSfp extends BaseEntity<EventSfp> {
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
    * 光口1
    */

    @ApiModelProperty(value = "光口1",name="fld01")
    private Integer fld01 = -9;
    /**
    * 光口2
    */

    @ApiModelProperty(value = "光口2",name="fld02")
    private Integer fld02 = -9;
    /**
    * 光口3
    */

    @ApiModelProperty(value = "光口3",name="fld03")
    private Integer fld03 = -9;
    /**
    * 光口4
    */

    @ApiModelProperty(value = "光口4",name="fld04")
    private Integer fld04 = -9;
    /**
    * 光口5
    */

    @ApiModelProperty(value = "光口5",name="fld05")
    private Integer fld05 = -9;
    /**
    * 光口6
    */

    @ApiModelProperty(value = "光口6",name="fld06")
    private Integer fld06 = -9;
    /**
    * 光口7
    */

    @ApiModelProperty(value = "光口7",name="fld07")
    private Integer fld07 = -9;
    /**
    * 光口8
    */

    @ApiModelProperty(value = "光口8",name="fld08")
    private Integer fld08 = -9;
    /**
    * 预留9
    */

    @ApiModelProperty(value = "预留9",name="fld09")
    private Integer fld09 = -9;
    /**
    * 预留10
    */

    @ApiModelProperty(value = "预留10",name="fld10")
    private Integer fld10 = -9;
    /**
    * 预留11
    */

    @ApiModelProperty(value = "预留11",name="fld11")
    private Integer fld11 = -9;
    /**
    * 预留12
    */

    @ApiModelProperty(value = "预留12",name="fld12")
    private Integer fld12 = -9;
    /**
    * 预留13
    */

    @ApiModelProperty(value = "预留13",name="fld13")
    private Integer fld13 = -9;
    /**
    * 预留14
    */

    @ApiModelProperty(value = "预留14",name="fld14")
    private Integer fld14 = -9;
    /**
    * 预留15
    */

    @ApiModelProperty(value = "预留15",name="fld15")
    private Integer fld15 = -9;
    /**
    * 预留16
    */

    @ApiModelProperty(value = "预留16",name="fld16")
    private Integer fld16 = -9;
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
    * SAAS租户编码
    */

    @ApiModelProperty(value = "SAAS租户编码",name="tenantCode")
    private String tenantCode;
}
