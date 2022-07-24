package com.aswl.as.ibrs.api.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author aswl.com
 * @version 1.0.0
 * @create 2019/10/21 16:29
 */
@Getter@Setter
public class AlarmTypeVo implements Serializable {

    private static final long serialVersionUID = 1L;
    private String id;
    private String groupName;
    private String name;
    private String nameEn;
    private Integer priority;
    private Boolean isGroup;
    private String alarmType;
    private String alarmTypeName;
    private String alarmTypeNameEn;
    private String flag;
    private String kind;
    private Integer alarmLevel;
    private Integer vectorType;
    private String codeCn;
    private String code;
    private Integer pageShow;
    private Integer statusValue;
    private String iconPath;
    
}
