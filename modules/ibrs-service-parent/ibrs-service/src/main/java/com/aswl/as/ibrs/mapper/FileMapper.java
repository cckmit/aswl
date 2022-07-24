package com.aswl.as.ibrs.mapper;
import com.aswl.as.common.core.persistence.CrudMapper;
import org.apache.ibatis.annotations.Mapper;
import com.aswl.as.ibrs.api.module.File;
import org.apache.ibatis.annotations.Param;

/**
*
* 上传文件表Mapper
* @author com.aswl
* @date 2020-04-17 13:25
*/

@Mapper
public interface FileMapper extends CrudMapper<File> {

    /**
     * 根据fileMd5查询
     * @param fileMd5
     * @return
     */
    File selectByFileMd5(@Param("fileMd5") String fileMd5);

}
