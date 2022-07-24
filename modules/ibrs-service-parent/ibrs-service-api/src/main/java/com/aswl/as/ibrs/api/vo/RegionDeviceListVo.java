package com.aswl.as.ibrs.api.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author df
 * @date 2020/12/08 15:31
 */
@Data
public class RegionDeviceListVo implements Serializable {

    /**
     * 区域名称
     */
    private String regionName;


    /**
     * 在线设备数
     */
    private Integer onlineCount;


    /**
     * 完好设备数
     */

    private  Integer intactCount;


    /**
     * 设备总数
     */
    private Integer  totalCount;

}
