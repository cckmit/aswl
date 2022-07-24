package com.aswl.as.ibrs.controller;

import com.aswl.as.common.core.constant.CommonConstant;
import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.utils.PageUtil;
import com.aswl.as.common.core.utils.SysUtil;
import com.aswl.as.common.core.web.BaseController;
import com.aswl.as.common.log.annotation.Log;
import com.aswl.as.ibrs.api.dto.RatingStandardDto;
import com.aswl.as.ibrs.api.module.RatingStandard;
import com.aswl.as.ibrs.service.RatingStandardService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import com.github.pagehelper.PageInfo;
import javax.validation.Valid;
import java.util.List;
import org.apache.commons.lang.StringUtils;


/**
 * 告警评级设置controller
 *
 * @author hzj
 * @date 2021/01/11 16:03
 */
@Slf4j
@AllArgsConstructor
@Api(value = "/api/v1/ratingStandard", tags = "告警评级设置")
@RestController
@RequestMapping("/v1/ratingStandard")
public class RatingStandardController extends BaseController {

    private final RatingStandardService ratingStandardService;

    /**
     * 根据ID获取告警评级设置
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "根据告警评级设置ID查询告警评级设置")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "告警评级设置ID", paramType = "path", required = true, dataType = "String"))
    @GetMapping(value = "/{id}")
    public ResponseBean<RatingStandard> findById(@PathVariable("id") String id) {
        RatingStandard ratingStandard = new RatingStandard();
        ratingStandard.setId(id);
        return new ResponseBean<>(ratingStandardService.get(ratingStandard));
    }

    /**
     * 查询所有告警评级设置
     *
     * @param ratingStandard
     * @return ResponseBean
     */

    @ApiOperation(value = "查询所有告警评级设置列表")
    @ApiImplicitParam(name = "ratingStandard", value = "告警评级设置对象", paramType = "path", required = true, dataType = "ratingStandard")
    @GetMapping(value = "ratingStandards")
    public ResponseBean
            <List<RatingStandard>> findAll(RatingStandard ratingStandard) {
        ratingStandard.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>(ratingStandardService.findList(ratingStandard));
    }

    /**
     * 分页查询告警评级设置列表
     *
     * @param pageNum
     * @param pageSize
     * @param sort
     * @param order
     * @param ratingStandard
     * @return PageInfo
     */
    @ApiOperation(value = "分页查询告警评级设置列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "ratingStandard", value = "告警评级设置信息", dataType = "ratingStandard")
    })

    @GetMapping("ratingStandardList")
    public ResponseBean
            <PageInfo<RatingStandard>> ratingStandardList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                                          @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                                          @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                                          @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
                                                          RatingStandard ratingStandard) {
        ratingStandard.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>(ratingStandardService.findPage(PageUtil.pageInfo(pageNum, pageSize, sort, order), ratingStandard));
    }

    /**
     * 新增告警评级设置
     *
     * @param ratingStandardDto
     * @return ResponseBean
     */
    @ApiOperation(value = "新增告警评级设置", notes = "新增告警评级设置")
    @PostMapping
    @Log("新增告警评级设置")
    public ResponseBean
            <Boolean> insertRatingStandard(@RequestBody @Valid RatingStandardDto ratingStandardDto) {
        RatingStandard ratingStandard = new RatingStandard();
        BeanUtils.copyProperties(ratingStandardDto, ratingStandard);
        ratingStandard.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(ratingStandardService.insert(ratingStandard) > 0);
    }

    /**
     * 修改告警评级设置
     *
     * @param ratingStandardDtos
     * @return ResponseBean
     */

    @ApiOperation(value = "更新告警评级设置信息", notes = "根据告警评级设置ID更新告警评级设置信息")
    @Log("修改告警评级设置")
    @PutMapping
    public ResponseBean
            <Boolean> updateRatingStandard(@RequestBody @Valid List<RatingStandardDto> ratingStandardDtos) {
//        RatingStandard ratingStandard = new RatingStandard();
//        BeanUtils.copyProperties(ratingStandardDto, ratingStandard);
//        ratingStandard.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(ratingStandardService.updateRating(ratingStandardDtos));
    }

    /**
     * 根据告警评级设置ID删除告警评级设置信息
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "删除告警评级设置信息", notes = "根据告警评级设置ID删除告警评级设置信息")
    @ApiImplicitParam(name = "id", value = "告警评级设置ID", paramType = "path", required = true, dataType =
            "String")
    @DeleteMapping(value = "/{id}")
    public ResponseBean
            <Boolean> deleteRatingStandardById(@PathVariable("id") String id) {
        RatingStandard ratingStandard = new RatingStandard();
        ratingStandard.setId(id);
        ratingStandard.setNewRecord(false);
        ratingStandard.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(ratingStandardService.delete(ratingStandard) > 0);
    }

    /**
     * 批量删除告警评级设置信息
     *
     * @param ratingStandard
     * @return ResponseBean
     */

    @ApiOperation(value = "批量删除告警评级设置", notes = "根据告警评级设置ID批量删除告警评级设置")
    @ApiImplicitParam(name = "ratingStandard", value = "告警评级设置信息", dataType = "ratingStandard")
    @Log("批量删除告警评级设置")
    @PostMapping("deleteAll")
    public ResponseBean
            <Boolean> deleteAllRatingStandard(@RequestBody RatingStandard ratingStandard) {
        boolean success = false;
        try {
            if (StringUtils.isNotEmpty(ratingStandard.getIdString()))
                success = ratingStandardService.deleteAll(ratingStandard.getIdString().split(",")) > 0;
        } catch (Exception e) {
            log.error("删除告警评级设置失败！", e);
        }
        return new ResponseBean<>(success);
    }


}
