package com.aswl.as.asos.modules.ibrs.controller;
import com.aswl.as.asos.common.utils.OsGlobalData;
import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.utils.OsinfoUtil;
import com.aswl.as.common.core.web.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author dingfei (因为没写到公共类，直接复制过来使用了，兼容ibrs的下载，暂时主要是日志)
 * @date 2019-12-02 16:03
 * @Version V1
 */
@Slf4j
@RestController
@Api(value = "/os/file", tags = "图片上传")
@RequestMapping("os/file")
public class OsFileController extends BaseController {
    @ApiOperation(value = "上传文件")
    @PostMapping(value = "/upload")
    public ResponseBean upload(HttpServletRequest request, HttpServletResponse response) {
        ResponseBean result = new ResponseBean();
        String ctxPath="";
        try {
            if (OsinfoUtil.isWindows()) {
                ctxPath= OsGlobalData.IBRS_WIN_UPLOAD_PATH;
            }else{
                ctxPath= OsGlobalData.IBRS_LINUX_UPLOAD_PATH;
            }
            String fileName = null;
            String bizPath = "qrcode";
           // String nowday = new SimpleDateFormat("yyyyMMdd").format(new Date());
            File file = new File(ctxPath + File.separator + bizPath + File.separator);
            if (!file.exists()) {
                file.mkdirs();// 创建文件根目录
            }
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            MultipartFile mf = multipartRequest.getFile("file");// 获取上传文件对象
            String orgName = mf.getOriginalFilename();// 获取文件名
            fileName = orgName.substring(0, orgName.lastIndexOf(".")) + "_" + System.currentTimeMillis() + orgName.substring(orgName.indexOf("."));
            String savePath = file.getPath() + File.separator + fileName;
            File savefile = new File(savePath);
            FileCopyUtils.copy(mf.getBytes(), savefile);
            String dbpath = bizPath + File.separator + fileName;
            if (dbpath.contains("\\")) {
                dbpath = dbpath.replace("\\", "/");
            }
            result.setData(dbpath);
            result.setMsg("success");
            result.setCode(200);
            result.setStatus(200);
        } catch (IOException e) {
            result.setData(null);
            result.setMsg("false");
            result.setMsg(e.getMessage());
            result.setCode(400);
            result.setStatus(400);
            log.error(e.getMessage(), e);
        }
        return result;
    }

    /**
     * 预览图片
     * @param request
     * @param response
     */
    @ApiOperation(value = "预览图片")
    @GetMapping(value = "/view/**")
    public void view(HttpServletRequest request, HttpServletResponse response) {
        // ISO-8859-1 ==> UTF-8 进行编码转换
        String imgPath = extractPathFromPattern(request);
        // 其余处理略
        InputStream inputStream = null;
        OutputStream outputStream = null;
        String localPath="";
        try {
            imgPath = imgPath.replace("..", "");
            if (imgPath.endsWith(",")) {
                imgPath = imgPath.substring(0, imgPath.length() - 1);
            }
            response.setContentType("image/jpeg;charset=utf-8");
            if (OsinfoUtil.isWindows()) {
                localPath= OsGlobalData.IBRS_WIN_UPLOAD_PATH;
            }else{
                localPath= OsGlobalData.IBRS_LINUX_UPLOAD_PATH;
            }
            String imgurl = localPath + File.separator + imgPath;
            inputStream = new BufferedInputStream(new FileInputStream(imgurl));
            outputStream = response.getOutputStream();
            byte[] buf = new byte[1024];
            int len;
            while ((len = inputStream.read(buf)) > 0) {
                outputStream.write(buf, 0, len);
            }
            response.flushBuffer();
        } catch (IOException e) {
            log.error("预览图片失败" + e.getMessage());
            // e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }

    }

    /**
     * 下载文件
     * @param request
     * @param response
     * @throws Exception
     */
    @GetMapping(value = "/download/**")
    public void download(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // ISO-8859-1 ==> UTF-8 进行编码转换
        String filePath = extractPathFromPattern(request);
        // 其余处理略
        InputStream inputStream = null;
        OutputStream outputStream = null;
        String localPath="";
        try {
            filePath = filePath.replace("..", "");
            if (filePath.endsWith(",")) {
                filePath = filePath.substring(0, filePath.length() - 1);
            }
            if (OsinfoUtil.isWindows()) {
                localPath= OsGlobalData.IBRS_WIN_UPLOAD_PATH;
            }else{
                localPath= OsGlobalData.IBRS_LINUX_UPLOAD_PATH;
            }
            String downloadFilePath = localPath + File.separator + filePath;
            File file = new File(downloadFilePath);
            if (file.exists()) {
                response.setContentType("application/force-download");// 设置强制下载不打开            
                response.addHeader("Content-Disposition", "attachment;fileName=" + new String(file.getName().getBytes("UTF-8"), "iso-8859-1"));
                inputStream = new BufferedInputStream(new FileInputStream(file));
                outputStream = response.getOutputStream();
                byte[] buf = new byte[1024];
                int len;
                while ((len = inputStream.read(buf)) > 0) {
                    outputStream.write(buf, 0, len);
                }
                response.flushBuffer();
            }

        } catch (Exception e) {
            log.info("文件下载失败" + e.getMessage());
            // e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    /**
     * @param modelAndView
     * @return
     * @功能：pdf预览Iframe
     */
    @RequestMapping("/pdf/pdfPreviewIframe")
    public ModelAndView pdfPreviewIframe(ModelAndView modelAndView) {
        modelAndView.setViewName("pdfPreviewIframe");
        return modelAndView;
    }

    /**
     * 把指定URL后的字符串全部截断当成参数
     * 这么做是为了防止URL中包含中文或者特殊字符（/等）时，匹配不了的问题
     *
     * @param request
     * @return
     */
    private static String extractPathFromPattern(final HttpServletRequest request) {
        String path = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
        String bestMatchPattern = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
        return new AntPathMatcher().extractPathWithinPattern(bestMatchPattern, path);
    }
}
