package com.aswl.as.ibrs.service;

import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.ibrs.api.module.EventUcStatusGroup;
import com.aswl.as.ibrs.api.vo.EventUcMetadataVo;
import com.aswl.as.ibrs.api.vo.EventUcStatusOperationVo;
import com.aswl.as.ibrs.api.vo.EventUcStatusGroupModelVo;
import com.aswl.as.ibrs.mapper.EventUcStatusGroupMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@AllArgsConstructor
@Slf4j
@Service
public class EventUcStatusGroupService extends CrudService<EventUcStatusGroupMapper, EventUcStatusGroup> {
    private final EventUcStatusGroupMapper eventUcStatusGroupMapper;

    /**
     * 新增事件状态组
     *
     * @param eventUcStatusGroup
     * @return int
     */
    @Transactional
    @Override
    public int insert(EventUcStatusGroup eventUcStatusGroup) {
        return super.insert(eventUcStatusGroup);
    }

    /**
     * 删除事件状态组
     *
     * @param eventUcStatusGroup
     * @return int
     */
    @Transactional
    @Override
    public int delete(EventUcStatusGroup eventUcStatusGroup) {
        return super.delete(eventUcStatusGroup);
    }


    /**
     * 根据设备型号ID查询事件状态组数据
     * @param deviceModelId 型号ID
     * @return list
     */
    public List<EventUcStatusGroupModelVo> getExtendStatusGroup(String deviceModelId){

        return eventUcStatusGroupMapper.getExtendStatusGroup(deviceModelId);
    }

    /**
     *  根据设备型号ID和状态组ID查询组扩展属性
     * @param deviceModelId 型号ID
     * @param groupId 状态组ID
     * @return list
     */
   public  List<EventUcMetadataVo> getExtendStatusGroupAttribute(String deviceModelId,String groupId){

        return eventUcStatusGroupMapper.getExtendStatusGroupAttribute(deviceModelId,groupId);
    }

    /**
     *  根据设备型号ID已选择操作列表
     * @param eventMetadataModelId 事件元数据-设备型号ID
     * @return list
     */
    public  List<EventUcStatusOperationVo> getSelectExtendStatusGroupOperationList(String eventMetadataModelId){

        return eventUcStatusGroupMapper.getSelectExtendStatusGroupOperationList(eventMetadataModelId);
    }


    /**
     *  根据元数据ID查询状态操作列表
     * @param eventMetadataId 元数据ID
     * @return list
     */
    public List<EventUcStatusOperationVo> getExtendStatusGroupOperationList(String eventMetadataId){

        return eventUcStatusGroupMapper.getExtendStatusGroupOperationList(eventMetadataId);
    }
}