package com.aswl.as.ibrs.mapper;
import com.aswl.as.common.core.persistence.CrudMapper;
import com.aswl.as.ibrs.api.module.EventNetwork;
import com.aswl.as.ibrs.api.vo.AsEventNetworkVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 *
 * 摄像头Mapper
 * @author liuliepan
 * @date 2019-09-276 14:01
 */
@Mapper
public interface AsEventNetworkMapper extends CrudMapper<EventNetwork> {

    /**
     * 根据区域编码查询离线设备数
     * @param regionCode 区域编码
     * @return int 数量
     */
   int findOffDevice(@Param("regionCode") String regionCode,@Param("tenantCode") String tenantCode,@Param("projectId") String projectId);

    /**
     * 根据区域编码查询在线设备数
     * @param regionCode 区域编码
     * @return int 数量
     */
   int findOnlineDevice(@Param("regionCode") String regionCode,@Param("tenantCode") String tenantCode,@Param("projectId") String projectId);

    /**
     *  根据区域编码查询报障箱总数
     * @param regionCode 区域编码
     * @param kind 设备种类（1：光纤收发器，2：传输箱，3：摄像机）
     * @return int 数量
     */
   int findBoxCounts(@Param("regionCode") String regionCode,@Param("kind") String kind,@Param("tenantCode") String tenantCode,@Param("projectId") String projectId);

    /**
     * 根据区域编码查询在线报障箱数量
     * @param regionCode 区域编码
     * @param kind 设备种类（1：光纤收发器，2：传输箱，3：摄像机）
     * @return 数量
     */
    int findOnLineBoxCounts(@Param("regionCode") String regionCode,@Param("kind") String kind,@Param("tenantCode") String tenantCode,@Param("projectId") String projectId);

    /**
     * 离线的智能箱设备数
     * @param kind
     * @param regionCode
     * @param tenantCode
     * @param projectId
     * @return
     */
    int findOffDeviceBox(@Param("kind") String kind, @Param("regionCode") String regionCode,
                         @Param("tenantCode") String tenantCode, @Param("projectId") String projectId);

    /**
     * 在线的智能箱设备数
     * @param kind
     * @param regionCode
     * @param tenantCode
     * @param projectId
     * @return
     */
    int findOnlineDeviceBox(@Param("kind") String kind, @Param("regionCode") String regionCode,
                            @Param("tenantCode") String tenantCode, @Param("projectId") String projectId,@Param("debug") String debug);

}