package com.aswl.as.ibrs.api.module;
import java.util.Date;
import com.aswl.as.common.core.persistence.BaseEntity;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 设备事件-输入Entity
 *
 * @author df
 * @date 2022/07/07 16:11
 */

@ApiModel(value = "EventInput", description = "设备事件-输入Entity")
@Data
public class EventInput extends BaseEntity<EventInput> {
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
     * 通道有效位1
     */

    @ApiModelProperty(value = "通道有效位1", name = "passway1")
    private String passway1;
    /**
     * 通道有效位2
     */

    @ApiModelProperty(value = "通道有效位2", name = "passway2")
    private String passway2;
    /**
     * 输入1
     */

    @ApiModelProperty(value = "输入1", name = "input01")
    private Integer input01;
    /**
     * 输入2
     */

    @ApiModelProperty(value = "输入2", name = "input02")
    private Integer input02;
    /**
     * 输入3
     */

    @ApiModelProperty(value = "输入3", name = "input03")
    private Integer input03;
    /**
     * 输入4
     */

    @ApiModelProperty(value = "输入4", name = "input04")
    private Integer input04;
    /**
     * 输入5
     */

    @ApiModelProperty(value = "输入5", name = "input05")
    private Integer input05;
    /**
     * 输入6
     */

    @ApiModelProperty(value = "输入6", name = "input06")
    private Integer input06;
    /**
     * 输入7
     */

    @ApiModelProperty(value = "输入7", name = "input07")
    private Integer input07;
    /**
     * 输入8
     */

    @ApiModelProperty(value = "输入8", name = "input08")
    private Integer input08;
    /**
     * 输入9
     */

    @ApiModelProperty(value = "输入9", name = "input09")
    private Integer input09;
    /**
     * 输入10
     */

    @ApiModelProperty(value = "输入10", name = "input10")
    private Integer input10;
    /**
     * 输入11
     */

    @ApiModelProperty(value = "输入11", name = "input11")
    private Integer input11;
    /**
     * 输入12
     */

    @ApiModelProperty(value = "输入12", name = "input12")
    private Integer input12;
    /**
     * 输入13
     */

    @ApiModelProperty(value = "输入13", name = "input13")
    private Integer input13;
    /**
     * 输入14
     */

    @ApiModelProperty(value = "输入14", name = "input14")
    private Integer input14;
    /**
     * 输入15
     */

    @ApiModelProperty(value = "输入15", name = "input15")
    private Integer input15;
    /**
     * 输入16
     */

    @ApiModelProperty(value = "输入16", name = "input16")
    private Integer input16;
    /**
     * 存储时间
     */

    @ApiModelProperty(value = "存储时间", name = "storeTime")
    private Date storeTime;
}
