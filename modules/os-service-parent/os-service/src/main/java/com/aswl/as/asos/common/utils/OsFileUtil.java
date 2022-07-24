package com.aswl.as.asos.common.utils;

import com.aswl.as.common.core.utils.OsinfoUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class OsFileUtil {

    protected static final Log logger = LogFactory.getLog(OsFileUtil.class);

//    private  static final SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyyMMdd");
//    private static final SimpleDateFormat yyyymmddhhmmss = new SimpleDateFormat("yyyyMMddHHmmss");

    public  static String saveErrorTxtByList(List<String> msg ,String winUploadPath,String linuxUploadPath,String saveDir, String name) {
        String  saveFullDir="";
        Date d = new Date();
//        String saveDir = "logs" + File.separator + yyyyMMdd.format(d) + File.separator;
        if (OsinfoUtil.isWindows()){
            saveFullDir= winUploadPath + File.separator + saveDir;
        }else{
            saveFullDir= linuxUploadPath + File.separator + saveDir;
        }
        File saveFile = new File(saveFullDir);
        if (!saveFile.exists()) {
            saveFile.mkdirs();
        }
//        name += yyyymmddhhmmss.format(d) + Math.round(Math.random() * 10000);
//        String saveFilePath = saveFullDir + name + ".txt";
        String saveFilePath = saveFullDir+ File.separator + name ;

        try {
            //封装目的地
            BufferedWriter bw = new BufferedWriter(new FileWriter(saveFilePath));
            //遍历集合
            for (String s : msg) {
                //写数据
                bw.write(s);
                bw.write("\r\n");
            }
            //释放资源
            bw.flush();
            bw.close();
        } catch (Exception e) {
            logger.info("excel导入生成错误日志文件异常:" + e.getMessage());
        }
        return saveDir + name;
    }

}
