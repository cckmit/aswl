package com.aswl.as.asos.modules.ibrs.controller;
import com.aswl.as.asos.common.utils.*;
import com.aswl.as.asos.modules.ibrs.entity.AsDeviceQrcode;
import com.aswl.as.asos.modules.ibrs.entity.AsQrcodeBatchDto;
import com.aswl.as.asos.modules.ibrs.service.IAsDeviceQrcodeService;
import com.aswl.as.common.core.utils.IdGen;
import com.aswl.as.common.core.utils.OsinfoUtil;
import com.google.zxing.WriterException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import com.aswl.as.asos.common.validator.ValidatorUtils;
import com.aswl.as.asos.common.annotation.SysLog;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

import com.aswl.as.asos.modules.ibrs.entity.AsQrcodeBatch;
import com.aswl.as.asos.modules.ibrs.service.IAsQrcodeBatchService;
import org.springframework.web.bind.annotation.RestController;
import com.aswl.as.asos.modules.sys.controller.AbstractController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 二维码批次表 前端控制器
 * </p>
 *
 * @author df
 * @since 2020-12-01
 */
@RestController
@RequestMapping("/ibrs/as-qrcode-batch")
@Api("二维码批次表")
public class AsQrcodeBatchController extends AbstractController {

    @Autowired
    IAsQrcodeBatchService iAsQrcodeBatchService;
    @Autowired
    IAsDeviceQrcodeService iAsDeviceQrcodeService;

    @Value(value = "${qrcode.url}")
    private String qrCodeUrl;


    /**
     * 二维码批次表分页列表
     */
    @GetMapping("/list")
    @ApiOperation("二维码批次表分页列表")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = iAsQrcodeBatchService.queryPage(params);
        return R.ok().put("page", page);
    }

    /**
     * 二维码批次表列表
     */
    @PostMapping("/findList")
    @ApiOperation("二维码批次表列表")
    public R findList(@RequestBody AsQrcodeBatch entity) {
        List<AsQrcodeBatch> list = iAsQrcodeBatchService.findList(entity);
        return R.ok().put("list", list);
    }

    /**
     * 二维码批次表信息
     */
    @GetMapping("/info/{entityId}")
    @RequiresPermissions("os:role:info")
    @ApiOperation("二维码批次表信息")
    public R info(@PathVariable("entityId") String entityId) {

        AsQrcodeBatch data = iAsQrcodeBatchService.getEntityById(entityId);
        return R.ok().put("data", data);
    }

    /**
     * 保存
     */
    @SysLog("保存二维码批次表")
    @PostMapping("/save")
    @ApiOperation("保存二维码批次表")
    public R save(@RequestBody AsQrcodeBatchDto entity) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
            AsQrcodeBatch asQrcodeBatch = new AsQrcodeBatch();
            BeanUtils.copyProperties(entity, asQrcodeBatch);
            // 设置默认logo
            if (StringUtils.isBlank(entity.getLogo())) {
                asQrcodeBatch.setLogo("qrcode/aswl.png");
            }
            asQrcodeBatch.setCreateDate(new Date());
            String dateTime = format.format(new Date());
            String zipUrl = "";
            if (StringUtils.isNotBlank(entity.getProjectName())) {
                zipUrl = entity.getProjectName() + "_" + entity.getQrcodeStartSn() + "-" + entity.getQrcodeEndSn() + ".zip";
            }else{
                zipUrl = entity.getQrcodeStartSn() + "-" + entity.getQrcodeEndSn() + ".zip";
            }
            asQrcodeBatch.setZipUrl(zipUrl);
            iAsQrcodeBatchService.saveEntity(asQrcodeBatch);
            // 生成二维码
            generateCode(entity, asQrcodeBatch.getId(), dateTime);
            return R.ok();
        }catch (RuntimeException e){
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    /**
     * 更新
     */
    @SysLog("更新二维码批次表")
    @PostMapping("/update")
    //@RequiresPermissions("os:role:update")
    @ApiOperation("更新二维码批次表")
    public R update(@RequestBody AsQrcodeBatch entity) {
        //数据校验
        String vStr = verifyForm(entity);
        if (vStr != null) {
            return R.error(vStr);
        }

        iAsQrcodeBatchService.updateEntityById(entity);

        return R.ok();
    }

    /**
     * 删除二维码批次表
     */
    @SysLog("删除二维码批次表")
    @PostMapping("/delete")
    @ApiOperation("删除二维码批次表")
    public R delete(@RequestBody AsQrcodeBatchDto asQrcodeBatchDto) {
        String path="";
        String [] ids =asQrcodeBatchDto.getIds();
        String zipName=asQrcodeBatchDto.getZipname().substring(0,asQrcodeBatchDto.getZipname().indexOf("."));
        iAsQrcodeBatchService.deleteEntityByIds(ids);
        //删除设备二维码表数据
        iAsDeviceQrcodeService.deleteByBathId(Arrays.toString(ids));
        //删除目录下图片
        if (OsinfoUtil.isWindows()) {
            path= OsGlobalData.IBRS_WIN_UPLOAD_PATH + "//" +zipName ;
        }else{
            path= OsGlobalData.IBRS_LINUX_UPLOAD_PATH + "//" +zipName;
        }
        File file = new File(path);
        File zipFile = new File(path+".zip");
        FileUtils.deleteFolder(file);
        FileUtils.deleteFolder(zipFile);
        return R.ok();
    }


    private String verifyForm(AsQrcodeBatch e) {

        try {
            //表单校验
            ValidatorUtils.validateEntity(e);
        } catch (Exception tempException) {
            return tempException.getMessage();
        }

        return null;
    }

    /**
     * 生成二维码
     */
    public void generateCode(AsQrcodeBatchDto entity,String bathId,String dateTime) {
        String realPath ="";
        String path="";
        if (OsinfoUtil.isWindows()) {
            realPath= OsGlobalData.IBRS_WIN_UPLOAD_PATH + "//";
        }else{
            realPath= OsGlobalData.IBRS_LINUX_UPLOAD_PATH + "//";
        }
        String projectName = entity.getProjectName();
        if (StringUtils.isNotBlank(projectName)) {
            path = realPath + projectName + "_" + entity.getQrcodeStartSn() + "-" + entity.getQrcodeEndSn();
        }else{
            path = realPath  + entity.getQrcodeStartSn() + "-" + entity.getQrcodeEndSn();
        }
        String qrCode = "";
        String deviceQrCode ="";
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        try {
            String source = "";
            if (StringUtils.isBlank(entity.getLogo())){
               // source =realPath + "qrcode/aswl.png";
                source= null;
            }else{
                source= realPath + entity.getLogo();
            }
            String codeSns [] =entity.getQrcodeSns().split(",");
            String datePrefix = dateTime;
            for (int i = 0; i < codeSns.length; i++) {
//                if (StringUtils.isNotBlank(entity.getProjectName())) {
//                    qrCode = path + "/" + entity.getProjectName() + "-" + entity.getProduceDate() + "-" + String.format("%04d", (entity.getQrCodeStartNum() + i)) + ".png";
//                }else{
//                    qrCode = path + "/" + codeSns[i] + ".png";
//                }
                qrCode = path + "/" + codeSns[i] + ".png";
//                deviceQrCode = datePrefix + "00" + (i+1);
                deviceQrCode = datePrefix + String.format("%05d", (entity.getQrCodeStartNum() + i));
                String parm="?qrcode=" +deviceQrCode;
                String url=qrCodeUrl +parm;
                try {
                    QrCodeUtils.generateQRImage(url, qrCode, source);//生成二维码的方法
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // 保存设备二维码数据
                AsDeviceQrcode  asDeviceQrcode = new AsDeviceQrcode();
                asDeviceQrcode.setId(IdGen.snowflakeId());
                asDeviceQrcode.setQrCode(deviceQrCode);
                asDeviceQrcode.setFactoryCode(codeSns[i]);
                asDeviceQrcode.setIsUsed(0);
                asDeviceQrcode.setVisitTime(0);
                asDeviceQrcode.setQrcodeBathId(bathId);
                iAsDeviceQrcodeService.saveEntity(asDeviceQrcode);
            }
        } catch (WriterException e) {
            e.printStackTrace();
        }
        try {
            ZipHelper.zipCompress(path, path + ".zip");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/download")
    @ApiOperation("下载打包二维码Zip包")
    public ResponseEntity<byte[]> download(String fileName) throws IOException {
        String realPath ="";
        if (OsinfoUtil.isWindows()) {
            realPath= OsGlobalData.IBRS_WIN_UPLOAD_PATH + "//";
        }else{
            realPath= OsGlobalData.IBRS_LINUX_UPLOAD_PATH + "//";
        }
        String path = realPath + fileName + "";
        File file = new File(path);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment",  new String(fileName.getBytes("UTF-8"), "iso-8859-1"));
        return new ResponseEntity<byte[]>(org.apache.commons.io.FileUtils.readFileToByteArray(file), headers, HttpStatus.CREATED);
    }
}
