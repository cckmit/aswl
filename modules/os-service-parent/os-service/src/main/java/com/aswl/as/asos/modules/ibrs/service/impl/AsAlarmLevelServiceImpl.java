package com.aswl.as.asos.modules.ibrs.service.impl;

import com.aswl.as.common.core.enums.AlarmLevelType;
import com.aswl.as.common.core.utils.IdGen;
import com.aswl.as.asos.common.utils.Query;
import com.aswl.as.asos.common.utils.PageUtils;
import com.aswl.as.asos.common.annotation.SysLog;
import com.aswl.as.asos.datasource.annotation.DataSource;
import com.aswl.as.asos.modules.ibrs.entity.AsAlarmLevel;
import com.aswl.as.asos.modules.ibrs.mapper.AsAlarmLevelMapper;
import com.aswl.as.asos.modules.ibrs.service.IAsAlarmLevelService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 报警级别 服务实现类
 * </p>
 *
 * @author hfx
 * @since 2020-03-23
 */
@Service
public class AsAlarmLevelServiceImpl extends ServiceImpl<AsAlarmLevelMapper, AsAlarmLevel> implements IAsAlarmLevelService {

    @DataSource("slave1")
    public PageUtils queryPage(Map<String, Object> params)
    {
        /*
        String projectName = (String)params.get("projectName");
        String projectDes = (String)params.get("projectDes");
        String projectOwner = (String)params.get("projectOwner");
        */

        IPage<AsAlarmLevel> page = this.page(
            new Query<AsAlarmLevel>().getPage(params),
                new QueryWrapper<AsAlarmLevel>()
                /*
                .like(StringUtils.isNotBlank(projectName),"project_name",projectName)
                .like(StringUtils.isNotBlank(projectDes),"project_des",projectDes)
                .like(StringUtils.isNotBlank(projectOwner),"project_owner",projectOwner)
                */
            );

        return new PageUtils(page);
    }

    @DataSource("slave1")
    public List<AsAlarmLevel> findList(AsAlarmLevel entity)
    {
        return list(new QueryWrapper<AsAlarmLevel>());
    }

    //根据id返回对应信息
    @DataSource("slave1")
    public AsAlarmLevel getEntityById(String id)
    {
        return this.getById(id);
    }

    @DataSource("slave1")
    public boolean saveEntity(AsAlarmLevel entity)
    {
//        entity.setProjectId(IdGen.snowflakeId());
        entity.setId(IdGen.snowflakeId());

        return this.save(entity);
    }

    @DataSource("slave1")
    public boolean updateEntityById(AsAlarmLevel entity)
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
    public boolean newAlarmLevelByTenant(String tenantCode)
    {
        save(createAlarmLevel(AlarmLevelType.FAULT.getType(),AlarmLevelType.FAULT.getDescription(),AlarmLevelType.FAULT.getDescriptionEn(),"设备故障","device fault",0,tenantCode));

        save(createAlarmLevel(AlarmLevelType.WARNING.getType(),AlarmLevelType.WARNING.getDescription(),AlarmLevelType.WARNING.getDescriptionEn(),"设备预警","device warning",1,tenantCode));

        return save(createAlarmLevel(AlarmLevelType.NORMAL.getType(),AlarmLevelType.NORMAL.getDescription(),AlarmLevelType.NORMAL.getDescriptionEn(),"设备正常","device normal",null,tenantCode));
    }

    private AsAlarmLevel createAlarmLevel(int alarmLevel,String alarmLevelName, String alarmLevelNameEn,String remark, String remarkEn, Integer orderGenType,String tenantCode)
    {
        AsAlarmLevel entity=new AsAlarmLevel();
        entity.setId(IdGen.snowflakeId());
        entity.setAlarmLevel(alarmLevel);
        entity.setAlarmLevelName(alarmLevelName);
        entity.setAlarmLevelNameEn(alarmLevelNameEn);
        entity.setRemark(remark);
        entity.setRemarkEn(remarkEn);
        entity.setOrderGenType(orderGenType);
        entity.setTenantCode(tenantCode);
        return entity;
    }

}
