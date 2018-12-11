package com.longfor.longjian.houseqm.domain.internalService;

import com.longfor.longjian.houseqm.dao.HouseQmCheckTaskIssueAttachmentMapper;
import com.longfor.longjian.houseqm.po.HouseQmCheckTaskIssueAttachment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

/**
 * @author Houyan
 * @date 2018/12/11 0011 19:38
 */
@Service
@Slf4j
public class HouseQmCheckTaskIssueAttachmentService {

    @Resource
    HouseQmCheckTaskIssueAttachmentMapper houseQmCheckTaskIssueAttachmentMapper;

    public List<HouseQmCheckTaskIssueAttachment> searchByIssueUuid(Set<String> issueUuids){

        return null;
    }
}
