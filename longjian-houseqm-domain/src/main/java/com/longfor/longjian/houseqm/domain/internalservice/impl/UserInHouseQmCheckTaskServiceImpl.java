package com.longfor.longjian.houseqm.domain.internalservice.impl;

import com.google.common.collect.Lists;
import com.longfor.gaia.gfs.data.mybatis.datasource.LFAssignDataSource;
import com.longfor.longjian.houseqm.dao.zj2db.UserInHouseQmCheckTaskMapper;
import com.longfor.longjian.houseqm.domain.internalservice.UserInHouseQmCheckTaskService;
import com.longfor.longjian.houseqm.po.zj2db.UserInHouseQmCheckTask;
import com.longfor.longjian.houseqm.utils.ExampleUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author lipeishuai
 * @date 2018/11/23 11:37
 */
@Transactional
@Service
@Slf4j
public class UserInHouseQmCheckTaskServiceImpl implements UserInHouseQmCheckTaskService {


    @Resource
    private UserInHouseQmCheckTaskMapper userInHouseQmCheckTaskMapper;
    private static final String DELETE_AT="deleteAt";

    private static final String SQUAL_ID="squadId";
    private static final String USER_ID="userId";
    private static final String TASK_ID="taskId";
    private static final String ROLE_TYPE="roleType";

    @Override
    @LFAssignDataSource("zhijian2")
    public List<UserInHouseQmCheckTask> searchByTaskIdInAndRoleType(List<Integer> taskIds, Integer roleType) {
        Example example = new Example(UserInHouseQmCheckTask.class);
        Example.Criteria criteria = example.createCriteria();
        if (CollectionUtils.isNotEmpty(taskIds)) criteria.andIn(TASK_ID, taskIds).andEqualTo(ROLE_TYPE, roleType);
        else return Lists.newArrayList();
        ExampleUtil.addDeleteAtJudge(example);
        return userInHouseQmCheckTaskMapper.selectByExample(example);
    }

    @Override
    @LFAssignDataSource("zhijian2")
    public List<UserInHouseQmCheckTask> searchByUserIdAndRoleType(int uid, Integer roleType) {
        Example example = new Example(UserInHouseQmCheckTask.class);
        example.createCriteria().andEqualTo(USER_ID, uid).andEqualTo(ROLE_TYPE, roleType);
        ExampleUtil.addDeleteAtJudge(example);
        return userInHouseQmCheckTaskMapper.selectByExample(example);
    }

    @Override
    @LFAssignDataSource("zhijian2")
    public List<UserInHouseQmCheckTask> searchBySquadIdIn(List<Integer> squadIds) {
        if (CollectionUtils.isEmpty(squadIds)) return Lists.newArrayList();
        Example example = new Example(UserInHouseQmCheckTask.class);
        example.createCriteria().andIn(SQUAL_ID, squadIds);
        ExampleUtil.addDeleteAtJudge(example);
        return userInHouseQmCheckTaskMapper.selectByExample(example);
    }

    @Override
    @LFAssignDataSource("zhijian2")
    public List<UserInHouseQmCheckTask> searchByTaskIdUserIdRoleType(int taskId, int userId, Integer roleType) {
        Example example = new Example(UserInHouseQmCheckTask.class);
        example.createCriteria().andEqualTo(TASK_ID, taskId).andEqualTo(USER_ID, userId).andEqualTo(ROLE_TYPE, roleType);
        ExampleUtil.addDeleteAtJudge(example);
        return userInHouseQmCheckTaskMapper.selectByExample(example);
    }


    @LFAssignDataSource("zhijian2")
    public List<UserInHouseQmCheckTask> searchByUserId(Integer userId) {
        Example example = new Example(UserInHouseQmCheckTask.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo(USER_ID, userId);
        ExampleUtil.addDeleteAtJudge(example);
        return userInHouseQmCheckTaskMapper.selectByExample(example);
    }


    @LFAssignDataSource("zhijian2")
    public List<UserInHouseQmCheckTask> selectByTaskIds(Set<Integer> taskIdList) {
        if(CollectionUtils.isEmpty(taskIdList))return Lists.newArrayList();
        List<Integer> list = taskIdList.stream().collect(Collectors.toList());
        return userInHouseQmCheckTaskMapper.selectByTaskIds(list, "false");
    }

    @LFAssignDataSource("zhijian2")
    public List<UserInHouseQmCheckTask> selectByTaskIdsEvenDeleted(Set<Integer> taskIdList) {
        if(CollectionUtils.isEmpty(taskIdList))return Lists.newArrayList();
        Example example = new Example(UserInHouseQmCheckTask.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn(TASK_ID, taskIdList);
        return userInHouseQmCheckTaskMapper.selectByExample(example);
    }

    @Override
    @LFAssignDataSource("zhijian2")
    public List<UserInHouseQmCheckTask> searchByTaskIdAndNoDeleted(Integer taskId) {
        Example example = new Example(UserInHouseQmCheckTask.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo(TASK_ID, taskId);
        ExampleUtil.addDeleteAtJudge(example);
        return userInHouseQmCheckTaskMapper.selectByExample(example);
    }


    @Override
    @LFAssignDataSource("zhijian2")
    public List<UserInHouseQmCheckTask> selectSquadIdByTaskIdAndUserIdAndRoleTypeAndNoDeleted(Integer checker, Integer uid, Integer taskId) {
        Example example = new Example(UserInHouseQmCheckTask.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo(USER_ID, uid).andEqualTo(TASK_ID, taskId).andEqualTo(ROLE_TYPE, checker);
        ExampleUtil.addDeleteAtJudge(example);
        return userInHouseQmCheckTaskMapper.selectByExample(example);
    }


    @Override
    @LFAssignDataSource("zhijian2")
    public List<UserInHouseQmCheckTask> selectUserIdBySquadIdInAndNoDeleted(List<Integer> squadIds) {
        Example example = new Example(UserInHouseQmCheckTask.class);
        Example.Criteria criteria = example.createCriteria();
        if (CollectionUtils.isNotEmpty(squadIds)) criteria.andIn(SQUAL_ID, squadIds);
        else return Lists.newArrayList();
        ExampleUtil.addDeleteAtJudge(example);
        return userInHouseQmCheckTaskMapper.selectByExample(example);
    }


    @Override
    @LFAssignDataSource("zhijian2")
    public List<UserInHouseQmCheckTask> selectUpdateAtByTaskIdAndNoDeletedOrderByUpdateAt(Integer taskId) {
        Example example = new Example(UserInHouseQmCheckTask.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo(TASK_ID, taskId);
        ExampleUtil.addDeleteAtJudge(example);
        example.orderBy("updateAt").desc();
        return userInHouseQmCheckTaskMapper.selectByExample(example);
    }

    @Override
    @LFAssignDataSource("zhijian2")
    @Transactional
    public int removeByTaskId(Integer taskId) {
        Example example = new Example(UserInHouseQmCheckTask.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo(TASK_ID, taskId);
        ExampleUtil.addDeleteAtJudge(example);
        List<UserInHouseQmCheckTask> userInHouseQmCheckTasks = userInHouseQmCheckTaskMapper.selectByExample(example);
        for (UserInHouseQmCheckTask task : userInHouseQmCheckTasks) {
            task.setDeleteAt(new Date());
            task.setUpdateAt(new Date());
            userInHouseQmCheckTaskMapper.updateByPrimaryKey(task);
        }
        return userInHouseQmCheckTasks.size();
    }

    @Transactional
    @Override
    @LFAssignDataSource("zhijian2")
    public UserInHouseQmCheckTask selectBysquadIdAnduserIdAndtaskIdAndNotDel(Integer squadId, Integer userId, Integer taskId) {
        Example example = new Example(UserInHouseQmCheckTask.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo(SQUAL_ID, squadId).andEqualTo(USER_ID, userId).andEqualTo(TASK_ID, taskId).andIsNull(DELETE_AT);
        return userInHouseQmCheckTaskMapper.selectOneByExample(example);
    }

    @Transactional
    @Override
    @LFAssignDataSource("zhijian2")
    public int update(UserInHouseQmCheckTask dbItem) {
        dbItem.setUpdateAt(new Date());
        return userInHouseQmCheckTaskMapper.updateByPrimaryKey(dbItem);
    }

    @Override
    @LFAssignDataSource("zhijian2")
    public List<UserInHouseQmCheckTask> selectByIdAndTaskId(Object o, Integer taskId) {
        Example example = new Example(UserInHouseQmCheckTask.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("id", o).andEqualTo(TASK_ID, taskId).andIsNull(DELETE_AT);
        return userInHouseQmCheckTaskMapper.selectByExample(example);
    }

    @Override
    @LFAssignDataSource("zhijian2")
    @Transactional
    public int delete(UserInHouseQmCheckTask userInHouseQmCheckTask) {
        UserInHouseQmCheckTask task = userInHouseQmCheckTaskMapper.selectOne(userInHouseQmCheckTask);
        task.setDeleteAt(new Date());
        return userInHouseQmCheckTaskMapper.updateByPrimaryKeySelective(task);
    }

    @Override
    @LFAssignDataSource("zhijian2")
    public List<UserInHouseQmCheckTask> selectByTaskIdAndRoleType(Integer taskId, Integer value) {
        Example example = new Example(UserInHouseQmCheckTask.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo(TASK_ID, taskId).andEqualTo(ROLE_TYPE, value).andIsNull(DELETE_AT);
        return userInHouseQmCheckTaskMapper.selectByExample(example);
    }

    @Override
    @LFAssignDataSource("zhijian2")
    public List<UserInHouseQmCheckTask> selectBysquadIdAndtaskId(Object o, Integer taskId) {
        Example example = new Example(UserInHouseQmCheckTask.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo(SQUAL_ID, o);
        criteria.andEqualTo(TASK_ID, taskId).andIsNull(DELETE_AT);
        return userInHouseQmCheckTaskMapper.selectByExample(example);

    }

    @Override
    @LFAssignDataSource("zhijian2")
    public List<UserInHouseQmCheckTask> searchUserInHouseQmCheckTaskByUserIdRoleType(Integer uid, Integer id) {
        Example example = new Example(UserInHouseQmCheckTask.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo(USER_ID, uid).andEqualTo(ROLE_TYPE, id);
        return userInHouseQmCheckTaskMapper.selectByExample(example);

    }

    @Override
    @LFAssignDataSource("zhijian2")
    public List<UserInHouseQmCheckTask> selectByTaskIdInAndRoleTypeNotDel(List<Integer> taskIds, Integer value) {
        Example example = new Example(UserInHouseQmCheckTask.class);
        Example.Criteria criteria = example.createCriteria();
        if (CollectionUtils.isNotEmpty(taskIds))criteria.andIn(TASK_ID, taskIds);
        criteria.andEqualTo(ROLE_TYPE, value)
                .andIsNull(DELETE_AT);
        return userInHouseQmCheckTaskMapper.selectByExample(example);
    }

    @Transactional
    @Override
    @LFAssignDataSource("zhijian2")
    public int add(UserInHouseQmCheckTask qmCheckTask) {
        qmCheckTask.setUpdateAt(new Date());
        qmCheckTask.setCreateAt(new Date());
        return userInHouseQmCheckTaskMapper.insert(qmCheckTask);
    }

    @Override
    @LFAssignDataSource("zhijian2")
    public UserInHouseQmCheckTask selectByTaskIdAndUserIdAndNotDel(Integer taskId, Integer uid) {
        Example example = new Example(UserInHouseQmCheckTask.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo(TASK_ID, taskId).andEqualTo(USER_ID, uid).andIsNull(DELETE_AT);
        List<UserInHouseQmCheckTask> tasks = userInHouseQmCheckTaskMapper.selectByExample(example);
        return CollectionUtils.isNotEmpty(tasks) ? tasks.get(0) : null;
    }

}
