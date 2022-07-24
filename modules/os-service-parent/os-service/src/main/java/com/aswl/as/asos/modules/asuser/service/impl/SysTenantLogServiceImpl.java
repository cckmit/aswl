package com.aswl.as.asos.modules.asuser.service.impl;

import com.aswl.as.common.core.utils.IdGen;
import com.aswl.as.asos.common.utils.Query;
import com.aswl.as.asos.common.utils.PageUtils;
import com.aswl.as.asos.common.annotation.SysLog;
import com.aswl.as.asos.datasource.annotation.DataSource;
import com.aswl.as.asos.modules.asuser.entity.SysTenantLog;
import com.aswl.as.asos.modules.asuser.mapper.SysTenantLogMapper;
import com.aswl.as.asos.modules.asuser.service.ISysTenantLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * SAAS租户信息记录表 服务实现类
 * </p>
 *
 * @author hfx
 * @since 2020-03-02
 */
@Service
public class SysTenantLogServiceImpl extends ServiceImpl<SysTenantLogMapper, SysTenantLog> implements ISysTenantLogService {

    @DataSource("slave2")
    public PageUtils queryPage(Map<String, Object> params)
    {
        /*
        String projectName = (String)params.get("projectName");
        String projectDes = (String)params.get("projectDes");
        String projectOwner = (String)params.get("projectOwner");
        */
        String tenantCode = (String)params.get("tenantCode");

        IPage<SysTenantLog> page = this.page(
            new Query<SysTenantLog>().getPage(params),
                new QueryWrapper<SysTenantLog>()
                .eq(StringUtils.isNotBlank(tenantCode),"tenant_code",tenantCode)
                /*
                .like(StringUtils.isNotBlank(projectName),"project_name",projectName)
                .like(StringUtils.isNotBlank(projectDes),"project_des",projectDes)
                .like(StringUtils.isNotBlank(projectOwner),"project_owner",projectOwner)
                */
            );

        return new PageUtils(page);
    }

    @DataSource("slave2")
    public List<SysTenantLog> findList(SysTenantLog entity)
    {
        return list(new QueryWrapper<SysTenantLog>()
                .eq(StringUtils.isNotBlank(entity.getTenantCode()),"tenant_code",entity.getTenantCode()));
    }

    //根据id返回对应信息
    @DataSource("slave2")
    public SysTenantLog getEntityById(String id)
    {
        return this.getById(id);
    }

    @DataSource("slave2")
    public boolean saveEntity(SysTenantLog entity)
    {
        entity.setId(IdGen.snowflakeId());
        return this.save(entity);
    }

    @DataSource("slave2")
    public boolean updateEntityById(SysTenantLog entity)
    {
        return this.updateById(entity);
    }

    @DataSource("slave2")
    public boolean deleteEntityById(String id)
    {
        return this.removeById(id);
    }

    @DataSource("slave2")
    public boolean deleteEntityByIds(String[] ids)
    {
        for(String id:ids)
        {
            this.removeById(id);
        }
        return true;
    }


}
