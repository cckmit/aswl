package com.aswl.as.user.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.aswl.as.common.core.enums.RoleEnum;
import com.aswl.as.ibrs.api.feign.RegionServiceClient;
import com.aswl.as.ibrs.api.module.AlarmTypeUserFavorite;
import com.aswl.as.ibrs.api.module.RegionLeader;
import com.aswl.as.user.api.dto.DutyUserDto;
import com.aswl.as.user.filter.RoleContextHolder;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.aswl.as.common.core.constant.CommonConstant;
import com.aswl.as.common.core.enums.BusinessType;
import com.aswl.as.common.core.exceptions.CommonException;
import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.utils.DateUtils;
import com.aswl.as.common.core.utils.ExcelToolUtil;
import com.aswl.as.common.core.utils.MapUtil;
import com.aswl.as.common.core.utils.PageUtil;
import com.aswl.as.common.core.utils.Servlets;
import com.aswl.as.common.core.utils.SysUtil;
import com.aswl.as.common.core.vo.OsVo;
import com.aswl.as.common.core.vo.UserVo;
import com.aswl.as.common.core.web.BaseController;
import com.aswl.as.common.log.annotation.Log;
import com.aswl.as.common.security.constant.SecurityConstant;
import com.aswl.as.ibrs.api.module.Region;
import com.aswl.as.user.api.dto.UserDto;
import com.aswl.as.user.api.dto.UserInfoDto;
import com.aswl.as.user.api.module.Dept;
import com.aswl.as.user.api.module.Role;
import com.aswl.as.user.api.module.User;
import com.aswl.as.user.api.module.UserAuths;
import com.aswl.as.user.api.module.UserRole;
import com.aswl.as.user.service.DeptService;
import com.aswl.as.user.service.UserAuthsService;
import com.aswl.as.user.service.UserRoleService;
import com.aswl.as.user.service.UserService;
import com.aswl.as.user.utils.UserUtils;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author aswl.com
 * @date 2018-08-25 16:20
 */
@Slf4j
@AllArgsConstructor
@Api(value = "/api/v1/user", tags = "用户信息")
@RestController
@RequestMapping(value = "/v1/user")
public class UserController extends BaseController {

    private final UserService userService;

    private final UserRoleService userRoleService;

    private final DeptService deptService;

    private final UserAuthsService userAuthsService;
    
    private  final RegionServiceClient regionServiceClient; 

    /**
     * 根据id获取
     *
     * @param id id
     * @return ResponseBean
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "获取用户信息", notes = "根据用户id获取用户详细信息")
    @ApiImplicitParam(name = "id", value = "用户ID", required = true, dataType = "String", paramType = "path")
    public ResponseBean<User> user(@PathVariable("id") String id) {
        User user = new User();
        user.setId(id);
        return new ResponseBean<>(userService.get(user));
    }

    /**
     * 获取当前用户信息（角色、权限）
     *
     * @return 用户名
     */
    @GetMapping("info")
    @ApiOperation(value = "获取用户信息", notes = "获取当前登录用户详细信息")
    @ApiImplicitParam(name = "identityType", value = "账号类型", required = true, dataType = "String")
    public ResponseBean<UserInfoDto> userInfo(@RequestParam(required = false) String identityType, OAuth2Authentication authentication) {
        try {
            UserVo userVo = new UserVo();
            if (StringUtils.isNotEmpty(identityType))
                userVo.setIdentityType(Integer.valueOf(identityType));
            userVo.setIdentifier(authentication.getName());
            userVo.setTenantCode(SysUtil.getTenantCode());
            return new ResponseBean<>(userService.findUserInfo(userVo));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new CommonException("获取当前登录用户详细信息");
        }
    }

    /**
     * 根据用户唯一标识获取用户详细信息
     *
     * @param identifier   identifier
     * @param identityType identityType
     * @param randomStr    随机字符串
     * @return UserVo
     */
    @GetMapping("/findUserByIdentifierForLogin/{identifier}")
    @ApiOperation(value = "获取用户信息", notes = "根据用户name获取用户详细信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "identifier", value = "用户唯一标识", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "identityType", value = "用户授权类型", dataType = "Integer"),
            @ApiImplicitParam(name = "tenantCode", value = "租户标识", required = true, dataType = "String"),
    })
    public ResponseBean<UserVo> findUserByIdentifierForLogin(@PathVariable String identifier, @RequestParam(required = false) Integer identityType, @RequestParam String randomStr) {

        if (OsVo.isWrongRandomStr(randomStr)) return OsVo.ERR_RESPONSE_BEAN;

        //没有这个类型，作为标记证明是登录调用的
        identityType = -1;

        return new ResponseBean<>(userService.findUserByIdentifier(identityType, identifier, null));
    }

    /**
     * 根据用户唯一标识获取用户详细信息
     *
     * @param identifier   identifier
     * @param identityType identityType
     * @param tenantCode   tenantCode
     * @return UserVo
     */
    @GetMapping("/findUserByIdentifier/{identifier}")
    @ApiOperation(value = "获取用户信息", notes = "根据用户name获取用户详细信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "identifier", value = "用户唯一标识", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "identityType", value = "用户授权类型", dataType = "Integer"),
            @ApiImplicitParam(name = "tenantCode", value = "租户标识", required = true, dataType = "String"),
    })
    public ResponseBean<UserVo> findUserByIdentifier(@PathVariable String identifier, @RequestParam(required = false) Integer identityType, @RequestParam String tenantCode) {
        return new ResponseBean<>(userService.findUserByIdentifier(identityType, identifier, tenantCode));
    }

    /**
     * 获取分页数据
     *
     * @param pageNum  pageNum@NotBlank
     * @param pageSize pageSize
     * @param sort     sort
     * @param order    order
     * @param userVo   userVo
     * @return PageInfo
     * @author aswl.com
     * @date 2018/8/26 22:56
     */
    @GetMapping("userList")
    @ApiOperation(value = "获取用户列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = "userVo", value = "用户信息", dataType = "UserVo")
    })
    public ResponseBean<PageInfo<UserDto>> userList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                                    @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                                    @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                                    @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
                                                    @RequestParam(value = "name", required = false, defaultValue = "") String name,
                                                    @RequestParam(value = "deptId", required = false, defaultValue = "") String deptId,
                                                    @RequestParam(value = "deptCode", required = false, defaultValue = "") String deptCode,
                                                    @RequestParam(value = "regionCode", required = false, defaultValue = "") String regionCode,
                                                    @RequestParam(value = "queryProjectId", required = false, defaultValue = "") String queryProjectId,
                                                    @RequestParam(value = "queryRole", required = false, defaultValue = "") String queryRole,
                                                    @RequestParam(value = "query", required = false, defaultValue = "") String query,
                                                    @RequestParam(value = "isAsOs", required = false, defaultValue = "") String isAsOs,
                                                    UserVo userVo) {
      /*  if(!SysUtil.isAdmin())
        {
            userVo.setTenantCode(SysUtil.getTenantCode());
            userVo.setProjectId(SysUtil.getProjectId());
        }*/
        String roles = RoleContextHolder.getRole();
        String tenantCode = SysUtil.getTenantCode();
        if (SysUtil.isAdmin() || roles.contains(SecurityConstant.ROLE_ADMIN)) { //超级管理员
            //加载所有项目
            tenantCode = null;
        }
        userVo.setTenantCode(tenantCode);
        if (roles != null && !"".equals(roles) && roles.contains(RoleEnum.ROLE_SYS_ADMIN.getCode()) || roles.contains(RoleEnum.ROLE_ADMIN.getCode())){
            userVo.setIsSysAdminRole("1");
        }
        PageInfo<UserDto> userDtoPageInfo = new PageInfo<>();
        List<UserDto> userDtos = Lists.newArrayList();
       /* if (!"1".equals(isAsOs)) {
            userVo.setTenantCode(SysUtil.getTenantCode());
        }*/
        User user = new User();
        BeanUtils.copyProperties(userVo, user);
        user.setName(name);
        user.setDeptId(deptId);
        user.setDeptCode(deptCode);
        user.setProjectId(SysUtil.getProjectId());
        user.setRegionCode(regionCode);
        if (StringUtils.isNotBlank(queryProjectId)){
            user.setProjectId(queryProjectId);
        }
        user.setQuery(query);
        if (StringUtils.isNotBlank(queryRole)) {
            user.setSysRole(queryRole);
        }
        PageInfo<User> page = userService.findPage(PageUtil.pageInfo(pageNum, pageSize, "", ""), user);
        List<User> users = page.getList();
        if (CollectionUtils.isNotEmpty(users)) {
            // 批量查询账户
            List<UserAuths> userAuths = userAuthsService.getListByUsers(users);
            // 批量查找部门
            List<Dept> deptList = deptService.getListByUsers(users);

            // 批量查找区域
            List<Region> regionList = userService.getRegionListByUsers(users);
            
            // 查询用户角色关联关系
            List<UserRole> userRoles = userRoleService.getByUserIds(users.stream().map(User::getId).collect(Collectors.toList()));
            // 批量查找角色
            List<Role> finalRoleList = userService.getUsersRoles(users);
            //批量查找区域负责人信息
            List<RegionLeader> regionLeaderList  = userService.getRegionLeaderListByUsers(users);
            // 组装数据
            users.forEach(tempUser -> userDtos.add(userService.getUserDtoByUserAndUserAuths(tempUser, userAuths, deptList, regionList, userRoles, finalRoleList,regionLeaderList)));
        }
        PageUtil.copyProperties(page, userDtoPageInfo);
        userDtoPageInfo.setList(userDtos);
        return new ResponseBean<>(userDtoPageInfo);
    }

    /**
     * 创建用户
     *
     * @param userDto userDto
     * @return ResponseBean
     * @author aswl.com
     * @date 2018/8/26 14:34
     */
    @PostMapping
    @PreAuthorize("hasAuthority('sys:user:add') or hasAnyRole('" + SecurityConstant.ROLE_ADMIN + "')")
    @ApiOperation(value = "创建用户", notes = "创建用户")
    @ApiImplicitParam(name = "userDto", value = "用户实体user", required = true, dataType = "UserDto")
    @Log(value = "新增用户", businessType = BusinessType.INSERT)
    public ResponseBean<Boolean> addUser(@RequestBody @Valid UserDto userDto) {
        boolean b = userService.checkIdentifierIsExist(userDto.getIdentifier());
        {
            if (b) {
                return new ResponseBean<>(false);
            }
        }
        return new ResponseBean<>(userService.createUser(userDto) > 0);
    }

    /**
     * 更新用户
     *
     * @param userDto userDto
     * @return ResponseBean
     * @author aswl.com
     * @date 2018/8/26 15:06
     */
    @PutMapping
   // @PreAuthorize("hasAuthority('sys:user:edit') or hasAnyRole('" + SecurityConstant.ROLE_ADMIN + "')")
    @ApiOperation(value = "更新用户信息", notes = "根据用户id更新用户的基本信息、角色信息")
    @ApiImplicitParam(name = "userDto", value = "用户实体user", required = true, dataType = "UserDto")
    @Log(value = "修改用户", businessType = BusinessType.UPDATE)
    public ResponseBean<Boolean> updateUser(@RequestBody UserDto userDto) {
        try {
            return new ResponseBean<>(userService.updateUser(userDto));
        } catch (Exception e) {
            log.error("更新用户信息失败！", e);
            throw new CommonException("更新用户信息失败！");
        }
    }

    /**
     * 更新用户的基本信息
     *
     * @param userDto userDto
     * @return ResponseBean
     * @author aswl.com
     * @date 2018/10/30 10:06
     */
    @PutMapping("updateInfo")
    @ApiOperation(value = "更新用户基本信息", notes = "根据用户id更新用户的基本信息")
    @ApiImplicitParam(name = "userDto", value = "用户实体user", required = true, dataType = "UserDto")
    @Log(value = "更新用户基本信息", businessType = BusinessType.UPDATE)
    public ResponseBean<Boolean> updateInfo(@RequestBody UserDto userDto) {
        User user = new User();
        BeanUtils.copyProperties(userDto, user);
        user.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), userDto.getTenantCode());
        return new ResponseBean<>(userService.update(user) > 0);
    }

    /**
     * 修改密码
     *
     * @param userDto userDto
     * @return ResponseBean
     * @author aswl.com
     * @date 2019/06/21 20:09
     */
    @PutMapping("updatePassword")
    @ApiOperation(value = "修改用户密码", notes = "修改用户密码")
    @ApiImplicitParam(name = "userDto", value = "用户实体user", required = true, dataType = "UserDto")
    @Log(value = "更新用户密码", businessType = BusinessType.UPDATE)
    public ResponseBean<Boolean> updatePassword(@RequestBody UserDto userDto) {
        return new ResponseBean<>(userService.updatePassword(userDto) > 0);
    }

    /**
     * 更新头像
     *
     * @param userDto
     * @return ResponseBean
     * @author aswl.com
     * @date 2019/06/21 18:08
     */
    @PutMapping("updateAvatar")
    @ApiOperation(value = "更新用户头像", notes = "根据用户id更新用户的头像信息")
    @ApiImplicitParam(name = "userDto", value = "用户实体user", required = true, dataType = "UserDto")
    @Log(value = "更新用户头像", businessType = BusinessType.UPDATE)
    public ResponseBean<Boolean> updateAvatar(@RequestBody UserDto userDto) {
        return new ResponseBean<>(userService.updateAvatar(userDto) > 0);
    }

    /**
     * 删除用户
     *
     * @param id id
     * @return ResponseBean
     * @author aswl.com
     * @date 2018/8/26 15:28
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('sys:user:del') or hasAnyRole('" + SecurityConstant.ROLE_ADMIN + "')")
    @ApiOperation(value = "删除用户", notes = "根据ID删除用户")
    @ApiImplicitParam(name = "id", value = "用户ID", required = true, paramType = "path")
    @Log(value = "删除用户", businessType = BusinessType.DELETE)
    public ResponseBean<Boolean> deleteUser(@PathVariable String id) {
        try {
            User user = new User();
            user.setId(id);
            user = userService.get(user);
            user.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
            return new ResponseBean<>(userService.delete(user) > 0);
        } catch (Exception e) {
            log.error("删除用户信息失败！", e);
            throw new CommonException("删除用户信息失败！");
        }
    }

    /**
     * 导出
     *
     * @param userVo userVo
     * @author aswl.com
     * @date 2018/11/26 22:11
     */
    @PostMapping("export")
    @PreAuthorize("hasAuthority('sys:user:export') or hasAnyRole('" + SecurityConstant.ROLE_ADMIN + "')")
    @ApiOperation(value = "导出用户", notes = "根据用户id导出用户")
    @ApiImplicitParam(name = "userVo", value = "用户信息", required = true, dataType = "UserVo")
    @Log(value = "导出用户", businessType = BusinessType.EXPORT)
    public void exportUser(@RequestBody UserVo userVo, HttpServletRequest request, HttpServletResponse response) {
        try {
            // 配置response
            response.setCharacterEncoding("utf-8");
            response.setContentType("multipart/form-data");
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, Servlets.getDownName(request, "用户信息" + DateUtils.localDateMillisToString(LocalDateTime.now()) + ".xlsx"));
            List<User> users;
            if (StringUtils.isNotEmpty(userVo.getIdString())) {
                User user = new User();
                user.setIds(Stream.of(userVo.getIdString().split(",")).filter(StringUtils::isNotBlank).distinct().toArray(String[]::new));
                users = userService.findListById(user);
            } else {
                // 导出本租户下的全部用户
                User user = new User();
                user.setTenantCode(SysUtil.getTenantCode());
                users = userService.findList(user);
            }
            if (CollectionUtils.isEmpty(users))
                throw new CommonException("无用户数据.");
            // 查询用户授权信息
            List<UserAuths> userAuths = userAuthsService.getListByUsers(users);
            // 组装数据，转成dto
            List<UserInfoDto> userInfoDtos = users.stream().map(tempUser -> {
                UserInfoDto userDto = new UserInfoDto();
                userAuths.stream()
                        .filter(userAuth -> userAuth.getUserId().equals(tempUser.getId()))
                        .findFirst()
                        .ifPresent(userAuth -> UserUtils.toUserInfoDto(userDto, tempUser, userAuth));
                return userDto;
            }).collect(Collectors.toList());
            ExcelToolUtil.exportExcel(request.getInputStream(), response.getOutputStream(), MapUtil.java2Map(userInfoDtos), UserUtils.getUserMap());
        } catch (Exception e) {
            log.error("导出用户数据失败！", e);
            throw new CommonException("导出用户数据失败！");
        }
    }

    /**
     * 导入数据
     *
     * @param file file
     * @return ResponseBean
     * @author aswl.com
     * @date 2018/11/28 12:44
     */
    @PostMapping("import")
    @PreAuthorize("hasAuthority('sys:user:import') or hasAnyRole('" + SecurityConstant.ROLE_ADMIN + "')")
    @ApiOperation(value = "导入数据", notes = "导入数据")
    @Log("导入用户")
    public ResponseBean<Boolean> importUser(@ApiParam(value = "要上传的文件", required = true) MultipartFile file, HttpServletRequest request) {
        try {
            log.debug("开始导入用户数据");
            List<UserInfoDto> userInfoDtos = MapUtil.map2Java(UserInfoDto.class,
                    ExcelToolUtil.importExcel(file.getInputStream(), UserUtils.getUserMap()));
            if (CollectionUtils.isEmpty(userInfoDtos))
                throw new CommonException("无用户数据导入.");
            return new ResponseBean<>(userService.importUsers(userInfoDtos));
        } catch (Exception e) {
            log.error("导入用户数据失败！", e);
            throw new CommonException("导入用户数据失败！");
        }
    }

    /**
     * 批量删除
     *
     * @param user user
     * @return ResponseBean
     * @author aswl.com
     * @date 2018/12/4 9:58
     */
    @PostMapping("deleteAll")
    //@PreAuthorize("hasAuthority('sys:user:del') or hasAnyRole('" + SecurityConstant.ROLE_ADMIN + "')")
    @ApiOperation(value = "批量删除用户", notes = "根据用户id批量删除用户")
    @ApiImplicitParam(name = "user", value = "用户信息", dataType = "User")
    @Log(value = "批量删除用户", businessType = BusinessType.DELETE)
    public ResponseBean<Boolean> deleteAllUsers(@RequestBody User user) {
        try {
            boolean success = Boolean.FALSE;
            if (StringUtils.isNotEmpty(user.getIdString()))
                success = userService.deleteAll(user.getIdString().split(",")) > 0;
            return new ResponseBean<>(success);
        } catch (Exception e) {
            log.error("删除用户失败！", e);
            throw new CommonException("删除用户失败！");
        }
    }

    /**
     * 根据ID查询
     *
     * @param userVo userVo
     * @return ResponseBean
     * @author aswl.com
     * @date 2018/12/31 21:16
     */
    @PostMapping(value = "findById")
    @ApiOperation(value = "根据ID查询用户", notes = "根据ID查询用户")
    @ApiImplicitParam(name = "userVo", value = "用户信息", required = true, paramType = "UserVo")
    public ResponseBean<List<UserVo>> findById(@RequestBody UserVo userVo) {
        return new ResponseBean<>(userService.findUserVoListById(userVo));
    }

    /**
     * 注册
     *
     * @param userDto userDto
     * @return ResponseBean
     * @author aswl.com
     * @date 2019/01/10 22:35
     */
    @ApiOperation(value = "注册", notes = "注册")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "grant_type", value = "授权类型（password、mobile）", required = true, defaultValue = "password", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "code", value = "验证码", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "randomStr", value = "随机数", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "mobile", value = "手机号", dataType = "String", paramType = "query")
    })
    @PostMapping("register")
    @Log(value = "注册用户", businessType = BusinessType.INSERT)
    public ResponseBean<Boolean> register(@RequestBody @Valid UserDto userDto) {
        return new ResponseBean<>(userService.register(userDto));
    }

    /**
     * 检查账号是否存在
     *
     * @param identifier identityType
     * @return ResponseBean
     * @author aswl.com
     * @date 2019/04/23 15:35
     */
    @ApiOperation(value = "检查账号是否存在", notes = "检查账号是否存在")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "identifier", value = "用户唯一标识", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "tenantCode", value = "租户标识", required = true, dataType = "String"),
    })
    @GetMapping("checkExist/{identifier}")
    public ResponseBean<Boolean> checkExist(@PathVariable("identifier") String identifier) {
        return new ResponseBean<>(userService.checkIdentifierIsExist(identifier));
    }

    /**
     * 查询用户数量
     *
     * @param userVo userVo
     * @return ResponseBean
     * @author aswl.com
     * @date 2019/05/09 22:09
     */
    @PostMapping("userCount")
    public ResponseBean<Integer> userCount(UserVo userVo) {

        return new ResponseBean<>(userService.userCount(userVo));
    }

    /**
     * 重置密码
     *
     * @param userDto userDto
     * @return ResponseBean
     * @author aswl.com
     * @date 2019/6/7 12:00
     */
    @PutMapping("/resetPassword")
    @PreAuthorize("hasAuthority('sys:user:edit') or hasAnyRole('" + SecurityConstant.ROLE_ADMIN + "')")
    @ApiOperation(value = "重置密码", notes = "根据用户id重置密码")
    @ApiImplicitParam(name = "userDto", value = "用户实体user", required = true, dataType = "UserDto")
    @Log(value = "重置密码", businessType = BusinessType.OTHER)
    public ResponseBean<Boolean> resetPassword(@RequestBody UserDto userDto) {
        return new ResponseBean<>(userService.resetPassword(userDto));
    }

    /**
     * 查询用户列表
     */
    @PostMapping("/findUserList")
    public List<UserAuths> findUserList(@RequestBody UserAuths userAuths) {
        return userAuthsService.findAllList(userAuths);
    }

    //----------------------------------------  共享接口提供给运营端使用 -------------------------------------

    @PostMapping("/os/userList")
    @ApiOperation(value = "获取用户列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = "userVo", value = "用户信息", dataType = "UserVo")
    })
    public ResponseBean<PageInfo<UserDto>> osUser1(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                                   @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                                   @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                                   @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
                                                   @RequestParam(value = "name", required = false, defaultValue = "") String name,
                                                   @RequestParam(value = "deptId", required = false, defaultValue = "") String deptId,
                                                   @RequestParam(value = "deptCode", required = false, defaultValue = "") String deptCode,
                                                   @RequestParam(value = "regionCode", required = false, defaultValue = "") String regionCode,
                                                   @RequestParam(value = "randomStr", required = false, defaultValue = "") String randomStr,
                                                   @RequestBody UserVo userVo) {
        if (OsVo.isWrongRandomStr(randomStr)) return OsVo.ERR_RESPONSE_BEAN;

        return userList(pageNum, pageSize, sort, order, name, deptId, deptCode, regionCode,"", "","1","", userVo);
    }


    //----------------------------------------  运营端注册用户 -------------------------------------

    /**
     * 手机端注册用户
     *
     * @param userDto userDto
     * @return ResponseBean
     * @author aswl.com
     * @date 2019/01/10 22:35
     */
    @ApiOperation(value = "手机端注册用户", notes = "手机端注册用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "grant_type", value = "授权类型（password、mobile）", required = true, defaultValue = "password", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "code", value = "验证码", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "randomStr", value = "随机数", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "mobile", value = "手机号", dataType = "String", paramType = "query")
    })
    @PostMapping("registerUser")
    @Log(value = "注册用户", businessType = BusinessType.INSERT)
    public ResponseBean<Boolean> registerUser(@RequestBody @Valid UserDto userDto) {
        return new ResponseBean<>(userService.registerUser(userDto));
    }

    /**
     * 验证手机验证码
     *
     * @param mobile
     * @param smsCode
     * @return
     */
    @ApiOperation(value = "验证手机验证码", notes = "验证手机验证码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mobile", value = "手机号", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "smsCode", value = "验证码", required = true, dataType = "String", paramType = "query"),
    })
    @GetMapping(value = "checkSmsCode")
    public ResponseBean<Boolean> checkSmsCode(@RequestParam("mobile") String mobile, @RequestParam("smsCode") String smsCode) {
        Boolean result = userService.checkSmsCode(mobile, smsCode);
        return new ResponseBean<>(result);
    }


    /**
     * 找回密码
     * @param userDto
     * @return
     */
    @PutMapping("findPassword")
    @ApiOperation(value = "找回用户密码", notes = "找回用户密码")
    @ApiImplicitParam(name = "userDto", value = "用户实体user", required = true, dataType = "UserDto")
    @Log(value = "找回密码", businessType = BusinessType.UPDATE)
    public ResponseBean<Boolean> findPassword(@RequestBody UserDto userDto) {
        return new ResponseBean<>(userService.findPassword(userDto) > 0);
    }


    /**
     * 验证租户试用手机号码
     *
     * @param mobile
     * @return boolean
     */
    @ApiOperation(value = "验证租户试用手机号码", notes = "验证租户试用手机号码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mobile", value = "手机号", required = true, dataType = "String", paramType = "query"),
    })
    @GetMapping(value = "checkTrialMobile")
    public ResponseBean<Boolean> checkTrialMobile(@RequestParam("mobile") String mobile) {
        Boolean result = userService.checkTrialMobile(mobile);
        return new ResponseBean<>(result);
    }

    /**
     * 创建系统值班员/项目值班员账号
     *
     * @param dto dto
     * @return ResponseBean
     * @author aswl.com
     * @date 2018/8/26 14:34
     */
    @PostMapping(value = "insertDutyUser")
    @ApiOperation(value = "创建系统值班员/项目值班员账号", notes = "创建系统值班员/项目值班员账号")
    @ApiImplicitParam(name = "userDto", value = "用户实体user", required = true, dataType = "UserDto")
    @Log(value = "创建系统值班员/项目值班员账号", businessType = BusinessType.INSERT)
    public ResponseBean<Boolean> insertDutyUser(@RequestBody DutyUserDto dto) {
        return new ResponseBean<>(userService.insertDutyUser(dto) > 0);
    }


    /**
     * 角色设置/添加用户
     *
     * @param userDto userDto
     * @return Boolean
     */
    @PostMapping(value = "insertRoleUser")
    @ApiOperation(value = "角色设置/添加用户", notes = "角色设置/添加用户")
    @ApiImplicitParam(name = "userDto", value = "用户实体user", required = true, dataType = "UserDto")
    @Log(value = "角色设置/添加用户", businessType = BusinessType.INSERT)
    public ResponseBean<Boolean> insertRoleUser(@RequestBody UserDto userDto) {
        return new ResponseBean<>(userService.insertRoleUser(userDto) > 0);
    }


    /**
     * 角色设置/编辑用户
     *
     * @param userDto userDto
     * @return Boolean
     */
    @PostMapping(value = "updateRoleUser")
    @ApiOperation(value = "角色设置/编辑用户", notes = "角色设置/编辑用户")
    @ApiImplicitParam(name = "userDto", value = "用户实体user", required = true, dataType = "UserDto")
    @Log(value = "角色设置/编辑用户", businessType = BusinessType.UPDATE)
    public ResponseBean<Boolean> updateRoleUser(@RequestBody UserDto userDto) {
        return new ResponseBean<>(userService.updateRoleUser(userDto));
    }


    /**
     * 根据项目ID获取审核人员
     *
     * @param projectId projectId
     * @return UserVo
     */
    @PostMapping(value = "getApproveUsers")
    @ApiOperation(value = "根据项目ID获取审核人员", notes = "根据项目ID获取审核人员")
    @ApiImplicitParam(name = "projectId", value = "项目ID", required = true, dataType = "projectId")
    public ResponseBean<List<UserVo>> getApproveUsers (@RequestParam("projectId") String projectId) {
        return new ResponseBean<>(userService.getApproveUsers(projectId));
    }


    /**
     * 添加项目时添加项目管理员用户
     *
     * @param userDto userDto
     * @return Boolean
     */
    @PostMapping(value = "insertProjectAdminUser")
    @ApiOperation(value = "添加项目时添加项目管理员用户", notes = "添加项目时添加项目管理员用户")
    @ApiImplicitParam(name = "userDto", value = "用户实体user", required = true, dataType = "UserDto")
    @Log(value = "添加项目时添加项目管理员用户", businessType = BusinessType.INSERT)
    public ResponseBean<Boolean> insertProjectAdminUser(@RequestBody UserDto userDto) {
        return new ResponseBean<>(userService.insertProjectAdminUser(userDto) > 0);
    }


    /**
     * 获取项目管理员角色ID
     *
     * @param roleCode 
     * @param tenantCode
     * @return Boolean
     */
    @GetMapping(value = "getRoleId")
    @ApiOperation(value = "获取项目管理员角色ID", notes = "获取项目管理员角色ID")
    @Log(value = "获取项目管理员角色ID", businessType = BusinessType.INSERT)
    public ResponseBean<Role> getRoleId(@RequestParam("roleCode") String roleCode, @RequestParam("tenantCode") String tenantCode) {
        return new ResponseBean<>(userService.getRoleId(roleCode,tenantCode));
    }

    /**
     * 获取发送APP通知的项目管理员/项目值班员
     * @param projectId
     * @return list
     */
    @GetMapping(value = "sendNoticeUsers")
    @ApiOperation(value = "获取发送APP通知的项目管理员/项目值班员", notes = "获取发送APP通知的项目管理员/项目值班员")
    public ResponseBean<List<User>> findSendNoticeUsers( @RequestParam("projectId") String projectId) {
        
        return new ResponseBean<>(userService.findSendNoticeUsers(projectId));
    }


    /**
     * 获取发送邮件不为空的用户
     * @param projectId
     * @return list
     */
    @GetMapping(value = "sendEmailUsers")
    @ApiOperation(value = "获取发送邮件不为空的用户", notes = "获取发送邮件不为空的用户")
    public ResponseBean<List<User>> findEmailNoticeUsers( @RequestParam("projectId") String projectId) {

        return new ResponseBean<>(userService.findSendEmailUsers(projectId));
    }


    /**
     * 根据项目ID查询用户列表
     * @param projectId
     * @return list
     */
    @GetMapping(value = "findUserByProjectId")
    @ApiOperation(value = "根据项目ID查询用户列表", notes = "根据项目ID查询用户列表")
    public ResponseBean<List<User>> findUserByProjectId( @RequestParam("projectId") String projectId) {

        return new ResponseBean<>(userService.findUserByProjectId(projectId));
    }

    /**
     * 更新在线时长
     * @param userDto
     * @return list
     */
    @PostMapping(value = "updateOnlineDuration")
    @ApiOperation(value = "更新在线时长", notes = "更新在线时长")
    public ResponseBean<Boolean> updateOnlineDuration(@RequestBody UserDto userDto) {

        return new ResponseBean<>(userService.updateOnlineDuration(userDto) > 0);
    }

    /**
     * 设置用户告警类型订阅
     * @param AlarmTypeUserFavorite
     * @return list
     */
    @PostMapping(value = "setUserAlarmType")
    @ApiOperation(value = "设置用户告警类型订阅", notes = "设置用户告警类型订阅")
    public ResponseBean<Boolean> setUserAlarmType(@RequestBody AlarmTypeUserFavorite AlarmTypeUserFavorite) {
        return regionServiceClient.updateAlarmTypeUserFavorite(AlarmTypeUserFavorite);
    }

    /**
     * 查看用户订阅的告警类型
     * @param userId
     * @return list
     */
    @GetMapping(value = "findByUserId")
    @ApiOperation(value = "查看用户订阅的告警类型", notes = "查看用户订阅的告警类型")
    public ResponseBean<AlarmTypeUserFavorite> findUserAlarmType(@RequestParam("userId") String userId) {
        return regionServiceClient.findByUserId(userId);
    }
}
