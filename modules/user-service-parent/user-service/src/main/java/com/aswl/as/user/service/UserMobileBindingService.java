package com.aswl.as.user.service;
import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.common.core.utils.SysUtil;
import com.aswl.as.ibrs.api.module.AlarmType;
import com.aswl.as.user.api.dto.UserMobileBindingDto;
import com.aswl.as.user.api.module.UserMobileBinding;
import com.aswl.as.user.mapper.UserMobileBindingMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@Slf4j
@Service
public class UserMobileBindingService extends CrudService<UserMobileBindingMapper, UserMobileBinding> {
    private final UserMobileBindingMapper userMobileBindingMapper;

    /**
     * 新增手机端登录授权
     *
     * @param userMobileBindingDto
     * @return int
     */
    @Transactional
    public int insert(UserMobileBindingDto userMobileBindingDto) {
        int update;
        String mobileMeid = userMobileBindingDto.getMobileMeid();
        int count = userMobileBindingMapper.findByMobileMeid(mobileMeid);
        if (count == 0) {
            UserMobileBinding userMobileBinding = new UserMobileBinding();
            BeanUtils.copyProperties(userMobileBindingDto, userMobileBinding);
            userMobileBinding.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
            userMobileBinding.setLoginTime(new Date());
           update =  userMobileBindingMapper.insert(userMobileBinding);
        }else{
            update = userMobileBindingMapper.updateByMobileMeid(mobileMeid);
        }
        return  update;
    }

    /**
     * 删除手机端登录授权
     *
     * @param userMobileBinding
     * @return int
     */
    @Transactional
    @Override
    public int delete(UserMobileBinding userMobileBinding) {
        return userMobileBindingMapper.delete(userMobileBinding);
    }

    /**
     * 批量修改手机端登录授权
     * @param list
     * @return int
     */
    @Transactional
    public int updateBath(List<UserMobileBinding> list) {
        int update = 0;
        for (UserMobileBinding u: list) {
            userMobileBindingMapper.update(u);
            update ++;
        }
        return update;
    }
}