package com.aswl.as.asos.modules.ibrs.service;

import com.aswl.as.asos.modules.ibrs.entity.AsCategoryTree;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aswl.as.asos.common.utils.PageUtils;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 通用分类树表，普通的树可以直接用一个type辨别来获取 服务类
 * </p>
 *
 * @author hfx
 * @since 2020-03-03
 */
public interface IAsCategoryTreeService extends IService<AsCategoryTree> {

    public PageUtils queryPage(Map<String, Object> params);

    public boolean saveEntity(AsCategoryTree entity);

    public boolean updateEntityById(AsCategoryTree entity);

    public boolean deleteEntityById(String id);

    public boolean deleteEntityByIds(String[] ids);

    public AsCategoryTree getEntityById(String id);

    public List<AsCategoryTree> findList(AsCategoryTree entity);

}
