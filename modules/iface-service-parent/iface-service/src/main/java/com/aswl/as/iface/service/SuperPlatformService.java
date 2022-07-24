package com.aswl.as.iface.service;

import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.iface.mapper.SuperPlatformMapper;
import com.aswl.as.iface.model.SuperPlatform;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author aswl
 * @date 2019-09-11
 */
@AllArgsConstructor
@Slf4j
@Service
@Transactional
public class SuperPlatformService extends CrudService<SuperPlatformMapper, SuperPlatform> {

    @Autowired
    private SuperPlatformMapper superPlatformMapper;

    /**
     * genjuserviceId查询接口用户
     */
    public SuperPlatform findByServiceId(String serviceId){

        return superPlatformMapper.findByServiceId(serviceId);
    }

    public SuperPlatform findSuperPlatform(String appId) {
        return superPlatformMapper.findSuperPlatform(appId);
    }
}
