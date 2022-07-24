package com.aswl.as.metadata.mapper;

import com.aswl.as.common.core.persistence.CrudMapper;
import com.aswl.as.ibrs.api.module.AlarmLevel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

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

    List<Map> findAlarmLevel();

    /**
     *
     * @return
     */
    @Select("select * from as_alarm_level where alarm_level = #{alarmLevel} and tenant_code = #{tenantCode}")
    AlarmLevel findByAlarmLevel(@Param("alarmLevel") Integer alarmLevel, @Param("tenantCode") String tenantCode);

    /**
     * 根据alarmType集合获取告警最小级别（最高级）
     * @param alarmTypes
     * @return
     */
    AlarmLevel loadMinLevelByAlarmTypes(@Param("alarmTypes") List<String> alarmTypes, @Param("tenantCode") String tenantCode);
}
