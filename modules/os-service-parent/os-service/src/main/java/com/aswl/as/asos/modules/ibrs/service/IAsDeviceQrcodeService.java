package com.aswl.as.asos.modules.ibrs.service;

import com.aswl.as.asos.modules.ibrs.entity.AsDeviceQrcode;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aswl.as.asos.common.utils.PageUtils;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 设备二维码 服务类
 * </p>
 *
 * @author df
 * @since 2020-12-16
 */
public interface IAsDeviceQrcodeService extends IService<AsDeviceQrcode> {

    public PageUtils queryPage(Map<String, Object> params);

    public boolean saveEntity(AsDeviceQrcode entity);

    public boolean updateEntityById(AsDeviceQrcode entity);

    public boolean deleteEntityById(String id);

    public boolean deleteEntityByIds(String[] ids);

    public AsDeviceQrcode getEntityById(String id);

    public List<AsDeviceQrcode> findList(AsDeviceQrcode entity);

    public boolean deleteByBathId(String bathId);

}
