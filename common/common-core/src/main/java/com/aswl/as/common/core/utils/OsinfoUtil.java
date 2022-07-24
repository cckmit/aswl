package com.aswl.as.common.core.utils;

/**
 * 获取操作系统类型
 * @author dingfei
 * @date 2019-12-04 17:38
 * @Version V1
 */
public class OsinfoUtil {
    private static String OS = System.getProperty("os.name").toLowerCase();

    /**
     * linux
     * @return boolean
     */
    public static boolean isLinux(){
        return OS.indexOf("linux")>=0;
    }

    /**
     *  windows
     * @return boolean
     */
    public static boolean isWindows(){
        return OS.indexOf("windows")>=0;
    }
}
