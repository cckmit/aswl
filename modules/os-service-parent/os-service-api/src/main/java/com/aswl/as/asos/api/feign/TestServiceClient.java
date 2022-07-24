package com.aswl.as.asos.api.feign;

import com.aswl.as.asos.api.feign.factory.TestServiceClientFallbackFactory;
import com.aswl.as.common.core.constant.ServiceConstant;
import com.aswl.as.common.feign.config.CustomFeignConfig;
import com.aswl.as.user.api.module.User;
import com.aswl.as.asos.api.module.Test;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * 测试请求
 * @author dingfei
 * @date 2019-10-14 16:19
 */
@FeignClient(value = ServiceConstant.OS_SERVICE, configuration = CustomFeignConfig.class, fallbackFactory = TestServiceClientFallbackFactory.class)
public interface TestServiceClient {

    /**
     * 根据用户对象获取区域 没有这个请求，后续再加
     *
     * @param  userList
     * @return  List<Region>
     */
    @RequestMapping(value = "/v1/temp/region/getRegionListByUsers", method = RequestMethod.POST)
    List<Test> getTestListByUsers(@RequestBody List<User> userList);

    /**
     * 根据ID获取区域
     * @param id 区域ID
     * @return Region
     */
    @RequestMapping(value = "/v1/temp/region/{id}", method = RequestMethod.GET)
    Test  getTestById(@PathVariable("id") String id);
}
