package com.aswl.as.ibrs.mapper;

import com.aswl.as.ibrs.api.module.DeviceDiscover;
import com.aswl.as.ibrs.api.vo.DeviceDiscoverVo;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface DeviceDiscoverMapper {
    /**
     * 查询所有设备发现的设备
     * @return
     */
    @Select("select * from as_device_discover")
    List<DeviceDiscover> findAll();

    /**
     * 主键查询
     * @param id
     * @return
     */
    @Select("select * from as_device_discover where id = #{id}")
    DeviceDiscover findById(@Param("id") String id);

    /**
     * 根据Id删除
     * @param id
     */
    @Delete("delete from as_device_discover where id = #{id}")
    int deleteById(String id);

    /**
     * 分页排序查询
     */
    List<DeviceDiscoverVo> findPage(DeviceDiscover deviceDiscover);
}
