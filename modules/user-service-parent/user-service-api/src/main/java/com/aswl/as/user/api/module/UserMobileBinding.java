package com.aswl.as.user.api.module;

import java.util.Date;

import com.aswl.as.common.core.persistence.BaseEntity;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 手机端登录授权Entity
 *
 * @author df
 * @date 2022/03/18 14:24
 */

@ApiModel(value = "UserMobileBinding", description = "手机端登录授权Entity")
@Data
public class UserMobileBinding extends BaseEntity<UserMobileBinding> {
    /**
     * 用户ID
     */

    @ApiModelProperty(value = "用户ID", name = "userId")
    private String userId;
    /**
     * MEID唯一码
     */

    @ApiModelProperty(value = "MEID唯一码", name = "mobileMeid")
    private String mobileMeid;
    /**
     * 手机型号
     */

    @ApiModelProperty(value = "手机型号", name = "mobileModel")
    private String mobileModel;
    /**
     * 是否授权（0：否，1：是）
     */

    @ApiModelProperty(value = "是否授权（0：否，1：是）", name = "isAuth")
    private Integer isAuth;
    /**
     * 登录时间
     */

    @ApiModelProperty(value = "登录时间", name = "loginTime")
    private Date loginTime;
}
