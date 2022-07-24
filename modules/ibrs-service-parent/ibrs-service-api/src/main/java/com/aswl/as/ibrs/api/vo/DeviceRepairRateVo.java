package com.aswl.as.ibrs.api.vo;
import lombok.Data;
import java.io.Serializable;

/** 设备修复率VO
 * @author df
 * @date 2021/01/16 14:09
 */
@Data
public class DeviceRepairRateVo implements Serializable {

    /**
     * 城市
     */
    private String cityName;

    /**
     * 修复数
     */
    private Integer repairNum;


    /**
     * 工单数
     */
    private Integer  runNum;

}
