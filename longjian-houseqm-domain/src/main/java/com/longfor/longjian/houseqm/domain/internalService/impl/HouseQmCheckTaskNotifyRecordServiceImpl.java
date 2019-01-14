package com.longfor.longjian.houseqm.domain.internalService.impl;

import com.longfor.gaia.gfs.data.mybatis.datasource.LFAssignDataSource;
import com.longfor.longjian.houseqm.dao.HouseQmCheckTaskNotifyRecordMapper;
import com.longfor.longjian.houseqm.domain.internalService.HouseQmCheckTaskNotifyRecordService;
import com.longfor.longjian.houseqm.po.HouseQmCheckTaskNotifyRecord;
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
    @Override
    @LFAssignDataSource("zhijian2")
    public int add(HouseQmCheckTaskNotifyRecord record) {
        return houseQmCheckTaskNotifyRecordMapper.insert(record);
    }
}
