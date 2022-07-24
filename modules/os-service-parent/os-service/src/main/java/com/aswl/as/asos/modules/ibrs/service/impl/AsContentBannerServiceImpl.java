package com.aswl.as.asos.modules.ibrs.service.impl;

import com.aswl.as.asos.common.utils.Constant;
import com.aswl.as.asos.modules.ibrs.entity.AsContentIndustry;
import com.aswl.as.asos.modules.ibrs.entity.AsContentProduct;
import com.aswl.as.common.core.utils.IdGen;
import com.aswl.as.asos.common.utils.Query;
import com.aswl.as.asos.common.utils.PageUtils;
import com.aswl.as.asos.common.annotation.SysLog;
import com.aswl.as.asos.datasource.annotation.DataSource;
import com.aswl.as.asos.modules.ibrs.entity.AsContentBanner;
import com.aswl.as.asos.modules.ibrs.mapper.AsContentBannerMapper;
import com.aswl.as.asos.modules.ibrs.service.IAsContentBannerService;
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
 * banner管理 服务实现类
 * </p>
 *
 * @author hfx
 * @since 2020-03-12
 */
@Service
public class AsContentBannerServiceImpl extends ServiceImpl<AsContentBannerMapper, AsContentBanner> implements IAsContentBannerService {

    String[] URL_ARR=new String[]{"/contentMalfunction/html/","/contentIndustry/html/","/contentProduct/html/"};

    @DataSource("slave1")
    public PageUtils queryPage(Map<String, Object> params)
    {

//        String title = (String)params.get("title");
//        String displayPosition = (String)params.get("displayPosition");
//        String isRelease=(String)params.get("isRelease");
//
//        IPage<AsContentBanner> page = this.page(
//            new Query<AsContentBanner>().getPage(params),
//                new QueryWrapper<AsContentBanner>()
//                        .like(StringUtils.isNotBlank(title),"title",title)
//                        .eq(StringUtils.isNotBlank(displayPosition),"display_position",displayPosition)
//                        .eq(StringUtils.isNotBlank(isRelease),"is_release",isRelease)
//                        .orderByDesc("sort")
//            );
//
//        return new PageUtils(page);


        // 修改成用sql查出来的东西
        IPage<AsContentBanner> page=new Query<AsContentBanner>().getPage(params);

        Map<String, Object> temp=new HashMap<>(params);
        temp.put(Constant.PAGE,null);
        temp.put(Constant.LIMIT,null);

        int totalCount=baseMapper.countBanners(temp);
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

            List<AsContentBanner>list=baseMapper.queryBanners(temp);
            page.setRecords(list);
        }

        return new PageUtils(page);
    }

    @DataSource("slave1")
    public List<AsContentBanner> findList(AsContentBanner entity)
    {
        // 这里如果需要的话，就改成连表查出来
        return list(
                new QueryWrapper<AsContentBanner>()
                        .like(StringUtils.isNotBlank(entity.getTitle()),"title",entity.getTitle())
                        .eq(StringUtils.isNotBlank(entity.getDisplayPosition()),"display_position",entity.getDisplayPosition())
                        .eq(entity.getIsRelease()!=null,"is_release",entity.getIsRelease())
        );
    }

    //根据id返回对应信息
    @DataSource("slave1")
    public AsContentBanner getEntityById(String id)
    {
        return this.getById(id);
    }

    @DataSource("slave1")
    public boolean saveEntity(AsContentBanner entity)
    {
        entity.setId(IdGen.snowflakeId());

        // 判断是否在数组里面
        if(inUrlArr(entity.getContentLink()))
        {
            entity.setCategory(4);
            entity.setContentId(entity.getId());
        }

        entity.setIsRelease(0);
        entity.setClicks(0);
        entity.setDelFlag(0);
        entity.setSort(getMaxSort()+1);

        return this.save(entity);
    }

    @DataSource("slave1")
    public boolean updateEntityById(AsContentBanner entity)
    {
        // 判断是否在数组里面
        if(inUrlArr(entity.getContentLink()))
        {
            entity.setCategory(4);
            entity.setContentId(entity.getId());
        }
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

    //发布和取消发布
    @DataSource("slave1")
    public boolean setIsRelease(AsContentBanner entity, String[] ids)
    {
        return baseMapper.update(entity,new QueryWrapper<AsContentBanner>().in("id",ids))>0;
    }

    // 上移下移置顶
    @DataSource("slave1")
    public boolean moveUp(AsContentBanner entity)
    {
        return changeSort(entity,true);
    }

    @DataSource("slave1")
    public boolean moveDown(AsContentBanner entity)
    {
        return changeSort(entity,false);
    }

    @DataSource("slave1")
    public boolean sticky(AsContentBanner entity)
    {
        entity.setSort(getMaxSort()+1);
        return baseMapper.updateById(entity)>0;
    }

    private boolean inUrlArr(String contentLink)
    {
        if(StringUtils.isNotEmpty(contentLink))
        {
            for(String temp:URL_ARR)
            {
                if( contentLink.indexOf(temp)>-1)
                {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean changeSort(AsContentBanner entity,boolean isUp)
    {
        AsContentBanner p=getSort(entity.getSort(),isUp);
        if(p!=null)
        {
            int tempSort=p.getSort();
            p.setSort(entity.getSort());
            entity.setSort(tempSort);
            baseMapper.updateById(p);
            baseMapper.updateById(entity);
        }
        return true;
    }

    public Integer getMaxSort()
    {
        AsContentBanner r1 =baseMapper.selectOne(new QueryWrapper<AsContentBanner>().orderByDesc("sort").last(" limit 1"));
        if(r1 != null)
        {
            return r1.getSort();
        }
        else
        {
            return 0;
        }
    }

    /**
     * 获取sort
     * @param sort
     * @param isUp
     * @return
     */
    @DataSource("slave1")
    public AsContentBanner getSort(Integer sort,boolean isUp)
    {
        AsContentBanner r1 =baseMapper.selectOne(new QueryWrapper<AsContentBanner>()
                .gt(sort !=null && isUp,"sort",sort)
                .orderByAsc(isUp,"sort")
                .lt(sort !=null && !isUp,"sort",sort)
                .orderByDesc(!isUp,"sort")
                .last(" limit 1")
        );
        if(r1!=null)
        {
            return r1;
        }
        return null;
    }


}
