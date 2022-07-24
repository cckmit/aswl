package com.aswl.as.common.core.vo;

import com.aswl.as.common.core.persistence.BaseEntity;
import lombok.Data;

/**
 * 角色
 *
 * @author aswl.com
 * @date 2018-08-25 13:58
 */
@Data
public class RoleVo extends BaseEntity<RoleVo> {

    private String roleName;

    private String roleCode;

    private String roleDesc;

}
