package com.aswl.as.asos.modules.ibrs.service.impl;

import com.aswl.as.asos.common.exception.RRException;
import com.aswl.as.asos.common.utils.Constant;
import com.aswl.as.asos.modules.ibrs.entity.AsContentProduct;
import com.aswl.as.common.core.exceptions.CommonException;
import com.aswl.as.common.core.utils.IdGen;
import com.aswl.as.asos.common.utils.Query;
import com.aswl.as.asos.common.utils.PageUtils;
import com.aswl.as.asos.common.annotation.SysLog;
import com.aswl.as.asos.datasource.annotation.DataSource;
import com.aswl.as.asos.modules.ibrs.entity.AsContentMalfunction;
import com.aswl.as.asos.modules.ibrs.mapper.AsContentMalfunctionMapper;
import com.aswl.as.asos.modules.ibrs.service.IAsContentMalfunctionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.*;

/**
 * <p>
 * 常见故障表 服务实现类
 * </p>
 *
 * @author hfx
 * @since 2020-03-04
 */
@Service
public class AsContentMalfunctionServiceImpl extends ServiceImpl<AsContentMalfunctionMapper, AsContentMalfunction> implements IAsContentMalfunctionService {

    // 最大的橱窗推荐数量
    @Value(value = "${asOsContent.showcaseMaxCount.malfunction}")
    private Integer malfunctionShowcaseMaxCount;

    //最小的 最终热点数量
    private int minHeatMaxCount=3000;

    //最大的 最终热点数量
    private int maxHeatMaxCount=5000;

    @DataSource("slave1")
    public PageUtils queryPage(Map<String, Object> params)
    {

        /*
        IPage<AsContentMalfunction> page = this.page(
            new Query<AsContentMalfunction>().getPage(params),
                new QueryWrapper<AsContentMalfunction>()
            );

         */

        // 修改成用sql查出来的东西
        IPage<AsContentMalfunction> page=new Query<AsContentMalfunction>().getPage(params);

        Map<String, Object> temp=new HashMap<>(params);
        temp.put(Constant.PAGE,null);
        temp.put(Constant.LIMIT,null);

        int totalCount=baseMapper.countMalfunctions(temp);
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

            List<AsContentMalfunction>list=baseMapper.queryMalfunctions(temp);
            for(AsContentMalfunction m:list)
            {
                m.setUrl("/contentMalfunction/html/"+m.getId());
            }
            page.setRecords(list);
        }

        return new PageUtils(page);
    }

    @DataSource("slave1")
    public List<AsContentMalfunction> findList(AsContentMalfunction entity)
    {
        return list(new QueryWrapper<AsContentMalfunction>());
    }

    //根据id返回对应信息
    @DataSource("slave1")
    public AsContentMalfunction getEntityById(String id)
    {
        return this.getById(id);
    }

    @DataSource("slave1")
    public boolean saveEntity(AsContentMalfunction entity)
    {
        entity.setId(IdGen.snowflakeId());

        entity.setIsShowcase(0);
        entity.setIsRelease(0);
        entity.setClicks(0);
        entity.setDelFlag(0);
        entity.setSort(getMaxSort()+1);

        //设置热点数量
        entity.setHeatCount(0);
        entity.setHeatMaxCount(minHeatMaxCount+new Random().nextInt(maxHeatMaxCount-minHeatMaxCount));
        entity.setLastHeatTime(new Date());

        return this.save(entity);
    }

    @DataSource("slave1")
    public boolean updateEntityById(AsContentMalfunction entity)
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

    //设为橱窗
    @DataSource("slave1")
    public boolean setShowcase(AsContentMalfunction entity)
    {
        if(entity.getIsShowcase()==1)
        {
            List<AsContentMalfunction> list= baseMapper.selectList(new QueryWrapper<AsContentMalfunction>().eq("is_showcase","1"));
            int count=0;
            for(AsContentMalfunction temp:list)
            {
                if(!temp.getId().equals(entity.getId()))
                {
                    count++;
                }
            }
            if(count+1>malfunctionShowcaseMaxCount)
            {
                throw new RRException("当前分类只能设置"+malfunctionShowcaseMaxCount+"个橱窗，已超出设置数量。");
            }
        }

        return this.updateById(entity);
    }

    //发布和取消发布
    @DataSource("slave1")
    public boolean setIsRelease(AsContentMalfunction entity,String[] ids)
    {
        return baseMapper.update(entity,new QueryWrapper<AsContentMalfunction>().in("id",ids))>0;
    }

    // 添加上移 下移 和 置顶功能--------------------------------------------------


    @DataSource("slave1")
    public boolean moveUp(AsContentMalfunction entity)
    {
        return changeSort(entity,true);
    }

    @DataSource("slave1")
    public boolean moveDown(AsContentMalfunction entity)
    {
        return changeSort(entity,false);
    }

    @DataSource("slave1")
    public boolean sticky(AsContentMalfunction entity)
    {
        entity.setSort(getMaxSort()+1);
        return baseMapper.updateById(entity)>0;
    }

    public boolean changeSort(AsContentMalfunction entity,boolean isUp)
    {
        AsContentMalfunction p=getSort(entity.getSort(),isUp);
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
        AsContentMalfunction r1 =baseMapper.selectOne(new QueryWrapper<AsContentMalfunction>().orderByDesc("sort").last(" limit 1"));
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
    public AsContentMalfunction getSort(Integer sort,boolean isUp)
    {
        AsContentMalfunction r1 =baseMapper.selectOne(new QueryWrapper<AsContentMalfunction>()
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
