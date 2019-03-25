package com.longfor.longjian.houseqm.domain.internalservice.impl;

import com.google.common.collect.Lists;
import com.longfor.gaia.gfs.data.mybatis.datasource.LFAssignDataSource;
import com.longfor.longjian.houseqm.dao.zj2db.CheckItemMapper;
import com.longfor.longjian.houseqm.domain.internalservice.CheckItemService;
import com.longfor.longjian.houseqm.po.zj2db.CheckItem;
import com.longfor.longjian.houseqm.utils.ExampleUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Dongshun on 2018/12/21.
 */
@Service
@Slf4j
public class CheckItemServiceImpl implements CheckItemService {
    @Resource
    private CheckItemMapper checkItemMapper;

    @Override
    @LFAssignDataSource("zhijian2")
    public List<CheckItem> searchCheckItemByKeyIn(List<String> keys) {
        if (CollectionUtils.isEmpty(keys)) return Lists.newArrayList();
        Example example = new Example(CheckItem.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("key", keys);
        ExampleUtil.addDeleteAtJudge(example);
        return checkItemMapper.selectByExample(example);
    }
}
