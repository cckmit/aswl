package com.aswl.as.user.api.module;

import com.aswl.as.common.core.persistence.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @version 1.0.0
 * @Author ke
 * @create 2019/9/17 13:33
 */
@Data
public class Post extends BaseEntity<Post> {

    /**
     * 岗位名称
     */
    private String postName;

    /**
     * 月工作天数
     */
    private Integer workingDays;

    /**
     * 降级标准
     */
    private Integer standardDown;

    /**
     * 升级考评标准
     */
    private Integer standardUp;

    /**
     * 提成比
     */
    private BigDecimal commission;
}
