package com.aswl.as.asos.modules.asuser.service;

import com.aswl.as.asos.modules.asuser.entity.AsUserSysUserRole;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aswl.as.asos.common.utils.PageUtils;
import java.util.Map;

/**
 * <p>
 * 用户角色表 服务类
 * </p>
 *
 * @author hfx
 * @since 2019-11-22
 */
public interface IAsUserSysUserRoleService extends IService<AsUserSysUserRole> {

    public PageUtils queryPage(Map<String, Object> params);

    public boolean saveEntity(AsUserSysUserRole entity);

    public boolean updateEntityById(AsUserSysUserRole entity);

    public boolean deleteEntityById(String id);

    public boolean deleteEntityByIds(String[] ids);

}
