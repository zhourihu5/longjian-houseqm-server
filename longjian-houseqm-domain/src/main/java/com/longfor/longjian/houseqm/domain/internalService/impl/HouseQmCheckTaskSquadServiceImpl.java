package com.longfor.longjian.houseqm.domain.internalService.impl;

import com.longfor.gaia.gfs.data.mybatis.datasource.LFAssignDataSource;
import com.longfor.longjian.houseqm.dao.HouseQmCheckTaskSquadMapper;
import com.longfor.longjian.houseqm.domain.internalService.HouseQmCheckTaskSquadService;
import com.longfor.longjian.houseqm.po.HouseQmCheckTaskSquad;
import com.longfor.longjian.houseqm.po.UserInHouseQmCheckTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
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
    HouseQmCheckTaskSquadMapper houseQmCheckTaskSquadMapper;


    /**
     *
     * @param taskIdList
     * @return
     */
    @LFAssignDataSource("zhijian2")
    public List<HouseQmCheckTaskSquad> selectByTaskIds(Set<Integer> taskIdList){

        return houseQmCheckTaskSquadMapper.selectByTaskIds(taskIdList,"false");
    }

    /**
     *
     * @param taskIdList
     * @return
     */
    @LFAssignDataSource("zhijian2")
    public List<HouseQmCheckTaskSquad> selectByTaskIdsEvenDeleted(Set<Integer> taskIdList){

        return houseQmCheckTaskSquadMapper.selectByTaskIds(taskIdList,"true");
    }

    @Override
    @LFAssignDataSource("zhijian2")
    public int add(HouseQmCheckTaskSquad squad) {
        return houseQmCheckTaskSquadMapper.add(squad);
    }

    @Override
    @LFAssignDataSource("zhijian2")
    public HouseQmCheckTaskSquad selectById(int squadInfo) {
        Example example = new Example(HouseQmCheckTaskSquad.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("id",squadInfo).andIsNull("deleteAt");
        return houseQmCheckTaskSquadMapper.selectOneByExample(example);
    }

    @Override
    @LFAssignDataSource("zhijian2")
    public List<HouseQmCheckTaskSquad> searchHouseqmCheckTaskSquad(String projectId, String taskId) {
        Example example = new Example(HouseQmCheckTaskSquad.class);
            example.createCriteria().andEqualTo("projectId",projectId).andEqualTo("taskId",taskId);
        return  houseQmCheckTaskSquadMapper.selectByExample(example);

    }

    @Override
    @LFAssignDataSource("zhijian2")
    public int delete(HouseQmCheckTaskSquad dbItem) {
        return houseQmCheckTaskSquadMapper.delete(dbItem);
    }

    @Override
    @LFAssignDataSource("zhijian2")
    public int update(HouseQmCheckTaskSquad dbItem) {
        return houseQmCheckTaskSquadMapper.updateByPrimaryKey(dbItem);
    }

    @Override
    @LFAssignDataSource("zhijian2")
    public List<HouseQmCheckTaskSquad> selectByProjectIdAndTaskIdAndSquadType(Integer project_id, Integer task_id, Integer value) {
        Example example = new Example(HouseQmCheckTaskSquad.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("projectId",project_id).andEqualTo("taskId",task_id).andEqualTo("squadType",value);
        return houseQmCheckTaskSquadMapper.selectByExample(example);
    }


}
