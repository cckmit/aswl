package com.aswl.as.setting.utils;

import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JedisUtil {

    private static StringRedisSerializer stringRedisSerializer_key = new StringRedisSerializer();
    private static JdkSerializationRedisSerializer jdkSerializationRedisSerializer_value = new JdkSerializationRedisSerializer();

    /**
     * 声明连接池对象，声明成static类型，保证项目在启动时，就加载连接池
     */
    private static JedisPool pool;

    /***
     * Jedis连接池连接的最大值
     */
    private static int maxTotal = 20;

    /***
     * Jedis连接池中最大的空闲连接数
     */
    private static int maxIdel = 10;

    /***
     * Jedis连接池中最小的空闲连接数
     */
    private static int miniIdel = 3;

    /**
     * IP地址
     */
    private static String ip = "127.0.0.1";
    /**
     * 端口号
     */
    private static int port = 6379;

    /**
     * 初始化Jedis连接池
     */
    public static void initJedisPool(){
        synchronized (JedisUtil.class){
            if(pool == null){
                JedisPoolConfig config = new JedisPoolConfig();
                config.setMaxTotal(maxTotal);
                config.setMaxIdle(maxIdel);
                config.setMinIdle(miniIdel);
                //创建连接池，超时时间为2秒
                pool = new JedisPool(config,ip,port,2000);
            }
        }
    }

    /**
     * 静态代码块初始化连接池
     */
    static {
        initJedisPool();
    }

    /**
     * 从连接池中获取Jedis连接
     * @return jedis连接
     */
    public static Jedis getJedis(){
        return pool.getResource();
    }


    /**
     * 回收jedis连接
     * @param jedis  需要关闭的jedis连接对象
     */
    public static void returnJedis(Jedis jedis) {
        //3.0之前所用的归还连接方式，现在使用close
        // pool.returnResource(jedis);
        if ( null != jedis ) {
            jedis.close();
        }
    }

    /**
     * 设置Strring类型的值
     * @param key
     * @param value
     * @return
     */
    public static boolean setKey(String key, String value) {

        Jedis jedis = null;

        try {
            jedis = getJedis();
            String set = jedis.set(stringRedisSerializer_key.serialize(key), jdkSerializationRedisSerializer_value.serialize(value));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            returnJedis(jedis);
        }
        return true;

    }


    /***
     * 获取String类型的值
     * @param key
     * @return
     */
    public static String getValue(String key) {

        Jedis jedis = null;
        String value = "";
        try {
            jedis = getJedis();
            value = jedis.get(key);
            return value;
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            returnJedis(jedis);
        }
        return value;
    }

    /***
     * 根据key值删除数据,可批量删除
     * @param keys
     * @return
     */
    public static boolean delKey (String... keys) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            jedis.del(keys);
            return true;
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            returnJedis(jedis);
        }
        return false;
    }


    /**
     * 判断key值是否存在
     * @param key
     * @return
     */
    public static boolean isExists(String key) {
        Jedis jedis = null;
        boolean isExists = false;
        try{
            jedis = getJedis();
            isExists = jedis.exists(key);
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            returnJedis(jedis);
        }
        return isExists;
    }
}
