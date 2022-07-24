package com.aswl.as.asos.modules.ibrs.service.impl;

import com.aswl.as.asos.common.utils.Query;
import com.aswl.as.asos.modules.ibrs.entity.AsProject;
import com.aswl.as.asos.modules.ibrs.mapper.AsProjectMapper;
import com.aswl.as.common.core.constant.CommonConstant;
import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.utils.IdGen;
import com.aswl.as.asos.common.utils.PageUtils;
import com.aswl.as.asos.datasource.annotation.DataSource;
import com.aswl.as.asos.modules.ibrs.service.IAsProjectService;
import com.aswl.as.common.core.utils.NumberUtil;
import com.aswl.as.common.core.utils.Pinyin4jUtil;
import com.aswl.as.common.core.utils.SysUtil;
import com.aswl.as.ibrs.api.dto.DeviceAlarmDto;
import com.aswl.as.ibrs.api.dto.ProjectDto;
import com.aswl.as.ibrs.api.feign.RegionServiceClient;
import com.aswl.as.ibrs.api.module.Attachment;
import com.aswl.as.ibrs.api.module.Device;
import com.aswl.as.common.core.vo.OsVo;
import com.aswl.as.ibrs.api.module.Project;
import com.aswl.as.ibrs.api.vo.KindVo;
import com.aswl.as.ibrs.api.vo.ModelStatisticsVo;
import com.aswl.as.ibrs.api.vo.ProjectDeviceVo;
import com.aswl.as.ibrs.api.vo.StatisticsVo;
import com.aswl.as.user.api.feign.UserServiceClient;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageInfo;
import feign.Response;
import lombok.AllArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 项目表 服务实现类
 * </p>
 *
 * @author hfx
 * @since 2019-11-15
 */
@Service
@AllArgsConstructor
public class AsProjectServiceImpl extends ServiceImpl<AsProjectMapper, AsProject> implements IAsProjectService {

    private RegionServiceClient regionServiceClient;

    private UserServiceClient userServiceClient;

    public String checkProjectDevice(String projectId)
    {

        if(StringUtils.isEmpty(projectId))
        {
            return "传入的项目id不能为空";
        }
        Device d=new Device();
        // 暂时用租户来查，后面要换成项目，因为其他模块要上线，暂时不上传代码，所以不直接修改代码
        //d.setTenantCode("aswl");
        d.setProjectId(projectId);
        d.setRandomStr(OsVo.getRandomStr());
        //如果太久或数据太多，就直接连数据库查
        ResponseBean<List<Device>>  r=regionServiceClient.osDevice1(d);
//        regionServiceClient.getRegionById("6");
        if(r==null || r.getCode()!=ResponseBean.SUCCESS)
        {
            return "删除时校验项目设备时，获取设备列表失败，请重试";
        }

        List<Device> list=r.getData();
        if(list.size()>0)
        {
            return "该项目下还有设备，请删除或转移设备到其他项目后再重试";
        }

        return null;
    }

    @DataSource("slave1")
    public PageUtils queryPage(Map<String, Object> params)
    {
        String projectName = (String)params.get("projectName");
        String projectDes = (String)params.get("projectDes");
        String projectOwner = (String)params.get("projectOwner");
        String tenantCode = (String)params.get("tenantCode");

        IPage<AsProject> page = this.page(
                new Query<AsProject>().getPage(params),
                new QueryWrapper<AsProject>()
                        .like(StringUtils.isNotBlank(projectName),"project_name",projectName)
                        .like(StringUtils.isNotBlank(projectDes),"project_des",projectDes)
                        .like(StringUtils.isNotBlank(projectOwner),"project_owner",projectOwner)
                        .eq(StringUtils.isNotBlank(tenantCode),"tenant_code",tenantCode)
        );

        return new PageUtils(page);
    }

    //根据id返回对应信息
    @DataSource("slave1")
    public AsProject getEntityById(String id)
    {
        return this.getById(id);
    }

    @DataSource("slave1")
    public boolean saveEntity(AsProject entity)
    {
        //到时候测试这里能不能用maven引用 使用项目id
        entity.setProjectId(IdGen.snowflakeId());
        entity.setApplicationCode(SysUtil.getSysCode());

        //设置项目编码
        String tempCode= Pinyin4jUtil.getFirstSpell(entity.getProjectName());
        generateProjectCodeCode(entity,tempCode);

        return this.save(entity);
    }

    @DataSource("slave1")
    public boolean updateEntityById(AsProject entity)
    {
        return this.updateById(entity);
    }

    @DataSource("slave1")
    public boolean deleteEntityById(String id)
    {
        return this.removeById(id);
    }

    @DataSource("slave1")
    public String deleteEntityByIds(String[] ids) {
       /* for (String id:ids)
        {
//            System.out.println(JSON.toJSONString(userServiceClient.findAllMenu("aswl")));
            String result=checkProjectDevice(id);
            if(result!=null)
            {
                return result;
            }
        }

        for(String id:ids)
        {
            this.removeById(id);
        }

        return null;*/

        // 远程调用删除项目信息
        for (String id : ids) {
            ResponseBean<Boolean> responseBean = regionServiceClient.deleteProjectById(id);
            if (responseBean.getCode() == 200) {
                return responseBean.getMsg();
            } else {
                return null;
            }
        }
        return null;
    }
        

    /**
     * 自动生成项目编码
     * @param asProject 项目
     */
    @DataSource("slave1")
    public void generateProjectCodeCode(AsProject asProject,String tempCode) {

        AsProject temp=new AsProject();
        temp.setProjectCode(tempCode);
        temp=baseMapper.selectProjectForProjectCode(temp);
        if(temp==null||StringUtils.isEmpty(temp.getProjectCode()))
        {
            asProject.setProjectCode(tempCode+"001");
        }
        else
        {
            asProject.setProjectCode(NumberUtil.addOne(temp.getProjectCode()));
        }
    }

    @DataSource("slave1")
    public List<AsProject> getProjectListByTenantCode(String tenantCode)
    {
        return baseMapper.selectList(new QueryWrapper<AsProject>().eq("tenant_code",tenantCode));
    }

    @DataSource("slave1")
    public List<AsProject> getProjectListByProjectCode(String projectCode)
    {
        return baseMapper.selectList(new QueryWrapper<AsProject>().eq("project_code",projectCode));
    }

    @DataSource("slave1")
    public List<AsProject> findList(AsProject asProject)
    {
        QueryWrapper<AsProject> q = new QueryWrapper<AsProject>();
        return baseMapper.selectList(q);
    }

    @Override
    public PageInfo<Project> projectList(String pageNum, String pageSize, Project project) {
        ResponseBean<PageInfo<Project>> response = regionServiceClient.projectList(pageNum, pageSize, "", "", project);
        if (response.getCode() == 200) {
            return response.getData();
        }
        return null;
    }

    @Override
    public Boolean insertProject(ProjectDto projectDto) {
        ResponseBean<Boolean> r = regionServiceClient.insertProject(projectDto);
        if (r.getCode() == 200) {
            return true;
        }
        return false;
    }

    @Override
    public Boolean updateProject(ProjectDto projectDto) {
        ResponseBean<Boolean> r = regionServiceClient.updateProject(projectDto);
        if (r.getCode() == 200) {
            return true;
        }
        return false;
    }

    @Override
    public List<StatisticsVo> getProjectMaintainStatistics(DeviceAlarmDto deviceAlarmDto) {
        ResponseBean<List<StatisticsVo>> r = regionServiceClient.getProjectMaintainStatistics(deviceAlarmDto);
        if (r.getCode() == 200) {
            return r.getData();
        }
        return null;
    }

    @Override
    public List<KindVo> findDeviceModelKind() {
        ResponseBean<List<KindVo>> r = regionServiceClient.findDeviceModelKind();
        if (r.getCode() == 200) {
            return r.getData();
        }
        return null;
    }

    @Override
    public List<ProjectDeviceVo> getProjectDeviceList(DeviceAlarmDto deviceAlarmDto) {
        ResponseBean<List<ProjectDeviceVo>> r = regionServiceClient.getProjectDeviceList(deviceAlarmDto);
        if (r.getCode() == 200) {
            return r.getData();
        }
        return null;
    }

    @Override
    public List<Attachment> findAttachments(String projectId) {
        ResponseBean<List<Attachment>> r = regionServiceClient.findAttachments(projectId);
        if (r.getCode() == 200) {
            return r.getData();
        }
        return null;
    }

    @Override
    public ResponseEntity<byte[]> download(String filePath) {
        return regionServiceClient.download(filePath);
    }

    @Override
   public  List<ModelStatisticsVo> findModelStatistics(String projectId){
        ResponseBean<List<ModelStatisticsVo>> r = regionServiceClient.findModelStatistics(projectId);
        if (r.getCode() == 200){
            return r.getData();
        }
        return null;

    }
}
