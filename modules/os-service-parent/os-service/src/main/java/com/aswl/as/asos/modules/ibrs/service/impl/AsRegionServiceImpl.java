package com.aswl.as.asos.modules.ibrs.service.impl;

import com.aswl.as.asos.common.exception.CommonException;
import com.aswl.as.asos.common.exception.RRException;
import com.aswl.as.asos.modules.asuser.entity.SysTenant;
import com.aswl.as.common.core.constant.CommonConstant;
import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.utils.IdGen;
import com.aswl.as.asos.common.utils.Query;
import com.aswl.as.asos.common.utils.PageUtils;
import com.aswl.as.asos.common.annotation.SysLog;
import com.aswl.as.asos.datasource.annotation.DataSource;
import com.aswl.as.asos.modules.ibrs.entity.AsRegion;
import com.aswl.as.asos.modules.ibrs.mapper.AsRegionMapper;
import com.aswl.as.asos.modules.ibrs.service.IAsRegionService;
import com.aswl.as.common.core.utils.NumberUtil;
import com.aswl.as.common.core.utils.SysUtil;
import com.aswl.as.common.core.utils.YouBianCodeUtil;
import com.aswl.as.common.core.vo.OsVo;
import com.aswl.as.ibrs.api.feign.RegionServiceClient;
import com.aswl.as.ibrs.api.module.Device;
import com.aswl.as.ibrs.api.module.Region;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.netty.util.internal.StringUtil;
import lombok.AllArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 区域表 服务实现类
 * </p>
 *
 * @author hfx
 * @since 2019-12-11
 */
@Service
@AllArgsConstructor
public class AsRegionServiceImpl extends ServiceImpl<AsRegionMapper, AsRegion> implements IAsRegionService {

    private RegionServiceClient regionServiceClient;

    @DataSource("slave1")
    public PageUtils queryPage(Map<String, Object> params)
    {
        /*
        String projectName = (String)params.get("projectName");
        String projectDes = (String)params.get("projectDes");
        String projectOwner = (String)params.get("projectOwner");
        */

        IPage<AsRegion> page = this.page(
            new Query<AsRegion>().getPage(params),
                new QueryWrapper<AsRegion>()
                /*
                .like(StringUtils.isNotBlank(projectName),"project_name",projectName)
                .like(StringUtils.isNotBlank(projectDes),"project_des",projectDes)
                .like(StringUtils.isNotBlank(projectOwner),"project_owner",projectOwner)
                */
            );

        return new PageUtils(page);
    }

    @DataSource("slave1")
    public List<AsRegion> findList(AsRegion entity)
    {
        return baseMapper.selectList(
                new QueryWrapper<AsRegion>().eq(entity.getDelFlag()!=null,"del_flag", entity.getDelFlag())
                .eq(!StringUtils.isEmpty(entity.getTenantCode()),"tenant_code",entity.getTenantCode())
                .eq(!StringUtils.isEmpty(entity.getProjectId()),"project_id",entity.getProjectId())
                .eq(!StringUtils.isEmpty(entity.getRegionCode()),"region_code",entity.getRegionCode())
                .eq(!StringUtils.isEmpty(entity.getParentId()),"parent_id",entity.getParentId())
        );
    }

    //根据id返回对应信息
    @DataSource("slave1")
    public AsRegion getEntityById(String id)
    {
        return this.getById(id);
    }

    @DataSource("slave1")
    public boolean saveEntity(AsRegion entity)
    {
        //TODO 校验一个项目只有一个

        entity.setId(IdGen.snowflakeId());
        //生成区域编码
        generateRegionCode(entity);
        // 生成区域全称
        entity.setFullName(genareteRegionFullName(entity));
//        region.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        entity.setApplicationCode(SysUtil.getSysCode());
        entity.setDelFlag(CommonConstant.DEL_FLAG_NORMAL);
        return this.save(entity);
    }

    @DataSource("slave1")
    public boolean updateEntityById(AsRegion entity)
    {
        //生成区域编码
        generateRegionCode(entity);
        // 生成区域全称
        entity.setFullName(genareteRegionFullName(entity));
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

        // 删除区域时需要判断是否有设备数据，如果有，就不能删除

        // 判断是否有设备，如果有，就不能删除

        for(String id:ids)
        {

            AsRegion region=getEntityById(id);

            if(region!=null)
            {
                Device d=new Device();
//                d.setRegionId(id);
                d.setRegionCode(region.getRegionCode());
                d.setRandomStr(OsVo.getRandomStr());
                ResponseBean<List<Device>> r=regionServiceClient.osDevice1(d);
                if(r==null || r.getCode()!=ResponseBean.SUCCESS)
                {
                    throw new RRException("删除时校验区域设备时，获取设备列表失败，请重试.");
                }

                List<Device> list=r.getData();
                if(list.size()>0)
                {
                    throw new RRException("该区域下还有设备，请删除或转移设备到其他区域后再重试.");
                }

                this.removeById(id);
            }
        }
        return true;
    }

    @DataSource("slave1")
    public String getRegionByRegionCode(String regionCode)
    {
        AsRegion r=baseMapper.selectOne(new QueryWrapper<AsRegion>().eq("region_code",regionCode).last(" limit 1 "));
        return r==null?null:r.getParentId();
    }

    // 添加全称函数和生成区域编码函数
    /**
     * 自动生成区域全称
     * @return String
     */
    private String genareteRegionFullName(AsRegion region){
        StringBuilder sb=new StringBuilder();
        if (StringUtils.isNotBlank(region.getParentId())){
            if ("-1".equals(region.getParentId())){
                sb.append(region.getRegionName());
            }else {
                sb.append(region.getParentName()).append(",").append(region.getRegionName());
            }
            return sb.toString();
        }
        return null;
    }



    //
    /**
     * 自动生成区域编码
     * @param region 区域对象
     */
    private void generateRegionCode(AsRegion region) {

        if(!StringUtils.isEmpty(region.getId()))
        {
            //判断是否跟原来的那个上级区域一样，如果是一样，就不重复生成了
            AsRegion temp=baseMapper.selectById(region.getId());
            if(temp!=null && temp.getParentId()!=null && temp.getParentId().equals(region.getParentId()))
            {
                return;
            }
        }

        // 修改获取数据的函数
        AsRegion r1 =baseMapper.selectOne(new QueryWrapper<AsRegion>().eq("parent_id","-1").orderByDesc("create_date").orderByDesc("id").last(" limit 1"));
        if ("-1".equals(region.getParentId())) {
            if (r1 == null) {
                region.setParentId("-1");
                region.setRegionCode("A01");
            } else {
                region.setParentId("-1");
//                region.setRegionCode(NumberUtil.addOne(r1.getRegionCode()));
                region.setRegionCode(NumberUtil.addOneNum(r1.getRegionCode()));
            }
        }else{
            AsRegion r2 =baseMapper.selectOne(new QueryWrapper<AsRegion>().eq("parent_id",region.getParentId()).orderByDesc("create_date").orderByDesc("id").last(" limit 1"));
            if (r2==null){
                region.setRegionCode(region.getRegionCode()+"A01");
            }else{
//                region.setRegionCode(NumberUtil.addOne(r2.getRegionCode()));
                region.setRegionCode(NumberUtil.addOneNum(r2.getRegionCode()));
            }
        }
    }


}
