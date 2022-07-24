package com.aswl.as.ibrs.api.module;
import java.util.Date;
import com.aswl.as.common.core.persistence.BaseEntity;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 资产分类Entity
 *
 * @author df
 * @date 2022/01/14 15:51
 */

@ApiModel(value = "AssetsCategory", description = "资产分类Entity")
@Data
public class AssetsCategory extends BaseEntity<AssetsCategory> {
    
    /**
     * 分类名称
     */

    @ApiModelProperty(value = "分类名称", name = "name")
    private String name;
    /**
     * 编码
     */

    @ApiModelProperty(value = "编码", name = "code")
    private String code;
}
