package com.aswl.as.common.core.utils;

import java.math.BigDecimal;

/**
 * 根据两点间经纬度坐标（double值），计算两点间距离，单位为米 
 * @author df
 * @date 2021/11/03 11:25
 */
public class MapDistance {

    private static final double EARTH_RADIUS = 6378137;
    private static double rad(double d)
    {
        return d * Math.PI / 180.0;
    }

    /** 
     * 根据两点间经纬度坐标（double值），计算两点间距离，单位为米 
     * @param lng1 当前位置经度
     * @param lat1 当前维度
     * @param lng2 传入经度
     * @param lat2 传入维度
     * @return double
     */
    public static double GetDistance(double lng1, double lat1, double lng2, double lat2)
    {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a/2),2) +
                Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2),2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        return s;
    }


    /**
     * @param args
     */
    public static void main(String[] args)
    {
        // TODO 自动生成方法存根  
        double distance = GetDistance(113.459615,23.160854,113.351560,23.141300);
        BigDecimal b   =   new   BigDecimal(distance/1000);
        System.out.println("Distance is:"+distance/1000);
        System.out.println("Distance is:" +  b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue());
    }
}
