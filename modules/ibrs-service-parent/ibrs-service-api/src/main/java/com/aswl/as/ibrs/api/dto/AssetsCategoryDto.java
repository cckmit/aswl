package com.aswl.as.ibrs.api.dto;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 资产分类Dto
 *
 * @author df
 * @date 2022/01/14 15:51
 */

@ApiModel(value = "AssetsCategoryDto", description = "资产分类Dto")
@Data
public class AssetsCategoryDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @ApiModelProperty(value = "主键ID", name = "id")
    private String id;
    /**
     * 分类名称
     */
    @ApiModelProperty(value = "分类名称", name = "name")
    private String name;
    /**
     * 编码
     */
    @ApiModelProperty(value = "编码", name = "code")
    private String code;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间", name = "createDate")
    private Date createDate;
    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人", name = "creator")
    private String creator;
    /**
     * 系统编号
     */
    @ApiModelProperty(value = "系统编号", name = "applicationCode")
    private String applicationCode;
    /**
     * 租户编码
     */
    @ApiModelProperty(value = "租户编码", name = "tenantCode")
    private String tenantCode;
}
