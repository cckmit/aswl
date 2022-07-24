package com.aswl.as.ibrs.service;

import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.ibrs.api.dto.DeviceModelDto;
import com.aswl.as.ibrs.api.dto.ProjectModelDto;
import com.aswl.as.ibrs.api.module.DeviceModel;
import com.aswl.as.ibrs.api.vo.DeviceModelVo;
import com.aswl.as.ibrs.api.vo.ProjectModelVo;
import com.aswl.as.ibrs.mapper.DeviceModelMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@AllArgsConstructor
@Slf4j
@Service
public class DeviceModelService extends CrudService<DeviceModelMapper, DeviceModel> {
    private final DeviceModelMapper deviceModelMapper;


    /**
     * 分页查询列表
     * @param page
     * @param deviceModelDto
     * @return DeviceModelVo
     */
    public PageInfo<DeviceModelVo> findPage(PageInfo<DeviceModelDto> page, DeviceModelDto deviceModelDto) {
        PageHelper.startPage(page.getPageNum(), page.getPageSize());
        return new PageInfo<>(deviceModelMapper.findPageInfo(deviceModelDto));
    }

    /**
     *  根据设备种类查询设备型号
     * @param deviceModelDto
     * @return list
     */
    public List<DeviceModelVo> findDeviceModelsByKind (DeviceModelDto deviceModelDto){
        return deviceModelMapper.findDeviceModelsByKind(deviceModelDto);
    }

    /**
     * 新增设备型号
     *
     * @param deviceModel
     * @return int
     */
    @Transactional
    @Override
    public int insert(DeviceModel deviceModel) {
        return super.insert(deviceModel);
    }

    /**
     * 删除设备型号
     *
     * @param deviceModel
     * @return int
     */
    @Transactional
    @Override
    public int delete(DeviceModel deviceModel) {
        return super.delete(deviceModel);
    }


    /**
     * 根据设备型号查询设备型号是否存在
     *
     * @param deviceModelName
     * @return
     */
    public DeviceModel findByDeviceModelName(String deviceModelName) {
        return deviceModelMapper.findByDeviceModelName(deviceModelName);
    }

    /**
     * 返回当前用户下的设备型号
     * @param userName
     * @return
     */
    public List<String> getDeviceModelByUser(String userName){
        return deviceModelMapper.getDeviceModelByUser(userName);
    }

    /**
     * 根据设备查询设备型号
     * @param id
     * @return
     */
    public DeviceModel getByDeviceId(String id) {
        return deviceModelMapper.getByDeviceId(id);
    }
}