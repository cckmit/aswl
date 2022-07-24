package com.aswl.as.asos.modules.ibrs.service.impl;

import com.aswl.as.asos.common.utils.IbrsData;
import com.aswl.as.asos.common.utils.IbrsResponseBean;
import com.aswl.as.asos.common.utils.OsHttpUtil;
import com.aswl.as.asos.modules.ibrs.service.IOsIndexService;
import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.ibrs.api.feign.RegionServiceClient;
import com.aswl.as.ibrs.api.vo.DeviceVo;
import com.aswl.as.common.core.vo.OsVo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class OsIndexServiceImpl implements IOsIndexService {

    private RegionServiceClient regionServiceClient;


    /**
     * 首页设备故障趋势统计
     * @param queryType
     * @param deviceKind
     * @param deviceId
     * @return
     */
    public ResponseBean<Object> osIndex1(String queryType, int deviceKind, String deviceId)
    {
        return regionServiceClient.osIndex1(queryType,deviceKind,deviceId,OsVo.getRandomStr());
    }

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
    public ResponseBean<List<DeviceVo>> osIndex2(double longMin,double longMax,
                                                               double latMin, double latMax,
                                                               String devType, String keyWords)
    {
        return regionServiceClient.osIndex2(longMin,longMax,latMin,latMax,devType,keyWords,OsVo.getRandomStr());
    }

    /**
     * 首页告警分级统计（专门提供给给运营端调用）
     *
     * @return ResponseBean
     */
    public ResponseBean<Map> osIndex3()
    {
        return regionServiceClient.osIndex3(OsVo.getRandomStr());
    }

    /**
     * 首页告警分类统计（专门提供给给运营端调用）
     *
     * @return ResponseBean
     */
    public ResponseBean<List<Map>> osIndex4()
    {
        return regionServiceClient.osIndex4(OsVo.getRandomStr());
    }

    /**
     * 首页总体健康率统计（专门提供给给运营端调用）
     *
     * @return ResponseBean
     */
    public ResponseBean<Integer> osIndex5()
    {
        return regionServiceClient.osIndex5(OsVo.getRandomStr());
    }

    /**
     * 地图上设备数统计（专门提供给给运营端调用）
     *
     * @return
     */
    public ResponseBean<Map> osIndex6()
    {
        return regionServiceClient.osIndex6(OsVo.getRandomStr());
    }

    /**
     * 首页报障箱和摄像头统计(2报障箱 3摄像机)（专门提供给给运营端调用）
     *
     * @return
     */
    public ResponseBean<Map> osIndex7(String type)
    {
        return regionServiceClient.osIndex7(type,OsVo.getRandomStr());
    }

    // 多写一个HTTP版的备用(get方法) post方法参考device的testByHttp
    public IbrsResponseBean osGetOnLineDeviceDataByHttp(String type)
    {
        Map<String,Object> m= IbrsData.getBodyMapWithRandomStrMap();
        m.put("type",type);
//        IbrsData.setBodyMapRandomStr(m);
        try
        {
            return IbrsData.getBean(OsHttpUtil.getInstance().get(IbrsData.buildGetUrl("/v1/index/os/queryOnLine",m), IbrsData.getHeaderMap()));
        }
        catch (Exception e)
        {
            return new IbrsResponseBean(e);
        }
    }

}
