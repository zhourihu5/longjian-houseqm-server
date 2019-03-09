package com.longfor.longjian.houseqm.domain.internalservice.impl;

import com.longfor.gaia.gfs.data.mybatis.datasource.LFAssignDataSource;
import com.longfor.longjian.houseqm.dao.zj2db.ExportFileRecordMapper;
import com.longfor.longjian.houseqm.domain.internalservice.ExportFileRecordService;
import com.longfor.longjian.houseqm.po.zj2db.ExportFileRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@Slf4j
public class ExportFileRecordServiceImpl implements ExportFileRecordService {


    @Resource
    private ExportFileRecordMapper exportFileRecordMapper;

    @Override
    @LFAssignDataSource("zhijian2")
    @Transactional
    public ExportFileRecord insertFull(ExportFileRecord item) {
        exportFileRecordMapper.insertSelective(item);
        return item;
    }
}
