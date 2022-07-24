package com.aswl.as.ibrs.api.vo;

import lombok.*;
import java.util.List;
import java.util.Map;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MaintenanceRateStatisticsVo {

    /**
     * 故障派单数
     */
    private Long dispatchCount;

    /**
     * 告警数
     */
    private Long alarmCount;

    /**
     * 维修完成数
      */
    private Long finishedCount;

    /**
     * 月度派单明细
     */
    private List<Map> monthlyDispatchDetail;

    /**
     * 月度修复完成明细
     */
    private List<Map> monthlyRepairDetail;

    /**
     * 区域派单率排名
     */
    private List<Map> regionDispatchRateTop;

    /**
     * 区域修复率排名
     */
    private List<Map> regionRepairRateTop;
}
