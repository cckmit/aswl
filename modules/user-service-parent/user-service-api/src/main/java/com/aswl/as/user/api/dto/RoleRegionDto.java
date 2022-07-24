package com.aswl.as.user.api.dto;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
*
* 区域角色Dto
* @author dingfei
* @date 2019-10-15 13:56
*/

@ApiModel(value = "RoleRegionDto",description = "区域角色Dto")
@Data
public class RoleRegionDto implements Serializable {

private static final long serialVersionUID = 1L;

    /**
    * 主键
    */
    @ApiModelProperty(value = "主键",name="id")
    private String id;
    /**
    * 区域ID
    */
    @ApiModelProperty(value = "区域ID",name="regionId")
    private String regionId;
    /**
    * 角色ID
    */
    @ApiModelProperty(value = "角色ID",name="roleId")
    private String roleId;
}
