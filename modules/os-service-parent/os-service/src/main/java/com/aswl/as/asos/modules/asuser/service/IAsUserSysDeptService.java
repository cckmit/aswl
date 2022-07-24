package com.aswl.as.asos.modules.asuser.service;

import com.aswl.as.asos.modules.asuser.entity.AsUserSysDept;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aswl.as.asos.common.utils.PageUtils;
import java.util.Map;

/**
 * <p>
 * 部门表 服务类
 * </p>
 *
 * @author hfx
 * @since 2019-11-21
 */
public interface IAsUserSysDeptService extends IService<AsUserSysDept> {

    public PageUtils queryPage(Map<String, Object> params);

    public boolean saveEntity(AsUserSysDept entity);

    public boolean updateEntityById(AsUserSysDept entity);

    public boolean deleteEntityById(String id);

    public boolean deleteEntityByIds(String[] ids);

    public void generateDeptCode(AsUserSysDept dept);

}
