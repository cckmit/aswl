package com.aswl.as.metadata.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.common.core.utils.IdGen;
import com.aswl.as.common.core.utils.SysUtil;
import com.aswl.as.ibrs.api.dto.DeviceSettingsDto;
import com.aswl.as.ibrs.api.module.DeviceSettings;
import com.aswl.as.metadata.mapper.DeviceSettingsMapper;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
@Service
public class DeviceSettingsService extends CrudService<DeviceSettingsMapper, DeviceSettings> {
    private final DeviceSettingsMapper deviceSettingsMapper;

    /**
     * 新增设备设置
     *
     * @param deviceSettings
     * @return int
     */
    @Transactional
    @Override
    public int insert(DeviceSettings deviceSettings) {
        return super.insert(deviceSettings);
    }

    /**
     * 删除设备设置
     *
     * @param deviceSettings
     * @return int
     */
    @Transactional
    @Override
    public int delete(DeviceSettings deviceSettings) {
        return super.delete(deviceSettings);
    }


    /**
     * 批量新增设备设置
     *
     * @param list 集合
     * @return int 数量
     */
    @Transactional
    public int insertBatch(List<DeviceSettingsDto> list) {
        List<DeviceSettings> lists=new ArrayList<>();
        if (list!=null && list.size()>0) {
            for (DeviceSettingsDto dto : list) {
                DeviceSettings settings = new DeviceSettings();
                BeanUtils.copyProperties(dto, settings);
                settings.setId(IdGen.snowflakeId());
                settings.setStoreTime(new Date());
                settings.setApplicationCode(SysUtil.getSysCode());
                settings.setTenantCode(SysUtil.getTenantCode());
                lists.add(settings);
            }
        }
        return deviceSettingsMapper.insertBatch(lists);
    }

    /**
     *  根据设置类型删除数据
     * @param setType
     * @return int
     */
   public  int deleteByMode(String setType){
        return deviceSettingsMapper.deleteByMode(setType);
    }


    /**
     *  根据设备ID删除数据
     * @param list
     * @return int
     */
    public  int deleteBath(List list){
        return deviceSettingsMapper.deleteBath(list);
    }

    /**
     *  根据设备id和设置类型查询
     * @return
     */
    public DeviceSettings findDeviceSettings(String deviceId,String setType){

        return deviceSettingsMapper.findDeviceSettings(deviceId,setType);
    }



}