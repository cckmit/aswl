package com.aswl.as.asos.modules.ibrs.service.impl;

import com.aswl.as.common.core.utils.IdGen;
import com.aswl.as.asos.common.utils.Query;
import com.aswl.as.asos.common.utils.PageUtils;
import com.aswl.as.asos.common.annotation.SysLog;
import com.aswl.as.asos.datasource.annotation.DataSource;
import com.aswl.as.asos.modules.ibrs.entity.AsContentNotice;
import com.aswl.as.asos.modules.ibrs.mapper.AsContentNoticeMapper;
import com.aswl.as.asos.modules.ibrs.service.IAsContentNoticeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 系统消息表 服务实现类
 * </p>
 *
 * @author hfx
 * @since 2020-03-12
 */
@Service
public class AsContentNoticeServiceImpl extends ServiceImpl<AsContentNoticeMapper, AsContentNotice> implements IAsContentNoticeService {

    @DataSource("slave1")
    public PageUtils queryPage(Map<String, Object> params)
    {

        String content = (String)params.get("content");
        String isRelease=(String)params.get("isRelease");

        IPage<AsContentNotice> page = this.page(
            new Query<AsContentNotice>().getPage(params),
                new QueryWrapper<AsContentNotice>()
                        .like(StringUtils.isNotBlank(content),"content",content)
                        .and("1".equals(isRelease), wrapper -> wrapper.like("is_release",isRelease).or()
                                .le("release_time",new Date()))
                        .eq("0".equals(isRelease),"is_release",isRelease)
            );

        // 有个定时发布的话，如果按照定时发布，就到时候查出来的列表过滤一下
        List<AsContentNotice> list=page.getRecords();
        LocalDateTime now=LocalDateTime.now();
        Integer notRelease=0;
        for(AsContentNotice n:list)
        {
            if(notRelease.equals(n.getIsRelease()) && now.isAfter(n.getReleaseTime()) )
            {
                n.setIsRelease(1);
                this.updateById(n);
            }
        }

        return new PageUtils(page);
    }

    @DataSource("slave1")
    public List<AsContentNotice> findList(AsContentNotice entity)
    {

        return list(
                new QueryWrapper<AsContentNotice>()
                        .like(StringUtils.isNotBlank(entity.getContent()),"content",entity.getContent())
                        .eq(entity.getIsRelease()!=null,"is_release",entity.getIsRelease())
        );
    }

    //根据id返回对应信息
    @DataSource("slave1")
    public AsContentNotice getEntityById(String id)
    {
        return this.getById(id);
    }

    @DataSource("slave1")
    public boolean saveEntity(AsContentNotice entity)
    {
        entity.setId(IdGen.snowflakeId());
        return this.save(entity);
    }

    @DataSource("slave1")
    public boolean updateEntityById(AsContentNotice entity)
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
