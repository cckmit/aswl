/**
 * Copyright (c) 2019 aswl.com All rights reserved.
 *
 * https://www.gzaswl.net
 *
 * 2019.11
 */

package com.aswl.as.asos.common.aspect;

import cn.hutool.core.util.URLUtil;
import com.aswl.as.asos.modules.sys.entity.SysUserEntity;
import com.aswl.as.asos.modules.sys.service.SysLogService;
import com.aswl.as.asos.common.annotation.SysLog;
import com.aswl.as.asos.common.utils.HttpContextUtils;
import com.aswl.as.asos.common.utils.IPUtils;
import com.aswl.as.asos.modules.sys.entity.SysLogEntity;
import com.aswl.as.common.core.constant.CommonConstant;
import com.google.gson.Gson;
import eu.bitwalker.useragentutils.UserAgent;
import org.apache.shiro.SecurityUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;


/**
 * 系统日志，切面处理类
 *
 * @author admin@gzaswl.net
 */
@Aspect
@Component
public class SysLogAspect {
	@Autowired
	private SysLogService sysLogService;
	
	@Pointcut("@annotation(com.aswl.as.asos.common.annotation.SysLog)")
	public void logPointCut() { 
		
	}

	@Around("logPointCut()")
	public Object around(ProceedingJoinPoint point) throws Throwable {
		long beginTime = System.currentTimeMillis();
		//执行方法
		Object result = point.proceed();
		//执行时长(毫秒)
		long time = System.currentTimeMillis() - beginTime;

		//保存日志
		saveSysLog(point, time);

		return result;
	}

	private void saveSysLog(ProceedingJoinPoint joinPoint, long time) {
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();

		SysLogEntity sysLog = new SysLogEntity();
		SysLog syslog = method.getAnnotation(SysLog.class);
		if(syslog != null){
			//注解上的描述
			sysLog.setOperation(syslog.value());
		}

		//请求的方法名
		String className = joinPoint.getTarget().getClass().getName();
		String methodName = signature.getName();
		sysLog.setMethod(className + "." + methodName + "()");

		//请求的参数
		Object[] args = joinPoint.getArgs();
		try{
			String params = new Gson().toJson(args);
			sysLog.setParams(params);
		}catch (Exception e){

		}

		//获取request
		HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
		//设置IP地址
		sysLog.setIp(IPUtils.getIpAddr(request));

		// 可能 需要添加多点东西，比如请求地址和参数
		//http请求方式
		sysLog.setHttpMethod(request.getMethod());

		//转成UserAgent对象
		UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
		sysLog.setUserAgent(userAgent.getBrowser().getName() + " / " + userAgent.getOperatingSystem());
		sysLog.setRequestUri(URLUtil.getPath(request.getRequestURI()));
		sysLog.setDelFlag(CommonConstant.DEL_FLAG_NORMAL);

		//用户名
		String username = ((SysUserEntity) SecurityUtils.getSubject().getPrincipal()).getUsername();
		sysLog.setUsername(username);

		sysLog.setTime(time);
		sysLog.setCreateDate(new Date());
		//保存系统日志
		sysLogService.save(sysLog);
	}
}
