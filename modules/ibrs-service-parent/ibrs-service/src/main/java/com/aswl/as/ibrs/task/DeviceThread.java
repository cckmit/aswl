package com.aswl.as.ibrs.task;

import com.aswl.as.ibrs.api.module.Device;
import com.aswl.as.ibrs.mapper.DeviceMapper;

/**
 * @author dingfei
 * @date 2019-12-04 13:54
 * @Version V1
 */
public class DeviceThread implements Runnable {
    DeviceMapper deviceMapper;
    Device device;

    public DeviceThread(DeviceMapper deviceMapper,Device device) {
        this.deviceMapper = deviceMapper;
        this.device = device;
    }
    @Override
    public void run() {
        deviceMapper.insert(device);
    }
}
