package com.aswl.as.asos.modules.ibrs.service.impl;

import com.aswl.as.asos.common.utils.OsGlobalData;
import com.aswl.as.common.core.constant.CommonConstant;
import com.aswl.as.common.core.utils.IdGen;
import com.aswl.as.asos.common.utils.Query;
import com.aswl.as.asos.common.utils.PageUtils;
import com.aswl.as.asos.common.annotation.SysLog;
import com.aswl.as.asos.datasource.annotation.DataSource;
import com.aswl.as.asos.modules.ibrs.entity.AsDeviceKind;
import com.aswl.as.asos.modules.ibrs.mapper.AsDeviceKindMapper;
import com.aswl.as.asos.modules.ibrs.service.IAsDeviceKindService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 设备种类 服务实现类
 * </p>
 *
 * @author hfx
 * @since 2019-12-02
 */
@Service
public class AsDeviceKindServiceImpl extends ServiceImpl<AsDeviceKindMapper, AsDeviceKind> implements IAsDeviceKindService {

    @DataSource("slave1")
    public PageUtils queryPage(Map<String, Object> params)
    {
        /*
        String projectName = (String)params.get("projectName");
        String projectDes = (String)params.get("projectDes");
        String projectOwner = (String)params.get("projectOwner");
        */

        IPage<AsDeviceKind> page = this.page(
            new Query<AsDeviceKind>().getPage(params),
                new QueryWrapper<AsDeviceKind>()
                /*
                .like(StringUtils.isNotBlank(projectName),"project_name",projectName)
                .like(StringUtils.isNotBlank(projectDes),"project_des",projectDes)
                .like(StringUtils.isNotBlank(projectOwner),"project_owner",projectOwner)
                */
            );

        return new PageUtils(page);
    }

    @DataSource("slave1")
    public List<AsDeviceKind> findList(AsDeviceKind entity)
    {
        return list(new QueryWrapper<AsDeviceKind>());
    }

    //根据id返回对应信息
    @DataSource("slave1")
    public AsDeviceKind getEntityById(String id)
    {
        return this.getById(id);
    }

    @DataSource("slave1")
    public boolean saveEntity(AsDeviceKind entity)
    {
//        entity.setProjectId(IdGen.snowflakeId());
        entity.setId(IdGen.snowflakeId());
        entity.setDelFlag(CommonConstant.DEL_FLAG_NORMAL);
        return this.save(entity);
    }

    @DataSource("slave1")
    public boolean updateEntityById(AsDeviceKind entity)
    {
        //移除缓存
        boolean b=this.updateById(entity);
//        if(b)
//        {
//            OsGlobalData.COMMON_KIND_NAME_MAP.remove(entity.getId());
//        }
        return b;
    }

    @DataSource("slave1")
    public boolean deleteEntityById(String id)
    {
        return this.removeById(id);
    }

    @DataSource("slave1")
    public boolean deleteEntityByIds(String[] ids)
    {
        for(String id:ids)
        {
            this.removeById(id);
        }
        return true;
    }


}
