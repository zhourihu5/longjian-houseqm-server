package com.longfor.longjian.houseqm.app.vo.houseqmissue;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.app.vo.houseqmissue
 * @ClassName: HouseQmIssueReportMsg
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/1/12 16:56
 */
@Data
@NoArgsConstructor
public class HouseQmIssueReportMsg implements Serializable {
    private List<HouseQmIssue> created_issues;
    private List<HouseQmIssue> assigned_issues;
    private List<HouseQmIssue> reformed_issues;
    private List<HouseQmIssue> checked_issues;

    public void appendCreated(String uuid, int projId, int taskId, int checkerId, int repairerId, int areaId,
                              String areaPathAndId, String categoryKey, String categoryPathAndKey, int senderId,
                              Date timeAt) {
        HouseQmIssue qmIssue = new HouseQmIssue(uuid, projId, taskId, checkerId, repairerId, areaId, areaPathAndId, categoryKey, categoryPathAndKey, senderId, timeAt.getTime());
        this.created_issues.add(qmIssue);
    }

    public void appendAssigned(String uuid, int projId, int taskId, int checkerId, int repairerId, int areaId,
                               String areaPathAndId, String categoryKey, String categoryPathAndKey, int senderId,
                               Date timeAt) {
        HouseQmIssue qmIssue = new HouseQmIssue(uuid, projId, taskId, checkerId, repairerId, areaId, areaPathAndId, categoryKey, categoryPathAndKey, senderId, timeAt.getTime());
        this.assigned_issues.add(qmIssue);
    }

    public void appendReformed(String uuid, int projId, int taskId, int checkerId, int repairerId, int areaId,
                               String areaPathAndId, String categoryKey, String categoryPathAndKey, int senderId,
                               Date timeAt) {
        HouseQmIssue qmIssue = new HouseQmIssue(uuid,projId,taskId,checkerId,repairerId,areaId,areaPathAndId,categoryKey,categoryPathAndKey,senderId,timeAt.getTime());
        this.reformed_issues.add(qmIssue);
    }

    public void appendChecked(String uuid, int projId, int taskId, int checkerId, int repairerId, int areaId,
                              String areaPathAndId, String categoryKey, String categoryPathAndKey, int senderId,
                              Date timeAt) {
        HouseQmIssue qmIssue = new HouseQmIssue(uuid,projId,taskId,checkerId,repairerId,areaId,areaPathAndId,categoryKey,categoryPathAndKey,senderId,timeAt.getTime());
        this.checked_issues.add(qmIssue);
    }

}
