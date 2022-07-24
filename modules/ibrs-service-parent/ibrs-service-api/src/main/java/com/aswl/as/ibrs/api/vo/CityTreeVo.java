package com.aswl.as.ibrs.api.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 城市树菜单
 * @author df
 * @date 2021/01/18 17:11
 */
@Data
public class CityTreeVo implements Serializable {
    private Integer id;
    private String parentId;
    private String name;
    private String code;
    private Integer type;
    private List<CityTreeVo> children = new ArrayList<>();
}
