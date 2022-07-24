package com.aswl.as.ibrs.service;

import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.ibrs.api.module.FlowInfo;
import com.aswl.as.ibrs.mapper.FlowInfoMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Slf4j
@Service
public class FlowInfoService extends CrudService<FlowInfoMapper, FlowInfo> {
private final FlowInfoMapper flowInfoMapper;

/**
* 新增流程信息
* @param  flowInfo
* @return  int
*/
@Transactional
@Override
public int insert(FlowInfo flowInfo) {
return super.insert(flowInfo);
}

/**
* 删除流程信息
* @param flowInfo
* @return int
*/
@Transactional
@Override
public int delete(FlowInfo flowInfo) {
return super.delete(flowInfo);
}

}