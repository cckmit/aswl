package com.aswl.as.asos.modules.ibrs.service.impl;

import com.aswl.as.common.core.utils.IdGen;
import com.aswl.as.asos.common.utils.Query;
import com.aswl.as.asos.common.utils.PageUtils;
import com.aswl.as.asos.common.annotation.SysLog;
import com.aswl.as.asos.datasource.annotation.DataSource;
import com.aswl.as.asos.modules.ibrs.entity.AsAlarmOrderHandle;
import com.aswl.as.asos.modules.ibrs.mapper.AsAlarmOrderHandleMapper;
import com.aswl.as.asos.modules.ibrs.service.IAsAlarmOrderHandleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 派单处理工单设置 服务实现类
 * </p>
 *
 * @author hfx
 * @since 2020-03-23
 */
@Service
public class AsAlarmOrderHandleServiceImpl extends ServiceImpl<AsAlarmOrderHandleMapper, AsAlarmOrderHandle> implements IAsAlarmOrderHandleService {

    @DataSource("slave1")
    public PageUtils queryPage(Map<String, Object> params)
    {
        /*
        String projectName = (String)params.get("projectName");
        String projectDes = (String)params.get("projectDes");
        String projectOwner = (String)params.get("projectOwner");
        */

        IPage<AsAlarmOrderHandle> page = this.page(
            new Query<AsAlarmOrderHandle>().getPage(params),
                new QueryWrapper<AsAlarmOrderHandle>()
                /*
                .like(StringUtils.isNotBlank(projectName),"project_name",projectName)
                .like(StringUtils.isNotBlank(projectDes),"project_des",projectDes)
                .like(StringUtils.isNotBlank(projectOwner),"project_owner",projectOwner)
                */
            );

        return new PageUtils(page);
    }

    @DataSource("slave1")
    public List<AsAlarmOrderHandle> findList(AsAlarmOrderHandle entity)
    {
        return list(new QueryWrapper<AsAlarmOrderHandle>());
    }

    //根据id返回对应信息
    @DataSource("slave1")
    public AsAlarmOrderHandle getEntityById(String id)
    {
        return this.getById(id);
    }

    @DataSource("slave1")
    public boolean saveEntity(AsAlarmOrderHandle entity)
    {
//        entity.setProjectId(IdGen.snowflakeId());
        entity.setId(IdGen.snowflakeId());
        return this.save(entity);
    }

    @DataSource("slave1")
    public boolean saveEntityByTenantCode(String tenantCode,String creator)
    {
//        entity.setProjectId(IdGen.snowflakeId());
        AsAlarmOrderHandle entity=new AsAlarmOrderHandle();
        entity.setId(IdGen.snowflakeId());
        entity.setCreator(creator);
        entity.setCreateDate(LocalDateTime.now());
        entity.setTenantCode(tenantCode);
        entity.setOrderHandleType(0);// 自动
        return this.save(entity);
    }

    @DataSource("slave1")
    public boolean updateEntityById(AsAlarmOrderHandle entity)
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
