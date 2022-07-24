package com.aswl.as.asos.modules.ibrs.service.impl;

import com.aswl.as.common.core.constant.CommonConstant;
import com.aswl.as.common.core.utils.IdGen;
import com.aswl.as.asos.common.utils.Query;
import com.aswl.as.asos.common.utils.PageUtils;
import com.aswl.as.asos.common.annotation.SysLog;
import com.aswl.as.asos.datasource.annotation.DataSource;
import com.aswl.as.asos.modules.ibrs.entity.AsEventUcMetadataModelOperation;
import com.aswl.as.asos.modules.ibrs.mapper.AsEventUcMetadataModelOperationMapper;
import com.aswl.as.asos.modules.ibrs.service.IAsEventUcMetadataModelOperationService;
import com.aswl.as.ibrs.api.dto.EventUcMetadataModelOperationDto;
import com.aswl.as.ibrs.api.module.EventUcMetadataModelOperation;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 设备型号事件元数据-状态操作 服务实现类
 * </p>
 *
 * @author hfx
 * @since 2020-01-09
 */
@Service
public class AsEventUcMetadataModelOperationServiceImpl extends ServiceImpl<AsEventUcMetadataModelOperationMapper, AsEventUcMetadataModelOperation> implements IAsEventUcMetadataModelOperationService {

    @DataSource("slave1")
    public PageUtils queryPage(Map<String, Object> params)
    {

        IPage<AsEventUcMetadataModelOperation> page = this.page(
            new Query<AsEventUcMetadataModelOperation>().getPage(params),
                new QueryWrapper<AsEventUcMetadataModelOperation>()
            );

        return new PageUtils(page);
    }

    @DataSource("slave1")
    public List<AsEventUcMetadataModelOperation> findList(AsEventUcMetadataModelOperation entity)
    {
        return list(new QueryWrapper<AsEventUcMetadataModelOperation>());
    }

    //根据id返回对应信息
    @DataSource("slave1")
    public AsEventUcMetadataModelOperation getEntityById(String id)
    {
        return this.getById(id);
    }

    @DataSource("slave1")
    public boolean saveEntity(AsEventUcMetadataModelOperation entity)
    {
        entity.setId(IdGen.snowflakeId());
        return this.save(entity);
    }

    @DataSource("slave1")
    public boolean updateEntityById(AsEventUcMetadataModelOperation entity)
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

    //根据id删除所有
    @DataSource("slave1")
    public int osDeleteByEventMetadataModelId(String id)
    {
        return baseMapper.osDeleteByEventMetadataModelId(id);
    }

    @DataSource("slave1")
    public int osInsertBatch(List<AsEventUcMetadataModelOperation> list)
    {

        for(AsEventUcMetadataModelOperation operation:list)
        {
            operation.setId(IdGen.snowflakeId());
        }

        return baseMapper.osInsertBatch(list);
    }

}
