package com.aswl.as.ibrs.api.vo;

import lombok.*;
import java.util.List;
import java.util.Map;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FlowMaintenanceTrendVo {

    /**
     * 告警数,派单数 完成数
     */
    private Map count;

    /**
     * 告警数趋势
     */
    Map<String,Long> AlarmTrend;

    /**
     * 派单数趋势
     */
    Map<String,Long> dispatchTrend;

    /**
     * 完成数趋势
     */
    Map<String,Long> doneTrent;

    /**
     * 修复数趋势
     */
    Map<String,Long> repairTrent;

    /**
     * 未派单数趋势
     */
    Map<String,Long> noDispatchTrend;

}
