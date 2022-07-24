package com.aswl.as.user.api.dto;

import com.aswl.as.common.core.constant.CommonConstant;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @version 1.0.0
 * @Author ke
 * @create 2019/9/19 10:27
 */
@Data
public class PositionInfoDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String positionId;

    /**
     * 职位父ID
     */
    private String positionParentId;

    /**
     * 职位父ID
     */
    private String positionParentName;

    /**
     * 职位名称
     */
    private String positionName;

    /**
     * 职位描述
     */
    private String positionDes;

    /**
     * 岗位ID
     */
    private String postId;

    /**
     * 创建者
     */
    protected String creator;

    /**
     * 创建日期
     */
    protected Date createDate;

    /**
     * 更新者
     */
    protected String modifier;

    /**
     * 更新日期
     */
    protected Date modifyDate;

    /**
     * 删除标记 0:正常，1-删除
     */
    protected Integer delFlag = CommonConstant.DEL_FLAG_NORMAL;

    /**
     * 系统编号
     */
    protected String applicationCode;

    /**
     * 租户编号
     */
    protected String tenantCode;

    /**
     * 岗位名称
     */
    private String postName;

    /**
     * ID数组
     */
    protected String[] ids;

    /**
     * ID字符串，多个用逗号隔开
     */
    protected String idString;
}
