package com.longfor.longjian.houseqm.domain.internalService;

import com.longfor.gaia.gfs.data.mybatis.datasource.LFAssignDataSource;
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
@LFAssignDataSource("zhijian2")
public class HouseQmCheckTaskIssueLogService {
    @Resource
    HouseQmCheckTaskIssueLogMapper houseQmCheckTaskIssueLogMapper;

    /**
     * 根据issueUuid 查 取未删除的，并按客户端创建时间升序排序
     *
     * @param issueUuids
     * @return
     */
    public List<HouseQmCheckTaskIssueLog> searchByIssueUuid(Set<String> issueUuids){
        List<HouseQmCheckTaskIssueLog> houseQmCheckTaskIssueLogs = houseQmCheckTaskIssueLogMapper.selectByIssueUuid(issueUuids, "false");
        return houseQmCheckTaskIssueLogs;
    }
}
