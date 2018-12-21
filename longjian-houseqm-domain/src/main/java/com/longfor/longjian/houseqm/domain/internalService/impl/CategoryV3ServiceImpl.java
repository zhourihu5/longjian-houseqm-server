package com.longfor.longjian.houseqm.domain.internalService.impl;

import com.longfor.gaia.gfs.data.mybatis.datasource.LFAssignDataSource;
import com.longfor.longjian.houseqm.dao.CategoryV3Mapper;
import com.longfor.longjian.houseqm.domain.internalService.CategoryV3Service;
import com.longfor.longjian.houseqm.po.CategoryV3;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Houyan
 * @date 2018/12/21 0021 17:14
 */
@Service
@Slf4j
public class CategoryV3ServiceImpl implements CategoryV3Service {

    @Resource
    CategoryV3Mapper categoryV3Mapper;

    /**
     *
     * @author hy
     * @date 2018/12/21 0021
     *  * @param categoryKeys
     * @return java.util.List<com.longfor.longjian.houseqm.po.CategoryV3>
     */
    @LFAssignDataSource("zhijian2")
    @Override
    public List<CategoryV3> searchCategoryV3ByKeyInAndNoDeleted(List<String> categoryKeys) {
        return categoryV3Mapper.selectCategoryV3ByKeyInAndNoDeleted(categoryKeys,"false");
    }
}
