package com.longfor.longjian.houseqm.domain.internalService;

import com.longfor.longjian.houseqm.po.zj2db.HouseQmCheckTaskIssueUser;

import java.util.ArrayList;
import java.util.List;

public interface HouseQmCheckTaskIssueUserService {

    int insertBatch(List<HouseQmCheckTaskIssueUser> issueUsers);

    List<HouseQmCheckTaskIssueUser> searchByUserIdAndTaskIdAndCreateAt(int userId, int taskId, int timestamp);

    List<HouseQmCheckTaskIssueUser> selectIssueUUIDByUserIdAndTaskIdAndNoDeleted(Integer uid, Integer taskId);

    HouseQmCheckTaskIssueUser selectUpdateAtByTaskIdAndNoDeletedOrderByUpdateAt(Integer taskId);

    HouseQmCheckTaskIssueUser selectByIssueUUidAndUserIdAndRoleTypeAndNotDel(String uuid, Integer repairerId, Integer value);

    void add(HouseQmCheckTaskIssueUser repairerUserInfos);

    void update(HouseQmCheckTaskIssueUser repairerUserInfo);

    List<HouseQmCheckTaskIssueUser> selectByRoleTypeAndUserIdAndIssueUuid(Integer value, ArrayList<Integer> intFollowers, String uuid);

    void insertMany(ArrayList<HouseQmCheckTaskIssueUser> insertData);
}
