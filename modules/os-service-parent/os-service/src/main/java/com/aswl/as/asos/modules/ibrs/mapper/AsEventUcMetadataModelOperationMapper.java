package com.aswl.as.asos.modules.ibrs.mapper;

import com.aswl.as.asos.modules.ibrs.entity.AsEventUcMetadataModelOperation;
import com.aswl.as.ibrs.api.module.EventUcMetadataModelOperation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 设备型号事件元数据-状态操作 Mapper 接口
 * </p>
 *
 * @author hfx
 * @since 2020-01-09
 */
@Mapper
public interface AsEventUcMetadataModelOperationMapper extends BaseMapper<AsEventUcMetadataModelOperation> {

    public int osDeleteByEventMetadataModelId(String eventMetadataModelId);

    public int osInsertBatch(List<AsEventUcMetadataModelOperation> list);

}
