package com.aswl.as.asos.modules.asuser.service.impl;

import com.aswl.as.asos.common.utils.PageUtils;
import com.aswl.as.asos.common.utils.Query;
import com.aswl.as.asos.datasource.annotation.DataSource;
import com.aswl.as.asos.modules.asuser.entity.AsUserSysMenu;
import com.aswl.as.asos.modules.asuser.entity.SysTenantRole;
import com.aswl.as.asos.modules.asuser.entity.SysTenantRoleMenu;
import com.aswl.as.asos.modules.asuser.mapper.SysTenantRoleMapper;
import com.aswl.as.asos.modules.asuser.service.IAsUserSysMenuService;
import com.aswl.as.asos.modules.asuser.service.ISysTenantRoleMenuService;
import com.aswl.as.asos.modules.asuser.service.ISysTenantRoleService;
import com.aswl.as.common.core.utils.IdGen;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

/**
 * @author df
 * @date 2020/10/19 11:21
 */
@Service
@Slf4j
@AllArgsConstructor
public class SysTenantRoleServiceImpl extends ServiceImpl<SysTenantRoleMapper, SysTenantRole> implements ISysTenantRoleService {

       private final SysTenantRoleMapper sysTenantRoleMapper;
        private final IAsUserSysMenuService iAsUserSysMenuService;
        private final ISysTenantRoleMenuService iSysTenantRoleMenuService;
    /**
     * 获取列表
     *
     * @param params
     * @return
     */
    @DataSource("slave2")
    public PageUtils queryPage(Map<String, Object> params) {
        String tenantRoleName = (String) params.get("tenantRoleName");
        IPage<SysTenantRole> page = this.page(
                new Query<SysTenantRole>().getPage(params),
                new QueryWrapper<SysTenantRole>()
                        .like(StringUtils.isNotBlank(tenantRoleName), "tenant_role_name", tenantRoleName)
                        .select("id","tenant_role_name","remark","creator","create_date")
        );
        return new PageUtils(page);

    }

    /**
     * 根据id返回对应信息
     *
     * @param id
     * @return
     */
    @DataSource("slave2")
    public SysTenantRole getEntityById(String id) {
        return this.getById(id);
    }

    /**
     * 保存
     *
     * @param entity
     * @return
     */
    @DataSource("slave2")
    public boolean saveEntity(SysTenantRole entity) {
         entity.setId(IdGen.snowflakeId());
         this.save(entity);
        // 保存租户角色菜单信息
       AsUserSysMenu m=new AsUserSysMenu();
        m.setTenantCode("osDemo");//暂时获取osDemo的菜单
        List<AsUserSysMenu> list=iAsUserSysMenuService.findList(m);
        if (list != null && list.size() >0) {
            for (AsUserSysMenu menu : list) {
                SysTenantRoleMenu sysTenantRoleMenu = new SysTenantRoleMenu();
                sysTenantRoleMenu.setId(IdGen.snowflakeId());
                sysTenantRoleMenu.setTenantRoleId(entity.getId());
                sysTenantRoleMenu.setMenuId(menu.getId());
                iSysTenantRoleMenuService.saveEntity(sysTenantRoleMenu);
            }
            return true;
        }
        return false;
    }

    /**
     * 更新
     *
     * @param entity
     * @return
     */
    @DataSource("slave2")
    public boolean updateEntityById(SysTenantRole entity) {
        return this.updateById(entity);
    }

    /**
     * 删除
     *
     * @param ids
     * @return
     */
    @DataSource("slave2")
    public boolean deleteEntityByIds(String[] ids) {
        for (String id : ids) {
            return this.removeById(id);
        }
        return false;
    }

    @DataSource("slave2")
    public List<AsUserSysMenu> findMenuByRoleId(String roleId) {
        return sysTenantRoleMapper.findMenuByRoleId(roleId);
    }

}
