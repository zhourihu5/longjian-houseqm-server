package com.longfor.longjian.houseqm.domain.internalService;

import com.longfor.longjian.houseqm.dao.HouseQmCheckTaskIssueUserMapper;
import com.longfor.longjian.houseqm.po.HouseQmCheckTaskIssue;
import com.longfor.longjian.houseqm.po.HouseQmCheckTaskIssueUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

/**
 * @author houyan
 * @date 2018/12/11 0011 14:00
 */
@Service
@Slf4j
public class HouseQmCheckTaskIssueUserService {

    @Resource
    HouseQmCheckTaskIssueUserMapper houseQmCheckTaskIssueUserMapper;

    public List<HouseQmCheckTaskIssueUser> searchByUserIdAndTaskIdAndAndCreateAt(int userId, int taskId,int timestamp){

        return null;
    }

}
