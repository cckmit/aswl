package com.aswl.as.iface.utils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.UnsupportedEncodingException;

/**
 * @author jk
 * @version 1.0.0
 * @create 2020/1/8 11:50
 */
public class Base64 {
    public static void main(String[] args){
        String result= decodeBase64("MTIzNDU2OmFzd2w=");
        System.out.println(result);
    }

    //加密
    public static String encodeBase64(String str){
        byte[]  b = null;
        String s = null;
        try {
            b = str.getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if( b != null){
            s = new BASE64Encoder().encode(b);
        }
        return s;

    }

    //解密
    public static String decodeBase64(String str){
        byte[] b = null;
        String result = null;
        if(str != null){
            BASE64Decoder decoder = new BASE64Decoder();
            try {
                b = decoder.decodeBuffer(str);
                result = new String(b, "utf-8");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

}
