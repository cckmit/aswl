package com.aswl.as.ibrs.service;
import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.common.core.utils.SysUtil;
import com.aswl.as.ibrs.api.dto.CityManagePlatformDto;
import com.aswl.as.ibrs.api.module.CityManagePlatform;
import com.aswl.as.ibrs.api.vo.CityManagePlatformVo;
import com.aswl.as.ibrs.mapper.CityManagePlatformMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@AllArgsConstructor
@Slf4j
@Service
public class CityManagePlatformService extends CrudService<CityManagePlatformMapper, CityManagePlatform> {
    private final CityManagePlatformMapper cityManagePlatformMapper;


    /**
     * 查询所有智能箱管理平台数据表
     *
     * @param page
     * @param cityManagePlatformDto
     * @return CityManagePlatformVo
     */
    public PageInfo<CityManagePlatformVo> findPage(PageInfo<CityManagePlatformDto> page, CityManagePlatformDto cityManagePlatformDto) {
        PageHelper.startPage(page.getPageNum(), page.getPageSize());
        return new PageInfo<>(cityManagePlatformMapper.findPageInfo(cityManagePlatformDto));
    }

    /**
     * 新增智能箱管理平台数据表
     *
     * @param cityManagePlatform
     * @return int
     */
    @Transactional
    @Override
    public int insert(CityManagePlatform cityManagePlatform) {
        return cityManagePlatformMapper.insert(cityManagePlatform);
    }

    /**
     * 删除智能箱管理平台数据表
     *
     * @param cityManagePlatform
     * @return int
     */
    @Transactional
    @Override
    public int delete(CityManagePlatform cityManagePlatform) {
        return cityManagePlatformMapper.delete(cityManagePlatform);
    }


    /**
     * 批量新增智能箱管理平台数据表
     *
     * @param list list
     * @return int
     */
    @Transactional
    public int insertBath(List<CityManagePlatform> list) {
        int update = 0;
        // 修改/新增数据
        for (CityManagePlatform cityManagePlatform : list) {
            // 如果传过来的id为空、则添加
            if (cityManagePlatform.getId() == null) {
                cityManagePlatform.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
                cityManagePlatformMapper.insert(cityManagePlatform);
                update++;
            } else {
                cityManagePlatformMapper.update(cityManagePlatform);
                update++;
            }
        }
        return update;
    }
}