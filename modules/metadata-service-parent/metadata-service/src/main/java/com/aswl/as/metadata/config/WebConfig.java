package com.aswl.as.metadata.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.reactive.CorsUtils;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import reactor.core.publisher.Mono;

@Configuration
public class WebConfig implements WebMvcConfigurer {

//    @Bean
//    public WebFilter corsFilter() {
//        return (ServerWebExchange ctx, WebFilterChain chain) -> {
//            ServerHttpRequest request = ctx.getRequest();
//            if (!CorsUtils.isCorsRequest(request))
//                return chain.filter(ctx);
//            HttpHeaders requestHeaders = request.getHeaders();
//            ServerHttpResponse response = ctx.getResponse();
//            HttpMethod requestMethod = requestHeaders.getAccessControlRequestMethod();
//            HttpHeaders headers = response.getHeaders();
//            headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, ctx.getRequest().getHeaders().getOrigin());
//            headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, "Origin, X-Requested-With, Content-Type, Accept,authorization,tenant-code");
//            if (requestMethod != null)
//                headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "POST, GET, PATCH, DELETE, PUT");
//            headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");
//            headers.add(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "3600");
////            headers.add(HttpHeaders.ACCESS_CONTROL_MAX_AGE, MAX_AGE);
//            if (request.getMethod() == HttpMethod.OPTIONS) {
//                response.setStatusCode(HttpStatus.OK);
//                return Mono.empty();
//            }
//            return chain.filter(ctx);
//        };
//    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        /* 是否允许请求带有验证信息 */
        corsConfiguration.setAllowCredentials(true);
        /* 允许访问的客户端域名 */
        corsConfiguration.addAllowedOrigin("*");
//        corsConfiguration.addAllowedOrigin("null");
//        corsConfiguration.addAllowedOrigin("http://192.168.200.235:8097");
//        corsConfiguration.addAllowedOrigin("http://192.168.200.219:8080");
//        corsConfiguration.addAllowedOrigin("http://localhost:8097");
//        corsConfiguration.addAllowedOrigin("http://127.0.0.1:8097");
        /* 允许服务端访问的客户端请求头 */
        corsConfiguration.addAllowedHeader("*");
        /* 允许访问的方法名,GET POST等 */
        corsConfiguration.addAllowedMethod("*");
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
//        FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(urlBasedCorsConfigurationSource));
//        bean.setOrder(0);
//        return bean;
        return new CorsFilter(urlBasedCorsConfigurationSource);
    }
}
