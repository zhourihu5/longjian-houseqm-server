package com.longfor.longjian.houseqm.domain.internalservice.impl;

import com.google.common.collect.Lists;
import com.longfor.gaia.gfs.data.mybatis.datasource.LFAssignDataSource;
import com.longfor.longjian.houseqm.dao.zj2db.CheckItemV3Mapper;
import com.longfor.longjian.houseqm.domain.internalservice.CheckItemV3Service;
import com.longfor.longjian.houseqm.po.zj2db.CheckItemV3;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Houyan
 * @date 2018/12/21 0021 17:23
 */
@Service
@Slf4j
public class CheckItemV3ServiceImpl implements CheckItemV3Service {

    @Resource
    CheckItemV3Mapper checkItemV3Mapper;

    /**
     *
     * @author hy
     * @date 2018/12/21 0021
     *  * @param checkItems
     * @return java.util.List<com.longfor.longjian.houseqm.po.zj2db.CheckItemV3>
     */
    @LFAssignDataSource("zhijian2")
    @Override
    public List<CheckItemV3> searchCheckItemyV3ByKeyInAndNoDeleted(List<String> checkItems) {
        if (CollectionUtils.isEmpty(checkItems))return Lists.newArrayList();
        Example example = new Example(CheckItemV3.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("key",checkItems).andIsNull("deleteAt");
        return checkItemV3Mapper.selectByExample(example);
    }
    @LFAssignDataSource("zhijian2")
    @Override
    public CheckItemV3 selectByKeyNotDel(String checkItemKey) {
        Example example = new Example(CheckItemV3.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("key",checkItemKey).andIsNull("deleteAt");
        return checkItemV3Mapper.selectOneByExample(example);
    }
}
