package com.aswl.as.ibrs.api.module;
import java.util.Date;
import com.aswl.as.common.core.persistence.BaseEntity;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
*
* 用户点赞表Entity
* @author hfx
* @date 2020-03-16 10:26
*/

@ApiModel(value = "ContentLike",description = "用户点赞表Entity")
@Data
public class ContentLike extends BaseEntity<ContentLike> {

     /**
    * 类别，1为产品中心的分类，2-常见故障的分类，3-行业资讯的分类（实际上这个一般没有什么作用，因为（内容的唯一标识）是snowflakeId是不同的 ，不过如果到时候连表，可以根据该字段来判断是哪个表）
    */
    @ApiModelProperty(value = "类别，1为产品中心的分类，2-常见故障的分类，3-行业资讯的分类，4-banner如果链接是其它的链接的情况",name="category")
    private Integer category;

    /**
    * 内容的唯一标识，有产品中心，常见故障，行业咨询的表的id，上面的category区分
    */
    @ApiModelProperty(value = "内容的唯一标识，有产品中心，常见故障，行业咨询的表的id，下面的category区分，如果category为4，就是banner表的id",name="contentId")
    private String contentId;

    /**
    * 创建者（如果取消点赞，直接删除就是，不留del_flag） BaseEntity 里面已经有了creator 和 createDate
    */
    //private String creator;
    //protected Date createDate;

}
