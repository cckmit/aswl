package com.aswl.as.common.log.annotation;

import com.aswl.as.common.core.enums.BusinessType;

import java.lang.annotation.*;

/**
 * 日志注解
 *
 * @author aswl.com
 * @date 2019/3/12 23:50
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log {
    /**
     * 描述
     *
     * @return {String}
     */
    String value();
    /**
     * 功能
     */
    BusinessType businessType() default BusinessType.OTHER;
}
