package com.longfor.longjian.houseqm.domain.internalservice.impl;

import com.longfor.gaia.gfs.data.mybatis.datasource.LFAssignDataSource;
import com.longfor.longjian.common.util.DateUtil;
import com.longfor.longjian.houseqm.dao.zj2db.HouseQmCheckTaskIssueUserMapper;
import com.longfor.longjian.houseqm.domain.internalservice.HouseQmCheckTaskIssueUserService;
import com.longfor.longjian.houseqm.po.zj2db.HouseQmCheckTaskIssueUser;
import com.longfor.longjian.houseqm.utils.ExampleUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author houyan
 * @date 2018/12/11 0011 14:00
 */
@Transactional
@Service
@Slf4j
public class HouseQmCheckTaskIssueUserServiceImpl implements HouseQmCheckTaskIssueUserService {

    @Resource
    HouseQmCheckTaskIssueUserMapper houseQmCheckTaskIssueUserMapper;

    @Transactional
    @Override
    @LFAssignDataSource(value = "zhijian2")
    public int insertBatch(List<HouseQmCheckTaskIssueUser> issueUsers) {
        for (HouseQmCheckTaskIssueUser issueUser : issueUsers) {
            issueUser.setUpdateAt(new Date());
            issueUser.setCreateAt(new Date());
        }
        return houseQmCheckTaskIssueUserMapper.insertList(issueUsers);
    }

    /**
     * 根据userId taskId createAt时间 查 取未删除的
     *
     * @param userId
     * @param taskId
     * @param timestamp
     * @return
     */
    @LFAssignDataSource(value = "zhijian2")
    public List<HouseQmCheckTaskIssueUser> searchByUserIdAndTaskIdAndCreateAt(int userId, int taskId, int timestamp) {
        Example example = new Example(HouseQmCheckTaskIssueUser.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId", userId).andEqualTo("taskId", taskId);
        criteria.andGreaterThan("createAt", DateUtil.timestampToString(timestamp, "yyyy-MM-dd"));
        ExampleUtil.addDeleteAtJudge(example);
        //List<HouseQmCheckTaskIssueUser> houseQmCheckTaskIssueUsers = houseQmCheckTaskIssueUserMapper.selectByUserIdAndTaskIdAndCreateAt(userId, taskId, timestamp,"false");
        List<HouseQmCheckTaskIssueUser> houseQmCheckTaskIssueUsers = houseQmCheckTaskIssueUserMapper.selectByExample(example);
        return houseQmCheckTaskIssueUsers;
    }

    /**
     * @param uid
     * @param task_id
     * @return java.util.List<com.longfor.longjian.houseqm.po.zj2db.HouseQmCheckTaskIssueUser>
     * @author hy
     * @date 2018/12/25 0025
     */
    @Override
    @LFAssignDataSource(value = "zhijian2")
    public List<HouseQmCheckTaskIssueUser> selectIssueUUIDByUserIdAndTaskIdAndNoDeleted(Integer uid, Integer task_id) {
        return houseQmCheckTaskIssueUserMapper.selectIssueUUIDByUserIdAndTaskIdAndNoDeleted(uid, task_id, "false");
    }

    @Override
    @LFAssignDataSource(value = "zhijian2")
    public HouseQmCheckTaskIssueUser selectUpdateAtByTaskIdAndNoDeletedOrderByUpdateAt(Integer task_id) {
        Example example = new Example(HouseQmCheckTaskIssueUser.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("taskId", task_id);
        ExampleUtil.addDeleteAtJudge(example);
        example.orderBy("updateAt").desc();
        List<HouseQmCheckTaskIssueUser> result = houseQmCheckTaskIssueUserMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(result)) return null;
        else return result.get(0);
    }

    @Override
    @LFAssignDataSource(value = "zhijian2")
    public HouseQmCheckTaskIssueUser selectByIssueUUidAndUserIdAndRoleTypeAndNotDel(String uuid, Integer repairerId, Integer value) {
        Example example = new Example(HouseQmCheckTaskIssueUser.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("issueUuid", uuid);
        criteria.andEqualTo("userId", repairerId);
        criteria.andEqualTo("roleType", value).andIsNull("deleteAt");
        return houseQmCheckTaskIssueUserMapper.selectOneByExample(example);
    }

    @Transactional
    @Override
    @LFAssignDataSource(value = "zhijian2")
    public void add(HouseQmCheckTaskIssueUser repairerUserInfos) {
        repairerUserInfos.setCreateAt(new Date());
        repairerUserInfos.setUpdateAt(new Date());
        houseQmCheckTaskIssueUserMapper.insert(repairerUserInfos);

    }

    @Transactional
    @Override
    @LFAssignDataSource(value = "zhijian2")
    public void update(HouseQmCheckTaskIssueUser repairerUserInfo) {
        repairerUserInfo.setUpdateAt(new Date());
        houseQmCheckTaskIssueUserMapper.updateByPrimaryKeySelective(repairerUserInfo);
    }

    @Override
    @LFAssignDataSource(value = "zhijian2")
    public List<HouseQmCheckTaskIssueUser> selectByRoleTypeAndUserIdAndIssueUuid(Integer value, ArrayList<Integer> intFollowers, String uuid) {
        Example example = new Example(HouseQmCheckTaskIssueUser.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("roleType", value);
        criteria.andIn("userId", intFollowers);
        criteria.andEqualTo("issueUuid", uuid).andIsNull("deleteAt");
        return houseQmCheckTaskIssueUserMapper.selectByExample(example);
    }

    @Transactional
    @Override
    @LFAssignDataSource(value = "zhijian2")
    public void insertMany(ArrayList<HouseQmCheckTaskIssueUser> insertData) {
        for (HouseQmCheckTaskIssueUser datum : insertData) {
            datum.setUpdateAt(new Date());
            datum.setCreateAt(new Date());
        }
        houseQmCheckTaskIssueUserMapper.insertList(insertData);
    }

}
