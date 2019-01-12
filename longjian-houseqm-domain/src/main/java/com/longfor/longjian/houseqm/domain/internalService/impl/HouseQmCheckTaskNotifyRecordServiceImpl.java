package com.longfor.longjian.houseqm.domain.internalService.impl;
import java.util.Date;

import com.longfor.gaia.gfs.data.mybatis.datasource.LFAssignDataSource;
import com.longfor.longjian.houseqm.dao.HouseQmCheckTaskNotifyRecordMapper;
import com.longfor.longjian.houseqm.domain.internalService.HouseQmCheckTaskNotifyRecordService;
import com.longfor.longjian.houseqm.po.HouseQmCheckTaskNotifyRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.domain.internalService.impl
 * @ClassName: HouseQmCheckTaskNotifyRecordServiceImpl
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/1/12 15:52
 */
@Service
@Slf4j
public class HouseQmCheckTaskNotifyRecordServiceImpl implements HouseQmCheckTaskNotifyRecordService {
    @Resource
    private HouseQmCheckTaskNotifyRecordMapper houseQmCheckTaskNotifyRecordMapper;

    @Override
    @LFAssignDataSource("zhijian2")
    @Transactional
    public int insertFull(Integer projectId, Integer taskId, Integer srcUserId, String desUserIds, int moduleId, Integer issueId, Integer issueStatus, String extraInfo) {
        HouseQmCheckTaskNotifyRecord item = new HouseQmCheckTaskNotifyRecord();
        item.setProjectId(projectId);
        item.setTaskId(taskId);
        item.setSrcUserId(srcUserId);
        item.setDesUserIds(desUserIds);
        item.setModuleId(moduleId);
        item.setIssueId(issueId);
        item.setIssueStatus(issueStatus);
        item.setExtraInfo(extraInfo);
        item.setCreateAt(new Date());
        item.setUpdateAt(new Date());

        return houseQmCheckTaskNotifyRecordMapper.insertSelective(item);
    }
}
