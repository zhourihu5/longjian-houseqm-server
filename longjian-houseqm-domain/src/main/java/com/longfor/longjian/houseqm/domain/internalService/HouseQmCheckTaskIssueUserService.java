package com.longfor.longjian.houseqm.domain.internalService;

import com.longfor.longjian.houseqm.po.HouseQmCheckTaskIssueUser;

import java.util.List;

public interface HouseQmCheckTaskIssueUserService {

    List<HouseQmCheckTaskIssueUser> searchByUserIdAndTaskIdAndCreateAt(int userId, int taskId, int timestamp);

}
