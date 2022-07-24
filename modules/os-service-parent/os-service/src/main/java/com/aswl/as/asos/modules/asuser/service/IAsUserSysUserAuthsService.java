package com.aswl.as.asos.modules.asuser.service;

import com.aswl.as.asos.modules.asuser.entity.AsUserSysUserAuths;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aswl.as.asos.common.utils.PageUtils;
import java.util.Map;

/**
 * <p>
 * 用户授权表 服务类
 * </p>
 *
 * @author hfx
 * @since 2019-11-22
 */
public interface IAsUserSysUserAuthsService extends IService<AsUserSysUserAuths> {

    public PageUtils queryPage(Map<String, Object> params);

    public boolean saveEntity(AsUserSysUserAuths entity);

    public boolean updateEntityById(AsUserSysUserAuths entity);

    public boolean deleteEntityById(String id);

    public boolean deleteEntityByIds(String[] ids);

    public AsUserSysUserAuths getEntityForCheck(String identifier);

}
