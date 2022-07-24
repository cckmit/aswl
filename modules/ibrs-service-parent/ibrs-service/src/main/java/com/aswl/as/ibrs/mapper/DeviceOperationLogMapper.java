package com.aswl.as.ibrs.mapper;
import com.aswl.as.ibrs.api.dto.DeviceOperationLogDto;
import com.aswl.as.ibrs.api.vo.DeviceOperationLogVo;
import org.apache.ibatis.annotations.Mapper;
import com.aswl.as.common.core.persistence.CrudMapper;
import com.aswl.as.ibrs.api.module.DeviceOperationLog;

import java.util.List;

/**
 * 设备操作记录Mapper
 *
 * @author df
 * @date 2021/07/06 15:42
 */

@Mapper
public interface DeviceOperationLogMapper extends CrudMapper<DeviceOperationLog> {

    /**
     *  获取设备操作记录列表
     * @param dto
     * @return list
     */
    List<DeviceOperationLogVo> findInfoList(DeviceOperationLogDto dto);

}
