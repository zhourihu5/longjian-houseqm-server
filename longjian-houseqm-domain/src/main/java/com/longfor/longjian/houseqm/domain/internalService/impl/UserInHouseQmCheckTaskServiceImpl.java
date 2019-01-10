package com.longfor.longjian.houseqm.domain.internalService.impl;

import com.longfor.gaia.gfs.data.mybatis.datasource.LFAssignDataSource;
import com.longfor.longjian.houseqm.dao.UserInHouseQmCheckTaskMapper;
import com.longfor.longjian.houseqm.domain.internalService.UserInHouseQmCheckTaskService;
import com.longfor.longjian.houseqm.po.UserInHouseQmCheckTask;
import com.longfor.longjian.houseqm.utils.ExampleUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author lipeishuai
 * @date 2018/11/23 11:37
 */
@Service
@Slf4j
public class UserInHouseQmCheckTaskServiceImpl implements UserInHouseQmCheckTaskService {


    @Resource
    UserInHouseQmCheckTaskMapper userInHouseQmCheckTaskMapper;


    /**
     * @param userId
     * @return
     */
    @LFAssignDataSource("zhijian2")
    public List<UserInHouseQmCheckTask> searchByUserId(Integer userId) {
        return userInHouseQmCheckTaskMapper.selectByUserId(userId);
    }

    /**
     * @param taskIdList
     * @return
     */
    @LFAssignDataSource("zhijian2")
    public List<UserInHouseQmCheckTask> selectByTaskIds(Set<Integer> taskIdList) {
        List<Integer> list = taskIdList.stream().collect(Collectors.toList());
        return userInHouseQmCheckTaskMapper.selectByTaskIds(list, "false");
    }

    /**
     * @param taskIdList
     * @return
     */
    @LFAssignDataSource("zhijian2")
    public List<UserInHouseQmCheckTask> selectByTaskIdsEvenDeleted(Set<Integer> taskIdList) {
        List<Integer> list = taskIdList.stream().collect(Collectors.toList());
        return userInHouseQmCheckTaskMapper.selectByTaskIds(list, "true");
    }

    @Override
    @LFAssignDataSource("zhijian2")
    public List<UserInHouseQmCheckTask> searchByTaskIdAndNoDeleted(Integer taskId) {
        Example example = new Example(UserInHouseQmCheckTask.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("taskId",taskId);
        ExampleUtil.addDeleteAtJudge(example);
        return userInHouseQmCheckTaskMapper.selectByExample(example);
    }

    /**
     *
     * @author hy
     * @date 2018/12/25 0025
     * @param checker
     * @param uid
     * @param task_id
     * @return java.util.List<com.longfor.longjian.houseqm.po.UserInHouseQmCheckTask>
     */
    @Override
    @LFAssignDataSource("zhijian2")
    public List<UserInHouseQmCheckTask> selectSquadIdByTaskIdAndUserIdAndRoleTypeAndNoDeleted(Integer checker, Integer uid, Integer task_id) {
        return userInHouseQmCheckTaskMapper.selectSquadIdByTaskIdAndUserIdAndRoleTypeAndNoDeleted(uid,task_id,"false",checker);
    }

    /**
     *
     * @author hy
     * @date 2018/12/25 0025
     * @param squadIds
     * @return java.util.List<com.longfor.longjian.houseqm.po.UserInHouseQmCheckTask>
     */
    @Override
    @LFAssignDataSource("zhijian2")
    public List<UserInHouseQmCheckTask> selectUserIdBySquadIdInAndNoDeleted(List<Integer> squadIds) {
        return userInHouseQmCheckTaskMapper.selectUserIdBySquadIdInAndNoDeleted(squadIds,"false");
    }

    /**
     *
     * @author hy
     * @date 2018/12/25 0025
     * @param task_id
     * @return com.longfor.longjian.houseqm.po.UserInHouseQmCheckTask
     */
    @Override
    @LFAssignDataSource("zhijian2")
    public List<UserInHouseQmCheckTask> selectUpdateAtByTaskIdAndNoDeletedOrderByUpdateAt(Integer task_id) {
        return userInHouseQmCheckTaskMapper.selectUpdateAtByTaskIdAndNoDeletedOrderByUpdateAt(task_id,"false");
    }

    @Override
    @LFAssignDataSource("zhijian2")
    @Transactional
    public int removeByTaskId(Integer task_id) {
        Example example = new Example(UserInHouseQmCheckTask.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("taskId",task_id);
        return userInHouseQmCheckTaskMapper.deleteByExample(example);
    }

    @Override
    @LFAssignDataSource("zhijian2")
    public int add(UserInHouseQmCheckTask qmCheckTask) {
        return userInHouseQmCheckTaskMapper.insert(qmCheckTask);
    }

    @Override
    @LFAssignDataSource("zhijian2")
    public UserInHouseQmCheckTask selectByTaskIdAndUserIdAndNotDel(Integer taskId, Integer uid) {
        Example example = new Example(UserInHouseQmCheckTask.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("taskId",taskId).andEqualTo("userId",uid).andIsNull("deleteAt");
        return userInHouseQmCheckTaskMapper.selectOneByExample(example);
    }

}
