package com.longfor.longjian.houseqm.domain.internalservice.impl;

import com.longfor.gaia.gfs.data.mybatis.datasource.LFAssignDataSource;
import com.longfor.longjian.houseqm.dao.zhijian2_notify.StatScanRecordMapper;
import com.longfor.longjian.houseqm.domain.internalservice.StatScanRecordService;
import com.longfor.longjian.houseqm.po.zhijian2_notify.StatScanRecord;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Wang on 2019/3/1.
 */
@Slf4j
@Service
public class StatScanRecordServiceImpl implements StatScanRecordService {


    @Resource
    private StatScanRecordMapper statScanRecordMapper;

    @Override
    @LFAssignDataSource("zhijian2_notify")
    public StatScanRecord findByExample(Integer moduleId) {
        StatScanRecord statScanRecord=null;
        Example example = new Example(StatScanRecord.class);
        example.createCriteria()
                .andEqualTo("moduleId",moduleId);
        List<StatScanRecord>statScanRecordList=statScanRecordMapper.selectByExample(example);
        if(CollectionUtils.isNotEmpty(statScanRecordList)){
            statScanRecord=statScanRecordList.get(0);
        }
        return statScanRecord;
    }

    @Override
    @LFAssignDataSource("zhijian2_notify")
    public void update(StatScanRecord statScanRecord) {
        statScanRecordMapper.updateByPrimaryKey(statScanRecord);
    }

    @Override
    @LFAssignDataSource("zhijian2_notify")
    public void add(StatScanRecord statScanRecord) {
        statScanRecordMapper.insertSelective(statScanRecord);
    }
}
