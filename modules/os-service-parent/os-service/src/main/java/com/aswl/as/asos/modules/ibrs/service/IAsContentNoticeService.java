package com.aswl.as.asos.modules.ibrs.service;

import com.aswl.as.asos.modules.ibrs.entity.AsContentNotice;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aswl.as.asos.common.utils.PageUtils;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 系统消息表 服务类
 * </p>
 *
 * @author hfx
 * @since 2020-03-12
 */
public interface IAsContentNoticeService extends IService<AsContentNotice> {

    public PageUtils queryPage(Map<String, Object> params);

    public boolean saveEntity(AsContentNotice entity);

    public boolean updateEntityById(AsContentNotice entity);

    public boolean deleteEntityById(String id);

    public boolean deleteEntityByIds(String[] ids);

    public AsContentNotice getEntityById(String id);

    public List<AsContentNotice> findList(AsContentNotice entity);

}
