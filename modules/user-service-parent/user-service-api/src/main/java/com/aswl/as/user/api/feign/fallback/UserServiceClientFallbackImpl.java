package com.aswl.as.user.api.feign.fallback;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.aswl.as.user.api.dto.*;
import com.aswl.as.user.api.module.*;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import com.aswl.as.common.core.model.Log;
import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.vo.AttachmentVo;
import com.aswl.as.common.core.vo.DeptVo;
import com.aswl.as.common.core.vo.UserVo;
import com.aswl.as.user.api.feign.UserServiceClient;
import com.github.pagehelper.PageInfo;

import lombok.extern.slf4j.Slf4j;

/**
 * 日志断路器实现
 *
 * @author aswl.com
 * @date 2019/3/23 23:39
 */
@Slf4j
@Component
public class UserServiceClientFallbackImpl implements UserServiceClient {

    private Throwable throwable;

    /**
     * 根据用户名查询用户信息
     *
     * @param identifier identifier
     * @param randomStr 随机字符串
     * @return UserVo
     */
    @Override
    public ResponseBean<UserVo> findUserByIdentifierForLogin(String identifier, String randomStr) {
        log.error("feign 查询用户信息失败:{}, {}",  identifier, throwable);
        return null;
    }

    /**
     * 根据用户名查询用户信息
     *
     * @param identifier identifier
     * @param tenantCode 租户标识
     * @param tenantCode 租户标识
     * @return UserVo
     */
    @Override
    public ResponseBean<UserVo> findUserByIdentifier(String identifier, String tenantCode) {
        log.error("feign 查询用户信息失败:{}, {}, {}", tenantCode, identifier, throwable);
        return null;
    }

    /**
     * 根据用户名查询用户信息
     *
     * @param identifier   identifier
     * @param identityType identityType
     * @param tenantCode   租户标识
     * @return UserVo
     */
    @Override
    public ResponseBean<UserVo> findUserByIdentifier(String identifier, Integer identityType, String tenantCode) {
        log.error("feign 查询用户信息失败:{}, {}, {}, {}", tenantCode, identityType, identifier, throwable);
        return null;
    }

    /**
     * 查询当前登录的用户信息
     *
     * @return ResponseBean
     */
    @Override
    public ResponseBean<UserInfoDto> info() {
        log.error("feign 查询用户信息失败:{},{}", throwable);
        return null;
    }

    @Override
    public ResponseBean<User> user(String id) {
        log.error("feign 查询用户信息失败:{},{}", id,throwable);
        return null;
    }

    /**
     * 根据用户ID批量查询用户信息
     *
     * @param userVo userVo
     * @return ResponseBean
     */
    @Override
    public ResponseBean<List<UserVo>> findUserById(@RequestBody UserVo userVo) {
        log.error("调用{}异常:{},{}", "findById", userVo, throwable);
        return null;
    }

    /**
     * 查询用户数量
     *
     * @param userVo userVo
     * @return ResponseBean
     */
    @Override
    public ResponseBean<Integer> findUserCount(UserVo userVo) {
        log.error("调用{}异常:{},{}", "findUserCount", userVo, throwable);
        return new ResponseBean<>(0);
    }

    /**
     * 根据部门ID批量查询部门信息
     *
     * @param deptVo deptVo
     * @return ResponseBean
     */
    @Override
    public ResponseBean<List<DeptVo>> findDeptById(@RequestBody DeptVo deptVo) {
        log.error("调用{}异常:{},{}", "findById", deptVo, throwable);
        return null;
    }

    /**
     * 根据附件ID删除附件
     *
     * @param id id
     * @return ResponseBean
     */
    @Override
    public ResponseBean<Boolean> deleteAttachment(String id) {
        log.error("调用{}异常:{},{}", "delete", id, throwable);
        return new ResponseBean<>(Boolean.FALSE);
    }

    /**
     * 根据附件ID批量查询附件信息
     *
     * @param attachmentVo attachmentVo
     * @return ResponseBean
     */
    @Override
    public ResponseBean<List<AttachmentVo>> findAttachmentById(AttachmentVo attachmentVo) {
        log.error("调用{}异常:{},{}", "findById", attachmentVo, throwable);
        return new ResponseBean<>(new ArrayList<>());
    }

    /**
     * 保存日志
     *
     * @param logInfo logInfo
     * @return ResponseBean
     */
    @Override
    public ResponseBean<Boolean> saveLog(Log logInfo) {
        log.error("feign 插入日志失败,{}", throwable);
        return null;
    }

    /**
     * 根据角色查找菜单
     *
     * @param tenantCode 租户标识
     * @param role       角色
     * @return List
     */
    @Override
    public ResponseBean<List<Menu>> findMenuByRole(String role, String tenantCode) {
        log.error("feign 获取角色菜单失败, {}, {}", tenantCode, throwable);
        return new ResponseBean<>(new ArrayList<>());
    }

    /**
     * 查询所有菜单
     *
     * @param tenantCode 租户标识
     * @return List
     */
    @Override
    public ResponseBean<List<Menu>> findAllMenu(String tenantCode) {
        log.error("feign 获取所有菜单失败, {}, {}", tenantCode, throwable);
        return new ResponseBean<>(new ArrayList<>());
    }

    /**
     * 根据租户标识查询租户详细信息
     *
     * @param tenantCode 租户标识
     * @return Tenant
     */
    @Override
    public ResponseBean<Tenant> findTenantByTenantCode(String tenantCode) {
        log.error("feign 获取租户详细信息失败, {}, {}", tenantCode, throwable);
        return null;
    }

    /**
     * 注册用户
     *
     * @param userDto userDto
     * @return ResponseBean
     */
    @Override
    public ResponseBean<Boolean> registerUser(UserDto userDto) {
        log.error("feign 注册用户失败, {}, {}, {}", userDto.getIdentityType(), userDto.getIdentifier(), throwable);
        return null;
    }

    /**
     * 更新用户
     *
     * @param userDto userDto
     * @return ResponseBean
     */
    @Override
    public ResponseBean<Boolean> updateUser(UserDto userDto) {
        log.error("feign 更新用户失败, {}, {}, {}", userDto.getIdentityType(), userDto.getIdentifier(), throwable);
        return null;
    }

    /**
     * 更新用户基本信息
     *
     * @param userDto userDto
     * @return ResponseBean
     */
    @Override
    public ResponseBean<Boolean> updateInfo(UserDto userDto) {
        log.error("feign 更新用户基本信息失败, {}, {}, {}", userDto.getId(), userDto.getIdentifier(), throwable);
        return null;
    }
    /**
     * 查询用户列表数据
     */
	@Override
	public List<UserAuths> findUserList(UserAuths userAuths) {
        log.error("feign 查询用户列表失败, {}", throwable);
		return null;
	}

    /**
     * 获取所拥有租户，如果是admin 可以获取所有，如果是普通用户，只能获取拥有的租户
     * @return 租户列表
     */
    public ResponseBean<List<Tenant>> getOwnTenantList()
    {
        log.error("feign 查询用户列表失败, {}", throwable);
        return null;
    }

    /**
     * 获取是否部署到公网
     * @return
     */
    public ResponseBean<Boolean> findIsCloud(String findIsCloud){
        log.debug("feign 查询是否云端失败");
        return null;
    }

    /**
     * 根据条件 查询所有系统配置信息表 列表
     * @param config
     * @return
     */
    public ResponseBean<List<Config>> findConfigList(Config config)
    {
        log.debug("feign 根据条件 查询所有系统配置信息表 列表 失败");
        return null;
    }

    /**
     * 获取用户列表
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
    public ResponseBean<PageInfo<UserDto>> osUser1(String pageNum, String pageSize, String sort, String order, String name, String deptId, String deptCode, String regionCode, String randomStr, UserVo userVo)
    {
        log.debug("feign 获取用户列表失败, {}", throwable);
        return null;
    }


    /**
     * 运营端把租户传递过来 更新租户
     *
     * @param tenant tenant
     * @return ResponseBean
     */
    public ResponseBean<Boolean> osTenant1(String randomStr, Tenant tenant) {
        log.debug("feign 更新租户失败, {}, {}", tenant,throwable);
        return null;
    }

    /**
     * 批量删除租户
     *
     * @param tenant tenant
     * @return ResponseBean
     */
    public ResponseBean<Boolean> osTenant2(String randomStr, Tenant tenant) {
        log.debug("feign 删除租户失败, {}, {}", tenant,throwable);
        return null;
    }


    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }

    /**
     * 根据key获取Config
     * @param key
     * @param tenantCode
     * @return
     */
    @Override
    public ResponseBean<Config> findConfigByKey(String key, String tenantCode) {
        log.error("feign 根据key获取Config失败", throwable);
        return null;
    }

    /**
     * 更新Config
     * @param configDto
     * @return
     */
    @Override
    public ResponseBean<Boolean> updateConfig(ConfigDto configDto) {
        log.error("feign 更新Config失败", throwable);
        return null;
    }

    @Override
    public ResponseBean<List<MenuDto>> menus() {
        log.error("获取返回树形菜单集合失败", throwable);
        return null;
    }

    @Override
    public ResponseBean<SmsResponse> sendSms(SmsDto smsDto) {
        log.error("发送短信失败", throwable);
        return null;
    }

    @Override
    public ResponseBean<List<UserVo>> getApproveUsers(String projectId) {
        log.error("根据项目ID获取审核人员失败 {}, {}, {}",projectId , throwable);
        return null;
    }

    @Override
    public ResponseBean<Boolean> insertProjectAdminUser(UserDto userDto) {
        log.error("添加项目时添加项目管理员用户失败 {}, {}, {}",userDto , throwable);
        return null;
    }

    @Override
    public ResponseBean<Role> getRoleId(String roleCode, String tenantCode) {
        log.error("获取项目管理员角色ID失败 {}, {}, {}",roleCode ,tenantCode , throwable);
        return null;
    }

    @Override
    public ResponseBean<Boolean> deleteAllUsers(User user) {
        log.error("批量删除用户失败 {}, {}, {}",user , throwable);
        return null;
    }

    @Override
    public ResponseBean<Boolean> checkExist(String identifier) {
        log.error("检查账号是否存在失败 {}, {}, {}",identifier , throwable);
        return null;
    }

    @Override
    public ResponseBean<List<User>> findSendNoticeUsers(String projectId) {
        log.error("获取发送APP通知的项目管理员/项目值班员失败 {}, {}, {}",projectId , throwable);
        return null;
    }

    @Override
    public ResponseBean<List<User>> findSendEmailUsers(String projectId) {
        log.error("获取发送邮件不为空的用户失败 {}, {}, {}",projectId , throwable);
        return null;
    }

    @Override
    public ResponseBean<List<User>> findUserByProjectId(String projectId) {
        log.error("根据项目ID查询用户列表失败 {}, {}, {}",projectId , throwable);
        return null;
    }
}
