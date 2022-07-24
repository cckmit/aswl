package com.aswl.as.metadata.mapper;

import com.aswl.as.common.core.persistence.CrudMapper;
import com.aswl.as.ibrs.api.module.Device;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
*
* 设备Mapper
* @author dingfei
* @date 2019-09-27 14:17
*/

@Mapper
public interface DeviceMapper extends CrudMapper<Device> {

    /**
     * 根据id查询设备
     * @param id
     * @return DeviceVo
     */
    @Select("SELECT a.*,b.device_type FROM as_device a LEFT JOIN as_device_model b ON a.device_model_id = b.id WHERE a.id = #{id}")
    Device findById(@Param("id") String id);

    /**
     * 根据ids字符串查询设备
     * @param ids
     * @return
     */
    @Select("select a.*, b.device_type, c.project_code from as_device a LEFT JOIN as_device_model b ON a.device_model_id = b.id left join as_project c on a.project_id = c.project_id where a.id in (${ids})")
    List<Device> getDevicesByIds(@Param("ids") String ids);


    /**
     * 根据Ip和租户编码查询局域网设备
     */
    @Select("select a.* from as_device as a left join as_project b on a.project_id = b.project_id where a.ip = #{ip} and a.tenant_code = #{tenantCode} and b.project_code = #{projectCode}")
    com.aswl.iot.dto.Device findByIpAndTenantCode(@Param("ip") String ip, @Param("tenantCode") String tenantCode,@Param("projectCode") String projectCode);

    /**
     * 设备的下级设备
     * @param id
     * @param kind
     * @return
     */
    @Select("select a.*,c.device_type from as_device a left join as_device_model b on a.device_model_id = b.id left join as_device_type c on b.device_type = c.device_type " +
            "left join as_device_kind d on c.device_kind_id = d.id left join as_event_network e on a.id = e.device_id " +
            "where a.parent_device_id = #{id} and d.type = #{kind} and e.network_state = 1")
    List<Device> findChild(@Param("id") String id, @Param("kind") String kind);

    /**
     * 上级设备
     * @param deviceId
     * @return
     */
    @Select("select a.*,c.project_code from as_device a left join as_device b on a.id = b.parent_device_id LEFT JOIN as_project c ON b.project_id =c.project_id where b.id = #{deviceId}")
    Device findParent(@Param("deviceId") String deviceId);
}
