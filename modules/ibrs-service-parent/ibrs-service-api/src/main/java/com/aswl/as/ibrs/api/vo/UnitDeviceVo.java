package com.aswl.as.ibrs.api.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 用电单位点位VO
 * @author df
 * @date 2021/12/01 16:53
 */
@Data
public class UnitDeviceVo implements Serializable {
    /**
     * 单位名称
     */
    @ApiModelProperty(value = "单位名称",name="name")
    private String name;

    /**
     * 点位名称
     */
    @ApiModelProperty(value = "点位名称",name="deviceName")
    private String deviceName;

    /**
     * 总电量
     */
    @ApiModelProperty(value = "总电量",name="electricTotal")
    private double electricTotal;


    /**
     * 电价
     */
    @ApiModelProperty(value = "电价",name="electricPrice")
    private double electricPrice;

    /**
     * 时间
     */
    @ApiModelProperty(value = "时间",name="statisticsDate")
    private String  statisticsDate;
}
