package com.aswl.as.ibrs.service;

import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.ibrs.api.module.Device;
import com.aswl.as.ibrs.api.module.QrcodeBatch;
import com.aswl.as.ibrs.api.vo.AppSanDeviceVo;
import com.aswl.as.ibrs.api.vo.DeviceVo;
import com.aswl.as.ibrs.api.vo.QrCodeDeviceVo;
import com.aswl.as.ibrs.mapper.DeviceMapper;
import com.aswl.as.ibrs.mapper.QrcodeBatchMapper;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@AllArgsConstructor
@Slf4j
@Service
public class QrcodeBatchService extends CrudService<QrcodeBatchMapper, QrcodeBatch> {
    private final QrcodeBatchMapper qrcodeBatchMapper;
    private final DeviceMapper deviceMapper;

    /**
     * 新增二维码批次表记录
     *
     * @param qrcodeBatch
     * @return int
     */
    @Transactional
    @Override
    public int insert(QrcodeBatch qrcodeBatch) {
        return qrcodeBatchMapper.insert(qrcodeBatch);
    }

    /**
     * 删除二维码批次表记录
     *
     * @param qrcodeBatch
     * @return int
     */
    @Transactional
    @Override
    public int delete(QrcodeBatch qrcodeBatch) {
        return qrcodeBatchMapper.delete(qrcodeBatch);
    }


    /**
     * 获取二维码设备信息
     */
    public QrCodeDeviceVo queryQrCodeBath(String qrcode) {
        return  qrcodeBatchMapper.queryQrCodeBath(qrcode);
    }


    /**
     * APP扫码获取二维码设备信息
     */

    public AppSanDeviceVo getSanDevicesInfo(String qrcode) {
        AppSanDeviceVo appSanDeviceVo = new AppSanDeviceVo();
        // 获取设备二维码信息
        QrCodeDeviceVo qrCodeDeviceVo = qrcodeBatchMapper.queryQrCodeBath(qrcode);
        // 获取设备信息
        DeviceVo deviceVo = deviceMapper.findDeviceByQrCode(qrcode);
        appSanDeviceVo.setQrCodeDeviceVo(qrCodeDeviceVo);
        appSanDeviceVo.setDeviceVo(deviceVo);
        return appSanDeviceVo;
    }
}