package com.aswl.as.ibrs.api.module;
import com.aswl.as.common.core.persistence.BaseEntity;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
*
* 城市Entity
* @author hzj
* @date 2021/01/13 14:51
*/

@ApiModel(value = "City",description = "城市Entity")
@Data
public class City extends BaseEntity<City> {
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
     * 类型 1,城市,2,项目,3, 设备
     */
    private String type;


    /**
     * 子菜单
     */
    private List<City> children;


    /**
     * 项目信息
     */
    private List<Project> projectList;

}
