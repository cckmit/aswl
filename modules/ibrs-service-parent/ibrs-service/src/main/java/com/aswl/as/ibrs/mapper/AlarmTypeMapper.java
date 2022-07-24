package com.aswl.as.ibrs.mapper;

import com.aswl.as.ibrs.api.dto.AlarmTypeDto;
import com.aswl.as.ibrs.api.vo.AlarmTypeVo;
import com.aswl.as.ibrs.api.vo.StatusGroupAlarmTypeVo;
import org.apache.ibatis.annotations.Mapper;
import com.aswl.as.common.core.persistence.CrudMapper;
import com.aswl.as.ibrs.api.module.AlarmType;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 报警类型Mapper
 *
 * @author dingfei
 * @date 2019-10-22 11:48
 */

@Mapper
public interface AlarmTypeMapper extends CrudMapper<AlarmType> {

    /**
     * 查询报警类型
     *
     * @return
     */
    List<AlarmTypeVo> findAlarmType(@Param("alarmLevels") String[] alarmLevels);


    /**
     * 根据元数据ID和状态值查询
     *
     * @param list        事件元数据ID
     * @param statusValue 状态值
     * @return List
     */
    List<StatusGroupAlarmTypeVo> findAlarmByMetadataId(@Param("list") List<String> list, @Param("statusValue") String statusValue);

    List<AlarmTypeVo> findDeviceAlarmType(@Param("alarmLevels") String[] alarmLevels, @Param("id") String id,@Param("kind")String kind);

    /**
     * 模糊查询告警类型
     * @param prefix
     * @return
     */
    String findTypes(String prefix);

    /**
     * 根据alarmType集合获取告警最小级别（最高级）
     * @param alarmTypes
     * @return
     */
    Integer loadMinAlarmLevel(@Param("alarmTypes") List<String> alarmTypes);


    /**
     * 查询告警类型列表
     * @return
     */
    List<AlarmTypeVo> findAlarmTypeListInfo(AlarmTypeDto alarmTypeDto);
}
