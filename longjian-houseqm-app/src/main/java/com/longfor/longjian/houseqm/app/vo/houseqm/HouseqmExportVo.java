package com.longfor.longjian.houseqm.app.vo.houseqm;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class HouseqmExportVo {
    Integer projectId;
    Integer taskId;
    List<Integer> areaIds;
    Integer repairerId;
    java.util.Date beginOn;
    Date endOn;
    Integer categoryCls;
    boolean withRule;
}
