package com.aswl.as.ibrs.mapper;

import com.aswl.as.common.core.persistence.CrudMapper;
import com.aswl.as.ibrs.api.dto.DeviceAlarmDto;
import com.aswl.as.ibrs.api.module.DeviceEventHisAlarm;
import com.aswl.as.ibrs.api.vo.DeviceEventHisAlarmVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
@Mapper
public interface AsEventHisAlarmMapper extends CrudMapper<DeviceEventHisAlarm> {

     List<DeviceEventHisAlarmVo> finByalarmlevel(@Param("list") List list, DeviceAlarmDto deviceAlarmDto);
     List<DeviceEventHisAlarmVo> finByinfoTable(@Param("list") List list);

     List<DeviceEventHisAlarmVo> findEventHisTable(@Param("list") List<String> list);

     List<String> findHisPatrolTableName(@Param("histTablePrefix") String histTablePrefix);

     List<String> findHisPatrolTableNameByTime(@Param("list") List<String> list);

     List<String> findTables(@Param("hisTables") List<String> hisTables);

     List<String> findTableNames(@Param("list") List<String> list);

     List<String> findHisAlarmTab(@Param("list") List<String> list);

     List<String> finHisTable(@Param("tables") List<String> tables);

     List<String> findListTab(@Param("status") Integer status);
     
     List<String> findEventHisTab(@Param("status") Integer status);

     /**
      * 删除历史表里面告警信息
      * @param tabName
      *@param uEventIds
      * @param projectId
      * @param alarmLevel
      * @return int
      */
     int deleteHisAlarm(@Param("tabName") String tabName, @Param("uEventIds") List<String> uEventIds, 
                        @Param("projectId") String projectId, @Param("alarmLevel") String alarmLevel);

     /**
      *  根据项目ID和告警级别查询统一事件ID集合
      * @param tabName
      * @param projectId
      * @param alarmLevel
      * @return list
      */
     List<String> findByUeventIds(@Param("tabName") String tabName,@Param("projectId") String projectId, 
                                  @Param("alarmLevel") String alarmLevel);
     
}
