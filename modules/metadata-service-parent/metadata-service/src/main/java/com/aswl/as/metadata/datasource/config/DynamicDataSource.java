/**
 * Copyright (c) 2018 ASWL All rights reserved.
 *
 * https://www.gzaswl.net
 *
 * 2019.11
 */

package com.aswl.as.metadata.datasource.config;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 多数据源
 *
 * @author admin@gzaswl.net
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        return DynamicContextHolder.peek();
    }

}
