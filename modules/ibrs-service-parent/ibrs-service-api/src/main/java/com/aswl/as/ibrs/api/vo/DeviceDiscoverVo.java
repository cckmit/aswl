package com.aswl.as.ibrs.api.vo;

import lombok.Data;
import java.util.Date;

@Data
public class DeviceDiscoverVo {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private String id;

    /**
     * 设备名称
     */
    private String deviceName;

    /**
     * 出厂编码
     */
    private String deviceFactoryCode;

    /**
     * ip
     */
    private String ip;

    /**
     端口
     */
    private String port;

    /**
     * 设备型号
     */
    private String deviceModelId;

    /**
     * 存储时间
     */
    private Date storeTime;

    /**
     * 系统编码
     */
    private String applicationCode;
    /**
     * 租户编码
     */
    private String tenantCode;

    /**
     * 区域Id
     */
    private String regionId;

    /**
     * 区域code
     */
    private String regionCode;

    /**
     * 区域名称
     */
    private String regionName;
}
