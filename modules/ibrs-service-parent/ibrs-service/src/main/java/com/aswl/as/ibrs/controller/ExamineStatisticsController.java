package com.aswl.as.ibrs.controller;


import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.ibrs.api.dto.PeriodScopeDto;
import com.aswl.as.ibrs.api.module.ExamineStandard;
import com.aswl.as.ibrs.api.vo.ExamineStatisticsVo;
import com.aswl.as.ibrs.service.ExamineBaseLibService;
import com.aswl.as.ibrs.service.ExamineStatisticsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * 考核统计
 */

@Slf4j
@AllArgsConstructor
@Api(value = "/api/v1/examineStatistics", tags = "考核统计")
@RestController
@RequestMapping("/v1/examineStatistics")
public class ExamineStatisticsController {

    private final ExamineStatisticsService examineStatisticsService;

    /**
     * 根据考核标准Id和年度统计运维
     * @param standardId
     * @return ResponseBean
     * @Param year
     */
    @ApiOperation(value = "根据考核标准Id和年度统计运维")
    @ApiImplicitParams({@ApiImplicitParam(name = "standardId", value = "考核标准ID", paramType = "query", required = true, dataType = "String"),
            @ApiImplicitParam(name = "year", value = "年度", paramType = "query", required = true, dataType = "String"),
            @ApiImplicitParam(name = "regionCode", value = "区域", paramType = "query", required = true, dataType = "String")})
    @GetMapping(value = "examineSummary")
    public ResponseBean<List<Map<String, PeriodScopeDto>>> examineSummary(@RequestParam("standardId") String standardId, @RequestParam("year") String year ,@RequestParam("regionCode") String regionCode) throws ParseException {
        return new ResponseBean<>(examineStatisticsService.examineSummary(standardId,year,regionCode));
    }


    /**
     * 导出考核统计表
     */
    @ApiOperation(value = "导出考核统计表")
    @PostMapping(value = "export")
    public ResponseEntity<byte[]> export(@RequestBody List<Map<String, PeriodScopeDto>> list, HttpServletResponse response,String periodId,String standardId) throws UnsupportedEncodingException {
        return examineStatisticsService.export(list,response,periodId,standardId);
    }


}
