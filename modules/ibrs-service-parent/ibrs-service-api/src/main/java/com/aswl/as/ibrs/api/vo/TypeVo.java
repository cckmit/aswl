package com.aswl.as.ibrs.api.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jk
 * @version 1.0.0
 * @create 2019/10/23 13:28
 */
@Getter@Setter
public class TypeVo {
    private String label;
    private String value;
    private List<ModelVo> children = new ArrayList<>();
}
