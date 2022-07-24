package com.aswl.as.asos.modules.ibrs.service.impl;

import com.aswl.as.common.core.constant.CommonConstant;
import com.aswl.as.common.core.utils.IdGen;
import com.aswl.as.asos.common.utils.Query;
import com.aswl.as.asos.common.utils.PageUtils;
import com.aswl.as.asos.common.annotation.SysLog;
import com.aswl.as.asos.datasource.annotation.DataSource;
import com.aswl.as.asos.modules.ibrs.entity.AsEventUcStatusGroupModel;
import com.aswl.as.asos.modules.ibrs.mapper.AsEventUcStatusGroupModelMapper;
import com.aswl.as.asos.modules.ibrs.service.IAsEventUcStatusGroupModelService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 事件状态组-设备型号 服务实现类
 * </p>
 *
 * @author hfx
 * @since 2020-01-08
 */
@Service
public class AsEventUcStatusGroupModelServiceImpl extends ServiceImpl<AsEventUcStatusGroupModelMapper, AsEventUcStatusGroupModel> implements IAsEventUcStatusGroupModelService {

    @DataSource("slave1")
    public PageUtils queryPage(Map<String, Object> params)
    {
        /*
        String projectName = (String)params.get("projectName");
        String projectDes = (String)params.get("projectDes");
        String projectOwner = (String)params.get("projectOwner");
        */

        IPage<AsEventUcStatusGroupModel> page = this.page(
            new Query<AsEventUcStatusGroupModel>().getPage(params),
                new QueryWrapper<AsEventUcStatusGroupModel>()
                /*
                .like(StringUtils.isNotBlank(projectName),"project_name",projectName)
                .like(StringUtils.isNotBlank(projectDes),"project_des",projectDes)
                .like(StringUtils.isNotBlank(projectOwner),"project_owner",projectOwner)
                */
            );

        return new PageUtils(page);
    }

    @DataSource("slave1")
    public List<AsEventUcStatusGroupModel> findList(AsEventUcStatusGroupModel entity)
    {
        return list(new QueryWrapper<AsEventUcStatusGroupModel>());
    }

    //根据id返回对应信息
    @DataSource("slave1")
    public AsEventUcStatusGroupModel getEntityById(String id)
    {
        return this.getById(id);
    }

    @DataSource("slave1")
    public boolean saveEntity(AsEventUcStatusGroupModel entity)
    {
//        entity.setProjectId(IdGen.snowflakeId());
        entity.setId(IdGen.snowflakeId());
        return this.save(entity);
    }

    @DataSource("slave1")
    public boolean updateEntityById(AsEventUcStatusGroupModel entity)
    {
        return this.updateById(entity);
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
