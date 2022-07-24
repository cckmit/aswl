package com.aswl.as.asos.modules.ibrs.service;

import com.aswl.as.asos.modules.ibrs.entity.AsContentMalfunction;
import com.aswl.as.asos.modules.ibrs.entity.AsContentProduct;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aswl.as.asos.common.utils.PageUtils;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 常见故障表 服务类
 * </p>
 *
 * @author hfx
 * @since 2020-03-04
 */
public interface IAsContentMalfunctionService extends IService<AsContentMalfunction> {

    public PageUtils queryPage(Map<String, Object> params);

    public boolean saveEntity(AsContentMalfunction entity);

    public boolean updateEntityById(AsContentMalfunction entity);

    public boolean deleteEntityById(String id);

    public boolean deleteEntityByIds(String[] ids);

    public AsContentMalfunction getEntityById(String id);

    public List<AsContentMalfunction> findList(AsContentMalfunction entity);

    public boolean moveUp(AsContentMalfunction entity);

    public boolean moveDown(AsContentMalfunction entity);

    public boolean sticky(AsContentMalfunction entity);

    public boolean setShowcase(AsContentMalfunction entity);

    public boolean setIsRelease(AsContentMalfunction entity,String[] ids);

}
