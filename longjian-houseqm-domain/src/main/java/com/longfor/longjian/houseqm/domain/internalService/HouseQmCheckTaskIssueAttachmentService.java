package com.longfor.longjian.houseqm.domain.internalService;

import com.longfor.longjian.houseqm.po.HouseQmCheckTaskIssueAttachment;

import java.util.List;
import java.util.Set;

public interface HouseQmCheckTaskIssueAttachmentService {

    List<HouseQmCheckTaskIssueAttachment> searchByIssueUuid(Set<String> issueUuids);

    List<HouseQmCheckTaskIssueAttachment> selectByissueUuidAnduserIdAndpublicTypeAndattachmentTypeAndNotDel(String issueUuid,Integer uid, Integer value, Integer value1);

    List<HouseQmCheckTaskIssueAttachment> selectByIssueUuidAndpublicTypeAndattachmentTypeAndNotDel(String issueUuid, Integer value, Integer value1);
}
