package com.aswl.as.ibrs.mapper;
import com.aswl.as.ibrs.api.module.QrcodeBatch;
import com.aswl.as.ibrs.api.vo.QrCodeDeviceVo;
import org.apache.ibatis.annotations.Mapper;
import com.aswl.as.common.core.persistence.CrudMapper;
import org.apache.ibatis.annotations.Param;

/**
*
* 二维码批次表Mapper
* @author df
* @date 2020/12/17 13:58
*/

@Mapper
public interface QrcodeBatchMapper extends CrudMapper<QrcodeBatch> {

    /**
     * 微信扫码获取设备信息
     * @param qrcode
     * @return QrCodeDeviceVo
     */
    QrCodeDeviceVo queryQrCodeBath(@Param("qrcode") String qrcode);

}
