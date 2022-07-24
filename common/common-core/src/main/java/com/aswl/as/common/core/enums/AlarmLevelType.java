package com.aswl.as.common.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/** 设备告警枚举
 * @author dingfei
 * @date 2020-01-05 16:05
 */

@Getter
@AllArgsConstructor
public enum AlarmLevelType {
    FAULT(1,"故障","fault"),
    WARNING(2,"预警","warning"),
    NORMAL(3,"正常","normal");

    /**
     * 类型
     */
    private Integer type;

    /**
     * 描述
     */
    private String description;
    
    private String descriptionEn;



}
