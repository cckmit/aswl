package com.aswl.as.asos.dto;

import com.aswl.as.asos.modules.ibrs.entity.AsCategoryTree;
import com.aswl.as.common.core.persistence.TreeEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class OsCategoryTree extends TreeEntity<OsCategoryTree> {

    @ApiModelProperty(value = "类别，1为产品中心的分类，2-常见故障的分类，3-行业资讯的分类")
    private Integer category;

    @ApiModelProperty(value = "名称")
    private String nodeName;

    @ApiModelProperty(value = "父id")
    private String parentId;

    @ApiModelProperty(value = "节点编码")
    private String nodeCode;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    public OsCategoryTree(AsCategoryTree t)
    {
        this.id=t.getId();
        this.category=t.getCategory();
        this.nodeName=t.getNodeName();
        this.parentId=t.getParentId();
        this.nodeCode=t.getNodeCode();
        this.sort=t.getSort();
    }

}
