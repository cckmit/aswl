package com.aswl.as.asos.modules.asuser.service.impl;

import com.aswl.as.common.core.utils.IdGen;
import com.aswl.as.asos.common.utils.Query;
import com.aswl.as.asos.common.utils.PageUtils;
import com.aswl.as.asos.datasource.annotation.DataSource;
import com.aswl.as.asos.modules.asuser.entity.AsUserSysMenu;
import com.aswl.as.asos.modules.asuser.mapper.AsUserSysMenuMapper;
import com.aswl.as.asos.modules.asuser.service.IAsUserSysMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 菜单表 服务实现类
 * </p>
 *
 * @author hfx
 * @since 2019-12-13
 */
@Service
@AllArgsConstructor
public class AsUserSysMenuServiceImpl extends ServiceImpl<AsUserSysMenuMapper, AsUserSysMenu> implements IAsUserSysMenuService {


     private AsUserSysMenuMapper asUserSysMenuMapper;

    @DataSource("slave2")
    public PageUtils queryPage(Map<String, Object> params)
    {
        /*
        String projectName = (String)params.get("projectName");
        String projectDes = (String)params.get("projectDes");
        String projectOwner = (String)params.get("projectOwner");
        */

        IPage<AsUserSysMenu> page = this.page(
            new Query<AsUserSysMenu>().getPage(params),
                new QueryWrapper<AsUserSysMenu>()
                /*
                .like(StringUtils.isNotBlank(projectName),"project_name",projectName)
                .like(StringUtils.isNotBlank(projectDes),"project_des",projectDes)
                .like(StringUtils.isNotBlank(projectOwner),"project_owner",projectOwner)
                */
            );

        return new PageUtils(page);
    }

    @DataSource("slave2")
    public List<AsUserSysMenu> findList(AsUserSysMenu entity)
    {
        return list(new QueryWrapper<AsUserSysMenu>().eq(StringUtils.isNotBlank(entity.getTenantCode()),"tenant_code",entity.getTenantCode()));
    }

    @DataSource("slave2")
    public int deleteByTenantCode(String tenantCode) {
        return asUserSysMenuMapper.deleteByTenantCode(tenantCode);
    }

    //根据id返回对应信息
    @DataSource("slave2")
    public AsUserSysMenu getEntityById(String id)
    {
        return this.getById(id);
    }

    @DataSource("slave2")
    public boolean saveEntity(AsUserSysMenu entity)
    {
        entity.setId(IdGen.snowflakeId());
        return this.save(entity);
    }

    @DataSource("slave2")
    public boolean saveEntityNotIdGen(AsUserSysMenu entity)
    {
//        entity.setId(IdGen.snowflakeId());
        return this.save(entity);
    }

    @DataSource("slave2")
    public boolean updateEntityById(AsUserSysMenu entity)
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
