package com.aswl.as.ibrs.service;
import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.ibrs.api.module.EventElectricity;
import com.aswl.as.ibrs.mapper.EventElectricityMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Slf4j
@Service
public class EventElectricityService extends CrudService<EventElectricityMapper, EventElectricity> {
private final EventElectricityMapper eventElectricityMapper;

/**
* 新增设备事件-电量
* @param  eventElectricity
* @return  int
*/
@Transactional
@Override
public int insert(EventElectricity eventElectricity) {
return super.insert(eventElectricity);
}

/**
* 删除设备事件-电量
* @param eventElectricity
* @return int
*/
@Transactional
@Override
public int delete(EventElectricity eventElectricity) {
return super.delete(eventElectricity);
}
}