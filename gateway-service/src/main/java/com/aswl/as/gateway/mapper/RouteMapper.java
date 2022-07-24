package com.aswl.as.gateway.mapper;

import com.aswl.as.gateway.module.Route;
import com.aswl.as.common.core.persistence.CrudMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * Route mapper
 *
 * @author aswl.com
 * @date 2019/4/2 15:00
 */
@Mapper
public interface RouteMapper extends CrudMapper<Route> {
}
