package com.aswl.as.asos.modules.ibrs.service;

import com.aswl.as.asos.datasource.annotation.DataSource;
import com.aswl.as.asos.modules.ibrs.entity.AsContentProduct;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aswl.as.asos.common.utils.PageUtils;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 产品中心表 服务类
 * </p>
 *
 * @author hfx
 * @since 2020-03-04
 */
public interface IAsContentProductService extends IService<AsContentProduct> {

    public PageUtils queryPage(Map<String, Object> params);

    public boolean saveEntity(AsContentProduct entity);

    public boolean updateEntityById(AsContentProduct entity);

    public boolean deleteEntityById(String id);

    public boolean deleteEntityByIds(String[] ids);

    public AsContentProduct getEntityById(String id);

    public List<AsContentProduct> findList(AsContentProduct entity);

    public boolean moveUp(AsContentProduct entity);

    public boolean moveDown(AsContentProduct entity);

    public boolean sticky(AsContentProduct entity);

    public boolean setShowcase(AsContentProduct entity);

    public boolean setIsRelease(AsContentProduct entity,String[] ids);

}
