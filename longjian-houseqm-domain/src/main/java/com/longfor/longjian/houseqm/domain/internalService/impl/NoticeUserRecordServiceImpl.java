package com.longfor.longjian.houseqm.domain.internalService.impl;


import com.longfor.gaia.gfs.data.mybatis.datasource.LFAssignDataSource;
import com.longfor.longjian.houseqm.dao.zhijian2_notify.NoticeUserRecordMapper;
import com.longfor.longjian.houseqm.domain.internalService.NoticeUserRecordService;
import com.longfor.longjian.houseqm.po.zhijian2_notify.NoticeUserRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by Wang on 2019/3/1.
 */
@Slf4j
@Service
public class NoticeUserRecordServiceImpl implements NoticeUserRecordService {

    @Resource
    private NoticeUserRecordMapper noticeUserRecordMapper;

    @Override
    @LFAssignDataSource("zhijian2_notify")
    public void add(NoticeUserRecord noticeUserRecord) {
        noticeUserRecordMapper.insertSelective(noticeUserRecord);
    }
}
