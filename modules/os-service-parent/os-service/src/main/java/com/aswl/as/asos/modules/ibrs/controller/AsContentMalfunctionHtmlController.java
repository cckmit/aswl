package com.aswl.as.asos.modules.ibrs.controller;


import com.aswl.as.asos.common.annotation.SysLog;
import com.aswl.as.asos.common.utils.PageUtils;
import com.aswl.as.asos.common.utils.R;
import com.aswl.as.asos.common.validator.ValidatorUtils;
import com.aswl.as.asos.modules.ibrs.entity.AsContentMalfunction;
import com.aswl.as.asos.modules.ibrs.service.IAsContentMalfunctionService;
import com.aswl.as.asos.modules.sys.controller.AbstractController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 常见故障表 前端控制器
 * </p>
 *
 * @author hfx
 * @since 2020-03-04
 */
@RestController

@RequestMapping("/contentMalfunction")
@Api("常见故障表")
public class AsContentMalfunctionHtmlController extends AbstractController {

    @Autowired
    IAsContentMalfunctionService iAsContentMalfunctionService;

    /**
    * 常见故障表信息
    */
    @GetMapping(value = "/html/{entityId}",produces = {"text/plain;charset=utf-8","text/html;charset=utf-8"})
    @ResponseBody
    public String info(@PathVariable("entityId") String entityId){
        AsContentMalfunction data = iAsContentMalfunctionService.getEntityById(entityId);
        return data.getContent();
    }

}
