package com.longfor.longjian.houseqm.domain.internalService.impl;

import com.longfor.gaia.gfs.data.mybatis.datasource.LFAssignDataSource;
import com.longfor.longjian.houseqm.dao.HouseQmCheckTaskIssueLogMapper;
import com.longfor.longjian.houseqm.dao.UserInHouseQmCheckTaskMapper;
import com.longfor.longjian.houseqm.domain.internalService.IHouseQmCheckTaskIssueLogService;
import com.longfor.longjian.houseqm.po.HouseQmCheckTaskIssueLog;
import com.longfor.longjian.houseqm.po.UserInHouseQmCheckTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
@Slf4j
public class HouseQmCheckTaskIssueLogServiceImpl implements IHouseQmCheckTaskIssueLogService {
    @Autowired
    private UserInHouseQmCheckTaskMapper userInHouseQmCheckTaskMapper;
    @Autowired
    private HouseQmCheckTaskIssueLogMapper houseQmCheckTaskIssueLogMapper;
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
}
