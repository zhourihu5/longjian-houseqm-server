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
     *
     * @param userId
     * @return
     */
    public List<UserInHouseQmCheckTask> searchByUserId(Integer userId){

        return userInHouseQmCheckTaskMapper.selectByUserId(userId);
    }

    /**
     *
     * @param taskIdList
     * @return
     */
    public List<UserInHouseQmCheckTask> selectByTaskIds(Set<Integer> taskIdList){

        return userInHouseQmCheckTaskMapper.selectByTaskIds(taskIdList,"false");
    }

    /**
     *
     * @param taskIdList
     * @return
     */
    public List<UserInHouseQmCheckTask> selectByTaskIdsEvenDeleted(Set<Integer> taskIdList){

        return userInHouseQmCheckTaskMapper.selectByTaskIds(taskIdList,"true");
    }

    @Override
    @LFAssignDataSource("zhijian2")
    public List<UserInHouseQmCheckTask> searchByTaskIdAndNoDeleted(Integer taskId) {
        return userInHouseQmCheckTaskMapper.selectByTaskIdAndNoDeleted(taskId,"false");
    }

}
