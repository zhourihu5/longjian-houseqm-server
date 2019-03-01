package com.longfor.longjian.houseqm.domain.internalService;

import com.longfor.longjian.houseqm.po.zhijian2_notify.StatScanRecord;

import java.util.List;

/**
 * Created by Wang on 2019/3/1.
 */
public interface StatScanRecordService {

    List<StatScanRecord> findByExample(Integer moduleId);

}
