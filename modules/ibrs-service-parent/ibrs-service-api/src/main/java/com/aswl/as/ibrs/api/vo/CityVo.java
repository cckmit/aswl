package com.aswl.as.ibrs.api.vo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 区域项目导航VO
 *
 * @author df
 * @date 2021/01/07 16:23
 */
@Data
public class CityVo implements Serializable {

    /**
     * 主键
     */
    private  Integer id;
    /**
     * 名称
     */
    private String name;

    /**
     * 简拼
     */
    private String jianpin;

    /**
     * 编码
     */
    private String code;

    /**
     * 父级ID
     */
    private String parentId;

    /**
     * 行政级别
     */

    private String level;


    /**
     * 经度
     */
    private double lng;

    /**
     * 维度
     */

    private double lat;

    /**
     * 厂商
     */
    private String manufacturer;


    /**
     * 设备型号名称
     */
    private String deviceModelName;


    /**
     * 总数
     */
    private Integer deviceCount;

    /**
     * 在线数
     */
    private Integer onlineCount;


    /**
     * 完好数
     */
    private Integer intactCount;
}
