package com.aswl.as.asos.modules.ibrs.service.impl;

import com.aswl.as.asos.modules.ibrs.entity.AsEventUcMetadata;
import com.aswl.as.asos.modules.ibrs.entity.AsEventUcStatusGroupModel;
import com.aswl.as.asos.modules.ibrs.entity.AsEventUcStatusOperation;
import com.aswl.as.common.core.constant.CommonConstant;
import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.utils.IdGen;
import com.aswl.as.asos.common.utils.Query;
import com.aswl.as.asos.common.utils.PageUtils;
import com.aswl.as.asos.common.annotation.SysLog;
import com.aswl.as.asos.datasource.annotation.DataSource;
import com.aswl.as.asos.modules.ibrs.entity.AsEventUcStatusGroup;
import com.aswl.as.asos.modules.ibrs.mapper.AsEventUcStatusGroupMapper;
import com.aswl.as.asos.modules.ibrs.service.IAsEventUcStatusGroupService;
import com.aswl.as.common.core.vo.OsVo;
import com.aswl.as.ibrs.api.feign.RegionServiceClient;
import com.aswl.as.ibrs.api.vo.EventUcMetadataVo;
import com.aswl.as.ibrs.api.vo.EventUcStatusOperationVo;
//import com.aswl.as.ibrs.api.vo.eventUcStatusGroupModelVo;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 事件状态组 服务实现类
 * </p>
 *
 * @author hfx
 * @since 2020-01-08
 */
@Service
@AllArgsConstructor
public class AsEventUcStatusGroupServiceImpl extends ServiceImpl<AsEventUcStatusGroupMapper, AsEventUcStatusGroup> implements IAsEventUcStatusGroupService {

    private RegionServiceClient regionServiceClient;

    @DataSource("slave1")
    public PageUtils queryPage(Map<String, Object> params)
    {

        IPage<AsEventUcStatusGroup> page = this.page(
            new Query<AsEventUcStatusGroup>().getPage(params),
                new QueryWrapper<AsEventUcStatusGroup>()

            );

        return new PageUtils(page);
    }

    @DataSource("slave1")
    public List<AsEventUcStatusGroup> findList(AsEventUcStatusGroup entity)
    {
        return list(new QueryWrapper<AsEventUcStatusGroup>());
    }

    //根据id返回对应信息
    @DataSource("slave1")
    public AsEventUcStatusGroup getEntityById(String id)
    {
        return this.getById(id);
    }

    @DataSource("slave1")
    public boolean saveEntity(AsEventUcStatusGroup entity)
    {
        entity.setId(IdGen.snowflakeId());
        return this.save(entity);
    }

    @DataSource("slave1")
    public boolean updateEntityById(AsEventUcStatusGroup entity)
    {
        return this.updateById(entity);
    }

    @DataSource("slave1")
    public boolean deleteEntityById(String id)
    {
        return this.removeById(id);
    }

    @DataSource("slave1")
    public boolean deleteEntityByIds(String[] ids)
    {
        for(String id:ids)
        {
            this.removeById(id);
        }
        return true;
    }

    @DataSource("slave1")
    public List<AsEventUcStatusOperation> osGetselectExtendStatusGroupOperationList(String eventMetadataModelId)
    {
        return baseMapper.osGetselectExtendStatusGroupOperationList(eventMetadataModelId);
    }

    @DataSource("slave1")
    public ResponseBean<List<AsEventUcStatusOperation>> osEvent1(String eventMetadataId)
    {
        // 修改成直接从数据库查询,因为这接口的sql应该不会变的
        return new ResponseBean<List<AsEventUcStatusOperation>>(baseMapper.osGetExtendStatusGroupOperationList(eventMetadataId));
//        return regionServiceClient.osEvent1(eventMetadataId,OsVo.getRandomStr());
    }

    @DataSource("slave1")
    public ResponseBean<List<AsEventUcStatusGroupModel>> osEvent2(String deviceModelId)
    {
        // 修改成直接从数据库查询,因为这接口的sql应该不会变的
        return new ResponseBean<List<AsEventUcStatusGroupModel>>(baseMapper.osGetExtendStatusGroup(deviceModelId));
//        return regionServiceClient.osEvent2(deviceModelId,OsVo.getRandomStr());
    }

    @DataSource("slave1")
    public ResponseBean<List<AsEventUcMetadata>> osEvent3(String deviceModelId, String groupId)
    {
        // 修改成直接从数据库查询,因为这接口的sql应该不会变的
        AsEventUcStatusGroupModel data=new AsEventUcStatusGroupModel();
        data.setDeviceModelId(deviceModelId);
        data.setEventStatusGroupId(groupId);
        return new ResponseBean<List<AsEventUcMetadata>>(baseMapper.osGetExtendStatusGroupAttribute(data));
        //return regionServiceClient.osEvent3(deviceModelId,groupId,OsVo.getRandomStr());
    }

}
