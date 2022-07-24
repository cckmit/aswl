package com.aswl.as.ibrs.service;

import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.common.core.utils.IdGen;
import com.aswl.as.ibrs.api.dto.AlarmTypeDto;
import com.aswl.as.ibrs.api.module.AlarmType;
import com.aswl.as.ibrs.api.module.EventUcMetadata;
import com.aswl.as.ibrs.api.vo.AlarmTypeVo;
import com.aswl.as.ibrs.mapper.AlarmTypeMapper;
import com.aswl.as.ibrs.mapper.EventUcMetadataMapper;
import com.aswl.as.ibrs.utils.StringUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
@Slf4j
@Service
public class AlarmTypeService extends CrudService<AlarmTypeMapper, AlarmType> {
    private final AlarmTypeMapper alarmTypeMapper;
    private  final EventUcMetadataMapper metadataMapper;


    public PageInfo<AlarmTypeVo> findPage(PageInfo<AlarmTypeDto> page, AlarmTypeDto alarmTypeDto) {
      PageHelper.startPage(page.getPageNum(), page.getPageSize());
        return new PageInfo<>(alarmTypeMapper.findAlarmTypeListInfo(alarmTypeDto));
    }

    /**
     * 新增报警类型
     *
     * @param alarmTypeDto
     * @return int
     */
    @Transactional
    public int insert(AlarmTypeDto alarmTypeDto) {
        AlarmType alarmType = new AlarmType();
        BeanUtils.copyProperties(alarmTypeDto, alarmType);
        alarmType.setId(IdGen.snowflakeId());
        // 通过元数据ID查询事件元数据
        if (StringUtils.isNotEmpty(alarmTypeDto.getEventMetadataId())){
            EventUcMetadata eventUcMetadata=new EventUcMetadata();
            eventUcMetadata.setId(alarmTypeDto.getEventMetadataId());
            EventUcMetadata entity = metadataMapper.get(eventUcMetadata);
            alarmType.setStatusName(entity.getStatusName());
            alarmType.setAlarmType(entity.getStatusName()+alarmTypeDto.getKind()+alarmTypeDto.getStatusValue());
        }
        return super.insert(alarmType);
    }

    /**
     * 删除报警类型
     *
     * @param alarmType
     * @return int
     */
    @Transactional
    @Override
    public int delete(AlarmType alarmType) {
        return super.delete(alarmType);
    }

    /**
     * 查询报警类型
     *
     * @return
     */
    public List<AlarmTypeVo> findAlarmType(String[] alarmLevels) {
        return alarmTypeMapper.findAlarmType(alarmLevels);
    }

    public List<AlarmTypeVo> findDeviceAlarmType(String[] alarmLevels, String id, String kind) {

        return alarmTypeMapper.findDeviceAlarmType(alarmLevels, id, kind);

    }

    public static void main(String[] args) {
        String[] str={"张三","李四","王五","宋六","赵七","朱八","何九","田十"};
        List<String> list= Arrays.asList(str);//将数组转换为list集合
        if(list.contains("张三")){//加入集合中包含这个元素
            List<String> arrayList=new ArrayList<String>(list);//转换为ArrayLsit调用相关的remove方法
            arrayList.remove("张三");
            for(String str1:arrayList ){
                System.out.print(str1+",");
            }
        }
    }

    /**
     * 模糊查询告警类型
     * @param prefix
     * @return
     */
    public String findType(String prefix) {
        return alarmTypeMapper.findTypes(prefix);
    }


    /**
     * 批量修改报警类型
     * @param list 
     * @return int 
     */
    @Transactional
    public int updateBath(List<AlarmType> list) {
        int update = 0;
        for (AlarmType alarmType: list) {
            alarmTypeMapper.update(alarmType);
            update ++;
        }
        return update;
    }

    /**
     * 根据alarmType集合获取告警最小级别（最高级）
     * @param alarmTypes
     * @return
     */
    public Integer loadMinAlarmLevel(List<String> alarmTypes){
        if(alarmTypes == null || alarmTypes.size() == 0){
            return null;
        }
        return alarmTypeMapper.loadMinAlarmLevel(alarmTypes);
    }
}