package com.aswl.as.ibrs.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.aswl.as.common.core.persistence.CrudMapper;
import com.aswl.as.ibrs.api.module.EventUcStatusGroupModel;
import org.apache.ibatis.annotations.Param;

/**
 * 事件状态组-设备型号Mapper
 *
 * @author dingfei
 * @date 2019-12-02 10:22
 */

@Mapper
public interface EventUcStatusGroupModelMapper extends CrudMapper<EventUcStatusGroupModel> {


    /**
     * 根据状态组名查个数
     * @param groupName
     * @param deviceModelId
     * @return
     */
    Integer findPortNum(@Param("groupName") String groupName, @Param("deviceModelId") String deviceModelId);


    /**
     * 根据设备型号ID删除
     * @param deviceModelId
     * @return int
     */
    int deleteByDeviceModelId(@Param("deviceModelId") String deviceModelId);
}
