package com.longfor.longjian.houseqm.domain.internalservice.impl;

import com.google.common.collect.Lists;
import com.longfor.gaia.gfs.data.mybatis.datasource.LFAssignDataSource;
import com.longfor.longjian.houseqm.dao.zj2db.CategoryV3Mapper;
import com.longfor.longjian.houseqm.domain.internalservice.CategoryV3Service;
import com.longfor.longjian.houseqm.po.zj2db.CategoryV3;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

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
    private CategoryV3Mapper categoryV3Mapper;

    /**
     * @return java.util.List<com.longfor.longjian.houseqm.po.zj2db.CategoryV3>
     * @author hy
     * @date 2018/12/21 0021
     * * @param categoryKeys
     */
    @LFAssignDataSource("zhijian2")
    @Override
    public List<CategoryV3> searchCategoryV3ByKeyInAndNoDeleted(List<String> categoryKeys) {
        if (CollectionUtils.isEmpty(categoryKeys)) return Lists.newArrayList();
        Example example = new Example(CategoryV3.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("key", categoryKeys).andIsNull("deleteAt");
        return categoryV3Mapper.selectByExample(example);
    }

    @Override
    public CategoryV3 selectByKeyNotDel(String categoryKey) {
        Example example = new Example(CategoryV3.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("key", categoryKey).andIsNull("deleteAt");
        return categoryV3Mapper.selectOneByExample(example);
    }
}
