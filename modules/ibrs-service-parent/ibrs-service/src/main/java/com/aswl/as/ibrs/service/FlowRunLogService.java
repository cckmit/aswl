package com.aswl.as.ibrs.service;

import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.ibrs.api.module.FlowRunLog;
import com.aswl.as.ibrs.api.vo.DeviceFaultVo;
import com.aswl.as.ibrs.mapper.FlowRunLogMapper;
import com.aswl.as.user.api.feign.UserServiceClient;
import com.aswl.as.user.api.module.User;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Slf4j
@Service
public class FlowRunLogService extends CrudService<FlowRunLogMapper, FlowRunLog> {
    private final FlowRunLogMapper flowRunLogMapper;
    private final UserServiceClient userServiceClient;

    /**
     * 新增流程实例日志
     *
     * @param flowRunLog
     * @return int
     */
    @Transactional
    @Override
    public int insert(FlowRunLog flowRunLog) {
        return super.insert(flowRunLog);
    }

    /**
     * 删除流程实例日志
     *
     * @param flowRunLog
     * @return int
     */
    @Transactional
    @Override
    public int delete(FlowRunLog flowRunLog) {
        return super.delete(flowRunLog);
    }

    /**
     * 工单处理历史记录
     * @param pageInfo
     * @param id
     * @return
     */
    public PageInfo<DeviceFaultVo> findWorkOrderHistory(PageInfo<DeviceFaultVo> pageInfo, String id) {
        PageHelper.startPage(pageInfo.getPageNum(), pageInfo.getPageSize());
        List<DeviceFaultVo> list = flowRunLogMapper.findWorkOrderHistory(id);
        PageInfo<DeviceFaultVo> info = new PageInfo<>(list);
        List<DeviceFaultVo> voList = new ArrayList<>();
        for (DeviceFaultVo vo : list) {
            if (vo != null && vo.getUserId() != null) {
                ResponseBean<User> user = userServiceClient.user(vo.getUserId());
                if (user.getCode() == 200) {
                    vo.setHandler(user.getData().getName());
                }
            }
            voList.add(vo);
        }
        info.setList(voList);
        return info;
    }
}