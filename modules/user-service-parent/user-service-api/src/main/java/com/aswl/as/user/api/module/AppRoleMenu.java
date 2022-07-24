package com.aswl.as.user.api.module;

import com.aswl.as.common.core.persistence.BaseEntity;
import lombok.Data;

/**
 * APP角色菜单关联
 *
 * @author aswl.com
 * @date 2018/8/26 22:24
 */
@Data
public class AppRoleMenu extends BaseEntity<AppRoleMenu> {

    private String roleId;

    private String appMenuId;
}
