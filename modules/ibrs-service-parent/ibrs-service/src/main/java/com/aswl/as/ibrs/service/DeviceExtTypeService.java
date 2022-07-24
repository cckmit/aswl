package com.aswl.as.ibrs.service;

import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.ibrs.api.module.DeviceExtType;
import com.aswl.as.ibrs.mapper.DeviceExtTypeMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Slf4j
@Service
public class DeviceExtTypeService extends CrudService<DeviceExtTypeMapper, DeviceExtType> {
    private final DeviceExtTypeMapper deviceExtTypeMapper;

    /**
     * 新增外接设备类型表
     *
     * @param deviceExtType
     * @return int
     */
    @Transactional
    @Override
    public int insert(DeviceExtType deviceExtType) {
        return super.insert(deviceExtType);
    }

    /**
     * 删除外接设备类型表
     *
     * @param deviceExtType
     * @return int
     */
    @Transactional
    @Override
    public int delete(DeviceExtType deviceExtType) {
        return super.delete(deviceExtType);
    }
}