/**
 * Copyright (c) 2018 ASWL All rights reserved.
 *
 * https://www.gzaswl.net
 *
 * 2019.11
 */

package com.aswl.as.asos.datasource.annotation;

import java.lang.annotation.*;

/**
 * 多数据源注解
 *
 * @author admin@gzaswl.net
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface DataSource {
    String value() default "";
}
