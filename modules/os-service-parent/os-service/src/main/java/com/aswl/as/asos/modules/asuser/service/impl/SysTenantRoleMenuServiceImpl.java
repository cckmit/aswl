package com.aswl.as.asos.modules.asuser.service.impl;
import com.aswl.as.asos.common.utils.Query;
import com.aswl.as.asos.common.utils.PageUtils;
import com.aswl.as.asos.datasource.annotation.DataSource;
import com.aswl.as.asos.modules.asuser.entity.SysTenantRoleMenu;
import com.aswl.as.asos.modules.asuser.mapper.SysTenantRoleMenuMapper;
import com.aswl.as.asos.modules.asuser.service.ISysTenantRoleMenuService;
import com.aswl.as.common.core.utils.IdGen;
import com.aswl.as.user.api.module.RoleMenu;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 租户角色菜单表 服务实现类
 * </p>
 *
 * @author df
 * @since 2020-11-19
 */
@Service
public class SysTenantRoleMenuServiceImpl extends ServiceImpl<SysTenantRoleMenuMapper, SysTenantRoleMenu> implements ISysTenantRoleMenuService {

    @Autowired
    private  SysTenantRoleMenuMapper tenantRoleMenuMapper;

    @DataSource("slave2")
    public PageUtils queryPage(Map<String, Object> params)
    {

        IPage<SysTenantRoleMenu> page = this.page(
            new Query<SysTenantRoleMenu>().getPage(params),
                new QueryWrapper<SysTenantRoleMenu>()
            );

        return new PageUtils(page);
    }

    @DataSource("slave2")
    public List<SysTenantRoleMenu> findList(SysTenantRoleMenu entity)
    {
        return list(new QueryWrapper<SysTenantRoleMenu>());
    }

    //根据id返回对应信息
    @DataSource("slave2")
    public SysTenantRoleMenu getEntityById(String id)
    {
        return this.getById(id);
    }

    @DataSource("slave2")
    public boolean saveEntity(SysTenantRoleMenu entity)
    {
        return this.save(entity);
    }

    @DataSource("slave2")
    public boolean updateEntityById(SysTenantRoleMenu entity)
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
    public int saveRoleMenus(String role, List<String> menus) {
        int update = -1;
        if (CollectionUtils.isNotEmpty(menus)) {
            // 删除旧的管理数据
            tenantRoleMenuMapper.deleteByRoleId(role);
            List<SysTenantRoleMenu> roleMenus = new ArrayList<>();
            for (String menuId : menus) {
                SysTenantRoleMenu roleMenu = new SysTenantRoleMenu();
                roleMenu.setId(IdGen.snowflakeId());
                roleMenu.setTenantRoleId(role);
                roleMenu.setMenuId(menuId);
                roleMenus.add(roleMenu);
            }
            update = tenantRoleMenuMapper.insertBatch(roleMenus);
        }
        return update;
    }


}
