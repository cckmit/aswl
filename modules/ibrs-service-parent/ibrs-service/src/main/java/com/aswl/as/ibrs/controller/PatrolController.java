package com.aswl.as.ibrs.controller;

import com.aswl.as.common.core.constant.CommonConstant;
import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.utils.PageUtil;
import com.aswl.as.ibrs.api.dto.EventPatrolDto;
import com.aswl.as.ibrs.api.vo.EventPatrolVo;
import com.aswl.as.ibrs.service.EventPatrolService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@AllArgsConstructor
@Api(value = "/api/v1/patrol", tags = "设备巡更")
@RestController
@RequestMapping("/v1/patrol")
public class PatrolController {

    private final EventPatrolService eventPatrolService;

    /**
     * 分页查询巡更列表
     * @return
     */
    @GetMapping("patrolList")
    @ApiOperation(value = "分页查询巡更列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "EventPatrolDto", value = "设备巡更信息", dataType = "EventPatrolDto")
    })
    public ResponseBean<PageInfo<EventPatrolVo>> patrolList(@RequestParam(value = CommonConstant.PAGE_NUM,required = false,defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                               @RequestParam(value = CommonConstant.PAGE_SIZE,required = false,defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                                EventPatrolDto eventPatrolDto){

        return new ResponseBean<>(eventPatrolService.findByPage(pageNum,pageSize,eventPatrolDto));
    }
}
