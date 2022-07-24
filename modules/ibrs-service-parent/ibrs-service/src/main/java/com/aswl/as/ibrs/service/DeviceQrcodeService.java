package com.aswl.as.ibrs.service;

import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.ibrs.api.dto.DeviceQrcodeDto;
import com.aswl.as.ibrs.api.module.DeviceQrcode;
import com.aswl.as.ibrs.api.vo.DeviceQrcodeVo;
import com.aswl.as.ibrs.mapper.DeviceQrcodeMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Slf4j
@Service
public class DeviceQrcodeService extends CrudService<DeviceQrcodeMapper, DeviceQrcode> {
    private final DeviceQrcodeMapper deviceQrcodeMapper;

    /**
     * 新增地址库表
     *
     * @param deviceQrcode
     * @return int
     */
    @Transactional
    @Override
    public int insert(DeviceQrcode deviceQrcode) {
        return super.insert(deviceQrcode);
    }

    /**
     * 删除地址库表
     *
     * @param deviceQrcode
     * @return int
     */
    @Transactional
    @Override
    public int delete(DeviceQrcode deviceQrcode) {
        return super.delete(deviceQrcode);
    }

    public DeviceQrcodeVo getDeviceIdByQrCode(String qrCode)
    {
        return deviceQrcodeMapper.getDeviceIdByQrCode(qrCode);
    }

}