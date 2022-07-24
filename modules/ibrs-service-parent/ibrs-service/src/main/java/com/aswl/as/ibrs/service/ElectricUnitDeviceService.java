package com.aswl.as.ibrs.service;
import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.common.core.utils.IdGen;
import com.aswl.as.ibrs.api.module.ElectricUnitDevice;
import com.aswl.as.ibrs.mapper.ElectricUnitDeviceMapper;
import com.aswl.as.ibrs.utils.StringUtils;
import com.aswl.as.user.api.module.RoleRegion;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
@Slf4j
@Service
public class ElectricUnitDeviceService extends CrudService<ElectricUnitDeviceMapper, ElectricUnitDevice> {
    private final ElectricUnitDeviceMapper electricUnitDeviceMapper;

    /**
     * 新增用电单位设备关联表
     *
     * @param electricUnitDevice
     * @return int
     */
    @Transactional
    @Override
    public int insert(ElectricUnitDevice electricUnitDevice) {
        return electricUnitDeviceMapper.insert(electricUnitDevice);
    }

    /**
     * 删除用电单位设备关联表
     *
     * @param electricUnitDevice
     * @return int
     */
    @Transactional
    @Override
    public int delete(ElectricUnitDevice electricUnitDevice) {
        return electricUnitDeviceMapper.delete(electricUnitDevice);
    }

    /**
     * @param unitId  
     * @param devices
     * @return int
     */
    @Transactional
    public int insertBatch(String unitId, List<String> devices) {
        int update = -1;
        if (CollectionUtils.isNotEmpty(devices)) {
            // 删除旧的数据
            electricUnitDeviceMapper.deleteByUnitId(unitId);
            electricUnitDeviceMapper.deleteByDeviceId(StringUtils.join(devices.toArray(),","));
            List<ElectricUnitDevice> electricUnitDevices = new ArrayList<>();
            for (String deviceId : devices) {
                ElectricUnitDevice  electricUnitDevice = new ElectricUnitDevice();
                electricUnitDevice.setId(IdGen.snowflakeId());
                electricUnitDevice.setUnitId(unitId);
                electricUnitDevice.setDeviceId(deviceId);
                electricUnitDevices.add(electricUnitDevice);
            }
            update = electricUnitDeviceMapper.insertBatch(electricUnitDevices);
        }
        return update;
    }
}