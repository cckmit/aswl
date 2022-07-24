package com.aswl.as.iface.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class CustomMd5 {

    public static String MD5(String s){
        byte[] b = s.getBytes(StandardCharsets.UTF_8);
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            return s;
        }
        md.reset();
        md.update(b);
        b = md.digest();
        StringBuilder buf = new StringBuilder();
        for (byte value : b) {
            if ((value & 0xff) < 0x10) {
                buf.append("0");
            }
            buf.append(Long.toString(value & 0xff, 16));
        }
        return buf.toString();
    }
}
