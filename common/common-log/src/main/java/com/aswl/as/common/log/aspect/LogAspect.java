package com.aswl.as.common.log.aspect;

import com.aswl.as.common.core.utils.SpringContextHolder;
import com.aswl.as.common.core.utils.SysUtil;
import com.aswl.as.common.log.annotation.Log;
import com.aswl.as.common.log.event.LogEvent;
import com.aswl.as.common.log.utils.LogUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 日志切面，异步记录日志
 *
 * @author aswl.com
 * @date 2019/3/12 23:52
 */
@Aspect
public class LogAspect {

    private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);

    @Around("@annotation(log)")
    public Object around(ProceedingJoinPoint point, Log log) throws Throwable {
        String strClassName = point.getTarget().getClass().getName();
        String strMethodName = point.getSignature().getName();
        logger.debug("[类名]:{},[方法]:{}", strClassName, strMethodName);
        com.aswl.as.common.core.model.Log logVo = LogUtil.getLog();
        logVo.setTitle(log.value());
        logVo.setType(log.businessType().getType());
        // 发送异步日志事件
        Long startTime = System.currentTimeMillis();
        Object obj = point.proceed();
        //请求的参数
         /* Object[] args = point.getArgs();
        //将参数所在的数组转换成json
        String params =null;
        JSONArray jsonArray=new JSONArray(args);
        params = jsonArray.toString();
        if (params ==  null){
            logVo.setParams("");
        }else{
            logVo.setParams(params);
        }*/
        Long endTime = System.currentTimeMillis();
        logVo.setTime(String.valueOf(endTime - startTime));
        logVo.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(),SysUtil.getTenantCode());
        SpringContextHolder.publishEvent(new LogEvent(logVo));
        return obj;
    }
}
