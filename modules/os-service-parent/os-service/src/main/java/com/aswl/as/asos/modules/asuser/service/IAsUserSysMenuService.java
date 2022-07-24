package com.aswl.as.asos.modules.asuser.service;

import com.aswl.as.asos.modules.asuser.entity.AsUserSysMenu;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aswl.as.asos.common.utils.PageUtils;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 菜单表 服务类
 * </p>
 *
 * @author hfx
 * @since 2019-12-13
 */
public interface IAsUserSysMenuService extends IService<AsUserSysMenu> {

    public PageUtils queryPage(Map<String, Object> params);

    public boolean saveEntity(AsUserSysMenu entity);

    public boolean saveEntityNotIdGen(AsUserSysMenu entity);

    public boolean updateEntityById(AsUserSysMenu entity);

    public boolean deleteEntityById(String id);

    public boolean deleteEntityByIds(String[] ids);

    public AsUserSysMenu getEntityById(String id);

    public List<AsUserSysMenu> findList(AsUserSysMenu entity);

    public int deleteByTenantCode(String tenantCode);

}
