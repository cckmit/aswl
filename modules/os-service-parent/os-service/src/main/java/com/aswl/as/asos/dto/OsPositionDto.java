package com.aswl.as.asos.dto;

import com.aswl.as.asos.modules.sys.entity.SysPositionEntity;
import com.aswl.as.common.core.persistence.TreeEntity;
import lombok.Data;

/**
 * @version 1.0.0
 * @Author ke
 * @create 2019/9/18 14:34
 */
@Data
public class OsPositionDto extends TreeEntity<OsPositionDto> {

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

    public OsPositionDto(SysPositionEntity position) {
        this.id = position.getPositionId();
        this.parentId = position.getPositionParentId();
        this.positionName = position.getPositionName();
        this.positionDes = position.getPositionDes();
        this.postId = position.getPostId();
        this.sort = position.getSort();
    }

}
