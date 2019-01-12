package com.longfor.longjian.houseqm.domain.internalService.impl;

import com.longfor.gaia.gfs.data.mybatis.datasource.LFAssignDataSource;
import com.longfor.longjian.houseqm.dao.HouseQmCheckTaskIssueUserMapper;
import com.longfor.longjian.houseqm.domain.internalService.HouseQmCheckTaskIssueUserService;
import com.longfor.longjian.houseqm.po.HouseQmCheckTaskIssue;
import com.longfor.longjian.houseqm.po.HouseQmCheckTaskIssueUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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


    @Override
    @LFAssignDataSource(value = "zhijian2")
    @Transactional
    public int insertBatch(List<HouseQmCheckTaskIssueUser> issueUsers) {
        return houseQmCheckTaskIssueUserMapper.insertList(issueUsers);
    }

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

    /**
     *
     * @author hy
     * @date 2018/12/25 0025
     * @param uid
     * @param task_id
     * @return java.util.List<com.longfor.longjian.houseqm.po.HouseQmCheckTaskIssueUser>
     */
    @Override
    @LFAssignDataSource(value = "zhijian2")
    public List<HouseQmCheckTaskIssueUser> selectIssueUUIDByUserIdAndTaskIdAndNoDeleted(Integer uid, Integer task_id) {
        return houseQmCheckTaskIssueUserMapper.selectIssueUUIDByUserIdAndTaskIdAndNoDeleted(uid,task_id,"false");
    }

    /**
     *
     * @author hy
     * @date 2018/12/25 0025
     * @param task_id
     * @return com.longfor.longjian.houseqm.po.HouseQmCheckTaskIssueUser
     */
    @Override
    @LFAssignDataSource(value = "zhijian2")
    public List<HouseQmCheckTaskIssueUser> selectUpdateAtByTaskIdAndNoDeletedOrderByUpdateAt(Integer task_id) {
        return houseQmCheckTaskIssueUserMapper.selectUpdateAtByTaskIdAndNoDeletedOrderByUpdateAt(task_id,"false");
    }

}
