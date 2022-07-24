package com.aswl.as.asos.modules.asuser.mapper;

import com.aswl.as.asos.modules.asuser.entity.SysAppMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * APP菜单表(现在实际上没那么复杂，但是后面可能会很复杂，所以直接参考sys_menu表来弄，不用的列，清空就是) Mapper 接口
 * </p>
 *
 * @author hfx
 * @since 2020-03-25
 */
@Mapper
public interface SysAppMenuMapper extends BaseMapper<SysAppMenu> {

}
