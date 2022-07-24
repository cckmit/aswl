package com.aswl.as.ibrs.service;

import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.common.core.utils.SysUtil;
import com.aswl.as.ibrs.api.dto.AlarmOrderHandleDto;
import com.aswl.as.ibrs.api.module.AlarmOrderHandle;
import com.aswl.as.ibrs.api.vo.AlarmOrderHandleVo;
import com.aswl.as.ibrs.mapper.AlarmOrderHandleMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@AllArgsConstructor
@Slf4j
@Service
public class AlarmOrderHandleService extends CrudService<AlarmOrderHandleMapper, AlarmOrderHandle> {
    private final AlarmOrderHandleMapper alarmOrderHandleMapper;

    /**
     * 新增派单处理工单设置
     *
     * @param alarmOrderHandle
     * @return int
     */
    @Transactional
    @Override
    public int insert(AlarmOrderHandle alarmOrderHandle) {
        return super.insert(alarmOrderHandle);
    }

    /**
     * 删除派单处理工单设置
     *
     * @param alarmOrderHandle
     * @return int
     */
    @Transactional
    @Override
    public int delete(AlarmOrderHandle alarmOrderHandle) {
        return super.delete(alarmOrderHandle);
    }

    public AlarmOrderHandle initByTenantCode(String tenantCode)
    {
        AlarmOrderHandle h=new AlarmOrderHandle();
        h.setNewRecord(true);
        h.setCommonValue(SysUtil.getUser(),SysUtil.getSysCode(),tenantCode);
        h.setOrderHandleType(0);//默认是自动处理
        h.setHandleUserId(null);
        super.insert(h);
        return h;
    }

    /**
     * 查询项目派单设置列表（若项目未配置过，会返回默认的配置值）
     * @param alarmOrderHandleDto
     * @return
     */
    public List<AlarmOrderHandleVo> findProjectOrderList(AlarmOrderHandleDto alarmOrderHandleDto){
        return alarmOrderHandleMapper.findProjectOrderList(alarmOrderHandleDto);
    }

    /**
     * 分页查询项目派单设置列表
     * @param page
     * @param alarmOrderHandleDto
     * @return
     */
    public PageInfo<AlarmOrderHandleVo> findProjectOrderPage(PageInfo<AlarmOrderHandleVo> page, AlarmOrderHandleDto alarmOrderHandleDto){
        PageHelper.startPage(page.getPageNum(), page.getPageSize());
        return new PageInfo(this.findProjectOrderList(alarmOrderHandleDto));
    }

    /**
     * 查询指定项目ID的派单设置
     * @param projectId
     * @return
     */
    public AlarmOrderHandleVo findByProjectId(String projectId){
        AlarmOrderHandleDto alarmOrderHandleDto = new AlarmOrderHandleDto();
        alarmOrderHandleDto.setProjectId(projectId);
        List<AlarmOrderHandleVo> list = this.findProjectOrderList(alarmOrderHandleDto);
        return (list != null && list.size() > 0) ? list.get(0) : null;
    }

    /**
     * 批量保存派单设置
     * @param alarmOrderHandleList
     * @return
     */
    public int batchSave(List<AlarmOrderHandle> alarmOrderHandleList){
        int result = 0;
        for(AlarmOrderHandle alarmOrderHandle : alarmOrderHandleList){
            if(StringUtils.isEmpty(alarmOrderHandle.getId())){
                alarmOrderHandle.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode(), alarmOrderHandle.getProjectId());
                result = result + this.insert(alarmOrderHandle);
            }else{
                alarmOrderHandle.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), alarmOrderHandle.getTenantCode(), alarmOrderHandle.getProjectId());
                result = result + this.update(alarmOrderHandle);
            }
        }
        return result;
    }

    /**
     * 根据租户编码删除工单设置
     * @param tenantCode
     * @return int
     */
   public  int deleteByTenantCode(String tenantCode){
       
       return alarmOrderHandleMapper.deleteByTenantCode(tenantCode);
   }
}