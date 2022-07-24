package com.aswl.as.ibrs.service;

import com.aswl.as.common.core.constant.CommonConstant;
import com.aswl.as.common.core.exceptions.CommonException;
import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.ibrs.api.module.SuperPlatform;
import com.aswl.as.ibrs.mapper.SuperPlatformMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@AllArgsConstructor
@Slf4j
@Service
public class SuperPlatformService extends CrudService<SuperPlatformMapper, SuperPlatform> {
    private final SuperPlatformMapper superPlatformMapper;
    private static final PasswordEncoder encoder = new BCryptPasswordEncoder();

    /**
     * 新增上级平台信息
     *
     * @param superPlatform
     * @return int
     */
    @Transactional
    @Override
    public int insert(SuperPlatform superPlatform) {
        //生成服务号
        String serviceId = genareteServiceId();
        superPlatform.setClientId(serviceId);
        superPlatform.setStatus(1);
        if (StringUtils.isBlank(superPlatform.getClientSecret())) {
            superPlatform.setClientSecret(CommonConstant.DEFAULT_PASSWORD);
        }
        superPlatform.setClientSecret(encoder.encode(superPlatform.getClientSecret()));
        return super.insert(superPlatform);
    }

    /**
     * 修改上级平台
     * @param entity entity
     * @return
     */
    @Override
    public int update(SuperPlatform entity) {
        if (entity.getClientSecret() != null) {
            entity.setClientSecret(encoder.encode(entity.getClientSecret()));
        }
        return super.update(entity);
    }

    /**
     * 删除上级平台信息
     *
     * @param superPlatform
     * @return int
     */
    @Transactional
    @Override
    public int delete(SuperPlatform superPlatform) {
        return super.delete(superPlatform);
    }

    private synchronized String genareteServiceId(){
        return "asid" + new Date().getTime();
    }

    @Transactional
    public boolean resetPassword(SuperPlatform superPlatform) {

        SuperPlatform platform = superPlatformMapper.findByClientId(superPlatform.getClientId());
        if (platform == null) {
            throw new CommonException("账号不存在");
        }
        //重置密码
        platform.setClientSecret(encoder.encode(CommonConstant.DEFAULT_PASSWORD));
        return superPlatformMapper.update(platform) > 0;
    }
}