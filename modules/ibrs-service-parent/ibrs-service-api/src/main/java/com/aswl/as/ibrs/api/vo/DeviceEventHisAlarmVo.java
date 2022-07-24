package com.aswl.as.ibrs.api.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
@Data
public class DeviceEventHisAlarmVo {

    /**
     * 总数
     */
    @ApiModelProperty(value = "总数",name="total")
    private Integer total;
    /**
     * 三级
     */
    @ApiModelProperty(value = "三级",name="Level3")
    private Integer level3;

    /**
     * 二级
     */
    @ApiModelProperty(value = "二级",name="Level2")
    private Integer level2;

    /**
     * 一级
     */
    @ApiModelProperty(value = "一级",name="Level1")
    private Integer level1;
    /**
     * 表名
     */

    @ApiModelProperty(value = "表名",name="tableName")
    private String tableName;
    /**
     * 最高报警等级
     */
    @ApiModelProperty(value = "最高报警等级",name="alarmLevel")
    private Integer alarmLevel;
    /**
     * 存储时间
     */
    @ApiModelProperty(value = "存储时间",name="storeTime")
    private String date;

}
