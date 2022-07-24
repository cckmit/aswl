package com.aswl.as.asos.common.utils;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 简单版LRUMap
 */
public class LRUMap<K, V> extends LinkedHashMap {

    private int maxSize=20;

    public LRUMap(int size){
        super(size,0.75f,true);
        this.maxSize=size;
    };

    protected boolean removeEldestEntry(Map.Entry eldest) {
        return (size()>maxSize);
    }

}
