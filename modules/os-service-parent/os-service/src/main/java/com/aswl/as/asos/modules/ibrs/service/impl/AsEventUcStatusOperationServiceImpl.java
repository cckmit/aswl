package com.aswl.as.asos.modules.ibrs.service.impl;

import com.aswl.as.common.core.constant.CommonConstant;
import com.aswl.as.common.core.utils.IdGen;
import com.aswl.as.asos.common.utils.Query;
import com.aswl.as.asos.common.utils.PageUtils;
import com.aswl.as.asos.common.annotation.SysLog;
import com.aswl.as.asos.datasource.annotation.DataSource;
import com.aswl.as.asos.modules.ibrs.entity.AsEventUcStatusOperation;
import com.aswl.as.asos.modules.ibrs.mapper.AsEventUcStatusOperationMapper;
import com.aswl.as.asos.modules.ibrs.service.IAsEventUcStatusOperationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 事件状态操作 服务实现类
 * </p>
 *
 * @author hfx
 * @since 2020-01-09
 */
@Service
public class AsEventUcStatusOperationServiceImpl extends ServiceImpl<AsEventUcStatusOperationMapper, AsEventUcStatusOperation> implements IAsEventUcStatusOperationService {

    @DataSource("slave1")
    public PageUtils queryPage(Map<String, Object> params)
    {
        /*
        String projectName = (String)params.get("projectName");
        String projectDes = (String)params.get("projectDes");
        String projectOwner = (String)params.get("projectOwner");
        */

        IPage<AsEventUcStatusOperation> page = this.page(
            new Query<AsEventUcStatusOperation>().getPage(params),
                new QueryWrapper<AsEventUcStatusOperation>()
                /*
                .like(StringUtils.isNotBlank(projectName),"project_name",projectName)
                .like(StringUtils.isNotBlank(projectDes),"project_des",projectDes)
                .like(StringUtils.isNotBlank(projectOwner),"project_owner",projectOwner)
                */
            );

        return new PageUtils(page);
    }

    @DataSource("slave1")
    public List<AsEventUcStatusOperation> findList(AsEventUcStatusOperation entity)
    {
        return list(new QueryWrapper<AsEventUcStatusOperation>());
    }

    //根据id返回对应信息
    @DataSource("slave1")
    public AsEventUcStatusOperation getEntityById(String id)
    {
        return this.getById(id);
    }

    @DataSource("slave1")
    public boolean saveEntity(AsEventUcStatusOperation entity)
    {
        entity.setId(IdGen.snowflakeId());
        return this.save(entity);
    }

    @DataSource("slave1")
    public boolean updateEntityById(AsEventUcStatusOperation entity)
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
