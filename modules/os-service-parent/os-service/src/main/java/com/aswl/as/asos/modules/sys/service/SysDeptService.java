package com.aswl.as.asos.modules.sys.service;

import com.aswl.as.asos.modules.sys.entity.SysDeptEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aswl.as.asos.common.utils.PageUtils;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 部门表 服务类
 * </p>
 *
 * @author hfx
 * @since 2019-12-09
 */
public interface SysDeptService extends IService<SysDeptEntity> {

    public PageUtils queryPage(Map<String, Object> params);

    public boolean saveEntity(SysDeptEntity entity);

    public boolean updateEntityById(SysDeptEntity entity);

    public boolean deleteEntityById(String id);

    public boolean deleteEntityByIds(String[] ids);

    public List<SysDeptEntity> findList(SysDeptEntity entity);

}
