package com.aswl.as.asos.common.utils;

import java.io.File;

/**
 * 文件操作工具类
 * @author df
 * @date 2020/12/18 15:58
 */
public class FileUtils {

    /**
     * 删除文件及目录
     * @param folder
     */
    public static void deleteFolder (File folder){
        File[] files =folder.listFiles();
        if (files != null){
            for (File f: files) {
                if (f.isDirectory()){
                    deleteFolder(f);
                }else{
                    f.delete();
                }

            }
        }
        //删除文件夹
        folder.delete();
    }
}
