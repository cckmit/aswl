package com.aswl.as.ibrs.service;
import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.ibrs.api.dto.AlarmTypeDto;
import com.aswl.as.ibrs.api.dto.DeviceOperationLogDto;
import com.aswl.as.ibrs.api.module.DeviceOperationLog;
import com.aswl.as.ibrs.api.vo.AlarmTypeVo;
import com.aswl.as.ibrs.api.vo.DeviceOperationLogVo;
import com.aswl.as.ibrs.mapper.DeviceOperationLogMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Slf4j
@Service
public class DeviceOperationLogService extends CrudService<DeviceOperationLogMapper, DeviceOperationLog> {
    private final DeviceOperationLogMapper deviceOperationLogMapper;


    public PageInfo<DeviceOperationLogVo> findPage(PageInfo<DeviceOperationLogDto> page, DeviceOperationLogDto dto) {
        PageHelper.startPage(page.getPageNum(), page.getPageSize());
        return new PageInfo<>(deviceOperationLogMapper.findInfoList(dto));
    }

    /**
     * 新增设备操作记录
     *
     * @param deviceOperationLog
     * @return int
     */
    @Transactional
    @Override
    public int insert(DeviceOperationLog deviceOperationLog) {
        return deviceOperationLogMapper.insert(deviceOperationLog);
    }

    /**
     * 删除设备操作记录
     *
     * @param deviceOperationLog
     * @return int
     */
    @Transactional
    @Override
    public int delete(DeviceOperationLog deviceOperationLog) {
        return deviceOperationLogMapper.delete(deviceOperationLog);
    }
}