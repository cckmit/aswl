package com.aswl.as.gateway.filters;

import com.aswl.as.common.core.constant.CommonConstant;
import lombok.AllArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author jk
 * @version 1.0.0
 * @create 2019/11/14 13:23
 */
@AllArgsConstructor
@Component
public class BlackListFilter implements GlobalFilter, Ordered {
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 当前请求
        ServerHttpRequest request = exchange.getRequest();
        //请求的ip
        String ip = request.getRemoteAddress().getHostString();
        if (redisTemplate.hasKey(ip)) {
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.LOCKED);
            return response.setComplete();
        }
        //系统有效期 TODO：临时放在这个Filter类，后期再调整
        if(redisTemplate.hasKey(CommonConstant.SysValidityAuth.SYS_VALIDITY_AUTH_KEY)){
            String key = (String) redisTemplate.opsForValue().get(CommonConstant.SysValidityAuth.SYS_VALIDITY_AUTH_KEY);
            if(key == null || key.equals("")){
                ServerHttpResponse response = exchange.getResponse();
                response.setStatusCode(HttpStatus.NON_AUTHORITATIVE_INFORMATION);
                return response.setComplete();
            }else{
                ServerHttpResponse response = exchange.getResponse();
                response.setStatusCode(HttpStatus.PAYMENT_REQUIRED);
                return response.setComplete();
            }
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -100;
    }
}
