package com.aswl.as.asos.modules.ibrs.mapper;

import com.aswl.as.asos.modules.ibrs.entity.AsEventUcMetadata;
import com.aswl.as.asos.modules.ibrs.entity.AsEventUcStatusGroup;
import com.aswl.as.asos.modules.ibrs.entity.AsEventUcStatusGroupModel;
import com.aswl.as.asos.modules.ibrs.entity.AsEventUcStatusOperation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 事件状态组 Mapper 接口
 * </p>
 *
 * @author hfx
 * @since 2020-01-08
 */
@Mapper
public interface AsEventUcStatusGroupMapper extends BaseMapper<AsEventUcStatusGroup> {

    List<AsEventUcStatusOperation> osGetselectExtendStatusGroupOperationList(String eventMetadataModelId);

    List<AsEventUcStatusOperation> osGetExtendStatusGroupOperationList(String eventMetadataId);

    List<AsEventUcStatusGroupModel> osGetExtendStatusGroup(String deviceModelId);

    List<AsEventUcMetadata> osGetExtendStatusGroupAttribute(AsEventUcStatusGroupModel data);

}
