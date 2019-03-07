package com.longfor.longjian.houseqm.domain.internalservice.impl.test;

import com.google.common.collect.Lists;
import com.longfor.gaia.gfs.data.mybatis.datasource.LFAssignDataSource;
import com.longfor.longjian.houseqm.dao.zj2db.HouseQmCheckTaskIssueLogMapper;
import com.longfor.longjian.houseqm.dao.zj2db.UserInHouseQmCheckTaskMapper;
import com.longfor.longjian.houseqm.domain.internalservice.HouseQmCheckTaskIssueLogService;
import com.longfor.longjian.houseqm.po.zj2db.HouseQmCheckTaskIssueLog;
import com.longfor.longjian.houseqm.po.zj2db.UserInHouseQmCheckTask;
import com.longfor.longjian.houseqm.utils.ExampleUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.session.RowBounds;
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
    private static final String STATUS="status";
    private static final String DELETE_AT="deleteAt";
    private static final String ISSUE_UUID= "issueUuid";

    @Override
    @LFAssignDataSource("zhijian2")
    @Transactional
    public int deleteIssueLogByUuids(List<String> uuids) {
        Example example = new Example(HouseQmCheckTaskIssueLog.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("uuid", uuids);
        ExampleUtil.addDeleteAtJudge(example);
        List<HouseQmCheckTaskIssueLog> issueLogs = houseQmCheckTaskIssueLogMapper.selectByExample(example);
        for (HouseQmCheckTaskIssueLog issueLog : issueLogs) {
            issueLog.setDeleteAt(new Date());
            houseQmCheckTaskIssueLogMapper.updateByPrimaryKeySelective(issueLog);
        }
        return issueLogs.size();
    }

    @Override
    @LFAssignDataSource("zhijian2")
    @Transactional
    public int addBatch(List<HouseQmCheckTaskIssueLog> hIssueLogs) {
        for (HouseQmCheckTaskIssueLog issueLog : hIssueLogs) {
            issueLog.setCreateAt(new Date());
            issueLog.setUpdateAt(new Date());
            issueLog.setClientCreateAt(new Date());
            houseQmCheckTaskIssueLogMapper.insertSelective(issueLog);
        }
        return hIssueLogs.size();
    }

    /**
     * 根据issueUuid 查 取未删除的，并按客户端创建时间升序排序
     *
     * @param issueUuids
     * @return
     */
    @LFAssignDataSource("zhijian2")
    public List<HouseQmCheckTaskIssueLog> searchByIssueUuid(Set<String> issueUuids) {
        if (CollectionUtils.isEmpty(issueUuids)) return Lists.newArrayList();
        return houseQmCheckTaskIssueLogMapper.selectByIssueUuid(issueUuids, "false");
    }

    @Override
    @LFAssignDataSource("zhijian2")
    public List<HouseQmCheckTaskIssueLog> searchHouseQmCheckTaskIssueLogByMyIdTaskIdLastIdUpdateAtGt(Integer userId, Integer taskId, Integer lastId, Integer timestamp, Integer limit, Integer start, Integer checker) {
        List<Integer> squadIds = new ArrayList<>();
        List<Integer> userIds = new ArrayList<>();
        List<HouseQmCheckTaskIssueLog> houseQmCheckTaskIssueLogs = new ArrayList<>();
        try {
            List<UserInHouseQmCheckTask> userInHouseQmCheckTasks = userInHouseQmCheckTaskMapper.searchByTaskIdUserIdRoleType(userId, taskId, checker);
            userInHouseQmCheckTasks.forEach(userInHouseQmCheckTask -> squadIds.add(userInHouseQmCheckTask.getSquadId()));
            List<UserInHouseQmCheckTask> userInHouseQmCheckTaskSearchSquadIdsList = Lists.newArrayList();
            if (!squadIds.isEmpty())
                userInHouseQmCheckTaskSearchSquadIdsList = userInHouseQmCheckTaskMapper.searchBySquadIdIn(squadIds);
            userInHouseQmCheckTaskSearchSquadIdsList.forEach(userInHouseQmCheckTask -> userIds.add(userInHouseQmCheckTask.getUserId()));
            if (CollectionUtils.isEmpty(userIds)) userIds.add(userId);
            houseQmCheckTaskIssueLogs = houseQmCheckTaskIssueLogMapper.searchHouseQmCheckTaskIssueLogByMyIdTaskIdLastIdUpdateAtGt(userId, userIds, taskId, lastId, timestamp, start, limit);
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return houseQmCheckTaskIssueLogs;
    }

    @Override
    @LFAssignDataSource("zhijian2")
    public HouseQmCheckTaskIssueLog selectIdByTaskIdAndIdAndUuidInAndUpdateAtGtAndNoDeletedOrderById(Integer taskId, List<String> uuids, Date issueLogUpdateTime) {
        Example example = new Example(HouseQmCheckTaskIssueLog.class);

        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("taskId", taskId).andGreaterThan("id", 0);

        Example.Criteria criteria1 = example.createCriteria();
        if (CollectionUtils.isNotEmpty(uuids)) criteria1.andIn(ISSUE_UUID, uuids);

        Example.Criteria criteria2 = example.createCriteria();
        criteria2.andGreaterThan("updateAt", issueLogUpdateTime);

        example.and(criteria1);
        example.and(criteria2);
        ExampleUtil.addDeleteAtJudge(example);
        example.orderBy("id").desc();
        //限制 返回值数据条数
        RowBounds rowBounds = new RowBounds(0, 2);
        List<HouseQmCheckTaskIssueLog> result = houseQmCheckTaskIssueLogMapper.selectByExampleAndRowBounds(example, rowBounds);
        if (CollectionUtils.isEmpty(result)) return null;
        else return result.get(0);
    }

    @Override
    @LFAssignDataSource("zhijian2")
    public List<HouseQmCheckTaskIssueLog> selectByUuidAndNotDelete(String issueUuid) {
        Example example = new Example(HouseQmCheckTaskIssueLog.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo(ISSUE_UUID, issueUuid).andIsNull(DELETE_AT);
        return houseQmCheckTaskIssueLogMapper.selectByExample(example);
    }

    @Transactional
    @Override
    @LFAssignDataSource("zhijian2")
    public void add(HouseQmCheckTaskIssueLog newIssueLog) {
        newIssueLog.setClientCreateAt(new Date());
        newIssueLog.setUpdateAt(new Date());
        newIssueLog.setCreateAt(new Date());
        houseQmCheckTaskIssueLogMapper.insert(newIssueLog);
    }

    @Override
    @LFAssignDataSource("zhijian2")
    public List<HouseQmCheckTaskIssueLog> selectByIssueUuIdAndStatusNotDel(String issueUuid, ArrayList<Integer> issueLogStatus) {
        Example example = new Example(HouseQmCheckTaskIssueLog.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo(ISSUE_UUID, issueUuid);
        criteria.andIn(STATUS, issueLogStatus);
        criteria.andIsNull(DELETE_AT);
        return houseQmCheckTaskIssueLogMapper.selectByExample(example);
    }

    @Override
    @LFAssignDataSource("zhijian2")
    public int selectByIssueUuIdAndStatusNotDelAndCount(String issueUuid, ArrayList<Integer> issueLogStatus) {
        Example example = new Example(HouseQmCheckTaskIssueLog.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo(ISSUE_UUID, issueUuid);
        criteria.andIn(STATUS, issueLogStatus);
        criteria.andIsNull(DELETE_AT);
        return houseQmCheckTaskIssueLogMapper.selectCountByExample(example);
    }

    @Override
    @LFAssignDataSource("zhijian2")
    public List<HouseQmCheckTaskIssueLog> selectByUuidsAndNotDelete(List<String> logUuids) {
        Example example = new Example(HouseQmCheckTaskIssueLog.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("uuid", logUuids);
        criteria.andIsNull(DELETE_AT);
        return houseQmCheckTaskIssueLogMapper.selectByExample(example);
    }

    @Override
    @LFAssignDataSource("zhijian2")
    public List<HouseQmCheckTaskIssueLog> selectByIssueUuIdInAndStatus(List<String> issueUuids, Integer status) {
        Example example = new Example(HouseQmCheckTaskIssueLog.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn(ISSUE_UUID, issueUuids);
        criteria.andEqualTo(STATUS, status);
        ExampleUtil.addDeleteAtJudge(example);
        example.orderBy("clientCreateAt").asc();
        return houseQmCheckTaskIssueLogMapper.selectByExample(example);

    }
}
