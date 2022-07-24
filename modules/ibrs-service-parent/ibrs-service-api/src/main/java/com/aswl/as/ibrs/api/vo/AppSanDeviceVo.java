package com.aswl.as.ibrs.api.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author df
 * @date 2020/12/16 14:52
 */
@Data
public class AppSanDeviceVo implements Serializable {

    /**
     * 二维码批次基本信息
     */
    private QrCodeDeviceVo qrCodeDeviceVo;

    /**
     * 设备信息
     */
    private DeviceVo deviceVo;
}
