package com.aswl.as.ibrs.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import com.aswl.as.ibrs.api.vo.RegionLeaderVo;
import com.aswl.as.user.api.feign.UserServiceClient;
import com.aswl.as.user.api.module.User;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aswl.as.common.core.constant.CommonConstant;
import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.utils.PageUtil;
import com.aswl.as.common.core.utils.SysUtil;
import com.aswl.as.common.core.web.BaseController;
import com.aswl.as.common.log.annotation.Log;
import com.aswl.as.ibrs.api.dto.RegionLeaderDto;
import com.aswl.as.ibrs.api.module.RegionLeader;
import com.aswl.as.ibrs.service.RegionLeaderService;
import com.github.pagehelper.PageInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 区域负责人controller
 *
 * @author dingfei
 * @date 2019-11-08 10:20
 */
@Slf4j
@AllArgsConstructor
@Api(value = "/api/v1/regionLeader", tags = "区域负责人")
@RestController
@RequestMapping("/v1/regionLeader")
public class RegionLeaderController extends BaseController {

    private final RegionLeaderService regionLeaderService;

    /**
     * 根据ID获取区域负责人
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "根据区域负责人ID查询区域负责人")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "区域负责人ID", paramType = "path", required = true, dataType = "String"))
    @GetMapping(value = "/{id}")
    public ResponseBean<RegionLeader> findById(@PathVariable("id") String id) {
        RegionLeader regionLeader = new RegionLeader();
        regionLeader.setId(id);
        return new ResponseBean<>(regionLeaderService.get(regionLeader));
    }

    /**
     * 查询所有区域负责人
     *
     * @param regionLeader
     * @return ResponseBean
     */

    @ApiOperation(value = "查询所有区域负责人列表")
    @ApiImplicitParams(@ApiImplicitParam(name = "regionLeader", value = "区域负责人对象", paramType = "path", required = true, dataType = "regionLeader"))
    @GetMapping(value = "regionLeaders")
    public ResponseBean
            <List<RegionLeader>> findAll(RegionLeader regionLeader) {
        regionLeader.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>(regionLeaderService.findAllList(regionLeader));
    }

    /**
     * 分页查询区域负责人列表
     *
     * @param pageNum
     * @param pageSize
     * @param regionLeaderDto
     * @return PageInfo
     */
    @ApiOperation(value = "分页查询区域负责人列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "regionLeader", value = "区域负责人信息", dataType = "regionLeader")
    })

    @GetMapping("regionLeaderList")
    public ResponseBean<PageInfo<RegionLeaderVo>> regionLeaderList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                                                   @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                                                   RegionLeaderDto regionLeaderDto) {
        regionLeaderDto.setTenantCode(SysUtil.getTenantCode());
        regionLeaderDto.setProjectId(SysUtil.getProjectId());
        return new ResponseBean<>(regionLeaderService.findPage(PageUtil.pageInfo(pageNum, pageSize, "", ""), regionLeaderDto));
    }

    /**
     * 新增区域负责人
     *
     * @param regionLeaderDto
     * @return ResponseBean
     */
    @ApiOperation(value = "新增区域负责人", notes = "新增区域负责人")
    @ApiImplicitParams(@ApiImplicitParam(name = "regionLeaderDto", value = "设备dto", required = true, paramType = "body", dataType = "regionLeaderDto"))
    @PostMapping
    @Log("新增区域负责人")
    public ResponseBean
            <Boolean> insertRegionLeader(@RequestBody @Valid RegionLeaderDto regionLeaderDto) {
        return new ResponseBean<>(regionLeaderService.insert(regionLeaderDto) > 0);
    }

    /**
     * 修改区域负责人
     *
     * @param regionLeaderDto
     * @return ResponseBean
     */

    @ApiOperation(value = "更新区域负责人信息", notes = "根据区域负责人ID更新职位信息")
    @ApiImplicitParams(@ApiImplicitParam(name = "regionLeaderDto", value = "设备dto", required = true, paramType = "body", dataType = "regionLeaderDto"))
    @Log("修改区域负责人")
    @PutMapping
    public ResponseBean
            <Boolean> updateRegionLeader(@RequestBody @Valid RegionLeaderDto regionLeaderDto) {
        return new ResponseBean<>(regionLeaderService.update(regionLeaderDto) > 0);
    }

    /**
     * 根据区域负责人ID删除区域负责人信息
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "删除区域负责人信息", notes = "根据区域负责人ID删除区域负责人信息")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "区域负责人ID", paramType = "path", required = true, dataType = "String"))
    @DeleteMapping(value = "/{id}")
    public ResponseBean
            <Boolean> deleteRegionLeaderById(@PathVariable("id") String id) {
        RegionLeader regionLeader = new RegionLeader();
        regionLeader.setId(id);
        regionLeader.setNewRecord(false);
        regionLeader.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(regionLeaderService.delete(regionLeader) > 0);
    }

    /**
     * 批量删除区域负责人信息
     *
     * @param regionLeader
     * @return ResponseBean
     */

    @ApiOperation(value = "批量删除区域负责人", notes = "根据区域负责人ID批量删除区域负责人")
    @ApiImplicitParam(name = "regionLeader", value = "区域负责人信息", dataType = "regionLeader")
    @Log("批量删除区域负责人")
    @PostMapping("deleteAll")
    public ResponseBean
            <Boolean> deleteAllRegionLeader(@RequestBody RegionLeader regionLeader) {
        boolean success = false;
        try {
            if (StringUtils.isNotEmpty(regionLeader.getIdString())) ;
            success = regionLeaderService.deleteAll(regionLeader.getIdString().split(",")) > 0;
        } catch (Exception e) {
            log.error("删除区域负责人失败！", e);
        }
        return new ResponseBean<>(success);
    }

    /**
     * 根据区域ID查询所有改区域的维护人员
     *
     * @param regionCode 区域ID
     * @return RegionLeader 维护人员列表
     */
    @ApiOperation(value = "查询所有区域负责人列表")
    @ApiImplicitParams(@ApiImplicitParam(name = "regionCode", value = "区域ID", required = true, dataType = "String"))
    @GetMapping(value = "getRegionLeaderByRegionId/{regionCode}")
    public List<RegionLeader> getRegionLeaderByRegionId(@PathVariable("regionCode") String regionCode) {
        RegionLeader regionLeader = new RegionLeader();
        regionLeader.setRegionCode(regionCode);
        return regionLeaderService.findAllList(regionLeader);
    }

    /**
     * 根据设备的区域编码查询设备的区域负责人列表
     *
     * @param regionCode
     * @return
     */
    @ApiOperation(value = "根据设备的区域编码查询设备的区域负责人列表")
    @ApiImplicitParams(@ApiImplicitParam(name = "regionCode", value = "区域编码",  required = true, dataType = "String"))
    @GetMapping(value = "getRegionLeaderByRegionCode")
    public ResponseBean<List<RegionLeader>> findRegionLeaderByRegionCode(@RequestParam(value = "regionCode",defaultValue = "") String regionCode ,@RequestParam(value = "queryProjectId",required = false) String queryProjectId) {
        RegionLeader regionLeader = new RegionLeader();

        // 需要根据租户和项目过滤
        regionLeader.setTenantCode(SysUtil.getTenantCode());
        regionLeader.setProjectId(SysUtil.getProjectId());
        if (queryProjectId != null){
            regionLeader.setProjectId(queryProjectId);
        }
       

        List<RegionLeader> regionLeaders = regionLeaderService.findAllList(regionLeader);
        if (regionCode != null && !"".equals(regionCode)) {

            List<RegionLeader> list = new ArrayList<>();
            if (regionLeaders != null && regionLeaders.size() > 0) {
                for (RegionLeader leader : regionLeaders) {
                    if (leader.getRegionCode().length() <= regionCode.length() && regionCode.indexOf(leader.getRegionCode()) == 0) {
                        list.add(leader);
                    }
                }
            }
            return new ResponseBean<>(list);
        } else {
            return new ResponseBean<>(regionLeaders);
        }

    }

    /**
     * 根据巡更Id获取区域负责人列表
     *
     * @param patrolKeyId
     * @return
     */
    @ApiOperation(value = "根据巡更Id获取区域负责人列表")
    @ApiImplicitParams(@ApiImplicitParam(name = "patrolKeyId", value = "巡更Id",  required = true, dataType = "String"))
    @GetMapping(value = "getRegionLeaderByPatrolKeyId/{patrolKeyId}")
    public ResponseBean<List<RegionLeader>> getRegionLeaderByPatrolKeyId(@PathVariable("patrolKeyId") String patrolKeyId) {
        RegionLeader regionLeader = new RegionLeader();
        // 需要根据租户和项目过滤
        regionLeader.setTenantCode(SysUtil.getTenantCode());
        regionLeader.setProjectId(SysUtil.getProjectId());
        regionLeader.setPatrolKeyId(patrolKeyId);
        return new ResponseBean<>(regionLeaderService.findAllList(regionLeader));
    }

    /**
     * 根据用户id批量查询区域负责人信息
     *
     * @param userList
     * @return list
     */
    @ApiOperation(value = "根据用户id批量查询区域负责人信息")
    @PostMapping(value = "getRegionLeaderByUserId")
    public ResponseBean<List<RegionLeader>> getRegionLeaderByUserId(@RequestBody List<User> userList) {
        return new ResponseBean<>(regionLeaderService.getRegionLeaderByUserIds(userList));
    }

    /**
     * 根据用户id更新区域负责人信息
     *
     * @param regionLeaderDto 
     * @return boolean
     */
    @ApiOperation(value = "根据用户id更新区域负责人信息")
    @PostMapping(value = "updateByUserId")
    public ResponseBean<Boolean> updateByUserId(@RequestBody @Valid RegionLeaderDto regionLeaderDto) {
        return new ResponseBean<>(regionLeaderService.updateByUserId(regionLeaderDto) > 0);
    }

    /**
     * 根据用户id批量删除区域负责人信息
     *
     * @param ids
     * @return list
     */
    @ApiOperation(value = "根据用户id批量查询区域负责人信息")
    @PostMapping(value = "deleteByUserId")
    public ResponseBean<Boolean> deleteByUserId(@RequestBody String[] ids) {
        return new ResponseBean<>(regionLeaderService.deleteByUserId(ids) > 0);
    }

    /**
     * 根据用户ID查询区域负责人
     *
     * @param userId
     * @return
     */
    @ApiOperation(value = "根据用户ID查询区域负责人")
    @ApiImplicitParams(@ApiImplicitParam(name = "userId", value = "用户ID",  required = true, dataType = "String"))
    @GetMapping(value = "getRegionLeaderByUserId")
    public ResponseBean<RegionLeader> getRegionLeaderByUserId(@RequestParam("userId") String userId) {
        return new ResponseBean<>(regionLeaderService.findRegionLeaderByUserId(userId));
    }

    /**
     * 根据区域编码查询区域负责人是否存在
     *
     * @param regionCode
     * @return list
     */
    @ApiOperation(value = "根据区域编码查询区域负责人是否存在")
    @GetMapping(value = "isExistLeader")
    public ResponseBean<Boolean> getIsExistLeader(@RequestParam("regionCode") String regionCode) {
        return new ResponseBean<>(regionLeaderService.getIsExistLeader(regionCode));
    }
}
