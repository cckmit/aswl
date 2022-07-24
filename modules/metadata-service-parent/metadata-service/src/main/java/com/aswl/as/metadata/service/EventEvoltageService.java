package com.aswl.as.metadata.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.ibrs.api.module.EventEvoltage;
import com.aswl.as.metadata.mapper.EventEvoltageMapper;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
@Service
public class EventEvoltageService extends CrudService<EventEvoltageMapper, EventEvoltage> {
	private final EventEvoltageMapper eventEvoltageMapper;

	/**
	 * 新增设备事件-电压
	 * 
	 * @param eventEvoltage
	 * @return int
	 */
	@Transactional
	@Override
	public int insert(EventEvoltage eventEvoltage) {
		return super.insert(eventEvoltage);
	}
	
	/**
	 * 更新设备事件-电压
	 * 
	 * @param eventEvoltage
	 * @return int
	 */
	@Transactional
	@Override
	public int update(EventEvoltage eventEvoltage) {
		return eventEvoltageMapper.update(eventEvoltage);
	}

	/**
	 * 删除设备事件-电压
	 * 
	 * @param eventEvoltage
	 * @return int
	 */
	@Transactional
	@Override
	public int delete(EventEvoltage eventEvoltage) {
		return super.delete(eventEvoltage);
	}

	/**
	 * 根据deviceId查询
	 */
	public EventEvoltage findByDeviceId(String deviceId) {
		return eventEvoltageMapper.findByDeviceId(deviceId);
	}
}