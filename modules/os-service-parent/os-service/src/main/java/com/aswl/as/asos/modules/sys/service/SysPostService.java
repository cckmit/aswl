package com.aswl.as.asos.modules.sys.service;

import com.aswl.as.asos.modules.sys.entity.SysPostEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aswl.as.asos.common.utils.PageUtils;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 工作岗位 服务类
 * </p>
 *
 * @author hfx
 * @since 2019-12-17
 */
public interface SysPostService extends IService<SysPostEntity> {

    public PageUtils queryPage(Map<String, Object> params);

    public boolean saveEntity(SysPostEntity entity);

    public boolean updateEntityById(SysPostEntity entity);

    public boolean deleteEntityById(String id);

    public boolean deleteEntityByIds(String[] ids);

    public SysPostEntity getEntityById(String id);

    public List<SysPostEntity> findList(SysPostEntity entity);

}
