package com.aswl.as.metadata.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.ibrs.api.module.EventSfp;
import com.aswl.as.metadata.mapper.EventSfpMapper;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Set;

@AllArgsConstructor
@Slf4j
@Service
public class EventSfpService extends CrudService<EventSfpMapper, EventSfp> {
	private final EventSfpMapper eventSfpMapper;

	/**
	 * 新增设备事件-光口
	 * 
	 * @param eventSfp
	 * @return int
	 */
	@Transactional
	@Override
	public int insert(EventSfp eventSfp) {
		return super.insert(eventSfp);
	}
	
	/**
	 * 更新设备事件-光口
	 * 
	 * @param eventSfp
	 * @return int
	 */
	@Transactional
	@Override
	public int update(EventSfp eventSfp) {
		return eventSfpMapper.update(eventSfp);
	}

	/**
	 * 删除设备事件-光口
	 * 
	 * @param eventSfp
	 * @return int
	 */
	@Transactional
	@Override
	public int delete(EventSfp eventSfp) {
		return super.delete(eventSfp);
	}

	public EventSfp findByDeviceId(String deviceId) {
		return this.eventSfpMapper.findByDeviceId(deviceId);
	}


    /**
     * 个光纤口的禁用启用状态
     * @param key
     * @param deviceId
     * @return
     */
	public Map<String,String> findSfpState(Set<String> key, String deviceId) {
		return eventSfpMapper.findSfpState(key,deviceId);
	}
}