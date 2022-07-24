package com.aswl.as.asos.common.utils;

public class OsGlobalData {

    //常用的租户信息(写出来是因为，写在service里面的话，会不断切换数据库连接，浪费性能)
    public static LRUMap<String, String> COMMON_TENANT_NAME_MAP=new LRUMap<String, String>(1000);

    //常用的设备种类，实际上没多少个设备种类，也是放到LRUMap里面，快速取
//    public static LRUMap<String, String> COMMON_KIND_NAME_MAP=new LRUMap<String, String>(1000);

    //黑名单map（实际上很少用，请求的存到redis上面也行）
    public static LRUMap<String, String> BLACK_LIST_MAP=new LRUMap<String, String>(1000);

    //IBRS所设置windows下载文件夹设置
    public static String IBRS_WIN_UPLOAD_PATH="d://upload";

    //IBRS所设置的linux下载文件夹设置
    public static String IBRS_LINUX_UPLOAD_PATH="/upload";

}
