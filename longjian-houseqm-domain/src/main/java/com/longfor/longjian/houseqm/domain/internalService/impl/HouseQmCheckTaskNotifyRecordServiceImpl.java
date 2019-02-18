package com.longfor.longjian.houseqm.domain.internalService.impl;

import java.util.ArrayList;
import java.util.Date;

import com.longfor.gaia.gfs.data.mybatis.datasource.LFAssignDataSource;
import com.longfor.longjian.houseqm.dao.zj2db.HouseQmCheckTaskNotifyRecordMapper;
import com.longfor.longjian.houseqm.domain.internalService.HouseQmCheckTaskNotifyRecordService;
import com.longfor.longjian.houseqm.po.zj2db.HouseQmCheckTaskNotifyRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * Created by Dongshun on 2019/1/11.
 */
@Transactional
@Service
@Slf4j
public class HouseQmCheckTaskNotifyRecordServiceImpl implements HouseQmCheckTaskNotifyRecordService {
    @Resource
    HouseQmCheckTaskNotifyRecordMapper houseQmCheckTaskNotifyRecordMapper;
    @Transactional
    @Override
    @LFAssignDataSource("zhijian2")
    public int add(HouseQmCheckTaskNotifyRecord record) {
        return houseQmCheckTaskNotifyRecordMapper.insert(record);
    }
    @Transactional
    @Override
    @LFAssignDataSource("zhijian2")
    public void addMany(ArrayList<HouseQmCheckTaskNotifyRecord> dataSource) {
        houseQmCheckTaskNotifyRecordMapper.insertList(dataSource);
    }

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
