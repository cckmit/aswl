package com.aswl.as.ibrs.api.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 区域项目导航项目信息
 * @author df
 * @date 2021/01/08 15:24
 */
@Data
public class ProjectVo implements Serializable {

    /**
     * 项目ID
     */
    private String projectId;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 项目编码
     */
    private String projectCode;

    /**
     * 城市ID
     */
    private String id;

    /**
     * 城市名称
     */
    private String name;

    /**
     * 父ID
     */
    private String parentId;

    /**
     * 城市编码
     */
    private String code;


    /**
     * 父级城市ID
     */
    private String parentCityId;


    /**
     * 父级城市名称
     */
    private String parentCityName;

    /**
     * 父级城市父ID
     */
    private String parentCityParentId;


    /**
     * 父级城市城市编码
     */
    private String parentCityCode;

    /**
     * 行政级别
     */
    private String level;

    

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

    /**
     * 经度
     */
    
    private Double longitude;

    /**
     * 维度
     */
    private Double latitude;

    /**
     * 系统图标
     */
    private String sysLogo;
   
    
    /**
     * 告警级别
     */

    private Integer alarmLevel;

}
