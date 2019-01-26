package com.longfor.longjian.houseqm.domain.internalService;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.domain.internalService
 * @ClassName: HouseQmCheckTaskNotifyRecordService
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/1/12 15:52
 */

import com.longfor.longjian.houseqm.po.HouseQmCheckTaskNotifyRecord;

import java.util.ArrayList;

/**
 * Created by Dongshun on 2019/1/11.
 */
public interface HouseQmCheckTaskNotifyRecordService {
    int insertFull(Integer projectId, Integer taskId, Integer srcUserId, String desUserIds, int moduleId, Integer issueId, Integer issueStatus, String extraInfo);

    int add(HouseQmCheckTaskNotifyRecord record);

    void addMany(ArrayList<HouseQmCheckTaskNotifyRecord> dataSource);
}
