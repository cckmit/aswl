package com.aswl.as.user.controller;

import com.alibaba.fastjson.JSON;
import com.aswl.as.common.core.constant.MqConstant;
import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.web.BaseController;
import com.aswl.as.common.log.annotation.Log;
import com.aswl.as.user.api.dto.ConfigDto;
import com.aswl.as.user.api.module.Config;
import com.aswl.as.user.mq.MQSender;
import com.aswl.as.user.service.ConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.apache.ibatis.annotations.Param;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import com.aswl.as.common.core.utils.SysUtil;
import com.aswl.as.common.core.constant.CommonConstant;
import com.github.pagehelper.PageInfo;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.*;

import org.apache.commons.lang.StringUtils;
import com.aswl.as.common.core.utils.PageUtil;

/**
 * 系统配置信息表controller
 *
 * @author dingfei
 * @date 2019-12-18 10:57
 */
@Slf4j
@AllArgsConstructor
@Api(value = "/api/v1/config", tags = "系统配置信息表")
@RestController
@RequestMapping("/v1/config")
public class ConfigController extends BaseController {

    private final ConfigService configService;

    private MQSender mqSender;

    /**
     * 根据ID获取系统配置信息表
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "根据系统配置信息表ID查询系统配置信息表")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "系统配置信息表ID", paramType = "path", required = true, dataType = "String"))
    @GetMapping(value = "/{id}")
    public ResponseBean<Config> findById(@PathVariable("id") String id) {
        Config config = new Config();
        config.setId(id);
        return new ResponseBean<>(configService.get(config));
    }

    /**
     * 查询所有系统配置信息表
     *
     * @param config
     * @return ResponseBean
     */

    @ApiOperation(value = "查询所有系统配置信息表列表")
    @ApiImplicitParam(name = "config", value = "系统配置信息表对象", paramType = "path", required = true, dataType = "config")
    @GetMapping(value = "configs")
    public ResponseBean
            <List<Config>> findAll(Config config) {
        config.setTenantCode(SysUtil.getTenantCode());
       /*  List<Config> list = configService.findList(config);
       List<Map> result = new ArrayList<>();
        Map map1 = new LinkedHashMap();
        Map map2 = new LinkedHashMap();
        Map map3= new LinkedHashMap();
        for (Config vo : list) {
            if (vo.getParamKey().equals("SYS_NAME")) {
                map1.put("id", vo.getId());
                map1.put("paramKey",vo.getParamKey());
                map1.put("paramValue", vo.getParamValue());
                result.add(map1);
            } else if (vo.getParamKey().equals("SYS_LOGO")) {
                map2.put("id", vo.getId());
                map2.put("paramKey",vo.getParamKey());
                map2.put("paramValue", vo.getParamValue());
                result.add(map2);
            } else if (vo.getParamKey().equals("SYS_ICON")) {
                map3.put("id", vo.getId());
                map3.put("paramKey",vo.getParamKey());
                map3.put("paramValue", vo.getParamValue());
                result.add(map3);
            }

        }*/
        return new ResponseBean<>(configService.findList(config));
    }

    /**
     * 分页查询系统配置信息表列表
     *
     * @param pageNum
     * @param pageSize
     * @param sort
     * @param order
     * @param config
     * @return PageInfo
     */
    @ApiOperation(value = "分页查询系统配置信息表列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "config", value = "系统配置信息表信息", dataType = "config")
    })

    @GetMapping("configList")
    public ResponseBean<PageInfo<Config>> configList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                                     @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                                     @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                                     @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
                                                     Config config) {
        config.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>(configService.findPage(PageUtil.pageInfo(pageNum, pageSize, sort, order), config));
    }

    /**
     * 新增系统配置信息表
     *
     * @param configDto
     * @return ResponseBean
     */
    @ApiOperation(value = "新增系统配置信息表", notes = "新增系统配置信息表")
    @PostMapping
    @Log("新增系统配置信息表")
    public ResponseBean
            <Boolean> insertConfig(@RequestBody @Valid ConfigDto configDto) {
        Config config = new Config();
        BeanUtils.copyProperties(configDto, config);
        config.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(configService.insert(config) > 0);
    }

    /**
     * 修改系统配置信息表
     *
     * @param configDto
     * @return ResponseBean
     */

    @ApiOperation(value = "更新系统配置信息表信息", notes = "根据系统配置信息表ID更新系统配置信息表信息")
    @Log("修改系统配置信息表")
    @PutMapping
    public ResponseBean
            <Boolean> updateConfig(@RequestBody @Valid ConfigDto configDto) {
        Config config = new Config();
        BeanUtils.copyProperties(configDto, config);
        config.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        int result = configService.update(config);
        if(result > 0){
            //通知前台刷新
            Map object = new HashMap();
            object.put("alias", "sysInfo");
            object.put("value", "refresh");
            mqSender.send(MqConstant.SystemMqMessage.COMMON_MESSAGE_FANOUT_EXCHANGE, MqConstant.SystemMqMessage.SYSTEM_BROADCAST_MESSAGE_QUEUE, JSON.toJSONString(object), MessageProperties.CONTENT_TYPE_TEXT_PLAIN);
        }
        return new ResponseBean<>( result > 0);
    }

    /**
     * 根据系统配置信息表ID删除系统配置信息表信息
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "删除系统配置信息表信息", notes = "根据系统配置信息表ID删除系统配置信息表信息")
    @ApiImplicitParam(name = "id", value = "系统配置信息表ID", paramType = "path", required = true, dataType = "String")
    @DeleteMapping(value = "/{id}")
    public ResponseBean
            <Boolean> deleteConfigById(@PathVariable("id") String id) {
        Config config = new Config();
        config.setId(id);
        config.setNewRecord(false);
        config.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(configService.delete(config) > 0);
    }

    /**
     * 批量删除系统配置信息表信息
     *
     * @param config
     * @return ResponseBean
     */

    @ApiOperation(value = "批量删除系统配置信息表", notes = "根据系统配置信息表ID批量删除系统配置信息表")
    @ApiImplicitParam(name = "config", value = "系统配置信息表信息", dataType = "config")
    @Log("批量删除系统配置信息表")
    @PostMapping("deleteAll")
    public ResponseBean
            <Boolean> deleteAllConfig(@RequestBody Config config) {
        boolean success = false;
        try {
            if (StringUtils.isNotEmpty(config.getIdString()))
                success = configService.deleteAll(config.getIdString().split(",")) > 0;
        } catch (Exception e) {
            log.error("删除系统配置信息表失败！", e);
        }
        return new ResponseBean<>(success);
    }

    /**
     *  查询是否云平台
     * @return  ResponseBean
     */
    @ApiOperation(value = "查询是否云平台", notes = "查询是否云平台")
    @GetMapping
    public ResponseBean
            <Boolean> findIsCloud() {
        return new ResponseBean<>(configService.findIsCloud(SysUtil.getTenantCode()) > 0);
    }

    /**
     *  查询是否云平台
     * @return  ResponseBean
     */
    @ApiOperation(value = "查询是否云平台", notes = "查询是否云平台")
    @GetMapping("isCloud")
    public ResponseBean
            <Boolean> isCloud(@RequestParam("tenantCode") String tenantCode) {
        return new ResponseBean<>(configService.findIsCloud(tenantCode) > 0);
    }



    @ApiOperation(value = "根据条件 查询所有系统配置信息表 列表")
    @ApiImplicitParam(name = "config", value = "系统配置信息表对象", paramType = "path", required = true, dataType = "config")
    @PostMapping(value = "findConfigList")
    public ResponseBean
            <List<Config>> findList(@RequestBody Config config) {
        config.setTenantCode(SysUtil.getTenantCode());
        List<Config> list = configService.findList(config);
        return new ResponseBean<>(list);
    }

    // 专门给统计排版提供一个函数,用作获取对应的信息
    @ApiOperation(value = "专门给统计排版提供一个函数,用作获取对应的信息")
    @ApiImplicitParam(name = "config", value = "专门给统计排版提供一个函数,用作获取对应的信息", paramType = "path", required = true, dataType = "config")
    @GetMapping(value = "getPrintConfig")
    public ResponseBean<Map> getPrintConfig(Config config) {
        config.setTenantCode(SysUtil.getTenantCode());
        List<Config> list = configService.findList(config);

        Map<String,String>map=new HashMap<>();
        String report=null;
        String reportCopy=null;
        String printType=null;
        for(Config c:list)
        {
            if(CommonConstant.CONFIG_PARAM_KEY_REPORT_PERSON.equals(c.getParamKey()))
            {
                report=c.getParamValue();
            }
            if(CommonConstant.CONFIG_PARAM_KEY_REPORT_COPY_PERSON.equals(c.getParamKey()))
            {
                reportCopy=c.getParamValue();
            }
            if(CommonConstant.CONFIG_PARAM_KEY_REPORT_TOTAL_PRINT_TYPE.equals(c.getParamKey()))
            {
                printType=c.getParamValue();
            }
        }

        map.put(CommonConstant.CONFIG_PARAM_KEY_REPORT_PERSON,report);
        map.put(CommonConstant.CONFIG_PARAM_KEY_REPORT_COPY_PERSON,reportCopy);
        map.put(CommonConstant.CONFIG_PARAM_KEY_REPORT_TOTAL_PRINT_TYPE,printType);

        return new ResponseBean<>(map);
    }

    // 用来保存数据
    @ApiOperation(value = "专门给统计排版提供一个函数,用作保存对应的信息")
    @ApiImplicitParam(name = "config", value = "专门给统计排版提供一个函数,用作保存对应的信息", paramType = "path", required = true, dataType = "config")
    @PostMapping(value = "savePrintConfig")
    public ResponseBean<Boolean> savePrintConfig(@RequestBody Map<String,String> map)
    {

        if(map.containsKey(CommonConstant.CONFIG_PARAM_KEY_REPORT_PERSON))
        {
            configService.saveOrUpdatePrintConfig(CommonConstant.CONFIG_PARAM_KEY_REPORT_PERSON,map.get(CommonConstant.CONFIG_PARAM_KEY_REPORT_PERSON),"报表报送人");
        }
        if(map.containsKey(CommonConstant.CONFIG_PARAM_KEY_REPORT_COPY_PERSON))
        {
            configService.saveOrUpdatePrintConfig(CommonConstant.CONFIG_PARAM_KEY_REPORT_COPY_PERSON,map.get(CommonConstant.CONFIG_PARAM_KEY_REPORT_COPY_PERSON),"报表抄送人");
        }
        if(map.containsKey(CommonConstant.CONFIG_PARAM_KEY_REPORT_TOTAL_PRINT_TYPE))
        {
            configService.saveOrUpdatePrintConfig(CommonConstant.CONFIG_PARAM_KEY_REPORT_TOTAL_PRINT_TYPE,map.get(CommonConstant.CONFIG_PARAM_KEY_REPORT_TOTAL_PRINT_TYPE),"统计总表打印排版");
        }

        return new ResponseBean<>(true);
    }

    @ApiOperation(value = "根据key查询所有系统配置")
    @ApiImplicitParam(name = "key", value = "系统配置信息表对象", paramType = "path", required = true, dataType = "String")
    @GetMapping("feign/findByKey/{key}")
    public ResponseBean<Config> feignFindByKey(@PathVariable String key, @RequestParam @NotBlank String tenantCode) {
        Config config = configService.findByKey(key, tenantCode);
        return new ResponseBean<>(config);
    }

    @ApiOperation(value = "更新系统配置信息表信息", notes = "根据系统配置信息表ID更新系统配置信息表信息")
    @Log("修改系统配置信息表")
    @PutMapping("feign")
    public ResponseBean<Boolean> feignUpdateConfig(@RequestBody @Valid ConfigDto configDto) {
        Config config = new Config();
        BeanUtils.copyProperties(configDto, config);
        config.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(configService.update(config) > 0);
    }

}
