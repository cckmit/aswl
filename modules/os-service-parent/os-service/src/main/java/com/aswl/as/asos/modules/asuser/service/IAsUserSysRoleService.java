package com.aswl.as.asos.modules.asuser.service;

import com.aswl.as.asos.modules.asuser.entity.AsUserSysRole;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aswl.as.asos.common.utils.PageUtils;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 角色表 服务类
 * </p>
 *
 * @author hfx
 * @since 2019-11-21
 */
public interface IAsUserSysRoleService extends IService<AsUserSysRole> {

    public PageUtils queryPage(Map<String, Object> params);

    public boolean saveEntity(AsUserSysRole entity);

    public boolean updateEntityById(AsUserSysRole entity);

    public boolean deleteEntityById(String id);

    public boolean deleteEntityByIds(String[] ids);

   public List<AsUserSysRole> findRoleByTenantCode(String tenantCode);

}
