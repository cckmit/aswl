package com.aswl.as.asos.modules.asuser.service.impl;

import com.aswl.as.common.core.utils.IdGen;
import com.aswl.as.asos.common.utils.Query;
import com.aswl.as.asos.common.utils.PageUtils;
import com.aswl.as.asos.datasource.annotation.DataSource;
import com.aswl.as.asos.modules.asuser.entity.AsUserSysDept;
import com.aswl.as.asos.modules.asuser.mapper.AsUserSysDeptMapper;
import com.aswl.as.asos.modules.asuser.service.IAsUserSysDeptService;
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
 * 部门表 服务实现类
 * </p>
 *
 * @author hfx
 * @since 2019-11-21
 */
@Service
public class AsUserSysDeptServiceImpl extends ServiceImpl<AsUserSysDeptMapper, AsUserSysDept> implements IAsUserSysDeptService {

    @DataSource("slave2")
    public PageUtils queryPage(Map<String, Object> params)
    {
        IPage<AsUserSysDept> page = this.page(
            new Query<AsUserSysDept>().getPage(params),
                new QueryWrapper<AsUserSysDept>()
            );

        return new PageUtils(page);
    }

    //根据id返回对应信息
    @DataSource("slave2")
    public AsUserSysDept getEntityById(String id)
    {
        return this.getById(id);
    }

    @DataSource("slave2")
    public boolean saveEntity(AsUserSysDept entity)
    {
        entity.setId(IdGen.snowflakeId());
        //设置节点
        return this.save(entity);
    }

    @DataSource("slave2")
    public boolean updateEntityById(AsUserSysDept entity)
    {
        return this.updateById(entity);
    }

    @DataSource("slave2")
    public boolean deleteEntityById(String id)
    {
        return this.removeById(id);
    }

    @DataSource("slave2")
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
    @DataSource("slave2")
    public void generateDeptCode(AsUserSysDept dept) {
        //查询父级节点是否存在
        AsUserSysDept d1 = baseMapper.findDeptByParentId("-1");
        if (StringUtils.isEmpty(dept.getParentId())) {
            if (null == d1) {
                dept.setParentId("-1");
                dept.setDeptCode("A1");
            } else {
                dept.setParentId("-1");
                dept.setDeptCode(NumberUtil.addOne(d1.getDeptCode()));
            }
        }
        if (StringUtils.isNotEmpty(dept.getParentId())) {
            AsUserSysDept d2 = baseMapper.findDeptByParentId(dept.getParentId());
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
