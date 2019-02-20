package com.longfor.longjian.houseqm.app.service;

import com.longfor.longjian.common.base.LjBaseResponse;
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


    Map<String, Object> exportExcel(Integer uid, Integer project_id, Integer category_cls, Integer task_id, String category_key, String check_item_key, String area_ids, String status_in, Integer checker_id, Integer repairer_id, Integer type, Integer condition, String key_word, String create_on_begin, String create_on_end, Boolean is_overdue);

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

    Boolean repairNotifyExport2(Integer uid, Integer projectId, String issueUuid, HttpServletResponse response);

    Map<String, Object> repairReplyExport(Integer projectId, String issueIds);

    List<ProjectSettingV2> getProjectSettingId(Integer projectId);

    LjBaseResponse updateIssueRepairInfoByProjectAndUuid(Integer uid, Integer repairerId, String repairFollowerIds, Integer projectId, String issueUuid);

    LjBaseResponse<List<HouseQmCheckTaskIssueDetailRepairLogVo>>  getDetailRepairLogByIssueUuid(String issueUuid);

    LjBaseResponse<IssueInfoVo> getHouseQmCheckTaskIssueDetailBaseByProjectAndUuid(Integer uid, Integer projectId, String issueUuid);

    Boolean repairNotifyExport(Integer userId, int parseInt, String issueUuid, HttpServletResponse response, HttpServletRequest request);
}
