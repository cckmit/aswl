package com.aswl.as.metadata.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.ibrs.api.module.EventBase;
import com.aswl.as.metadata.mapper.EventBaseMapper;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
@Service
public class EventBaseService extends CrudService<EventBaseMapper, EventBase> {
	private final EventBaseMapper eventBaseMapper;

	/**
	 * 新增设备事件-基础
	 * 
	 * @param eventBase
	 * @return int
	 */
	@Transactional
	@Override
	public int insert(EventBase eventBase) {
		return super.insert(eventBase);
	}
	/**
	 * 更新设备事件-基础
	 * 
	 * @param eventBase
	 * @return int
	 */
	@Transactional
	@Override
	public int update(EventBase eventBase) {
		return super.update(eventBase);
	}
	/**
	 * 删除设备事件-基础
	 * 
	 * @param eventBase
	 * @return int
	 */
	@Transactional
	@Override
	public int delete(EventBase eventBase) {
		return super.delete(eventBase);
	}

	/**
	 * 根据deviceId查询基础设备事件
	 * 
	 * @param deviceId
	 * @return
	 */
	public EventBase findByDeviceId(String deviceId) {
		return eventBaseMapper.findByDeviceId(deviceId);
	}
}