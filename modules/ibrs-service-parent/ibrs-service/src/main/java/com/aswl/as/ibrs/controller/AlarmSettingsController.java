package com.aswl.as.ibrs.controller;

import com.aswl.as.common.core.constant.CommonConstant;
import com.aswl.as.common.core.enums.RoleEnum;
import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.utils.IdGen;
import com.aswl.as.common.core.utils.PageUtil;
import com.aswl.as.common.core.utils.SysUtil;
import com.aswl.as.common.core.web.BaseController;
import com.aswl.as.common.log.annotation.Log;
import com.aswl.as.common.security.constant.SecurityConstant;
import com.aswl.as.ibrs.api.dto.AlarmSettingsDto;
import com.aswl.as.ibrs.api.module.AlarmSettings;
import com.aswl.as.ibrs.api.module.ReportSetting;
import com.aswl.as.ibrs.api.vo.AlarmSettingsVo;
import com.aswl.as.ibrs.enums.AlarmTypePrefix;
import com.aswl.as.ibrs.filter.RoleContextHolder;
import com.aswl.as.ibrs.service.AlarmSettingsService;
import com.aswl.as.ibrs.service.AlarmTypeService;
import com.aswl.as.ibrs.utils.BeanUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import com.github.pagehelper.PageInfo;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang.StringUtils;

/**
 * 告警设置
 * @author hzj
 * @date 2020/12/08 10:43
 */
@Slf4j
@AllArgsConstructor
@Api(value = "/api/v1/alarmSettings", tags = "告警设置")
@RestController
@RequestMapping("/v1/alarmSettings")
public class AlarmSettingsController extends BaseController {

    private final AlarmSettingsService alarmSettingsService;
    private final AlarmTypeService alarmTypeService;

    /**
     * 根据ID获取告警设置
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "根据告警设置ID查询告警设置")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "告警设置ID", paramType = "path", required = true, dataType = "String"))
    @GetMapping(value = "/{id}")
    public ResponseBean<AlarmSettings> findById(@PathVariable("id") String id) {
        AlarmSettings alarmSettings = new AlarmSettings();
        alarmSettings.setId(id);
        return new ResponseBean<>(alarmSettingsService.get(alarmSettings));
    }

    /**
     * 查询所有告警设置
     *
     * @param alarmSettings
     * @return ResponseBean
     */

    @ApiOperation(value = "查询所有告警设置列表")
    @ApiImplicitParam(name = "alarmSettings", value = "告警设置对象", paramType = "path", required = true, dataType = "alarmSettings")
    @GetMapping(value = "alarmSettingss")
    public ResponseBean
            <List<AlarmSettings>> findAll(AlarmSettings alarmSettings) {
        alarmSettings.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>(alarmSettingsService.findList(alarmSettings));
    }

    /**
     * 分页查询告警设置列表
     *
     * @param pageNum
     * @param pageSize
     * @param sort
     * @param order
     * @param alarmSettings
     * @return PageInfo
     */
    @ApiOperation(value = "分页查询告警设置列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "alarmSettings", value = "告警设置信息", dataType = "alarmSettings")
    })

    @GetMapping("alarmSettingsList")
    public ResponseBean
            <PageInfo<AlarmSettings>> alarmSettingsList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                                        @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                                        @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                                        @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
                                                        AlarmSettings alarmSettings) {
        alarmSettings.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>(alarmSettingsService.findPage(PageUtil.pageInfo(pageNum, pageSize, sort, order), alarmSettings));
    }

    /**
     * 新增告警设置
     *
     * @param alarmSettings
     * @return ResponseBean
     */
    @ApiOperation(value = "新增告警设置", notes = "新增告警设置")
    @PostMapping
    @Log("新增告警设置")
    public ResponseBean
            <Boolean> insertAlarmSettings(@RequestBody @Valid AlarmSettings alarmSettings) {
        alarmSettings.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        alarmSettings.setStoreTime(new Date());
        String alarmTypes = alarmSettings.getAlarmType();
        if (alarmTypes != null && !"".equals(alarmTypes)) {
            if(alarmTypes.contains(AlarmTypePrefix.DCPOWER_EXT.getPrefix())){
                String type = alarmTypeService.findType(AlarmTypePrefix.DCPOWER_EXT.getPrefix());
                alarmTypes = alarmTypes.replaceAll(AlarmTypePrefix.DCPOWER_EXT.getPrefix(),type);
            }
            if(alarmTypes.contains(AlarmTypePrefix.VOLTAGE_VALUE_EXT.getPrefix())){
                String type = alarmTypeService.findType(AlarmTypePrefix.VOLTAGE_VALUE_EXT.getPrefix());
                alarmTypes = alarmTypes.replaceAll(AlarmTypePrefix.VOLTAGE_VALUE_EXT.getPrefix(),type);
            }
            if(alarmTypes.contains(AlarmTypePrefix.FIBRE_OPTICAL_EXT.getPrefix())){
                String type = alarmTypeService.findType(AlarmTypePrefix.FIBRE_OPTICAL_EXT.getPrefix());
                alarmTypes = alarmTypes.replaceAll(AlarmTypePrefix.FIBRE_OPTICAL_EXT.getPrefix(),type);
            }
            if(alarmTypes.contains(AlarmTypePrefix.RJ45_EXT.getPrefix())){
                String type = alarmTypeService.findType(AlarmTypePrefix.RJ45_EXT.getPrefix());
                alarmTypes = alarmTypes.replaceAll(AlarmTypePrefix.RJ45_EXT.getPrefix(),type);
            }
            if(alarmTypes.contains(AlarmTypePrefix.FAN_EXT.getPrefix())){
                String type = alarmTypeService.findType(AlarmTypePrefix.FAN_EXT.getPrefix());
                alarmTypes = alarmTypes.replaceAll(AlarmTypePrefix.FAN_EXT.getPrefix(),type);
            }
            alarmSettings.setAlarmType(alarmTypes);
        }
        return new ResponseBean<>(alarmSettingsService.insert(alarmSettings) > 0);
    }

    /**
     * 修改告警设置
     *
     * @param alarmSettingsDto
     * @return ResponseBean
     */

    @ApiOperation(value = "更新告警设置信息", notes = "根据告警设置ID更新告警设置信息")
    @Log("修改告警设置")
    @PutMapping
    public ResponseBean
            <Boolean> updateAlarmSettings(@RequestBody @Valid AlarmSettingsDto alarmSettingsDto) {
        String tenantCode = SysUtil.getTenantCode();
        AlarmSettings alarmSettings = new AlarmSettings();
        BeanUtils.copyProperties(alarmSettingsDto,alarmSettings);
        String alarmTypes = alarmSettings.getAlarmType();
        if (alarmTypes != null && !"".equals(alarmTypes)) {
            if(alarmTypes.contains(AlarmTypePrefix.DCPOWER_EXT.getPrefix())){
                String type = alarmTypeService.findType(AlarmTypePrefix.DCPOWER_EXT.getPrefix());
                alarmTypes = alarmTypes.replaceAll(AlarmTypePrefix.DCPOWER_EXT.getPrefix(),type);
            }
            if(alarmTypes.contains(AlarmTypePrefix.VOLTAGE_VALUE_EXT.getPrefix())){
                String type = alarmTypeService.findType(AlarmTypePrefix.VOLTAGE_VALUE_EXT.getPrefix());
                alarmTypes = alarmTypes.replaceAll(AlarmTypePrefix.VOLTAGE_VALUE_EXT.getPrefix(),type);
            }
            if(alarmTypes.contains(AlarmTypePrefix.FIBRE_OPTICAL_EXT.getPrefix())){
                String type = alarmTypeService.findType(AlarmTypePrefix.FIBRE_OPTICAL_EXT.getPrefix());
                alarmTypes = alarmTypes.replaceAll(AlarmTypePrefix.FIBRE_OPTICAL_EXT.getPrefix(),type);
            }
            if(alarmTypes.contains(AlarmTypePrefix.RJ45_EXT.getPrefix())){
                String type = alarmTypeService.findType(AlarmTypePrefix.RJ45_EXT.getPrefix());
                alarmTypes = alarmTypes.replaceAll(AlarmTypePrefix.RJ45_EXT.getPrefix(),type);
            }
            if(alarmTypes.contains(AlarmTypePrefix.FAN_EXT.getPrefix())){
                String type = alarmTypeService.findType(AlarmTypePrefix.FAN_EXT.getPrefix());
                alarmTypes = alarmTypes.replaceAll(AlarmTypePrefix.FAN_EXT.getPrefix(),type);
            }
            alarmSettings.setAlarmType(alarmTypes);
        }
        String projectId = alarmSettings.getProjectId();
        String isAsos = alarmSettingsDto.getIsAsos();
        if(!CommonConstant.IS_ASOS_TRUE.equals(isAsos)){  //不是云平台
            projectId = "1";
        }
        AlarmSettings db = alarmSettingsService.getByTenantCode(projectId,tenantCode);
        if(db == null){
            alarmSettings.setTenantCode(SysUtil.getTenantCode());
            alarmSettings.setProjectId(projectId);
            alarmSettings.setApplicationCode(SysUtil.getSysCode());
            alarmSettings.setId(IdGen.snowflakeId());
            alarmSettings.setStoreTime(new Date());
            return new ResponseBean<>(alarmSettingsService.insert(alarmSettings)>0);
        }else {
            alarmSettings.setId(db.getId());
            return new ResponseBean<>(alarmSettingsService.update(alarmSettings) > 0);
        }
    }

    /**
     * 根据告警设置ID删除告警设置信息
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "删除告警设置信息", notes = "根据告警设置ID删除告警设置信息")
    @ApiImplicitParam(name = "id", value = "告警设置ID", paramType = "path", required = true, dataType =
            "String")
    @DeleteMapping(value = "/{id}")
    public ResponseBean
            <Boolean> deleteAlarmSettingsById(@PathVariable("id") String id) {
        AlarmSettings alarmSettings = new AlarmSettings();
        alarmSettings.setId(id);
        alarmSettings.setNewRecord(false);
        alarmSettings.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(alarmSettingsService.delete(alarmSettings) > 0);
    }

    /**
     * 批量删除告警设置信息
     *
     * @param alarmSettings
     * @return ResponseBean
     */

    @ApiOperation(value = "批量删除告警设置", notes = "根据告警设置ID批量删除告警设置")
    @ApiImplicitParam(name = "alarmSettings", value = "告警设置信息", dataType = "alarmSettings")
    @Log("批量删除告警设置")
    @PostMapping("deleteAll")
    public ResponseBean
            <Boolean> deleteAllAlarmSettings(@RequestBody AlarmSettings alarmSettings) {
        boolean success = false;
        try {
            if (StringUtils.isNotEmpty(alarmSettings.getIdString()))
                success = alarmSettingsService.deleteAll(alarmSettings.getIdString().split(",")) > 0;
        } catch (Exception e) {
            log.error("删除告警设置失败！", e);
        }
        return new ResponseBean<>(success);
    }


    /**
     * 根据租户获取告警设置
     * @return ResponseBean
     */
    @ApiOperation(value = "根据租户获取告警设置")
    @GetMapping("getAlarmSet")
    public ResponseBean<AlarmSettings> getAlarmSet(String isAsos,String projectId) {
        String tenantCode = SysUtil.getTenantCode();
        if(!CommonConstant.IS_ASOS_TRUE.equals(isAsos)){  //不是云平台
            projectId = "1";
        }
        AlarmSettings settings = alarmSettingsService.getByTenantCode(projectId, tenantCode);
        if (settings != null && settings.getAlarmType() != null && !"".equals(settings.getAlarmType())) {
            StringBuilder alarmTypesBuilder = new StringBuilder();
            String[] split = settings.getAlarmType().split(",");
            for (String s : split) {
                if(s.startsWith(AlarmTypePrefix.DCPOWER_EXT.getPrefix())){
                    if(!alarmTypesBuilder.toString().contains(AlarmTypePrefix.DCPOWER_EXT.getPrefix())){
                        alarmTypesBuilder.append(AlarmTypePrefix.DCPOWER_EXT.getPrefix()).append(",");
                    }
                }
                else if(s.startsWith(AlarmTypePrefix.VOLTAGE_VALUE_EXT.getPrefix())){
                    if(!alarmTypesBuilder.toString().contains(AlarmTypePrefix.VOLTAGE_VALUE_EXT.getPrefix())){
                        alarmTypesBuilder.append(AlarmTypePrefix.VOLTAGE_VALUE_EXT.getPrefix()).append(",");
                    }
                }
                else if(s.startsWith(AlarmTypePrefix.FIBRE_OPTICAL_EXT.getPrefix())){
                    if(!alarmTypesBuilder.toString().contains(AlarmTypePrefix.FIBRE_OPTICAL_EXT.getPrefix())){
                        alarmTypesBuilder.append(AlarmTypePrefix.FIBRE_OPTICAL_EXT.getPrefix()).append(",");
                    }
                }
                else if(s.startsWith(AlarmTypePrefix.RJ45_EXT.getPrefix())){
                    if(!alarmTypesBuilder.toString().contains(AlarmTypePrefix.RJ45_EXT.getPrefix())){
                        alarmTypesBuilder.append(AlarmTypePrefix.RJ45_EXT.getPrefix()).append(",");
                    }
                }
                else if(s.startsWith(AlarmTypePrefix.FAN_EXT.getPrefix())){
                    if(!alarmTypesBuilder.toString().contains(AlarmTypePrefix.FAN_EXT.getPrefix())){
                        alarmTypesBuilder.append(AlarmTypePrefix.FAN_EXT.getPrefix()).append(",");
                    }
                }
                else {
                alarmTypesBuilder.append(s).append(",");
                }
            }
            String alarmTypes = alarmTypesBuilder.toString().substring(0,alarmTypesBuilder.toString().length()-1);
            settings.setAlarmType(alarmTypes);
        }
        return new ResponseBean<>(settings);
    }


    /**
     * 查询告警联动
     *
     * @param alarmSettings
     * @return ResponseBean
     */

    @ApiOperation(value = "查询所有告警设置列表")
    @ApiImplicitParam(name = "alarmSettings", value = "告警设置对象", paramType = "path", required = true, dataType = "alarmSettings")
    @GetMapping(value = "getAlarmList")
    public ResponseBean
            <List<AlarmSettingsVo>> getProjectAlarmList(AlarmSettings alarmSettings) {
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
        alarmSettings.setTenantCode(tenantCode);
        alarmSettings.setProjectId(projectId);
        return new ResponseBean<>(alarmSettingsService.getProjectAlarmSettings(alarmSettings));
    }

    /**
     * 新增修改告警设置列表
     *
     * @param alarmSettingsDto
     * @return ResponseBean
     */

    @ApiOperation(value = "新增修改告警设置列表")
    @ApiImplicitParam(name = "alarmSettingsDto", value = "告警设置对象", paramType = "body", required = true, dataType = "AlarmSettingsDto")
    @PostMapping(value = "insertOrUpdate")
    public ResponseBean
            <Boolean> inserUpdateAlarmSettings(@RequestBody AlarmSettingsDto alarmSettingsDto) {
        return new ResponseBean<>(alarmSettingsService.inserUpdateAlarmSettings(alarmSettingsDto) > 0 );
    }
}
