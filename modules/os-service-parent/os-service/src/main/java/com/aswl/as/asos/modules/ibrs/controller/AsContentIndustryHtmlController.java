package com.aswl.as.asos.modules.ibrs.controller;


import com.aswl.as.asos.common.annotation.SysLog;
import com.aswl.as.asos.common.utils.PageUtils;
import com.aswl.as.asos.common.utils.R;
import com.aswl.as.asos.common.validator.ValidatorUtils;
import com.aswl.as.asos.modules.ibrs.entity.AsContentIndustry;
import com.aswl.as.asos.modules.ibrs.service.IAsContentIndustryService;
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
 * 行业资讯表 前端控制器
 * </p>
 *
 * @author hfx
 * @since 2020-03-04
 */
@RestController
@RequestMapping("/contentIndustry")
@Api("行业资讯表")
public class AsContentIndustryHtmlController extends AbstractController {

    @Autowired
    IAsContentIndustryService iAsContentIndustryService;

    /**
    * 行业资讯表信息
    */
    @GetMapping(value = "/html/{entityId}",produces = {"text/plain;charset=utf-8","text/html;charset=utf-8"})
    @ResponseBody
    public String info(@PathVariable("entityId") String entityId){
        AsContentIndustry data = iAsContentIndustryService.getEntityById(entityId);
        return data.getContent();
    }

}
