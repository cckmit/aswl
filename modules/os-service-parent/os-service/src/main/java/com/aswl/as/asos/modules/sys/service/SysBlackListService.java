package com.aswl.as.asos.modules.sys.service;

import com.aswl.as.asos.modules.sys.entity.SysBlackListEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aswl.as.asos.common.utils.PageUtils;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 系统IP黑名单 服务类
 * </p>
 *
 * @author hfx
 * @since 2019-12-17
 */
public interface SysBlackListService extends IService<SysBlackListEntity> {

    public PageUtils queryPage(Map<String, Object> params);

    public boolean saveEntity(SysBlackListEntity entity);

    public boolean updateEntityById(SysBlackListEntity entity);

    public boolean deleteEntityById(String id);

    public boolean deleteEntityByIds(String[] ids);

    public SysBlackListEntity getEntityById(String id);

    public List<SysBlackListEntity> findList(SysBlackListEntity entity);

    public boolean isBlackList(String ip);

}
