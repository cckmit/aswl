package com.aswl.as.ibrs.api.dto;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 资产信息明细Dto
 *
 * @author df
 * @date 2022/03/11 13:38
 */

@ApiModel(value = "AssetsInfoDetailDto", description = "资产信息明细Dto")
@Data
public class AssetsInfoDetailDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @ApiModelProperty(value = "主键ID", name = "id")
    private String id;
    /**
     * 资产信息ID
     */
    @ApiModelProperty(value = "资产信息ID", name = "assetsInfoId")
    private String assetsInfoId;
    /**
     * 资产名称
     */
    @ApiModelProperty(value = "资产名称", name = "name")
    private String name;
    /**
     * 资产编号
     */
    @ApiModelProperty(value = "资产编号", name = "basicNo")
    private String basicNo;
    /**
     * 资产状态（0：空闲；1：使用中；2：维护；3：报废；4：外借；5：故障）
     */
    @ApiModelProperty(value = "资产状态（0：空闲；1：使用中；2：维护；3：报废；4：外借；5：故障）", name = "status")
    private Integer status;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注", name = "remark")
    private String remark;
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
