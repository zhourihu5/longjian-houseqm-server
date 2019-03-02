package com.longfor.longjian.houseqm.domain.internalservice.impl;

import com.longfor.longjian.houseqm.dao.zhijian2_notify.StatScanRecordMapper;
import com.longfor.longjian.houseqm.domain.internalservice.StatScanRecordService;
import com.longfor.longjian.houseqm.po.zhijian2_notify.StatScanRecord;
import lombok.extern.slf4j.Slf4j;
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
    public List<StatScanRecord> findByExample(Integer moduleId) {
        Example example = new Example(StatScanRecord.class);
        example.createCriteria()
                .andEqualTo("moduleId", moduleId)
                .andEqualTo("selectd", 1);
        return statScanRecordMapper.selectByExample(example);
    }
}
