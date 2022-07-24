package com.aswl.as.ibrs.api.feign.factory;
import com.aswl.as.ibrs.api.feign.RegionServiceClient;
import com.aswl.as.ibrs.api.feign.fallback.RegionServiceClientFallbackImpl;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * 区域断路器工厂
 *
 * @author aswl.com
 * @date 2019/3/23 23:38
 */
@Component
public class RegionServiceClientFallbackFactory implements FallbackFactory<RegionServiceClient> {

    @Override
    public RegionServiceClient create(Throwable throwable) {
        RegionServiceClientFallbackImpl regionServiceClientFallback = new RegionServiceClientFallbackImpl();
        regionServiceClientFallback.setThrowable(throwable);
        return regionServiceClientFallback;
    }
}
