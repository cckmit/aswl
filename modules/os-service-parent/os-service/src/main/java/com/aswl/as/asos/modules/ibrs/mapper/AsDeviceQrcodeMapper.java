package com.aswl.as.asos.modules.ibrs.mapper;

import com.aswl.as.asos.modules.ibrs.entity.AsDeviceQrcode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 设备二维码 Mapper 接口
 * </p>
 *
 * @author df
 * @since 2020-12-16
 */
@Mapper
public interface AsDeviceQrcodeMapper extends BaseMapper<AsDeviceQrcode> {

    int deleteByBathId(@Param("bathId") String bathId);
}
