package com.aswl.as.user.mapper;

import com.aswl.as.common.core.persistence.CrudMapper;
import com.aswl.as.user.api.module.Dept;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 菜单mapper
 *
 * @author aswl.com
 * @date 2018/8/26 22:34
 */
@Mapper
public interface DeptMapper extends CrudMapper<Dept> {
     /**
      * 根据父级部门ID查询
      * @param parentId
      * @return dept
      */
     Dept findDeptByParentId(@Param("parentId") String parentId);

     /**
      * 根据租户编码删除部门
      * @param tenantCode
      * @return int
      */
     int deleteByTenantCode(@Param("tenantCode") String tenantCode);
     
}
