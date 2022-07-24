package com.aswl.as.ibrs.mapper;
import com.aswl.as.ibrs.api.dto.DeviceSettingsDto;
import org.apache.ibatis.annotations.Mapper;
import com.aswl.as.common.core.persistence.CrudMapper;
import com.aswl.as.ibrs.api.module.DeviceSettings;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
*
* 设备设置Mapper
* @author dingfei
* @date 2019-12-18 16:24
*/

@Mapper
public interface DeviceSettingsMapper extends CrudMapper<DeviceSettings> {


    /**
     * 批量新增设备设置
     * @param list 集合
     * @return int 数量
     */
    int insertBatch(List<DeviceSettings> list);


    /**
     *  根据设置类型删除数据
     * @param setType
     * @return
     */
    int deleteByMode(@Param("setType") String setType);

    /**
     * 批量删除数据
     * @param list
     * @return
     */

      int deleteBath(List list);

    /**
     *  根据设备id和设置类型查询
     * @return
     */
      DeviceSettings findDeviceSettings(@Param("deviceId") String deviceId,@Param("setType") String setType);

    /**
     * 根据设备id和查询
     * @param deviceIds
     * @return list
     */
    List<DeviceSettings> findDeviceSettingsByDeviceId(@Param("deviceIds") String deviceIds,@Param("setType") String setType);
    

    /**
     *  根据设备id和设置模式查询
     * @return
     */
    DeviceSettings findByDeviceIdAndMode(@Param("deviceId") String deviceId,@Param("mode") String mode);

}
