package com.aswl.as.asos.modules.ibrs.service;

import com.aswl.as.asos.modules.ibrs.entity.AsAddressBase;
import com.aswl.as.common.core.model.ResponseBean;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aswl.as.asos.common.utils.PageUtils;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 点位地址库 服务类
 * </p>
 *
 * @author hfx
 * @since 2020-03-02
 */
public interface IAsAddressBaseService extends IService<AsAddressBase> {

    public PageUtils queryPage(Map<String, Object> params);

    public boolean saveEntity(AsAddressBase entity);

    public boolean updateEntityById(AsAddressBase entity);

    public boolean deleteEntityById(String id);

    public boolean deleteEntityByIds(String[] ids);

    public AsAddressBase getEntityById(String id);

    public List<AsAddressBase> findList(AsAddressBase entity);

    public List<AsAddressBase> findListForMap(Map<String, Object> params);

    public List<AsAddressBase> findListForQuery(Map<String, Object> params);

    public ResponseBean importBase(List<AsAddressBase> list);

}
