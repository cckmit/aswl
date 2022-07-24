package com.aswl.as.user.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.alibaba.fastjson.JSON;
import com.aswl.as.common.core.constant.CommonConstant;
import com.aswl.as.common.core.constant.MqConstant;
import com.aswl.as.common.core.enums.LoginType;
import com.aswl.as.common.core.enums.RoleEnum;
import com.aswl.as.common.core.exceptions.CommonException;
import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.properties.SysProperties;
import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.common.core.utils.DateUtils;
import com.aswl.as.common.core.utils.IdGen;
import com.aswl.as.common.core.utils.SysUtil;
import com.aswl.as.common.core.vo.OsVo;
import com.aswl.as.common.core.vo.UserVo;
import com.aswl.as.common.security.constant.SecurityConstant;
import com.aswl.as.ibrs.api.dto.RegionLeaderDto;
import com.aswl.as.ibrs.api.feign.RegionServiceClient;
import com.aswl.as.ibrs.api.module.Region;
import com.aswl.as.ibrs.api.module.RegionLeader;
import com.aswl.as.user.api.constant.AttachmentConstant;
import com.aswl.as.user.api.constant.MenuConstant;
import com.aswl.as.user.api.constant.RoleConstant;
import com.aswl.as.user.api.dto.UserDto;
import com.aswl.as.user.api.dto.UserInfoDto;
import com.aswl.as.user.api.dto.DutyUserDto;
import com.aswl.as.user.api.enums.IdentityType;
import com.aswl.as.user.api.module.*;
import com.aswl.as.user.mapper.*;
import com.aswl.as.user.mq.MQSender;
import com.aswl.as.user.utils.UserUtils;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 用户service实现
 *
 * @author aswl.com
 * @date 2018-08-25 16:17
 */
@AllArgsConstructor
@Slf4j
@Service
public class UserService extends CrudService<UserMapper, User> {

    private static final PasswordEncoder encoder = new BCryptPasswordEncoder();

    private final UserRoleMapper userRoleMapper;

    private final RoleMapper roleMapper;

    private final MenuService menuService;

    private final RedisTemplate redisTemplate;

    private final AttachmentService attachmentService;

    private final SysProperties sysProperties;

    private final UserAuthsService userAuthsService;

    private final RegionServiceClient regionServiceClient;

    private final TenantMapper tenantMapper;
    
    private  final UserMapper userMapper;
    
    private MQSender mqSender;


    /**
     * 新建用户
     *
     * @param userDto userDto
     * @return int
     * @author aswl.com
     * @date 2019/07/03 12:17:44
     */
    @Transactional
    public int createUser(UserDto userDto) {
        int update;
        User user = new User();
        BeanUtils.copyProperties(userDto, user);
        // 先保存用户基本信息
        user.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode(),user.getProjectId());
        user.setSysRole(RoleEnum.getByRoleCode(user.getRoleList().get(0).getRoleCode()));
        // 保存父子账号关系
        UserVo currentUser = this.findUserByIdentifier(userDto.getIdentityType(), SysUtil.getUser(), SysUtil.getTenantCode());
        user.setParentUid(currentUser.getId());
        if ((update = this.insert(user)) > 0) {
            // 保存用户授权信息
            UserAuths userAuths = new UserAuths();
            userAuths.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
            userAuths.setUserId(user.getId());
            userAuths.setIdentifier(userDto.getIdentifier());
            if (userDto.getIdentityType() == null)
                userAuths.setIdentityType(IdentityType.PASSWORD.getValue());
            // 默认密码为123456
            if (StringUtils.isBlank(userDto.getCredential()))
                userDto.setCredential(CommonConstant.DEFAULT_PASSWORD);
            userAuths.setCredential(encoder.encode(userDto.getCredential()));
            update = userAuthsService.insert(userAuths);
        }
        return update;
    }

    /**
     * 新增用户
     *
     * @param user user
     * @return int
     * @author aswl.com
     * @date 2018/10/30 12:43
     */
    @Override
    @Transactional
    public int insert(User user) {
        // 保存角色
        if (CollectionUtils.isNotEmpty(user.getRole())) {
            user.getRole().forEach(roleId -> {
                UserRole sysUserRole = new UserRole();
                sysUserRole.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode());
                sysUserRole.setUserId(user.getId());
                sysUserRole.setRoleId(roleId);
                // 保存角色
                userRoleMapper.insert(sysUserRole);
            });
        }
        return super.insert(user);
    }

    /**
     * 获取用户信息，包括头像、角色、权限信息
     *
     * @param userVo userVo
     * @return User
     * @author aswl.com
     * @date 2018/9/11 23:44
     */
    public UserInfoDto findUserInfo(UserVo userVo) {
        // 返回结果
        UserInfoDto userInfoDto = new UserInfoDto();
        String tenantCode = userVo.getTenantCode(), identifier = userVo.getIdentifier();
        // 根据唯一标识查询账号信息
        UserAuths userAuths = new UserAuths();
        if (userVo.getIdentityType() != null)
            userAuths.setIdentityType(userVo.getIdentityType());
        userAuths.setIdentifier(userVo.getIdentifier());
        userAuths = userAuthsService.getByIdentifier(userAuths);
        if (userAuths == null)
            throw new CommonException("账号" + identifier + "不存在.");
        // 根据用户id查询用户详细信息
        User user = new User();
        user.setId(userAuths.getUserId());
        user = this.get(user);
        if (user == null)
            throw new CommonException("查询用户信息失败.");
        // 查询用户的角色信息
        List<Role> roles = this.getUserRoles(user);
        //角色列表
        user.setRoleList(roles);
        // 根据角色查询权限
        List<String> permissions = this.getUserPermissions(user, identifier, roles);
        userInfoDto.setRoleIds(roles.stream().map(Role::getId).toArray(String[]::new));
        userInfoDto.setRoles(roles.stream().map(Role::getRoleCode).toArray(String[]::new));
        userInfoDto.setRoleNames(roles.stream().map(Role::getRoleName).toArray(String [] ::new));
        userInfoDto.setPermissions(permissions.toArray(new String[0]));
        // 获取租户名称
        Tenant tenant = tenantMapper.getByTenantCode(tenantCode);
        userInfoDto.setTenantName(tenant.getTenantName());
        //查询是否为区域负责人
        ResponseBean<RegionLeader> regionLeader = regionServiceClient.getRegionLeaderByUserId(userAuths.getUserId());
        if (regionLeader != null && regionLeader.getStatus() == 200 && regionLeader.getData()!= null){
            userInfoDto.setMaintainUserId(regionLeader.getData().getId());
        }
        UserUtils.toUserInfoDto(userInfoDto, user, userAuths);
        // 头像信息
        // this.initUserAvatar(userInfoDto, user);
        return userInfoDto;
    }

    /**
     * 查询用户权限数组
     *
     * @param user       user
     * @param identifier identifier
     * @return List
     * @author aswl.com
     * @date 2019/07/04 00:12:44
     */
    public List<String> getUserPermissions(User user, String identifier) {
        return this.getUserPermissions(user, identifier, this.getUserRoles(user));
    }

    /**
     * 根据指定角色集合，查询用户权限数组
     *
     * @param user       user
     * @param identifier identifier
     * @param roles      roles
     * @return List
     * @author aswl.com
     * @date 2019/07/04 00:14:44
     */
    public List<String> getUserPermissions(User user, String identifier, List<Role> roles) {
        // 用户权限
        List<String> permissions = new ArrayList<>();
        List<Menu> menuList = new ArrayList<>();
        // 管理员
        if (UserUtils.isAdmin(identifier)) {
            Menu menu = new Menu();
            menu.setTenantCode(user.getTenantCode());
            menu.setApplicationCode(user.getApplicationCode());
            menu.setType(MenuConstant.MENU_TYPE_PERMISSION);
            menuList = menuService.findAllList(menu);
        } else {
            for (Role role : roles) {
                // 根据角色查找菜单
                List<Menu> roleMenuList = menuService.findMenuByRole(role.getRoleCode(), user.getTenantCode());
                if (CollectionUtils.isNotEmpty(roleMenuList))
                    menuList.addAll(roleMenuList);
            }
        }
        if (CollectionUtils.isNotEmpty(menuList)) {
            permissions = menuList.stream()
                    // 获取权限菜单
                    .filter(menu -> MenuConstant.MENU_TYPE_PERMISSION.equals(menu.getType()))
                    // 获取权限
                    .map(Menu::getPermission).collect(Collectors.toList());

        }
        return permissions;
    }

    /**
     * 获取用户的角色
     *
     * @param user user
     * @return List
     * @author aswl.com
     * @date 2019/07/03 12:03:17
     */
    public List<Role> getUserRoles(User user) {
        List<Role> roles = Lists.newArrayList();
        List<UserRole> userRoles = userRoleMapper.getByUserId(user.getId());
        if (CollectionUtils.isNotEmpty(userRoles)) {
            Role role = new Role();
            role.setIds(userRoles.stream().map(UserRole::getRoleId).distinct().toArray(String[]::new));
            roles = roleMapper.findListById(role);
        }
        return roles;
    }

    /**
     * 批量查询用户的角色
     *
     * @param users users
     * @return List
     * @author aswl.com
     * @date 2019/07/03 12:13:38
     */
    public List<Role> getUsersRoles(List<User> users) {
        // 流处理获取用户ID集合，根据用户ID批量查找角色
        List<UserRole> userRoles = userRoleMapper.getByUserIds(users.stream().map(User::getId).collect(Collectors.toList()));
        List<Role> roleList = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(userRoles)) {
            Role role = new Role();
            // 获取角色ID集合，转成字节数组
            role.setIds(userRoles.stream().map(UserRole::getRoleId).distinct().toArray(String[]::new));
            // 批量查找角色
            roleList = roleMapper.findListById(role);
        }
        return roleList;
    }

    /**
     * 更新用户
     *
     * @param userDto userDto
     * @return int
     * @author aswl.com
     * @date 2018/8/26 15:15
     */
    @Transactional
   // @CacheEvict(value = "user", key = "#userDto.id")
    public boolean updateUser(UserDto userDto) {
        User user = new User();
        BeanUtils.copyProperties(userDto, user);
        user.setSysRole(RoleEnum.getByRoleCode(user.getRoleList().get(0).getRoleCode()));
        user.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        // 更新用户信息
        super.update(user);
        // 更新用户授权表
        if (StringUtils.isNotBlank(userDto.getCredential())) {
            UserAuths userAuths = new UserAuths();
            userAuths.setIdentifier(userDto.getIdentifier());
            userAuths.setTenantCode(userDto.getTenantCode());
            userAuths = userAuthsService.getByIdentifier(userAuths);
            userAuths.setCredential(encoder.encode(userDto.getCredential()));
            userAuthsService.update(userAuths);
        }
        // 更新用户角色关系
        UserRole sysUserRole = new UserRole();
        sysUserRole.setUserId(user.getId());
        // 删除原有的角色信息
        userRoleMapper.delete(sysUserRole);
        if (CollectionUtils.isNotEmpty(userDto.getRole())) {
            userDto.getRole().forEach(roleId -> {
                UserRole role = new UserRole();
                role.setId(IdGen.snowflakeId());
                role.setUserId(user.getId());
                role.setRoleId(roleId);
                // 保存角色信息
                userRoleMapper.insert(role);
            });
        }
        return Boolean.TRUE;
    }

    /**
     * 更新用户基本信息
     *
     * @param user user
     * @return int
     */
    @Override
    @Transactional
    public int update(User user) {
        return super.update(user);
    }

    /**
     * 更新密码
     *
     * @param userDto userDto
     * @return int
     * @author aswl.com
     * @date 2019/07/03 12:26:24
     */
    @Transactional
    @CacheEvict(value = "user", key = "#userDto.identifier")
    public int updatePassword(UserDto userDto) {
        userDto.setTenantCode(SysUtil.getTenantCode());
        if (StringUtils.isBlank(userDto.getNewPassword()))
            throw new CommonException("新密码不能为空.");
        if (StringUtils.isBlank(userDto.getIdentifier()))
            throw new CommonException("用户名不能为空.");
        UserAuths userAuths = new UserAuths();
        userAuths.setIdentifier(userDto.getIdentifier());
        userAuths.setTenantCode(userDto.getTenantCode());
        userAuths = userAuthsService.getByIdentifier(userAuths);
        if (userAuths == null)
            throw new CommonException("账号不存在.");
        if (!encoder.matches(userDto.getOldPassword(), userAuths.getCredential())) {
            throw new CommonException("修改失败，原密码不正确");
        } else {
            // 新旧密码一致，修改密码
            userAuths.setCredential(encoder.encode(userDto.getNewPassword()));
            return userAuthsService.update(userAuths);
        }
    }

    /**
     * 更新头像
     *
     * @param userDto
     * @return int
     * @author aswl.com
     * @date 2019/06/21 18:14
     */
    @Transactional
    @CacheEvict(value = "user", key = "#userDto.identifier")
    public int updateAvatar(UserDto userDto) {
        User user = new User();
        user.setId(userDto.getId());
        user = this.get(user);
        if (user == null)
            throw new CommonException("用户不存在.");
        // 先删除旧头像
        if (StringUtils.isNotBlank(user.getAvatarId())) {
            Attachment attachment = new Attachment();
            attachment.setId(user.getAvatarId());
            attachment = attachmentService.get(attachment);
            if (attachment != null)
                attachmentService.delete(attachment);
        }
        user.setAvatarId(userDto.getAvatarId());
        return super.update(user);
    }


    /**
     * 根据用户唯一标识获取用户详细信息
     *
     * @param identityType identityType
     * @param identifier   identifier
     * @param tenantCode   tenantCode
     * @return UserVo
     * @author aswl.com
     * @date 2019/07/03 13:00:39
     */
    // @Cacheable(value = "user#" + CommonConstant.CACHE_EXPIRE, key = "#identifier")
    public UserVo findUserByIdentifier(Integer identityType, String identifier, String tenantCode) {


        UserAuths userAuths = new UserAuths();
        userAuths.setIdentifier(identifier);
        if (identityType != null)
            userAuths.setIdentityType(IdentityType.match(identityType).getValue());

        //登录的时候过来的，这里 可以不用tenantCode,并没有-1这个类型
        if (identityType != null && identityType.equals(-1) && tenantCode != null) {
            userAuths.setTenantCode(tenantCode);
        }

        userAuths = userAuthsService.getByIdentifier(userAuths);
        if (userAuths == null)
            return null;
        // 查询用户信息
        User user = new User();
        user.setId(userAuths.getUserId());
        user = this.get(user);
        if (user == null && user.getStatus().equals("1"))
            return null;
        // 查询用户角色
        List<Role> roles = this.getUserRoles(user);
        UserVo userVo = new UserVo();
        BeanUtil.copyProperties(user, userVo);
        BeanUtil.copyProperties(userAuths, userVo, CopyOptions.create().setIgnoreNullValue(true).setIgnoreError(true));
        userVo.setRoleList(UserUtils.rolesToVo(roles));
        return userVo;
    }

    /**
     * 根据用户唯一标识获取用户详细信息
     *
     * @param identifier identifier
     * @param tenantCode tenantCode
     * @return UserVo
     * @author aswl.com
     * @date 2019/07/10 18:04:15
     */
    public UserVo findUserByIdentifier(String identifier, String tenantCode) {
        return this.findUserByIdentifier(null, identifier, tenantCode);
    }

    /**
     * 查询账号是否存在
     *
     * @param identifier identifier
     * @return boolean
     * @author aswl.com
     * @date 2019/07/03 13:23:10
     */
    public boolean checkIdentifierIsExist(String identifier) {
        UserAuths userAuths = new UserAuths();
        userAuths.setIdentifier(identifier);
        userAuths.setTenantCode(SysUtil.getTenantCode());
        return userAuthsService.getByIdentifier(userAuths) != null;
    }

    /**
     * 保存验证码
     *
     * @param tenantCode tenantCode
     * @param random     random
     * @param imageCode  imageCode
     * @author aswl.com
     * @date 2018/9/14 20:12
     */
    public void saveImageCode(String tenantCode, String random, String imageCode) {
        redisTemplate.opsForValue().set(tenantCode + ":" + CommonConstant.DEFAULT_CODE_KEY + LoginType.PWD.getType() + "@" + random, imageCode, SecurityConstant.DEFAULT_IMAGE_EXPIRE, TimeUnit.SECONDS);
    }

    /**
     * 删除用户
     *
     * @param user user
     * @return int
     */
    @Transactional
    @Override
    @CacheEvict(value = "user", key = "#user.id")
    public int delete(User user) {
        // 删除用户角色关系
        userRoleMapper.deleteByUserId(user.getId());
        // 删除用户授权信息
        UserAuths userAuths = new UserAuths();
        userAuths.setUserId(user.getId());
        userAuthsService.deleteByUserId(userAuths);
        return super.delete(user);
    }

    /**
     * 批量删除用户
     *
     * @param ids ids
     * @return int
     * @author aswl.com
     * @date 2019/07/04 11:44:45
     */
    @Transactional
    @Override
    @CacheEvict(value = "user", allEntries = true)
    public int deleteAll(String[] ids) {
        // 删除区域负责人信息
        regionServiceClient.deleteRegionLeaderByUserId(ids);
        String currentUser = SysUtil.getUser(), applicationCode = SysUtil.getSysCode(), tenantCode = SysUtil.getTenantCode();
        for (String id : ids) {
            // 删除用户角色关系
            userRoleMapper.deleteByUserId(id);
            // 删除用户授权信息
            UserAuths userAuths = new UserAuths();
            userAuths.setNewRecord(false);
            userAuths.setUserId(id);
            userAuths.setCommonValue(currentUser, applicationCode, tenantCode);
            userAuthsService.deleteByUserId(userAuths);
        }
        return super.deleteAll(ids);
    }

    /**
     * 查询用户数量
     *
     * @param userVo userVo
     * @return int
     * @author aswl.com
     * @date 2019/05/09 22:10
     */
    public Integer userCount(UserVo userVo) {
        return this.dao.userCount(userVo);
    }

    /**
     * 初始化用户头像信息
     *
     * @param userInfoDto userInfoDto
     * @param user        user
     * @author aswl.com
     * @date 2019/06/21 17:49
     */
    private void initUserAvatar(UserInfoDto userInfoDto, User user) {
        try {
            // 附件id不为空，获取对应的预览地址，否则获取配置默认头像地址
            if (StringUtils.isNotBlank(user.getAvatarId())) {
                Attachment attachment = new Attachment();
                attachment.setId(user.getAvatarId());
                userInfoDto.setAvatarUrl(attachmentService.getPreviewUrl(attachment));
            } else {
                userInfoDto.setAvatarUrl(sysProperties.getDefaultAvatar());
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 重置密码
     *
     * @param userDto userDto
     * @return int
     * @author aswl.com
     * @date 2019/07/03 13:27:39
     */
    @Transactional
    @CacheEvict(value = "user", key = "#userDto.identifier")
    public boolean resetPassword(UserDto userDto) {
        UserAuths userAuths = new UserAuths();
        userAuths.setIdentifier(userDto.getIdentifier());
        userAuths = userAuthsService.getByIdentifier(userAuths);
        if (userAuths == null)
            throw new CommonException("账号不存在.");
        // 重置密码为123456
        userAuths.setCredential(encoder.encode(CommonConstant.DEFAULT_PASSWORD));
        return userAuthsService.update(userAuths) > 0;
    }

    /**
     * 注册，注意要清除缓存
     *
     * @param userDto userDto
     * @return boolean
     * @author aswl.com
     * @date 2019/07/03 13:30:03
     */
    @Transactional
    @CacheEvict(value = "user", key = "#userDto.identifier")
    public boolean register(UserDto userDto) {
        boolean success = false;
        if (userDto.getIdentityType() == null)
            userDto.setIdentityType(IdentityType.PASSWORD.getValue());
        // 解密
        String password = this.decryptCredential(userDto.getCredential(), userDto.getIdentityType());
        User user = new User();
        BeanUtils.copyProperties(userDto, user);
        // 初始化用户名，系统编号，租户编号
        user.setCommonValue(userDto.getIdentifier(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        user.setStatus(CommonConstant.DEL_FLAG_NORMAL);
        // 初始化头像
        if (StringUtils.isNotBlank(userDto.getAvatarUrl())) {
            Attachment attachment = new Attachment();
            attachment.setCommonValue(userDto.getIdentifier(), SysUtil.getSysCode(), SysUtil.getTenantCode());
            attachment.setBusiType(AttachmentConstant.BUSI_TYPE_USER_AVATAR);
            attachment.setPreviewUrl(userDto.getAvatarUrl());
            if (attachmentService.insert(attachment) > 0)
                user.setAvatarId(attachment.getId());
        }
        // 保存用户基本信息
        if (this.insert(user) > 0) {
            // 保存账号信息
            UserAuths userAuths = new UserAuths();
            userAuths.setCommonValue(userDto.getIdentifier(), user.getApplicationCode(), user.getTenantCode());
            userAuths.setUserId(user.getId());
            userAuths.setIdentifier(userDto.getIdentifier());
            if (userDto.getIdentityType() != null)
                userAuths.setIdentityType(userDto.getIdentityType());
            // 设置密码
            userAuths.setCredential(encoder.encode(password));
            userAuthsService.insert(userAuths);
            // 分配默认角色
            success = this.defaultRole(user, userDto.getTenantCode(), userDto.getIdentifier());
        }
        return success;
    }


    /**
     * 手机端注册用户
     *
     * @param userDto userDto
     * @return boolean
     * @author aswl.com
     * @date 2020/10/14 16:40:03
     */
    @Transactional
    public boolean registerUser(UserDto userDto) {
        boolean success = false;
        if (userDto.getIdentityType() == null)
            userDto.setIdentityType(IdentityType.PASSWORD.getValue());
        // 解密
        String password = this.decryptCredential(userDto.getCredential(), userDto.getIdentityType());
        User user = new User();
        BeanUtils.copyProperties(userDto, user);
        // 初始化用户名，系统编号，租户编号
        //根据手机号查询用户所属租户
        Tenant tenant =tenantMapper.getTenantByMobile(userDto.getIdentifier());
        if (tenant!= null){
            user.setTenantCode(tenant.getTenantCode());
        }else{
            user.setTenantCode(SysUtil.getTenantCode());
        }
        user.setId(IdGen.snowflakeId());
        user.setCreator(userDto.getIdentifier());
        user.setPhone(userDto.getIdentifier());
        user.setApplicationCode(SysUtil.getSysCode());
        user.setStatus(CommonConstant.DEL_FLAG_NORMAL);
        // 初始化头像
        if (StringUtils.isNotBlank(userDto.getAvatarUrl())) {
            Attachment attachment = new Attachment();
            attachment.setCommonValue(userDto.getIdentifier(), SysUtil.getSysCode(), SysUtil.getTenantCode());
            attachment.setBusiType(AttachmentConstant.BUSI_TYPE_USER_AVATAR);
            attachment.setPreviewUrl(userDto.getAvatarUrl());
            if (attachmentService.insert(attachment) > 0)
                user.setAvatarId(attachment.getId());
        }
        // 保存用户基本信息
        if (this.insert(user) > 0) {
            // 保存账号信息
            UserAuths userAuths = new UserAuths();
            userAuths.setCommonValue(userDto.getIdentifier(), user.getApplicationCode(), user.getTenantCode());
            userAuths.setUserId(user.getId());
            userAuths.setIdentifier(userDto.getIdentifier());
            if (userDto.getIdentityType() != null)
                userAuths.setIdentityType(userDto.getIdentityType());
            // 设置密码
            userAuths.setCredential(encoder.encode(password));
            userAuthsService.insert(userAuths);
            // 分配角色
           // success = this.defaultRole(user, userDto.getTenantCode(), userDto.getIdentifier());
            // 根据租户查询角色ID
            Role role = roleMapper.getRoleIdByTenantCode(user.getTenantCode());
            UserRole userRole = new UserRole();
            userRole.setId(IdGen.snowflakeId());
            userRole.setUserId(user.getId());
            userRole.setRoleId(role!= null ? role.getId():"");
            // 保存用户角色关系
            success= userRoleMapper.insert(userRole) > 0;

        }
        return success;
    }

    /**
     * 验证手机验证码
     *
     * @param mobile
     * @param smsCode
     * @return
     */
    public Boolean checkSmsCode(String mobile, String smsCode) {
        Boolean result = false;
        if (StringUtils.isNotEmpty(mobile) && StringUtils.isNotEmpty(smsCode)) {
            String num = (String) redisTemplate.opsForValue().get("sms:" + mobile);
            if (smsCode.equals(num)) {
                result = true;
            }
        }
        return result;
    }


    /**
     * 验证租户试用手机号码
     *
     * @param mobile
     * @return
     */
    public Boolean checkTrialMobile(String mobile) {
        Boolean result = false;
        // 查询手机号是否注册过
        UserAuths userAuths = new UserAuths();
        userAuths.setIdentifier(mobile);
        UserAuths auths = userAuthsService.getByIdentifier(userAuths);
        if (auths != null) {
            throw new CommonException("注册失败，号码" + mobile + " 已经注册过");
        }
        // 先获取租户试用手机号码
        int count = tenantMapper.getExistMobile(mobile);
        if (count == 0) {
            throw new CommonException("手机号码无注册权限，请联系管理员");
        } else {
            result = true;
        }
        return result;
    }


    /**
     * 找回密码
     *
     * @param userDto
     * @return int
     */
    @Transactional
    public int findPassword(UserDto userDto) {
        userDto.setTenantCode(SysUtil.getTenantCode());
        if (StringUtils.isBlank(userDto.getNewPassword()))
            throw new CommonException("密码不能为空.");
        if (StringUtils.isBlank(userDto.getIdentifier()))
            throw new CommonException("用户名不能为空.");
        UserAuths userAuths = new UserAuths();
        userAuths.setIdentifier(userDto.getIdentifier());
        userAuths.setTenantCode(userDto.getTenantCode());
        userAuths = userAuthsService.getByIdentifier(userAuths);
        if (userAuths == null)
            throw new CommonException("账号不存在.");

        // 重新设置密码
        userAuths.setCredential(encoder.encode(decryptCredential(userDto.getNewPassword(), 1)));
        return userAuthsService.update(userAuths);
    }


    /**
     * 解密密码
     *
     * @param encoded encoded
     * @return String
     * @author aswl.com
     * @date 2019/07/05 12:39:13
     */
    private String decryptCredential(String encoded, Integer identityType) {
        // 返回默认密码
        if (StringUtils.isBlank(encoded))
            return CommonConstant.DEFAULT_PASSWORD;
        // 微信、手机号注册不需要解密
        if (IdentityType.WE_CHAT.getValue().equals(identityType) || IdentityType.PHONE_NUMBER.getValue().equals(identityType))
            return encoded;
        // 解密密码
        try {
            encoded = SysUtil.decryptAES(encoded, sysProperties.getKey()).trim();
            log.info("密码解密结果：{}", encoded);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new CommonException("注册失败，密码非法.");
        }
        return encoded;
    }

    /**
     * 分配默认角色
     *
     * @param user user
     * @param user user
     * @return
     * @author aswl.com
     * @date 2019/07/04 11:30:27
     */
    @Transactional
    public boolean defaultRole(User user, String tenantCode, String identifier) {
        Role role = new Role();
        role.setIsDefault(RoleConstant.IS_DEFAULT_ROLE);
        role.setTenantCode(tenantCode);
        // 查询默认角色
        Stream<Role> roleStream = roleMapper.findList(role).stream();
        if (Optional.ofNullable(roleStream).isPresent()) {
            Role defaultRole = roleStream.findFirst().orElse(null);
            if (defaultRole != null) {
                UserRole userRole = new UserRole();
                userRole.setCommonValue(identifier, user.getApplicationCode(), user.getTenantCode());
                userRole.setUserId(user.getId());
                userRole.setRoleId(defaultRole.getId());
                // 保存用户角色关系
                return userRoleMapper.insert(userRole) > 0;
            }
        }
        return false;
    }

    /**
     * 根据用户id批量查询UserVo
     *
     * @param userVo userVo
     * @return List
     * @author aswl.com
     * @date 2019/07/03 13:59:32
     */
    public List<UserVo> findUserVoListById(UserVo userVo) {
        List<UserVo> userVos = Lists.newArrayList();
        User user = new User();
        user.setIds(userVo.getIds());
        Stream<User> userStream = this.findListById(user).stream();
        if (Optional.ofNullable(userStream).isPresent()) {
            userVos = userStream.map(tempUser -> {
                UserVo tempUserVo = new UserVo();
                BeanUtils.copyProperties(tempUser, tempUserVo);
                return tempUserVo;
            }).collect(Collectors.toList());
        }
        return userVos;
    }

    /**
     * 组装用户数据，包括账号、部门、角色,区域
     *
     * @param tempUser      tempUser
     * @param userAuths     userAuths
     * @param deptList      deptList
     * @param userRoles     userRoles
     * @param finalRoleList finalRoleList
     * @param regionLeaderList regionLeaderList
     * @return UserDto
     * @author aswl.com
     * @date 2019/07/03 22:35:50
     */
    public UserDto getUserDtoByUserAndUserAuths(User tempUser, List<UserAuths> userAuths, List<Dept> deptList, List<Region> regionList, List<UserRole> userRoles, List<Role> finalRoleList,
                                                List<RegionLeader> regionLeaderList
                                                ) {
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(tempUser, userDto);
        // 设置账号信息
        if (CollectionUtils.isNotEmpty(userAuths)) {
            userAuths.stream().filter(tempUserAuths -> tempUserAuths.getUserId().equals(tempUser.getId()))
                    .findFirst().ifPresent(tempUserAuths -> userDto.setIdentifier(tempUserAuths.getIdentifier()));
        }
        // 设置部门信息
        if (CollectionUtils.isNotEmpty(deptList)) {
            // 用户所属部门
            deptList.stream()
                    // 按部门ID找到部门信息
                    .filter(tempDept -> tempDept.getId().equals(tempUser.getDeptId()))
                    .findFirst().ifPresent(userDept -> {
                userDto.setDeptId(userDept.getId());
                userDto.setDeptName(userDept.getDeptName());
            });
        }

        // 设置区域信息
        if (CollectionUtils.isNotEmpty(regionList)) {
            // 用户所属区域
            regionList.stream()
                    // 按区域ID找到区域信息
                    .filter(tempRegion -> tempRegion.getId().equals(tempUser.getRegionId()))
                    .findFirst().ifPresent(userRegion -> {
                userDto.setRegionId(userRegion.getId());
                userDto.setRegionName(userRegion.getRegionName());
            });
        }

        // 设置角色信息
        if (CollectionUtils.isNotEmpty(userRoles)) {
            List<Role> userRoleList = new ArrayList<>();
            userRoles.stream()
                    // 过滤
                    .filter(tempUserRole -> tempUser.getId().equals(tempUserRole.getUserId()))
                    .forEach(tempUserRole -> finalRoleList.stream()
                            .filter(role -> role.getId().equals(tempUserRole.getRoleId()))
                            .forEach(userRoleList::add));
            userDto.setRoleList(userRoleList);
        }
        // 区域负责人
        if (CollectionUtils.isNotEmpty(regionLeaderList)) {
            regionLeaderList.stream()
                    .filter(tempRegionLeader -> tempRegionLeader.getUserId().equals(tempUser.getId()))
                    .findFirst().ifPresent(userRegionLeader -> {
                userDto.setUserId(userRegionLeader.getUserId());
                userDto.setRegionId(userRegionLeader.getRegionId());
                userDto.setRegionCode(userRegionLeader.getRegionCode());
                userDto.setUserName(userRegionLeader.getUserName());
                userDto.setUserMobile(userRegionLeader.getUserMobile());
                userDto.setIsReceiveAlarm(userRegionLeader.getIsReceiveAlarm());
                userDto.setReceiveStartAt(userRegionLeader.getReceiveStartAt());
                userDto.setReceiveEndAt(userRegionLeader.getReceiveEndAt());
                userDto.setIsPatrol(userRegionLeader.getIsPatrol());
                userDto.setPatrolKeyId(userRegionLeader.getPatrolKeyId());
                userDto.setPatrolPeriodNum(userRegionLeader.getPatrolPeriodNum());
                userDto.setPatrolPeriodUnit(userRegionLeader.getPatrolPeriodUnit());
                userDto.setPatrolPeriod(userRegionLeader.getPatrolPeriod());
                userDto.setPatrolPeriodBeginTime(userRegionLeader.getPatrolPeriodBeginTime());
                userDto.setRemark(userRegionLeader.getRemark());
            });
        }
        return userDto;
    }

    /**
     * 导入用户
     *
     * @param userInfoDtos userInfoDtos
     * @return boolean
     * @author aswl.com
     * @date 2019/07/04 12:46:01
     */
    @Transactional
    @CacheEvict(value = "user", allEntries = true)
    public boolean importUsers(List<UserInfoDto> userInfoDtos) {
        String currentUser = SysUtil.getUser(), applicationCode = SysUtil.getSysCode(), tenantCode = SysUtil.getTenantCode();
        Date currentDate = DateUtils.asDate(LocalDateTime.now());
        for (UserInfoDto userInfoDto : userInfoDtos) {
            User user = new User();
            BeanUtils.copyProperties(userInfoDto, user);
            user.setModifier(currentUser);
            user.setModifyDate(currentDate);
            if (this.update(user) < 1) {
                user.setCommonValue(currentUser, applicationCode, tenantCode);
                user.setStatus(CommonConstant.STATUS_NORMAL);
                this.insert(user);
            }
            // 先删除用户授权信息
            UserAuths userAuths = new UserAuths();
            userAuths.setIdentifier(userInfoDto.getIdentifier());
            // 默认密码
            if (StringUtils.isBlank(userInfoDto.getCredential())) {
                userInfoDto.setCredential(encoder.encode(CommonConstant.DEFAULT_PASSWORD));
            }
            userAuths.setCredential(userInfoDto.getCredential());
            userAuths.setModifier(currentUser);
            userAuths.setModifyDate(currentDate);
            userAuths.setTenantCode(tenantCode);
            userAuthsService.deleteByIdentifier(userAuths);
            // 重新insert
            userAuths.setCommonValue(currentUser, applicationCode, tenantCode);
            userAuths.setUserId(user.getId());
            userAuths.setIdentityType(userInfoDto.getIdentityType());
            userAuthsService.insert(userAuths);
        }
        return true;
    }

    /**
     * 根据用户对象获取区域
     *
     * @param userList
     * @return List<Region>
     */
    public List<Region> getRegionListByUsers(List<User> userList) {

        //实际上也是调用相同的函数
        return regionServiceClient.osRegion1(OsVo.getRandomStr(), userList);
        //return  regionServiceClient.getRegionListByUsers(userList);
    }


    /**
     * 根据用户对象获取区域负责人
     *
     * @param userList
     * @return list
     */
    public List<RegionLeader> getRegionLeaderListByUsers(List<User> userList) {
         ResponseBean<List<RegionLeader>> responseBean =  regionServiceClient.getRegionLeaderByUserIds(userList);
         if (responseBean != null && responseBean.getStatus() == 200){
             return  responseBean.getData();
         }
         return null;
        
    }

    /**
     * 创建系统值班员/项目值班员账号
     *
     * @param dto dto
     * @return int
     */
    @Transactional
    public int insertDutyUser(DutyUserDto dto) {
        int update = 0 ;
        List<UserDto> list = dto.getUserDtoList();
        if (StringUtils.isNotBlank(dto.getUserIds())) {
            String[] userIds = dto.getUserIds().split(",");
            for (int i = 0; i < userIds.length; i++) {
                String userId = userIds[i];
                User user = new User();
                user.setId(userId);
                // 删除用户信息表
                super.delete(user);
                // 删除用户授权表
                UserAuths userAuths = new UserAuths();
                userAuths.setUserId(userId);
                userAuthsService.deleteByUserId(userAuths);
                //删除用户角色表
                UserRole userRole = new UserRole();
                userRole.setUserId(userId);
                userRoleMapper.deleteByUserId(userId);
            }
        }
        // 修改/新增数据
        if (list != null) {
            for (UserDto userDto : list) {
                // 如果传过来的id为空、则添加
                if (userDto.getId() == null) {
                    User user = new User();
                    BeanUtils.copyProperties(userDto, user);
                    user.setPhone(userDto.getIdentifier());
                    if (RoleEnum.ROLE_SYS_WATCHER.getCode().equals(userDto.getRoleCode())){
                            user.setSysRole(RoleEnum.ROLE_SYS_WATCHER.getType());
                     }
                     else{
                            user.setSysRole(RoleEnum.ROLE_PROJECT_WATCHER.getType());
                    }
                    user.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode(),user.getProjectId());
                    if (super.insert(user) > 0) {
                        // 保存用户授权信息
                        UserAuths userAuths = new UserAuths();
                        userAuths.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
                        userAuths.setUserId(user.getId());
                        userAuths.setIdentifier(userDto.getIdentifier());
                        userAuths.setIdentityType(IdentityType.PASSWORD.getValue());
                        userAuths.setCredential(encoder.encode(userDto.getCredential()));
                        userAuthsService.insert(userAuths);
                        //保存角色
                        UserRole sysUserRole = new UserRole();
                        sysUserRole.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode());
                        sysUserRole.setUserId(user.getId());
                        // 获取系统角色
                        String roleId = authorizeAdminRole(userDto.getIsCheck(), userDto.getRoleCode());
                        sysUserRole.setRoleId(roleId);
                        userRoleMapper.insert(sysUserRole);
                        update++;
                    }
                } else {
                    //修改
                    User user = new User();
                    BeanUtils.copyProperties(userDto, user);
                    user.setPhone(userDto.getIdentifier());
                    if (RoleEnum.ROLE_SYS_WATCHER.getCode().equals(userDto.getRoleCode())){
                        user.setSysRole(RoleEnum.ROLE_SYS_WATCHER.getType());
                    }else{
                        user.setSysRole(RoleEnum.ROLE_PROJECT_WATCHER.getType());
                    }
                    user.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode(),user.getProjectId());
                    super.update(user);
                    UserAuths userAuths = new UserAuths();
                    userAuths.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
                    userAuths.setIdentifier(userDto.getIdentifier());
                    userAuths.setIdentityType(IdentityType.PASSWORD.getValue());
                    if (StringUtils.isNotBlank(userDto.getCredential())) {
                        userAuths.setCredential(encoder.encode(userDto.getCredential()));
                    }
                    userAuthsService.update(userAuths);
                    // 先删除用户角色关联表信息
                    userRoleMapper.deleteByUserId(user.getId());
                    //保存角色
                    UserRole sysUserRole = new UserRole();
                    sysUserRole.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode());
                    sysUserRole.setUserId(user.getId());
                    // 获取系统角色
                    String roleId = authorizeAdminRole(userDto.getIsCheck(), userDto.getRoleCode());
                    sysUserRole.setRoleId(roleId);
                    userRoleMapper.insert(sysUserRole);
                    update++;
                }
            }
            //通知前台刷新
            Map object = new HashMap();
            object.put("alias", "userCompany");
            object.put("value", "refresh");
            mqSender.send(MqConstant.SystemMqMessage.COMMON_MESSAGE_FANOUT_EXCHANGE, MqConstant.SystemMqMessage.SYSTEM_BROADCAST_MESSAGE_QUEUE, JSON.toJSONString(object), MessageProperties.CONTENT_TYPE_TEXT_PLAIN);
        }
        return update;
        
    }


    /**
     * 系统值班员/项目值班员授权管理员功能
     * @param isCheck
     * @param roleCode
     */
    public String authorizeAdminRole(String isCheck,String roleCode){
        Role role;
       //选择了授权管理员功能权限
       if ("1" .equals(isCheck)){
           if (RoleEnum.ROLE_SYS_WATCHER.getCode().equals(roleCode)) { 
               role = roleMapper.getRoleByRoleCodeAndTenantCode(RoleEnum.ROLE_SYS_ADMIN.getCode(), SysUtil.getTenantCode());
           }else{
               role = roleMapper.getRoleByRoleCodeAndTenantCode(RoleEnum.ROLE_PROJECT_ADMIN.getCode(), SysUtil.getTenantCode());
           }
       }else{
           if (RoleEnum.ROLE_SYS_WATCHER.getCode().equals(roleCode)) {
               role = roleMapper.getRoleByRoleCodeAndTenantCode(RoleEnum.ROLE_SYS_WATCHER.getCode(), SysUtil.getTenantCode());
           }else{
               role = roleMapper.getRoleByRoleCodeAndTenantCode(RoleEnum.ROLE_PROJECT_WATCHER.getCode(), SysUtil.getTenantCode());
           }
       }
       return role.getId();
    }


    /**
     * 角色设置/添加用户
     * @param userDto userDto
     * @return int
     */
    @Transactional
    public int insertRoleUser(UserDto userDto) {
        // 如果添加是区域负责人角色、则验证区域负责人是否存在
        if (userDto.getRoleList().get(0).getRoleCode().contains(RoleEnum.ROLE_PROJECT_REGION.getCode())){
            ResponseBean<Boolean> responseData = regionServiceClient.getIsExistLeader(userDto.getRegionCode());
            if (responseData.getStatus() == 200 && responseData.getData().equals(Boolean.FALSE)){
                 throw  new CommonException("该区域下已经存在负责人");
            }
        }
        int update;
        User user = new User();
        BeanUtils.copyProperties(userDto, user);
        // 先保存用户基本信息
        user.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), user.getTenantCode(),user.getProjectId());
        user.setPhone(userDto.getIdentifier());
        user.setSysRole(RoleEnum.getByRoleCode(user.getRoleList().get(0).getRoleCode()));
        // 保存父子账号关系
        UserVo currentUser = this.findUserByIdentifier(userDto.getIdentityType(), SysUtil.getUser(), SysUtil.getTenantCode());
        user.setParentUid(currentUser.getId());
        if ((update = this.insert(user)) > 0) {
            // 保存用户授权信息
            UserAuths userAuths = new UserAuths();
            userAuths.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), user.getTenantCode());
            userAuths.setUserId(user.getId());
            userAuths.setIdentifier(userDto.getIdentifier());
            if (userDto.getIdentityType() == null)
                userAuths.setIdentityType(IdentityType.PASSWORD.getValue());
            // 默认密码为123456
            if (StringUtils.isBlank(userDto.getCredential()))
                userDto.setCredential(CommonConstant.DEFAULT_PASSWORD);
            userAuths.setCredential(encoder.encode(userDto.getCredential()));
            update = userAuthsService.insert(userAuths);
            // 是否区域负责人 
            if (userDto.getRoleList().get(0).getRoleCode().contains(RoleEnum.ROLE_PROJECT_REGION.getCode())){
                //添加区域负责人信息
                RegionLeaderDto regionLeaderDto = new RegionLeaderDto();
                regionLeaderDto.setUserId(user.getId());
                regionLeaderDto.setRegionId(userDto.getRegionId());
                regionLeaderDto.setRegionCode(userDto.getRegionCode());
                regionLeaderDto.setUserName(userDto.getName());
                regionLeaderDto.setUserMobile(user.getPhone());
                regionLeaderDto.setIsReceiveAlarm(userDto.getIsReceiveAlarm());
                regionLeaderDto.setReceiveStartAt(userDto.getReceiveStartAt());
                regionLeaderDto.setReceiveEndAt(userDto.getReceiveEndAt());
                regionLeaderDto.setIsPatrol(userDto.getIsPatrol());
                regionLeaderDto.setPatrolKeyId(userDto.getPatrolKeyId());
                regionLeaderDto.setPatrolPeriodNum(userDto.getPatrolPeriodNum());
                regionLeaderDto.setPatrolPeriodUnit(userDto.getPatrolPeriodUnit());
                regionLeaderDto.setPatrolPeriod(userDto.getPatrolPeriod());
                regionLeaderDto.setPatrolPeriodBeginTime(userDto.getPatrolPeriodBeginTime());
                regionLeaderDto.setRemark(userDto.getRemark());
                regionLeaderDto.setTenantCode(user.getTenantCode());
                regionServiceClient.insertRegionLeader(regionLeaderDto);
            }
        }
        return update;
    }


    /**
     *  角色设置/编辑用户
     *
     * @param userDto userDto
     * @return boolean
     */
    @Transactional
    public boolean updateRoleUser(UserDto userDto) {
        User user = new User();
        BeanUtils.copyProperties(userDto, user);
        //根据用户ID查询用户角色
        Role roles = roleMapper.findByUserId(userDto.getId());
        if (user.getRoleList().get(0).getRoleCode().contains(roles.getRoleCode())){ 
            
        }else{
           user.setSysRole(RoleEnum.getByRoleCode(user.getRoleList().get(0).getRoleCode()));
        }
        user.setPhone(userDto.getIdentifier());
        user.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode(),user.getProjectId());
      // 更新用户信息
        super.update(user);
        // 更新用户授权表
        UserAuths userAuths = new UserAuths();
        userAuths.setIdentifier(userDto.getIdentifier());
        userAuths.setUserId(user.getId());
        if (StringUtils.isNotBlank(userDto.getCredential())){
            userAuths.setCredential(encoder.encode(userDto.getCredential()));
        }
        userAuthsService.updateByUserId(userAuths);
        // 更新用户角色关系
        UserRole sysUserRole = new UserRole();
        sysUserRole.setUserId(user.getId());
        // 删除原有的角色信息
        userRoleMapper.delete(sysUserRole);
        if (CollectionUtils.isNotEmpty(userDto.getRole())) {
            userDto.getRole().forEach(roleId -> {
                UserRole userRole = new UserRole();
                userRole.setId(IdGen.snowflakeId());
                userRole.setUserId(user.getId());
                userRole.setRoleId(roleId);
                // 保存角色信息
                userRoleMapper.insert(userRole);
            });
        }
        // 是否区域负责人角色
        if (userDto.getRoleList().get(0).getRoleCode().contains(RoleEnum.ROLE_PROJECT_REGION.getCode())) {
            //更新区域负责人信息
            RegionLeaderDto regionLeaderDto = new RegionLeaderDto();
            regionLeaderDto.setUserId(user.getId());
            regionLeaderDto.setRegionId(userDto.getRegionId());
            regionLeaderDto.setRegionCode(userDto.getRegionCode());
            regionLeaderDto.setUserName(userDto.getName());
            regionLeaderDto.setUserMobile(user.getPhone());
            regionLeaderDto.setIsReceiveAlarm(userDto.getIsReceiveAlarm());
            regionLeaderDto.setReceiveStartAt(userDto.getReceiveStartAt());
            regionLeaderDto.setReceiveEndAt(userDto.getReceiveEndAt());
            regionLeaderDto.setIsPatrol(userDto.getIsPatrol());
            regionLeaderDto.setPatrolKeyId(userDto.getPatrolKeyId());
            regionLeaderDto.setPatrolPeriodNum(userDto.getPatrolPeriodNum());
            regionLeaderDto.setPatrolPeriodUnit(userDto.getPatrolPeriodUnit());
            regionLeaderDto.setPatrolPeriod(userDto.getPatrolPeriod());
            regionLeaderDto.setPatrolPeriodBeginTime(userDto.getPatrolPeriodBeginTime());
            regionLeaderDto.setRemark(userDto.getRemark());
            regionServiceClient.updateRegionLeaderByUserId(regionLeaderDto);
        }
        return Boolean.TRUE;
    }

    /**
     * 根据项目ID获取审核人员
     * @param projectId
     * @return  UserVo
     */
    public List<UserVo> getApproveUsers(String projectId){
        String tenantCode =SysUtil.getTenantCode();
        return userMapper.findUsersByProjectId(projectId,tenantCode);
    }


    /**
     * 添加项目时添加项目管理员用户
     * @param userDto userDto
     * @return int
     */
    @Transactional
    public int insertProjectAdminUser(UserDto userDto) {
        int update;
        User user = new User();
        BeanUtils.copyProperties(userDto, user);
        // 先保存用户基本信息
        user.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), user.getTenantCode(),user.getProjectId());
        user.setPhone(userDto.getIdentifier());
        user.setSysRole(RoleEnum.ROLE_PROJECT_ADMIN.getType());
        // 保存父子账号关系
        UserVo currentUser = this.findUserByIdentifier(userDto.getIdentityType(), SysUtil.getUser(), user.getTenantCode());
        user.setParentUid(currentUser.getId());
        if ((update = this.insert(user)) > 0) {
            // 保存用户授权信息
            UserAuths userAuths = new UserAuths();
            userAuths.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), user.getTenantCode());
            userAuths.setUserId(user.getId());
            userAuths.setIdentifier(userDto.getIdentifier());
            if (userDto.getIdentityType() == null)
                userAuths.setIdentityType(IdentityType.PASSWORD.getValue());
            // 默认密码为123456
            if (StringUtils.isBlank(userDto.getCredential()))
                userDto.setCredential(CommonConstant.DEFAULT_PASSWORD);
            userAuths.setCredential(encoder.encode(userDto.getCredential()));
            update = userAuthsService.insert(userAuths);
        }
        return update;
    }

    /**
     * 获取项目管理员角色ID
     * @param roleCode
     * @param tenantCode
     * @return
     */
    public Role getRoleId( String roleCode,String tenantCode){
        return roleMapper.getRoleByRoleCodeAndTenantCode(roleCode,tenantCode);
    }

    /**
     * 获取发送APP通知的项目管理员/项目值班员
     * @param projectId
     * @return list
     */
    public List<User> findSendNoticeUsers(String projectId){
        return userMapper.findSendNoticeUsers(projectId);
    }

    /**
     * 获取发送邮件不为空的用户
     * @param projectId
     * @return list
     */
    public List<User> findSendEmailUsers(String projectId){
        return userMapper.findSendEmailUsers(projectId);
        
    }

    /**
     * 根据项目ID查询用户列表
     * @param projectId
     * @return list
     */
    public List<User> findUserByProjectId(String projectId){
        return userMapper.findUserByProjectId(projectId);
    }

    /**
     * 更新在线时长
     * @param userDto
     * @return int
     */
    public int  updateOnlineDuration(UserDto userDto){
        int nowTime = (int)(System.currentTimeMillis()/1000);
         User user = new User();
         user.setId(userDto.getId());
         User u = userMapper.get(user);
         if (u.getOnlineDurationBegin() == 0 || u.getOnlineDurationBegin() ==  null){
             userDto.setOnlineDuration(0);
         }else {
             userDto.setOnlineDuration(nowTime - u.getOnlineDurationBegin());
         }
        userDto.setOnlineDurationBegin(nowTime);
        return userMapper.updateOnlineDuration(userDto);
    }
}
