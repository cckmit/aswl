package com.aswl.as.ibrs.mapper;

import com.aswl.as.common.core.persistence.CrudMapper;
import com.aswl.as.ibrs.api.dto.DeviceQrcodeDto;
import com.aswl.as.ibrs.api.vo.DeviceQrcodeVo;
import org.apache.ibatis.annotations.Mapper;
import com.aswl.as.ibrs.api.module.DeviceQrcode;
import org.apache.ibatis.annotations.Param;

/**
 * 地址库表Mapper
 *
 * @author hfx
 * @date 2020-02-26 15:44
 */

@Mapper
public interface DeviceQrcodeMapper extends CrudMapper<DeviceQrcode> {

    DeviceQrcodeVo getDeviceIdByQrCode(@Param("qrCode") String qrCode);

    int updateByQrCode(@Param("qrCode") String qrCode);

}
