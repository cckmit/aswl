package com.aswl.as.user.api.module;

import com.aswl.as.common.core.persistence.BaseEntity;
import lombok.Data;

/**
 * @version 1.0.0
 * @Author ke
 * @create 2019/9/17 13:22
 */
@Data
public class Position extends BaseEntity<Position> {

    /**
     * 职位父ID
     */
    private String positionParentId;

    /**
     * 职位名称
     */
    private String positionName;

    /**
     * 职位描述
     */
    private String positionDes;

    /**
     * 岗位级别
     */
    private String postId;

    /**
     * 排序
     */
    private String sort;
}
