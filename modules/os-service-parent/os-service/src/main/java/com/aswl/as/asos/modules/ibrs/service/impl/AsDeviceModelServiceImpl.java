package com.aswl.as.asos.modules.ibrs.service.impl;

import com.aswl.as.asos.common.utils.Constant;
import com.aswl.as.common.core.constant.CommonConstant;
import com.aswl.as.common.core.utils.IdGen;
import com.aswl.as.asos.common.utils.Query;
import com.aswl.as.asos.common.utils.PageUtils;
import com.aswl.as.asos.common.annotation.SysLog;
import com.aswl.as.asos.datasource.annotation.DataSource;
import com.aswl.as.asos.modules.ibrs.entity.AsDeviceModel;
import com.aswl.as.asos.modules.ibrs.mapper.AsDeviceModelMapper;
import com.aswl.as.asos.modules.ibrs.service.IAsDeviceModelService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 设备型号 服务实现类
 * </p>
 *
 * @author hfx
 * @since 2019-12-02
 */
@Service
public class AsDeviceModelServiceImpl extends ServiceImpl<AsDeviceModelMapper, AsDeviceModel> implements IAsDeviceModelService {

    @DataSource("slave1")
    public PageUtils queryPage(Map<String, Object> params)
    {
        /*
        String projectName = (String)params.get("projectName");
        String projectDes = (String)params.get("projectDes");
        String projectOwner = (String)params.get("projectOwner");
        */

        /*
        IPage<AsDeviceModel> page = this.page(
            new Query<AsDeviceModel>().getPage(params),
                new QueryWrapper<AsDeviceModel>()
            );
        */

        //使用连表来查询

        // 修改成用sql查出来的东西
        IPage<AsDeviceModel> page=new Query<AsDeviceModel>().getPage(params);

        Map<String, Object> temp=new HashMap<>(params);
        temp.put(Constant.PAGE,null);
        temp.put(Constant.LIMIT,null);

        int totalCount=baseMapper.countDeviceModels(temp);
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

            List<AsDeviceModel>list=baseMapper.queryDeviceModels(temp);
            page.setRecords(list);
        }

        return new PageUtils(page);
    }

    @DataSource("slave1")
    public List<AsDeviceModel> findList(AsDeviceModel entity)
    {
        return list(new QueryWrapper<AsDeviceModel>());
    }

    //根据id返回对应信息
    @DataSource("slave1")
    public AsDeviceModel getEntityById(String id)
    {
        return this.getById(id);
    }

    @DataSource("slave1")
    public boolean saveEntity(AsDeviceModel entity)
    {
//        entity.setProjectId(IdGen.snowflakeId());
        entity.setId(IdGen.snowflakeId());
        entity.setDelFlag(CommonConstant.DEL_FLAG_NORMAL);
        return this.save(entity);
    }

    @DataSource("slave1")
    public boolean updateEntityById(AsDeviceModel entity)
    {
        return this.updateById(entity);
    }

    @DataSource("slave1")
    public boolean deleteEntityById(String id)
    {
        return this.removeById(id);
    }

    @DataSource("slave1")
    public boolean checkDeviceModel(AsDeviceModel entity)
    {
        AsDeviceModel d=this.getOne(new QueryWrapper<AsDeviceModel>().eq("device_model_name",entity.getDeviceModelName()));
        if(d==null)
        {
            return false;
        }
        return true;
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
