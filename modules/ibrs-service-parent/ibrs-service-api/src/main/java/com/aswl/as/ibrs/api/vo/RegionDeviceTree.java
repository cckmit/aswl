package com.aswl.as.ibrs.api.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author aswl.com
 * @version 1.0.0
 * @create 2019/10/16 9:39
 */
@Getter@Setter
public class RegionDeviceTree  implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String parentId;
    private String name;
    private String code;
    private String symbol;
    private Integer isAlarm;
    private Integer isOnline;
    private String projectId;
    private Integer type;
    private String alarmLevel;
    private String alarmLevelName;
    private Integer isVideo;
    private String communicationOperatorId;
    private String communicationOperatorName;
    private List<RegionDeviceTree> children = new ArrayList<>();
}
