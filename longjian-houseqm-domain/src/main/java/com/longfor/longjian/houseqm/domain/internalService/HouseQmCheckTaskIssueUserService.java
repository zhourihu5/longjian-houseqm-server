package com.longfor.longjian.houseqm.domain.internalService;

import com.longfor.longjian.houseqm.po.HouseQmCheckTaskIssueUser;

import java.util.List;

public interface HouseQmCheckTaskIssueUserService {

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

}
