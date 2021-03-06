package com.longfor.longjian.houseqm.domain.internalservice.impl;

import com.google.common.collect.Lists;
import com.longfor.gaia.gfs.data.mybatis.datasource.LFAssignDataSource;
import com.longfor.longjian.houseqm.dao.zj2db.HouseQmCheckTaskSquadMapper;
import com.longfor.longjian.houseqm.domain.internalservice.HouseQmCheckTaskSquadService;
import com.longfor.longjian.houseqm.po.zj2db.HouseQmCheckTaskSquad;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author lipeishuai
 * @date 2018/11/23 11:37
 */
@Transactional
@Service
@Slf4j
public class HouseQmCheckTaskSquadServiceImpl implements HouseQmCheckTaskSquadService {


    @Resource
    private HouseQmCheckTaskSquadMapper houseQmCheckTaskSquadMapper;

    private static final String TASK_ID="taskId";

    @LFAssignDataSource("zhijian2")
    public List<HouseQmCheckTaskSquad> selectByTaskIds(Set<Integer> taskIdList) {
        if(CollectionUtils.isEmpty(taskIdList))return Lists.newArrayList();
        return houseQmCheckTaskSquadMapper.selectByTaskIds(taskIdList, "false");
    }


    @LFAssignDataSource("zhijian2")
    public List<HouseQmCheckTaskSquad> selectByTaskIdsEvenDeleted(Set<Integer> taskIdList) {
        if(CollectionUtils.isEmpty(taskIdList))return Lists.newArrayList();
        Example example = new Example(HouseQmCheckTaskSquad.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn(TASK_ID, taskIdList);
        return houseQmCheckTaskSquadMapper.selectByExample(example);
    }

    @Transactional
    @Override
    @LFAssignDataSource("zhijian2")
    public int add(HouseQmCheckTaskSquad squad) {
        houseQmCheckTaskSquadMapper.insert(squad);
        return squad.getId();
    }

    @Override
    @LFAssignDataSource("zhijian2")
    public HouseQmCheckTaskSquad selectById(int squadInfo) {
        Example example = new Example(HouseQmCheckTaskSquad.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("id", squadInfo).andIsNull("deleteAt");
        return houseQmCheckTaskSquadMapper.selectOneByExample(example);
    }

    @Override
    @LFAssignDataSource("zhijian2")
    public List<HouseQmCheckTaskSquad> searchHouseqmCheckTaskSquad(String projectId, String taskId) {
        Example example = new Example(HouseQmCheckTaskSquad.class);
        example.createCriteria().andEqualTo("projectId", projectId).andEqualTo(TASK_ID, taskId);
        return houseQmCheckTaskSquadMapper.selectByExample(example);

    }

    @Override
    @LFAssignDataSource("zhijian2")
    public int delete(HouseQmCheckTaskSquad dbItem) {
        HouseQmCheckTaskSquad squad = houseQmCheckTaskSquadMapper.selectOne(dbItem);
        squad.setDeleteAt(new Date());
        return houseQmCheckTaskSquadMapper.updateByPrimaryKeySelective(squad);
    }

    @Override
    @LFAssignDataSource("zhijian2")
    public int update(HouseQmCheckTaskSquad dbItem) {
        dbItem.setUpdateAt(new Date());
        return houseQmCheckTaskSquadMapper.updateByPrimaryKey(dbItem);
    }

    @Override
    @LFAssignDataSource("zhijian2")
    public List<HouseQmCheckTaskSquad> selectByProjectIdAndTaskIdAndSquadType(Integer projectId, Integer taskId, Integer value) {
        Example example = new Example(HouseQmCheckTaskSquad.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("projectId", projectId).andEqualTo(TASK_ID, taskId).andEqualTo("squadType", value).andIsNull("deleteAt");
        return houseQmCheckTaskSquadMapper.selectByExample(example);
    }


}
