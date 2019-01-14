package com.longfor.longjian.houseqm.domain.internalService;

import com.longfor.longjian.houseqm.po.HouseQmCheckTaskIssueUser;

import java.util.ArrayList;
import java.util.List;

public interface HouseQmCheckTaskIssueUserService {

    int insertBatch(List<HouseQmCheckTaskIssueUser> issueUsers);

    List<HouseQmCheckTaskIssueUser> searchByUserIdAndTaskIdAndCreateAt(int userId, int taskId, int timestamp);

    /**
     *
     * @author hy
     * @date 2018/12/25 0025
     * @param
     * @param uid
     * @param task_id
     * @return java.util.List<com.longfor.longjian.houseqm.po.HouseQmCheckTaskIssueUser>
     */
    List<HouseQmCheckTaskIssueUser> selectIssueUUIDByUserIdAndTaskIdAndNoDeleted(Integer uid, Integer task_id);

    /**
     *
     * @author hy
     * @date 2018/12/25 0025
     * @param task_id
     * @return com.longfor.longjian.houseqm.po.HouseQmCheckTaskIssueUser
     */
    List<HouseQmCheckTaskIssueUser> selectUpdateAtByTaskIdAndNoDeletedOrderByUpdateAt(Integer task_id);

    HouseQmCheckTaskIssueUser selectByIssueUUidAndUserIdAndRoleTypeAndNotDel(String uuid, Integer repairerId, Integer value);

    void add(HouseQmCheckTaskIssueUser repairerUserInfos);

    void update(HouseQmCheckTaskIssueUser repairerUserInfo);

    List<HouseQmCheckTaskIssueUser> selectByRoleTypeAndUserIdAndIssueUuid(Integer value, ArrayList<Integer> intFollowers, String uuid);

    void insertMany(ArrayList<HouseQmCheckTaskIssueUser> insertData);
}
