package com.aswl.as.user.api.dto;

import com.aswl.as.common.core.persistence.TreeEntity;
import com.aswl.as.user.api.module.Dept;
import lombok.Data;

/**
 * 部门dto
 *
 * @author aswl.com
 * @date 2018-10-25 12:49
 */
@Data
public class DeptDto extends TreeEntity<DeptDto> {

    /**
     * 部门名称
     */
    private String deptName;

    /**
     * 部门描述
     */
    private String deptDesc;

    /**
     * 编码
     */

    private String deptCode;

    /**
     * 部门负责人
     */
    private String deptLeader;

    /**
     * 父部门ID
     */
    private String parentId;

    public DeptDto(Dept dept) {
        this.id = dept.getId();
        this.deptName = dept.getDeptName();
        this.deptDesc = dept.getDeptDesc();
        this.deptCode=dept.getDeptCode();
        this.deptLeader = dept.getDeptLeader();
        this.parentId = dept.getParentId();
        this.sort = Integer.parseInt(dept.getSort());
        this.creator = dept.getCreator();
        this.createDate = dept.getCreateDate();
        this.modifier = dept.getModifier();
        this.modifyDate = dept.getModifyDate();
    }
}
