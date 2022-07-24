/**
 * Copyright (c) 2019 aswl.com All rights reserved.
 *
 * https://www.gzaswl.net
 *
 * 2019.11
 */

package com.aswl.as.asos.config;

import com.aswl.as.asos.common.xss.XssFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.DelegatingFilterProxy;

import javax.servlet.DispatcherType;

/**
 * Filter配置
 *
 * @author admin@gzaswl.net
 */
@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean shiroFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new DelegatingFilterProxy("shiroFilter"));
        //该值缺省为false，表示生命周期由SpringApplicationContext管理，设置为true则表示由ServletContainer管理
        registration.addInitParameter("targetFilterLifecycle", "true");
        registration.setEnabled(true);
        registration.setOrder(Integer.MAX_VALUE - 1);
        registration.addUrlPatterns("/*");
        return registration;
    }

    //不过滤特定页面
    @Bean
    public FilterRegistrationBean xssFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setDispatcherTypes(DispatcherType.REQUEST);
        registration.setFilter(new XssFilter());

        //这三个比较特殊，如果过滤掉就不能添加有特殊字符的数据了,*/ibrs/as-content-product/*，可以更细化一点,比如只能是save和update函数能允许通过
//        registration.addInitParameter("asExclude","/ibrs/as-content-product/save,/ibrs/as-content-product/update,/ibrs/as-content-malfunction/save,/ibrs/as-content-malfunction/update,/ibrs/as-content-industry/save,/ibrs/as-content-industry/update");
        registration.addInitParameter("asExclude","*/ibrs/as-content-product/*,*/ibrs/as-content-malfunction/*,*/ibrs/as-content-industry/*,*/ibrs/as-device-model/*");

        registration.addUrlPatterns("/*");
        registration.setName("xssFilter");

//        registration.addInitParameter("exclusions","*/ibrs/as-content-product/save*");

        registration.setOrder(Integer.MAX_VALUE);
        return registration;
    }
}
