package com.aswl.as.ibrs.service;

import com.aswl.as.common.core.enums.AlarmLevelType;
import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.common.core.utils.IdGen;
import com.aswl.as.common.core.utils.SysUtil;
import com.aswl.as.ibrs.api.module.AlarmLevel;
import com.aswl.as.ibrs.api.module.AlarmOrderHandle;
import com.aswl.as.ibrs.mapper.AlarmLevelMapper;
import com.aswl.as.ibrs.utils.StringUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Slf4j
@Service
public class AlarmLevelService extends CrudService<AlarmLevelMapper, AlarmLevel> {
    private final AlarmLevelMapper alarmLevelMapper;

    private AlarmOrderHandleService alarmOrderHandleService;

    private static final String ALARM_LEVEL_1_STRING="alarmLevel1";

    private static final String ALARM_LEVEL_2_STRING="alarmLevel2";

    private static final String ORDER_HANDLE_TYPE_STRING="orderHandleType";

    private static final String HANDLE_USER_ID="handleUserId";

    /**
     * 新增设备报警级别
     *
     * @param alarmLevel
     * @return int
     */
    @Transactional
    @Override
    public int insert(AlarmLevel alarmLevel) {
        return super.insert(alarmLevel);
    }

    /**
     * 删除设备报警级别
     *
     * @param alarmLevel
     * @return int
     */
    @Transactional
    @Override
    public int delete(AlarmLevel alarmLevel) {
        return super.delete(alarmLevel);
    }

    public List<Map> findAlarmLevel() {
        // 添加tenantCode条件
        String tenantCode=SysUtil.getTenantCode();
        return alarmLevelMapper.findAlarmLevel(tenantCode);
    }

    public List<AlarmLevel> findWorkOrderConfig() {
        //添加tenantCode条件
        String tenantCode=SysUtil.getTenantCode();
        return alarmLevelMapper.findWorkOrderConfig(tenantCode);
    }

    public int batchUpdate(List<AlarmLevel> alarmLevels) {
        //添加tenantCode条件
        String tenantCode=SysUtil.getTenantCode();
        return alarmLevelMapper.batchUpdate(alarmLevels,tenantCode);
    }

    public Map queryDataForUpdate()
    {
        String tenantCode=SysUtil.getTenantCode();

        // 这里需要改一下
        Map<String,Object> m=new HashMap<>();
        List<AlarmLevel> list= findWorkOrderConfig();
        if(list==null||list.size()==0)
        {
            //没初始化过这些数据，可能初始化失败或者本来没有
            list=initAlarmLevelByTenant(tenantCode);
        }

        m.put(ALARM_LEVEL_1_STRING,list.get(0).getOrderGenType());
        m.put(ALARM_LEVEL_2_STRING,list.get(1).getOrderGenType());

        // 去拿as_alarm_order_handle的值
        AlarmOrderHandle temp=new AlarmOrderHandle();
        temp.setTenantCode(tenantCode);
        List<AlarmOrderHandle> handleList=alarmOrderHandleService.findList(temp);
        AlarmOrderHandle h;
        if(handleList != null && handleList.size() >0)
        {
            h=handleList.get(0);
        }
        else
        {
            h=alarmOrderHandleService.initByTenantCode(tenantCode);
        }

        m.put(ORDER_HANDLE_TYPE_STRING,h.getOrderHandleType());
        m.put(HANDLE_USER_ID,h.getHandleUserId());
        return m;
    }

    public boolean updateData(Map<String,Object> map)
    {
        //  到时候修复这些数据
        String tenantCode=SysUtil.getTenantCode();
        List<AlarmLevel> list= findWorkOrderConfig();
        if(list==null||list.size()==0)
        {
            //没初始化过这些数据，可能初始化失败或者本来没有
            list=initAlarmLevelByTenant(tenantCode);
        }
        AlarmLevel entity;
        if(map.containsKey(ALARM_LEVEL_1_STRING))
        {
            entity=list.get(0);
            entity.setOrderGenType(getInteger(map.get(ALARM_LEVEL_1_STRING)));
            update(entity);
        }
        if(map.containsKey(ALARM_LEVEL_2_STRING))
        {
            entity=list.get(1);
            entity.setOrderGenType(getInteger(map.get(ALARM_LEVEL_2_STRING)));
            update(entity);
        }

        if(map.containsKey(ORDER_HANDLE_TYPE_STRING)||map.containsKey(HANDLE_USER_ID))
        {
            AlarmOrderHandle temp=new AlarmOrderHandle();
            temp.setTenantCode(tenantCode);
            List<AlarmOrderHandle> handleList=alarmOrderHandleService.findList(temp);
            if(handleList!=null&&handleList.size()>0)
            {
                temp=handleList.get(0);
            }
            else
            {
                temp=alarmOrderHandleService.initByTenantCode(tenantCode);
            }
            temp.setOrderHandleType(getInteger(map.get(ORDER_HANDLE_TYPE_STRING)));
            temp.setHandleUserId(getString(map.get(HANDLE_USER_ID)));
            alarmOrderHandleService.update(temp);
        }

        return true;
    }



    public List<AlarmLevel> initAlarmLevelByTenant(String tenantCode)
    {
        List<AlarmLevel> list=new ArrayList<>();

        //
        list.add(createAlarmLevel(AlarmLevelType.FAULT.getType(),AlarmLevelType.FAULT.getDescription(),"设备故障",0,tenantCode));

        list.add(createAlarmLevel(AlarmLevelType.WARNING.getType(),AlarmLevelType.WARNING.getDescription(),"设备预警",1,tenantCode));

        //这个只需要创建，不用返回
        createAlarmLevel(AlarmLevelType.NORMAL.getType(),AlarmLevelType.NORMAL.getDescription(),"设备正常",null,tenantCode);

        return list;
    }

    private Integer getInteger(Object o)
    {
        if(o==null|| StringUtils.isEmpty(o.toString()))
        {
            return null;
        }
        else
        {
            return Integer.valueOf(String.valueOf(o));
        }
    }

    private String getString(Object o)
    {
        if(o==null|| StringUtils.isEmpty(o.toString()))
        {
            return null;
        }
        else
        {
            return String.valueOf(String.valueOf(o));
        }
    }

    //创建一个AlarmLevel对象
    private AlarmLevel createAlarmLevel(int alarmLevel,String alarmLevelName,String remark,Integer orderGenType,String tenantCode)
    {
        AlarmLevel entity=new AlarmLevel();
        entity.setId(IdGen.snowflakeId());
        entity.setAlarmLevel(alarmLevel);
        entity.setAlarmLevelName(alarmLevelName);
        entity.setRemark(remark);
        entity.setOrderGenType(orderGenType);
        entity.setTenantCode(tenantCode);
        super.insert(entity);
        return entity;
    }

    /**
     * 根据租户编码删除报警级别
     * @param tenantCode
     * @return
     */
    public int deleteByTenantCode(String tenantCode){
        
        return  alarmLevelMapper.deleteByTenantCode(tenantCode);
        
    }





}