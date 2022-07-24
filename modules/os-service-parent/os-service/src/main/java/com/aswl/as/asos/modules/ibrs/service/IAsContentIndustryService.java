package com.aswl.as.asos.modules.ibrs.service;

import com.aswl.as.asos.modules.ibrs.entity.AsContentIndustry;
import com.aswl.as.asos.modules.ibrs.entity.AsContentMalfunction;
import com.aswl.as.asos.modules.ibrs.entity.AsContentProduct;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aswl.as.asos.common.utils.PageUtils;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 行业资讯表 服务类
 * </p>
 *
 * @author hfx
 * @since 2020-03-04
 */
public interface IAsContentIndustryService extends IService<AsContentIndustry> {

    public PageUtils queryPage(Map<String, Object> params);

    public boolean saveEntity(AsContentIndustry entity);

    public boolean updateEntityById(AsContentIndustry entity);

    public boolean deleteEntityById(String id);

    public boolean deleteEntityByIds(String[] ids);

    public AsContentIndustry getEntityById(String id);

    public List<AsContentIndustry> findList(AsContentIndustry entity);

    public boolean moveUp(AsContentIndustry entity);

    public boolean moveDown(AsContentIndustry entity);

    public boolean sticky(AsContentIndustry entity);

    public boolean setShowcase(AsContentIndustry entity);

    public boolean setIsRelease(AsContentIndustry entity,String[] ids);

}
