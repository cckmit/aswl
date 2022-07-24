package com.aswl.as.user.api.dto;

import com.aswl.as.common.core.persistence.TreeEntity;
import com.aswl.as.user.api.module.AppMenu;
import lombok.Data;

/**
 * 菜单dto
 *
 * @author aswl.com
 * @date 2018-09-13 20:39
 */
@Data
public class AppMenuDto extends TreeEntity<AppMenuDto> {

    /**
     * 父菜单ID
     */
    private String parentId;

    private String icon;

    private String name;

    private String url;

    private String redirect;

    private boolean spread = false;

    private String path;

    private String component;

    private String authority;

    private String code;

    private String type;

    private String label;

    private String[] roles;

    private String remark;

    public AppMenuDto(AppMenu menu) {
        this.id = menu.getId();
        this.parentId = menu.getParentId();
        this.icon = menu.getIcon();
        this.name = menu.getName();
        this.url = menu.getUrl();
        this.type = menu.getType();
        this.label = menu.getName();
        this.sort = Integer.parseInt(menu.getSort());
        this.component = menu.getComponent();
        this.path = menu.getPath();
        this.redirect = menu.getRedirect();
        this.remark = menu.getRemark();
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public void setCode(String code) {
        this.code = code;
    }
}
