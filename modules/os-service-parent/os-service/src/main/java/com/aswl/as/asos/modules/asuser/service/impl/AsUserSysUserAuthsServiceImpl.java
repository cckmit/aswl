package com.aswl.as.asos.modules.asuser.service.impl;

import com.aswl.as.common.core.utils.IdGen;
import com.aswl.as.asos.common.utils.Query;
import com.aswl.as.asos.common.utils.PageUtils;
import com.aswl.as.asos.datasource.annotation.DataSource;
import com.aswl.as.asos.modules.asuser.entity.AsUserSysUserAuths;
import com.aswl.as.asos.modules.asuser.mapper.AsUserSysUserAuthsMapper;
import com.aswl.as.asos.modules.asuser.service.IAsUserSysUserAuthsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import java.util.Map;

/**
 * <p>
 * 用户授权表 服务实现类
 * </p>
 *
 * @author hfx
 * @since 2019-11-22
 */
@Service
public class AsUserSysUserAuthsServiceImpl extends ServiceImpl<AsUserSysUserAuthsMapper, AsUserSysUserAuths> implements IAsUserSysUserAuthsService {

    @DataSource("slave2")
    public PageUtils queryPage(Map<String, Object> params)
    {
        /*
        String projectName = (String)params.get("projectName");
        String projectDes = (String)params.get("projectDes");
        String projectOwner = (String)params.get("projectOwner");
        */

        IPage<AsUserSysUserAuths> page = this.page(
            new Query<AsUserSysUserAuths>().getPage(params),
                new QueryWrapper<AsUserSysUserAuths>()
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
    public AsUserSysUserAuths getEntityById(String id)
    {
        return this.getById(id);
    }

    @DataSource("slave2")
    public boolean saveEntity(AsUserSysUserAuths entity)
    {
        entity.setId(IdGen.snowflakeId());
        return this.save(entity);
    }

    @DataSource("slave2")
    public boolean updateEntityById(AsUserSysUserAuths entity)
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
    public AsUserSysUserAuths getEntityForCheck(String identifier)
    {
        //判断是否有该登录号码
        return this.getOne(new QueryWrapper<AsUserSysUserAuths>().eq("identifier",identifier));
    }

}
