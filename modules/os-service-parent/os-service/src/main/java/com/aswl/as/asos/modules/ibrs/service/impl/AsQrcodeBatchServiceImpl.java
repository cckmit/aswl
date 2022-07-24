package com.aswl.as.asos.modules.ibrs.service.impl;
import com.aswl.as.common.core.utils.IdGen;
import com.aswl.as.asos.common.utils.Query;
import com.aswl.as.asos.common.utils.PageUtils;
import com.aswl.as.asos.datasource.annotation.DataSource;
import com.aswl.as.asos.modules.ibrs.entity.AsQrcodeBatch;
import com.aswl.as.asos.modules.ibrs.mapper.AsQrcodeBatchMapper;
import com.aswl.as.asos.modules.ibrs.service.IAsQrcodeBatchService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 二维码批次表 服务实现类
 * </p>
 *
 * @author df
 * @since 2020-12-01
 */
@Service
@AllArgsConstructor
public class AsQrcodeBatchServiceImpl extends ServiceImpl<AsQrcodeBatchMapper, AsQrcodeBatch> implements IAsQrcodeBatchService {

    @DataSource("slave1")
    public PageUtils queryPage(Map<String, Object> params)
    {
        /*
        String projectName = (String)params.get("projectName");
        String projectDes = (String)params.get("projectDes");
        String projectOwner = (String)params.get("projectOwner");
        */

        IPage<AsQrcodeBatch> page = this.page(
            new Query<AsQrcodeBatch>().getPage(params),
                new QueryWrapper<AsQrcodeBatch>().orderByDesc("create_date")
                /*
                .like(StringUtils.isNotBlank(projectName),"project_name",projectName)
                .like(StringUtils.isNotBlank(projectDes),"project_des",projectDes)
                .like(StringUtils.isNotBlank(projectOwner),"project_owner",projectOwner)
                */
            );

        return new PageUtils(page);
    }

    @DataSource("slave1")
    public List<AsQrcodeBatch> findList(AsQrcodeBatch entity)
    {
        return list(new QueryWrapper<AsQrcodeBatch>());
    }

    //根据id返回对应信息
    @DataSource("slave1")
    public AsQrcodeBatch getEntityById(String id)
    {
        return this.getById(id);
    }

    @DataSource("slave1")
    @Transactional
    public boolean saveEntity(AsQrcodeBatch entity)
    {
        entity.setId(IdGen.snowflakeId());
        return this.save(entity);
    }

    @DataSource("slave1")
    public boolean updateEntityById(AsQrcodeBatch entity)
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
