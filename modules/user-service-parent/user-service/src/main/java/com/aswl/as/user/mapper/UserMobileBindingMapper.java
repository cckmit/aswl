package com.aswl.as.user.mapper;
import org.apache.ibatis.annotations.Mapper;
import com.aswl.as.common.core.persistence.CrudMapper;
import com.aswl.as.user.api.module.UserMobileBinding;
import org.apache.ibatis.annotations.Param;

/**
 * 手机端登录授权Mapper
 *
 * @author df
 * @date 2022/03/18 14:24
 */

@Mapper
public interface UserMobileBindingMapper extends CrudMapper<UserMobileBinding> {

    /**
     * 根据MEID唯一码查询
     * @param mobileMeid
     * @return int
     */
    int findByMobileMeid(@Param("mobileMeid") String mobileMeid);


    /**
     *  根据MEID唯一码更新
     * @param mobileMeid
     * @return int
     */
    int updateByMobileMeid(@Param("mobileMeid") String mobileMeid);

}
