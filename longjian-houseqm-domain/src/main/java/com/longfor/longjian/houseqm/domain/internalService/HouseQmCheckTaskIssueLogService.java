package com.longfor.longjian.houseqm.domain.internalService;

import com.longfor.longjian.houseqm.dao.HouseQmCheckTaskIssueLogMapper;
import com.longfor.longjian.houseqm.po.HouseQmCheckTaskIssueAttachment;
import com.longfor.longjian.houseqm.po.HouseQmCheckTaskIssueLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

/**
 * @author Houyan
 * @date 2018/12/11 0011 16:01
 */
@Service
@Slf4j
public class HouseQmCheckTaskIssueLogService {
    @Resource
    HouseQmCheckTaskIssueLogMapper houseQmCheckTaskIssueLogMapper;

    public List<HouseQmCheckTaskIssueLog> searchByIssueUuid(Set<String> issueUuids){

        return null;
    }
}
