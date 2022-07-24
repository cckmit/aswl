package com.aswl.as.ibrs.api.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class PeriodScopeDto implements Serializable {

    private static final long serialVersionUID = 1L;

    //周期
    private String period;

    //周期Id
    private String periodId;

    //分数
    private String totalScope;

    //费用
    private String totalFee;

    //当前周期的指标的分数
    private List<LabelScopeDto> labelScopeDtoList;

    //当前周期详细扣分记录
    private List<DeductScopeDto> deductScopeDtoList;
}
