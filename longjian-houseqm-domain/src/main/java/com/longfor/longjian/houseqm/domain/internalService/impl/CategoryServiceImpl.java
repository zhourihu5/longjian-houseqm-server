package com.longfor.longjian.houseqm.domain.internalService.impl;

import com.longfor.gaia.gfs.data.mybatis.datasource.LFAssignDataSource;
import com.longfor.longjian.houseqm.dao.CategoryV3Mapper;
import com.longfor.longjian.houseqm.domain.internalService.CategoryService;
import com.longfor.longjian.houseqm.po.CategoryV3;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Dongshun on 2018/12/21.
 */
@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {
    @Resource
    CategoryV3Mapper categoryMapper;

    @Override
    @LFAssignDataSource("zhijian2")
    public List<CategoryV3> searchCategoryByKeyIn(List<String> keys) {
        Example example = new Example(CategoryV3.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("key",keys);
      return categoryMapper.selectByExample(example);
    }

    @Override
    @LFAssignDataSource("zhijian2")
    public List<CategoryV3> searchCategoryByFatherKey(String categoryRootKey) {
        Example example = new Example(CategoryV3.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("fatherKey",categoryRootKey);
        return   categoryMapper.selectByExample(example);
    }
}
