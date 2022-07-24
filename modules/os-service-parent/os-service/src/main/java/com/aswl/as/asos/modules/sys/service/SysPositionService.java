package com.aswl.as.asos.modules.sys.service;

import com.aswl.as.asos.modules.sys.entity.SysPositionEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aswl.as.asos.common.utils.PageUtils;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 职位表 服务类
 * </p>
 *
 * @author hfx
 * @since 2019-12-17
 */
public interface SysPositionService extends IService<SysPositionEntity> {

    public PageUtils queryPage(Map<String, Object> params);

    public boolean saveEntity(SysPositionEntity entity);

    public boolean updateEntityById(SysPositionEntity entity);

    public boolean deleteEntityById(String id);

    public boolean deleteEntityByIds(String[] ids);

    public SysPositionEntity getEntityById(String id);

    public List<SysPositionEntity> findList(SysPositionEntity entity);

}
