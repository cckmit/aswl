package com.aswl.as.asos.modules.ibrs.service;

import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.ibrs.api.vo.DeviceVo;

import java.util.List;
import java.util.Map;

public interface IOsIndexService {

    /**
     * 首页设备故障趋势统计
     * @param queryType
     * @param deviceKind
     * @param deviceId
     * @return
     */
    public ResponseBean<Object> osIndex1(String queryType, int deviceKind, String deviceId);

    /**
     * 根据经纬度查询区域设备信息（专门提供给给运营端调用）
     *
     * @param longMin  经度最小值
     * @param longMax  经度最大值
     * @param latMin   维度最小值
     * @param latMax   维度最大值
     * @param devType  设备类型
     * @param keyWords 关键字
     * @return
     */
    public ResponseBean<List<DeviceVo>> osIndex2(double longMin, double longMax,
                                                               double latMin, double latMax,
                                                               String devType, String keyWords);

    /**
     * 首页告警分级统计（专门提供给给运营端调用）
     *
     * @return ResponseBean
     */
    public ResponseBean<Map> osIndex3();

    /**
     * 首页告警分类统计（专门提供给给运营端调用）
     *
     * @return ResponseBean
     */
    public ResponseBean<List<Map>> osIndex4();

    /**
     * 首页总体健康率统计（专门提供给给运营端调用）
     *
     * @return ResponseBean
     */
    public ResponseBean<Integer> osIndex5();

    /**
     * 地图上设备数统计（专门提供给给运营端调用）
     *
     * @return
     */
    public ResponseBean<Map> osIndex6();

    /**
     * 首页报障箱和摄像头统计(2报障箱 3摄像机)（专门提供给给运营端调用）
     *
     * @return
     */
    public ResponseBean<Map> osIndex7(String type);

}
