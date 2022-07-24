package com.aswl.as.ibrs.api.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 城市项目设备信息
 * @author df
 * @date 2021/02/02 17:26
 */
@Data
public class CityProjectDeviceVo implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * ID
     */
    private String id;

    /**
     * 父ID
     */
    private String parentId;
    /**
     * 名称
     */

    private String name;

    /**
     * 编码
     */
    private String code;

    /**
     * 类型 (0,城市,1,项目,2,报障箱,3,摄像机)
     */
    private String type;

    /**
     * 设备状态
     */
    private String status;
}
