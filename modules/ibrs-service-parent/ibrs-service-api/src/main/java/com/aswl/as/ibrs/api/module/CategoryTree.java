package com.aswl.as.ibrs.api.module;
import java.util.Date;
import java.util.List;

import com.aswl.as.common.core.persistence.BaseEntity;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
*
* 通用分类树表Entity
* @author hfx
* @date 2020-03-06 09:16
*/

@ApiModel(value = "CategoryTree",description = "通用分类树表Entity")
@Data
public class CategoryTree extends BaseEntity<CategoryTree> {

    /**
    * 类别，1为产品中心的分类，2-常见故障的分类，3-行业资讯的分类
    */

    @ApiModelProperty(value = "类别，1为产品中心的分类，2-常见故障的分类，3-行业资讯的分类",name="category")
    private Integer category;
    /**
    * 名称
    */

    @ApiModelProperty(value = "名称",name="nodeName")
    private String nodeName;
    /**
    * 父id
    */

    @ApiModelProperty(value = "父id",name="parentId")
    private String parentId;
    /**
    * 节点编码
    */

    @ApiModelProperty(value = "节点编码",name="nodeCode")
    private String nodeCode;
    /**
    * 排序
    */
    @ApiModelProperty(value = "排序",name="sort")
    private Integer sort;

    @ApiModelProperty(value = "数据列表,只用来传递数据",name="tempDataList")
    private List<Object> tempDataList;

}
