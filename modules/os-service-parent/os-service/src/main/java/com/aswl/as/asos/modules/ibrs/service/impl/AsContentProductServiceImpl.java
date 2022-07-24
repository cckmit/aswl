package com.aswl.as.asos.modules.ibrs.service.impl;

import com.aswl.as.asos.common.exception.RRException;
import com.aswl.as.asos.common.utils.Constant;
import com.aswl.as.common.core.exceptions.CommonException;
import com.aswl.as.common.core.utils.IdGen;
import com.aswl.as.asos.common.utils.Query;
import com.aswl.as.asos.common.utils.PageUtils;
import com.aswl.as.asos.common.annotation.SysLog;
import com.aswl.as.asos.datasource.annotation.DataSource;
import com.aswl.as.asos.modules.ibrs.entity.AsContentProduct;
import com.aswl.as.asos.modules.ibrs.mapper.AsContentProductMapper;
import com.aswl.as.asos.modules.ibrs.service.IAsContentProductService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 产品中心表 服务实现类
 * </p>
 *
 * @author hfx
 * @since 2020-03-04
 */
@Service
public class AsContentProductServiceImpl extends ServiceImpl<AsContentProductMapper, AsContentProduct> implements IAsContentProductService {

    // 最大的橱窗推荐数量
    @Value("${asOsContent.showcaseMaxCount.product}")
    private Integer productShowcaseMaxCount;

    @DataSource("slave1")
    public PageUtils queryPage(Map<String, Object> params)
    {
        /*
        IPage<AsContentProduct> page = this.page(
            new Query<AsContentProduct>().getPage(params),
                new QueryWrapper<AsContentProduct>()
            );
        */

        // 修改成用sql查出来的东西
        IPage<AsContentProduct> page=new Query<AsContentProduct>().getPage(params);

        Map<String, Object> temp=new HashMap<>(params);
        temp.put(Constant.PAGE,null);
        temp.put(Constant.LIMIT,null);


        temp.get(Constant.ORDER_FIELD);
        temp.get(Constant.ORDER);


        int totalCount=baseMapper.countProducts(temp);
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


            List<AsContentProduct>list=baseMapper.queryProducts(temp);
            for(AsContentProduct p:list)
            {
                p.setUrl("/contentProduct/html/"+p.getId());
            }
            page.setRecords(list);
        }



        return new PageUtils(page);
    }

    @DataSource("slave1")
    public List<AsContentProduct> findList(AsContentProduct entity)
    {
        return list(new QueryWrapper<AsContentProduct>());
    }

    //根据id返回对应信息
    @DataSource("slave1")
    public AsContentProduct getEntityById(String id)
    {
        return this.getById(id);
    }

    @DataSource("slave1")
    public boolean saveEntity(AsContentProduct entity)
    {
        entity.setId(IdGen.snowflakeId());

        entity.setIsShowcase(0);
        entity.setIsRelease(0);
        entity.setClicks(0);
        entity.setDelFlag(0);

        entity.setSort(getMaxSort()+1);
        return this.save(entity);
    }

    @DataSource("slave1")
    public boolean updateEntityById(AsContentProduct entity)
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
    public boolean setShowcase(AsContentProduct entity)
    {
        if(entity.getIsShowcase()==1)
        {
            List<AsContentProduct> list= baseMapper.selectList(new QueryWrapper<AsContentProduct>().eq("is_showcase","1"));
            int count=0;
            for(AsContentProduct temp:list)
            {
                if(!temp.getId().equals(entity.getId()))
                {
                    count++;
                }
            }

            if(count+1>productShowcaseMaxCount)
            {
                throw new RRException("当前分类只能设置"+productShowcaseMaxCount+"个橱窗，已超出设置数量。");
            }
        }

        return this.updateById(entity);
    }

    //发布和取消发布
    @DataSource("slave1")
    public boolean setIsRelease(AsContentProduct entity,String[] ids)
    {
        return baseMapper.update(entity,new QueryWrapper<AsContentProduct>().in("id",ids))>0;
    }

    // 添加上移 下移 和 置顶功能--------------------------------------------------

    @DataSource("slave1")
    public boolean moveUp(AsContentProduct entity)
    {
        return changeSort(entity,true);
    }

    @DataSource("slave1")
    public boolean moveDown(AsContentProduct entity)
    {
        return changeSort(entity,false);
    }

    @DataSource("slave1")
    public boolean sticky(AsContentProduct entity)
    {
        entity.setSort(getMaxSort()+1);
        return baseMapper.updateById(entity)>0;
    }

    public boolean changeSort(AsContentProduct entity,boolean isUp)
    {
        AsContentProduct p=getSort(entity.getSort(),isUp);
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
        AsContentProduct r1 =baseMapper.selectOne(new QueryWrapper<AsContentProduct>().orderByDesc("sort").last(" limit 1"));
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
    public AsContentProduct getSort(Integer sort,boolean isUp)
    {
        AsContentProduct r1 =baseMapper.selectOne(new QueryWrapper<AsContentProduct>()
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
