package com.aswl.as.asos.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.FilterConfig;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * 跨域配置
 *
 * @author aswl.com
 * @date 2019/07/06 17:12
 */
@WebFilter(filterName = "CORSFilter", urlPatterns = {"/*"})
@Order(value = 2)
@Configuration         //这3个注解的作用 等同与web.xml 中配置的过滤映射 ，可以2选1
public class CorsConfig implements Filter{

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)throws IOException, ServletException {
//        System.out.println("Filter 过滤器 执行 了");
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        // 响应标头指定 指定可以访问资源的URI路径
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));

        //响应标头指定响应访问所述资源到时允许的一种或多种方法
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, PATCH, DELETE, PUT, OPTIONS");
//        response.setHeader("Access-Control-Allow-Methods", "*");

        //设置 缓存可以生存的最大秒数
        response.setHeader("Access-Control-Max-Age", "3600");

        //设置  受支持请求标头（自定义  可以访问的请求头  例如：Token）
        response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept,authorization,tenant-code,project-id,token");

        //自定义 可以被访问的响应头
        response.setHeader("Access-Control-Expose-Headers","checkTokenResult,token");

        // 指示的请求的响应是否可以暴露于该页面。当true值返回时它可以被暴露
        response.setHeader("Access-Control-Allow-Credentials","true");

        filterChain.doFilter(servletRequest, servletResponse);

    }

    @Override
    public void destroy() {

    }
}
