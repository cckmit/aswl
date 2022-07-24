package com.aswl.as.asos.common.utils;

import java.util.LinkedHashMap;

/**
 * @author dingfei
 * @date 2019-11-20 13:23
 * @Version V1
 */
public class DeviceUtils {

    /**
     * 获取Device属性的map
     *
     * @return LinkedHashMap
     */
    public static LinkedHashMap<String, String> getOsDeviceMap() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        //map.put("id", "设备id");
        map.put("projectId","项目编码"); //只是拿这个来暂时保存项目编码
        map.put("deviceName", "设备名称");
        map.put("deviceCode", "设备编码");
        map.put("regionCode", "区域编码");
        map.put("deviceModelName", "设备型号");
        map.put("deviceType", "设备类型");
        map.put("address", "地址");
        map.put("ip", "IP");
        map.put("port", "端口");
        map.put("userName", "用户名");
        map.put("password", "密码");
        map.put("longitude", "经度");
        map.put("latitude", "维度");
        map.put("longitudeA", "经度A");
        map.put("latitudeA", "维度A");
        map.put("rj45No", "网络口");
        map.put("fibreOpticalNo", "光纤口");
        map.put("parentDeviceId", "上级设备名称");
        map.put("parentRj45No", "上级网络口");
        map.put("parentFibreOpticalNo", "上级光纤口");
        map.put("parentDcpowerNo", "上级直流电源口");
        map.put("parentAcpowerNo", "上级220V电源口");
        map.put("purchaseDate", "购买日期");
        map.put("guaranteeTime", "保修期");
        map.put("remark", "备注");
        return map;
    }
}
