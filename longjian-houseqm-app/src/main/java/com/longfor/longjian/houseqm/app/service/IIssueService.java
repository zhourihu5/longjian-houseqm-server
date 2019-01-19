package com.longfor.longjian.houseqm.app.service;

import com.longfor.gaia.gfs.core.bean.PageInfo;
import com.longfor.longjian.common.base.LjBaseResponse;
import com.longfor.longjian.houseqm.app.vo.*;
import com.longfor.longjian.houseqm.app.vo.issuelist.IssueListRsp;
import com.longfor.longjian.houseqm.po.ProjectSettingV2;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Houyan
 * @date 2018/12/21 0021 10:50
 */
public interface IIssueService {

    /**
     *
     * @author hy
     * @date 2018/12/21 0021
     * @param projectId
     * @param categoryCls
     * @param taskId
     * @param categoryKey
     * @param checkItemKey
     * @param areaIds
     * @param statusIn
     * @param checkerId
     * @param repairerId
     * @param type
     * @param condition
     * @param keyWord
     * @param createOnBegin
     * @param createOnEnd
     * @param isOverDue
     * @param pageNum
     * @param pageSize
     * @return com.longfor.gaia.gfs.core.bean.PageInfo<com.longfor.longjian.houseqm.app.vo.IssueListVo>
     */
    IssueListRsp list(Integer projectId, Integer categoryCls, Integer taskId, String categoryKey, String checkItemKey,
                      String areaIds, String statusIn, Integer checkerId, Integer repairerId, Integer type, Integer condition, String keyWord,
                      String createOnBegin, String createOnEnd, Boolean isOverDue, Integer pageNum, Integer pageSize);

    List<HouseQmCheckTaskIssueHistoryLogVo> getHouseQmCheckTaskIssueActionLogByIssueUuid(String issueUuid);

    void deleteHouseqmCheckTaskIssueByProjectAndUuid(Integer projectId, String issueUuid);

    LjBaseResponse updeteIssueDescByUuid(Integer projectId, String issueUuid, Integer uid, String content);

    LjBaseResponse updateIssuePlanEndOnByProjectAndUuid(Integer projectId, String issueUuid, Integer uid, Integer plan_end_on);

    LjBaseResponse updateIssueApproveStatusByUuid(Integer projectId, String issueUuid, Integer uid, Integer status, String content);

    RepairNotifyExportVo repairNotifyExport2(Integer uid, Integer projectId, String issueUuid);

    List<ProjectSettingV2> getProjectSettingId(Integer projectId);

    LjBaseResponse updateIssueRepairInfoByProjectAndUuid(Integer uid, Integer repairerId, String repairFollowerIds, Integer projectId, String issueUuid);

    LjBaseResponse<List<HouseQmCheckTaskIssueDetailRepairLogVo>>  getDetailRepairLogByIssueUuid(String issueUuid);

    LjBaseResponse<IssueInfoVo> getHouseQmCheckTaskIssueDetailBaseByProjectAndUuid(Integer uid, Integer projectId, String issueUuid);
}
