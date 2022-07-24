package com.aswl.as.asos.modules.ibrs.mapper;

import com.aswl.as.asos.modules.ibrs.entity.AsDeviceType;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 设备类型 Mapper 接口
 * </p>
 *
 * @author hfx
 * @since 2019-12-02
 */
@Mapper
public interface AsDeviceTypeMapper extends BaseMapper<AsDeviceType> {

    int countAsDeviceTypes(Map<String, Object> params);

    List<AsDeviceType> queryAsDeviceTypes(Map<String, Object> params);

}
