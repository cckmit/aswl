package com.aswl.as.ibrs.mapper;

import com.aswl.as.common.core.persistence.CrudMapper;
import com.aswl.as.ibrs.api.module.Device;
import com.aswl.as.ibrs.api.vo.AlarmStatusVo;
import com.aswl.as.ibrs.api.vo.DeviceModelMetadataVo;
import com.aswl.as.ibrs.api.vo.DeviceVo;
import com.aswl.as.ibrs.api.vo.EventUcStatusOperationVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
*
* 设备详情Mapper
* @author liuliepan
* @date 2019-09-27 14:17
*/

@Mapper
public interface DeviceDetailsMapper extends CrudMapper<Device> {

    /**
     * 根据id查询设备
     * @param id
     * @return DeviceVo
     */
    DeviceVo findById(@Param("id") String id);

    /**
     * 查询上级设备
     * @param parentId 父级ID
     * @return DeviceVo
     */
    DeviceVo getSuperiorDevice(@Param("parentId") String parentId);

    /**
     * 查询下级设备
     * @param id 设备id
     * @return DeviceVo
     */
    List<DeviceVo> getSubordinateDevice(@Param("id") String id);


    /**
     * 根据设备ID获取设备状态信息
     * @param id
     * @return
     */
    List<DeviceModelMetadataVo> getDeviceStautsData(@Param("id") String id);

    /**
     *  动态获取表
     * @param fld
     * @param deviceId
     * @return
     */
    Map<String,Object> getDynamicTable(@Param("fld") String fld, @Param("tables") String tables, @Param("deviceId") String deviceId);


    Map<String,Object> getData(@Param("fld") String fld, @Param("tables") String tables);

    /**
     * 获取设备型号可操作信息
     * @return
     */
    List<EventUcStatusOperationVo> getEventUcStatusOperation(@Param("deviceModelId") String deviceModelId);


    Map getOnlineStatusByDeviceId(@Param("id") String id);
}
