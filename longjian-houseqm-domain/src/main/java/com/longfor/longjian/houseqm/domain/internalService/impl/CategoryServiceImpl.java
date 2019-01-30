package com.longfor.longjian.houseqm.domain.internalService.impl;

import com.google.common.collect.Lists;
import com.longfor.gaia.gfs.data.mybatis.datasource.LFAssignDataSource;
import com.longfor.longjian.houseqm.dao.CategoryV3Mapper;
import com.longfor.longjian.houseqm.domain.internalService.CategoryService;
import com.longfor.longjian.houseqm.po.CategoryV3;
import com.longfor.longjian.houseqm.utils.ExampleUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
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
        if (keys.size()>0)criteria.andIn("key", keys);
        ExampleUtil.addDeleteAtJudge(example);
        return categoryMapper.selectByExample(example);
    }

    @Override
    @LFAssignDataSource("zhijian2")
    public List<CategoryV3> searchCategoryByFatherKey(String categoryRootKey) {
        Example example = new Example(CategoryV3.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("key", categoryRootKey);
        ExampleUtil.addDeleteAtJudge(example);
        CategoryV3 item = categoryMapper.selectOneByExample(example);

        Example example1 = new Example(CategoryV3.class);
        Example.Criteria criteria1 = example1.createCriteria();
        criteria1.andEqualTo("fatherKey", categoryRootKey);
        ExampleUtil.addDeleteAtJudge(example1);
        List<CategoryV3> items = categoryMapper.selectByExample(example1);
        List<CategoryV3> result = Lists.newArrayList();
        result.add(item);
        result.addAll(items);
        return result;
    }
}
