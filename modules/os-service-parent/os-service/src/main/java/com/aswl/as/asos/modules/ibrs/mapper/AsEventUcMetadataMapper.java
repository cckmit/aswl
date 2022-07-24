package com.aswl.as.asos.modules.ibrs.mapper;

import com.aswl.as.asos.modules.ibrs.entity.AsEventUcMetadata;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 事件元数据 Mapper 接口
 * </p>
 *
 * @author hfx
 * @since 2020-01-08
 */
@Mapper
public interface AsEventUcMetadataMapper extends BaseMapper<AsEventUcMetadata> {

    List<AsEventUcMetadata> osFindEventUcMetadataByDeviceModelId(String deviceModelId);

}
