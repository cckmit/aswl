package com.aswl.as.ibrs.api.vo;

import lombok.*;

import java.util.List;
import java.util.Map;

/**
 * 告警统计Vo
 */
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AlarmStatisticsVo {
    /**
     * 区域下所有的保障数
     */
    private Integer allRegionAlarmCount;

    /**
     * 每个区域的故障数
     */
    private List<RegionAlarmCountVo> regionAlarmCount;

    /**
     *所有型号的保障数
     */
    private Integer allModelAlarmCount;

    /**
     * 每个设备型号故障数
     */
    private List<DeviceModelAlarmCountVo> modelAlarmCount;

}
