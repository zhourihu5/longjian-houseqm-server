package com.longfor.longjian.houseqm.domain.internalService.impl;

import com.longfor.gaia.gfs.data.mybatis.datasource.LFAssignDataSource;
import com.longfor.longjian.houseqm.dao.zhijian2_notify.NoticeStatRecordMapper;
import com.longfor.longjian.houseqm.domain.internalService.NoticeStatRecordService;
import com.longfor.longjian.houseqm.po.zhijian2_notify.NoticeStatRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * Created by Wang on 2019/3/1.
 */
@Slf4j
@Service
public class NoticeStatRecordServiceImpl implements NoticeStatRecordService {

    @Resource
    private NoticeStatRecordMapper noticeStatRecordMapper;

    @Override
    @LFAssignDataSource("zhijian2_notify")
    public NoticeStatRecord add(NoticeStatRecord noticeStatRecord) {
        noticeStatRecord.setCreateAt(new Date());
        noticeStatRecord.setUpdateAt(new Date());
        noticeStatRecordMapper.insert(noticeStatRecord);
        return noticeStatRecord;
    }
}
