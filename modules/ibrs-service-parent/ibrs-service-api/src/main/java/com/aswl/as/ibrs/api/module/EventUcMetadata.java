package com.aswl.as.ibrs.api.module;

import java.util.Date;

import com.aswl.as.common.core.persistence.BaseEntity;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 事件元数据Entity
 *
 * @author dingfei
 * @date 2019-10-22 11:42
 */

@ApiModel(value = "EventUcMetadata", description = "事件元数据Entity")
@Data
public class EventUcMetadata extends BaseEntity<EventUcMetadata> {
    /**
     * 表编码
     */

    @ApiModelProperty(value = "表编码", name = "tabCode")
    private String tabCode;
    /**
     * 表名
     */

    @ApiModelProperty(value = "表名", name = "tabName")
    private String tabName;
    /**
     * 字段编码
     */

    @ApiModelProperty(value = "字段编码", name = "fldCode")
    private String fldCode;
    /**
     * 字段名称
     */

    @ApiModelProperty(value = "字段名称", name = "fldName")
    private String fldName;

    /**
     * 字段名称英文
     */

    @ApiModelProperty(value = "字段名称英文", name = "fldNameEn")
    private String fldNameEn;
    /**
     * 字段类型
     */

    @ApiModelProperty(value = "字段类型", name = "fldType")
    private String fldType;
    /**
     * 字段单位
     */

    @ApiModelProperty(value = "字段单位", name = "fldUnit")
    private String fldUnit;
    /**
     * 状态名
     */

    @ApiModelProperty(value = "状态名", name = "statusName")
    private String statusName;
    /**
     *优先权
     */

    @ApiModelProperty(value = "优先权", name = "priority")
    private Integer priority;
    /**
     * 是否启用
     */

    @ApiModelProperty(value = "是否启用", name = "enable")
    private Integer enable;
    /**
     * 是否报警
     */

    @ApiModelProperty(value = "是否报警", name = "isAlarm")
    private Integer isAlarm;
    /**
     * 矢量类型
     */

    @ApiModelProperty(value = "矢量类型", name = "vectorType")
    private Integer vectorType;
    /**
     * 图标URL
     */

    @ApiModelProperty(value = "图标URL", name = "iconPath")
    private String iconPath;
    /**
     * 是否状态组
     */

    @ApiModelProperty(value = "是否状态组", name = "isStatusGroup")
    private Integer isStatusGroup;
    /**
     * 对应状态组ID
     */

    @ApiModelProperty(value = "对应状态组ID", name = "eventStatusGroupId")
    private String eventStatusGroupId;
    /**
     * 备注
     */

    @ApiModelProperty(value = "备注", name = "remark")
    private String remark;
    /**
     * 创建人账号
     */

    @ApiModelProperty(value = "创建人账号", name = "createBy")
    private String createBy;
    /**
     * 创建人名称
     */

    @ApiModelProperty(value = "创建人名称", name = "createName")
    private String createName;
    /**
     * 更新时间
     */

    @ApiModelProperty(value = "更新时间", name = "updateDate")
    private Date updateDate;
    /**
     * 更新人账号
     */

    @ApiModelProperty(value = "更新人账号", name = "updateBy")
    private String updateBy;
    /**
     * 更新人名称
     */

    @ApiModelProperty(value = "更新人名称", name = "updateName")
    private String updateName;
}
