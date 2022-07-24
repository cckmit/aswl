package com.aswl.as.user.api.module;

import com.aswl.as.common.core.persistence.BaseEntity;
import lombok.Data;

/**
 * 用户角色关系
 *
 * @author aswl.com
 * @date 2018/8/26 09:29
 */
@Data
public class UserRole extends BaseEntity<UserRole> {

    private String userId;

    private String roleId;
}
