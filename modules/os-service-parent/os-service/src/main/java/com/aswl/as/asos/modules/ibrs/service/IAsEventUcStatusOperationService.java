package com.aswl.as.asos.modules.ibrs.service;

import com.aswl.as.asos.modules.ibrs.entity.AsEventUcStatusOperation;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aswl.as.asos.common.utils.PageUtils;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 事件状态操作 服务类
 * </p>
 *
 * @author hfx
 * @since 2020-01-09
 */
public interface IAsEventUcStatusOperationService extends IService<AsEventUcStatusOperation> {

    public PageUtils queryPage(Map<String, Object> params);

    public boolean saveEntity(AsEventUcStatusOperation entity);

    public boolean updateEntityById(AsEventUcStatusOperation entity);

    public boolean deleteEntityById(String id);

    public boolean deleteEntityByIds(String[] ids);

    public AsEventUcStatusOperation getEntityById(String id);

    public List<AsEventUcStatusOperation> findList(AsEventUcStatusOperation entity);

}
