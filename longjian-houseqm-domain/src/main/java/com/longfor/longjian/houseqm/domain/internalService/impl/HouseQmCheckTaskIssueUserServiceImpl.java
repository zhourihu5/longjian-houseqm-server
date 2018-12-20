package com.longfor.longjian.houseqm.domain.internalService.impl;

import com.longfor.gaia.gfs.data.mybatis.datasource.LFAssignDataSource;
import com.longfor.longjian.houseqm.dao.HouseQmCheckTaskIssueUserMapper;
import com.longfor.longjian.houseqm.domain.internalService.HouseQmCheckTaskIssueUserService;
import com.longfor.longjian.houseqm.po.HouseQmCheckTaskIssue;
import com.longfor.longjian.houseqm.po.HouseQmCheckTaskIssueUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

/**
 * @author houyan
 * @date 2018/12/11 0011 14:00
 */
@Service
@Slf4j
public class HouseQmCheckTaskIssueUserServiceImpl implements HouseQmCheckTaskIssueUserService {

    @Resource
    HouseQmCheckTaskIssueUserMapper houseQmCheckTaskIssueUserMapper;

    /**
     * 根据userId taskId createAt时间 查 取未删除的
     * @param userId
     * @param taskId
     * @param timestamp
     * @return
     */
    @LFAssignDataSource(value = "zhijian2")
    public List<HouseQmCheckTaskIssueUser> searchByUserIdAndTaskIdAndCreateAt(int userId, int taskId,int timestamp){
        List<HouseQmCheckTaskIssueUser> houseQmCheckTaskIssueUsers = houseQmCheckTaskIssueUserMapper.selectByUserIdAndTaskIdAndCreateAt(userId, taskId, timestamp,"false");
        return houseQmCheckTaskIssueUsers;
    }

}
