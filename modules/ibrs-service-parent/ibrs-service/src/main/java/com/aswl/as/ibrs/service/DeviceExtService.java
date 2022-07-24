package com.aswl.as.ibrs.service;

import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.ibrs.api.module.DeviceExt;
import com.aswl.as.ibrs.mapper.DeviceExtMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Slf4j
@Service
public class DeviceExtService extends CrudService<DeviceExtMapper, DeviceExt> {
    private final DeviceExtMapper deviceExtMapper;

    /**
     * 新增外接设备表
     *
     * @param deviceExt
     * @return int
     */
    @Transactional
    @Override
    public int insert(DeviceExt deviceExt) {
        return super.insert(deviceExt);
    }

    /**
     * 删除外接设备表
     *
     * @param deviceExt
     * @return int
     */
    @Transactional
    @Override
    public int delete(DeviceExt deviceExt) {
        return super.delete(deviceExt);
    }
}