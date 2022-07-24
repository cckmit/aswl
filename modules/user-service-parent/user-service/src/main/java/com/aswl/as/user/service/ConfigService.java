package com.aswl.as.user.service;
import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.common.core.utils.SysUtil;
import com.aswl.as.user.api.module.Config;
import com.aswl.as.user.mapper.ConfigMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Slf4j
@Service
public class ConfigService extends CrudService<ConfigMapper, Config> {
    private final ConfigMapper configMapper;

    /**
     * 新增系统配置信息表
     *
     * @param config
     * @return int
     */
    @Transactional
    @Override
    public int insert(Config config) {
        return super.insert(config);
    }

    /**
     * 删除系统配置信息表
     *
     * @param config
     * @return int
     */
    @Transactional
    @Override
    public int delete(Config config) {
        return super.delete(config);
    }

    /**
     *  查询是否云平台
     * @return int 数量
     */
    public int findIsCloud( String tenantCode){
        return configMapper.findIsCloud(tenantCode);
    }

    //判断原来有没有该配置，如果没有该配置，就新增，如果有，就更新
    public int saveOrUpdatePrintConfig(String paramKey,String paramValue,String remark)
    {
        Config c=new Config();
        c.setParamKey(paramKey);
        c.setTenantCode(SysUtil.getTenantCode());

        List<Config> list=findList(c);
        if(list!=null && list.size()>0)
        {
            //更新
            c=list.get(0);
            c.setParamValue(paramValue);
            c.setRemark(remark);
            return update(c);
        }
        else
        {
            //新建
            c.setParamValue(paramValue);
            c.setRemark(remark);
            c.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
            return insert(c);
        }
    }

    /**
     * 根据paramKey获取Config
     * @param paramKey
     * @param tenantCode
     * @return
     */
    public Config findByKey(String paramKey, String tenantCode){
        return configMapper.findByKey(paramKey, tenantCode);
    }

}