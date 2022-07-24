package com.aswl.as.asos.config;

import com.aswl.as.asos.common.utils.OsGlobalData;
import com.aswl.as.asos.common.utils.RedisUtils;
import com.aswl.as.asos.modules.sys.service.SysBlackListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;

import javax.servlet.FilterConfig;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * 黑名单配置
 *
 * @author aswl.com
 * @date 2019/07/06 17:12
 */
@WebFilter(filterName = "BlackListFilter", urlPatterns = {"/*"})
@Order(value = 1)
@Configuration         //这3个注解的作用 等同与web.xml 中配置的过滤映射 ，可以2选1
public class BlackListConfig implements Filter{

    @Autowired
    SysBlackListService sysBlackListService;

    @Autowired
    private RedisUtils redisUtils;

    private static String BACK_LIST_STR="BACK_LIST_";

    private static String IS_BACK_LIST_STATUS="1";

    private static String IS_NOT_BACK_LIST_STATUS="0";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        //        String ip = request.getRemoteAddr();
        String ip = getIpAddr(request);//获得客户端的IP
        if(isBlackList(ip))
        {
//            response.setContentType("text/html;charset=utf-8");
//            response.getWriter().println("该IP已被列入黑名单!");
            response.setStatus(HttpStatus.LOCKED.value());
        }
        else
        {
            //放行
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    //获取IP
    public String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * 判断是否黑名单//TODO 需要放到redis缓存
     * @param ip
     * @return
     */
    private boolean isBlackList(String ip)
    {
        String key=BACK_LIST_STR+ip;
        String value= (String)OsGlobalData.BLACK_LIST_MAP.get(key);
        if(value==null)
        {
            //需要到数据库查
            boolean b=sysBlackListService.isBlackList(ip);
//            System.out.println("使用数据库查询"+ip);
            if(b)
            {
                value=IS_BACK_LIST_STATUS;
            }
            else
            {
                value=IS_NOT_BACK_LIST_STATUS;
            }
//            redisUtils.set(key,value);
            OsGlobalData.BLACK_LIST_MAP.put(key,value);
        }

//        System.out.println("ip="+ip+",value="+value);
        return IS_BACK_LIST_STATUS.equals(value);
    }

    @Override
    public void destroy() {

    }
}
