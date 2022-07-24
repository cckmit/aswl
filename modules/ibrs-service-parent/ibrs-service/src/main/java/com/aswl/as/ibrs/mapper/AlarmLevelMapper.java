package com.aswl.as.ibrs.mapper;

import com.aswl.as.common.core.persistence.CrudMapper;
import org.apache.ibatis.annotations.Mapper;
import com.aswl.as.ibrs.api.module.AlarmLevel;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 设备报警级别Mapper
 *
 * @author dingfei
 * @date 2019-10-23 16:21
 */

@Mapper
public interface AlarmLevelMapper extends CrudMapper<AlarmLevel> {

    List<Map> findAlarmLevel(@Param("tenantCode")String tenantCode);

    List<AlarmLevel> findWorkOrderConfig(@Param("tenantCode")String tenantCode);

    int batchUpdate(@Param("alarmLevels") List<AlarmLevel> alarmLevels,@Param("tenantCode")String tenantCode);
    
    int deleteByTenantCode(@Param("tenantCode") String tenantCode);
}
