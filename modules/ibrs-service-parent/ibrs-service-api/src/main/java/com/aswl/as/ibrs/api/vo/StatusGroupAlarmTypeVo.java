package com.aswl.as.ibrs.api.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author dingfei
 * @Description
 * @date 2019-10-25 19:46
 * @Version V1
 */
@Data
public class StatusGroupAlarmTypeVo implements java.io.Serializable {

    /**
     * 主键
     */
    @ApiModelProperty(value ="主键" ,name = "id")
    private String id;
    /**
     * 状态位名称
     */

    @ApiModelProperty(value ="状态位名称" ,name = "statusName")
    private String statusName;
    /**
     * 状态位值
     */

    @ApiModelProperty(value ="状态位值" ,name = "statusValue")
    private Integer statusValue;
    /**
     *报警类型
     */

    @ApiModelProperty(value ="报警类型" ,name = "alarmType")
    private String alarmType;
    /**
     * 报警类型名称
     */

    @ApiModelProperty(value ="报警类型名称" ,name = "alarmTypeName")
    private String alarmTypeName;

    /**
     * 状态组ID
     */

    @ApiModelProperty(value ="状态组ID" ,name = "statusGroupId")
    private String statusGroupId;

    /**
     * 字段编码
     */

    @ApiModelProperty(value ="字段编码" ,name = "fldCode")
    private String fldCode;

    /**
     * 字段值
     */

    @ApiModelProperty(value ="字段值" ,name = "fldName")
    private String fldName;

    /**
     * 单位
     */

    @ApiModelProperty(value ="单位" ,name = "fldUnit")
    private String fldUnit;

    /**
     * 编码
     */

    @ApiModelProperty(value ="编码" ,name = "code")
    private  String code;

    /**
     * 创建时间
     */

    @ApiModelProperty(value ="创建时间" ,name = "createDate")
    private String createDate;

    /**
     * code中文名
     */

    @ApiModelProperty(value ="code中文名" ,name = "codeCN")
    private String codeCN;

    /**
     * 图标路径
     */

    @ApiModelProperty(value ="图标路径" ,name = "iconPath")
    private String iconPath;

    /**
     * 报警级别
     */
    @ApiModelProperty(value = "报警级别",name = "alarmLevel")
    private Integer alarmLevel;
}
