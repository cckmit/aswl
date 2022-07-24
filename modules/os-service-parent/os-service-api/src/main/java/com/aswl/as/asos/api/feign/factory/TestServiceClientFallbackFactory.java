package com.aswl.as.asos.api.feign.factory;
import com.aswl.as.asos.api.feign.TestServiceClient;
import com.aswl.as.asos.api.feign.fallback.TestServiceClientFallbackImpl;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * 区域断路器工厂
 *
 * @author aswl.com
 * @date 2019/3/23 23:38
 */
@Component
public class TestServiceClientFallbackFactory implements FallbackFactory<TestServiceClient> {

    @Override
    public TestServiceClient create(Throwable throwable) {
        TestServiceClientFallbackImpl testServiceClientFallback = new TestServiceClientFallbackImpl();
        testServiceClientFallback.setThrowable(throwable);
        return testServiceClientFallback;
    }
}
