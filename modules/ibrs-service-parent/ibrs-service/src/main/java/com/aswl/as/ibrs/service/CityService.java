package com.aswl.as.ibrs.service;
import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.common.core.utils.DateUtils;
import com.aswl.as.common.core.utils.SysUtil;
import com.aswl.as.ibrs.api.module.City;
import com.aswl.as.ibrs.api.module.Project;
import com.aswl.as.ibrs.api.vo.*;
import com.aswl.as.ibrs.mapper.CityAlarmStatisticsMapper;
import com.aswl.as.ibrs.mapper.CityMapper;
import com.aswl.as.ibrs.mapper.ProjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
@Slf4j
@Service
public class CityService extends CrudService<CityMapper, City> {
    private final CityMapper cityMapper;
    private final CityAlarmStatisticsMapper cityAlarmStatisticsMapper;
    private final ProjectMapper projectMapper;

    /**
     * 新增城市
     *
     * @param city
     * @return int
     */
    @Transactional
    @Override
    public int insert(City city) {
        return super.insert(city);
    }

    /**
     * 删除城市
     *
     * @param city
     * @return int
     */
    @Transactional
    @Override
    public int delete(City city) {
        return super.delete(city);
    }

    public City findByCode(String cityCode) {
        return cityMapper.findByCode(cityCode);
    }


    /**
     * 城市在线率完好率信息
     *
     * @param kind     设备类型 2 报障箱 3 摄像机
     * @param parentId 城市父级ID
     * @param cityCode 城市编码
     * @return list
     */
    public List<CityVo> getCityRate(String kind, String parentId, String cityCode) {
        return cityMapper.getCityRate(kind, parentId,cityCode,null,null);
    }


    /**
     * 地图在线率完好率统计
     *
     * @param kind     设备类型 2 报障箱 3 摄像机
     * @param cityCode 城市编码
     * @param projectId 项目ID
     * @return map
     */
    public Map getMapOnlineIntactStatistics(String kind, String cityCode,String projectId) {
        return cityMapper.findDevicesStatistics(kind, cityCode,null,projectId,null,null,null,null);
    }

    /**
     * 区域项目导航项目信息
     *
     * @param kind     设备类型 2 报障箱 3 摄像机
     * @param cityCode 城市编码
     * @return list
     */
    public List<ProjectVo> getProjectRate(String kind,String cityCode) {
        return cityMapper.findProjectRate(kind,cityCode);
    }


    /**
     * 总体健康率
     *
     * @param kind     设备类型 2 报障箱 3 摄像机
     * @param cityCode 城市编码
     * @param projectId 项目ID
     * @return list
     */
    public Map getHealthyRate(String kind, String cityCode,String projectId) {
        return cityMapper.getHealthyRate(kind, cityCode,projectId);
    }

    /**
     * 故障类型排名
     *
     * @param kind     设备类型 2 报障箱 3 摄像机
     * @param cityCode 城市编码
     * @param projectId 项目ID
     * @return list
     */
    public List<Map> getFaultType(String kind, String cityCode,String projectId) {
        return cityMapper.getFaultType(kind, cityCode,projectId);
    }


    /**
     * 故障设备型号排名
     *
     * @param kind     设备类型 2 报障箱 3 摄像机
     * @param cityCode 城市编码
     * @param projectId 项目ID
     * @return list
     */
    public List<Map> getFaultModel(String kind, String cityCode,String projectId) {
        return cityMapper.getFaultModel(kind, cityCode,projectId);
    }


    /**
     * 近30天在线率完好率总体变化趋势
     *
     * @param kind          设备种类 2报障箱 3摄像机
     * @param cityCode      城市CODE
     * @param projectId     项目ID
     * @param startTime     开始日期
     * @param endTime     结束日期
     * @param endTime     结束日期
     * @param timeUnit     显示单位
     * @return list
     */
    public List<Object> getDeviceOnlineIntactTrend(String kind, String cityCode, String projectId,String startTime,String endTime,String timeUnit) {
        List<Map> mapList =cityAlarmStatisticsMapper.getDeviceOnlineIntactTrend(kind,cityCode,projectId,startTime,endTime,timeUnit);
        Map<String,List<Map>> resultData = new LinkedHashMap<>();
        if("day".equals(timeUnit)) {
            //返回日期之间所有的日期
            List<String> dayList = DateUtils.getDayBetweenDates(startTime, endTime);
            for (int i = 0; i < dayList.size(); i++) {
                List<Map> putList = new ArrayList<>();
                Map map = new HashMap();
                map.put("onlineNum", 0L);
                map.put("intactNum", 0L);
                map.put("faultNum", 0L);
                map.put("deviceNum", 0L);
                putList.add(map);
                resultData.put(dayList.get(i) + "日", putList);
            }
            for (int i = 0; i < mapList.size(); i++) {
                Map map = new HashMap();
                List<Map> putList = new ArrayList<>();
                map.put("onlineNum", mapList.get(i).get("onlineNum"));
                map.put("intactNum", mapList.get(i).get("intactNum"));
                map.put("faultNum", mapList.get(i).get("faultNum"));
                map.put("deviceNum", mapList.get(i).get("deviceNum"));
                putList.add(map);
                resultData.put(mapList.get(i).get("date") + "", putList);
            }
        }
        else{
                //返回日期之间所有的月份
                List<String> monthList = DateUtils.getMonthBetween(startTime, endTime);
                for (int i = 0; i < monthList.size(); i++) {
                   List<Map> putList=new ArrayList<>();
                    Map map =new HashMap();
                    map.put("onlineNum",0L);
                    map.put("intactNum",0L);
                    map.put("faultNum", 0L);
                    map.put("deviceNum",0L);
                    putList.add(map);
                    resultData.put(monthList.get(i) + "月", putList);
                }
                for (int i = 0; i < mapList.size(); i++) {
                    List<Map> putList=new ArrayList<>();
                    Map map =new HashMap();
                    map.put("onlineNum",mapList.get(i).get("onlineNum"));
                    map.put("intactNum",mapList.get(i).get("intactNum"));
                    map.put("faultNum", mapList.get(i).get("faultNum"));
                    map.put("deviceNum",mapList.get(i).get("deviceNum"));
                    putList.add(map);
                    resultData.put(mapList.get(i).get("date") + "",putList);
                }
            }
        ArrayList<Object> result = new ArrayList<>();
        result.add(resultData);
        return result;
    }



    /**
     * 设备修复率查询
     *
     * @param kind      设备种类 2报障箱 3摄像机
     * @param cityCode  城市CODE
     * @param projectId 项目ID
     * @param startTime 开始日期
     * @param endTime   结束日期
     * @return list
     */
    public List<DeviceRepairRateVo> findRepairRate(String kind, String cityCode, String projectId, String startTime, String endTime) {
        return cityAlarmStatisticsMapper.findRepairRate(kind, cityCode, projectId, startTime, endTime);
    }

    /**
     * 生成城市项目树方法
     *
     * @return list
     */
    public List<City> findCityTree() {
        //查询城市信息
        List<City> allCity = cityMapper.findList(new City());
        //查询所有项目
        List<Project> projects = projectMapper.findList(new Project());
        //根节点
        List<City> root = new ArrayList<City>();
        for (City nav : allCity) {
            if (nav.getParentId().equals("-1")) {//父节点是-1的，为根节点。
                root.add(nav);
            }
        }
        //为根菜单设置子菜单，getClild是递归调用的
        for (City nav : root) {
            /* 获取根节点下的所有子节点 使用getChild方法*/
            List<City> childList = getChild(nav.getId(), allCity);
            nav.setChildren(childList);//给根节点设置子节点
        }
        // 根据城市ID查询项目
        for (City city : allCity) {
            List<Project> list = projects.stream().filter(p ->city.getId().equals(p.getCityId()) && p.getTenantCode().equals(SysUtil.getTenantCode())).collect(Collectors.toList());
            if (list!= null && list.size() >0){
                city.setProjectList(list);
            }
        }
        return root;

    }


    /**
     * 获取子节点
     *
     * @param id      父节点id
     * @param allCity 所有城市列表
     * @return 每个根节点下，所有子菜单列表
     */
    public List<City> getChild(String id, List<City> allCity) {
        //子菜单
        List<City> childList = new ArrayList<City>();
        for (City nav : allCity) {
            // 遍历所有节点，将所有菜单的父id与传过来的根节点的id比较
            //相等说明：为该根节点的子节点。
            if (nav.getParentId().equals(id)) {
                childList.add(nav);
            }
        }
        //递归
        for (City nav : childList) {
            nav.setChildren(getChild(nav.getId(), allCity));
        }
        //如果节点下没有子节点，返回一个空List（递归退出）
        if (childList.size() == 0) {
            return new ArrayList<City>();
        }
        return childList;
    }


    /**
     *获取地图设备列表
     * @param  kind 设备种类 2报障箱 3摄像机
     * @param cityCodes 城市编码集合
     * @param projectId 项目ID
     * @param longMin  经度最小值
     * @param longMax  经度最大值
     * @param latMin   维度最小值
     * @param latMax   维度最大值
     * @return list
     */
    public List<DeviceVo> findDeviceList(String kind,String cityCodes,String projectId,String longMin,String longMax,String latMin,String latMax ){

        return cityMapper.findDeviceList(kind,cityCodes,projectId,longMin,longMax,latMin,latMax);
    }


    /**
     *获取地图设备统计
     * @param  kind 设备种类 2报障箱 3摄像机
     * @param cityCode 城市编码
     * @param cityCodes 城市编码集合
     * @param projectId 项目ID
     * @param longMin  经度最小值
     * @param longMax  经度最大值
     * @param latMin   维度最小值
     * @param latMax   维度最大值
     * @return list
     */
    public Map findDevicesStatistics(String kind, String cityCode, String cityCodes, String projectId,String longMin,String longMax,String latMin,String latMax ){

        return cityMapper.findDevicesStatistics(kind,cityCode,cityCodes,projectId,longMin,longMax,latMin,latMax);
    }


    /**
     * 总体在线率
     * @param  kind 设备种类 2报障箱 3摄像机
     * @param cityCode 城市编码
     * @param projectId 项目ID
     * @param startTime 开始日期
     * @param endTime 结束日期
     * @return Map
     */
    public Map getDevicesStatistics(String kind, String cityCode, String projectId,String startTime,String endTime){
        if (endTime.equals(startTime)){
            // 查询当前
            return cityMapper.findDevicesStatistics(kind, cityCode,null, projectId,null,null,null,null);
        }else{
            // 本月/本年
            return cityAlarmStatisticsMapper.findDeviceCount(kind,cityCode,projectId,startTime,endTime);
        }
    }


    /**
     * 各区域在线率
     * @param  kind 设备种类 2报障箱 3摄像机
     * @param cityCode 城市编码
     * @param parentId 父级ID
     * @param projectId 项目ID
     * @param level 级别
     * @param startTime 开始日期
     * @param endTime 结束日期
     * @return list
     */
    public List<CityVo> getCityOnlineStatistics(String kind, String cityCode,String parentId, String projectId, String level,String startTime,String endTime) {
        if (endTime.equals(startTime)) {
            // 当前
            return cityMapper.getCityRate(kind, parentId, cityCode,projectId,level);
        }else{
            // 本月/本年度
            return cityAlarmStatisticsMapper.findCityDeviceCount(kind,cityCode,parentId,projectId,level,startTime,endTime);

        }
    }

    /**
     * 各设备厂商在线率
     *
     * @param kind          设备种类 2报障箱 3摄像机
     * @param cityCode      城市CODE
     * @param projectId     项目ID
     * @param startTime     开始日期
     * @param endTime     结束日期
     * @param endTime     结束日期
     * @return list
     */
    public List<CityVo> getManufacturerOnlineStatistics(String kind, String cityCode, String projectId,String startTime,String endTime) {
        if (endTime.equals(startTime)) {
            // 当前
            return cityMapper.findManufacturerDevicesStatistics(kind, cityCode, projectId,"manufacturer");
        } else {
            // 本月/本年度
           return  cityAlarmStatisticsMapper.findManufacturerDeviceCount(kind, cityCode, projectId, startTime, endTime,"manufacturer");
        }
    }


    /**
     * 各设备型号在线率
     *
     * @param kind          设备种类 2报障箱 3摄像机
     * @param cityCode      城市CODE
     * @param projectId     项目ID
     * @param startTime     开始日期
     * @param endTime     结束日期
     * @param endTime     结束日期
     * @return list
     */
    public List<CityVo> getDeviceModelOnlineStatistics(String kind, String cityCode, String projectId,String startTime,String endTime) {
        if (endTime.equals(startTime)) {
            // 当前
            return cityMapper.findManufacturerDevicesStatistics(kind, cityCode, projectId,"device_type");
        } else {
            // 本月/本年度
            return  cityAlarmStatisticsMapper.findManufacturerDeviceCount(kind, cityCode, projectId, startTime, endTime,"device_type");
        }
    }



    /**
     * 故障类型占比
     *
     * @param kind          设备种类 2报障箱 3摄像机
     * @param cityCode      城市CODE
     * @param projectId     项目ID
     * @param startTime     开始日期
     * @param endTime     结束日期
     * @param endTime     结束日期
     * @return list
     */
    public List<Map> getFaultTypeStatistics(String kind, String cityCode, String projectId,String startTime,String endTime) {
        if (endTime.equals(startTime)) {
            // 当前
           return  cityMapper.getFaultType(kind, cityCode, projectId);
        } else {
            // 本月/本年度
         return   cityAlarmStatisticsMapper.findFaultTypeDeviceCount(kind, cityCode, projectId, startTime, endTime);
        }

    }


    /**
     * 资产统计
     *
     * @param kind          设备种类 2报障箱 3摄像机
     * @param cityCode      城市CODE
     * @param parentId      父级ID
     * @param projectId     项目ID
     * @param queryDate     查询日期
     * @param deviceType     设备类型
     * @param deviceModelName    设备型号
     * @return list
     */
    public List<Map> getAssetsStatistics(String kind, String cityCode,String parentId, String projectId,String queryDate,String deviceType,String deviceModelName) {
            return   cityMapper.getAssetsStatistics(kind, cityCode,parentId, projectId,queryDate,deviceType,deviceModelName);
        }


    /**
     * 查询网络拓扑图
     *
     * @param cityCodes 城市编码集合
     * @param projectId 项目ID
     * @return list
     */
    public List<CityProjectDeviceVo> getCityTree(String cityCodes, String projectId) {

        return cityMapper.getCityTree(cityCodes,projectId);
    }

}