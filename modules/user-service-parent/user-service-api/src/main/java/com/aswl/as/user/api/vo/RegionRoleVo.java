package com.aswl.as.user.api.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author dingfei
 * @date 2019-10-15 17:15
 */
@Data
public class RegionRoleVo implements Serializable {
    /**
     * 区域ID
     */
    private String regionId;
    /**
     * 角色ID
     */
    private String roleId;
    /**
     * 区域名称
     */
    private String roleName;
}
