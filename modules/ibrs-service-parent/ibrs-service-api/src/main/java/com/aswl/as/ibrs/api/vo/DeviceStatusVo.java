package com.aswl.as.ibrs.api.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author dingfei
 * @Description
 * @date 2019-10-25 19:43
 * @Version V1
 */
@Data
public class DeviceStatusVo {

    /**
     * 温度/湿度值
     */
    @ApiModelProperty(value = "温度/湿度值",name = "value")
    private String value;

    /**
     * 报警类型
     */
    @ApiModelProperty(value = "报警类型",name = "alarmType")
    private String alarmType;

    /**
     * 报警类型名称
     */

    @ApiModelProperty(value = "报警类型名称",name = "alarmTypeName")
    private String alarmTypeName;

    /**
     * 编码
     */
    @ApiModelProperty(value = "编码",name = "code")
    private String code;

    /**
     * code中文名
     */

    @ApiModelProperty(value = "code中文名",name = "codeCN")
    private String codeCN;

    /**
     * 矢量类型
     */
    @ApiModelProperty(value = "矢量类型 0 模拟量 1开关量",name = "vectorType")
    private Integer vectorType;

    /**
     * 字段编码
     */

    @ApiModelProperty(value = "字段编码",name = "fldCode")
    private String fldCode;

    /**
     * 字段值
     */

    @ApiModelProperty(value = "字段值",name = "fldName")
    private String fldName;

    /**
     * 字段值英文
     */

    @ApiModelProperty(value = "字段值英文",name = "fldNameEn")
    private String fldNameEn;

    /**
     * 单位
     */

    @ApiModelProperty(value = "单位",name = "fldUnit")
    private String fldUnit;

    /**
     * 报警类型图片地址
     */

    @ApiModelProperty(value = "报警类型图片地址",name = "iconPath")
    private String iconPath;


    /**
     * 状态位操作权限list
     */

    @ApiModelProperty(value = "状态位操作权限list",name = "operationList")
    private List<StatusOperationVo> operationList;

    /**
     * 报警级别
     */
    @ApiModelProperty(value = "报警级别",name = "alarmLevel")
    private Integer alarmLevel;

    /**
     * 下级设备名
     */
    @ApiModelProperty(value = "下级设备",name = "subsetDevice")
    private String subsetDevice;

    /**
     * 下级设备ip
     */
    @ApiModelProperty(value = "下级设备ip",name = "subsetIp")
    private String subsetIp;

    /**
     * 状态组
     */
    @ApiModelProperty(value = "状态组",name = "statusGroupName")
    private String statusGroupName;

    /**
     * 状态组(中文)
     */
    @ApiModelProperty(value = "状态组(中文)",name = "statusGroupNameCn")
    private String statusGroupNameCn;
}
