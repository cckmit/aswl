package com.aswl.as.user.api.dto;

import com.aswl.as.common.core.persistence.TreeEntity;
import com.aswl.as.user.api.module.Position;
import lombok.Data;

/**
 * @version 1.0.0
 * @Author ke
 * @create 2019/9/18 14:34
 */
@Data
public class PositionDto extends TreeEntity<PositionDto> {

    private String id;
    /**
     * 职位父ID
     */
    private String parentId;

    /**
     * 职位名称
     */
    private String positionName;

    /**
     * 职位描述
     */
    private String positionDes;

    /**
     * 岗位ID
     */
    private String postId;

    /**
     * 岗位名称
     */
    private String postName;

    public PositionDto(Position position) {
        this.id = position.getId();
        this.parentId = position.getPositionParentId();
        this.positionName = position.getPositionName();
        this.positionDes = position.getPositionDes();
        this.postId = position.getPostId();
        this.sort = Integer.parseInt(position.getSort());
    }
}
