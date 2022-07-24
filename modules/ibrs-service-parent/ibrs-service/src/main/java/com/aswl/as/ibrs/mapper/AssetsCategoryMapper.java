package com.aswl.as.ibrs.mapper;
import org.apache.ibatis.annotations.Mapper;
import com.aswl.as.common.core.persistence.CrudMapper;
import com.aswl.as.ibrs.api.module.AssetsCategory;

/**
 * 资产分类Mapper
 *
 * @author df
 * @date 2022/01/14 15:51
 */

@Mapper
public interface AssetsCategoryMapper extends CrudMapper<AssetsCategory> {

}
