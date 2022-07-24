package com.aswl.as.ibrs.api.dto;
import java.io.Serializable;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
*
* 城市Dto
* @author hzj
* @date 2021/01/13 14:51
*/

@ApiModel(value = "CityDto",description = "城市Dto")
@Data
public class CityDto implements Serializable {

private static final long serialVersionUID = 1L;

    /**
    * ID主键
    */
    @ApiModelProperty(value = "ID主键",name="id")
    private String id;
    /**
    * 名称
    */
    @ApiModelProperty(value = "名称",name="name")
    private String name;
    /**
    * 简称
    */
    @ApiModelProperty(value = "简称",name="shortName")
    private String shortName;
    /**
    * 拼音
    */
    @ApiModelProperty(value = "拼音",name="pinyin")
    private String pinyin;
    /**
    * 简拼
    */
    @ApiModelProperty(value = "简拼",name="jianpin")
    private String jianpin;
    /**
    * 编码
    */
    @ApiModelProperty(value = "编码",name="code")
    private String code;
    /**
    * 父级ID
    */
    @ApiModelProperty(value = "父级ID",name="parentId")
    private String parentId;

    /**
     * 行政级别
     */

    @ApiModelProperty(value = "行政级别",name="level")
    private String level;

    /**
     * 经度
     */

    @ApiModelProperty(value = "行政级别",name="level")
    private double lng;

    /**
     * 维度
     */

    @ApiModelProperty(value = "维度",name="lat")
    private double lat;

    /**
    * 系统编码
    */
    @ApiModelProperty(value = "系统编码",name="applicationCode")
    private String applicationCode;
    /**
    * 租户编码
    */
    @ApiModelProperty(value = "租户编码",name="tenantCode")
    private String tenantCode;
}
