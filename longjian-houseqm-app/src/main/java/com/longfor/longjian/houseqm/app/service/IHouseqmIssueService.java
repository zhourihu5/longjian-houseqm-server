package com.longfor.longjian.houseqm.app.service;

import com.longfor.longjian.houseqm.po.HouseQmCheckTaskIssue;
import com.longfor.longjian.houseqm.po.zj2db.ExportFileRecord;
import com.longfor.longjian.houseqm.po.zj2db.Project;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.app.service
 * @ClassName: IHouseqmIssueService
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/1/10 17:26
 */
public interface IHouseqmIssueService {

    Project getProjectByProjId(Integer project_id);

    List<HouseQmCheckTaskIssue> searchHouseQmIssueListByProjUuidIn(Integer project_id, List<String> uuids);

    ExportFileRecord create(int userId, Integer teamId, Integer project_id, int exportType, Map<String, String> args, String exportName, Date executeAt);

    List<String> updateBatchIssueRepairInfoByUuids(List<String> uuids, Integer project_id, int uid, Integer repairer_id, String repair_follower_ids, Integer plan_end_on);
}
