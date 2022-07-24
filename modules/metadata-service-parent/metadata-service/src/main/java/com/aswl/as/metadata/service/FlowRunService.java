package com.aswl.as.metadata.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.ibrs.api.module.FlowRun;
import com.aswl.as.metadata.mapper.FlowRunMapper;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
@Service
public class FlowRunService extends CrudService<FlowRunMapper, FlowRun> {
	private final FlowRunMapper flowRunMapper;

	/**
	 * 新增流程实例基本信息
	 * 
	 * @param flowRun
	 * @return int
	 */
	@Transactional
	@Override
	public int insert(FlowRun flowRun) {
		return super.insert(flowRun);
	}

	/**
	 * 删除流程实例基本信息
	 * 
	 * @param flowRun
	 * @return int
	 */
	@Transactional
	@Override
	public int delete(FlowRun flowRun) {
		return super.delete(flowRun);
	}
	
	/**
	 * 根据设备ID 与 报警类型查询报警工单
	 * 
	 * @param deviceId 设备ID
	 * @param alarmType 报警类型
	 * @return List<FlowRun> 工单列表
	 */
	public List<FlowRun> findListByDeviceIdAndAlarmType(String deviceId, String alarmType) {
		FlowRun  flowRun = new FlowRun();
		flowRun.setBeginDeviceId(deviceId);
		flowRun.setAlarmType(alarmType);
		return flowRunMapper.findList(flowRun);
	}
}