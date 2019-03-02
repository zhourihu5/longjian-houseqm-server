package com.longfor.longjian.houseqm.app.service;

import com.longfor.longjian.common.base.LjBaseResponse;
import com.longfor.longjian.houseqm.app.req.IssueListDoActionReq;
import com.longfor.longjian.houseqm.app.req.bgtask.ExportBuildingExcelReq;
import com.longfor.longjian.houseqm.app.vo.*;
import com.longfor.longjian.houseqm.app.vo.issuelist.IssueListRsp;
import com.longfor.longjian.houseqm.po.zj2db.ProjectSettingV2;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;


/**
 * @author Houyan
 * @date 2018/12/21 0021 10:50
 */
public interface IIssueService {


    Map<String, Object> exportExcel(Integer uid, ExportBuildingExcelReq req);

    /**
     *
     * @author hy
     * @date 2018/12/21 0021
     * @return com.longfor.gaia.gfs.core.bean.PageInfo<com.longfor.longjian.houseqm.app.vo.IssueListVo>
     */
    IssueListRsp list(IssueListDoActionReq req);

    List<HouseQmCheckTaskIssueHistoryLogVo> getHouseQmCheckTaskIssueActionLogByIssueUuid(String issueUuid);

    void deleteHouseqmCheckTaskIssueByProjectAndUuid(Integer projectId, String issueUuid);

    LjBaseResponse updeteIssueDescByUuid(Integer projectId, String issueUuid, Integer uid, String content);

    LjBaseResponse updateIssuePlanEndOnByProjectAndUuid(Integer projectId, String issueUuid, Integer uid, Integer planEndOn);

    LjBaseResponse updateIssueApproveStatusByUuid(Integer projectId, String issueUuid, Integer uid, Integer status, String content);

    Boolean repairNotifyExport2(Integer uid, Integer projectId, String issueUuid, HttpServletResponse response);

    Map<String, Object> repairReplyExport(Integer projectId, String issueIds);

    List<ProjectSettingV2> getProjectSettingId(Integer projectId);

    LjBaseResponse updateIssueRepairInfoByProjectAndUuid(Integer uid, Integer repairerId, String repairFollowerIds, Integer projectId, String issueUuid);

    LjBaseResponse<List<HouseQmCheckTaskIssueDetailRepairLogVo>>  getDetailRepairLogByIssueUuid(String issueUuid);

    LjBaseResponse<IssueInfoVo> getHouseQmCheckTaskIssueDetailBaseByProjectAndUuid(Integer uid, Integer projectId, String issueUuid);

    Boolean repairNotifyExport(Integer userId, int parseInt, String issueUuid, HttpServletResponse response, HttpServletRequest request);

    LjBaseResponse<Object> updateIssueDetailByProjectAndUuid(Integer userId, Integer projectId, String issueUuid, Integer typ, String data);
}
