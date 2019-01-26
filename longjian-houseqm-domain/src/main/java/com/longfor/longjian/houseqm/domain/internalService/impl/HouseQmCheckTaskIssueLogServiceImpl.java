package com.longfor.longjian.houseqm.domain.internalService.impl;

import com.google.common.collect.Lists;
import com.longfor.gaia.gfs.data.mybatis.datasource.LFAssignDataSource;
import com.longfor.longjian.houseqm.dao.HouseQmCheckTaskIssueLogMapper;
import com.longfor.longjian.houseqm.dao.UserInHouseQmCheckTaskMapper;
import com.longfor.longjian.houseqm.domain.internalService.HouseQmCheckTaskIssueLogService;
import com.longfor.longjian.houseqm.po.HouseQmCheckTaskIssueLog;
import com.longfor.longjian.houseqm.po.UserInHouseQmCheckTask;
import com.longfor.longjian.houseqm.util.CollectionUtil;
import com.longfor.longjian.houseqm.utils.ExampleUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author Houyan
 * @date 2018/12/11 0011 16:01
 */
@Transactional
@Service
@Slf4j
public class HouseQmCheckTaskIssueLogServiceImpl implements HouseQmCheckTaskIssueLogService {
    @Resource
    HouseQmCheckTaskIssueLogMapper houseQmCheckTaskIssueLogMapper;
    @Resource
    private UserInHouseQmCheckTaskMapper userInHouseQmCheckTaskMapper;

    @Override
    @LFAssignDataSource("zhijian2")
    @Transactional
    public int deleteIssueLogByUuids(List<String> uuids) {
        Example example = new Example(HouseQmCheckTaskIssueLog.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("uuid", uuids);
        ExampleUtil.addDeleteAtJudge(example);
        return houseQmCheckTaskIssueLogMapper.deleteByExample(example);
    }

    @Override
    @LFAssignDataSource("zhijian2")
    @Transactional
    public int addBatch(List<HouseQmCheckTaskIssueLog> hIssueLogs) {
        return houseQmCheckTaskIssueLogMapper.insertList(hIssueLogs);
    }

    /**
     * 根据issueUuid 查 取未删除的，并按客户端创建时间升序排序
     *
     * @param issueUuids
     * @return
     */
    @LFAssignDataSource("zhijian2")
    public List<HouseQmCheckTaskIssueLog> searchByIssueUuid(Set<String> issueUuids) {
        List<HouseQmCheckTaskIssueLog> houseQmCheckTaskIssueLogs = houseQmCheckTaskIssueLogMapper.selectByIssueUuid(issueUuids, "false");
        return houseQmCheckTaskIssueLogs;
    }

    @Override
    @LFAssignDataSource("zhijian2")
    public List<HouseQmCheckTaskIssueLog> searchHouseQmCheckTaskIssueLogByMyIdTaskIdLastIdUpdateAtGt(Integer userId, Integer task_id, Integer last_id, Integer timestamp, Integer limit, Integer start, Integer checker) {
        List<Integer> squadIds = new ArrayList<>();
        List<Integer> userIds = new ArrayList<>();
        List<HouseQmCheckTaskIssueLog> items = new ArrayList<>();
        try {
            List<UserInHouseQmCheckTask> rs = userInHouseQmCheckTaskMapper.searchByTaskIdUserIdRoleType(userId, task_id, checker);
            rs.forEach(r -> squadIds.add(r.getSquadId()));

            List<UserInHouseQmCheckTask> res;
            if (squadIds.size() > 0) {
                List<Integer> list = CollectionUtil.removeDuplicate(squadIds);
                res = userInHouseQmCheckTaskMapper.searchBySquadIdIn(list);
            } else {
                res = Lists.newArrayList();
            }
            res.forEach(r -> userIds.add(r.getUserId()));
            List<Integer> userIdList = CollectionUtil.removeDuplicate(userIds);
            if (userIdList.size() == 0) {
                userIdList.add(userId);
            }
            items = houseQmCheckTaskIssueLogMapper.searchHouseQmCheckTaskIssueLogByMyIdTaskIdLastIdUpdateAtGt(userId, userIdList, task_id, last_id, timestamp, start, limit);
        } catch (Exception e) {
            log.error("error:" + e);
        }

        return items;
    }

    /**
     * @param task_id
     * @param uuids
     * @param issueLogUpdateTime
     * @return com.longfor.longjian.houseqm.po.HouseQmCheckTaskIssueLog
     * @author hy
     * @date 2018/12/25 0025
     */
    @Override
    @LFAssignDataSource("zhijian2")
    public HouseQmCheckTaskIssueLog selectIdByTaskIdAndIdAndUuidInAndUpdateAtGtAndNoDeletedOrderById(Integer task_id, List<String> uuids, Date issueLogUpdateTime) {
        Example example = new Example(HouseQmCheckTaskIssueLog.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("taskId", task_id).andGreaterThan("id", 0);
        Example.Criteria criteria1 = example.createCriteria();
        if (uuids.size() > 0) criteria1.andIn("issueUuid", uuids);
        Example.Criteria criteria2 = example.createCriteria();
        criteria2.andGreaterThan("updateAt", issueLogUpdateTime);
        example.and(criteria);
        example.and(criteria1);
        example.and(criteria2);
        return houseQmCheckTaskIssueLogMapper.selectOneByExample(example);
    }

    @Override
    @LFAssignDataSource("zhijian2")
    public List<HouseQmCheckTaskIssueLog> selectByUuidAndNotDelete(String issueUuid) {
        Example example = new Example(HouseQmCheckTaskIssueLog.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("issueUuid", issueUuid);
        criteria.andIsNull("deleteAt");
        return houseQmCheckTaskIssueLogMapper.selectByExample(example);
    }

    @Override
    @LFAssignDataSource("zhijian2")
    public void add(HouseQmCheckTaskIssueLog new_issue_log) {
        houseQmCheckTaskIssueLogMapper.insert(new_issue_log);
    }

    @Override
    @LFAssignDataSource("zhijian2")
    public List<HouseQmCheckTaskIssueLog> selectByIssueUuIdAndStatusNotDel(String issueUuid, ArrayList<Integer> issueLogStatus) {
        Example example = new Example(HouseQmCheckTaskIssueLog.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("issueUuid", issueUuid);
        criteria.andIn("status", issueLogStatus);
        criteria.andIsNull("deleteAt");
        return houseQmCheckTaskIssueLogMapper.selectByExample(example);
    }

    @Override
    @LFAssignDataSource("zhijian2")
    public int selectByIssueUuIdAndStatusNotDelAndCount(String issueUuid, ArrayList<Integer> issueLogStatus) {
        Example example = new Example(HouseQmCheckTaskIssueLog.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("issueUuid", issueUuid);
        criteria.andIn("status", issueLogStatus);
        criteria.andIsNull("deleteAt");
        return houseQmCheckTaskIssueLogMapper.selectCountByExample(example);
    }

    @Override
    @LFAssignDataSource("zhijian2")
    public List<HouseQmCheckTaskIssueLog> selectByUuidsAndNotDelete(List<String> log_uuids) {
        Example example = new Example(HouseQmCheckTaskIssueLog.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("uuid",log_uuids);
        criteria.andIsNull("deleteAt");
        return houseQmCheckTaskIssueLogMapper.selectByExample(example);
    }
}
