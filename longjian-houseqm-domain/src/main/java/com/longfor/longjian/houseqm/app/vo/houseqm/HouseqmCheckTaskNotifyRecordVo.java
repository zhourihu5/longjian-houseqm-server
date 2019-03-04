package com.longfor.longjian.houseqm.app.vo.houseqm;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class HouseqmCheckTaskNotifyRecordVo {
    private Integer projectId;
    private Integer taskId;
    private Integer srcUserId;
    private String desUserIds;
    private int moduleId;
    private Integer issueId;
    private Integer issueStatus;
    private String extraInfo;
};
