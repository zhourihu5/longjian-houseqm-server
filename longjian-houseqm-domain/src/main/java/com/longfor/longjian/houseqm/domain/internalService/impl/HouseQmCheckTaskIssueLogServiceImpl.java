package com.longfor.longjian.houseqm.domain.internalService.impl;

import com.longfor.gaia.gfs.data.mybatis.datasource.LFAssignDataSource;
import com.longfor.longjian.houseqm.dao.HouseQmCheckTaskIssueLogMapper;
import com.longfor.longjian.houseqm.dao.UserInHouseQmCheckTaskMapper;
import com.longfor.longjian.houseqm.domain.internalService.HouseQmCheckTaskIssueLogService;
import com.longfor.longjian.houseqm.po.HouseQmCheckTaskIssueLog;
import com.longfor.longjian.houseqm.po.UserInHouseQmCheckTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author Houyan
 * @date 2018/12/11 0011 16:01
 */
@Service
@Slf4j
public class HouseQmCheckTaskIssueLogServiceImpl implements HouseQmCheckTaskIssueLogService {
    @Resource
    HouseQmCheckTaskIssueLogMapper houseQmCheckTaskIssueLogMapper;
    @Resource
    private UserInHouseQmCheckTaskMapper userInHouseQmCheckTaskMapper;
    /**
     * 根据issueUuid 查 取未删除的，并按客户端创建时间升序排序
     *
     * @param issueUuids
     * @return
     */
    @LFAssignDataSource("zhijian2")
    public List<HouseQmCheckTaskIssueLog> searchByIssueUuid(Set<String> issueUuids){
        List<HouseQmCheckTaskIssueLog> houseQmCheckTaskIssueLogs = houseQmCheckTaskIssueLogMapper.selectByIssueUuid(issueUuids, "false");
        return houseQmCheckTaskIssueLogs;
    }
    @Override
    @LFAssignDataSource("zhijian2")
    public List<HouseQmCheckTaskIssueLog> searchHouseQmCheckTaskIssueLogByMyIdTaskIdLastIdUpdateAtGt(Integer userId, Integer task_id, Integer last_id, Integer timestamp, Integer limit,Integer start,Integer checker) {
        List<Integer> squadIds = new ArrayList<>();
        List<Integer> userIds = new ArrayList<>();
        List<HouseQmCheckTaskIssueLog>  houseQmCheckTaskIssueLogs = new ArrayList<>();
        try {
            List<UserInHouseQmCheckTask> userInHouseQmCheckTasks=userInHouseQmCheckTaskMapper.searchByTaskIdUserIdRoleType(userId,task_id,checker);
            userInHouseQmCheckTasks.forEach(userInHouseQmCheckTask -> {
                squadIds.add(userInHouseQmCheckTask.getSquadId());
            });
            List<UserInHouseQmCheckTask>  userInHouseQmCheckTaskSearchSquadIdsList=userInHouseQmCheckTaskMapper.searchBySquadIdIn(squadIds);
            userInHouseQmCheckTaskSearchSquadIdsList.forEach(userInHouseQmCheckTask -> {
                userIds.add(userInHouseQmCheckTask.getUserId());
            });
            if(userIds.size()==0){
                userIds.add(userId);
            }
            houseQmCheckTaskIssueLogs=houseQmCheckTaskIssueLogMapper.searchHouseQmCheckTaskIssueLogByMyIdTaskIdLastIdUpdateAtGt(userId,userIds,task_id,last_id,timestamp,start,limit);
           /* houseQmCheckTaskIssueLogs.forEach(houseQmCheckTaskIssueLog -> {
                System.out.println(houseQmCheckTaskIssueLog.getId());
            });*/
        }catch (Exception e){
            log.error("error:"+e);
        }

        return houseQmCheckTaskIssueLogs;
    }

    /**
     *
     * @author hy
     * @date 2018/12/25 0025
     * @param task_id
     * @param uuids
     * @param issueLogUpdateTime
     * @return com.longfor.longjian.houseqm.po.HouseQmCheckTaskIssueLog
     */
    @Override
    @LFAssignDataSource("zhijian2")
    public List<HouseQmCheckTaskIssueLog> selectIdByTaskIdAndIdAndUuidInAndUpdateAtGtAndNoDeletedOrderById(Integer task_id, List<String> uuids, Date issueLogUpdateTime) {
        return houseQmCheckTaskIssueLogMapper.selectIdByTaskIdAndIdAndUuidInAndUpdateAtGtAndNoDeletedOrderById(task_id,uuids,issueLogUpdateTime,"false");
    }
}
