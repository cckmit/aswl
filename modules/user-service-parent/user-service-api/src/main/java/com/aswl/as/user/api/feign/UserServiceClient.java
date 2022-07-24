package com.aswl.as.user.api.feign;

import java.util.List;
import java.util.Map;

import com.aswl.as.user.api.dto.*;
import com.aswl.as.user.api.module.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.aswl.as.common.core.constant.CommonConstant;
import com.aswl.as.common.core.constant.ServiceConstant;
import com.aswl.as.common.core.model.Log;
import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.vo.AttachmentVo;
import com.aswl.as.common.core.vo.DeptVo;
import com.aswl.as.common.core.vo.OsVo;
import com.aswl.as.common.core.vo.UserVo;
import com.aswl.as.common.feign.config.CustomFeignConfig;
import com.aswl.as.user.api.feign.factory.UserServiceClientFallbackFactory;
import com.github.pagehelper.PageInfo;

/**
 * 用户服务
 *
 * @author aswl.com
 * @date 2018-12-30 23:21
 */
@FeignClient(value = ServiceConstant.USER_SERVICE, configuration = CustomFeignConfig.class, fallbackFactory = UserServiceClientFallbackFactory.class)
public interface UserServiceClient {

    /**
     * 根据用户名获取用户详细信息
     *
     * @param identifier identifier
     * @param randomStr  随机字符串
     * @return UserVo
     * @author aswl.com
     * @date 2019/03/17 12:14
     */
    @GetMapping("/v1/user/findUserByIdentifierForLogin/{identifier}")
    ResponseBean<UserVo> findUserByIdentifierForLogin(@PathVariable("identifier") String identifier, @RequestParam("randomStr") String randomStr);

    /**
     * 根据用户名获取用户详细信息
     *
     * @param identifier identifier
     * @param tenantCode 租户标识
     * @return UserVo
     * @author aswl.com
     * @date 2019/03/17 12:14
     */
    @GetMapping("/v1/user/findUserByIdentifier/{identifier}")
    ResponseBean<UserVo> findUserByIdentifier(@PathVariable("identifier") String identifier, @RequestParam("tenantCode") String tenantCode);

    /**
     * 根据用户名获取用户详细信息
     *
     * @param identifier   identifier
     * @param identityType identityType
     * @param tenantCode   租户标识
     * @return UserVo
     * @author aswl.com
     * @date 2019/07/06 14:14:11
     */
    @GetMapping("/v1/user/findUserByIdentifier/{identifier}")
    ResponseBean<UserVo> findUserByIdentifier(@PathVariable("identifier") String identifier, @RequestParam(value = "identityType", required = false) Integer identityType, @RequestParam("tenantCode") String tenantCode);


    /**
     * 获取当前用户的信息
     *
     * @return ResponseBean
     * @author aswl.com
     * @date 2019/03/23 23:44
     */
    @GetMapping("/v1/user/info")
    ResponseBean<UserInfoDto> info();

    /**
     * 根据用户id获取用户
     *
     * @param id
     * @return
     */
    @GetMapping("/v1/user/{id}")
    ResponseBean<User> user(@PathVariable("id") String id);

    /**
     * 根据用户id获取用户
     *
     * @param userVo userVo
     * @return UserVo
     */
    @RequestMapping(value = "/v1/user/findById", method = RequestMethod.POST)
    ResponseBean<List<UserVo>> findUserById(@RequestBody UserVo userVo);

    /**
     * 查询用户数量
     *
     * @param userVo userVo
     * @return ResponseBean
     * @author aswl.com
     * @date 2019/05/09 22:04
     */
    @RequestMapping(value = "/v1/user/userCount", method = RequestMethod.POST)
    ResponseBean<Integer> findUserCount(@RequestBody UserVo userVo);

    /**
     * 根据部门id获取部门
     *
     * @param deptVo deptVo
     * @return ResponseBean
     */
    @RequestMapping(value = "/v1/dept/findById", method = RequestMethod.POST)
    ResponseBean<List<DeptVo>> findDeptById(@RequestBody DeptVo deptVo);

    /**
     * 根据ID删除附件
     *
     * @param id id
     * @return ResponseBean
     * @author aswl.com
     * @date 2019/01/01 20:44
     */
    @DeleteMapping("/v1/attachment/{id}")
    ResponseBean<Boolean> deleteAttachment(@PathVariable(value = "id") String id);

    /**
     * 根据附件id获取附件
     *
     * @param attachmentVo attachmentVo
     * @return ResponseBean
     */
    @RequestMapping(value = "/v1/attachment/findById", method = RequestMethod.POST)
    ResponseBean<List<AttachmentVo>> findAttachmentById(@RequestBody AttachmentVo attachmentVo);

    /**
     * 保存日志
     *
     * @param log log
     * @return ResponseBean
     * @author aswl.com
     * @date 2019/03/23 23:26
     */
    @PostMapping("/v1/log")
    ResponseBean<Boolean> saveLog(@RequestBody Log log);

    /**
     * 根据角色查找菜单
     *
     * @param role       角色
     * @param tenantCode 租户标识
     * @return List
     * @author aswl.com
     * @date 2019/04/08 20:42
     */
    @GetMapping("/v1/menu/findMenuByRole/{role}")
    ResponseBean<List<Menu>> findMenuByRole(@PathVariable("role") String role, @RequestParam("tenantCode") String tenantCode);

    /**
     * 查询所有菜单
     *
     * @param tenantCode 租户标识
     * @return List
     * @author aswl.com
     * @date 2019/04/26 11:48
     */
    @GetMapping("/v1/menu/findAllMenu")
    ResponseBean<List<Menu>> findAllMenu(@RequestParam("tenantCode") String tenantCode);

    /**
     * 根据租户code获取租户的详细信息
     *
     * @param tenantCode 租户标识
     * @return UserVo
     * @author aswl.com
     * @date 2019/05/26 10:21
     */
    @GetMapping("/v1/tenant/findTenantByTenantCode/{tenantCode}")
    ResponseBean<Tenant> findTenantByTenantCode(@PathVariable("tenantCode") String tenantCode);

    /**
     * 注册用户
     *
     * @param userDto userDto
     * @return ResponseBean
     * @author aswl.com
     * @date 2019/07/05 20:57:31
     */
    @PostMapping("/v1/user/register")
    ResponseBean<Boolean> registerUser(@RequestBody UserDto userDto);

    /**
     * 更新用户
     *
     * @param userDto userDto
     * @return ResponseBean
     * @author aswl.com
     * @date 2019/07/05 20:59:06
     */
    @PutMapping("/v1/user")
    ResponseBean<Boolean> updateUser(UserDto userDto);


    /**
     * 更新用户基本信息
     *
     * @param userDto userDto
     * @return ResponseBean
     * @author aswl.com
     * @date 2019/07/05 20:59:06
     */
    @PutMapping("/v1/user/updateInfo")
    ResponseBean<Boolean> updateInfo(UserDto userDto);

    /**
     * 查询系统所有用户
     *
     * @return 用户列表
     */
    @RequestMapping(value = "/v1/user/findUserList", method = RequestMethod.POST)
    List<UserAuths> findUserList(@RequestBody UserAuths userAuths);

    /**
     * 查询系统所有租户 获取所拥有租户，如果是admin 可以获取所有，如果是普通用户，只能获取拥有的租户
     *
     * @return 用户列表
     */
    @RequestMapping(value = "/v1/tenant/getOwnTenantList", method = RequestMethod.POST)
    ResponseBean<List<Tenant>> getOwnTenantList();

    /**
     * 返回是否是云平台
     *
     * @return
     */
    @GetMapping(value = "/v1/config/isCloud")
    ResponseBean<Boolean> findIsCloud(@RequestParam("tenantCode") String tenantCode);

    /**
     * 获取租户对应的所有系统配置
     *
     * @return
     */
    @PostMapping(value = "/v1/config/findConfigList")
    ResponseBean<List<Config>> findConfigList(@RequestBody Config config);


    // ----------------------------    下面是提供给运营端使用的函数 -----------------------------------------------

    /**
     * 获取用户列表
     *
     * @param pageNum
     * @param pageSize
     * @param sort
     * @param order
     * @param name
     * @param deptId
     * @param deptCode
     * @param regionCode
     * @param randomStr
     * @param userVo
     * @return
     */
    @PostMapping("/v1/user/os/userList")
    public ResponseBean<PageInfo<UserDto>> osUser1(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                                   @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                                   @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                                   @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
                                                   @RequestParam(value = "name", required = false, defaultValue = "") String name,
                                                   @RequestParam(value = "deptId", required = false, defaultValue = "") String deptId,
                                                   @RequestParam(value = "deptCode", required = false, defaultValue = "") String deptCode,
                                                   @RequestParam(value = "regionCode", required = false, defaultValue = "") String regionCode,
                                                   @RequestParam(value = "randomStr", required = false, defaultValue = "") String randomStr,
                                                   @RequestBody UserVo userVo);


    /**
     * 运营端把租户传递过来 更新租户
     *
     * @param tenant tenant
     * @return ResponseBean
     */
    @PostMapping("/v1/tenant/os/updateTenant")
    public ResponseBean<Boolean> osTenant1(@RequestParam(value = OsVo.CHECK_STRING, required = false, defaultValue = "") String randomStr,
                                           @RequestBody Tenant tenant);

    /**
     * 批量删除
     *
     * @param tenant tenant
     * @return ResponseBean
     */
    @PostMapping("/v1/tenant/os/deleteAll")
    public ResponseBean<Boolean> osTenant2(
            @RequestParam(value = OsVo.CHECK_STRING, required = false, defaultValue = "") String randomStr, @RequestBody Tenant tenant);

    /**
     * 根据key获取Config
     *
     * @param key
     * @param tenantCode
     * @return
     */
    @GetMapping("/v1/config/feign/findByKey/{key}")
    ResponseBean<Config> findConfigByKey(@PathVariable("key") String key, @RequestParam("tenantCode") String tenantCode);

    /**
     * 更新Config
     *
     * @param configDto
     * @return
     */
    @PutMapping("/v1/config/feign")
    ResponseBean<Boolean> updateConfig(@RequestBody ConfigDto configDto);


    /**
     * 返回树形菜单集合
     *
     * @return ResponseBean
     */
    @GetMapping("/v1/menu/menus")
    ResponseBean<List<MenuDto>> menus();

    /**
     * 发送短信
     *
     * @return ResponseBean
     */
    @PostMapping("/v1/sms/sendSms")
    ResponseBean<SmsResponse> sendSms(@RequestBody SmsDto smsDto);


    /**
     * 根据项目ID获取审核人员
     *
     * @return ResponseBean
     */
    @PostMapping("/v1/user/getApproveUsers")
    ResponseBean<List<UserVo>> getApproveUsers(@RequestParam("projectId") String projectId);


    /**
     * 添加项目时添加项目管理员用户
     *
     * @return ResponseBean
     */
    @PostMapping("/v1/user/insertProjectAdminUser")
    ResponseBean<Boolean> insertProjectAdminUser(@RequestBody UserDto userDto);

    /**
     * 获取项目管理员角色ID
     *
     * @param roleCode
     * @param tenantCode
     * @return ResponseBean
     */
    @GetMapping("/v1/user/getRoleId")
    ResponseBean<Role> getRoleId(@RequestParam("roleCode") String roleCode, @RequestParam("tenantCode") String tenantCode);


    /**
     * 批量删除用户
     *
     * @param user
     * @return ResponseBean
     */
    @PostMapping("/v1/user/deleteAll")
    ResponseBean<Boolean> deleteAllUsers(@RequestBody User user);

    /**
     * 检查账号是否存在
     *
     * @param identifier
     * @return ResponseBean
     */
    @GetMapping("/v1/user/checkExist/{identifier}")
    ResponseBean<Boolean> checkExist(@PathVariable("identifier") String identifier);


    /**
     * 获取发送APP通知的项目管理员/项目值班员
     *
     * @param projectId
     * @return ResponseBean
     */
    @GetMapping("/v1/user/sendNoticeUsers")
    ResponseBean<List<User>> findSendNoticeUsers(@RequestParam("projectId") String projectId);

    /**
     * 获取发送邮件不为空的用户
     *
     * @param projectId
     * @return ResponseBean
     */
    @GetMapping("/v1/user/sendEmailUsers")
    ResponseBean<List<User>> findSendEmailUsers(@RequestParam("projectId") String projectId);


    /**
     * 根据项目ID查询用户列表
     *
     * @param projectId
     * @return ResponseBean
     */
    @GetMapping("/v1/user/findUserByProjectId")
    ResponseBean<List<User>> findUserByProjectId(@RequestParam("projectId") String projectId);
    
}
