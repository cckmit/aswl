package com.aswl.as.asos.modules.asuser.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.aswl.as.asos.common.exception.CommonException;
import com.aswl.as.asos.common.exception.RRException;
import com.aswl.as.asos.common.utils.*;
import com.aswl.as.asos.modules.asuser.entity.*;
import com.aswl.as.asos.modules.asuser.service.*;
import com.aswl.as.asos.modules.ibrs.entity.AsProject;
import com.aswl.as.asos.modules.ibrs.service.IAsProjectService;
import com.aswl.as.common.core.constant.CommonConstant;
import com.aswl.as.common.core.dto.SysConfigDto;
import com.aswl.as.common.core.enums.RoleEnum;
import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.utils.IdGen;
import com.aswl.as.asos.datasource.annotation.DataSource;
import com.aswl.as.asos.modules.asuser.mapper.SysTenantMapper;
import com.aswl.as.common.core.utils.NumberUtil;
import com.aswl.as.common.core.utils.Pinyin4jUtil;
import com.aswl.as.common.core.utils.SysUtil;
import com.aswl.as.common.core.vo.OsVo;
import com.aswl.as.ibrs.api.feign.RegionServiceClient;
import com.aswl.as.ibrs.api.module.Region;
import com.aswl.as.user.api.constant.RoleConstant;
import com.aswl.as.user.api.enums.IdentityType;
import com.aswl.as.user.api.feign.UserServiceClient;
import com.aswl.as.user.api.module.Role;
import com.aswl.as.user.api.module.RoleMenu;
import com.aswl.as.user.api.module.Tenant;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.implementation.bytecode.Throw;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * SAAS租户信息表 服务实现类
 * </p>
 *
 * @author hfx
 * @since 2019-11-18
 */
@Service
@Slf4j
@AllArgsConstructor
public class SysTenantServiceImpl extends ServiceImpl<SysTenantMapper, SysTenant> implements ISysTenantService {

    private UserServiceClient userServiceClient;

    private static final PasswordEncoder encoder = new BCryptPasswordEncoder();

    @Autowired
    IAsUserSysRoleService iAsUserSysRoleService;

    @Autowired
    IAsUserSysDeptService iAsUserSysDeptService;

    @Autowired
    IAsUserSysPostService iAsUserSysPostService;

    @Autowired
    IAsUserSysPositionService iAsUserSysPositionService;

    @Autowired
    IAsUserSysUserService iAsUserSysUserService;

    @Autowired
    IAsUserSysUserAuthsService iAsUserSysUserAuthsService;

    @Autowired
    IAsUserSysUserRoleService iAsUserSysUserRoleService;

    @Autowired
    IAsUserSysMenuService iAsUserSysMenuService;

    @Autowired
    ISysAppMenuService iSysAppMenuService;

    @Autowired
    IAsUserSysRoleMenuService iAsUserSysRoleMenuService;

    @Autowired
    ISysAppRoleMenuService iSysAppRoleMenuService;


    @Autowired
    IAsUserSysAttachmentService iAsUserSysAttachmentService;

    @Autowired
    IAsProjectService iAsProjectService;

    @Autowired
    ISysTenantLogService iSysTenantLogService;

    @Autowired
    ISysTenantRoleService iSysTenantRoleService;
    
    @Autowired
    SysTenantMapper sysTenantMapper;
    
    @Autowired
    ISysConfigService iSysConfigService;

    /**
     * 测试远程
     * @return List<Region>
     */
    public Region getRegionListByUsers(){

//        System.out.println("远程获得的数据为"+JSON.toJSON(userServiceClient.findAllMenu("aswl")));
//        System.out.println(userServiceClient.findUserByIdentifier("admin", "admin"));
//        userServiceClient.findUserById();

        return null;
        //return  regionServiceClient.getRegionById("6");
    }

    @DataSource("slave2")
    public PageUtils queryPage(Map<String, Object> params)
    {
        /*
        String projectName = (String)params.get("projectName");
        String projectDes = (String)params.get("projectDes");
        String projectOwner = (String)params.get("projectOwner");
        */

        IPage<SysTenant> page = this.page(
            new Query<SysTenant>().getPage(params),
                new QueryWrapper<SysTenant>().eq("del_flag","0")
                /*
                .like(StringUtils.isNotBlank(projectName),"project_name",projectName)
                .like(StringUtils.isNotBlank(projectDes),"project_des",projectDes)
                .like(StringUtils.isNotBlank(projectOwner),"project_owner",projectOwner)
                */
            );

        // 如果查出来的数据，是激活的话，就设置状态为已过期
        for(SysTenant temp:page.getRecords())
        {
            checkEffective(temp);
        }

        return new PageUtils(page);
    }

    //根据id返回对应信息
    @DataSource("slave2")
    public SysTenant getEntityById(String id)
    {
        return this.getById(id);
    }

    @DataSource("slave2")
    public boolean saveEntity(SysTenant entity)
    {
        // 验证试用号码是否存在
        if (entity.getTrialMobile() != null) {
            String[] mobiles = entity.getTrialMobile().split("/");
            for (String s:mobiles) {
                int count = this.getMobileExist(s,null);
                if (count > 0){
                    throw new RRException("试用手机号" +s + " 已存在,请更新其他手机号码");
                }
            }
        }
        entity.setId(IdGen.snowflakeId());
        entity.setDelFlag("0");//新创建的不会被删除
        entity.setStatus("0");
        entity.setModifier(null);
        entity.setModifyDate(null);

        //使用租户名称首字母来生成code
        //String tempCode= Pinyin4jUtil.getFirstSpell(entity.getTenantCode());
        String tempCode= Pinyin4jUtil.getFirstSpell(entity.getTenantName());
        generateProjectCodeCode(entity,tempCode);

        return this.save(entity);
    }

    // 添加 启用 锁定 续期 功能接口，只能在这些接口操作状态，并且记录
    @DataSource("slave2")
    public boolean enableTenant(SysTenant entity)
    {
        // 启用账号
        //查询原来的对象出来操作，也可以判断
        entity.setStatus("1");

        //TODO 查找出原来的
        SysTenant old=getById(entity.getId());

        LocalDateTime now=LocalDateTime.now();
        if(old.getEffectiveEndTime()!=null && now.isBefore(old.getEffectiveEndTime()) )
        {
            //根据传过来的东西，设置生效结束时间
            entity.setEffectiveEndTime(getEffectiveEndTime(old.getEffectiveStartTime(),old.getValidCount(),old.getValidUnit()));
        }
        else
        {
            //根据传过来的东西，设置生效结束时间
            entity.setEffectiveEndTime(getEffectiveEndTime(entity.getEffectiveStartTime(),entity.getValidCount(),entity.getValidUnit()));
        }

        // 如果是有原来就有时间的激活，就在这里设置validCount为0
        if(old.getEffectiveEndTime()!=null && old.getEffectiveEndTime().isAfter(LocalDateTime.now()))
        {
            entity.setValidCount(0);
            entity.setValidUnit("m");
        }

        entity.setValidTime(getValidTime(entity));
        updateEntityById(entity);
        //保存日志
        SysTenantLog tenantLog=new SysTenantLog(entity);
        iSysTenantLogService.saveEntity(tenantLog);

        return true;
    }

    @DataSource("slave2")
    public boolean disableTenant(SysTenant entity)
    {
        //TODO 锁定账号
        //查询原来的对象出来操作，也可以判断

        //根据传过来的东西，设置生效结束时间
        entity.setStatus("0");

        updateEntityById(entity);
        entity.setValidCount(0);
        entity.setValidUnit("m");

        //保存日志
        SysTenantLog tenantLog=new SysTenantLog(entity);
        iSysTenantLogService.saveEntity(tenantLog);

        return true;
    }

    //续租
    @DataSource("slave2")
    public boolean renewTenant(SysTenant entity)
    {
        // 如果是还没过期，就用生效结束时间来当结束时间，否则会直接传过来

        //判断旧的有效期是否已过期
        SysTenant old=this.getById(entity.getId());

        if(old.getEffectiveEndTime()==null || old.getEffectiveEndTime().isBefore(LocalDateTime.now()))
        {
            //过期了的话，就用现在的生效日期,使用现在服务器时间来算
            entity.setEffectiveStartTime(LocalDateTime.now());
            entity.setEffectiveEndTime(getEffectiveEndTime(entity.getEffectiveStartTime(),entity.getValidCount(),entity.getValidUnit()));
            if("2".equals(entity.getStatus()))
            {
                entity.setStatus("1");//如果状态为过期，就设置为激活
            }
        }
        else
        {
            //还没过期
            entity.setEffectiveStartTime(old.getEffectiveEndTime());
            entity.setEffectiveEndTime(getEffectiveEndTime(old.getEffectiveEndTime(),entity.getValidCount(),entity.getValidUnit()));
        }

        entity.setValidTime(getValidTime(entity));
        updateEntityById(entity);
        SysTenantLog tenantLog=new SysTenantLog(entity);
        tenantLog.setStatus("3");//续期，续期只会出现在log里面，租户状态依然是1
        return iSysTenantLogService.saveEntity(tenantLog);
    }

    @DataSource("slave2")
    public int getMobileExist(String mobile, String id) {
        
        return  sysTenantMapper.getMobileExist(mobile,id);
    }

    @DataSource("slave2")
    public SysTenant findTenantByTenantCode(String tenantCode) {
        return sysTenantMapper.findTenantByTenantCode(tenantCode);
    }

    // 在这里算最后的时间
    private String getValidTime(SysTenant tenant)
    {
        SysTenantLog log=new SysTenantLog();
        log.setTenantCode(tenant.getTenantCode());

        //未设置的话，就返回空
        LocalDateTime now=LocalDateTime.now();
        if(tenant.getEffectiveEndTime()==null)
        {
            return "";
        }

        List<SysTenantLog> list=iSysTenantLogService.findList(log);
        int monthCount=0;
        if (list.size()>0)
        {
            // 根据启用续费的日志来获取数据
            // 获取有效的条数

            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime lastTime=list.get(0).getEffectiveEndTime();

            for(int i=0;i<list.size();i++)
            {
                SysTenantLog temp=list.get(i);
                //禁用不用累加
                if(lastTime.isAfter(LocalDateTime.parse(temp.getCreateDate(),df)))
                {
                    //累加
                    monthCount+=getMonthCount(temp.getValidCount(),temp.getValidUnit());
                }
                else
                {
                    //重新开始，将y和m设置为本元素的值
                    monthCount=getMonthCount(temp.getValidCount(),temp.getValidUnit());
                }
                lastTime=temp.getEffectiveEndTime();
            }

            //如果前面累加的都已过期了，就清空
            if(now.isAfter(lastTime))
            {
                monthCount=0;
            }

            //判断是否普通启用
            DateTimeFormatter shortDf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            //相等的话就不加,就是普通的启用
            if(! shortDf.format(tenant.getEffectiveEndTime()).equals(shortDf.format(list.get(list.size()-1).getEffectiveEndTime())))
            {
                //不相等的话，就加上tenant传进来的
                monthCount+=getMonthCount(tenant.getValidCount(),tenant.getValidUnit());
            }
        }
        else
        {
            //没有的话，直接返回现在的数
            monthCount=getMonthCount(tenant.getValidCount(),tenant.getValidUnit());
        }

        //除以12就是年数，余以12就是月数
        int y=monthCount/12;
        int m=monthCount%12;

        if(m==0&& y==0)
        {
            return "";
        }
        if(m==0)
        {
            return y+"年";
        }
        if(y==0)
        {
            return m+"月";
        }

        return y+"年"+m+"月";
    }

    //全部转化成月来算
    private int getMonthCount(Integer validCount,String validUnit)
    {
        if(validCount!=null)
        {
            if("y".equals(validUnit))
            {
                return validCount*12;
            }
            if("m".equals(validUnit))
            {
                return validCount;
            }
        }
        return 0;
    }

    private LocalDateTime getEffectiveEndTime(LocalDateTime effectiveStartTime,Integer validCount,String validUnit)
    {
        if("y".equals(validUnit))
        {
            return effectiveStartTime.plus(validCount, ChronoUnit.YEARS);
        }
        if("m".equals(validUnit))
        {
            return effectiveStartTime.plus(validCount, ChronoUnit.MONTHS);
        }
        if("d".equals(validUnit))
        {
            return effectiveStartTime.plus(validCount, ChronoUnit.DAYS);
        }
        throw new CommonException("传递过来的使用年限单位错误");
    }

    private void checkEffective(SysTenant temp)
    {
        if("1".equals(temp.getStatus()) && temp.getEffectiveEndTime()!=null && temp.getEffectiveEndTime().isBefore(LocalDateTime.now()) ) //超过有效期并且等于1
        {
            //设置为已过期
            temp.setStatus("2");
            updateEntityById(temp);
        }
    }

    @DataSource("slave2")
    public boolean updateEntityById(SysTenant entity)
    {
        // 更新租户信息，发送到user更新 不然那边设置了缓存,要更新那边的缓存，可能不是用同一个redis，所以不能直接改redis，所以用feign直接请求过去更新

//        boolean b=this.updateById(entity);
        // 验证试用号码是否存在
        if (entity.getTrialMobile() != null) {
            String[] mobiles = entity.getTrialMobile().split("/");
            for (String s:mobiles) {
                int count = this.getMobileExist(s,entity.getId());
                if (count > 0){
                    throw new RRException("试用手机号" +s + " 已存在,请更新其他手机号码");
                }
            }
        }
        Tenant tenant=new Tenant();
        BeanUtils.copyProperties(entity, tenant);
        tenant.setCreateDate(DateUtils.stringToDate(entity.getCreateDate(),DateUtils.DATE_TIME_PATTERN));
        tenant.setModifyDate(new Date());

        tenant.setValidCount(entity.getValidCount());
        tenant.setValidUnit(entity.getValidUnit());
        tenant.setValidTime(entity.getValidTime());
        if(entity.getEffectiveStartTime()!=null)
        {
            tenant.setEffectiveStartTime(Date.from(entity.getEffectiveStartTime().atZone(ZoneId.systemDefault()).toInstant()));
        }
        if(entity.getEffectiveEndTime()!=null)
        {
            tenant.setEffectiveEndTime(Date.from(entity.getEffectiveEndTime().atZone(ZoneId.systemDefault()).toInstant()));
        }
        tenant.setMaxDeviceCount(entity.getMaxDeviceCount());
        tenant.setTenantRoleId(entity.getTenantRoleId());
        tenant.setTenantRoleName(entity.getTenantRoleName());
        tenant.setTrialMobile(entity.getTrialMobile());
        ResponseBean<Boolean> r= userServiceClient.osTenant1(OsVo.getRandomStr(),tenant);

        if(r!=null )
        {
            Boolean b=r.getData();
            if( ResponseBean.SUCCESS == r.getCode() && b )
            {
                updateSysAttachmentBySysTenant(entity);
                //移除租户编码的key，该key的数据需要重新查
                OsGlobalData.COMMON_TENANT_NAME_MAP.remove(entity.getTenantCode());

                return b;
            }
            else
            {
                throw new RRException(r.getMsg());
            }
        }
        else
        {
            throw new RRException("更新租户失败");
        }
    }

    @DataSource("slave2")
    public boolean update(SysTenant entity){
        // 查询租户角色
        SysTenant tenant = this.findTenantByTenantCode(entity.getTenantCode());
        //获取租户角色是否发生变化
        if (tenant.getTenantRoleId() ==  null  || !tenant.getTenantRoleId().equals(entity.getTenantRoleId())){
            //先根据租户编码删除菜单
            iAsUserSysMenuService.deleteByTenantCode(entity.getTenantCode());
            // 根据租户角色ID查询拥有菜单
            List<AsUserSysMenu> list = iSysTenantRoleService.findMenuByRoleId(entity.getTenantRoleId());
            LocalDateTime now=LocalDateTime.now();
            Map<String,String> idMap=new HashMap<String,String>();
            //重新增加菜单
            for(AsUserSysMenu temp:list)
            {
                String newId=IdGen.snowflakeId();
                idMap.put(temp.getId(),newId);
                temp.setId(newId);
                temp.setCreator(entity.getCreator());
                temp.setCreateDate(now);
                temp.setTenantCode(entity.getTenantCode());
                temp.setModifier(null);
                temp.setModifyDate(null);
            }
            for(AsUserSysMenu temp:list)
            {
                //替换父id
                if(idMap.containsKey(temp.getParentId()))
                {
                    temp.setParentId(idMap.get(temp.getParentId()));
                }
                iAsUserSysMenuService.saveEntityNotIdGen(temp);
            }
            // 根据租户编码查询角色ID
            List<AsUserSysRole> userSysRole = iAsUserSysRoleService.findRoleByTenantCode(entity.getTenantCode());
            if (null != userSysRole){
                for (AsUserSysRole role: userSysRole) {
                    // 根据角色ID删除角色菜单
                    iAsUserSysRoleMenuService.deleteByRoleId(role.getId());
                    // 如果是系统管理员则添加新的菜单角色关联数据
                    if((RoleEnum.ROLE_SYS_ADMIN + "_"+ entity.getTenantCode()).equalsIgnoreCase(role.getRoleCode())){
                        saveAsUserSysRoleMenu(list,role);
                    }else if((RoleEnum.ROLE_PROJECT_ADMIN +"_"+ entity.getTenantCode()).equalsIgnoreCase(role.getRoleCode())){
//                        saveAsUserSysProjectRoleMenu(list,role);
                        saveAsUserSysRoleMenu(list,role);   //项目管理员更改为拥有所有菜单20210918
                    }
                }
            }
            return this.updateEntityById(entity);
        }else{
           return this.updateEntityById(entity);   
        }
    }

    @DataSource("slave2")
    public boolean deleteEntityById(String id)
    {
        // 现在不直接删
        setDelFlag(id,"1");
        return true;
        //return this.removeById(id);
    }

    @DataSource("slave2")
    public boolean deleteEntityByIds(String[] ids)
    {
        //这里并不是真正的删除，只是设置delFlag 为1

        // 判断该项目下面有没有没删除的项目，如果有，不能删除

        for(String id:ids)
        {
            setDelFlag(id,"1");
        }


        return true;
    }

    @DataSource("slave2")
    public SysTenant getSysTenantByTenantCode(String tenantCode)
    {
        return baseMapper.selectOne(new QueryWrapper<SysTenant>().eq("tenant_code",tenantCode));
    }

    //设置租户禁用或者激活
    @DataSource("slave2")
    public boolean updateStatus(String[] tenantCodes, String status)
    {
        SysTenant t=new SysTenant();
        t.setStatus(status);
        return this.update(t,new QueryWrapper<SysTenant>().in("tenant_code",tenantCodes));
    }

    /**
     * 自动生成项目编码
     * @param sysTenant 项目
     */
    @DataSource("slave2")
    public void generateProjectCodeCode(SysTenant sysTenant, String tempCode) {

        SysTenant temp=new SysTenant();
        temp.setTenantCode(tempCode);
        temp=baseMapper.selectTenantForTenantCode(temp);
        if(temp==null|| org.apache.commons.lang.StringUtils.isEmpty(temp.getTenantCode()))
        {
            sysTenant.setTenantCode(tempCode+"001");
        }
        else
        {
            sysTenant.setTenantCode(NumberUtil.addOne(temp.getTenantCode()));
        }
    }

    // 需要修改创建角色 部门 职位 岗位 等等 ，并绑定对应的数据， 创建人员资料,登录账号，分配权限，创建用户
    @Transactional //测试事务，在同一个表里面 ，能一起提交
    @DataSource("slave2")
    public boolean insertTenantData(SysTenant entity) throws Exception
    {
        // 系统管理员角色
        AsUserSysRole roleSysAdmin=null;
        
        //项目管理员角色
        AsUserSysRole roleProjectAdmin=null;
       
        
        AsUserSysDept dept=null;

        AsUserSysPost post=null;

        AsUserSysPosition position=null;

        AsUserSysUser user=null;

        AsUserSysUserAuths auths=null;

        AsUserSysUserRole userRole=null;

        List<AsUserSysMenu> menuList=null;

        List<AsUserSysRoleMenu> roleMenuList=null;

        List<SysAppMenu> appMenuList=null;


        try
        {

            saveEntity(entity);

            // 如果有上传图片，更新图片所属的租户
            updateSysAttachmentBySysTenant(entity);
            // 添加创建角色
           // role=saveAsUserSysRole(entity);
            
            // 系统管理员角色
            roleSysAdmin = saveRoleSysAdmin(entity);
            // 系统值班员角色 (没有分配菜单)
              saveRoleSysWatcher(entity);
            //项目管理员角色
            roleProjectAdmin =saveRoleProjectAdmin(entity);
            //项目值班员角色 (没有分配菜单)
             saveRoleProjectWatcher(entity);
            //区域负责人角色 (没有分配菜单)
            saveRoleProjectRegion(entity);
            //创建部门
            dept=saveAsUserSysDept(entity);

            //创建岗位
            post=saveAsUserSysPost(entity);

            //创建职位
            position=saveAsUserSysPosition(entity,post);

            //创建人员资料(头像默认为空)
            user=saveAsUserSysUser(entity,dept,position);

            //创建登录账号
            auths=saveAsUserSysUserAuths(entity,user);

            //用户绑定角色
            userRole=saveAsUserSysUserRole(roleSysAdmin,user);

            //添加菜单 （暂时复制aswl的菜单）  未知想要什么样的菜单
            menuList=saveAsUserSysMenu(entity);

            //角色拥有的菜单  修改添加新的角色菜单 (系统管理员)
            roleMenuList=saveAsUserSysRoleMenu(menuList,roleSysAdmin);
            
            
            //角色拥有的菜单  修改添加新的角色菜单 (项目管理员)

            roleMenuList=saveAsUserSysRoleMenu(menuList,roleProjectAdmin);
            //saveAsUserSysProjectRoleMenu(menuList,roleProjectAdmin);
            

            //添加APP菜单 （暂时复制aswl的菜单）
            appMenuList=saveAsUserSysAppMenu(entity);

            //给新创建的用户添加菜单权限
            saveAsUserSysAppRoleMenu(appMenuList,roleSysAdmin);
            
            // 添加默认系统配置
            saveSysConfig(entity);

        }
        catch (Exception e)
        {

            e.printStackTrace();
            System.out.println(e.getMessage());

            throw e;
        }

        return true;
    }

    @DataSource("slave2")
    public List<SysTenant> findList(SysTenant entity)
    {

        QueryWrapper<SysTenant> q=new QueryWrapper<SysTenant>();
        if(!StringUtils.isEmpty(entity.getDelFlag()))
        {
            q=q.eq("del_flag","0");
        }

        List<SysTenant> list=baseMapper.selectList(q);

        for(SysTenant temp:list)
        {
            checkEffective(temp);
        }
        return list;
    }

    private boolean updateSysAttachmentBySysTenant(SysTenant tenant)
    {
        List<String> list=new ArrayList<>();
        if(tenant.getLicenseImage()!=null)
        {
            list.add(tenant.getLicenseImage());
        }
        if(tenant.getTenantLogo()!=null)
        {
            list.add(tenant.getTenantLogo());
        }
        if(list.size()>0)
        {
            List<AsUserSysAttachment> aList=iAsUserSysAttachmentService.findListForUpdateTenantCode(list);
            for(AsUserSysAttachment a:aList)
            {
                a.setTenantCode(tenant.getTenantCode());
                iAsUserSysAttachmentService.updateEntityById(a);
            }
        }
        return true;
    }

    /**/
    private String getImageName(String s)
    {
        if(StringUtils.isEmpty(s))
        {
            return null;
        }
        int lastIndex=s.lastIndexOf("/");
        if(lastIndex==-1)
        {
            return s;
        }
        return s.substring(lastIndex);
    }

    // 用户绑定角色
    private AsUserSysUserRole saveAsUserSysUserRole(AsUserSysRole role,AsUserSysUser user)
    {
        AsUserSysUserRole r=new AsUserSysUserRole();
        r.setRoleId(role.getId());
        r.setUserId(user.getId());
        iAsUserSysUserRoleService.saveEntity(r);

        return r;
    }

    // 复制菜单
    private List<AsUserSysMenu> saveAsUserSysMenu(SysTenant tenant)
    {
        // 根据租户角色ID查询拥有菜单
        List<AsUserSysMenu> list = iSysTenantRoleService.findMenuByRoleId(tenant.getTenantRoleId());
        LocalDateTime now=LocalDateTime.now();
        Map<String,String> idMap=new HashMap<String,String>();
        for(AsUserSysMenu temp:list)
        {
            String newId=IdGen.snowflakeId();
            idMap.put(temp.getId(),newId);

            temp.setId(newId);
            temp.setCreator(tenant.getCreator());
            temp.setCreateDate(now);
            temp.setTenantCode(tenant.getTenantCode());
            temp.setModifier(null);
            temp.setModifyDate(null);
        }

        for(AsUserSysMenu temp:list)
        {
            //替换父id
            if(idMap.containsKey(temp.getParentId()))
            {
                temp.setParentId(idMap.get(temp.getParentId()));
            }
            iAsUserSysMenuService.saveEntityNotIdGen(temp);
        }

       return list;
    }

    // 复制App菜单 TODO
    private List<SysAppMenu> saveAsUserSysAppMenu(SysTenant tenant)
    {
        //复制菜单，根据 osDemo 这个菜单来复制一份

        SysAppMenu m=new SysAppMenu();
        m.setTenantCode("osDemo");//暂时获取osDemo的菜单
        List<SysAppMenu> list=iSysAppMenuService.findList(m);

        LocalDateTime now=LocalDateTime.now();
        Map<String,String> idMap=new HashMap<String,String>();
        for(SysAppMenu temp:list)
        {
            String newId=IdGen.snowflakeId();
            idMap.put(temp.getId(),newId);

            temp.setId(newId);
            temp.setCreator(tenant.getCreator());
            temp.setCreateDate(now);
            temp.setTenantCode(tenant.getTenantCode());
            temp.setModifier(null);
            temp.setModifyDate(null);
        }

        for(SysAppMenu temp:list)
        {
            //替换父id
            if(idMap.containsKey(temp.getParentId()))
            {
                temp.setParentId(idMap.get(temp.getParentId()));
            }
            iSysAppMenuService.saveEntityNotIdGen(temp);
        }

        return list;
    }

    // 角色拥有的菜单(系统管理员)
    private List<AsUserSysRoleMenu> saveAsUserSysRoleMenu(List<AsUserSysMenu> menuList,AsUserSysRole role)
    {
        // 先模拟些菜单数据，还没决定
//        String s="571367565360762900,624969840960081900,624977223748816900,571368181252362240,571372425787346940,571372559308820500,571372707153842200,633264064931434500,571368627413061600,571373219269972000,571373292582211600,571373363046518800,571373478440210400,571369094226513900,571373881496047600,571373962609692700,571374025859797000,571374113881460740,571374178956087300,628165317507551200,631092091174391800,628169825692291100,628170196460376000,628170504737525800";
//        String[] arr=s.split(",");
        AsUserSysRoleMenu m;
        List<AsUserSysRoleMenu> list=new ArrayList<AsUserSysRoleMenu>();
        for(AsUserSysMenu temp:menuList)
        {
            m=new AsUserSysRoleMenu();
            m.setRoleId(role.getId());
            m.setMenuId(temp.getId());

            iAsUserSysRoleMenuService.saveEntity(m);
            list.add(m);
        }

        return list;
    }


    // 角色拥有的菜单(项目管理员)
    private List<AsUserSysRoleMenu> saveAsUserSysProjectRoleMenu(List<AsUserSysMenu> menuList,AsUserSysRole role)
    {
        AsUserSysRoleMenu m;
        List<AsUserSysRoleMenu> list=new ArrayList<AsUserSysRoleMenu>();
        List<AsUserSysMenu> filterList = menuList.stream()
                .filter(s->s.getPermission().equalsIgnoreCase("device") ||s.getPermission().equals("device:list") 
                        ||s.getPermission().equals("personal")||s.getPermission().equals("personal") || s.getPermission().equals("personal:message")||s.getPermission().equals("personal:password")).collect(Collectors.toList());
        for(AsUserSysMenu temp:filterList)
        {
            m=new AsUserSysRoleMenu();
            m.setRoleId(role.getId());
            m.setMenuId(temp.getId());

            iAsUserSysRoleMenuService.saveEntity(m);
            list.add(m);
        }

        return list;
    }

    // 角色拥有的App菜单,
    private List<SysAppRoleMenu> saveAsUserSysAppRoleMenu(List<SysAppMenu> menuList,AsUserSysRole role)
    {
        // 先模拟些菜单数据，还没决定
        SysAppRoleMenu m;
        List<SysAppRoleMenu> list=new ArrayList<SysAppRoleMenu>();
        for(SysAppMenu temp:menuList)
        {
            m=new SysAppRoleMenu();
            m.setRoleId(role.getId());
            m.setAppMenuId(temp.getId());

            iSysAppRoleMenuService.saveEntity(m);
            list.add(m);
        }

        return list;
    }

    private AsUserSysUserAuths saveAsUserSysUserAuths(SysTenant tenant,AsUserSysUser user)
    {
        AsUserSysUserAuths a=new AsUserSysUserAuths();
        a.setUserId(user.getId());
        a.setIdentityType(IdentityType.PASSWORD.getValue());
        a.setIdentifier(tenant.getOwnerMobile()); // 这里改成需要用用户名 tenant.getOwnerMobile()
        a.setCredential(encoder.encode(tenant.getOwnerMobile())); //现在暂时直接设置为123456 后面默认密码手机号码

        a.setCreator(tenant.getCreator());
        a.setCreateDate(new Date());
        a.setDelFlag(CommonConstant.DEL_FLAG_NORMAL);
        //application_code 直接设置成 AS 暂时只有一个系统
        a.setApplicationCode(SysUtil.getSysCode());

        a.setTenantCode(tenant.getTenantCode());

        iAsUserSysUserAuthsService.saveEntity(a);

        return a;
    }

    //创建用户
    private AsUserSysUser saveAsUserSysUser(SysTenant tenant,AsUserSysDept dept,AsUserSysPosition position)
    {
        //  用租户负责人的 姓名、电话来创建用户，用手机号码登录，默认密码手机号码用租户负责人的 姓名、电话来创建用户，用手机号码登录，默认密码手机号码
        // 判断是否存在此用户

        //判断是否存在该用户，如果存在，应该怎样弄

        AsUserSysUser u=new AsUserSysUser();
        //u.setName(tenant.getTenantName());//这里可能取其它值
        u.setName(tenant.getOwnerName());//现在取的是负责人名字
        u.setPhone(tenant.getOwnerMobile());
        //以下的字段设置为空 avatar_id，born,sex, parent_uid，street_id，county_id，
        // city_id，province_id，login_time，lock_time，wechat,family_role
        // 以下的字段可能能拿到其它数据 region_id,region_code
        u.setEmail(tenant.getNotifyEmail());
        u.setStatus(CommonConstant.DEL_FLAG_NORMAL);
        u.setDeptId(dept.getId());
        u.setDeptCode(dept.getDeptCode());
        u.setPositionId(position.getPositionId());
        u.setPositionName(position.getPositionName());
        u.setSysRole(RoleEnum.ROLE_SYS_ADMIN.getType());
        u.setUserDesc("在"+ DateUtils.format(new Date())+"创建，租户名称为"+tenant.getTenantName()+"的职位,手机号码为"+tenant.getOwnerMobile());
        u.setCreator(tenant.getCreator());
        u.setCreateDate(new Date());
        u.setDelFlag(CommonConstant.DEL_FLAG_NORMAL);
        //application_code 直接设置成 AS 暂时只有一个系统
        u.setApplicationCode(SysUtil.getSysCode());
        u.setTenantCode(tenant.getTenantCode());

        iAsUserSysUserService.saveEntity(u);

        return u;
    }

    //创建职位
    private AsUserSysPosition saveAsUserSysPosition(SysTenant tenant,AsUserSysPost post)
    {
        AsUserSysPosition p=new AsUserSysPosition();
        p.setPositionParentId("-1");
        p.setPositionName("管理员");
        p.setPositionDes("在"+DateUtils.format(new Date())+"创建，租户名称为"+tenant.getTenantName()+"的职位");
        p.setPostId(post.getPostId());
        p.setSort(1);
        p.setCreator(tenant.getCreator());
        p.setCreateDate(new Date());
        p.setDelFlag(CommonConstant.DEL_FLAG_NORMAL);
        //application_code 直接设置成 AS 暂时只有一个系统
        p.setApplicationCode(SysUtil.getSysCode());

        p.setTenantCode(tenant.getTenantCode());

        iAsUserSysPositionService.saveEntity(p);

        return p;
    }

    //创建岗位
    private AsUserSysPost saveAsUserSysPost(SysTenant tenant)
    {
        AsUserSysPost p=new AsUserSysPost();
        p.setPostName("管理员");
        // 以下字段设置为空 working_days,standard_down,standard_up,commission
        p.setCreator(tenant.getCreator());
        p.setCreateDate(new Date());
        p.setDelFlag(CommonConstant.DEL_FLAG_NORMAL);
        p.setTenantCode(tenant.getTenantCode());

        //application_code 直接设置成 AS 暂时只有一个系统
        p.setApplicationCode(SysUtil.getSysCode());
        iAsUserSysPostService.saveEntity(p);

        return p;
    }

    //创建职位


    //创建部门
    private AsUserSysDept saveAsUserSysDept(SysTenant tenant)
    {
        //TODO 可能需要校验该部门原来存在不

        AsUserSysDept d=new AsUserSysDept();
        String deptName= StringUtils.isEmpty(tenant.getFullName())?tenant.getTenantName():tenant.getFullName();
        d.setDeptName(deptName);
        d.setDeptDesc("在"+DateUtils.format(new Date())+"创建，租户名称为"+deptName+"的部门");
        d.setParentId("-1");
        d.setSort(0);
        d.setCreator(tenant.getCreator());
        d.setCreateDate(new Date());
        d.setDelFlag(CommonConstant.DEL_FLAG_NORMAL);
        //application_code 直接设置成 AS 暂时只有一个系统
        d.setApplicationCode(SysUtil.getSysCode());
        d.setTenantCode(tenant.getTenantCode());

        iAsUserSysDeptService.generateDeptCode(d);
        iAsUserSysDeptService.saveEntity(d);

        return d;
    }

    //TDOO 到时候如果创建失败，需要回滚，就是需要删除已创建的数据 创建角色
   
    //创建角色
   /* private AsUserSysRole saveAsUserSysRole(SysTenant tenant)
    {

        //TODO 可能还要去重
        AsUserSysRole role=new AsUserSysRole();
        role.setId(IdGen.snowflakeId());
        role.setRoleName("管理员");
        role.setRoleCode("role_admin_"+ tenant.getTenantCode());
        role.setRoleDesc("在"+DateUtils.format(new Date())+"创建，租户名称为"+tenant.getTenantName()+"的管理员账号"+"手机号为"+tenant.getOwnerMobile());
        role.setIsDefault(RoleConstant.NORMAL_ROLE); //普通的权限
        role.setStatus(0);//正常状态

        //设置创建人
        role.setCreator(tenant.getCreator());
        role.setCreateDate(new Date());
        role.setDelFlag(CommonConstant.DEL_FLAG_NORMAL);
        role.setTenantCode(tenant.getTenantCode()); //tenantCode

        iAsUserSysRoleService.saveEntity(role);
        return role;
    }*/

    /**
     *  创建系统管理员角色
     * @param tenant
     * @return
     */
    private AsUserSysRole saveRoleSysAdmin(SysTenant tenant)
    {
        AsUserSysRole role=new AsUserSysRole();
        role.setId(IdGen.snowflakeId());
        role.setRoleName(RoleEnum.ROLE_SYS_ADMIN.getDescription());
        role.setRoleCode(RoleEnum.ROLE_SYS_ADMIN.getCode() + "_" + tenant.getTenantCode());
        role.setRoleDesc("在"+DateUtils.format(new Date())+"创建，租户名称为"+tenant.getTenantName()+"的系统管理员账号"+"手机号为"+tenant.getOwnerMobile());
        role.setIsDefault(RoleConstant.NORMAL_ROLE); //普通的权限
        role.setStatus(0);//正常状态
        role.setIsEdit(0); //不可编辑
        //设置创建人
        role.setCreator(tenant.getCreator());
        role.setCreateDate(new Date());
        role.setDelFlag(CommonConstant.DEL_FLAG_NORMAL);
        role.setApplicationCode(SysUtil.getSysCode());
        role.setTenantCode(tenant.getTenantCode()); //tenantCode
        iAsUserSysRoleService.saveEntity(role);
        return role;
    }

    /**
     *  创建系统值班员角色
     * @param tenant
     * @return
     */
    private AsUserSysRole saveRoleSysWatcher(SysTenant tenant)
    {
        
        AsUserSysRole role=new AsUserSysRole();
        role.setId(IdGen.snowflakeId());
        role.setRoleName(RoleEnum.ROLE_SYS_WATCHER.getDescription());
        role.setRoleCode(RoleEnum.ROLE_SYS_WATCHER.getCode() +"_"+ tenant.getTenantCode());
        role.setRoleDesc("在"+DateUtils.format(new Date())+"创建，租户名称为"+tenant.getTenantName()+"的系统值班员账号"+"手机号为"+tenant.getOwnerMobile());
        role.setIsDefault(RoleConstant.NORMAL_ROLE); //普通的权限
        role.setStatus(0);//正常状态
        role.setIsEdit(0); //不可编辑
        //设置创建人
        role.setCreator(tenant.getCreator());
        role.setCreateDate(new Date());
        role.setDelFlag(CommonConstant.DEL_FLAG_NORMAL);
        role.setApplicationCode(SysUtil.getSysCode());
        role.setTenantCode(tenant.getTenantCode()); //tenantCode
        iAsUserSysRoleService.saveEntity(role);
        return role;
    }


    /**
     *  创建项目管理员角色
     * @param tenant
     * @return
     */
    private AsUserSysRole saveRoleProjectAdmin(SysTenant tenant)
    {
        AsUserSysRole role=new AsUserSysRole();
        role.setId(IdGen.snowflakeId());
        role.setRoleName(RoleEnum.ROLE_PROJECT_ADMIN.getDescription());
        role.setRoleCode(RoleEnum.ROLE_PROJECT_ADMIN.getCode()+ "_" + tenant.getTenantCode());
        role.setRoleDesc("在"+DateUtils.format(new Date())+"创建，租户名称为"+tenant.getTenantName()+"的项目管理员账号"+"手机号为"+tenant.getOwnerMobile());
        role.setIsDefault(RoleConstant.NORMAL_ROLE); //普通的权限
        role.setStatus(0);//正常状态
        role.setIsEdit(0); //不可编辑
        //设置创建人
        role.setCreator(tenant.getCreator());
        role.setCreateDate(new Date());
        role.setDelFlag(CommonConstant.DEL_FLAG_NORMAL);
        role.setApplicationCode(SysUtil.getSysCode());
        role.setTenantCode(tenant.getTenantCode()); //tenantCode
        iAsUserSysRoleService.saveEntity(role);
        return role;
    }

    /**
     *  创建项目值班员角色
     * @param tenant
     * @return
     */
    private AsUserSysRole saveRoleProjectWatcher(SysTenant tenant)
    {
        AsUserSysRole role=new AsUserSysRole();
        role.setId(IdGen.snowflakeId());
        role.setRoleName(RoleEnum.ROLE_PROJECT_WATCHER.getDescription());
        role.setRoleCode(RoleEnum.ROLE_PROJECT_WATCHER.getCode() +"_"+ tenant.getTenantCode());
        role.setRoleDesc("在"+DateUtils.format(new Date())+"创建，租户名称为"+tenant.getTenantName()+"的项目值班员账号"+"手机号为"+tenant.getOwnerMobile());
        role.setIsDefault(RoleConstant.NORMAL_ROLE); //普通的权限
        role.setStatus(0);//正常状态
        role.setIsEdit(0); //不可编辑
        //设置创建人
        role.setCreator(tenant.getCreator());
        role.setCreateDate(new Date());
        role.setDelFlag(CommonConstant.DEL_FLAG_NORMAL);
        role.setApplicationCode(SysUtil.getSysCode());
        role.setTenantCode(tenant.getTenantCode()); //tenantCode
        iAsUserSysRoleService.saveEntity(role);
        return role;
    }


    /**
     *  创建区域负责人角色
     * @param tenant
     * @return
     */
    private AsUserSysRole saveRoleProjectRegion(SysTenant tenant)
    {
        AsUserSysRole role=new AsUserSysRole();
        role.setId(IdGen.snowflakeId());
        role.setRoleName(RoleEnum.ROLE_PROJECT_REGION.getDescription());
        role.setRoleCode(RoleEnum.ROLE_PROJECT_REGION.getCode() +"_"+ tenant.getTenantCode());
        role.setRoleDesc("在"+DateUtils.format(new Date())+"创建，租户名称为"+tenant.getTenantName()+"的区域负责人账号"+"手机号为"+tenant.getOwnerMobile());
        role.setIsDefault(RoleConstant.NORMAL_ROLE); //普通的权限
        role.setStatus(0);//正常状态
        role.setIsEdit(0); //不可编辑
        //设置创建人
        role.setCreator(tenant.getCreator());
        role.setCreateDate(new Date());
        role.setDelFlag(CommonConstant.DEL_FLAG_NORMAL);
        role.setApplicationCode(SysUtil.getSysCode());
        role.setTenantCode(tenant.getTenantCode()); //tenantCode
        iAsUserSysRoleService.saveEntity(role);
        return role;
    }


    /**
     *  创建默认系统配置信息
     * @param tenant
     * @return
     */
    private void  saveSysConfig(SysTenant tenant)
    {
        String year =new SimpleDateFormat("yyyy").format(new Date());
        SysConfig sysConfig = new SysConfig();
        sysConfig.setId(IdGen.snowflakeId());
        sysConfig.setParamKey("SYS_NAME");
        sysConfig.setParamValue("AS视频监控故障诊断运维管理系统");
        sysConfig.setStatus(1);
        sysConfig.setRemark("系统名称");
        sysConfig.setApplicationCode(SysUtil.getSysCode());
        sysConfig.setTenantCode(tenant.getTenantCode());
        iSysConfigService.saveEntity(sysConfig);
        
        sysConfig.setId(IdGen.snowflakeId());
        sysConfig.setParamKey("SYS_COPYRIGHT");
        sysConfig.setParamValue(year + " " + (org.apache.commons.lang3.StringUtils.isNotEmpty(tenant.getFullName()) ? tenant.getFullName() : "广州傲视物联网信息技术有限公司"));
        sysConfig.setStatus(1);
        sysConfig.setRemark("系统版权");
        sysConfig.setApplicationCode(SysUtil.getSysCode());
        sysConfig.setTenantCode(tenant.getTenantCode());
        iSysConfigService.saveEntity(sysConfig);
       
        sysConfig.setId(IdGen.snowflakeId());
        sysConfig.setParamKey("SYS_LOGO");
        sysConfig.setParamValue(null);
        sysConfig.setStatus(1);
        sysConfig.setRemark("系统LOGO");
        sysConfig.setApplicationCode(SysUtil.getSysCode());
        sysConfig.setTenantCode(tenant.getTenantCode());
        iSysConfigService.saveEntity(sysConfig);

        sysConfig.setId(IdGen.snowflakeId());
        sysConfig.setParamKey("IS_CLOUD");
        sysConfig.setParamValue("1");
        sysConfig.setStatus(1);
        sysConfig.setRemark("是否云平台");
        sysConfig.setApplicationCode(SysUtil.getSysCode());
        sysConfig.setTenantCode(tenant.getTenantCode());
        iSysConfigService.saveEntity(sysConfig);
        
    }
   
   


    private int setDelFlag(String id, String delFlag)
    {

        // 请求远程删除
//        SysTenant temp=new SysTenant();
//        temp.setId(id);
//        temp.setDelFlag(delFlag);
//        return baseMapper.updateDelFlag(temp);

        Tenant tenant=new Tenant();
        tenant.setIdString(id);
        ResponseBean<Boolean> r=userServiceClient.osTenant2(OsVo.getRandomStr(),tenant);

        if(r!=null )
        {
            Boolean b=r.getData();
            if( ResponseBean.SUCCESS == r.getCode() && b )
            {
                return 1;
            }
            else
            {
                throw new RRException(r.getMsg());
            }
        }
        else
        {
            throw new RRException("更新租户失败");
        }

    }

}
