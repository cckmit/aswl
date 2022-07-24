package com.aswl.as.ibrs.api.vo;

import com.aswl.as.common.core.persistence.TreeEntity;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author aswl.com
 * @version 1.0.0
 * @create 2019/10/14 16:16
 */
@Getter@Setter
public class RegionDeviceVo  extends TreeEntity<RegionDeviceVo> {
    private static final long serialVersionUID = 1L;
    private String id;
    private String parentId;
    private String code;
    private String ip;
    private Integer isOnline;
    private Integer isVideo;
    private String name;
    private Integer type;
    private String iconSkin;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private Integer alarmLevel;
    private String projectId;
}
