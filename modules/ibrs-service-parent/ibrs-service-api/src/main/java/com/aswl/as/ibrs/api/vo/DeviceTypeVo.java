package com.aswl.as.ibrs.api.vo;

import com.aswl.as.common.core.persistence.BaseEntity;
import com.aswl.as.ibrs.api.module.DeviceKind;
import com.aswl.as.ibrs.api.module.DeviceType;
import lombok.Data;

import java.io.Serializable;

/**
 * 设备类型Vo
 *
 * @author dingfei
 * @date 2019-09-26 15:29
 */
@Data
public class DeviceTypeVo extends BaseEntity<DeviceType> {

    private static final long serialVersionUID = 1L;

    /**
     * 设备类型
     */

    private String deviceType;
    /**
     * 设备种类
     */

    private String deviceKindId;

    /**
     * 设备种类名称
     */

    private String deviceKindName;

    /**
     * 设备类型名
     */

    private String deviceTypeName;
    /**
     * 是否报告状态
     */

    private Integer isReportStatus;
    /**
     * 创建终端
     */

    private String createTerminal;
    /**
     * 修改终端
     */

    private String modifyTerminal;
    /**
     * 备注
     */

    private String remark;

    /**
     * 是否可编辑（0：否，1：是）
     */
    private Integer isEdit;
}
