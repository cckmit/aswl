package com.aswl.as.asos.modules.ibrs.mapper;

import com.aswl.as.asos.modules.ibrs.entity.AsDeviceModel;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 设备型号 Mapper 接口
 * </p>
 *
 * @author hfx
 * @since 2019-12-02
 */
@Mapper
public interface AsDeviceModelMapper extends BaseMapper<AsDeviceModel> {

    int countDeviceModels(Map<String, Object> params);

    List<AsDeviceModel> queryDeviceModels(Map<String, Object> params);

}
