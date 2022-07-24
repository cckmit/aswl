package com.aswl.as.user.api.vo;

import com.aswl.as.common.core.constant.CommonConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * @author jk
 * @version 1.0.0
 * @create 2019/11/6 14:01
 */
@ApiModel(value = "PositionVo",description = "职位Vo")
@Getter@Setter
public class PositionVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键",name="positionId")
    private String positionId;

    /**
     * 职位父ID
     */
    @ApiModelProperty(value = "职位父id",name="positionParentId")
    private String positionParentId;

    /**
     * 职位父ID
     */
    @ApiModelProperty(value = "职位父职位",name="positionParentName")
    private String positionParentName;

    /**
     * 职位名称
     */
    @ApiModelProperty(value = "职位名称",name="positionName")
    private String positionName;

    /**
     * 职位描述
     */
    @ApiModelProperty(value = "职位描述",name="positionDes")
    private String positionDes;

    /**
     * 岗位ID
     */
    @ApiModelProperty(value = "岗位id",name="postId")
    private String postId;

    /**
     * 岗位名称
     */
    @ApiModelProperty(value = "岗位名称",name="postName")
    private String postName;
    @ApiModelProperty(value = "排序",name="sort")
    private Integer sort;

}
