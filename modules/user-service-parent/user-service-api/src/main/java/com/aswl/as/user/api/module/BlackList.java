package com.aswl.as.user.api.module;

import com.aswl.as.common.core.persistence.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
*
* 系统IP黑名单Entity
* @author dingfei
* @date 2019-11-11 10:40
*/

@ApiModel(value = "BlackList",description = "系统IP黑名单Entity")
@Data
public class BlackList extends BaseEntity<BlackList> {


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
