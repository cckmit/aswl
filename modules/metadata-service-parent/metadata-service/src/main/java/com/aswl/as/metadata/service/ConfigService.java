package com.aswl.as.metadata.service;
import com.aswl.as.common.core.constant.CommonConstant;
import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.metadata.datasource.annotation.DataSource;
import com.aswl.as.metadata.mapper.ConfigMapper;
import com.aswl.as.user.api.module.Config;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Slf4j
@Service
public class ConfigService extends CrudService<ConfigMapper, Config> {
    private final ConfigMapper configMapper;

    /**
     *  查询是否云平台
     * @return int 数量
     */
    @DataSource(value = CommonConstant.DataBaseConstant.USER_DATABASE)
    public Boolean findIsCloud(String tenantCode){
        return configMapper.findIsCloud(tenantCode) > 0;
    }

}