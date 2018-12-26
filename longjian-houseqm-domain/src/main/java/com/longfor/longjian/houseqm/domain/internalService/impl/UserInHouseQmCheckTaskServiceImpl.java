package com.longfor.longjian.houseqm.domain.internalService.impl;

import com.longfor.gaia.gfs.data.mybatis.datasource.LFAssignDataSource;
import com.longfor.longjian.houseqm.dao.UserInHouseQmCheckTaskMapper;
import com.longfor.longjian.houseqm.domain.internalService.UserInHouseQmCheckTaskService;
import com.longfor.longjian.houseqm.po.UserInHouseQmCheckTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
        return userInHouseQmCheckTaskMapper.selectByTaskIdAndNoDeleted(taskId, "false");
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

}
