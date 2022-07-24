package com.aswl.as.asos.modules.sys.service.impl;

import com.aswl.as.common.core.constant.CommonConstant;
import com.aswl.as.common.core.utils.IdGen;
import com.aswl.as.asos.common.utils.Query;
import com.aswl.as.asos.common.utils.PageUtils;
import com.aswl.as.asos.modules.sys.entity.SysBlackListEntity;
import com.aswl.as.asos.modules.sys.dao.SysBlackListDao;
import com.aswl.as.asos.modules.sys.service.SysBlackListService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 系统IP黑名单 服务实现类
 * </p>
 *
 * @author hfx
 * @since 2019-12-17
 */
@Service
public class SysBlackListServiceImpl extends ServiceImpl<SysBlackListDao, SysBlackListEntity> implements SysBlackListService {

    public PageUtils queryPage(Map<String, Object> params)
    {

        String ip=(String)params.get("ip");

        IPage<SysBlackListEntity> page = this.page(
            new Query<SysBlackListEntity>().getPage(params),
                new QueryWrapper<SysBlackListEntity>().eq("del_flag", CommonConstant.DEL_FLAG_NORMAL)
                        .like(StringUtils.isNotBlank(ip),"ip",ip)
            );

        return new PageUtils(page);
    }

    public List<SysBlackListEntity> findList(SysBlackListEntity entity)
    {
        return list(new QueryWrapper<SysBlackListEntity>().eq("del_flag", CommonConstant.DEL_FLAG_NORMAL)
                .like(StringUtils.isNotBlank(entity.getIp()),"ip",entity.getIp()));
    }

    //根据id返回对应信息
    public SysBlackListEntity getEntityById(String id)
    {
        return this.getById(id);
    }

    public boolean saveEntity(SysBlackListEntity entity)
    {
        entity.setId(IdGen.snowflakeId());
        entity.setDelFlag(CommonConstant.DEL_FLAG_NORMAL);
        return this.save(entity);
    }

    public boolean updateEntityById(SysBlackListEntity entity)
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
//        for(String id:ids)
//        {
//            this.removeById(id);
//        }
//        return true;
        SysBlackListEntity entity=new SysBlackListEntity();
        entity.setDelFlag(CommonConstant.DEL_FLAG_DEL);
        return this.update(entity,
                new QueryWrapper<SysBlackListEntity>().in("id", ids));
    }

    public boolean isBlackList(String ip)
    {
        return list(new QueryWrapper<SysBlackListEntity>().eq("del_flag", CommonConstant.DEL_FLAG_NORMAL)
                .like(StringUtils.isNotBlank(ip),"ip",ip)).size()>0;
    }


}
