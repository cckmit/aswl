package com.aswl.as.user.api.module;

import com.aswl.as.common.core.persistence.BaseEntity;
import lombok.Data;

/**
 * 角色菜单关联
 *
 * @author aswl.com
 * @date 2018/8/26 22:24
 */
@Data
public class RoleMenu extends BaseEntity<RoleMenu> {

    private String roleId;

    private String menuId;
}
