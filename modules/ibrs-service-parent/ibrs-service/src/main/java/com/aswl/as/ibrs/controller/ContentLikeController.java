package com.aswl.as.ibrs.controller;

import com.aswl.as.common.core.constant.CommonConstant;
import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.utils.PageUtil;
import com.aswl.as.common.core.utils.SysUtil;
import com.aswl.as.common.core.web.BaseController;
import com.aswl.as.common.log.annotation.Log;
import com.aswl.as.ibrs.api.dto.ContentLikeDto;
import com.aswl.as.ibrs.api.module.*;
import com.aswl.as.ibrs.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import com.github.pagehelper.PageInfo;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

/**
 * 用户点赞表controller
 *
 * @author hfx
 * @date 2020-03-16 10:26
 */
@Slf4j
@AllArgsConstructor
@Api(value = "/api/v1/contentLike", tags = "用户点赞表")
@RestController
@RequestMapping("/v1/contentLike")
public class ContentLikeController extends BaseController {

    private final ContentLikeService contentLikeService;

    @Autowired
    private ContentProductService contentProductService;

    @Autowired
    private ContentIndustryService contentIndustryService;

    @Autowired
    private ContentMalfunctionService contentMalfunctionService;

    @Autowired
    private ContentBannerService contentBannerService;


    /**
     * 根据ID获取用户点赞表
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "根据用户点赞表ID查询用户点赞表")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "用户点赞表ID", paramType = "path", required = true, dataType = "String"))
    @GetMapping(value = "/{id}")
    public ResponseBean<ContentLike> findById(@PathVariable("id") String id) {
        ContentLike contentLike = new ContentLike();
        contentLike.setId(id);
        return new ResponseBean<>(contentLikeService.get(contentLike));
    }

    /**
     * 查询所有用户点赞表
     *
     * @param contentLike
     * @return ResponseBean
     */

    @ApiOperation(value = "查询所有用户点赞表列表")
    @ApiImplicitParam(name = "contentLike", value = "用户点赞表对象", paramType = "path", required = true, dataType = "contentLike")
    @GetMapping(value = "contentLikes")
    public ResponseBean
            <List<ContentLike>> findAll(ContentLike contentLike) {
        contentLike.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>(contentLikeService.findList(contentLike));
    }

    /**
     * 分页查询用户点赞表列表
     *
     * @param pageNum
     * @param pageSize
     * @param sort
     * @param order
     * @param contentLike
     * @return PageInfo
     */
    @ApiOperation(value = "分页查询用户点赞表列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "contentLike", value = "用户点赞表信息", dataType = "contentLike")
    })

    @GetMapping("contentLikeList")
    public ResponseBean<PageInfo<ContentLike>> contentLikeList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                                               @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                                               @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                                               @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
                                                               ContentLike contentLike) {
        contentLike.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>(contentLikeService.findPage(PageUtil.pageInfo(pageNum, pageSize, sort, order), contentLike));
    }

    /**
     * 新增用户点赞表
     *
     * @param contentLikeDto
     * @return ResponseBean
     */
    @ApiOperation(value = "新增用户点赞表", notes = "新增用户点赞表")
    @PostMapping
    @Log("新增用户点赞表")
    public ResponseBean
            <Boolean> insertContentLike(@RequestBody @Valid ContentLikeDto contentLikeDto) {
        ContentLike contentLike = new ContentLike();
        BeanUtils.copyProperties(contentLikeDto, contentLike);
        contentLike.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());

        // 点赞数+1
        //1为产品中心的分类，2-常见故障的分类，3-行业资讯的分类
        if(contentLikeDto.getCategory() == 1)
        {
            //产品中心
            contentProductService.addLikes(contentLikeDto.getContentId(),1);
        }
        if(contentLikeDto.getCategory() == 2)
        {
            //常见故障
            contentMalfunctionService.addLikes(contentLikeDto.getContentId(),1);
        }
        if(contentLikeDto.getCategory() == 3)
        {
            //行业资讯
            contentIndustryService.addLikes(contentLikeDto.getContentId(),1);
        }
        if (contentLikeDto.getCategory() == 4)
        {
            //banner的链接为外网id的情况
            contentBannerService.addLikes(contentLikeDto.getContentId(),1);
        }

        return new ResponseBean<>(contentLikeService.insert(contentLike) > 0);
    }

    /**
     * 修改用户点赞表
     *
     * @param contentLikeDto
     * @return ResponseBean
     */

    @ApiOperation(value = "更新用户点赞表信息", notes = "根据用户点赞表ID更新用户点赞表信息")
    @Log("修改用户点赞表")
    @PutMapping
    public ResponseBean
            <Boolean> updateContentLike(@RequestBody @Valid ContentLikeDto contentLikeDto) {
        ContentLike contentLike = new ContentLike();
        BeanUtils.copyProperties(contentLikeDto, contentLike);
        contentLike.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(contentLikeService.update(contentLike) > 0);
    }

    /**
     * 根据用户点赞表ID删除用户点赞表信息
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "删除用户点赞表信息", notes = "根据用户点赞表ID删除用户点赞表信息")
    @ApiImplicitParam(name = "id", value = "用户点赞表ID", paramType = "path", required = true, dataType = "String")
    @DeleteMapping(value = "/{id}")
    public ResponseBean
            <Boolean> deleteContentLikeById(@PathVariable("id") String id) {
        ContentLike contentLike = new ContentLike();
        contentLike.setId(id);
        contentLike.setNewRecord(false);
        contentLike.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(contentLikeService.delete(contentLike) > 0);
    }

    /**
     * 批量删除用户点赞表信息
     *
     * @param contentLike
     * @return ResponseBean
     */
    @ApiOperation(value = "批量删除用户点赞表", notes = "根据用户点赞表ID批量删除用户点赞表")
    @ApiImplicitParam(name = "contentLike", value = "用户点赞表信息", dataType = "contentLike")
    @Log("批量删除用户点赞表")
    @PostMapping("deleteAll")
    public ResponseBean
            <Boolean> deleteAllContentLike(@RequestBody ContentLike contentLike) {
        boolean success = false;
        try {
            if (StringUtils.isNotEmpty(contentLike.getIdString()))
                success = contentLikeService.deleteAll(contentLike.getIdString().split(",")) > 0;
        } catch (Exception e) {
            log.error("删除用户点赞表失败！", e);
        }
        return new ResponseBean<>(success);
    }

    // 根据传递过来的文章id和分类来判断是否该用户有没有点赞
    /**
     * 查询所有用户点赞表
     *
     * @param category
     * @param contentId
     * @return ResponseBean
     */
    @ApiOperation(value = "查询所有用户点赞表列表")
    @ApiImplicitParam(name = "contentData", value = "用户点赞表对象", paramType = "path", required = true, dataType = "contentLike")
    @GetMapping(value = "contentData/{category}/{contentId}")
    public ResponseBean<Map<String,Object>> hasLike(@PathVariable("category") Integer category,@PathVariable("contentId") String contentId) {
        ContentLike contentLike=new ContentLike();

        Map<String,Object> result=new HashMap<String,Object>();
        result.put("clicks",0);
        result.put("likes",0);
        result.put("hasLike",false);

        if(category==null || contentId==null)
        {
            return new ResponseBean<>(result);
        }

        contentLike.setCategory(category);
        contentLike.setContentId(contentId);
        contentLike.setCreator(SysUtil.getUser());

        try {

            //1为产品中心的分类，2-常见故障的分类，3-行业资讯的分类
            if(category == 1)
            {
                //产品中心
                contentProductService.addClicks(contentId); // 查看数+1
                ContentProduct p=new ContentProduct();
                p.setId(contentId);
                p=contentProductService.get(p);
                if(p!=null)
                {
                    result.put("clicks",p.getClicks());
                    result.put("likes",p.getLikes());
                }
            }
            if(category == 2)
            {
                //常见故障
                contentMalfunctionService.addClicks(contentId);
                ContentMalfunction m=new ContentMalfunction();
                m.setId(contentId);
                m =contentMalfunctionService.get(m);
                if(m!=null)
                {
                    result.put("clicks",m.getClicks());
                    result.put("likes",m.getLikes());
                }
            }
            if(category == 3)
            {
                //行业资讯
                contentIndustryService.addClicks(contentId);
                ContentIndustry i=new ContentIndustry();
                i.setId(contentId);
                i =contentIndustryService.get(i);
                if(i!=null)
                {
                    result.put("clicks",i.getClicks());
                    result.put("likes",i.getLikes());
                }
            }
            if(category == 4)
            {
                //行业资讯
                contentBannerService.addClicks(contentId);
                ContentBanner b=new ContentBanner();
                b.setId(contentId);
                b =contentBannerService.get(b);
                if(b!=null)
                {
                    result.put("clicks",b.getClicks());
                    result.put("likes",b.getLikes());
                }
            }


        }
        catch (Exception e)
        {
            return new ResponseBean<>(e);
        }

        result.put("hasLike",contentLikeService.findList(contentLike).size()>0);

        return new ResponseBean<>(result);
    }


    // 根据传递过来的文章id和分类来判断是否该用户有没有点赞
    /**
     * 查询所有用户点赞表(这个没有分类)
     *
     * @param contentId
     * @return ResponseBean
     */
    @ApiOperation(value = "查询所有用户点赞表列表")
    @ApiImplicitParam(name = "contentData", value = "用户点赞表对象", paramType = "path", required = true, dataType = "contentLike")
    @GetMapping(value = "contentDataBak/{contentId}")
    public ResponseBean<Map<String,Object>> contentDataBak(@PathVariable("contentId") String contentId) {
        ContentLike contentLike=new ContentLike();

        Map<String,Object> result=new HashMap<String,Object>();
        result.put("clicks",0);
        result.put("likes",0);
        result.put("hasLike",false);

        if(contentId==null)
        {
            return new ResponseBean<>(result);
        }

        //1为产品中心的分类，2-常见故障的分类，3-行业资讯的分类
        Integer category=null;

        try {

            //产品中心
            ContentProduct p=new ContentProduct();
            p.setId(contentId);
            p=contentProductService.get(p);
            if(p!=null)
            {
                result.put("clicks",p.getClicks());
                result.put("likes",p.getLikes());
                contentProductService.addClicks(contentId); // 查看数+1
                category=1;
            }

            //常见故障
            ContentMalfunction m=new ContentMalfunction();
            m.setId(contentId);
            m =contentMalfunctionService.get(m);
            if(m!=null)
            {
                result.put("clicks",m.getClicks());
                result.put("likes",m.getLikes());
                contentMalfunctionService.addClicks(contentId);
                category=2;
            }

            //行业资讯
            ContentIndustry i=new ContentIndustry();
            i.setId(contentId);
            i =contentIndustryService.get(i);
            if(i!=null)
            {
                result.put("clicks",i.getClicks());
                result.put("likes",i.getLikes());
                contentIndustryService.addClicks(contentId);
                category=3;
            }

            //banner
            ContentBanner b=new ContentBanner();
            b.setId(contentId);
            b =contentBannerService.get(b);
            if(b!=null)
            {
                result.put("clicks",b.getClicks());
                result.put("likes",b.getLikes());
                contentBannerService.addClicks(contentId);
                category=4;
            }

        }
        catch (Exception e)
        {
            return new ResponseBean<>(e);
        }

        if(category!=null)
        {
            contentLike.setCategory(category);
        }
        contentLike.setContentId(contentId);
        contentLike.setCreator(SysUtil.getUser());
        result.put("hasLike",contentLikeService.findList(contentLike).size()>0);

        return new ResponseBean<>(result);
    }

    /**
     * 取消点赞接口
     *
     * @param category
     * @param contentId
     * @return ResponseBean
     */
    @ApiOperation(value = "查询所有用户点赞表列表")
    @ApiImplicitParam(name = "deleteLike", value = "用户点赞表对象", paramType = "path", required = true, dataType = "contentLike")
    @GetMapping(value = "deleteLike/{category}/{contentId}")
    public ResponseBean<Boolean> deleteLike(@PathVariable("category") Integer category,@PathVariable("contentId") String contentId)
    {
        ContentLike contentLike=new ContentLike();
        contentLike.setContentId(contentId);
        contentLike.setCreator(SysUtil.getUser());
//        System.out.println("删除了like");
        List<ContentLike> list=contentLikeService.findList(contentLike);
        if(list!=null&&list.size()>0)
        {
            contentLike=list.get(0);
            if(category == 1)
            {
                //产品中心
                contentProductService.addLikes(contentId,-1);
            }
            if(category == 2)
            {
                //常见故障
                contentMalfunctionService.addLikes(contentId,-1);
            }
            if(category == 3)
            {
                //行业资讯
                contentIndustryService.addLikes(contentId,-1);
            }
            if(category == 4)
            {
                //banner
                contentBannerService.addLikes(contentId,-1);
            }

            return new ResponseBean<>(contentLikeService.delete(contentLike)>0);
        }

        return new ResponseBean<>(true);
    }


}
