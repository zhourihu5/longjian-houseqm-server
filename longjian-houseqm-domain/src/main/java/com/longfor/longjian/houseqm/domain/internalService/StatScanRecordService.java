package com.longfor.longjian.houseqm.domain.internalService;

import com.longfor.longjian.houseqm.po.zhijian2_notify.StatScanRecord;


/**
 * Created by Wang on 2019/3/1.
 */
public interface StatScanRecordService {

    StatScanRecord findByExample(Integer moduleId);

    void update(StatScanRecord statScanRecord);

    void add(StatScanRecord statScanRecord);

}
