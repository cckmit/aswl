package com.aswl.as.metadata.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.ibrs.api.module.EventEcurrent;
import com.aswl.as.metadata.mapper.EventEcurrentMapper;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
@Service
public class EventEcurrentService extends CrudService<EventEcurrentMapper, EventEcurrent> {
	private final EventEcurrentMapper eventEcurrentMapper;

	/**
	 * 新增设备事件-电源电流
	 * 
	 * @param eventEcurrent
	 * @return int
	 */
	@Transactional
	@Override
	public int insert(EventEcurrent eventEcurrent) {
		return super.insert(eventEcurrent);
	}

	/**
	 * 更新设备事件-电源电流
	 * 
	 * @param eventEcurrent
	 * @return int
	 */
	@Transactional
	@Override
	public int update(EventEcurrent eventEcurrent) {
		return eventEcurrentMapper.update(eventEcurrent);
	}
	/**
	 * 删除设备事件-电源电流
	 * 
	 * @param eventEcurrent
	 * @return int
	 */
	@Transactional
	@Override
	public int delete(EventEcurrent eventEcurrent) {
		return super.delete(eventEcurrent);
	}
	
	/***
	 * 根据设备ID查询设备
	 * @param deviceId
	 * @return
	 */
    public EventEcurrent findByDeviceId(String deviceId){
        return eventEcurrentMapper.findByDeviceId(deviceId);
    }

}