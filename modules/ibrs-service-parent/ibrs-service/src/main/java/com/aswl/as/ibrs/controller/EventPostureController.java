package com.aswl.as.ibrs.controller;
import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.web.BaseController;
import com.aswl.as.common.log.annotation.Log;
import com.aswl.as.ibrs.api.dto.EventPostureDto;
import com.aswl.as.ibrs.api.module.EventPosture;
import com.aswl.as.ibrs.service.EventPostureService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import com.aswl.as.common.core.utils.SysUtil;
import com.aswl.as.common.core.constant.CommonConstant;
import com.github.pagehelper.PageInfo;
import javax.validation.Valid;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import com.aswl.as.common.core.utils.PageUtil;

/**
*  设备事件-姿态信息controller
*
* @author  zgl
* @date  2022-07-15 16:36
*/
@Slf4j
@AllArgsConstructor
@Api(value = "/api/v1/eventPosture",tags="设备事件-姿态信息" )
@RestController
@RequestMapping("/v1/eventPosture")
public class EventPostureController extends BaseController {

private final EventPostureService eventPostureService;

/**
* 根据ID获取设备事件-姿态信息
* @param id
* @return ResponseBean
*/
@ApiOperation(value = "根据设备事件-姿态信息ID查询设备事件-姿态信息")
@ApiImplicitParams(@ApiImplicitParam(name = "id", value = "设备事件-姿态信息ID",  paramType = "path", required = true, dataType = "String"))
@GetMapping(value = "/{id}")
public ResponseBean<EventPosture> findById(@PathVariable("id") String id) {
EventPosture eventPosture = new EventPosture();
eventPosture.setId(id);
return new ResponseBean<>(eventPostureService.get(eventPosture));
}

/**
* 查询所有设备事件-姿态信息
* @param eventPosture
* @return ResponseBean
*/

@ApiOperation(value = "查询所有设备事件-姿态信息列表")
@ApiImplicitParams(@ApiImplicitParam(name = "eventPosture", value = "设备事件-姿态信息对象",  paramType = "path", required = true, dataType = "eventPosture"))
@GetMapping(value = "eventPostures")
public ResponseBean
<List<EventPosture>> findAll(EventPosture eventPosture) {
eventPosture.setTenantCode(SysUtil.getTenantCode());
return new ResponseBean<>(eventPostureService.findAllList(eventPosture));
}
/**
* 分页查询设备事件-姿态信息列表
* @param pageNum
* @param pageSize
* @param sort
* @param order
* @param eventPosture
* @return PageInfo
*/
@ApiOperation(value = "分页查询设备事件-姿态信息列表")
@ApiImplicitParams({
@ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT,paramType = "query", dataType = "String"),
@ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT,paramType = "query", dataType = "String"),
@ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT,paramType = "query", dataType = "String"),
@ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT,paramType = "query", dataType = "String"),
@ApiImplicitParam(name = "eventPosture", value = "设备事件-姿态信息信息", dataType = "eventPosture")
})

@GetMapping("eventPostureList")
public PageInfo<EventPosture> eventPostureList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
@RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
@RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
@RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
EventPosture eventPosture) {
eventPosture.setTenantCode(SysUtil.getTenantCode());
return eventPostureService.findPage(PageUtil.pageInfo(pageNum, pageSize, sort, order), eventPosture);
}

/**
* 新增设备事件-姿态信息
* @param eventPostureDto
* @return ResponseBean
*/
@ApiOperation(value = "新增设备事件-姿态信息", notes = "新增设备事件-姿态信息")
@ApiImplicitParams(@ApiImplicitParam(name = "eventPostureDto", value = "设备dto", required = true, paramType = "body", dataType = "eventPostureDto"))
@PostMapping
@Log("新增设备事件-姿态信息")
public ResponseBean
<Boolean> insertEventPosture(@RequestBody @Valid EventPostureDto eventPostureDto) {
    EventPosture eventPosture=new EventPosture();
    BeanUtils.copyProperties(eventPostureDto,eventPosture);
    eventPosture.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
    return new ResponseBean<>(eventPostureService.insert(eventPosture) > 0);
    }
    /**
    * 修改设备事件-姿态信息
    * @param eventPostureDto
    * @return ResponseBean
    */

    @ApiOperation(value = "更新设备事件-姿态信息信息", notes = "根据设备事件-姿态信息ID更新职位信息")
    @ApiImplicitParams(@ApiImplicitParam(name = "eventPostureDto", value = "设备dto", required = true, paramType = "body", dataType = "eventPostureDto"))
    @Log("修改设备事件-姿态信息")
    @PutMapping
    public ResponseBean
    <Boolean> updateEventPosture(@RequestBody @Valid EventPostureDto eventPostureDto) {
        EventPosture eventPosture=new EventPosture();
        BeanUtils.copyProperties(eventPostureDto,eventPosture);
        eventPosture.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(eventPostureService.update(eventPosture) > 0);
        }

        /**
        * 根据设备事件-姿态信息ID删除设备事件-姿态信息信息
        * @param id
        * @return ResponseBean
        */
        @ApiOperation(value = "删除设备事件-姿态信息信息",notes = "根据设备事件-姿态信息ID删除设备事件-姿态信息信息")
        @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "设备事件-姿态信息ID",  paramType = "path", required = true, dataType = "String"))
        @DeleteMapping(value = "/{id}")
        public ResponseBean
        <Boolean> deleteEventPostureById(@PathVariable("id") String id) {
            EventPosture eventPosture = new EventPosture();
            eventPosture.setId(id);
            eventPosture.setNewRecord(false);
            eventPosture.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
            return new ResponseBean<>(eventPostureService.delete(eventPosture) > 0);
            }
            /**
            * 批量删除设备事件-姿态信息信息
            * @param eventPosture
            * @return ResponseBean
            */

            @ApiOperation(value = "批量删除设备事件-姿态信息", notes = "根据设备事件-姿态信息ID批量删除设备事件-姿态信息")
            @ApiImplicitParam(name = "eventPosture", value = "设备事件-姿态信息信息", dataType = "eventPosture")
            @Log("批量删除设备事件-姿态信息")
            @PostMapping("deleteAll")
            public ResponseBean
            <Boolean> deleteAllEventPosture(@RequestBody EventPosture eventPosture) {
                boolean success = false;
                try {
                if (StringUtils.isNotEmpty(eventPosture.getIdString()))
                success = eventPostureService.deleteAll(eventPosture.getIdString().split(",")) > 0;
                } catch (Exception e) {
                log.error("删除设备事件-姿态信息失败！", e);
                }
                return new ResponseBean<>(success);
                }
                }
