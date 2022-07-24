package com.aswl.as.asos.modules.ibrs.service.impl;

import com.aswl.as.asos.modules.ibrs.entity.AsRegion;
import com.aswl.as.asos.modules.sys.entity.SysDeptEntity;
import com.aswl.as.common.core.utils.IdGen;
import com.aswl.as.asos.common.utils.Query;
import com.aswl.as.asos.common.utils.PageUtils;
import com.aswl.as.asos.common.annotation.SysLog;
import com.aswl.as.asos.datasource.annotation.DataSource;
import com.aswl.as.asos.modules.ibrs.entity.AsCategoryTree;
import com.aswl.as.asos.modules.ibrs.mapper.AsCategoryTreeMapper;
import com.aswl.as.asos.modules.ibrs.service.IAsCategoryTreeService;
import com.aswl.as.common.core.utils.NumberUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 通用分类树表，普通的树可以直接用一个type辨别来获取 服务实现类
 * </p>
 *
 * @author hfx
 * @since 2020-03-03
 */
@Service
public class AsCategoryTreeServiceImpl extends ServiceImpl<AsCategoryTreeMapper, AsCategoryTree> implements IAsCategoryTreeService {

    @DataSource("slave1")
    public PageUtils queryPage(Map<String, Object> params)
    {
        Integer category = (Integer)params.get("category");

        IPage<AsCategoryTree> page = this.page(
            new Query<AsCategoryTree>().getPage(params),
                new QueryWrapper<AsCategoryTree>()
                .eq(category!=null,"category",category)
            );

        return new PageUtils(page);
    }

    @DataSource("slave1")
    public List<AsCategoryTree> findList(AsCategoryTree entity)
    {
        return list(new QueryWrapper<AsCategoryTree>().eq(
                entity.getCategory()!=null,"category",entity.getCategory()
        ));
    }

    //根据id返回对应信息
    @DataSource("slave1")
    public AsCategoryTree getEntityById(String id)
    {
        return this.getById(id);
    }

    @DataSource("slave1")
    public boolean saveEntity(AsCategoryTree entity)
    {
        entity.setId(IdGen.snowflakeId());
        generateEntityCode(entity);
        return this.save(entity);
    }

    @DataSource("slave1")
    public boolean updateEntityById(AsCategoryTree entity)
    {
        generateEntityCode(entity);
        //TODO 如果修改了分类，就把下面的分类全部更新

        //TODO 这里如果修改了的话，就把对应的文章的分类修改
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


    /**
     * 自动生成区域编码
     * @param entity 区域对象
     */
    private void generateEntityCode(AsCategoryTree entity) {
        if(!StringUtils.isEmpty(entity.getId()))
        {
            //判断是否跟原来的那个上级区域一样，如果是一样，就不重复生成了
            AsCategoryTree temp=baseMapper.selectById(entity.getId());
            if(temp!=null && temp.getParentId()!=null && temp.getParentId().equals(entity.getParentId()))
            {
                return;
            }
        }

        // 修改获取数据的函数
        AsCategoryTree r1 =baseMapper.selectOne(new QueryWrapper<AsCategoryTree>().eq("parent_id","-1").orderByDesc("create_date").orderByDesc("id").last(" limit 1"));
        if ("-1".equals(entity.getParentId())) {
            if (r1 == null) {
                entity.setParentId("-1");
                entity.setNodeCode("A01");
            } else {
                entity.setParentId("-1");
//                entity.setNodeCode(NumberUtil.addOne(r1.getNodeCode()));
                entity.setNodeCode(NumberUtil.addOneNum(r1.getNodeCode()));
            }
        }else{
            AsCategoryTree r2 =baseMapper.selectOne(new QueryWrapper<AsCategoryTree>().eq("parent_id",entity.getParentId()).orderByDesc("create_date").orderByDesc("id").last(" limit 1"));
            if (r2==null){
                entity.setNodeCode(entity.getNodeCode()+"A01");
            }else{
//                entity.setNodeCode(NumberUtil.addOne(r2.getNodeCode()));
                entity.setNodeCode(NumberUtil.addOneNum(r2.getNodeCode()));
            }
        }
    }


}
