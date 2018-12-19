package com.longfor.longjian.houseqm.domain.internalService.impl;

import com.longfor.gaia.gfs.data.mybatis.datasource.LFAssignDataSource;
import com.longfor.longjian.houseqm.dao.HouseQmCheckTaskIssueAttachmentMapper;
import com.longfor.longjian.houseqm.domain.internalService.HouseQmCheckTaskIssueAttachmentService;
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
public class HouseQmCheckTaskIssueAttachmentServiceImpl implements HouseQmCheckTaskIssueAttachmentService {

    @Resource
    HouseQmCheckTaskIssueAttachmentMapper houseQmCheckTaskIssueAttachmentMapper;

    /**
     * 根据问题uuid 查 取 未删除的 数据
     * @param issueUuids
     * @return
     */
    @LFAssignDataSource(value = "zhijian2")
    public List<HouseQmCheckTaskIssueAttachment> searchByIssueUuid(Set<String> issueUuids){
        List<HouseQmCheckTaskIssueAttachment> houseQmCheckTaskIssueAttachments = houseQmCheckTaskIssueAttachmentMapper.selectByIssueUuid(issueUuids, "false");
        return houseQmCheckTaskIssueAttachments;
    }
}
