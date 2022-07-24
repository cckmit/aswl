package com.aswl.as.ibrs.service;

import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.ibrs.api.module.AlarmRevoke;
import com.aswl.as.ibrs.mapper.AlarmRevokeMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Slf4j
@Service
public class AlarmRevokeService extends CrudService<AlarmRevokeMapper, AlarmRevoke> {
    private final AlarmRevokeMapper alarmRevokeMapper;

    /**
     * 新增报警撤销记录表
     *
     * @param alarmRevoke
     * @return int
     */
    @Transactional
    @Override
    public int insert(AlarmRevoke alarmRevoke) {
        return super.insert(alarmRevoke);
    }

    /**
     * 删除报警撤销记录表
     *
     * @param alarmRevoke
     * @return int
     */
    @Transactional
    @Override
    public int delete(AlarmRevoke alarmRevoke) {
        return super.delete(alarmRevoke);
    }
}