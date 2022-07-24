package com.aswl.as.asos.modules.ibrs.service;

import com.aswl.as.asos.modules.ibrs.entity.AsEventUcMetadata;
import com.aswl.as.asos.modules.ibrs.entity.AsEventUcStatusGroup;
import com.aswl.as.asos.modules.ibrs.entity.AsEventUcStatusGroupModel;
import com.aswl.as.asos.modules.ibrs.entity.AsEventUcStatusOperation;
import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.ibrs.api.vo.EventUcMetadataVo;
import com.aswl.as.ibrs.api.vo.EventUcStatusOperationVo;
//import com.aswl.as.ibrs.api.vo.eventUcStatusGroupModelVo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aswl.as.asos.common.utils.PageUtils;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 事件状态组 服务类
 * </p>
 *
 * @author hfx
 * @since 2020-01-08
 */
public interface IAsEventUcStatusGroupService extends IService<AsEventUcStatusGroup> {

    public PageUtils queryPage(Map<String, Object> params);

    public boolean saveEntity(AsEventUcStatusGroup entity);

    public boolean updateEntityById(AsEventUcStatusGroup entity);

    public boolean deleteEntityById(String id);

    public boolean deleteEntityByIds(String[] ids);

    public AsEventUcStatusGroup getEntityById(String id);

    public List<AsEventUcStatusGroup> findList(AsEventUcStatusGroup entity);

    public List<AsEventUcStatusOperation> osGetselectExtendStatusGroupOperationList(String eventMetadataModelId);

    public ResponseBean<List<AsEventUcStatusOperation>> osEvent1(String eventMetadataId);

    public ResponseBean<List<AsEventUcStatusGroupModel>> osEvent2(String deviceModelId);

    public ResponseBean<List<AsEventUcMetadata>> osEvent3(String deviceModelId, String groupId);

}
