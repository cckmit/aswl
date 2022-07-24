package com.aswl.as.asos.modules.ibrs.service.impl;

import com.aswl.as.common.core.constant.CommonConstant;
import com.aswl.as.common.core.utils.IdGen;
import com.aswl.as.asos.common.utils.Query;
import com.aswl.as.asos.common.utils.PageUtils;
import com.aswl.as.asos.common.annotation.SysLog;
import com.aswl.as.asos.datasource.annotation.DataSource;
import com.aswl.as.asos.modules.ibrs.entity.AsDeviceModelAlarmThreshold;
import com.aswl.as.asos.modules.ibrs.mapper.AsDeviceModelAlarmThresholdMapper;
import com.aswl.as.asos.modules.ibrs.service.IAsDeviceModelAlarmThresholdService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 设备型号区间报警 服务实现类
 * </p>
 *
 * @author hfx
 * @since 2020-01-08
 */
@Service
public class AsDeviceModelAlarmThresholdServiceImpl extends ServiceImpl<AsDeviceModelAlarmThresholdMapper, AsDeviceModelAlarmThreshold> implements IAsDeviceModelAlarmThresholdService {

    @DataSource("slave1")
    public PageUtils queryPage(Map<String, Object> params)
    {

        IPage<AsDeviceModelAlarmThreshold> page = this.page(
            new Query<AsDeviceModelAlarmThreshold>().getPage(params),
                new QueryWrapper<AsDeviceModelAlarmThreshold>()
            );

        return new PageUtils(page);
    }

    @DataSource("slave1")
    public List<AsDeviceModelAlarmThreshold> findList(AsDeviceModelAlarmThreshold entity)
    {
        return list(new QueryWrapper<AsDeviceModelAlarmThreshold>()
                .eq(StringUtils.isNotEmpty(entity.getEventMetadataModelId()),"event_metadata_model_id",entity.getEventMetadataModelId()));
    }

    //根据id返回对应信息
    @DataSource("slave1")
    public AsDeviceModelAlarmThreshold getEntityById(String id)
    {
        return this.getById(id);
    }

    @DataSource("slave1")
    public boolean saveEntity(AsDeviceModelAlarmThreshold entity)
    {
        entity.setId(IdGen.snowflakeId());
        return this.save(entity);
    }

    @DataSource("slave1")
    public boolean updateEntityById(AsDeviceModelAlarmThreshold entity)
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


    @DataSource("slave1")
    @Transactional
    public boolean insertBatch(List<AsDeviceModelAlarmThreshold> list)
    {
//        entity.setId(IdGen.snowflakeId());
//        return this.save(entity);
        for(AsDeviceModelAlarmThreshold temp:list)
        {
            temp.setId(IdGen.snowflakeId());
            this.save(temp);
        }
        return true;
    }

}
