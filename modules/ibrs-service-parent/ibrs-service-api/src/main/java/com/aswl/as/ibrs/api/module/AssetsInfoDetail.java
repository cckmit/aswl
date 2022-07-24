package com.aswl.as.ibrs.api.module;
import com.aswl.as.common.core.persistence.BaseEntity;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
*
* 资产信息明细Entity
* @author df
* @date 2022/03/11 13:38
*/

@ApiModel(value = "AssetsInfoDetail",description = "资产信息明细Entity")
@Data
public class AssetsInfoDetail extends BaseEntity<AssetsInfoDetail> {
    /**
    * 资产信息ID
    */

    @ApiModelProperty(value = "资产信息ID",name="assetsInfoId")
    private String assetsInfoId;
    /**
    * 资产名称
    */

    @ApiModelProperty(value = "资产名称",name="name")
    private String name;
    /**
    * 资产编号
    */

    @ApiModelProperty(value = "资产编号",name="basicNo")
    private String basicNo;
    /**
    * 资产状态（0：空闲；1：使用中；2：维护；3：报废；4：外借；5：故障）
    */

    @ApiModelProperty(value = "资产状态（0：空闲；1：使用中；2：维护；3：报废；4：外借；5：故障）",name="status")
    private Integer status;
    /**
    * 备注
    */

    @ApiModelProperty(value = "备注",name="remark")
    private String remark;
}
