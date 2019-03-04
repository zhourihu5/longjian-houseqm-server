package com.longfor.longjian.houseqm.app.service;

import com.longfor.longjian.houseqm.po.zj2db.ExportFileRecord;
import com.longfor.longjian.houseqm.po.zj2db.HouseQmCheckTaskIssue;
import com.longfor.longjian.houseqm.po.zj2db.Project;

import java.io.IOException;
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

    Project getProjectByProjId(Integer projectId);

    List<HouseQmCheckTaskIssue> searchHouseQmIssueListByProjUuidIn(Integer projectId, List<String> uuids) throws Exception;

    ExportFileRecord create(int userId, Integer teamId, Integer projectId, int exportType, Map<String, String> args, String exportName, Date executeAt) throws IOException;

    List<String> updateBatchIssueRepairInfoByUuids(List<String> uuids, Integer projectId, int uid, Integer repairerId, String repairFollowerIds, Integer planEndOn) throws Exception;

    List<String> updateBatchIssueApproveStatusByUuids(List<String> uuids, int projectId, int senderId, int status, String desc, String attachmentMd5List) throws Exception;

    void deleteHouseQmCheckTaskIssueByProjUuid(Integer projectId, String issueUuid) throws Exception;

}
