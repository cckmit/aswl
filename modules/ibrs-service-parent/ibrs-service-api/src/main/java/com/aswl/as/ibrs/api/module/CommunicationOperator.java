package com.aswl.as.ibrs.api.module;
import java.util.Date;
import com.aswl.as.common.core.persistence.BaseEntity;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
*
* 通讯运营商Entity
* @author df
* @date 2021/12/03 11:23
*/

@ApiModel(value = "CommunicationOperator",description = "通讯运营商Entity")
@Data
public class CommunicationOperator extends BaseEntity<CommunicationOperator> {
    /**
    * 名称
    */

    @ApiModelProperty(value = "名称",name="name")
    private String name;
    /**
    * 备注
    */

    @ApiModelProperty(value = "备注",name="remark")
    private String remark;
}
