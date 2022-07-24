package com.aswl.as.ibrs.mapper;
import com.aswl.as.common.core.persistence.CrudMapper;
import com.aswl.as.ibrs.api.vo.DeviceAlarmVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Mapper;
import com.aswl.as.ibrs.api.module.AddressBase;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
*
* 地址库表Mapper
* @author hfx
* @date 2020-01-04 15:35
*/

@Mapper
public interface AddressBaseMapper extends CrudMapper<AddressBase> {

    /**
     * 获取列表数据
     *
     * @param entity entity
     * @return List
     */
    List<AddressBase> findListForApp(AddressBase entity);


    List<DeviceAlarmVo> findCamByDeviceParentId(@Param("bindDeviceId") String bindDeviceId);

    /**
     * 清除地址库的设备绑定关系
     * @param deviceIds
     * @return
     */
    @Update("update as_address_base set bind_device_id = '', is_used = 0 where bind_device_id in (${deviceIds})")
    int updateAddressBaseAfterDeleteDevices(@Param("deviceIds") String deviceIds);
}
