package com.aswl.as.common.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;


/**
 *  角色枚举
 */
@Getter
@AllArgsConstructor
public enum RoleEnum {
    ROLE_ADMIN("1","role_admin","超级管理员"),
    ROLE_SYS_ADMIN("2","role_sys_admin","系统管理员"),
    ROLE_SYS_WATCHER("3","role_sys_watcher","系统值班员"),
    ROLE_PROJECT_ADMIN("4","role_project_admin","项目管理员"),
    ROLE_PROJECT_WATCHER("5","role_project_watcher","项目值班员"),
    ROLE_PROJECT_REGION("6","role_project_region","区域负责人");

    /**
     * 类型
     */
    private String type;

    /**
     * 标识
     */
    private String code;

    /**
     * 描述
     */
    private String description;


    public static String getByRoleCode(String code){
        String value = null;
        for(RoleEnum e : RoleEnum.values()){
            if(code.contains(e.code)){
                value = e.getType();
                break;
            }
        }
        return value;
    }
    
    }
