package com.aswl.as.ibrs.api.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author df
 * @date 2021/06/02 09:46
 */
@Data
public class UnitElectricStatisticsVo implements Serializable {

    /**
     * 单位ID
     */
    private String id;
    /**
     * 单位名称
     */
    private String name;

    /**
     * 电量值
     */
    private double electricNum;

    /**
     * 电费单价
     */
    private double electricPrice;

    /**
     * 电费费用
     */
    private double electricFee;

    /**
     * 所属月份
     */
    private String month;
    
}
