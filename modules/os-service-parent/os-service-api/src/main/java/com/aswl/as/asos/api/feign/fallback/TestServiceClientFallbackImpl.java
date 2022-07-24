package com.aswl.as.asos.api.feign.fallback;

import com.aswl.as.asos.api.feign.TestServiceClient;
import com.aswl.as.asos.api.module.Test;
import com.aswl.as.user.api.module.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 日志断路器实现
 *
 * @author aswl.com
 * @date 2019/3/23 23:39
 */
@Slf4j
@Component
public class TestServiceClientFallbackImpl implements TestServiceClient {

    private Throwable throwable;

    /**
     * 根据用户对象获取区域
     * @param  userList
     * @return List<Region>
     */
    @Override
    public List<Test> getTestListByUsers(List<User> userList) {
        log.error("feign 根据用户对象获取区域信息失败, {}, {}", userList, throwable);
        return null;
    }

    /**
     *根据ID获取区域
     * @param id 区域ID
     * @return Region
     */
    @Override
    public Test getTestById(String id) {
        log.error("feign 获取测试数据失败, {}, {}", id, throwable);
        return null;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }
}
