package com.aswl.as.asos.modules.asuser.service;

import com.aswl.as.asos.modules.asuser.entity.AsUserSysPost;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aswl.as.asos.common.utils.PageUtils;
import java.util.Map;

/**
 * <p>
 * 工作岗位 服务类
 * </p>
 *
 * @author hfx
 * @since 2019-11-21
 */
public interface IAsUserSysPostService extends IService<AsUserSysPost> {

    public PageUtils queryPage(Map<String, Object> params);

    public boolean saveEntity(AsUserSysPost entity);

    public boolean updateEntityById(AsUserSysPost entity);

    public boolean deleteEntityById(String id);

    public boolean deleteEntityByIds(String[] ids);

}
