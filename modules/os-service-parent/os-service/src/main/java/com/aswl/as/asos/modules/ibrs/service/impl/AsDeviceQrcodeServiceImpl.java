package com.aswl.as.asos.modules.ibrs.service.impl;
import com.aswl.as.common.core.utils.IdGen;
import com.aswl.as.asos.common.utils.Query;
import com.aswl.as.asos.common.utils.PageUtils;
import com.aswl.as.asos.datasource.annotation.DataSource;
import com.aswl.as.asos.modules.ibrs.entity.AsDeviceQrcode;
import com.aswl.as.asos.modules.ibrs.mapper.AsDeviceQrcodeMapper;
import com.aswl.as.asos.modules.ibrs.service.IAsDeviceQrcodeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 设备二维码 服务实现类
 * </p>
 *
 * @author df
 * @since 2020-12-16
 */
@Service
@AllArgsConstructor
public class AsDeviceQrcodeServiceImpl extends ServiceImpl<AsDeviceQrcodeMapper, AsDeviceQrcode> implements IAsDeviceQrcodeService {

     private AsDeviceQrcodeMapper asDeviceQrcodeMapper;

    @DataSource("slave1")
    public PageUtils queryPage(Map<String, Object> params)
    {
        /*
        String projectName = (String)params.get("projectName");
        String projectDes = (String)params.get("projectDes");
        String projectOwner = (String)params.get("projectOwner");
        */

        IPage<AsDeviceQrcode> page = this.page(
            new Query<AsDeviceQrcode>().getPage(params),
                new QueryWrapper<AsDeviceQrcode>()
                /*
                .like(StringUtils.isNotBlank(projectName),"project_name",projectName)
                .like(StringUtils.isNotBlank(projectDes),"project_des",projectDes)
                .like(StringUtils.isNotBlank(projectOwner),"project_owner",projectOwner)
                */
            );

        return new PageUtils(page);
    }

    @DataSource("slave1")
    public List<AsDeviceQrcode> findList(AsDeviceQrcode entity)
    {
        return list(new QueryWrapper<AsDeviceQrcode>());
    }

    @DataSource("slave1")
    public boolean deleteByBathId(String bathId) {
        int count = asDeviceQrcodeMapper.deleteByBathId(bathId);
        if (count >0){
            return true;
        }
        return false;
    }

    //根据id返回对应信息
    @DataSource("slave1")
    public AsDeviceQrcode getEntityById(String id)
    {
        return this.getById(id);
    }

    @DataSource("slave1")
    public boolean saveEntity(AsDeviceQrcode entity)
    {
        entity.setId(IdGen.snowflakeId());
        return this.save(entity);
    }

    @DataSource("slave1")
    public boolean updateEntityById(AsDeviceQrcode entity)
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
