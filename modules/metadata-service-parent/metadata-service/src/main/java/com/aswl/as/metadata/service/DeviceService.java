package com.aswl.as.metadata.service;

import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.ibrs.api.module.Device;
import com.aswl.as.metadata.mapper.DeviceMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Slf4j
@Service
public class DeviceService extends CrudService<DeviceMapper, Device> {

    private final DeviceMapper deviceMapper;

    public Device findById(String id){
        return deviceMapper.findById(id);
    }

    public List<Device> findByIds(String ids){
        return deviceMapper.getDevicesByIds(ids);
    }


    /**
     * 根据Ip和租户编码查询局域网设备
     */
    public com.aswl.iot.dto.Device findByIpAndTenantCode(String ip, String tenantCode,String projectCode) {
        return deviceMapper.findByIpAndTenantCode(ip,tenantCode,projectCode);
    }

    /**
     * 设备的下级设备
     * @param id
     * @param kind
     * @return
     */
    public List<Device> findChild(String id, String kind) {
        return deviceMapper.findChild(id,kind);
    }

    /**
     * 上级设备
     * @param deviceId
     * @return
     */
    public Device findParent(String deviceId) {
        return deviceMapper.findParent(deviceId);
    }
}
