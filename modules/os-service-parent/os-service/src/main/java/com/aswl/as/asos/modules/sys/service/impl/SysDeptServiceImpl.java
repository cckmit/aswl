package com.aswl.as.asos.modules.sys.service.impl;

import com.aswl.as.asos.modules.sys.service.SysUserService;
import com.aswl.as.common.core.utils.IdGen;
import com.aswl.as.asos.common.utils.Query;
import com.aswl.as.asos.common.utils.PageUtils;
import com.aswl.as.asos.modules.sys.entity.SysDeptEntity;
import com.aswl.as.asos.modules.sys.dao.SysDeptDao;
import com.aswl.as.asos.modules.sys.service.SysDeptService;
import com.aswl.as.common.core.utils.NumberUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 部门表 服务实现类
 * </p>
 *
 * @author hfx
 * @since 2019-12-09
 */
@Service
public class SysDeptServiceImpl extends ServiceImpl<SysDeptDao, SysDeptEntity> implements SysDeptService {

    @Autowired
    private SysUserService sysUserService;

    public PageUtils queryPage(Map<String, Object> params)
    {
        /*
        String projectName = (String)params.get("projectName");
        String projectDes = (String)params.get("projectDes");
        String projectOwner = (String)params.get("projectOwner");
        */

        IPage<SysDeptEntity> page = this.page(
            new Query<SysDeptEntity>().getPage(params),
                new QueryWrapper<SysDeptEntity>()
                /*
                .like(StringUtils.isNotBlank(projectName),"project_name",projectName)
                .like(StringUtils.isNotBlank(projectDes),"project_des",projectDes)
                .like(StringUtils.isNotBlank(projectOwner),"project_owner",projectOwner)
                */
            );

        return new PageUtils(page);
    }

    public List<SysDeptEntity> findList(SysDeptEntity entity)
    {
        return list(new QueryWrapper<SysDeptEntity>());
    }

    //根据id返回对应信息
    public SysDeptEntity getEntityById(String id)
    {
        return this.getById(id);
    }

    public boolean saveEntity(SysDeptEntity entity)
    {
        //entity.setProjectId(IdGen.snowflakeId());
        entity.setId(IdGen.snowflakeId());
        generateDeptCode(entity);
        return this.save(entity);
    }

    public boolean updateEntityById(SysDeptEntity entity)
    {
        generateDeptCode(entity);
        //更新对应的用户表的deptCode
        sysUserService.updateDeptCode(entity.getId(),entity.getDeptCode());

        return this.updateById(entity);
    }

    public boolean deleteEntityById(String id)
    {
        return this.removeById(id);
    }

    public boolean deleteEntityByIds(String[] ids)
    {
        for(String id:ids)
        {
            this.removeById(id);
        }
        return true;
    }

    /**
     * 自动生成部门编码
     * @param dept 部门对象
     */
    public void generateDeptCode(SysDeptEntity dept) {
        //查询父级节点是否存在

        //修改时先判断是否还在原来的父部门中,直接返回不用重新生成了
        if(!StringUtils.isEmpty(dept.getId()))
        {
            SysDeptEntity old =baseMapper.selectById(dept.getId());
            if(old!=null && old.getParentId()!=null && old.getParentId().equals(dept.getParentId()) )
            {
                return;
            }
        }

        //SysDeptEntity d1 = baseMapper.findDeptByParentId("-1"); //"parent_id"
        //SysDeptEntity d1 =baseMapper.selectOne(new QueryWrapper<SysDeptEntity>().eq("parent_id","-1"));
        SysDeptEntity d1=baseMapper.selectOne(new QueryWrapper<SysDeptEntity>().eq("parent_id","-1").orderByDesc("create_date").last(" limit 1"));

        if ("-1".equals(dept.getParentId())) {
            if (null == d1) {
                dept.setParentId("-1");
                dept.setDeptCode("A1");
            } else {
                dept.setParentId("-1");
                dept.setDeptCode(NumberUtil.addOne(d1.getDeptCode()));
            }
        }
        else{
            //SysDeptEntity d2 = baseMapper.findDeptByParentId(dept.getParentId());
            //SysDeptEntity d2 =baseMapper.selectOne(new QueryWrapper<SysDeptEntity>().eq("parent_id",dept.getParentId()));

            SysDeptEntity d2=baseMapper.selectOne(new QueryWrapper<SysDeptEntity>().eq("parent_id",dept.getParentId()).orderByDesc("create_date").last(" limit 1"));

            if (null == d2) {
                if (dept.getDeptCode().length() == 2) {
                    dept.setDeptCode(dept.getDeptCode()+"B1");
                } else {
                    dept.setDeptCode(dept.getDeptCode() + "C1");
                }
            } else {
                dept.setDeptCode(NumberUtil.addOne(d2.getDeptCode()));
            }
        }
    }


}
