package com.aswl.as.ibrs.api.vo;
import lombok.Data;
import java.io.Serializable;

/**
 * 设备在线率完好率Vo
 * @author df
 * @date 2021/01/15 15:25
 */
@Data
public class DeviceOnlineIntactVo implements Serializable {

    /**
     * 统计时间
     */
    private String statisticsDate;

    /**
     * 在线数
     */
    private Integer onlineNum;

    /**
     * 总设备数
     */
    private Integer deviceNum;


    /**
     * 故障数
     */
    private Integer faultNum;



    /**
     * 在线率
     */
    private double onlineRate;


    /**
     * 完好率
     */
    private double intactRate;

}
