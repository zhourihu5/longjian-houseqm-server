package com.longfor.longjian.houseqm.domain.internalService.impl.test;

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
    UserInHouseQmCheckTaskMapper userInHouseQmCheckTaskMapper;


    @Override
    @LFAssignDataSource("zhijian2")
    public List<UserInHouseQmCheckTask> searchByTaskIdInAndRoleType(List<Integer> taskIds, Integer roleType) {
        Example example = new Example(UserInHouseQmCheckTask.class);
        Example.Criteria criteria = example.createCriteria();
        if (CollectionUtils.isNotEmpty(taskIds)) criteria.andIn("taskId", taskIds).andEqualTo("roleType", roleType);
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
        if (CollectionUtils.isEmpty(squadIds)) return Lists.newArrayList();
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


    @LFAssignDataSource("zhijian2")
    public List<UserInHouseQmCheckTask> searchByUserId(Integer userId) {
        Example example = new Example(UserInHouseQmCheckTask.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId", userId);
        ExampleUtil.addDeleteAtJudge(example);
        return userInHouseQmCheckTaskMapper.selectByExample(example);
    }


    @LFAssignDataSource("zhijian2")
    public List<UserInHouseQmCheckTask> selectByTaskIds(Set<Integer> taskIdList) {
        List<Integer> list = taskIdList.stream().collect(Collectors.toList());
        return userInHouseQmCheckTaskMapper.selectByTaskIds(list, "false");
    }

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


    @Override
    @LFAssignDataSource("zhijian2")
    public List<UserInHouseQmCheckTask> selectSquadIdByTaskIdAndUserIdAndRoleTypeAndNoDeleted(Integer checker, Integer uid, Integer taskId) {
        Example example = new Example(UserInHouseQmCheckTask.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId", uid).andEqualTo("taskId", taskId).andEqualTo("roleType", checker);
        ExampleUtil.addDeleteAtJudge(example);
        return userInHouseQmCheckTaskMapper.selectByExample(example);
    }


    @Override
    @LFAssignDataSource("zhijian2")
    public List<UserInHouseQmCheckTask> selectUserIdBySquadIdInAndNoDeleted(List<Integer> squadIds) {
        Example example = new Example(UserInHouseQmCheckTask.class);
        Example.Criteria criteria = example.createCriteria();
        if (CollectionUtils.isNotEmpty(squadIds)) criteria.andIn("squadId", squadIds);
        else return Lists.newArrayList();
        ExampleUtil.addDeleteAtJudge(example);
        return userInHouseQmCheckTaskMapper.selectByExample(example);
    }


    @Override
    @LFAssignDataSource("zhijian2")
    public List<UserInHouseQmCheckTask> selectUpdateAtByTaskIdAndNoDeletedOrderByUpdateAt(Integer taskId) {
        Example example = new Example(UserInHouseQmCheckTask.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("taskId", taskId);
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
        criteria.andEqualTo("taskId", taskId);
        ExampleUtil.addDeleteAtJudge(example);
        List<UserInHouseQmCheckTask> userInHouseQmCheckTasks = userInHouseQmCheckTaskMapper.selectByExample(example);
        for (UserInHouseQmCheckTask task : userInHouseQmCheckTasks) {
            task.setDeleteAt(new Date());
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
        criteria.andEqualTo("squadId", squadId).andEqualTo("userId", userId).andEqualTo("taskId", taskId).andIsNull("deleteAt");
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
        criteria.andEqualTo("id", o).andEqualTo("taskId", taskId).andIsNull("deleteAt");
        return userInHouseQmCheckTaskMapper.selectByExample(example);
    }

    @Override
    @LFAssignDataSource("zhijian2")
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
        criteria.andEqualTo("taskId", taskId).andEqualTo("roleType", value).andIsNull("deleteAt");
        return userInHouseQmCheckTaskMapper.selectByExample(example);
    }

    @Override
    @LFAssignDataSource("zhijian2")
    public List<UserInHouseQmCheckTask> selectBysquadIdAndtaskId(Object o, Integer taskId) {
        Example example = new Example(UserInHouseQmCheckTask.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("squadId", o);
        criteria.andEqualTo("taskId", taskId).andIsNull("deleteAt");
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
    public List<UserInHouseQmCheckTask> selectByTaskIdInAndRoleTypeNotDel(List<Integer> taskIds, Integer value) {
        Example example = new Example(UserInHouseQmCheckTask.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("taskId", taskIds).andEqualTo("roleType", value).andIsNull("deleteAt");
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
        List<UserInHouseQmCheckTask> tasks = userInHouseQmCheckTaskMapper.selectByExample(example);
        return CollectionUtils.isNotEmpty(tasks) ? tasks.get(0) : null;
    }

}
