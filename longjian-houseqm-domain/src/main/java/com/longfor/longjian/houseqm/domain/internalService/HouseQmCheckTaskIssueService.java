package com.longfor.longjian.houseqm.domain.internalService;

import com.longfor.longjian.houseqm.dao.HouseQmCheckTaskIssueMapper;
import com.longfor.longjian.houseqm.po.HouseQmCheckTaskIssue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

/**
 * @author Houyan
 * @date 2018/12/11 0011 15:39
 */
@Service
@Slf4j
public class HouseQmCheckTaskIssueService {
    @Resource
    HouseQmCheckTaskIssueMapper houseQmCheckTaskIssueMapper;

    public List<HouseQmCheckTaskIssue> searchByIssueUuidsAndclientCreateAt(Set<String> issueUuids,int timestamp){

        return null;
    }

}
