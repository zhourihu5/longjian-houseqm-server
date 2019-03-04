package com.longfor.longjian.houseqm.app.vo.houseqmstatistic;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TaskIssueRepairListVo {
    private Integer projectId;
    private Integer taskId;
    private Integer areaId;
    private Integer beginOn;
    private Integer endOn;
    private Integer timestamp;
    private Integer planStatus;
    private String source;
    private Integer page;
    private Integer pageSize;
}
