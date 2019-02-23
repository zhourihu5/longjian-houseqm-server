package com.longfor.longjian.houseqm.domain.internalService.impl;

import com.google.common.collect.Lists;
import com.longfor.gaia.gfs.data.mybatis.datasource.LFAssignDataSource;
import com.longfor.longjian.houseqm.dao.zj2db.UserInHouseQmCheckTaskMapper;
import com.longfor.longjian.houseqm.domain.internalService.UserInHouseQmCheckTaskService;
import com.longfor.longjian.houseqm.po.zj2db.UserInHouseQmCheckTask;
import com.longfor.longjian.houseqm.utils.ExampleUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
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
    UserInHouseQmCheckTaskMapper userInHouseQmCheckTaskMapper;


    @Override
    @LFAssignDataSource("zhijian2")
    public List<UserInHouseQmCheckTask> searchByTaskIdInAndRoleType(List<Integer> task_ids, Integer roleType) {
        Example example = new Example(UserInHouseQmCheckTask.class);
        Example.Criteria criteria = example.createCriteria();
        if (task_ids.size() > 0) criteria.andIn("taskId", task_ids).andEqualTo("roleType", roleType);
        else return Lists.newArrayList();
        ExampleUtil.addDeleteAtJudge(example);
        return userInHouseQmCheckTaskMapper.selectByExample(example);
    }

    @Override
    @LFAssignDataSource("zhijian2")
    public List<UserInHouseQmCheckTask> searchByUserIdAndRoleType(int uid, Integer roleType) {
        Example example = new Example(UserInHouseQmCheckTask.class);
        example.createCriteria().andEqualTo("userId", uid).andEqualTo("roleType", roleType);
        ExampleUtil.addDeleteAtJudge(example);
        return userInHouseQmCheckTaskMapper.selectByExample(example);
    }

    @Override
    @LFAssignDataSource("zhijian2")
    public List<UserInHouseQmCheckTask> searchBySquadIdIn(List<Integer> squadIds) {
        if (squadIds.size() <= 0) return Lists.newArrayList();
        Example example = new Example(UserInHouseQmCheckTask.class);
        example.createCriteria().andIn("squadId", squadIds);
        ExampleUtil.addDeleteAtJudge(example);
        return userInHouseQmCheckTaskMapper.selectByExample(example);
    }

    @Override
    @LFAssignDataSource("zhijian2")
    public List<UserInHouseQmCheckTask> searchByTaskIdUserIdRoleType(int taskId, int userId, Integer roleType) {
        Example example = new Example(UserInHouseQmCheckTask.class);
        example.createCriteria().andEqualTo("taskId", taskId).andEqualTo("userId", userId).andEqualTo("roleType", roleType);
        ExampleUtil.addDeleteAtJudge(example);
        return userInHouseQmCheckTaskMapper.selectByExample(example);
    }

    /**
     * @param userId
     * @return
     */
    @LFAssignDataSource("zhijian2")
    public List<UserInHouseQmCheckTask> searchByUserId(Integer userId) {
        Example example = new Example(UserInHouseQmCheckTask.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId", userId);
        ExampleUtil.addDeleteAtJudge(example);
        return userInHouseQmCheckTaskMapper.selectByExample(example);
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
        Example example = new Example(UserInHouseQmCheckTask.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("taskId", taskIdList);
        return userInHouseQmCheckTaskMapper.selectByExample(example);
    }

    @Override
    @LFAssignDataSource("zhijian2")
    public List<UserInHouseQmCheckTask> searchByTaskIdAndNoDeleted(Integer taskId) {
        Example example = new Example(UserInHouseQmCheckTask.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("taskId", taskId);
        ExampleUtil.addDeleteAtJudge(example);
        return userInHouseQmCheckTaskMapper.selectByExample(example);
    }

    /**
     * @param checker
     * @param uid
     * @param task_id
     * @return java.util.List<com.longfor.longjian.houseqm.po.zj2db.UserInHouseQmCheckTask>
     * @author hy
     * @date 2018/12/25 0025
     */
    @Override
    @LFAssignDataSource("zhijian2")
    public List<UserInHouseQmCheckTask> selectSquadIdByTaskIdAndUserIdAndRoleTypeAndNoDeleted(Integer checker, Integer uid, Integer task_id) {
        Example example = new Example(UserInHouseQmCheckTask.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId", uid).andEqualTo("taskId", task_id).andEqualTo("roleType", checker);
        ExampleUtil.addDeleteAtJudge(example);
        return userInHouseQmCheckTaskMapper.selectByExample(example);
    }

    /**
     * @param squadIds
     * @return java.util.List<com.longfor.longjian.houseqm.po.zj2db.UserInHouseQmCheckTask>
     * @author hy
     * @date 2018/12/25 0025
     */
    @Override
    @LFAssignDataSource("zhijian2")
    public List<UserInHouseQmCheckTask> selectUserIdBySquadIdInAndNoDeleted(List<Integer> squadIds) {
        Example example = new Example(UserInHouseQmCheckTask.class);
        Example.Criteria criteria = example.createCriteria();
        if (squadIds.size() > 0) criteria.andIn("squadId", squadIds);
        else return Lists.newArrayList();
        ExampleUtil.addDeleteAtJudge(example);
        return userInHouseQmCheckTaskMapper.selectByExample(example);
    }

    /**
     * @param task_id
     * @return com.longfor.longjian.houseqm.po.zj2db.UserInHouseQmCheckTask
     * @author hy
     * @date 2018/12/25 0025
     */
    @Override
    @LFAssignDataSource("zhijian2")
    public List<UserInHouseQmCheckTask> selectUpdateAtByTaskIdAndNoDeletedOrderByUpdateAt(Integer task_id) {
        Example example = new Example(UserInHouseQmCheckTask.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("taskId", task_id);
        ExampleUtil.addDeleteAtJudge(example);
        example.orderBy("updateAt").desc();
        return userInHouseQmCheckTaskMapper.selectByExample(example);
    }

    @Override
    @LFAssignDataSource("zhijian2")
    @Transactional
    public int removeByTaskId(Integer task_id) {
        Example example = new Example(UserInHouseQmCheckTask.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("taskId", task_id);
        ExampleUtil.addDeleteAtJudge(example);
        List<UserInHouseQmCheckTask> userInHouseQmCheckTasks = userInHouseQmCheckTaskMapper.selectByExample(example);
        for (UserInHouseQmCheckTask task : userInHouseQmCheckTasks) {
            task.setDeleteAt(new Date());
            userInHouseQmCheckTaskMapper.updateByPrimaryKey(task);
        }
        return userInHouseQmCheckTasks.size();
    }

    @Override
    @LFAssignDataSource("zhijian2")
    public UserInHouseQmCheckTask selectBysquadIdAnduserIdAndtaskIdAndNotDel(Integer squadId, Integer userId, Integer taskId) {
        Example example = new Example(UserInHouseQmCheckTask.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("squadId", squadId).andEqualTo("userId", userId).andEqualTo("taskId", taskId).andIsNull("deleteAt");
        return userInHouseQmCheckTaskMapper.selectOneByExample(example);
    }

    @Override
    @LFAssignDataSource("zhijian2")
    public int update(UserInHouseQmCheckTask dbItem) {
        dbItem.setUpdateAt(new Date());
        return userInHouseQmCheckTaskMapper.updateByPrimaryKey(dbItem);
    }

    @Override
    @LFAssignDataSource("zhijian2")
    public List<UserInHouseQmCheckTask> selectByIdAndTaskId(Object o, Integer task_id) {
        Example example = new Example(UserInHouseQmCheckTask.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("id", o).andEqualTo("taskId", task_id).andIsNull("deleteAt");
        return userInHouseQmCheckTaskMapper.selectByExample(example);
    }

    @Override
    @LFAssignDataSource("zhijian2")
    public int delete(UserInHouseQmCheckTask userInHouseQmCheckTask) {
        List<UserInHouseQmCheckTask> tasks = userInHouseQmCheckTaskMapper.select(userInHouseQmCheckTask);
        for (UserInHouseQmCheckTask task : tasks) {
            task.setUpdateAt(new Date());
            task.setDeleteAt(new Date());
            userInHouseQmCheckTaskMapper.updateByPrimaryKeySelective(task);
        }
        return tasks.size();
    }

    @Override
    @LFAssignDataSource("zhijian2")
    public List<UserInHouseQmCheckTask> selectByTaskIdAndRoleType(Integer task_id, Integer value) {
        Example example = new Example(UserInHouseQmCheckTask.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("taskId", task_id).andEqualTo("roleType", value).andIsNull("deleteAt");
        return userInHouseQmCheckTaskMapper.selectByExample(example);
    }

    @Override
    @LFAssignDataSource("zhijian2")
    public List<UserInHouseQmCheckTask> selectBysquadIdAndtaskId(Object o, Integer task_id) {
        Example example = new Example(UserInHouseQmCheckTask.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("squadId", o);
        criteria.andEqualTo("taskId", task_id).andIsNull("deleteAt");
        return userInHouseQmCheckTaskMapper.selectByExample(example);

    }

    @Override
    @LFAssignDataSource("zhijian2")
    public List<UserInHouseQmCheckTask> searchUserInHouseQmCheckTaskByUserIdRoleType(Integer uid, Integer id) {
        Example example = new Example(UserInHouseQmCheckTask.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId", uid).andEqualTo("roleType", id);
        return userInHouseQmCheckTaskMapper.selectByExample(example);

    }

    @Override
    @LFAssignDataSource("zhijian2")
    public List<UserInHouseQmCheckTask> selectByTaskIdInAndRoleTypeNotDel(ArrayList<Integer> taskIds, Integer value) {
        Example example = new Example(UserInHouseQmCheckTask.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("taskId",taskIds).andEqualTo("roleType",value).andIsNull("deleteAt");
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
        criteria.andEqualTo("taskId", taskId).andEqualTo("userId", uid).andIsNull("deleteAt");
        return userInHouseQmCheckTaskMapper.selectOneByExample(example);
    }

}
