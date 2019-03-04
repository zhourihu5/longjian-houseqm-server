package com.longfor.longjian.houseqm.domain.internalService.impl;

import java.util.*;

import com.longfor.gaia.gfs.data.mybatis.datasource.LFAssignDataSource;
import com.longfor.longjian.houseqm.dao.zj2db.HouseQmCheckTaskNotifyRecordMapper;
import com.longfor.longjian.houseqm.domain.internalService.HouseQmCheckTaskNotifyRecordService;
import com.longfor.longjian.houseqm.po.zhijian2_notify.StatScanRecord;
import com.longfor.longjian.houseqm.po.zj2db.HouseQmCheckTaskNotifyRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

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
        record.setCreateAt(new Date());
        record.setUpdateAt(new Date());
        return houseQmCheckTaskNotifyRecordMapper.insert(record);
    }
    @Transactional
    @Override
    @LFAssignDataSource("zhijian2")
    public void addMany(ArrayList<HouseQmCheckTaskNotifyRecord> dataSource) {
        for (HouseQmCheckTaskNotifyRecord record : dataSource) {
            record.setCreateAt(new Date());
            record.setUpdateAt(new Date());
        }
        houseQmCheckTaskNotifyRecordMapper.insertList(dataSource);
    }

    @Override
    @LFAssignDataSource("zhijian2")
    public List<HouseQmCheckTaskNotifyRecord> findExample(Map<String,Object> map,List<Integer>statusList) {

        Example example = new Example(HouseQmCheckTaskNotifyRecord.class);
        example.createCriteria()
                .andEqualTo("moduleId",map.get("moduleId"))
                .andIn("issueStatus", statusList)
                .andGreaterThan("createAt",map.get("statBeg"))
                .andLessThanOrEqualTo("createAt",map.get("statEnd"))
                .andIsNull("deleteAt");

        return houseQmCheckTaskNotifyRecordMapper.selectByExample(example);
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
