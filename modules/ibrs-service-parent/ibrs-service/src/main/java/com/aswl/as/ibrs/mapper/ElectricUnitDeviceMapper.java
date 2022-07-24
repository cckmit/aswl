package com.aswl.as.ibrs.mapper;
import com.aswl.as.user.api.module.RoleRegion;
import org.apache.ibatis.annotations.Mapper;
import com.aswl.as.common.core.persistence.CrudMapper;
import com.aswl.as.ibrs.api.module.ElectricUnitDevice;

import java.util.List;

/**
*
* 用电单位设备关联表Mapper
* @author df
* @date 2021/06/01 20:56
*/

@Mapper
public interface ElectricUnitDeviceMapper extends CrudMapper<ElectricUnitDevice> {

    /**
     * 根据用电单位ID删除
     *
     * @param unitId
      * @return int
     */
    int deleteByUnitId(String unitId);


    /**
     * 根据设备ID删除
     *
     * @param deviceId
     * @return int
     */
    int deleteByDeviceId(String deviceId);
    
    
    


    /**
     * 批量保存
     *
     * @param electricUnitDevices
     * @return int
     */
    int insertBatch(List<ElectricUnitDevice> electricUnitDevices);
}
