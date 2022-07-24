package com.aswl.as.asos.modules.ibrs.service.impl;

import com.aswl.as.common.core.constant.CommonConstant;
import com.aswl.as.common.core.utils.IdGen;
import com.aswl.as.asos.common.utils.Query;
import com.aswl.as.asos.common.utils.PageUtils;
import com.aswl.as.asos.common.annotation.SysLog;
import com.aswl.as.asos.datasource.annotation.DataSource;
import com.aswl.as.asos.modules.ibrs.entity.AsEventUcMetadataModel;
import com.aswl.as.asos.modules.ibrs.mapper.AsEventUcMetadataModelMapper;
import com.aswl.as.asos.modules.ibrs.service.IAsEventUcMetadataModelService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 事件元数据-设备型号 服务实现类
 * </p>
 *
 * @author hfx
 * @since 2020-01-08
 */
@Service
public class AsEventUcMetadataModelServiceImpl extends ServiceImpl<AsEventUcMetadataModelMapper, AsEventUcMetadataModel> implements IAsEventUcMetadataModelService {

    @DataSource("slave1")
    public PageUtils queryPage(Map<String, Object> params)
    {
        IPage<AsEventUcMetadataModel> page = this.page(
            new Query<AsEventUcMetadataModel>().getPage(params),
                new QueryWrapper<AsEventUcMetadataModel>()
            );
        return new PageUtils(page);
    }

    @DataSource("slave1")
    public List<AsEventUcMetadataModel> findList(AsEventUcMetadataModel entity)
    {
        return list(new QueryWrapper<AsEventUcMetadataModel>()
        .eq(StringUtils.isNotEmpty(entity.getDeviceModelId()),"device_model_id",entity.getDeviceModelId())
        );
    }

    //根据id返回对应信息
    @DataSource("slave1")
    public AsEventUcMetadataModel getEntityById(String id)
    {
        return this.getById(id);
    }

    @DataSource("slave1")
    public boolean saveEntity(AsEventUcMetadataModel entity)
    {
//        entity.setProjectId(IdGen.snowflakeId());
        entity.setId(IdGen.snowflakeId());
        return this.save(entity);
    }

    @DataSource("slave1")
    public boolean updateEntityById(AsEventUcMetadataModel entity)
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
