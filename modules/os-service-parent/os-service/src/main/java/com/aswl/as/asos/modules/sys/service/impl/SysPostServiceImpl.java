package com.aswl.as.asos.modules.sys.service.impl;

import com.aswl.as.common.core.constant.CommonConstant;
import com.aswl.as.common.core.utils.IdGen;
import com.aswl.as.asos.common.utils.Query;
import com.aswl.as.asos.common.utils.PageUtils;
import com.aswl.as.asos.modules.sys.entity.SysPostEntity;
import com.aswl.as.asos.modules.sys.dao.SysPostDao;
import com.aswl.as.asos.modules.sys.service.SysPostService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 工作岗位 服务实现类
 * </p>
 *
 * @author hfx
 * @since 2019-12-17
 */
@Service
public class SysPostServiceImpl extends ServiceImpl<SysPostDao, SysPostEntity> implements SysPostService {

    public PageUtils queryPage(Map<String, Object> params)
    {
        String postName=(String)params.get("postName");

        IPage<SysPostEntity> page = this.page(
            new Query<SysPostEntity>().getPage(params),
            new QueryWrapper<SysPostEntity>().eq("del_flag",CommonConstant.DEL_FLAG_NORMAL)
                .like(StringUtils.isNotBlank(postName),"post_name",postName)
            );

        return new PageUtils(page);
    }

    public List<SysPostEntity> findList(SysPostEntity entity)
    {
        return list(new QueryWrapper<SysPostEntity>().eq("del_flag",CommonConstant.DEL_FLAG_NORMAL)
                .like(StringUtils.isNotBlank(entity.getPostName()),"post_name",entity.getPostName()));
    }

    //根据id返回对应信息
    public SysPostEntity getEntityById(String id)
    {
        return this.getById(id);
    }

    public boolean saveEntity(SysPostEntity entity)
    {
        entity.setPostId(IdGen.snowflakeId());
        entity.setDelFlag(CommonConstant.DEL_FLAG_NORMAL);
        return this.save(entity);
    }

    public boolean updateEntityById(SysPostEntity entity)
    {
        return this.updateById(entity);
    }

    public boolean deleteEntityById(String id)
    {
        return deleteEntityByIds(new String[]{id});
//        return this.removeById(id);
    }

    public boolean deleteEntityByIds(String[] ids)
    {
        //设置为删除
        SysPostEntity entity=new SysPostEntity();
        entity.setDelFlag(CommonConstant.DEL_FLAG_DEL);
        return this.update(entity,
                new QueryWrapper<SysPostEntity>().in("post_id", ids));
    }


}
