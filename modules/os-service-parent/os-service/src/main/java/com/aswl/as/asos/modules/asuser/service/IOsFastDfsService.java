package com.aswl.as.asos.modules.asuser.service;

import com.aswl.as.asos.common.utils.PageUtils;
import com.aswl.as.asos.modules.asuser.entity.SysTenant;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.tobato.fastdfs.domain.fdfs.MetaData;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * SAAS租户信息表 服务类
 * </p>
 *
 * @author hfx
 * @since 2019-11-18
 */
public interface IOsFastDfsService {

    /**
     * 上传文件
     *
     * @param inputStream 输入流
     * @param size        文件大小
     * @param extName     扩展名
     * @return 完整路径
     * @author aswl.com
     * @date 2018/1/5 12:02
     */
    public String uploadFile(InputStream inputStream, long size, String extName);

    /**
     * 上传文件
     *
     * @param inputStream
     * @param size        文件大小
     * @param extName     扩展名
     * @param metaDataSet 元数据集
     * @return 完整路径
     * @author aswl.com
     * @date 2018/1/5 12:02
     */
    public String uploadFile(InputStream inputStream, long size, String extName, Set<MetaData> metaDataSet);

    /**
     * 支持断点续传，适合上传大文件，需要指定组名
     *
     * @param groupName   组名
     * @param inputStream 附件输入流
     * @param size        附件大小
     * @param extName     附件扩展名
     * @return 完整路径
     * @author aswl.com
     * @date 2018/3/8 15:51
     */
    public String uploadAppenderFile(String groupName, InputStream inputStream, long size, String extName);

    /**
     * 续传文件
     *
     * @param groupName   组名，如group1
     * @param path        路径名，M00/00/04/wKgAUFpO84CAA4HvAAAABs4Fkco168.txt
     * @param inputStream 输入流
     * @param size        附件大小
     * @return 是否续传成功
     * @author aswl.com
     * @date 2018/3/8 15:53
     */
    public boolean appendFile(String groupName, String path, InputStream inputStream, long size);

    /**
     * 下载文件
     *
     * @param groupName 组名，如：group1
     * @param path      路径名，如：M00/00/04/wKgAUFpO84CAA4HvAAAABs4Fkco168.txt
     * @return 字节数组
     * @author aswl.com
     * @date 2018/1/5 11:59
     */
    public byte[] downloadFile(String groupName, String path);

    /**
     * 下载附件，并保存到指定的文件，适合下载大文件
     *
     * @param groupName 组名，如：group1
     * @param path      路径名，如：M00/00/04/wKgAUFpO84CAA4HvAAAABs4Fkco168.txt
     * @param filePath  文件存放的路径，如：C:\attach\1.rar
     * @return 文件存放的路径
     * @author aswl.com
     * @date 2018/3/9 10:10
     */
    public String downloadFile(String groupName, String path, String filePath);

    /**
     * 下载文件，返回流
     *
     * @param groupName 组名，如：group1
     * @param path      路径名，如：M00/00/04/wKgAUFpO84CAA4HvAAAABs4Fkco168.txt
     * @return 附件输入流
     * @author aswl.com
     * @date 2018/1/5 12:00
     */
    public InputStream downloadStream(String groupName, String path);

    /**
     * 删除文件
     *
     * @param groupName 组名，如：group1
     * @param path      路径名，如：M00/00/04/wKgAUFpO84CAA4HvAAAABs4Fkco168.txt
     * @author aswl.com
     * @date 2018/1/5 12:01
     */
    public void deleteFile(String groupName, String path);

    /**
     * 修改文件
     *
     * @param groupName   组名，如：group1
     * @param oldPath     旧的路径
     * @param inputStream 附件输入流
     * @param size        附件大小
     * @param extName     扩展名
     * @return 完整路径
     * @author aswl.com
     * @date 2018/1/5 12:01
     */
    public String modify(String groupName, String oldPath, InputStream inputStream, long size, String extName);

    /**
     * 获取一个组
     *
     * @return 组名
     * @author aswl.com
     * @date 2018/3/9 10:43
     */
    public String getGroup();

    /**
     * 获取所有组
     *
     * @return 可用的组列表
     * @author aswl.com
     * @date 2018/3/9 10:42
     */
    public List<String> listGroups();


}
