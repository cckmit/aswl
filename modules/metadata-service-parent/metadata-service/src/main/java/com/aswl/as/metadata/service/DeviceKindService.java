package com.aswl.as.metadata.service;

import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.ibrs.api.module.DeviceKind;
import com.aswl.as.metadata.mapper.DeviceKindMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Slf4j
@Service
public class DeviceKindService extends CrudService<DeviceKindMapper, DeviceKind> {
    private final DeviceKindMapper deviceKindMapper;

    /**
     * 新增设备种类
     *
     * @param deviceKind
     * @return int
     */
    @Transactional
    @Override
    public int insert(DeviceKind deviceKind) {
        return super.insert(deviceKind);
    }

    /**
     * 删除设备种类
     *
     * @param deviceKind
     * @return int
     */
    @Transactional
    @Override
    public int delete(DeviceKind deviceKind) {
        return super.delete(deviceKind);
    }

    public DeviceKind findByDeviceId(String deviceId){
        return deviceKindMapper.findByDeviceId(deviceId);
    }
}