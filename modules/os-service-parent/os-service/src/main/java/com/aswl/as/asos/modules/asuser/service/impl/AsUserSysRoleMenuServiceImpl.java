package com.aswl.as.asos.modules.asuser.service.impl;

import com.aswl.as.asos.common.utils.Query;
import com.aswl.as.asos.common.utils.PageUtils;
import com.aswl.as.asos.datasource.annotation.DataSource;
import com.aswl.as.asos.modules.asuser.entity.AsUserSysRoleMenu;
import com.aswl.as.asos.modules.asuser.mapper.AsUserSysRoleMenuMapper;
import com.aswl.as.asos.modules.asuser.service.IAsUserSysRoleMenuService;
import com.aswl.as.common.core.utils.IdGen;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import java.util.Map;

/**
 * <p>
 * 角色权限表 服务实现类
 * </p>
 *
 * @author hfx
 * @since 2019-11-22
 */
@Service
public class AsUserSysRoleMenuServiceImpl extends ServiceImpl<AsUserSysRoleMenuMapper, AsUserSysRoleMenu> implements IAsUserSysRoleMenuService {
    @Autowired
    AsUserSysRoleMenuMapper asUserSysRoleMenuMapper;

    @DataSource("slave2")
    public PageUtils queryPage(Map<String, Object> params)
    {
        /*
        String projectName = (String)params.get("projectName");
        String projectDes = (String)params.get("projectDes");
        String projectOwner = (String)params.get("projectOwner");
        */

        IPage<AsUserSysRoleMenu> page = this.page(
            new Query<AsUserSysRoleMenu>().getPage(params),
                new QueryWrapper<AsUserSysRoleMenu>()
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
    public AsUserSysRoleMenu getEntityById(String id)
    {
        return this.getById(id);
    }

    @DataSource("slave2")
    public boolean saveEntity(AsUserSysRoleMenu entity)
    {
//        entity.setProjectId(IdGen.snowflakeId());
        entity.setId(IdGen.snowflakeId());
        return this.save(entity);
    }

    @DataSource("slave2")
    public boolean updateEntityById(AsUserSysRoleMenu entity)
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

    @Override
    public int deleteByRoleId(String roleId) {
        return asUserSysRoleMenuMapper.deleteByRoleId(roleId);
    }


}
