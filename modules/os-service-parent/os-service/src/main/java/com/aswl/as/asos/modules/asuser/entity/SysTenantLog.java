package com.aswl.as.asos.modules.asuser.entity;

import java.time.LocalDateTime;
import java.io.Serializable;

import com.aswl.as.common.core.utils.IdGen;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * SAAS租户信息记录表
 * </p>
 *
 * @author hfx
 * @since 2020-03-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="SysTenantLog对象", description="SAAS租户信息记录表")
public class SysTenantLog implements Serializable {

    private static final long serialVersionUID = 1L;

    public SysTenantLog() {
    }

    public SysTenantLog(SysTenant tenant) {
        this.id= IdGen.snowflakeId();
        this.tenantCode=tenant.getTenantCode();
        this.status=tenant.getStatus();
        this.creator=tenant.getModifier();
        this.createDate=tenant.getModifyDate();
        this.validCount=tenant.getValidCount();
        this.validUnit=tenant.getValidUnit();
        this.effectiveStartTime=tenant.getEffectiveStartTime();
        this.effectiveEndTime=tenant.getEffectiveEndTime();
    }

    @ApiModelProperty(value = "唯一ID")
    @TableId(type = IdType.INPUT)
    private String id;

    @ApiModelProperty(value = "租户标识")
    private String tenantCode;

    @ApiModelProperty(value = "状态，0-锁定，1-激活，2-已过期 ,3-续费")
    private String status;

    private String creator;

    private String createDate;

    @ApiModelProperty(value = "续费有效数量")
    private Integer validCount;

    @ApiModelProperty(value = "续费有效期单位")
    private String validUnit;

    @ApiModelProperty(value = "生效开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private LocalDateTime effectiveStartTime;

    @ApiModelProperty(value = "生效结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private LocalDateTime effectiveEndTime;


}
