package com.aswl.as.ibrs.service;

import com.aswl.as.common.core.enums.RoleEnum;
import com.aswl.as.common.core.exceptions.CommonException;
import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.common.core.utils.SysUtil;
import com.aswl.as.common.security.constant.SecurityConstant;
import com.aswl.as.common.security.project.CloudCommon;
import com.aswl.as.ibrs.api.module.*;
import com.aswl.as.ibrs.filter.RoleContextHolder;
import com.aswl.as.ibrs.mapper.ExamineStandardMapper;
import com.aswl.as.ibrs.utils.StringUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@AllArgsConstructor
@Slf4j
@Service
public class ExamineStandardService extends CrudService<ExamineStandardMapper, ExamineStandard> {
    private final ExamineStandardMapper examineStandardMapper;

    private final ExamineRegionService examineRegionService;

    private final ExamineItemService examineItemService;

    private final ExamineTimePartConfigService examineTimePartConfigService;

    private final ExamineDeductRuleService examineDeductRuleService;

    private final ProjectService projectService;

    private final ExamineBaseLibService examineBaseLibService;


    /**
     * 新增考核标准
     *
     * @param standard
     * @return int
     */
    @Transactional
    @Override
    public int insert(ExamineStandard standard) {

        // 有特殊逻辑，需要编写特殊逻辑保存的数据
        // 新增时把所有都保存
        String userCode=SysUtil.getUser();
        String applicationCode=SysUtil.getSysCode();
        String tenantCode=SysUtil.getTenantCode();
        String projectId=standard.getProjectId();
        Date now=new Date();

        standard.setEnable(0);
        standard.setStoreTime(now);
        standard.setNewRecord(true);
        standard.setCommonValue(userCode,applicationCode,tenantCode,projectId);
        super.insert(standard);

        //直接插入
        if(standard.getRegionList()!=null)
        {
            for(ExamineRegion r:standard.getRegionList())
            {
                setRegion(true,r,userCode,applicationCode,tenantCode,projectId,standard.getId());
                examineRegionService.insert(r);
            }
        }

        //直接插入
        if(standard.getItemList()!=null)
        {
            for(ExamineItem item:standard.getItemList())
            {
                insertItem(item,userCode,applicationCode,tenantCode,projectId,standard.getId(),now);
            }
        }

        return 1;
    }

    //TODO update 也要重新写，查询也要重新写
    public ExamineStandard getStandardDetails(ExamineStandard standard)
    {
        standard.getId();

        standard=get(standard);

        standard.setRegionList(examineRegionService.getListByStandardId(standard.getId()));
        standard.setItemList(examineItemService.getItemListByStandardId(standard.getId()));

        return standard;
    }

    public List getExamineTree()
    {
        String roles = RoleContextHolder.getRole();
        String tenantCode = SysUtil.getTenantCode();
        String projectId = SysUtil.getProjectId();
        if (SysUtil.isAdmin() || roles.contains(SecurityConstant.ROLE_ADMIN)) { //超级管理员
            //加载所有项目
            tenantCode = null;
        } else if (roles != null && !"".equals(roles) &&
                (roles.contains(RoleEnum.ROLE_SYS_ADMIN.getCode()) || roles.contains(RoleEnum.ROLE_SYS_WATCHER.getCode()))) {  //租户系统管理员或租户系统值班员
            //加载用户所在租户的所有项目
        } else if (roles != null && !"".equals(roles) &&
                (roles.contains(RoleEnum.ROLE_PROJECT_ADMIN.getCode()) || roles.contains(RoleEnum.ROLE_PROJECT_WATCHER.getCode()))) {   //项目管理员或项目值班员
            //只加载用户关联的项目（已在SysUtil.getProjectId()获取）
        } else {

        }
        List<Map> result=new ArrayList<>();
        Project p=new Project();
        //2021-08-26，局域网版也调整为有多项目，因此此处不用区分判断
//        // 如果项目是局域网环境就指定projectId = 1,是云平台则不用指定
//        if (!CloudCommon.isCloud()) {
//            p.setProjectId(SecurityConstant.DEFAULT_PROJECT_ID);
//        }
        p.setTenantCode(tenantCode);
        List<Project> pList=projectService.findList(p);

        ExamineStandard tempS=new ExamineStandard();
        tempS.setTenantCode(tenantCode);
        List<ExamineStandard> sList=findList(tempS);

        Map<String,Object> map;
        List<ExamineStandard> list;
        for(Project temp : pList)
        {
            map=new HashMap<>();
            map.put("projectName",temp.getProjectName());
            map.put("projectId",temp.getProjectId());
            list=new ArrayList<>();
            map.put("standard",list);
            for(ExamineStandard s:sList)
            {
                if(temp.getProjectId().equals(s.getProjectId()))
                {
                    list.add(s);
                }
            }
            result.add(map);
        }
        return result;
    }

    /**
     * 更新数据
     *
     * @param entity entity
     * @return int
     */
    @Transactional
    public int update(ExamineStandard entity) {

        // 修改的话，尽量把原来的数据查出来
        List<ExamineItem> oldItemList=examineItemService.getItemListByStandardId(entity.getId());

        Map<String,ExamineItem> oldItemMap=new HashMap<>();

        // 根据原来的数据，删除
        List<ExamineRegion> oldRegionList=examineRegionService.getListByStandardId(entity.getId());

        String userCode=SysUtil.getUser();
        String applicationCode=entity.getApplicationCode();
        String tenantCode=entity.getTenantCode();
        String projectId=entity.getProjectId();
        Date now=new Date();

        //判断有没有删除的
        Set<String> oldSet=new HashSet<String>();
        Set<String> existSet=new HashSet<>();
        for(ExamineItem item:oldItemList)
        {
            oldSet.add(item.getId());
            oldItemMap.put(item.getId(),item);
        }

        for(ExamineItem item:entity.getItemList())
        {
            if(StringUtils.isEmpty(item.getId()))
            {
                //新增的
                insertItem(item,userCode,applicationCode,tenantCode,projectId,entity.getId(),now);
            }
            else
            {
                if(oldSet.contains(item.getId()))
                {
                    //如果在，就普通update
                    existSet.add(item.getId());

                    //更新之后也要更新里面的配置和规则
                    updateItem(item,oldItemMap.get(item.getId()),userCode,applicationCode,tenantCode,projectId,entity.getId(),now);
                }
            }
        }

        //这些都是需要删除的
        oldSet.removeAll(existSet);
        List<String> idList=new ArrayList<String>();
        if(oldSet.size()>0)
        {
            for(ExamineItem item:oldItemList)
            {
                if(oldSet.contains(item.getId()))
                {
                    //删除之前删除里面的配置和规则
                    for(ExamineTimePartConfig config:item.getTimePartConfigList())
                    {
                        idList.add(config.getId());
                    }
                    examineTimePartConfigService.deleteAll(idList.toArray(new String[idList.size()]));
                    idList.clear();

                    for(ExamineDeductRule rule:item.getRuleList())
                    {
                        idList.add(rule.getId());
                    }
                    examineDeductRuleService.deleteAll(idList.toArray(new String[idList.size()]));
                    idList.clear();

                    examineItemService.delete(item);
                }
            }
        }

        //更新所选分区
        updateRegion(entity.getRegionList(),oldRegionList,userCode,applicationCode,tenantCode,projectId,entity.getId());

        entity.setStoreTime(now);
        entity.setNewRecord(false);
        entity.setCommonValue(userCode,applicationCode,tenantCode,projectId);
        return dao.update(entity);
    }

    //判断有没有新增修改的
    private void updateRegion(List<ExamineRegion> list,List<ExamineRegion> oldList,String userCode,String applicationCode,String tenantCode,String projectId,String standardId)
    {
        Set<String> oldSet=new HashSet<String>();
        Set<String> existSet=new HashSet<>();

        for(ExamineRegion region:oldList)
        {
            oldSet.add(region.getId());
        }

        for(ExamineRegion r:list)
        {
            if(StringUtils.isEmpty(r.getId()))
            {
                //新增
                setRegion(true,r,userCode,applicationCode,tenantCode,projectId,standardId);
                examineRegionService.insert(r);
            }
            else
            {
                if (oldSet.contains(r.getId()))
                {
                    //更新
                    existSet.add(r.getId());
                    setRegion(false,r,userCode,applicationCode,tenantCode,projectId,standardId);
                    examineRegionService.update(r);
                }
            }
        }

        //删除
        oldSet.removeAll(existSet);
        if(oldSet.size()>0)
        {
            examineDeductRuleService.deleteAll(oldSet.toArray(new String[oldSet.size()]));
        }
    }

    private void setRegion(boolean isNewRecord,ExamineRegion r,String userCode,String applicationCode,String tenantCode,String projectId,String standardId)
    {
        r.setNewRecord(isNewRecord);
        r.setExamineStandardId(standardId);
        r.setCommonValue(userCode,applicationCode,tenantCode,projectId);
//        examineRegionService.insert(r);
    }



    private void updateItem(ExamineItem item,ExamineItem oldItem,String userCode,String applicationCode,String tenantCode,String projectId,String standardId,Date storeTime)
    {
        setItem(false,item,userCode,applicationCode,tenantCode,projectId,standardId,storeTime);
        //根据选择的类型清了时间部分
        clearRule(item);
        examineItemService.update(item);



        //实际上一定会有的，因为是外面判断过的
        if(oldItem==null)
        {
            throw new CommonException("更新 考核项 错误！");
        }

        List<ExamineDeductRule> oldRuleList = oldItem.getRuleList();
        List<ExamineTimePartConfig> oldConfigList=oldItem.getTimePartConfigList();

        //判断原来是否有这些数据，如果没有，就新增，
        Set<String> oldSet=new HashSet<String>();
        Set<String> existSet=new HashSet<>();

        for(ExamineDeductRule rule:oldRuleList)
        {
            oldSet.add(rule.getId());
        }

        for(ExamineDeductRule rule:item.getRuleList())
        {
            if(StringUtils.isEmpty(rule.getId()))
            {
                //新增
                setRule(true,rule,userCode,applicationCode,tenantCode,projectId,item.getId(),storeTime);
                examineDeductRuleService.insert(rule);
            }
            else
            {
                if (oldSet.contains(rule.getId()))
                {
                    //更新
                    existSet.add(rule.getId());
                    setRule(false,rule,userCode,applicationCode,tenantCode,projectId,item.getId(),storeTime);
                    examineDeductRuleService.update(rule);
                }
            }
        }

        //删除
        oldSet.removeAll(existSet);
        if(oldSet.size()>0)
        {
            examineDeductRuleService.deleteAll(oldSet.toArray(new String[oldSet.size()]));
        }


        //--------上面是处理rule，下面是处理config---------------

        oldSet.clear();
        existSet.clear();
        for(ExamineTimePartConfig config:oldConfigList)
        {
            oldSet.add(config.getId());
        }
        for(ExamineTimePartConfig config:item.getTimePartConfigList())
        {
            if(StringUtils.isEmpty(config.getId()))
            {
                //新增
                setConfig(true,config,userCode,applicationCode,tenantCode,projectId,item.getId(),storeTime);
                examineTimePartConfigService.insert(config);
            }
            else
            {
                if (oldSet.contains(config.getId()))
                {
                    //更新
                    existSet.add(config.getId());
                    setConfig(false,config,userCode,applicationCode,tenantCode,projectId,item.getId(),storeTime);
                    examineTimePartConfigService.update(config);
                }
            }
        }

        //删除
        oldSet.removeAll(existSet);
        if(oldSet.size()>0)
        {
            examineTimePartConfigService.deleteAll(oldSet.toArray(new String[oldSet.size()]));
        }
    }

    private void setRespondTime(ExamineTimePartConfig config)
    {
        if(config.getRespondNum()==null)
        {
            throw new CommonException("响应时长数量 不能为空");
        }

        if("day".equals(config.getRespondUnit()))
        {
            config.setRespondTime(config.getRespondNum()*24*60*60);
        }
        else if("hours".equals(config.getRespondUnit()))
        {
            config.setRespondTime(config.getRespondNum()*60*60);
        }
        else if("minute".equals(config.getRespondUnit()))
        {
            config.setRespondTime(config.getRespondNum()*60);
        }
        else
        {
            throw new CommonException("响应时长单位错误.");
        }

    }

    //到时候可能整合一个删除函数出来

    // 编写新建item和里面的数据的函数
    private void insertItem(ExamineItem item,String userCode,String applicationCode,String tenantCode,String projectId,String standardId,Date storeTime)
    {
        setItem(true,item,userCode,applicationCode,tenantCode,projectId,standardId,storeTime);
        //根据选择的类型清了时间部分
        clearRule(item);
        examineItemService.insert(item);

        for(ExamineTimePartConfig config:item.getTimePartConfigList())
        {
            setConfig(true,config,userCode,applicationCode,tenantCode,projectId,item.getId(),storeTime);
            examineTimePartConfigService.insert(config);
        }

        for(ExamineDeductRule rule:item.getRuleList())
        {
            setRule(true,rule,userCode,applicationCode,tenantCode,projectId,item.getId(),storeTime);
            examineDeductRuleService.insert(rule);
        }
    }

    private void setItem(boolean isNewRecord,ExamineItem item,String userCode,String applicationCode,String tenantCode,String projectId,String standardId,Date storeTime)
    {
        item.setNewRecord(isNewRecord);
        item.setCommonValue(userCode,applicationCode,tenantCode,projectId);
        item.setExamineStandardId(standardId);//standard.getId()
        item.setStoreTime(storeTime);
    }

    private void setRule(boolean isNewRecord,ExamineDeductRule rule,String userCode,String applicationCode,String tenantCode,String projectId,String itemId,Date storeTime)
    {
        rule.setNewRecord(isNewRecord);
        rule.setCommonValue(userCode,applicationCode,tenantCode,projectId);
        rule.setExamineItemId(itemId);
        rule.setStoreTime(storeTime);
    }

    private void setConfig(boolean isNewRecord,ExamineTimePartConfig config,String userCode,String applicationCode,String tenantCode,String projectId,String itemId,Date storeTime)
    {
        config.setNewRecord(isNewRecord);
        config.setExamineItemId(itemId);
        config.setCommonValue(userCode,applicationCode,tenantCode,projectId);
        config.setStoreTime(storeTime);
        setRespondTime(config);
    }

    /**
     * 删除考核标准
     *
     * @param examineStandard
     * @return int
     */
    @Transactional
    @Override
    public int delete(ExamineStandard examineStandard) {

        return deleteExamineData(examineStandard.getId());
//        return super.delete(examineStandard);
    }

    @Transactional
    public int  deleteExamineData(String ids)
    {
        String[] arr=ids.split(",");
        ExamineStandard temp=new ExamineStandard();
        Set<String>regionIdSet=new HashSet<>();
        Set<String>itemIdSet=new HashSet<>();
        Set<String> ruleIdSet=new HashSet<>();
        Set<String> configIdSet=new HashSet<>();
        ExamineStandard s;

        for(String id:arr)
        {
            temp.setId(id);
            s=getStandardDetails(temp);
            if(s!=null)
            {
                for(ExamineRegion r:s.getRegionList())
                {
                    regionIdSet.add(r.getId());
                }

                for(ExamineItem i:s.getItemList())
                {
                    itemIdSet.add(i.getId());
                    for(ExamineDeductRule r:i.getRuleList())
                    {
                        ruleIdSet.add(r.getId());
                    }
                    for(ExamineTimePartConfig config:i.getTimePartConfigList())
                    {
                        configIdSet.add(config.getId());
                    }
                }

                if(regionIdSet.size()>0)
                {
                    examineRegionService.deleteAll(regionIdSet.toArray(new String[regionIdSet.size()]));
                }
                if(itemIdSet.size()>0)
                {
                    examineItemService.deleteAll(itemIdSet.toArray(new String[itemIdSet.size()]));
                }
                if(ruleIdSet.size()>0)
                {
                    examineItemService.deleteAll(ruleIdSet.toArray(new String[ruleIdSet.size()]));
                }
                if(configIdSet.size()>0)
                {
                    examineItemService.deleteAll(configIdSet.toArray(new String[configIdSet.size()]));
                }

                //最后删除
                examineStandardMapper.delete(s);
            }

        }

        return 1;
    }

    public ExamineStandard getById(String standardId) {
        ExamineStandard examineStandard = new ExamineStandard();
        examineStandard.setId(standardId);
        return examineStandardMapper.get(examineStandard);
    }

    /**
     * 根据选择的类型清了时间部分
     * @param item
     */
    private void clearRule(ExamineItem item)
    {

        // 如果不按 时段考核 ，就把timePart 全清了删了
//        if(!Integer.valueOf(1).equals(item.getExamineTimePart()))
//        {
//            item.getRuleList().clear();
//        }

        ExamineBaseLib l=new ExamineBaseLib();
        l.setId(item.getExamineBaseLibId());
        l=examineBaseLibService.get(l);

        if(l==null)
        {
            throw new CommonException("传递过来的 考核类型 错误！");
        }

        //按规巡检 的类型清了时间
        if("PATROL".equals(l.getCode()))
        {
            item.setExamineTimePart(0);
            item.getTimePartConfigList().clear();
        }
        else
        {
            item.setExamineTimePart(1);
        }
    }

}