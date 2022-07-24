package com.aswl.as.asos.modules.asuser.service.impl;

import com.aswl.as.asos.common.utils.Query;
import com.aswl.as.asos.common.utils.PageUtils;
import com.aswl.as.asos.datasource.annotation.DataSource;
import com.aswl.as.asos.modules.asuser.entity.AsUserSysUser;
import com.aswl.as.asos.modules.asuser.mapper.AsUserSysUserMapper;
import com.aswl.as.asos.modules.asuser.service.IAsUserSysUserService;
import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.utils.IdGen;
import com.aswl.as.common.core.vo.OsVo;
import com.aswl.as.common.core.vo.UserVo;
import com.aswl.as.user.api.dto.UserDto;
import com.aswl.as.user.api.feign.UserServiceClient;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageInfo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import java.util.Map;

/**
 * <p>
 * 用户信息表 服务实现类
 * </p>
 *
 * @author hfx
 * @since 2019-11-22
 */
@Service
@AllArgsConstructor
public class AsUserSysUserServiceImpl extends ServiceImpl<AsUserSysUserMapper, AsUserSysUser> implements IAsUserSysUserService {

    UserServiceClient userServiceClient;

    @DataSource("slave2")
    public PageUtils queryPage(Map<String, Object> params)
    {
        /*
        String projectName = (String)params.get("projectName");
        String projectDes = (String)params.get("projectDes");
        String projectOwner = (String)params.get("projectOwner");
        */

        IPage<AsUserSysUser> page = this.page(
            new Query<AsUserSysUser>().getPage(params),
                new QueryWrapper<AsUserSysUser>()
                /*
                .like(StringUtils.isNotBlank(projectName),"project_name",projectName)
                .like(StringUtils.isNotBlank(projectDes),"project_des",projectDes)
                .like(StringUtils.isNotBlank(projectOwner),"project_owner",projectOwner)
                */
            );

        return new PageUtils(page);
    }

    //根据id返回对应信息
    @DataSource("slave2")
    public AsUserSysUser getEntityById(String id)
    {
        return this.getById(id);
    }

    @DataSource("slave2")
    public boolean saveEntity(AsUserSysUser entity)
    {
//        entity.setProjectId(IdGen.snowflakeId());
        entity.setId(IdGen.snowflakeId());
        return this.save(entity);
    }

    @DataSource("slave2")
    public boolean updateEntityById(AsUserSysUser entity)
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

    public ResponseBean<PageInfo<UserDto>> osUser1(String pageNum, String pageSize, String sort, String order, String name, String deptId, String deptCode, String regionCode, UserVo userVo)
    {
        return userServiceClient.osUser1(pageNum,pageSize,sort,order,name,deptId,deptCode,regionCode, OsVo.getRandomStr(),userVo);
    }

    @Override
    public ResponseBean<Boolean> checkExist(String identifier) {
        return userServiceClient.checkExist(identifier);
    }


}
