package com.aswl.as.metadata.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.ibrs.api.module.AlarmType;
import com.aswl.as.ibrs.api.vo.AlarmTypeVo;
import com.aswl.as.metadata.mapper.AlarmTypeMapper;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
@Service
public class AlarmTypeService extends CrudService<AlarmTypeMapper, AlarmType> {
    private final AlarmTypeMapper alarmTypeMapper;

    /**
     * 新增报警类型
     *
     * @param alarmType
     * @return int
     */
    @Transactional
    @Override
    public int insert(AlarmType alarmType) {
        return super.insert(alarmType);
    }

    /**
     * 删除报警类型
     *
     * @param alarmType
     * @return int
     */
    @Transactional
    @Override
    public int delete(AlarmType alarmType) {
        return super.delete(alarmType);
    }

    /**
     * 查询报警类型
     * @return
     */
    public List<AlarmTypeVo> findAlarmType() {
        return alarmTypeMapper.findAlarmType();
    }

    /**
     * 根据状态值及元数据表名、字段查询数据
     * @param statusValue
     * @param tabCode
     * @param fldCode
     * @return
     */
    public AlarmType findByStatusValueWithTabFld(Integer statusValue, String tabCode, String fldCode){
        return alarmTypeMapper.findByStatusValueWithTabFld(statusValue, tabCode, fldCode);
    }
    
    /**
     * 根据alarmTypes 字符串信息 查询出目前包含的告警状态
     * @param alarmTypes
     * @return
     */
    public List<AlarmType> findAlarmTypes(String alarmTypes) {
        return alarmTypeMapper.findByAlarmTypes(alarmTypes);
    }

    /**
     * 根据alarmType获取数据
     * @param alarmType
     * @return
     */
    public AlarmType findAlarmType(String alarmType){
        List<AlarmType> alarmTypeList = this.findAlarmTypes(alarmType);
        return (alarmTypeList != null && alarmTypeList.size() > 0) ? alarmTypeList.get(0) : null;
    }
}