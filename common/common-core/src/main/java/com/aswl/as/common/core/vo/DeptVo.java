package com.aswl.as.common.core.vo;

import com.aswl.as.common.core.persistence.BaseEntity;
import lombok.Data;

/**
 * 部门vo
 *
 * @author aswl.com
 * @date 2018/12/31 22:02
 */
@Data
public class DeptVo extends BaseEntity<DeptVo> {

    private String deptName;
}
