package com.longfor.longjian.houseqm.domain.internalService.impl.test;

import com.longfor.gaia.gfs.data.mybatis.datasource.LFAssignDataSource;
import com.longfor.longjian.houseqm.dao.zhijian2_notify.GcglIssueStatRecordMapper;
import com.longfor.longjian.houseqm.domain.internalservice.GcglIssueStatRecordService;
import com.longfor.longjian.houseqm.po.zhijian2_notify.GcglIssueStatRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by Wang on 2019/3/1.
 */
@Slf4j
@Service
public class GcglIssueStatRecordServiceImpl implements GcglIssueStatRecordService {

    @Resource
    private GcglIssueStatRecordMapper gcglIssueStatRecordMapper;

    @Override
    @LFAssignDataSource("zhijian2_notify")
    public void add(GcglIssueStatRecord gcglIssueStatRecord) {
        gcglIssueStatRecordMapper.insertSelective(gcglIssueStatRecord);
    }
}
