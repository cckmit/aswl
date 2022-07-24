package com.aswl.as.ibrs.service;

import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.ibrs.api.module.EventUcStatusGroupModel;
import com.aswl.as.ibrs.mapper.EventUcStatusGroupModelMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Slf4j
@Service
public class EventUcStatusGroupModelService extends CrudService<EventUcStatusGroupModelMapper, EventUcStatusGroupModel> {
    private final EventUcStatusGroupModelMapper eventUcStatusGroupModelMapper;

    /**
     * 新增事件状态组-设备型号
     *
     * @param eventUcStatusGroupModel
     * @return int
     */
    @Transactional
    @Override
    public int insert(EventUcStatusGroupModel eventUcStatusGroupModel) {
        return super.insert(eventUcStatusGroupModel);
    }

    /**
     * 删除事件状态组-设备型号
     *
     * @param eventUcStatusGroupModel
     * @return int
     */
    @Transactional
    @Override
    public int delete(EventUcStatusGroupModel eventUcStatusGroupModel) {
        return super.delete(eventUcStatusGroupModel);
    }



    /**
     * 根据状态组名查个数
     * @param groupName
     * @param deviceModelId
     * @return
     */
    public Integer findPortNum(String groupName,String deviceModelId) {

        return eventUcStatusGroupModelMapper.findPortNum(groupName,deviceModelId);
    }

    /**
     * 根据设备型号ID删除
     * @param deviceModelId
     * @return int
     */
   public  int deleteByDeviceModelId(String deviceModelId){
        
        return eventUcStatusGroupModelMapper.deleteByDeviceModelId(deviceModelId);
    }
}