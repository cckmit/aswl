/**
 * Copyright (c) 2019 aswl.com All rights reserved.
 *
 * https://www.gzaswl.net
 *
 * 2019.11
 */

package com.aswl.as.asos.common.utils;

/**
 * Redis所有Keys
 *
 * @author admin@gzaswl.net
 */
public class RedisKeys {

    public static String getSysConfigKey(String key){
        return "sys:config:" + key;
    }
}
