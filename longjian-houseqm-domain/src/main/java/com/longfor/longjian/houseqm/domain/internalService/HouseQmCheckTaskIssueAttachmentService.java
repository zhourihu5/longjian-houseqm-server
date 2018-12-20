package com.longfor.longjian.houseqm.domain.internalService;

import com.longfor.longjian.houseqm.po.HouseQmCheckTaskIssueAttachment;

import java.util.List;
import java.util.Set;

public interface HouseQmCheckTaskIssueAttachmentService {

    List<HouseQmCheckTaskIssueAttachment> searchByIssueUuid(Set<String> issueUuids);

}
