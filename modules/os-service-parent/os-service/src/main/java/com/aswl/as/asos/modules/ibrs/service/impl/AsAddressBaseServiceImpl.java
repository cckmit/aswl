package com.aswl.as.asos.modules.ibrs.service.impl;

import com.aswl.as.asos.common.utils.OsFileUtil;
import com.aswl.as.asos.common.utils.OsValueComponent;
import com.aswl.as.asos.modules.ibrs.entity.AsProject;
import com.aswl.as.asos.modules.ibrs.service.IAsProjectService;
import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.utils.IdGen;
import com.aswl.as.asos.common.utils.Query;
import com.aswl.as.asos.common.utils.PageUtils;
import com.aswl.as.asos.common.annotation.SysLog;
import com.aswl.as.asos.datasource.annotation.DataSource;
import com.aswl.as.asos.modules.ibrs.entity.AsAddressBase;
import com.aswl.as.asos.modules.ibrs.mapper.AsAddressBaseMapper;
import com.aswl.as.asos.modules.ibrs.service.IAsAddressBaseService;
import com.aswl.as.common.core.utils.SysUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.micrometer.core.instrument.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 * 点位地址库 服务实现类
 * </p>
 *
 * @author hfx
 * @since 2020-03-02
 */
@Service
public class AsAddressBaseServiceImpl extends ServiceImpl<AsAddressBaseMapper, AsAddressBase> implements IAsAddressBaseService {

    @Autowired
    IAsProjectService iAsProjectService;

    @Autowired
    private OsValueComponent osValueComponent;

    @DataSource("slave1")
    public PageUtils queryPage(Map<String, Object> params)
    {
        // 添加查询条件
        String tenantCode=(String)params.get("tenantCode");
        String projectId=(String)params.get("projectId");
        String queryStr=(String)params.get("queryStr");




        IPage<AsAddressBase> page = this.page(
            new Query<AsAddressBase>().getPage(params),
                new QueryWrapper<AsAddressBase>()
                .eq(StringUtils.isNotBlank(tenantCode),"tenant_code",tenantCode)
                .eq(StringUtils.isNotBlank(projectId),"project_id",projectId)
                .and(StringUtils.isNotBlank(queryStr),wrapper -> wrapper.like(StringUtils.isNotBlank(queryStr),"name",queryStr).or().like(StringUtils.isNotBlank(queryStr),"ip",queryStr))
            );

        return new PageUtils(page);
    }

    @DataSource("slave1")
    public List<AsAddressBase> findList(AsAddressBase entity)
    {
        return list(
                new QueryWrapper<AsAddressBase>()
                        .eq(StringUtils.isNotBlank(entity.getTenantCode()),"tenant_code",entity.getTenantCode())
                        .eq(StringUtils.isNotBlank(entity.getProjectId()),"project_id",entity.getProjectId())
        );
    }

    @DataSource("slave1")
    public List<AsAddressBase> findListForMap(Map<String, Object> params)
    {
        // 添加查询条件
        String tenantCode=(String)params.get("tenantCode");
        String projectId=(String)params.get("projectId");

        String longMin=getNotNullString(params.get("longMin"));
        String longMax=getNotNullString(params.get("longMax"));
        String latMin=getNotNullString(params.get("latMin"));
        String latMax=getNotNullString(params.get("latMax"));

        String queryStr=(String)params.get("queryStr");

        return list(
                new QueryWrapper<AsAddressBase>()
                        .eq(StringUtils.isNotBlank(tenantCode),"tenant_code",tenantCode)
                        .eq(StringUtils.isNotBlank(projectId),"project_id",projectId)
                        .ge(StringUtils.isNotBlank(longMin),"longitude",longMin)
                        .le(StringUtils.isNotBlank(longMax),"longitude",longMax)
                        .ge(StringUtils.isNotBlank(latMin),"latitude",latMin)
                        .le(StringUtils.isNotBlank(latMax),"latitude",latMax)
                        .and(StringUtils.isNotBlank(queryStr),wrapper -> wrapper.like(StringUtils.isNotBlank(queryStr),"name",queryStr).or().like(StringUtils.isNotBlank(queryStr),"ip",queryStr))
        );
    }



    @DataSource("slave1")
    public List<AsAddressBase> findListForQuery(Map<String, Object> params)
    {
        // 添加查询条件
        String tenantCode=(String)params.get("tenantCode");
        String projectId=(String)params.get("projectId");
        String longMin=(String)params.get("longMin");
        String longMax=(String)params.get("longMax");
        String latMin=(String)params.get("latMin");
        String latMax=(String)params.get("latMax");

        String queryStr=(String)params.get("queryStr");

        return list(
                new QueryWrapper<AsAddressBase>()
                        /* */
                        .eq(StringUtils.isNotBlank(tenantCode),"tenant_code",tenantCode)
                        .eq(StringUtils.isNotBlank(projectId),"project_id",projectId)
                        .eq(StringUtils.isNotBlank(longMin),"longitude",longMin)
                        .eq(StringUtils.isNotBlank(longMax),"longitude",longMax)
                        .eq(StringUtils.isNotBlank(latMin),"latitude",latMin)
                        .eq(StringUtils.isNotBlank(latMax),"latitude",latMax)
                        .and(StringUtils.isNotBlank(queryStr),wrapper -> wrapper.like(StringUtils.isNotBlank(queryStr),"name",queryStr).or().like(StringUtils.isNotBlank(queryStr),"ip",queryStr))
        );
    }

    //根据id返回对应信息
    @DataSource("slave1")
    public AsAddressBase getEntityById(String id)
    {
        return this.getById(id);
    }

    @DataSource("slave1")
    public boolean saveEntity(AsAddressBase entity)
    {
        entity.setId(IdGen.snowflakeId());
        entity.setApplicationCode(SysUtil.getSysCode());
        return this.save(entity);
    }

    @DataSource("slave1")
    public boolean updateEntityById(AsAddressBase entity)
    {
        return this.updateById(entity);
    }

    @DataSource("slave1")
    public boolean deleteEntityById(String id)
    {
        return this.removeById(id);
    }

    @DataSource("slave1")
    public ResponseBean importBase(List<AsAddressBase> list)
    {

        List<String> errList=new ArrayList();

        ResponseBean r=new ResponseBean();

        try
        {
            // 直接查询所有项目出来(一般没有多少个,就算几万也不是事，如果，超过几万，改成普通按项目名称来查询  iAsProjectService.getProjectListByProjectCode )
            Map<String,AsProject> pMap=new HashMap<>();
            Map<String,List<AsAddressBase>> pAddressBaseListMap=new HashMap<String,List<AsAddressBase>>();
            AsProject p;
            for(int i=0;i<list.size();i++)
            {
                AsAddressBase temp=list.get(i);
                int row=i+1;
                String projectCode=temp.getProjectCode();
                if(StringUtils.isBlank(projectCode))
                {
                    errList.add("第"+row+"行的项目编号不能为空");
                    continue;
                }
                else
                {
                    //设置项目id和租户编码
                    if(pMap.containsKey(projectCode) && pMap.get(projectCode)!=null )
                    {
                        p=pMap.get(projectCode);
                    }
                    else
                    {
                        List<AsProject> pList=iAsProjectService.getProjectListByProjectCode(projectCode);
                        if(pList!=null && pList.size()>0)
                        {
                            p=pList.get(0);
                            pMap.put(projectCode,p);

                            //获取该项目下所有的地址库
                            List<AsAddressBase> asAddressBaseList=list(
                                    new QueryWrapper<AsAddressBase>()
                                            .eq(StringUtils.isNotBlank(p.getProjectId()),"project_id",p.getProjectId())
                            );
                            pAddressBaseListMap.put(p.getProjectId(),asAddressBaseList);

                        }
                        else
                        {
                            p=null;
                            pMap.put(projectCode,null);
                            errList.add("第"+row+"行的项目编号不存在");
                            continue;
                        }
                    }

                    if(p!=null)
                    {
                        temp.setProjectId(p.getProjectId());
                        temp.setTenantCode(p.getTenantCode());
                    }
                }

                if (StringUtils.isBlank(temp.getName()))
                {
                    errList.add("第"+row+"行的点位名称不能为空");
                    continue;
                }
                if (temp.getLatitude()==null)
                {
                    errList.add("第"+row+"行的经度不能为空");
                    continue;
                }
                if (temp.getLongitude()==null)
                {
                    errList.add("第"+row+"行的纬度不能为空");
                    continue;
                }

                // IP和经纬度一样就不能重复导入
                boolean isEquals=false;
                for(int j=0;j<i;j++)
                {
                    AsAddressBase base=list.get(j);
                    if(isEqualsAsAddressBase(temp,base))
                    {
                        errList.add("第"+row+"行的纬度已有IP经度纬度相同的 重复数据");
                        isEquals=true;break;
                    }
                }
                if(isEquals)
                {
                    continue;
                }
                //查找该项目，以前所有的地址库，
                List<AsAddressBase> addressBaseList=pAddressBaseListMap.get(temp.getProjectId());
                if(addressBaseList!=null)
                {
                    for(AsAddressBase base:addressBaseList)
                    {
                        if(isEqualsAsAddressBase(temp,base))
                        {
                            errList.add("第"+row+"行的纬度已有IP经度纬度相同的 重复数据");
                            isEquals=true;break;
                        }
                    }
                }
                if(isEquals)
                {
                    continue;
                }

                //保存数据
                temp.setId(IdGen.snowflakeId());
                temp.setApplicationCode(SysUtil.getSysCode());
                this.save(temp);
            }
        }
        catch (Exception e)
        {
            r=new ResponseBean(e);
        }

        if(errList.size()>0)
        {
            r.setCode(ResponseBean.FAIL);
        }

        r.setMsg("总上传行数：" + list.size() + "，已导入行数：" + (list.size() - (errList.size())) + "，错误行数：" + errList.size());
        Map<String,Object> data=new HashMap<>();
        r.setData(data);
        data.put("totalCount",list.size());
        data.put("errorCount",errList.size());
        data.put("successCount",list.size()-errList.size());

        if(errList.size()>0)
        {
            Date now =new Date();
            String saveDir="logs" + File.separator + new SimpleDateFormat("yyyyMMdd").format(now)+ File.separator;
            String name= "AddressBaseImportExcelErrorLog"+ new SimpleDateFormat("yyyymmddhhmmss").format(now) + Math.round(Math.random() * 10000)+".txt";

            String fileUrl = OsFileUtil.saveErrorTxtByList(errList, osValueComponent.winUploadPath,osValueComponent.linuxUploadPath,saveDir,name);
//            int lastIndex = fileUrl.lastIndexOf(File.separator);
//            String fileName = fileUrl.substring(lastIndex + 1);
            data.put("fileUrl","/v1/sys/file/download/" + fileUrl);
        }

        return r;

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

    private boolean isEqualsAsAddressBase(AsAddressBase old,AsAddressBase base)
    {
        boolean b1=false;
        boolean b2=false;
        boolean b3=false;

        if(
                (old.getLongitude()== null && base.getLongitude()==null)
                ||
                (old.getLongitude()!= null && old.getLongitude().compareTo(base.getLongitude())==0)
        )
        {
            b1=true;
        }

        if(
                (old.getLatitude()== null && base.getLatitude()==null)
                        ||
                        (old.getLatitude()!= null && old.getLatitude().compareTo(base.getLatitude())==0)
        )
        {
            b2=true;
        }

        if(
                (StringUtils.isEmpty(old.getIp()) && StringUtils.isEmpty(base.getIp()))
                        ||
                        (!StringUtils.isEmpty(old.getIp()) && old.getIp().equals(base.getIp()))
        )
        {
            b3=true;
        }

        return b1&&b2&&b3;
    }

    private String getNotNullString(Object o)
    {
        if(o==null)
        {
            return "";
        }
        if(o instanceof String)
        {
            return (String)o;
        }
        if(o instanceof Short)
        {
            return ((Short)o).toString();
        }
        if(o instanceof Integer)
        {
            return ((Integer)o).toString();
        }
        if(o instanceof Long)
        {
            return ((Long)o).toString();
        }
        if(o instanceof Double)
        {
            return ((Double)o).toString();
        }
        if(o instanceof Number)
        {
            return new Double(((Number)o).doubleValue()).toString();
        }
        return o.toString();
    }


}
