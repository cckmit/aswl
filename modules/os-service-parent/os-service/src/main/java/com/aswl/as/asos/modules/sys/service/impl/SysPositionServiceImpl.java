package com.aswl.as.asos.modules.sys.service.impl;

import com.aswl.as.asos.common.utils.Constant;
import com.aswl.as.asos.modules.sys.entity.SysUserEntity;
import com.aswl.as.common.core.constant.CommonConstant;
import com.aswl.as.common.core.utils.IdGen;
import com.aswl.as.asos.common.utils.Query;
import com.aswl.as.asos.common.utils.PageUtils;
import com.aswl.as.asos.datasource.annotation.DataSource;
import com.aswl.as.asos.modules.sys.entity.SysPositionEntity;
import com.aswl.as.asos.modules.sys.dao.SysPositionDao;
import com.aswl.as.asos.modules.sys.service.SysPositionService;
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
 * 职位表 服务实现类
 * </p>
 *
 * @author hfx
 * @since 2019-12-17
 */
@Service
public class SysPositionServiceImpl extends ServiceImpl<SysPositionDao, SysPositionEntity> implements SysPositionService {

    public PageUtils queryPage(Map<String, Object> params)
    {
        String positionName = (String)params.get("positionName");

        /*
        IPage<SysPositionEntity> page = this.page(
            new Query<SysPositionEntity>().getPage(params),
                new QueryWrapper<SysPositionEntity>().eq("del_flag",CommonConstant.DEL_FLAG_NORMAL)
                .like(StringUtils.isNotBlank(positionName),"position_name",positionName)
            );

        return new PageUtils(page);
         */

        // 修改成用sql查出来的东西
        IPage<SysPositionEntity> page=new Query<SysPositionEntity>().getPage(params);

        Map<String, Object> temp=new HashMap<>(params);
        temp.put(Constant.PAGE,null);
        temp.put(Constant.LIMIT,null);

        int totalCount=baseMapper.countPositions(temp);
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

            List<SysPositionEntity>list=baseMapper.queryPositions(temp);
            page.setRecords(list);
        }

        return new PageUtils(page);
    }

    public List<SysPositionEntity> findList(SysPositionEntity entity)
    {
        /*
        return list(new QueryWrapper<SysPositionEntity>().eq("del_flag",CommonConstant.DEL_FLAG_NORMAL)
                .like(StringUtils.isNotBlank(entity.getPositionName()),"position_name",entity.getPositionName()));

         */
        Map<String, Object> temp=new HashMap<>();
        return baseMapper.queryPositions(temp);
    }

    //根据id返回对应信息
    public SysPositionEntity getEntityById(String id)
    {
        return this.getById(id);
    }

    public boolean saveEntity(SysPositionEntity entity)
    {
//        entity.setProjectId(IdGen.snowflakeId());
        entity.setDelFlag(CommonConstant.DEL_FLAG_NORMAL);
        entity.setPositionId(IdGen.snowflakeId());
        return this.save(entity);
    }

    public boolean updateEntityById(SysPositionEntity entity)
    {
        return this.updateById(entity);
    }

    public boolean deleteEntityById(String id)
    {
        return deleteEntityByIds(new String[]{id});
    }

    public boolean deleteEntityByIds(String[] ids)
    {
        //设置为删除
        SysPositionEntity entity=new SysPositionEntity();
        entity.setDelFlag(CommonConstant.DEL_FLAG_DEL);
        return this.update(entity,
                new QueryWrapper<SysPositionEntity>().in("position_id", ids));
    }


}
