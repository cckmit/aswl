package com.aswl.as.asos.modules.ibrs.service;

import com.aswl.as.asos.datasource.annotation.DataSource;
import com.aswl.as.asos.modules.ibrs.entity.AsContentBanner;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aswl.as.asos.common.utils.PageUtils;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * banner管理 服务类
 * </p>
 *
 * @author hfx
 * @since 2020-03-12
 */
public interface IAsContentBannerService extends IService<AsContentBanner> {

    public PageUtils queryPage(Map<String, Object> params);

    public boolean saveEntity(AsContentBanner entity);

    public boolean updateEntityById(AsContentBanner entity);

    public boolean deleteEntityById(String id);

    public boolean deleteEntityByIds(String[] ids);

    public AsContentBanner getEntityById(String id);

    public List<AsContentBanner> findList(AsContentBanner entity);

    public boolean moveUp(AsContentBanner entity);

    public boolean moveDown(AsContentBanner entity);

    public boolean sticky(AsContentBanner entity);

    public boolean setIsRelease(AsContentBanner entity, String[] ids);

}
