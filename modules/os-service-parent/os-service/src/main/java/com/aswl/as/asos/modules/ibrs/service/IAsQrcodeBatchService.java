package com.aswl.as.asos.modules.ibrs.service;

import com.aswl.as.asos.modules.ibrs.entity.AsQrcodeBatch;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aswl.as.asos.common.utils.PageUtils;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 二维码批次表 服务类
 * </p>
 *
 * @author df
 * @since 2020-12-01
 */
public interface IAsQrcodeBatchService extends IService<AsQrcodeBatch> {

    public PageUtils queryPage(Map<String, Object> params);

    public boolean saveEntity(AsQrcodeBatch entity);

    public boolean updateEntityById(AsQrcodeBatch entity);

    public boolean deleteEntityById(String id);

    public boolean deleteEntityByIds(String[] ids);

    public AsQrcodeBatch getEntityById(String id);

    public List<AsQrcodeBatch> findList(AsQrcodeBatch entity);

}
