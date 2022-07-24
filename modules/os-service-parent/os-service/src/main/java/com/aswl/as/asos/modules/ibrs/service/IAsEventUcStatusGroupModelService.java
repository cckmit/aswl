package com.aswl.as.asos.modules.ibrs.service;

import com.aswl.as.asos.modules.ibrs.entity.AsEventUcStatusGroupModel;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aswl.as.asos.common.utils.PageUtils;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 事件状态组-设备型号 服务类
 * </p>
 *
 * @author hfx
 * @since 2020-01-08
 */
public interface IAsEventUcStatusGroupModelService extends IService<AsEventUcStatusGroupModel> {

    public PageUtils queryPage(Map<String, Object> params);

    public boolean saveEntity(AsEventUcStatusGroupModel entity);

    public boolean updateEntityById(AsEventUcStatusGroupModel entity);

    public boolean deleteEntityById(String id);

    public boolean deleteEntityByIds(String[] ids);

    public AsEventUcStatusGroupModel getEntityById(String id);

    public List<AsEventUcStatusGroupModel> findList(AsEventUcStatusGroupModel entity);

}
