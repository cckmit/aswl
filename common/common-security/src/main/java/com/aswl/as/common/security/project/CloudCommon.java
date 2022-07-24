package com.aswl.as.common.security.project;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;


/**
 * <p>
 * 云平台常用类
 * </p>
 *
 * @author hfx
 * @since 2019-12-21
 */
@Component
public class CloudCommon implements ApplicationContextAware {

    /**
     * 默认的不是云平台
     */
    private static boolean DEFAULT_IS_CLOUD=false;

    /**
     * 是否是云平台
     */
    private static boolean IS_CLOUD=false;

    /**
     * 是否已经确认了是否云平台
     */
    private static boolean IS_INIT=false;

    /**
     * 上下文对象实例
     */
    private static ApplicationContext applicationContext;

    /**
     * 1 秒
     */
    private static int INTERVAL_TIME=1000;

    /**
     * 尝试次数
     */
    private static int TRY_COUNT=0;

    /**
     * 最大尝试次数
     */
    private static int MAX_COUNT=1200;

    /**
     * 最后尝试时间
     */
    private static long LAST_TIME=0;

    /**
     * 是否云平台
     * @return
     */
    public static boolean isCloud()
    {

//        System.out.println("IS_INIT="+IS_INIT+",IS_CLOUD="+IS_CLOUD);

        if(IS_INIT)
        {
            return IS_CLOUD;
        }
        else
        {
            long now=System.currentTimeMillis();
            if(TRY_COUNT < MAX_COUNT && now - LAST_TIME>INTERVAL_TIME )  //这里可能需要不是马上有这个bean，或者其他bean需要等待，实际上不会出现
            {
                //没有实现 isCloudData这个bean 的话，就不是运营端，或者不需要判断是不是运营端的组件了
                // 实际上这里运行不了多少次的，因为IS_INIT初始化后就不用访问这个了，或者确认了没这个bean的话，就直接跳出去了
                if(applicationContext.containsBean("isCloudData"))
                {
                    IsCloudData isCloudData=applicationContext.getBean("isCloudData",IsCloudData.class);
                    if(isCloudData != null && isCloudData.isCloud() !=null)
                    {
                        //初始化
                        IS_INIT=true;
                        IS_CLOUD=isCloudData.isCloud();

                        //初始化完就不用了
                        applicationContext=null;

                        return IS_CLOUD;
                    }
                }
                else
                {
                    //如果没有实现
                    TRY_COUNT++;
                    LAST_TIME=now;

                    if(TRY_COUNT>=MAX_COUNT)
                    {
                        IS_INIT=true;
                        IS_CLOUD=DEFAULT_IS_CLOUD;

                        applicationContext=null;
                    }
                }
            }
        }
        return DEFAULT_IS_CLOUD;
    }

    @SuppressWarnings("static-access")
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

}
