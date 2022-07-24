package com.aswl.as.ibrs.api.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class LabelScopeDto implements Serializable{

    private static final long serialVersionUID = 1L;

    //指标标题
    private String labelTitle;

    //指标分数
    private String labelScope;

    //指标费用
    private String labelFee;

}
