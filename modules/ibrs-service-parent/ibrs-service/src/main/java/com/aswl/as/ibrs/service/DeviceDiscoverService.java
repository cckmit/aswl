package com.aswl.as.ibrs.service;

import com.aswl.as.ibrs.api.module.DeviceDiscover;
import com.aswl.as.ibrs.api.vo.DeviceDiscoverVo;
import com.aswl.as.ibrs.mapper.DeviceDiscoverMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class DeviceDiscoverService {
    private final DeviceDiscoverMapper deviceDiscoverMapper;

    /**
     * 查询所有设备发现的设备
     * @return
     */
    public List<DeviceDiscover> findAll(){
        return deviceDiscoverMapper.findAll();
    }
    /**
     * 主键查询
     * @Param id
     * @return
     */
    public DeviceDiscover findById(String id){
        return deviceDiscoverMapper.findById(id);
    }

    /**
     * 根据Id删除
     */
    @Transactional
    public int deleteById(String id){
        return deviceDiscoverMapper.deleteById(id);
    }

    /**
     * 分页查询设备发现
     * @param pageNum 页码
     * @param pageSize 页大小
     * @return
     */
    public PageInfo<DeviceDiscoverVo> findPage(String pageNum, String pageSize, DeviceDiscover deviceDiscover) {
        //Page<DeviceDiscover> page = PageHelper.startPage(Integer.parseInt(pageNum), Integer.parseInt(pageSize)).doSelectPage(() -> deviceDiscoverMapper.findPage(deviceDiscover, order));
        //return new PageInfo<>(page.getResult());
        PageHelper.startPage(Integer.parseInt(pageNum),Integer.parseInt(pageSize));
        return new PageInfo<>(deviceDiscoverMapper.findPage(deviceDiscover));
    }
}
