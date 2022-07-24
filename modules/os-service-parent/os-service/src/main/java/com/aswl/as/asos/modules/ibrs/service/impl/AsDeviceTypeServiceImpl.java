package com.aswl.as.asos.modules.ibrs.service.impl;

import com.aswl.as.asos.common.utils.Constant;
import com.aswl.as.asos.modules.sys.entity.SysUserEntity;
import com.aswl.as.common.core.constant.CommonConstant;
import com.aswl.as.common.core.utils.IdGen;
import com.aswl.as.asos.common.utils.Query;
import com.aswl.as.asos.common.utils.PageUtils;
import com.aswl.as.asos.datasource.annotation.DataSource;
import com.aswl.as.asos.modules.ibrs.entity.AsDeviceType;
import com.aswl.as.asos.modules.ibrs.mapper.AsDeviceTypeMapper;
import com.aswl.as.asos.modules.ibrs.service.IAsDeviceTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 设备类型 服务实现类
 * </p>
 *
 * @author hfx
 * @since 2019-12-02
 */
@Service
public class AsDeviceTypeServiceImpl extends ServiceImpl<AsDeviceTypeMapper, AsDeviceType> implements IAsDeviceTypeService {

    @DataSource("slave1")
    public PageUtils queryPage(Map<String, Object> params)
    {

        // 重新写sql
        String deviceTypeName = (String)params.get("deviceTypeName");
        String deviceKindId = (String)params.get("deviceKindId");



//        IPage<AsDeviceType> page = this.page(new Query<AsDeviceType>().getPage(params),new QueryWrapper<AsDeviceType>()
//                .like(StringUtils.isNotEmpty(deviceTypeName),"device_type_name",deviceTypeName)
//                .eq(StringUtils.isNotEmpty(deviceKindId),"device_kind_id",deviceKindId));


        // 修改成用sql查出来的东西
        IPage<AsDeviceType> page=new Query<AsDeviceType>().getPage(params);

        Map<String, Object> temp=new HashMap<>(params);
        temp.put(Constant.PAGE,null);
        temp.put(Constant.LIMIT,null);

        temp.put("deviceTypeName",deviceTypeName);
        temp.put("deviceKindId",deviceKindId);

        int totalCount=baseMapper.countAsDeviceTypes(temp);
        page.setTotal(totalCount);
        page.setPages((totalCount+page.getSize()-1)/page.getSize());
        if(totalCount<1)
        {
            page.setRecords(new ArrayList<>());
        }
        else
        {
            temp.put("limit1",(page.getCurrent()-1)*page.getSize());
            temp.put("limit2",page.getSize());

            List<AsDeviceType>list=baseMapper.queryAsDeviceTypes(temp);
            page.setRecords(list);
        }

        return new PageUtils(page);
    }

    @DataSource("slave1")
    public List<AsDeviceType> findList(AsDeviceType entity)
    {
        return list(new QueryWrapper<AsDeviceType>());
    }

    //根据id返回对应信息
    @DataSource("slave1")
    public AsDeviceType getEntityById(String id)
    {
        return this.getById(id);
    }

    @DataSource("slave1")
    public boolean saveEntity(AsDeviceType entity)
    {
        entity.setId(IdGen.snowflakeId());
        entity.setDelFlag(CommonConstant.DEL_FLAG_NORMAL);
        return this.save(entity);
    }

    //添加判断是否有这种设备
    @DataSource("slave1")
    public boolean checkDeviceType(AsDeviceType entity)
    {
        AsDeviceType d=this.getOne(new QueryWrapper<AsDeviceType>().eq("device_type",entity.getDeviceType()));
        if(d==null)
        {
            return false;
        }
        return true;
    }

    @DataSource("slave1")
    public boolean updateEntityById(AsDeviceType entity)
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


}
