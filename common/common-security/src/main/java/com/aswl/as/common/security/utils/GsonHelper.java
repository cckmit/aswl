package com.aswl.as.common.security.utils;

import com.google.gson.Gson;

import java.lang.reflect.Type;

/**
 * 基于Gson的json工具类
 *
 * @author aswl.com
 * @date 2017-11-23 18:03
 */
public class GsonHelper {

    /**
     * Gson对象
     */
    private static final Gson gson = new Gson();

    /**
     * 单例
     */
    private volatile static GsonHelper instance;

    /**
     * 获取单例
     *
     * @author aswl.com
     * @date 2017/11/23 18:10
     */
    public static GsonHelper getInstance() {
        if (instance == null) {
            synchronized (GsonHelper.class) {
                if (instance == null)
                    instance = new GsonHelper();
            }
        }
        return instance;
    }


    /**
     * 将json转为对象
     *
     * @param json  json
     * @param clazz clazz
     * @return T
     * @author aswl.com
     * @date 2017/11/23 18:09
     */
    public <T> T fromJson(String json, Class<T> clazz) {
        return gson.fromJson(json, clazz);
    }

    /**
     * 将json转为对象
     *
     * @param json json
     * @param type type
     * @return T
     * @author aswl.com
     * @date 2017/11/28 15:41
     */
    public <T> T fromJson(String json, Type type) {
        return gson.fromJson(json, type);
    }

    /**
     * 将对象转为json
     *
     * @param src
     * @return String
     * @author aswl.com
     * @date 2017/11/23 18:09
     */
    public String toJson(Object src) {
        return gson.toJson(src);
    }
}
