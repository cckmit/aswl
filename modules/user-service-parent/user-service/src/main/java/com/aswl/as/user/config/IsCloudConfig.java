package com.aswl.as.user.config;

import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.utils.SysUtil;
import com.aswl.as.common.security.project.IsCloudData;
import com.aswl.as.user.api.feign.UserServiceClient;
import com.aswl.as.user.service.ConfigService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//本类用来注入是否是云平台的设置
@Configuration
@AllArgsConstructor
public class IsCloudConfig {

    private ConfigService configService;

    private static Boolean isCloud=null;

    @Bean(value = "isCloudData")
    public IsCloudData isCloudData() {
        String tenantCode = SysUtil.getTenantCode();
        return ()->{
            if(isCloud!=null)
            {
                return isCloud;
            }
            else
            {
                // 这里
                isCloud = configService.findIsCloud(tenantCode)>0;
            }
            return null;
        };
    }

}
