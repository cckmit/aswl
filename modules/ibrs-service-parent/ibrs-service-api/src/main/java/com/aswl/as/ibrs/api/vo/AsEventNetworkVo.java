package com.aswl.as.ibrs.api.vo;

import com.aswl.as.common.core.persistence.BaseEntity;
import lombok.Data;

/**
 * 设备事件-网络Vo
 *
 * @author liuliepan
 * @date 2019-09-26 15:29
 */
@Data
public class AsEventNetworkVo extends BaseEntity<AsEventNetworkVo> {
    private static final long serialVersionUID = 1L;
    /**
     * 主键ID
     */

    private String id;

    /**
     * 设备ID
     */
    private Integer deviceId;
    /**
     * 区域编码
     */
    private String regionNo;
    /**
     * 网络状态
     */
    private String networkState;
    /**
     *存储时间
     */
    private String storeTime;
    /**
     *系统编号
     */
    private String applicationCode;
    /**
     *租户编码
     */
    private String tenantCode;

}
