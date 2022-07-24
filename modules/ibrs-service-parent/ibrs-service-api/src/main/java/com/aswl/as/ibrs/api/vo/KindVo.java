package com.aswl.as.ibrs.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jk
 * @version 1.0.0
 * @create 2019/10/23 13:25
 */
@ApiModel(value = "KindVo",description = "设备类型Vo")
@Getter@Setter
public class KindVo {
    @ApiModelProperty(value = "设备类型名称",name = "label")
    private String label;
    @ApiModelProperty(value = "设备类型",name = "value")
    private String value;
    private List<TypeVo> children = new ArrayList<>();
}
