package com.aswl.as.ibrs.service;

import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.common.core.utils.IdGen;
import com.aswl.as.ibrs.api.module.DeviceType;
import com.aswl.as.ibrs.mapper.DeviceTypeMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Slf4j
@Service
public class DeviceTypeService extends CrudService<DeviceTypeMapper, DeviceType> {
    private final DeviceTypeMapper deviceTypeMapper;

    /**
     * 新增设备类型
     *
     * @param deviceType
     * @return int
     */
    @Transactional
    @Override
    public int insert(DeviceType deviceType) {
        return super.insert(deviceType);
    }

    /**
     * 删除设备类型
     *
     * @param deviceType
     * @return int
     */
    @Transactional
    @Override
    public int delete(DeviceType deviceType) {
        return super.delete(deviceType);
    }

    /**
     *  根据设备类型查询设备类型是否存在
     * @param deviceType
     * @return DeviceType
     */
   public  DeviceType findByDeviceType(@Param("deviceType") String deviceType){
        return deviceTypeMapper.findByDeviceType(deviceType);
    }

}

