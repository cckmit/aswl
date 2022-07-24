package com.aswl.as.asos.modules.asuser.service.impl;

import com.aswl.as.asos.modules.asuser.entity.AsUserSysMenu;
import com.aswl.as.common.core.utils.IdGen;
import com.aswl.as.asos.common.utils.Query;
import com.aswl.as.asos.common.utils.PageUtils;
import com.aswl.as.asos.common.annotation.SysLog;
import com.aswl.as.asos.datasource.annotation.DataSource;
import com.aswl.as.asos.modules.asuser.entity.SysAppMenu;
import com.aswl.as.asos.modules.asuser.mapper.SysAppMenuMapper;
import com.aswl.as.asos.modules.asuser.service.ISysAppMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * APP菜单表(现在实际上没那么复杂，但是后面可能会很复杂，所以直接参考sys_menu表来弄，不用的列，清空就是) 服务实现类
 * </p>
 *
 * @author hfx
 * @since 2020-03-25
 */
@Service
public class SysAppMenuServiceImpl extends ServiceImpl<SysAppMenuMapper, SysAppMenu> implements ISysAppMenuService {

    @DataSource("slave2")
    public PageUtils queryPage(Map<String, Object> params)
    {
        /*
        String projectName = (String)params.get("projectName");
        String projectDes = (String)params.get("projectDes");
        String projectOwner = (String)params.get("projectOwner");
        */

        IPage<SysAppMenu> page = this.page(
            new Query<SysAppMenu>().getPage(params),
                new QueryWrapper<SysAppMenu>()
                /*
                .like(StringUtils.isNotBlank(projectName),"project_name",projectName)
                .like(StringUtils.isNotBlank(projectDes),"project_des",projectDes)
                .like(StringUtils.isNotBlank(projectOwner),"project_owner",projectOwner)
                */
            );

        return new PageUtils(page);
    }

    @DataSource("slave2")
    public List<SysAppMenu> findList(SysAppMenu entity)
    {
        return list(new QueryWrapper<SysAppMenu>().eq(StringUtils.isNotBlank(entity.getTenantCode()),"tenant_code",entity.getTenantCode()));
    }

    //根据id返回对应信息
    @DataSource("slave2")
    public SysAppMenu getEntityById(String id)
    {
        return this.getById(id);
    }

    @DataSource("slave2")
    public boolean saveEntity(SysAppMenu entity)
    {
        entity.setId(IdGen.snowflakeId());
        return this.save(entity);
    }

//    @DataSource("slave2")
    public boolean saveEntityNotIdGen(SysAppMenu entity)
    {
//        entity.setId(IdGen.snowflakeId());
        return this.save(entity);
    }

    @DataSource("slave2")
    public boolean updateEntityById(SysAppMenu entity)
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
