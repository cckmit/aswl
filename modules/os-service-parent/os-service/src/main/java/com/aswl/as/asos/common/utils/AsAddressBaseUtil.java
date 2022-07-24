package com.aswl.as.asos.common.utils;

import java.util.LinkedHashMap;

public class AsAddressBaseUtil {

    /**
     * 获取地址库属性的map
     *
     * @return LinkedHashMap
     */
    public static LinkedHashMap<String, String> getAsAddressBaseMap() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        //map.put("id", "设备id");
        map.put("projectCode","项目编码"); //只是拿这个来暂时保存项目编码
        map.put("name", "点位名称");
//        map.put("address", "详细地址");
        map.put("longitude", "经度");
        map.put("latitude", "纬度");
        map.put("ip", "IP");
        map.put("remark", "备注");
        return map;
    }

}
