package com.longfor.longjian.houseqm.domain.internalservice.impl;

import com.google.common.collect.Lists;
import com.longfor.gaia.gfs.data.mybatis.datasource.LFAssignDataSource;
import com.longfor.longjian.houseqm.dao.zj2db.HouseOwnerInfoMapper;
import com.longfor.longjian.houseqm.domain.internalservice.HouseOwnerInfoService;
import com.longfor.longjian.houseqm.po.zj2db.HouseOwnerInfo;
import com.longfor.longjian.houseqm.utils.ExampleUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;


@Service
@Slf4j
public class HouseOwnerInfoServiceImpl implements HouseOwnerInfoService {

    @Resource
    private HouseOwnerInfoMapper houseOwnerInfoMapper;

    @Override
    @LFAssignDataSource("zhijian2")
    public List<HouseOwnerInfo> searchHouseOwnerInfoByProjInAreaIdIn(List<Integer> projIds, List<Integer> houseIds) {
        if (CollectionUtils.isEmpty(projIds)&&CollectionUtils.isEmpty(houseIds))return Lists.newArrayList();
        Example example = new Example(HouseOwnerInfo.class);
        Example.Criteria criteria = example.createCriteria();
        if (CollectionUtils.isNotEmpty(projIds))criteria.andIn("projectId", projIds);
        if (CollectionUtils.isNotEmpty(houseIds))criteria.andIn("areaId", houseIds);
        ExampleUtil.addDeleteAtJudge(example);
        return houseOwnerInfoMapper.selectByExample(example);
    }
}
