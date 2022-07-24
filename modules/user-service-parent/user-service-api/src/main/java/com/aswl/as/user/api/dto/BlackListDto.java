package com.aswl.as.user.api.dto;
import java.io.Serializable;
import java.util.Date;

import com.aswl.as.common.core.persistence.TreeEntity;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
*
* 系统IP黑名单Dto
* @author dingfei
* @date 2019-11-11 10:40
*/

@ApiModel(value = "BlackListDto",description = "系统IP黑名单Dto")
@Data
public class BlackListDto extends TreeEntity<BlackListDto> {


    /**
    * ip地址
    */
    @ApiModelProperty(value = "ip地址",name="ip")
    private String ip;
    /**
    * 所属部门
    */
    @ApiModelProperty(value = "所属部门",name="orgCode")
    private String orgCode;
    /**
    * 创建人名称
    */
    @ApiModelProperty(value = "创建人名称",name="createName")
    private String createName;
    /**
    * 备注
    */
    @ApiModelProperty(value = "备注",name="remark")
    private String remark;
}
