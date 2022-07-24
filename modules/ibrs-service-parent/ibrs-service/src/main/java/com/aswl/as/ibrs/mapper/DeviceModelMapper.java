package com.aswl.as.ibrs.mapper;
import com.aswl.as.ibrs.api.dto.DeviceModelDto;
import com.aswl.as.ibrs.api.module.DeviceType;
import com.aswl.as.ibrs.api.vo.DeviceModelVo;
import org.apache.ibatis.annotations.Mapper;
import com.aswl.as.common.core.persistence.CrudMapper;
import com.aswl.as.ibrs.api.module.DeviceModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
*
* 设备型号Mapper
* @author dingfei
* @date 2019-09-26 15:22
*/

@Mapper
public interface DeviceModelMapper extends CrudMapper<DeviceModel> {

    /**
     * 分页查询
     * @param deviceModelDto
     * @return DeviceModelVo
     */
    List<DeviceModelVo> findPageInfo(DeviceModelDto deviceModelDto);

    /**
     * 查询所有设备型号
     * @return List
     */
    List<DeviceModel> findAll();


    /**
     *  根据设备型号查询设备型号是否存在
     * @param deviceModelName
     * @return
     */
    DeviceModel findByDeviceModelName(@Param("deviceModelName") String deviceModelName);

    /**
     * 返回当前用户下的设备型号
     * @param userName
     * @return
     */
    List<String> getDeviceModelByUser(@Param("userName") String userName);

    /**
     * 根据设备查询设备型号
     * @param id
     * @return
     */
    DeviceModel getByDeviceId(@Param("id") String id);

    /**
     * 根据设备种类查询设备型号
     * @param deviceModelDto
     * @return list
     */
    List<DeviceModelVo> findDeviceModelsByKind(DeviceModelDto deviceModelDto);
}
