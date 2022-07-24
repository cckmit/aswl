package com.aswl.as.ibrs.api.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 地图上设备搜索参数
 *
 * @author df
 * @date 2020/12/09 14:26
 */
@Data
public class MapQueryDto implements Serializable {

    /**
     * 区域编码
     */
    private String regionCode;

    /**
     * 最小经度
     */
    private String latMin;


    /**
     * 最大经度
     */
    private String latMax;


    /**
     * 最小维度
     */
    private String lonMin;

    /**
     * 最小维度
     */
    private String lonMax;

    /**
     * 设备类型(2,报障箱 ,3,摄像机)
     */
    private String devType;


    /**
     * 租户编码
     */
    private String tenantCode;

    /**
     * 项目ID
     */
    private String projectId;

    /**
     * 关键字
     */
    private String keywords;

    /**
     * 报警级别
     */
    private String[] alarmLevels;

    /**
     * 设备监控列表离线标识
     */
    private String offlineFlag;

    /**
     * 云端标识
     */
    private String isAsOs;


}
