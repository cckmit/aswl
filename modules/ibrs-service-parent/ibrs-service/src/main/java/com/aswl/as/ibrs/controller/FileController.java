package com.aswl.as.ibrs.controller;
import com.alibaba.fastjson.JSON;
import com.aswl.as.common.core.enums.BusinessKeyEnum;
import com.aswl.as.common.core.exceptions.CommonException;
import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.utils.IdGen;
import com.aswl.as.common.core.utils.OsinfoUtil;
import com.aswl.as.common.core.utils.SysUtil;
import com.aswl.as.common.core.web.BaseController;
import com.aswl.as.ibrs.api.dto.FileDto;
import com.aswl.as.ibrs.api.module.Attachment;
import com.aswl.as.ibrs.service.AttachmentService;
import com.aswl.as.ibrs.service.FileService;
import com.google.common.base.Stopwatch;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author dingfei
 * @date 2019-12-02 16:03
 * @Version V1
 */
@Slf4j
@RestController
@Api(value = "/api/v1/sys/file", tags = "图片上传")
@RequestMapping("v1/sys/file")
public class FileController extends BaseController {

    @Autowired
    private FileService fileService;
    @Autowired
    private AttachmentService attachmentService;
    /**
     * windows上传路径
     */
    @Value(value = "${file.path.winUpload}")
    private String winUploadPath;

    /**
     * linux上传路径
     */
    @Value(value = "${linuxfile.path.linuxUpload}")
    private String linuxUploadPath;

    @ApiOperation(value = "上传文件")
    @PostMapping(value = "/upload")
    public ResponseBean upload(HttpServletRequest request, HttpServletResponse response) {
        ResponseBean result = new ResponseBean();
        String ctxPath = "";
        try {
            if (OsinfoUtil.isWindows()) {
                ctxPath = winUploadPath;
            } else {
                ctxPath = linuxUploadPath;
            }
            String fileName = null;
            String bizPath = "files";
            String nowday = new SimpleDateFormat("yyyyMMdd").format(new Date());
            File file = new File(ctxPath + File.separator + bizPath + File.separator + nowday);
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
            String dbpath = bizPath + File.separator + nowday + File.separator + fileName;
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

    @ApiOperation(value = "上传文件")
    @PostMapping(value = "/upload1")
    public ResponseBean upload1(
                                @RequestParam("projectId")String projectId,
                                HttpServletRequest request, HttpServletResponse response) {
        Stopwatch watch = Stopwatch.createStarted();
        ResponseBean result = new ResponseBean();
        String ctxPath = "";
        try {
            if (OsinfoUtil.isWindows()) {
                ctxPath = winUploadPath;
            } else {
                ctxPath = linuxUploadPath;
            }
            String fileName = null;
            String bizPath = "files";
            String nowday = new SimpleDateFormat("yyyyMMdd").format(new Date());
            File file = new File(ctxPath + File.separator + bizPath + File.separator + nowday);
            if (!file.exists()) {
                file.mkdirs();// 创建文件根目录
            }
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            MultipartFile mf = multipartRequest.getFile("file");// 获取上传文件对象
            String orgName = mf.getOriginalFilename();// 获取文件名
            String extName = orgName.substring(orgName.indexOf(".") + 1).toLowerCase().trim(); //获取文件后缀名
            String name = orgName.substring(0, orgName.indexOf(".")); //获取除后缀的文件名
            long fileSize = mf.getSize(); //获取文件大小
            String fileSizeStr = byteFormat(fileSize, true); //文件大小转化单位
            fileName = orgName.substring(0, orgName.lastIndexOf(".")) + "_" + System.currentTimeMillis() + orgName.substring(orgName.indexOf("."));
            String savePath = file.getPath() + File.separator + fileName;
            File savefile = new File(savePath);
            FileCopyUtils.copy(mf.getBytes(), savefile);
            String dbpath = bizPath + File.separator + nowday + File.separator + fileName;
            if (dbpath.contains("\\")) {
                dbpath = dbpath.replace("\\", "/");
            }
            // TODO 封装数据存入数据库
            FileDto fileDto = new FileDto();
            fileDto.setFileName(name);
            fileDto.setFileSize(fileSizeStr);
            fileDto.setProjectId(projectId);
            fileDto.setPath(savePath);
            fileDto.setUrl(dbpath);
            fileDto.setUserName(SysUtil.getUser());
            com.aswl.as.ibrs.api.module.File dbFile = new com.aswl.as.ibrs.api.module.File();
            BeanUtils.copyProperties(fileDto, dbFile);
            int flag = fileService.insert(dbFile);
            if (flag > 0) {
                result.setData(dbpath);
                result.setMsg("success");
                result.setCode(200);
                result.setStatus(200);
            } else {
                result.setData(null);
                result.setMsg("false");
                result.setCode(400);
                result.setStatus(400);
            }
        } catch (IOException e) {
            result.setData(null);
            result.setMsg("false");
            result.setMsg(e.getMessage());
            result.setCode(400);
            result.setStatus(400);
            log.error(e.getMessage(), e);
        }
        watch.stop();
        log.info("Upload File Cost: {}ms",
                watch.elapsed(TimeUnit.MILLISECONDS));
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
        String localPath = "";
        String imgType = "";
        try {
            imgPath = imgPath.replace("..", "");
            if (imgPath.endsWith(",")) {
                imgPath = imgPath.substring(0, imgPath.length() - 1);
            }
            imgType = imgPath.substring(imgPath.lastIndexOf(".") + 1);
            if("jpg".equals(imgType)){
                response.setContentType("image/jpeg;charset=utf-8");
            }else if("bmp".equals(imgType)){
                response.setContentType("image/x-bmp;charset=utf-8");
            }else if(imgType != null && !"".equals(imgType)){
                response.setContentType("image/" + imgType + ";charset=utf-8");
            }
            if (OsinfoUtil.isWindows()) {
                localPath = winUploadPath;
            } else {
                localPath = linuxUploadPath;
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
     * 预览视频
     * @param request
     * @param response
     */
    @ApiOperation(value = "预览视频")
    @GetMapping(value = "/viewVideo/**")
    public void viewVideo(HttpServletRequest request, HttpServletResponse response) {
        // ISO-8859-1 ==> UTF-8 进行编码转换
        String fileUrl = extractPathFromPattern(request);
        String filePath = "";
        String fileName = "";
        String localPath = "";

        response.reset();
        //获取从那个字节开始读取文件
        String rangeString = request.getHeader("Range");
        RandomAccessFile targetFile = null;
        OutputStream outputStream = null;
        try {
            if (OsinfoUtil.isWindows()) {
                localPath = winUploadPath;
            } else {
                localPath = linuxUploadPath;
            }
            filePath = localPath + File.separator + fileUrl;
            fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
            //获取响应的输出流
            outputStream = response.getOutputStream();
            //视频资源
            File file = new File(filePath);
            if(file.exists()){
                targetFile = new RandomAccessFile(file, "r");
                long fileLength = targetFile.length();
                //播放
                if(rangeString != null){
                    long range = Long.valueOf(rangeString.substring(rangeString.indexOf("=") + 1, rangeString.indexOf("-")));
                    //设置内容类型
                    response.setHeader("Content-Type", "video/mp4");
                    //设置此次相应返回的数据长度
                    response.setHeader("Content-Length", String.valueOf(fileLength - range));
                    //设置此次相应返回的数据范围
                    response.setHeader("Content-Range", "bytes "+range+"-"+(fileLength-1)+"/"+fileLength);
                    //返回码需要为206，而不是200
//                    response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
                    //设定文件读取开始位置（以字节为单位）
                    targetFile.seek(range);
                }else {
                    response.setHeader("accept-ranges", "bytes");
//                    response.setHeader("ali-swift-global-savetime", "1595126079");
                    response.setContentType("video/mp4");
//                    response.setContentLength(fileLength);
                    response.setHeader("Content-Length", String.valueOf(fileLength));
//                    response.setHeader("Content-Range", "" + Integer.valueOf(data.length-1));
//                    response.setHeader("eagleid", "793f8d1515952240032886035e");
                    response.setHeader("Etag", "W/\"9767057-1323779115364\"");
//                    response.setHeader("etag", "59f4210c-4dc01");
//                    response.setHeader("server", "Tengine");
//                    response.setHeader("timing-allow-origin", "*");
//                    response.setHeader("via", "cache77.l2cn1837[76,304-0,H], cache77.l2cn1837[77,0], cache6.cn284[0,200-0,H], cache1.cn284[1,0]");
//                    response.setHeader("x-cache", "HIT TCP_MEM_HIT dirn:2:754516230");
//                    response.setHeader("X-Firefox-Spdy", "h2");
                }


                byte[] cache = new byte[1024 * 300];
                int flag;
                while ((flag = targetFile.read(cache))!=-1){
                    outputStream.write(cache, 0, flag);
                }
            }else {
                ResponseBean result = new ResponseBean();
                String message = "file:" + fileName + " not exists";
                result.setMsg("false");
                result.setCode(400);
                result.setStatus(400);
                result.setData(message);
                //解决编码问题
                response.setHeader("Content-Type","application/json");
                outputStream.write(JSON.toJSONString(result).getBytes(StandardCharsets.UTF_8));
            }

            outputStream.flush();
        } catch (FileNotFoundException e) {

        } catch (IOException e) {

        }finally {
            if(outputStream != null){
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(targetFile != null){
                try {
                    targetFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
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
        String localPath = "";
        try {
            filePath = filePath.replace("..", "");
            if (filePath.endsWith(",")) {
                filePath = filePath.substring(0, filePath.length() - 1);
            }
            if (OsinfoUtil.isWindows()) {
                localPath = winUploadPath;
            } else {
                localPath = linuxUploadPath;
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

    public ResponseEntity<byte[]> download(String filePath) throws Exception {

        //获取文件名
        String name = filePath.substring(filePath.lastIndexOf("/"));
        String extName = filePath.substring(filePath.indexOf(".") ).toLowerCase().trim(); //获取文件后缀名
        String realName = name.substring(1, name.lastIndexOf("_")) + extName;
        System.out.println(realName);
        HttpHeaders headers = new HttpHeaders();
        ResponseEntity<byte[]> entity = null;
        InputStream in = null;
        String localPath = "";
        try {
            filePath = filePath.replace("..", "");
            if (filePath.endsWith(",")) {
                filePath = filePath.substring(0, filePath.length() - 1);
            }
            if (OsinfoUtil.isWindows()) {
                localPath = winUploadPath;
            } else {
                localPath = linuxUploadPath;
            }
            String downloadFilePath = localPath + File.separator + filePath;
            File file = new File(downloadFilePath);
            if (file.exists()) {
                in = new BufferedInputStream(new FileInputStream(file));
                byte[] bytes = new byte[in.available()];
                in.read(bytes);

                headers.add("Content-Disposition", "attachment;filename=" + new String(realName.getBytes(), "iso-8859-1"));

                entity = new ResponseEntity<byte[]>(bytes, headers, HttpStatus.OK);
            }

        } catch (Exception e) {
            log.info("文件下载失败" + e.getMessage());
            // e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return entity;
    }

    /**
     * 删除上传文件
     * @param fileName
     * @param attachmentId
     * @return Boolean
     */
    @ApiOperation(value = "删除上传文件")
    @DeleteMapping(value = "/delete")
    public ResponseBean delete(@RequestParam("fileName") String fileName,
                               @RequestParam("attachmentId") String attachmentId) {
        ResponseBean result = new ResponseBean();
        // ISO-8859-1 ==> UTF-8 进行编码转换
        // String filePath = extractPathFromPattern(request);
        // String filePath = URLEncoder.encode(fileName, "UTF-8");
        String filePath = fileName;
        // 其余处理略
        String localPath = "";
        try {
            filePath = filePath.replace("..", "");
            if (filePath.endsWith(",")) {
                filePath = filePath.substring(0, filePath.length() - 1);
            }
            if (OsinfoUtil.isWindows()) {
                localPath = winUploadPath;
            } else {
                localPath = linuxUploadPath;
            }
            String downloadFilePath = localPath + File.separator + filePath;
            File file = new File(downloadFilePath);
            if (!file.exists()) {
                //删除文件失败
                log.error("删除文件{}失败", file);
                result.setData(false);
                result.setMsg("failure");
                result.setCode(400);
                result.setStatus(400);
            }
            boolean flag = file.delete();
            if (flag) {
                Attachment attachment = new Attachment();
                attachment.setId(attachmentId);
                int row = attachmentService.delete(attachment);
                if (row > 0) {
                    result.setData(true);
                    result.setMsg("success");
                    result.setCode(200);
                    result.setStatus(200);
                    log.info("删除文件成功{}", file);
                }
            } else {
                log.error("删除文件失败{}", filePath);
                result.setData(false);
                result.setMsg("failure");
                result.setCode(400);
                result.setStatus(400);
            }
        } catch (Exception e) {
            log.error("删除文件失败{}", filePath);
            result.setData(false);
            result.setMsg("failure");
            result.setCode(400);
            result.setStatus(400);
        }
        return result;
    }


    /**
     * 根据fileMd5查询文件是否存在
     * @param fileMd5
     * @return Boolean
     */
    @ApiOperation(value = "验证FileMd5")
    @GetMapping(value = "/checkFileMd5")
    public ResponseBean<Boolean> checkFileMd5(@RequestParam("fileMd5") String fileMd5) {
        return new ResponseBean<>(fileService.selectByFileMd5(fileMd5) != null);
    }
    
    @ApiOperation(value = "秒传文件")
    @PostMapping(value = "/attachmentFileLoad")
    public ResponseBean attachmentFileLoad(@RequestParam(value = "projectId",required = false)String projectId, @RequestParam("fileMd5") String fileMd5,
            @RequestParam("fileNames") String fileNames,@RequestParam(value = "businessKey",required = false) String businessKey, 
             HttpServletRequest request) throws  Exception {
        Stopwatch watch = Stopwatch.createStarted();
        String ctxPath;
        String bizPath = "files";
        String nowDay = new SimpleDateFormat("yyyyMMdd").format(new Date());
        if (!StringUtils.isEmpty(businessKey)) {
            if (BusinessKeyEnum.getValueByKey(businessKey) == null) {
                throw new CommonException("业务Key值不存在");
            }
        }
        if (OsinfoUtil.isWindows()) {
            ctxPath = winUploadPath;
        } else {
            ctxPath = linuxUploadPath;
        }
        ResponseBean result = new ResponseBean();
        if (fileService.selectByFileMd5(fileMd5) == null) {
            String fileName;
            File filePath = new File(ctxPath + File.separator + bizPath + File.separator + nowDay);
            if (!filePath.exists()) {
                filePath.mkdirs();// 创建文件根目录
            }
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            MultipartFile mf = multipartRequest.getFile("file");// 获取上传文件对象
            String orgName = mf.getOriginalFilename();// 获取文件名
            String name = orgName.substring(0, orgName.lastIndexOf(".")); //获取除后缀的文件名
            long fileSize = mf.getSize(); //获取文件大小
            String fileSizeStr = byteFormat(fileSize, true); //文件大小转化单位
            fileName = UUID.randomUUID() + "_" + System.currentTimeMillis() + orgName.substring(orgName.lastIndexOf("."));
            String savePath = filePath.getPath() + File.separator + fileName;
            File file = new File(savePath);
            FileCopyUtils.copy(mf.getBytes(), file);
            String dbPath = bizPath + File.separator + nowDay + File.separator + fileName;
            if (dbPath.contains("\\")) {
                dbPath = dbPath.replace("\\", "/");
            }
            // TODO 封装数据存入数据库
            FileDto fileDto = new FileDto();
            fileDto.setFileName(name);
            fileDto.setFileSize(fileSizeStr);
            fileDto.setFileMd5(fileMd5);
            fileDto.setProjectId(projectId);
            fileDto.setPath(savePath);
            fileDto.setUrl(dbPath);
            fileDto.setUserName(SysUtil.getUser());
            com.aswl.as.ibrs.api.module.File dbFile = new com.aswl.as.ibrs.api.module.File();
            BeanUtils.copyProperties(fileDto, dbFile);
            int flag = fileService.insert(dbFile, businessKey);
            if (flag > 0) {
                result.setData(dbPath);
                result.setMsg("success");
                result.setCode(200);
                result.setStatus(200);
            } else {
                result.setData(null);
                result.setMsg("false");
                result.setCode(400);
                result.setStatus(400);
            }
            watch.stop();
            log.info("Upload File Cost: {}ms",
                    watch.elapsed(TimeUnit.MILLISECONDS));
        } else {
            com.aswl.as.ibrs.api.module.File file = fileService.selectByFileMd5(fileMd5);
            Attachment attachment = new Attachment();
            attachment.setId(IdGen.snowflakeId());
            attachment.setFileName(fileNames);
            attachment.setFileId(file.getId());
            attachment.setProjectId(projectId);
            attachment.setExtend(file.getFileSize());
            attachment.setBusinessKey(businessKey);
            String url;
            if(file.getPath().contains("upload/"+bizPath)){
                url = file.getPath().substring(file.getPath().indexOf("upload/"+bizPath)+7);
            }else{
                url = file.getPath().substring(file.getPath().indexOf("upload\\"+bizPath)+7).replace("\\", "/");
            }
            attachment.setDownloadUrl(url);
            attachment.setCreator(SysUtil.getUser());
            attachment.setCreateDate(new Date());
            attachment.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
            if (attachmentService.insert(attachment) > 0) {
                result.setData(attachment.getDownloadUrl());
                result.setMsg("success");
                result.setCode(200);
                result.setStatus(200);
            } else {
                result.setData(null);
                result.setMsg("false");
                result.setCode(400);
                result.setStatus(400);
            }
        }
        return result;
           
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

    public static String byteFormat(long bytes, boolean si) {
        String[] units = new String[]{" B", " KB", " MB", " GB", " TB", " PB", " EB", " ZB", " YB"};
        int unit = 1024;
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        double pre = 0;
        if (bytes > 1024) {
            pre = bytes / Math.pow(unit, exp);
        } else {
            pre = (double) bytes / (double) unit;
        }
        if (si) {
            return String.format(Locale.ENGLISH, "%.1f%s", pre, units[(int) exp]);
        }
        return String.format(Locale.ENGLISH, "%.1f", pre);
    }
}
