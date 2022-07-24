package com.aswl.as.ibrs.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 报送人、报送人配置表VO
 *
 * @author df
 * @date 2021/07/08 18:12
 */

@ApiModel(value = "ReportSettingDto", description = "报送人、报送人配置表Dto")
@Data
public class ReportSettingVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键", name = "id")
    private String id;

    /**
     * 项目名称
     */
    @ApiModelProperty(value = "项目名称", name = "projectName")
    private String projectName;

    /**
     * 报送人
     */
    @ApiModelProperty(value = "报送人", name = "reportUser")
    private String reportUser;


    /**
     * 报送人邮箱
     */
    @ApiModelProperty(value = "报送人邮箱", name = "reportUserEmail")
    private String reportUserEmail;
    /**
     * 抄送人
     */
    @ApiModelProperty(value = "抄送人", name = "sendUser")
    private String sendUser;

    /**
     * 抄送人邮箱
     */
    @ApiModelProperty(value = "抄送人邮箱", name = "sendUserEmail")
    private String sendUserEmail;
    /**
     * 项目ID
     */
    @ApiModelProperty(value = "项目ID", name = "projectId")
    private String projectId;
    /**
     * 区域编码
     */
    @ApiModelProperty(value = "区域编码", name = "regionCode")
    private String regionCode;

    /**
     * 打印排版类型
     */
    @ApiModelProperty(value = "打印排版类型", name = "composeType")
    private String composeType;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间", name = "createDate")
    private String createDate;
    /**
     * 系统编码
     */
    @ApiModelProperty(value = "系统编码", name = "applicationCode")
    private String applicationCode;
    /**
     * 租户编码
     */
    @ApiModelProperty(value = "租户编码", name = "tenantCode")
    private String tenantCode;
}
