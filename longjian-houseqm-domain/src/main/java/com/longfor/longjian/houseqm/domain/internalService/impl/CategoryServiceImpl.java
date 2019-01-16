package com.longfor.longjian.houseqm.domain.internalService.impl;

import com.longfor.gaia.gfs.data.mybatis.datasource.LFAssignDataSource;
import com.longfor.longjian.houseqm.dao.CategoryMapper;
import com.longfor.longjian.houseqm.domain.internalService.CategoryService;
import com.longfor.longjian.houseqm.po.Category;
import com.longfor.longjian.houseqm.po.CategoryV3;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Dongshun on 2018/12/21.
 */
@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {
    @Resource
    CategoryMapper categoryMapper;

    @Override
    @LFAssignDataSource("zhijian2")
    public List<CategoryV3> SearchCategoryByKeyIn(List<String> keys) {
        return categoryMapper.searchCategoryByKeyIn(keys);
    }

    @Override
    @LFAssignDataSource("zhijian2")
    public List<CategoryV3> SearchCategoryByFatherKey(String categoryRootKey) {
        return categoryMapper.searchCategoryByFatherKey(categoryRootKey);
    }
}
