package com.aswl.as.user.api.module;

import com.aswl.as.common.core.persistence.BaseEntity;
import lombok.Data;

/**
 * 用户部门关系
 *
 * @author aswl.com
 * @date 2018/10/27 10:23
 */
@Data
public class UserDept extends BaseEntity<UserDept> {

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 部门ID
     */
    private String deptId;
}
