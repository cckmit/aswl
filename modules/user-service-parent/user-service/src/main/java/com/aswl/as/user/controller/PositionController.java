package com.aswl.as.user.controller;

import cn.hutool.core.collection.CollUtil;
import com.aswl.as.common.core.constant.CommonConstant;
import com.aswl.as.common.core.enums.BusinessType;
import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.utils.PageUtil;
import com.aswl.as.common.core.utils.SysUtil;
import com.aswl.as.common.core.utils.TreeUtil;
import com.aswl.as.common.core.web.BaseController;
import com.aswl.as.common.log.annotation.Log;
import com.aswl.as.user.api.dto.PositionDto;
import com.aswl.as.user.api.dto.PositionInfoDto;
import com.aswl.as.user.api.module.Position;
import com.aswl.as.user.api.vo.PositionVo;
import com.aswl.as.user.service.PositionService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @version 1.0.0
 * @Author ke
 * @create 2019/9/17 14:14
 */
@Slf4j
@AllArgsConstructor
@Api(value = "/api/v1/position", tags = "职位管理")
@RestController
@RequestMapping("/v1/position")
public class PositionController extends BaseController {
    private final PositionService positionService;

    /**
     * 根据ID获取职位
     *
     * @param positionId
     * @return
     */
    @GetMapping(value = "/{positionId}")
    @ApiOperation(value = "根据职位ID查询职位")
    public ResponseBean<Position> findById(@PathVariable("positionId") String positionId) {
        Position position = new Position();
        position.setId(positionId);
         return new ResponseBean<>(positionService.get(position));
    }

    @GetMapping("positionInfoList")
    @ApiOperation(value = "分页查询职位列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = "position", value = "职位信息", dataType = "Position")
    })
    public ResponseBean<PageInfo<PositionVo>> positionList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                             @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                             @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                             @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order, PositionInfoDto positionInfoDto) {
        positionInfoDto.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>(positionService.findPage(PageUtil.pageInfo(pageNum, pageSize, sort, order),positionInfoDto));
    }

    @GetMapping(value = "positions")
    @ApiOperation(value = "查询所有职位树列表")
    public ResponseBean<List<PositionDto>> findAll() {
        Position position = new Position();
        position.setTenantCode(SysUtil.getTenantCode());
        Stream<Position> postStream = positionService.findList(position).stream();
        if (Optional.ofNullable(postStream).isPresent()) {
            List<PositionDto> postTreeList = postStream.map(PositionDto::new).collect(Collectors.toList());
            return new ResponseBean<>(TreeUtil.buildTree(CollUtil.sort(postTreeList, Comparator.comparingInt(PositionDto::getSort)),"-1"));
        }
        return new ResponseBean<>(new ArrayList<>());
    }

    /**
     * 新增职位
     * @param position
     * @return
     */
    @PostMapping
    @ApiOperation(value = "创建职位", notes = "创建职位")
    @Log(value = "新增职位",businessType = BusinessType.INSERT)
    public ResponseBean<Boolean> insertPosition(@RequestBody @Valid Position position) {
        position.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(positionService.insert(position) > 0);
    }

    /**
     * 修改职位
     * @param position
     * @return
     */
    @PutMapping
    @ApiOperation(value = "更新职位信息", notes = "根据职位ID更新职位信息")
    @Log(value = "修改职位",businessType = BusinessType.UPDATE)
    public ResponseBean<Boolean> updatePosition(@RequestBody @Valid Position position) {
        position.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(positionService.update(position) > 0);
    }

    @DeleteMapping(value = "/{positionId}")
    @ApiOperation(value = "删除职位信息",notes = "根据职位ID删除职位信息")
    @Log(value = "删除职位",businessType = BusinessType.DELETE)
    public ResponseBean<Boolean> deleteposition(@PathVariable("positionId") String positionId) {
        Position position = new Position();
        position.setId(positionId);
        position.setNewRecord(false);
        position.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(positionService.delete(position) > 0);
    }

    @PostMapping("deleteAll")
    @ApiOperation(value = "批量删除职位", notes = "根据职位ID批量删除职位")
    @ApiImplicitParam(name = "position", value = "职位信息", dataType = "position")
    @Log(value = "批量删除职位",businessType = BusinessType.DELETE)
    public ResponseBean<Boolean> deleteAllpositions(@RequestBody Position position) {
        boolean success = false;
        try {
            if (StringUtils.isNotEmpty(position.getIdString()))
                success = positionService.deleteAll(position.getIdString().split(",")) > 0;
        } catch (Exception e) {
            log.error("删除职位失败！", e);
        }
        return new ResponseBean<>(success);
    }
}
