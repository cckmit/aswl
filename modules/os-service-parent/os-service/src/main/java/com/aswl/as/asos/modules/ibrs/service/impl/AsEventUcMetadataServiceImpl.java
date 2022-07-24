package com.aswl.as.asos.modules.ibrs.service.impl;

import com.aswl.as.common.core.constant.CommonConstant;
import com.aswl.as.common.core.utils.IdGen;
import com.aswl.as.asos.common.utils.Query;
import com.aswl.as.asos.common.utils.PageUtils;
import com.aswl.as.asos.common.annotation.SysLog;
import com.aswl.as.asos.datasource.annotation.DataSource;
import com.aswl.as.asos.modules.ibrs.entity.AsEventUcMetadata;
import com.aswl.as.asos.modules.ibrs.mapper.AsEventUcMetadataMapper;
import com.aswl.as.asos.modules.ibrs.service.IAsEventUcMetadataService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 事件元数据 服务实现类
 * </p>
 *
 * @author hfx
 * @since 2020-01-08
 */
@Service
public class AsEventUcMetadataServiceImpl extends ServiceImpl<AsEventUcMetadataMapper, AsEventUcMetadata> implements IAsEventUcMetadataService {

    @DataSource("slave1")
    public PageUtils queryPage(Map<String, Object> params)
    {
        IPage<AsEventUcMetadata> page = this.page(
            new Query<AsEventUcMetadata>().getPage(params),
                new QueryWrapper<AsEventUcMetadata>()
            );

        return new PageUtils(page);
    }

    @DataSource("slave1")
    public List<AsEventUcMetadata> findList(AsEventUcMetadata entity)
    {
        //TODO 看看需要什么数据
        return list(new QueryWrapper<AsEventUcMetadata>()
                .eq(entity.getIsStatusGroup()!=null,"is_status_group", entity.getIsStatusGroup())
                .eq(StringUtils.isNotEmpty(entity.getEventStatusGroupId()),"event_status_group_id",entity.getEventStatusGroupId())
        );
    }

    //根据id返回对应信息
    @DataSource("slave1")
    public AsEventUcMetadata getEntityById(String id)
    {
        return this.getById(id);
    }

    @DataSource("slave1")
    public boolean saveEntity(AsEventUcMetadata entity)
    {
        entity.setId(IdGen.snowflakeId());
        return this.save(entity);
    }

    @DataSource("slave1")
    public boolean updateEntityById(AsEventUcMetadata entity)
    {
        return this.updateById(entity);
    }

    @DataSource("slave1")
    public boolean deleteEntityById(String id)
    {
        return this.removeById(id);
    }

    @DataSource("slave1")
    public List<AsEventUcMetadata> osFindEventUcMetadataByDeviceModelId(String id)
    {
        return baseMapper.osFindEventUcMetadataByDeviceModelId(id);
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
