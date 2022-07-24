package com.aswl.as.asos.modules.ibrs.service.impl;

import cn.hutool.json.JSONObject;
import com.aswl.as.asos.common.utils.*;
import com.aswl.as.common.core.constant.CommonConstant;
import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.asos.datasource.annotation.DataSource;
import com.aswl.as.asos.modules.ibrs.entity.AsIbrsAsDevice;
import com.aswl.as.asos.modules.ibrs.mapper.AsIbrsAsDeviceMapper;
import com.aswl.as.asos.modules.ibrs.service.IAsIbrsAsDeviceService;
import com.aswl.as.common.core.utils.OsinfoUtil;
import com.aswl.as.common.core.utils.SysUtil;
import com.aswl.as.ibrs.api.dto.DeviceDto;
import com.aswl.as.ibrs.api.feign.RegionServiceClient;
import com.aswl.as.ibrs.api.module.Device;
import com.aswl.as.ibrs.api.vo.DeviceVo;
import com.aswl.as.common.core.vo.OsVo;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageInfo;
import lombok.AllArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 设备表 服务实现类
 * </p>
 *
 * @author hfx
 * @since 2019-11-25
 */
@Service
@AllArgsConstructor
public class AsIbrsAsDeviceServiceImpl extends ServiceImpl<AsIbrsAsDeviceMapper, AsIbrsAsDevice> implements IAsIbrsAsDeviceService {

    private RegionServiceClient regionServiceClient;


    @DataSource("slave1")
    public PageUtils queryPage(Map<String, Object> params)
    {
        /*
        String projectName = (String)params.get("projectName");
        String projectDes = (String)params.get("projectDes");
        String projectOwner = (String)params.get("projectOwner");
        */

        IPage<AsIbrsAsDevice> page = this.page(
            new Query<AsIbrsAsDevice>().getPage(params),
                new QueryWrapper<AsIbrsAsDevice>()
                /*
                .like(StringUtils.isNotBlank(projectName),"project_name",projectName)
                .like(StringUtils.isNotBlank(projectDes),"project_des",projectDes)
                .like(StringUtils.isNotBlank(projectOwner),"project_owner",projectOwner)
                */
            );

        return new PageUtils(page);
    }

    //根据id返回对应信息
    @DataSource("slave1")
    public AsIbrsAsDevice getEntityById(String id)
    {
        return this.getById(id);
    }

    @DataSource("slave1")
    public boolean saveEntity(AsIbrsAsDevice entity)
    {
//        entity.setProjectId(IdGen.snowflakeId());
        entity.setDelFlag(CommonConstant.DEL_FLAG_NORMAL);
        return this.save(entity);
    }

    @DataSource("slave1")
    public boolean updateEntityById(AsIbrsAsDevice entity)
    {
        return this.updateById(entity);
    }

    @DataSource("slave1")
    public boolean deleteEntityById(String id)
    {
        return this.removeById(id);
    }

    @DataSource("slave1")
    public boolean deleteEntityByIds(String[] ids)
    {
        for(String id:ids)
        {
            this.removeById(id);
        }
        return true;
    }

    //新增
    // 直接跳转到请求IBRS项目的serviceClient
    public ResponseBean<Boolean> osDevice2(DeviceDto deviceDto)
    {
        deviceDto.setIsAsOs(CommonConstant.IS_ASOS_TRUE);

        // 测试，暂时添加区域，到时候需要去掉
//        deviceDto.setRegionCode("A03");
//        deviceDto.setRegionId("6");
        deviceDto.setRandomStr(OsVo.getRandomStr());
        deviceDto.setApplicationCode(SysUtil.getSysCode());

        ResponseBean<Boolean> r=regionServiceClient.osDevice2(deviceDto);
//        System.out.println(JSON.toJSONString(r));
        return r;
    }

    /**
     * 根据ID获取设备
     *
     * @param id
     * @return ResponseBean
     */
    public ResponseBean<DeviceVo> osDevice3(String id)
    {
        return regionServiceClient.osDevice3(id, OsVo.getRandomStr());
    }

    /**
     * 修改设备
     *
     * @param deviceDto
     * @return ResponseBean
     */
    public ResponseBean<Boolean> osDevice4(DeviceDto deviceDto)
    {
        // 修改数据
        deviceDto.setIsAsOs(CommonConstant.IS_ASOS_TRUE);
        deviceDto.setRandomStr(OsVo.getRandomStr());

        return regionServiceClient.osDevice4(deviceDto);
    }

    /**
     * 批量删除设备信息
     *
     * @param device
     * @return ResponseBean
     */
    public ResponseBean<Boolean> osDevice5(Device device)
    {
        device.setRandomStr(OsVo.getRandomStr());
        return regionServiceClient.osDevice5(device);
    }

    public ResponseBean<Boolean> test(DeviceDto deviceDto)
    {
        deviceDto.setRandomStr(OsVo.getRandomStr());
        return regionServiceClient.test(deviceDto);
    }

    //用来提交post的数据（get方法参考OsIndex的osGetOnLineDeviceDataByHttp）
    public IbrsResponseBean testByHttp(DeviceDto deviceDto)
    {
        deviceDto.setRandomStr(OsVo.getRandomStr());
        try
        {
            return IbrsData.getBean(OsHttpUtil.getInstance().postJson(IbrsData.buildPostUrl("/v1/device/os/test"), IbrsData.getHeaderMap(),deviceDto));
        }
        catch (Exception e)
        {
            return new IbrsResponseBean(e);
        }
    }

    /**
     * 分页查询设备列表
     *
     * @param deviceDto
     * @return ResponseBean
     */
    public ResponseBean<PageInfo<DeviceVo>> osDevice6(String pageNum,String pageSize,String sort,String order, String regionId, DeviceDto deviceDto)
    {
        if(deviceDto==null)
        {
            deviceDto=new DeviceDto();
        }
        deviceDto.setRandomStr(OsVo.getRandomStr());
        return regionServiceClient.osDevice6(pageNum,pageSize,sort,order,regionId,deviceDto);
    }

    /**
     * 导入设备数据(专门提供给运营端使用，不ignore原来的接口)
     * @param deviceDtos
     * @return
     */
    public ResponseBean osDevice7(List<DeviceDto> deviceDtos)
    {
        if(deviceDtos!=null&&deviceDtos.size()>0)
        {
            deviceDtos.get(0).setRandomStr(OsVo.getRandomStr());
        }

        ResponseBean r=regionServiceClient.osDevice7(deviceDtos);

        // 需要判断和提供下载
        if(r!=null && ResponseBean.SUCCESS==r.getCode() && r.getData()!=null )
        {
            JSONObject result=(JSONObject) r.getData();

            if(result!=null)
            {
                result=(JSONObject)result.get("data");

                //如果没有错误，就不用这个了
                if(Boolean.FALSE.equals(result.getBool("isAllTrue")))
                {
                    List<String>errorStrs=(List<String>)result.getObj("errorStrs");
                    String originalFileUrl=result.getStr("originalFileUrl");

                    String uploadPath;
                    String winUploadPath=result.getStr("winUploadPath");
                    String linuxUploadPath=result.getStr("linuxUploadPath");

                    //上传路径（实际上都是相同的）
                    if(StringUtils.isNotBlank(winUploadPath))
                    {
                        OsGlobalData.IBRS_WIN_UPLOAD_PATH=winUploadPath;
                    }
                    if(StringUtils.isNotBlank(linuxUploadPath))
                    {
                        OsGlobalData.IBRS_LINUX_UPLOAD_PATH=linuxUploadPath;
                    }

                    if (OsinfoUtil.isWindows()) {
                        uploadPath=winUploadPath;
                    }else{
                        uploadPath=linuxUploadPath;
                    }

                    //判断是否有该文件
                    File f=new File(uploadPath+File.separator+originalFileUrl);
                    if(f.exists())
                    {
                        //如果存在，就是跟ibrs部署在同一台机子上，就什么都不用改
                    }
                    else
                    {
                        //如果不存在，就要生成该文件，然后给下载
                        OsFileUtil.saveErrorTxtByList(errorStrs,winUploadPath,linuxUploadPath,originalFileUrl.substring(0,originalFileUrl.lastIndexOf(File.separator)),result.getStr("fileName"));
                    }
                }

                // 删除这些传递消息的key
                result.remove("errorStrs");
                result.remove("originalFileUrl");
                result.remove("winUploadPath");
                result.remove("linuxUploadPath");
            }
        }

        return r;
    }

    /**
     * 导出设备数据(专门提供给运营端使用，不ignore原来的接口)
     *
     * @param deviceDto deviceDto
     */
    public ResponseBean<List<Device>> osDevice8(DeviceDto deviceDto)
    {
        deviceDto.setRandomStr(OsVo.getRandomStr());
        return regionServiceClient.osDevice8(deviceDto);
    }


    /**
     * 获取上级设备信息(专门提供给运营端使用，不ignore原来的接口)
     *
     * @param parentId
     * @return ResponseBean
     */
    public ResponseBean<DeviceVo> osDevice9(String parentId) {
        return regionServiceClient.osDevice9(parentId, OsVo.getRandomStr());
    }

    /**
     * 获取下级设备信息(专门提供给运营端使用，不ignore原来的接口)
     *
     * @param id
     * @return ResponseBean
     */
    public ResponseBean<List<DeviceVo>> osDevice10(String id) {
        return regionServiceClient.osDevice10(id, OsVo.getRandomStr());
    }

    /**
     * 根据设备id更新上级设备ID为空
     *
     * @param id
     * @return ResponseBean
     */
    public ResponseBean<Boolean> osDevice11(String id)
    {
        return regionServiceClient.osDevice11(id, OsVo.getRandomStr());
    }


}
