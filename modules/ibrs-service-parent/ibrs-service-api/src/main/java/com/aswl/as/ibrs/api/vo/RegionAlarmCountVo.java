package com.aswl.as.ibrs.api.vo;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * 每个区域报障数
 */
@Data
@Getter
@Setter
public class RegionAlarmCountVo {

    private String regionName;

    private Integer RegionAlarmCount;

}
