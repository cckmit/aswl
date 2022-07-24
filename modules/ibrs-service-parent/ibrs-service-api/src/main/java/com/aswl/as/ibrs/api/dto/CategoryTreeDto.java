package com.aswl.as.ibrs.api.dto;
import java.io.Serializable;
import java.util.Date;

import com.aswl.as.common.core.persistence.TreeEntity;
import com.aswl.as.ibrs.api.module.CategoryTree;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
*
* 地址库表Dto
* @author hfx
* @date 2020-03-06 09:16
*/

@ApiModel(value = "CategoryTreeDto",description = "地址库表Dto")
@Data
public class CategoryTreeDto extends TreeEntity<CategoryTreeDto> {

private static final long serialVersionUID = 1L;

    /**
    * 主键
    */
    @ApiModelProperty(value = "主键",name="id")
    private String id;
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
    /**
    * 创建人
    */
    @ApiModelProperty(value = "创建人",name="creator")
    private String creator;
    /**
    * 创建时间
    */
    @ApiModelProperty(value = "创建时间",name="createDate")
    private Date createDate;
    /**
    * 删除标记 0:正常;1:删除
    */
    @ApiModelProperty(value = "删除标记 0:正常;1:删除",name="delFlag")
    private Integer delFlag;
    /**
    * 修改者
    */
    @ApiModelProperty(value = "修改者",name="modifier")
    private String modifier;
    /**
    * 最后修改时间
    */
    @ApiModelProperty(value = "最后修改时间",name="modifyDate")
    private Date modifyDate;

    public CategoryTreeDto(CategoryTree t)
    {
        this.id=t.getId();
        this.category=t.getCategory();
        this.nodeName=t.getNodeName();
        this.parentId=t.getParentId();
        this.nodeCode=t.getNodeCode();
        this.sort=t.getSort();

        this.creator=t.getCreator();
        this.createDate=t.getCreateDate();
        this.delFlag=t.getDelFlag();
        this.modifier=t.getModifier();
        this.modifyDate=t.getModifyDate();
    }

}
