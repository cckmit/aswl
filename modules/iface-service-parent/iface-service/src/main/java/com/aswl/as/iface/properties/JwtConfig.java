package com.aswl.as.iface.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "jwt")
public class JwtConfig {
    //生成token的密钥
    private String saltkey;
    //token的过期时间,暂时一天,可以再配置中改
    private long expiration;
}
