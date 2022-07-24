package com.aswl.as.iface.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author jk
 * @version 1.0.0
 * @create 2019/12/31 14:17
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "socket")
public class SocketConfig {
    private String url;
    private String send;
}
