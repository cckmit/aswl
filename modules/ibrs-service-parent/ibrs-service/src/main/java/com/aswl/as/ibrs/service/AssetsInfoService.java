package com.aswl.as.ibrs.service;
import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.common.core.utils.SysUtil;
import com.aswl.as.ibrs.api.dto.AssetsInfoDto;
import com.aswl.as.ibrs.api.module.AssetsInfo;
import com.aswl.as.ibrs.api.module.AssetsInfoDetail;
import com.aswl.as.ibrs.api.vo.AssetsInfoVo;
import com.aswl.as.ibrs.mapper.AssetsInfoDetailMapper;
import com.aswl.as.ibrs.mapper.AssetsInfoMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@AllArgsConstructor
@Slf4j
@Service
public class AssetsInfoService extends CrudService<AssetsInfoMapper, AssetsInfo> {
    private final AssetsInfoMapper assetsInfoMapper;
    private  final AssetsInfoDetailMapper assetsInfoDetailMapper;


    /**
     * 分页查询所有资产信息
     *
     * @param page
     * @param assetsInfoDto
     * @return PageInfo
     */
    public PageInfo<AssetsInfoVo> findPage(PageInfo<AssetsInfoDto> page, AssetsInfoDto assetsInfoDto) {
        PageHelper.startPage(page.getPageNum(), page.getPageSize());
        return new PageInfo<>(assetsInfoMapper.findInfo(assetsInfoDto));
    }

    /**
     * 新增资产信息
     *
     * @param assetsInfoDto
     * @return int
     */
    @Transactional
    public int insert(AssetsInfoDto assetsInfoDto) {
        int update ;
        AssetsInfo assetsInfo = new AssetsInfo();
        BeanUtils.copyProperties(assetsInfoDto, assetsInfo);
        assetsInfo.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        if ((update = this.insert(assetsInfo)) > 0) {
            for (int i = 1 ;i <= assetsInfoDto.getQuantity() ;i++) {
                AssetsInfoDetail assetsInfoDetail = new AssetsInfoDetail();
                assetsInfoDetail.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
                assetsInfoDetail.setAssetsInfoId(assetsInfo.getId());
                assetsInfoDetail.setName(assetsInfo.getName() + " -"  + i);
                assetsInfoDetail.setBasicNo(assetsInfo.getBasicNo() + " -" + i);
                assetsInfoDetail.setStatus(assetsInfo.getStatus());
                update = assetsInfoDetailMapper.insert(assetsInfoDetail);
            }
        }
        return update;
    }

    /**
     * 删除资产信息
     *
     * @param id
     * @return int
     */
    @Transactional
    public int delete(String id) {
        assetsInfoDetailMapper.deleteByAssetsInfoId(id);
        AssetsInfo assetsInfo = new AssetsInfo();
        assetsInfo.setId(id);
        return assetsInfoMapper.delete(assetsInfo);
    }
}