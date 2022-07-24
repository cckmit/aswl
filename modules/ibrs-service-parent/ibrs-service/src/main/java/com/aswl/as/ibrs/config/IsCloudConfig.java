package com.aswl.as.ibrs.config;

import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.utils.SysUtil;
import com.aswl.as.common.security.project.IsCloudData;
import com.aswl.as.user.api.feign.UserServiceClient;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//本类用来注入是否是云平台的设置
@Configuration
@AllArgsConstructor
public class IsCloudConfig {

    private UserServiceClient userServiceClient;

    private static Boolean isCloud=null;

    @Bean(value = "isCloudData")
    public IsCloudData isCloudData() {
        return ()->{
            if(isCloud!=null)
            {
                return isCloud;
            }
            else
            {
                // 这里真正去查的逻辑
                ResponseBean<Boolean> r= userServiceClient.findIsCloud(SysUtil.getTenantCode());
                if(r!=null && ResponseBean.SUCCESS==r.getCode())
                {
                    isCloud=r.getData();
                    return isCloud;
                }
            }
            return null;
        };
    }

}
