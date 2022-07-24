package com.aswl.as.ibrs.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ValueComponent {

    //------------- winUploadPath 和 linuxUploadPath 这两个值用在上传文件，但是运营端也会根据该路径生成的日志判断是否存在 -------------------

    /**
     * windows上传路径
     */
    @Value(value = "${file.path.winUpload}")
    public String winUploadPath;

    /**
     * linux上传路径
     */
    @Value(value = "${linuxfile.path.linuxUpload}")
    public String linuxUploadPath;

}
