package com.aswl.as.common.core.persistence;

import com.aswl.as.common.core.persistence.TreeEntity;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

// 该类是为了不同的树节点类型，如 租户-项目-区域
@Data
public class AsOsTreeEntity<T> {

    /**
     * code
     */
    protected String code;

    /**
     * 父节点
     */
    protected Object parent;

    /**
     * 父节点id
     */
    protected String parentId;

    /**
     * 排序号
     */
    protected Integer sort;

    /**
     * 子节点
     */
    protected List<Object> children = new ArrayList<>();

    public void add(Object node) {
        children.add(node);
    }

}
