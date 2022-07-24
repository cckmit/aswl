package com.aswl.as.ibrs.controller;

import com.aswl.as.common.core.constant.CommonConstant;
import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.ibrs.api.dto.EventPatrolDto;
import com.aswl.as.ibrs.api.vo.PatrolHisNoRecordVo;
import com.aswl.as.ibrs.service.PatrolHisNoRecordService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 未巡更controller
 */
@Slf4j
@AllArgsConstructor
@Api(value = "/api/v1/patrolNoRecord", tags = "未巡更controller")
@RestController
@RequestMapping("/v1/patrolNoRecord")
public class PatrolHisNoRecordController {
    private final PatrolHisNoRecordService patrolHisNoRecordService;

    /**
     * 分页未巡更列表
     * @param pageNum
     * @param pageSize
     * @param eventPatrolDto
     * @return
     */
    @GetMapping("patrolNoRecordList")
    @ApiOperation(value = "分页未巡更列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "EventPatrolDto", value = "设备未巡更信息", dataType = "EventPatrolDto")
    })
    public ResponseBean<PageInfo<PatrolHisNoRecordVo>> patrolNoRecordList(@RequestParam(value = CommonConstant.PAGE_NUM,required = false,defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                                                          @RequestParam(value = CommonConstant.PAGE_SIZE,required = false,defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                                                          EventPatrolDto eventPatrolDto){
        return patrolHisNoRecordService.findByPage(pageNum,pageSize,eventPatrolDto);
    }
}
