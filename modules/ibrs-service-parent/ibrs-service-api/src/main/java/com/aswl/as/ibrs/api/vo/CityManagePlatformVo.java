package com.aswl.as.ibrs.api.vo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;

/**
 * 智能箱管理平台数据表VO
 *
 * @author df
 * @date 2021/07/26 14:33
 */

@ApiModel(value = "CityManagePlatformDto", description = "智能箱管理平台数据表VO")
@Data
public class CityManagePlatformVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键", name = "id")
    private String id;
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
     * 1,授权,0,关闭
     */
    @ApiModelProperty(value = "1,授权,0,关闭", name = "longRangeControl")
    private Integer longRangeControl;
    /**
     * 1,授权,0,关闭
     */
    @ApiModelProperty(value = "1,授权,0,关闭", name = "dataUpload")
    private Integer dataUpload;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间", name = "createDate")
    private Date createDate;
    /**
     * 系统编号
     */
    @ApiModelProperty(value = "系统编号", name = "applicationCode")
    private String applicationCode;
    /**
     * SAAS租户编码
     */
    @ApiModelProperty(value = "SAAS租户编码", name = "tenantCode")
    private String tenantCode;

    /**
     * 项目名称
     */
    @ApiModelProperty(value = "项目名称", name = "projectName")
    private String projectName;
}
