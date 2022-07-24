package com.aswl.as.metadata.mapper;

import com.aswl.as.common.core.persistence.CrudMapper;
import com.aswl.as.ibrs.api.module.AlarmType;
import com.aswl.as.ibrs.api.vo.AlarmTypeVo;
import com.aswl.as.ibrs.api.vo.StatusGroupAlarmTypeVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
*
* 报警类型Mapper
* @author dingfei
* @date 2019-10-22 11:48
*/

@Mapper
public interface AlarmTypeMapper extends CrudMapper<AlarmType> {

    /**
     * 查询报警类型
     * @return
     */
    List<AlarmTypeVo> findAlarmType();


    /**
     *  根据元数据ID和状态值查询
     * @param list  事件元数据ID
     * @param statusValue 状态值
     * @return List
     */
    List<StatusGroupAlarmTypeVo> findAlarmByMetadataId(List<String> list, @Param("statusValue") int statusValue);

    /**
     * 根据状态值及元数据表名、字段查询数据
     * @param statusValue
     * @param tabCode
     * @param fldCode
     * @return
     */
    AlarmType findByStatusValueWithTabFld(@Param("statusValue") Integer statusValue, @Param("tabCode") String tabCode, @Param("fldCode") String fldCode);
    
    /**
     * 根据alarmTypes 字符串信息 查询出目前包含的告警状态
     * @param alarmTypes
     * @return
     */
    List<AlarmType> findByAlarmTypes(@Param("alarmTypes") String alarmTypes);
}
