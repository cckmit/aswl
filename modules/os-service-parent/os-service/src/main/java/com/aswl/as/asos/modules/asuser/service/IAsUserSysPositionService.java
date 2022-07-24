package com.aswl.as.asos.modules.asuser.service;

import com.aswl.as.asos.modules.asuser.entity.AsUserSysPosition;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aswl.as.asos.common.utils.PageUtils;
import java.util.Map;

/**
 * <p>
 * 职位表 服务类
 * </p>
 *
 * @author hfx
 * @since 2019-11-21
 */
public interface IAsUserSysPositionService extends IService<AsUserSysPosition> {

    public PageUtils queryPage(Map<String, Object> params);

    public boolean saveEntity(AsUserSysPosition entity);

    public boolean updateEntityById(AsUserSysPosition entity);

    public boolean deleteEntityById(String id);

    public boolean deleteEntityByIds(String[] ids);

}
