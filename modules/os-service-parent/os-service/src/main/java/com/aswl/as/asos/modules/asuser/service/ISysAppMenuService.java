package com.aswl.as.asos.modules.asuser.service;

import com.aswl.as.asos.modules.asuser.entity.SysAppMenu;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aswl.as.asos.common.utils.PageUtils;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * APP菜单表(现在实际上没那么复杂，但是后面可能会很复杂，所以直接参考sys_menu表来弄，不用的列，清空就是) 服务类
 * </p>
 *
 * @author hfx
 * @since 2020-03-25
 */
public interface ISysAppMenuService extends IService<SysAppMenu> {

    public PageUtils queryPage(Map<String, Object> params);

    public boolean saveEntity(SysAppMenu entity);

    public boolean updateEntityById(SysAppMenu entity);

    public boolean deleteEntityById(String id);

    public boolean deleteEntityByIds(String[] ids);

    public SysAppMenu getEntityById(String id);

    public List<SysAppMenu> findList(SysAppMenu entity);

    public boolean saveEntityNotIdGen(SysAppMenu entity);

}
