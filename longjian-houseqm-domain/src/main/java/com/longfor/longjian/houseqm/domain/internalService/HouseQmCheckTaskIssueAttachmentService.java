package com.longfor.longjian.houseqm.domain.internalService;

import com.longfor.longjian.houseqm.po.zj2db.HouseQmCheckTaskIssueAttachment;

import java.util.List;
import java.util.Set;

public interface HouseQmCheckTaskIssueAttachmentService {

    List<HouseQmCheckTaskIssueAttachment> searchByIssueUuid(Set<String> issueUuids);

    int inseretBatch(List<HouseQmCheckTaskIssueAttachment> attachements);

    int deleteByIssueUuidMd5(String issueUuid, String md5);

    List<HouseQmCheckTaskIssueAttachment> selectByissueUuidAnduserIdAndpublicTypeAndattachmentTypeAndNotDel(String issueUuid,Integer uid, Integer value, Integer value1);

    List<HouseQmCheckTaskIssueAttachment> selectByIssueUuidAndpublicTypeAndattachmentTypeAndNotDel(String issueUuid, Integer value, Integer value1);

    int add(HouseQmCheckTaskIssueAttachment value);


    HouseQmCheckTaskIssueAttachment selectByMd5AndNotDel(Object attachment);
}
