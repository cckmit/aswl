package com.aswl.as.metadata.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.ibrs.api.module.FlowRunPrcs;
import com.aswl.as.metadata.mapper.FlowRunPrcsMapper;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
@Service
public class FlowRunPrcsService extends CrudService<FlowRunPrcsMapper, FlowRunPrcs> {
	
	private final FlowRunPrcsMapper flowRunPrcsMapper;

	/**
	 * 新增流程实例批注信息
	 * 
	 * @param flowRun
	 * @return int
	 */
	@Transactional
	@Override
	public int insert(FlowRunPrcs flowRunPrcs) {
		return super.insert(flowRunPrcs);
	}

	/**
	 * 删除流程实例批注信息
	 * 
	 * @param flowRun
	 * @return int
	 */
	@Transactional
	@Override
	public int delete(FlowRunPrcs flowRunPrcs) {
		return super.delete(flowRunPrcs);
	}
}