package com.aswl.as.ibrs.api.module;
import com.aswl.as.common.core.persistence.BaseEntity;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
*
* 智能箱管理平台数据表Entity
* @author df
* @date 2021/07/26 14:33
*/

@ApiModel(value = "CityManagePlatform",description = "智能箱管理平台数据表Entity")
@Data
public class CityManagePlatform extends BaseEntity<CityManagePlatform> {
    /**
    * 项目ID
    */

    @ApiModelProperty(value = "项目ID",name="projectId")
    private String projectId;
    /**
    * 区域编码
    */

    @ApiModelProperty(value = "区域编码",name="regionCode")
    private String regionCode;
    /**
    * 1,授权,0,关闭
    */

    @ApiModelProperty(value = "1,授权,0,关闭",name="longRangeControl")
    private Integer longRangeControl;
    /**
    * 1,授权,0,关闭
    */

    @ApiModelProperty(value = "1,授权,0,关闭",name="dataUpload")
    private Integer dataUpload;
}
