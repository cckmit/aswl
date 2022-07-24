package com.aswl.as.ibrs.api.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author jk
 * @version 1.0.0
 * @create 2020/4/16 16:54
 */
@Data
public class StatisticsVo implements Serializable {
    private String dateTime;
    private Double rat;
    private String repairTime;
}
