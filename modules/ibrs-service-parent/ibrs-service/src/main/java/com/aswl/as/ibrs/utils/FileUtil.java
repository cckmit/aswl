package com.aswl.as.ibrs.utils;

import com.aswl.as.common.core.utils.OsinfoUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author dingfei
 * @date 2019-12-04 10:07
 * @Version V1
 */
@Slf4j
@Component
public class FileUtil {
    private  static final SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyyMMdd");
    private static final SimpleDateFormat yyyymmddhhmmss = new SimpleDateFormat("yyyyMMddHHmmss");

    private static String uploadPath;
    @Value("${file.path.winUpload}")
    public void setUploadPath(String uploadPath) {
        FileUtil.uploadPath = uploadPath;
    }

    private static String uploadPath2;
    @Value("${linuxfile.path.linuxUpload}")
    public void setUploadPath2(String uploadPath) {
        FileUtil.uploadPath2 = uploadPath;
    }
    public  static String saveErrorTxtByList(List<String> msg, String name) {
        String  saveFullDir="";
        Date d = new Date();
        String saveDir = "logs" + File.separator + yyyyMMdd.format(d) + File.separator;
        if (OsinfoUtil.isWindows()){
            saveFullDir= uploadPath + File.separator + saveDir;
        }else{
            saveFullDir= uploadPath2 + File.separator + saveDir;
        }
        File saveFile = new File(saveFullDir);
        if (!saveFile.exists()) {
            saveFile.mkdirs();
        }
        name += yyyymmddhhmmss.format(d) + Math.round(Math.random() * 10000);
        String saveFilePath = saveFullDir + name + ".txt";

        try {
            //封装目的地
            BufferedWriter bw = new BufferedWriter(new FileWriter(saveFilePath));
            //遍历集合
            for (String s : msg) {
                /*//写数据
                if (s.indexOf("_") > 0) {
                    String arr[] = s.split("_");
                    bw.write("第" + arr[0] + "行:" + arr[1]);
                } else {
                    bw.write(s);
                }*/
                //bw.newLine();
                bw.write(s);
                bw.write("\r\n");
            }
            //释放资源
            bw.flush();
            bw.close();
        } catch (Exception e) {
            log.info("excel导入生成错误日志文件异常:" + e.getMessage());
        }
        return saveDir + name + ".txt";
    }
}
