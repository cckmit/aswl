package com.aswl.as.asos.modules.asuser.service.impl;

import com.aswl.as.common.core.utils.IdGen;
import com.aswl.as.asos.common.utils.Query;
import com.aswl.as.asos.common.utils.PageUtils;
import com.aswl.as.asos.datasource.annotation.DataSource;
import com.aswl.as.asos.modules.asuser.entity.AsUserSysRole;
import com.aswl.as.asos.modules.asuser.mapper.AsUserSysRoleMapper;
import com.aswl.as.asos.modules.asuser.service.IAsUserSysRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.bytebuddy.implementation.auxiliary.AuxiliaryType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author hfx
 * @since 2019-11-21
 */
@Service
public class AsUserSysRoleServiceImpl extends ServiceImpl<AsUserSysRoleMapper, AsUserSysRole> implements IAsUserSysRoleService {
    
    @Autowired
    AsUserSysRoleMapper asUserSysRoleMapper;

    @DataSource("slave2")
    public PageUtils queryPage(Map<String, Object> params)
    {
        /*
        String projectName = (String)params.get("projectName");
        String projectDes = (String)params.get("projectDes");
        String projectOwner = (String)params.get("projectOwner");
        */

        IPage<AsUserSysRole> page = this.page(
            new Query<AsUserSysRole>().getPage(params),
                new QueryWrapper<AsUserSysRole>()
                /*
                .like(StringUtils.isNotBlank(projectName),"project_name",projectName)
                .like(StringUtils.isNotBlank(projectDes),"project_des",projectDes)
                .like(StringUtils.isNotBlank(projectOwner),"project_owner",projectOwner)
                */
            );

        return new PageUtils(page);
    }

    //根据id返回对应信息
    @DataSource("slave2")
    public AsUserSysRole getEntityById(String id)
    {
        return this.getById(id);
    }

    @DataSource("slave2")
    public boolean saveEntity(AsUserSysRole entity)
    {
        entity.setId(IdGen.snowflakeId());
        return this.save(entity);
    }

    @DataSource("slave2")
    public boolean updateEntityById(AsUserSysRole entity)
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

    @DataSource("slave2")
    public List<AsUserSysRole> findRoleByTenantCode(String tenantCode) {
        return asUserSysRoleMapper.findRoleByTenantCode(tenantCode);
    }

    @DataSource("slave2")
    public boolean getRoleByUserId(String userId)
    {
        /*
        for(String id:ids)
        {
            this.removeById(id);
        }
        */
        return true;
    }


}
