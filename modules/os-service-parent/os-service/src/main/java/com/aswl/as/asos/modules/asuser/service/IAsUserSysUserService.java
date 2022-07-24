package com.aswl.as.asos.modules.asuser.service;

import com.aswl.as.asos.modules.asuser.entity.AsUserSysUser;
import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.vo.UserVo;
import com.aswl.as.user.api.dto.UserDto;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aswl.as.asos.common.utils.PageUtils;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

/**
 * <p>
 * 用户信息表 服务类
 * </p>
 *
 * @author hfx
 * @since 2019-11-22
 */
public interface IAsUserSysUserService extends IService<AsUserSysUser> {

    public PageUtils queryPage(Map<String, Object> params);

    public boolean saveEntity(AsUserSysUser entity);

    public boolean updateEntityById(AsUserSysUser entity);

    public boolean deleteEntityById(String id);

    public boolean deleteEntityByIds(String[] ids);

    public ResponseBean<PageInfo<UserDto>> osUser1(String pageNum, String pageSize, String sort, String order, String name, String deptId, String deptCode, String regionCode, UserVo userVo);

    public ResponseBean<Boolean> checkExist( String identifier);
    
    


}
