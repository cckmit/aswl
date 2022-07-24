package com.aswl.as.ibrs.mapper;
import com.aswl.as.ibrs.api.vo.EventUcMetadataVo;
import com.aswl.as.ibrs.api.vo.EventUcStatusOperationVo;
import com.aswl.as.ibrs.api.vo.EventUcStatusGroupModelVo;
import org.apache.ibatis.annotations.Mapper;
import com.aswl.as.common.core.persistence.CrudMapper;
import com.aswl.as.ibrs.api.module.EventUcStatusGroup;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
*
* 事件状态组Mapper
* @author dingfei
* @date 2019-10-22 11:45
*/

@Mapper
public interface EventUcStatusGroupMapper extends CrudMapper<EventUcStatusGroup> {

    /**
     * 根据设备型号ID查询事件状态组数据
     * @param deviceModelId 型号ID
     * @return list
     */
    List<EventUcStatusGroupModelVo> getExtendStatusGroup(@Param("deviceModelId") String deviceModelId);

    /**
     *  根据设备型号ID和状态组ID查询组扩展属性
     * @param deviceModelId 型号ID
     * @param groupId 状态组ID
     * @return list
     */
    List<EventUcMetadataVo> getExtendStatusGroupAttribute(@Param("deviceModelId")String deviceModelId,@Param("groupId") String groupId);

    /**
     *  根据设备型号ID已选择操作列表
     * @param eventMetadataModelId 事件元数据-设备型号ID
     * @return list
     */
    List<EventUcStatusOperationVo> getSelectExtendStatusGroupOperationList(@Param("eventMetadataModelId") String eventMetadataModelId);


    /**
     *  根据元数据ID查询状态操作列表
     * @param eventMetadataId 元数据ID
     * @return list
     */
    List<EventUcStatusOperationVo> getExtendStatusGroupOperationList(@Param("eventMetadataId") String eventMetadataId);


}
