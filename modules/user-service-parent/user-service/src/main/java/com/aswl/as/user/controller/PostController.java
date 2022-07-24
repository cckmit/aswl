package com.aswl.as.user.controller;

import com.aswl.as.common.core.constant.CommonConstant;
import com.aswl.as.common.core.enums.BusinessType;
import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.utils.PageUtil;
import com.aswl.as.common.core.utils.SysUtil;
import com.aswl.as.common.core.web.BaseController;
import com.aswl.as.common.log.annotation.Log;
import com.aswl.as.user.api.module.Post;
import com.aswl.as.user.service.PostService;
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
import java.util.List;

/**
 * @version 1.0.0
 * @Author ke
 * @create 2019/9/17 14:15
 */
@Slf4j
@AllArgsConstructor
@Api(value = "/api/v1/post", tags = "岗位管理")
@RestController
@RequestMapping("/v1/post")
public class PostController extends BaseController {
    private final PostService postService;

    /**
     * 根据ID获取岗位
     * @param postId
     * @return
     */
    @GetMapping(value = "/{postId}")
    @ApiOperation(value = "根据岗位ID查询岗位")
    public ResponseBean<Post> findById(@PathVariable("postId") String postId) {
        Post post = new Post();
        post.setId(postId);
        return new ResponseBean<>(postService.get(post));
    }

    /**
     * 查询所有岗位
     * @param post
     * @return
     */
    @GetMapping(value = "posts")
    @ApiOperation(value = "查询所有岗位列表")
    public ResponseBean<List<Post>> findAll(Post post) {
        post.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>(postService.findAllList(post));
    }

    @GetMapping("postList")
    @ApiOperation(value = "分页查询岗位列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = "post", value = "职位信息", dataType = "post")
    })
    public ResponseBean<PageInfo<Post>> postList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                           @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                           @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                           @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
                                           Post post) {
        post.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>(postService.findPage(PageUtil.pageInfo(pageNum, pageSize, sort, order), post));
    }

    @PostMapping
    @ApiOperation(value = "创建岗位", notes = "创建岗位")
    @Log(value = "新增岗位",businessType = BusinessType.INSERT)
    public ResponseBean<Boolean> insertpost(@RequestBody @Valid Post post) {
        post.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(postService.insert(post) > 0);
    }

    @PutMapping
    @ApiOperation(value = "更新岗位信息", notes = "根据岗位ID更新职位信息")
    @Log(value = "修改岗位",businessType = BusinessType.UPDATE)
    public ResponseBean<Boolean> updatepost(@RequestBody @Valid Post post) {
        post.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(postService.update(post) > 0);
    }

    @DeleteMapping(value = "/{postId}")
    @ApiOperation(value = "删除岗位信息",notes = "根据职位ID删除岗位信息")
    @Log(value = "删除岗位",businessType = BusinessType.DELETE)
    public ResponseBean<Boolean> deletepost(@PathVariable("postId") String postId) {
        Post post = new Post();
        post.setId(postId);
        post.setNewRecord(false);
        post.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(postService.delete(post) > 0);
    }

    @PostMapping("deleteAll")
    @ApiOperation(value = "批量删除岗位", notes = "根据岗位ID批量删除岗位")
    @ApiImplicitParam(name = "post", value = "职位信息", dataType = "post")
    @Log(value = "批量删除岗位",businessType = BusinessType.DELETE)
    public ResponseBean<Boolean> deleteAllposts(@RequestBody Post post) {
        boolean success = false;
        try {
            if (StringUtils.isNotEmpty(post.getIdString()))
                success = postService.deleteAll(post.getIdString().split(",")) > 0;
        } catch (Exception e) {
            log.error("删除岗位失败！", e);
        }
        return new ResponseBean<>(success);
    }
}
