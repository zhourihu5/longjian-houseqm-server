package com.longfor.longjian.houseqm.domain.internalService;

import com.longfor.longjian.houseqm.po.HouseQmCheckTaskIssueLog;

import java.util.List;
import java.util.Set;

public interface HouseQmCheckTaskIssueLogService {

    List<HouseQmCheckTaskIssueLog> searchByIssueUuid(Set<String> issueUuids);

}
