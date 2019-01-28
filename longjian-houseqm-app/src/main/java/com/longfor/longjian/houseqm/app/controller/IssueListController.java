package com.longfor.longjian.houseqm.app.controller;

import com.google.common.collect.Lists;
import com.longfor.gaia.gfs.core.bean.PageInfo;
import com.longfor.gaia.gfs.web.mock.MockOperation;
import com.longfor.longjian.common.base.LjBaseResponse;
import com.longfor.longjian.common.consts.CommonGlobal;
import com.longfor.longjian.common.util.CtrlTool;
import com.longfor.longjian.common.util.SessionInfo;
import com.longfor.longjian.houseqm.app.req.IssueListDoActionReq;
import com.longfor.longjian.houseqm.app.service.IIssueService;
import com.longfor.longjian.houseqm.app.vo.*;
import com.longfor.longjian.houseqm.app.vo.issuelist.IssueListRsp;
import com.longfor.longjian.houseqm.consts.CommonGlobalEnum;
import com.longfor.longjian.houseqm.po.ProjectSettingV2;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.ArrayList;
import java.util.List;

/**
 * http://192.168.37.159:3000/project/8/interface/api/286 问题检索
 *
 * @author lipeishuai
 * @date 2018/11/17 15:07
 */

@RestController
@RequestMapping("buildingqm/v3/papi/issue/")
@Slf4j
public class IssueListController {

    @Resource
    private IIssueService iIssueService;
    @Resource
    private CtrlTool ctrlTool;
    @Resource
    private SessionInfo sessionInfo;

    /**
     * 问题检索
     * http://192.168.37.159:3000/project/8/interface/api/286
     * //PageInfo<IssueListVo>
     *
     * @param req
     * @return
     */
    @GetMapping(value = "list/", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public TaskResponse<IssueListRsp> doAction(HttpServletRequest request, @Valid IssueListDoActionReq req) {
        log.info("list, project_id=%d, category_cls=%d, task_id=%d, category_key=%s, check_item_key=%s, area_ids=%s, status_in=%s, checker_id=%d, repairer_id=%d," +
                " type=%d, condition=%d, key_word=%s, create_on_begin=%s, create_on_end=%s, is_overdue=%d, page=%d, page_size=%d"
        );
        Integer userId = (Integer) sessionInfo.getBaseInfo("userId");
        TaskResponse<IssueListRsp> response = new TaskResponse<>();
        try {
            ctrlTool.projPerm(request, "项目.工程检查.问题管理.查看");
            IssueListRsp result = iIssueService.list(req.getProject_id(), req.getCategory_cls(), req.getTask_id(), req.getCategory_key(), req.getCheck_item_key(),
                    req.getArea_ids(), req.getStatus_in(), req.getChecker_id(), req.getRepairer_id(), req.getType(), req.getCondition(), req.getKey_word(),
                    req.getCreate_on_begin(), req.getCreate_on_end(), req.is_overdue(), req.getPage(), req.getPage_size());

            response.setData(result);
        } catch (Exception e) {
            e.printStackTrace();
            response.setResult(1);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    /**
     * 项目下问题详情中历史信息
     *
     * @param projectId
     * @param issueUuid
     * @return
     */
    @GetMapping(value = "detail_log/", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public TaskResponse<ArrayList<HouseQmCheckTaskIssueHistoryLogVo>> detailLog(HttpServletRequest request, @RequestParam(value = "project_id", required = true) Integer projectId,
                                                                                @RequestParam(value = "issue_uuid", required = true) String issueUuid) {
        TaskResponse<ArrayList<HouseQmCheckTaskIssueHistoryLogVo>> response = new TaskResponse<>();
        Integer userId = (Integer) sessionInfo.getBaseInfo("userId");
        try {
             ctrlTool.projPerm(request, "项目.工程检查.问题管理.查看");
            List<HouseQmCheckTaskIssueHistoryLogVo> result = iIssueService.getHouseQmCheckTaskIssueActionLogByIssueUuid(issueUuid);
            response.setData(result);
        } catch (Exception e) {
            e.printStackTrace();
            response.setResult(1);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    @GetMapping(value = "repair_notify_export2/", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<String> repairNotifyExport2(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "project_id", required = true) Integer projectId,
                                                      @RequestParam(value = "issue_uuid", required = true) String issueUuid) {
        if (request.getMethod() == "POST") {
            String project_id = request.getParameter("project_id");
            String issue_ids = request.getParameter("issue_ids");
        }
        log.info("repair_notify_export2, project_id=" + projectId + ", issue_ids=" + issueUuid + "");

        Integer userId = (Integer) sessionInfo.getBaseInfo("userId");
        if (projectId == null || issueUuid == null) {
            LjBaseResponse<String> objectTaskResponse = new LjBaseResponse<>();
            objectTaskResponse.setMessage("args error");
            objectTaskResponse.setResult((Integer) CommonGlobalEnum.RES_ERROR.getId());
            return objectTaskResponse;

        }
        RepairNotifyExportVo repairNotifyExportVo = iIssueService.repairNotifyExport2(userId, projectId, issueUuid);
        log.info("export repair notify, result=" + repairNotifyExportVo.getResult() + ", message=" + repairNotifyExportVo.getMessage() + ", path=" + repairNotifyExportVo.getPath() + "");
        if (repairNotifyExportVo.getResult() != 0) {
            LjBaseResponse<String> objectTaskResponse = new LjBaseResponse<>();
            objectTaskResponse.setMessage(repairNotifyExportVo.getMessage());
            objectTaskResponse.setResult(repairNotifyExportVo.getResult());
            return objectTaskResponse;
        }
        return null;
    }

    /**
     * 项目下问题详情
     *
     * @param projectId
     * @return
     */
    @GetMapping(value = "configs/", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<ProjectSettingConfigVo> configs(HttpServletRequest request, @RequestParam(value = "project_id", required = true) Integer projectId) {
        LjBaseResponse<ProjectSettingConfigVo> response = new LjBaseResponse<>();
        Integer userId = (Integer) sessionInfo.getBaseInfo("userId");
        try {
            ctrlTool.projPerm(request, "项目.工程检查.问题管理.查看");
        } catch (Exception e) {
            e.printStackTrace();
            response.setResult(1);
            response.setMessage(e.getMessage());
        }
        List<ProjectSettingConfigVo.HouseQmIssueReason> reason_list = Lists.newArrayList();
        Integer reasonId = 0;

        List<ProjectSettingV2> projectSetting = iIssueService.getProjectSettingId(projectId);
        ProjectSettingConfigVo vo = new ProjectSettingConfigVo();
        for (int i = 0; i < projectSetting.size(); i++) {
            if (projectSetting.get(i).getsKey().equals("PROJ_ISSUE_REASON_SWITCH")) {
                vo.setHas_issue_reason(true);
            }
            if (projectSetting.get(i).getsKey().equals("PROJ_ISSUE_SUGGEST_SWITCH")) {
                vo.setHas_issue_suggest(true);
            }
            if (projectSetting.get(i).getsKey().equals("PROJ_POTENTIAL_RISK_SWITCH")) {
                vo.setHas_issue_potential_rist(true);
            }
            if (projectSetting.get(i).getsKey().equals("PROJ_PREVENTIVE_ACTION_SWITCH")) {
                vo.setHas_issue_preventive_action(true);
            }
            if (projectSetting.get(i).getsKey().equals("PROJ_ISSUE_REASON_NAME")) {
                reasonId = projectSetting.get(i).getId();
            }
            if (projectSetting.get(i).getsKey().equals("PROJ_ISSUE_REASON_LIST")) {
                ProjectSettingConfigVo.HouseQmIssueReason single_reason = new ProjectSettingConfigVo().new HouseQmIssueReason();
                single_reason.setId(projectSetting.get(i).getId());
                single_reason.setValue(projectSetting.get(i).getValue());
                reason_list.add(single_reason);
            }
        }
        if (reasonId > 0) {
            for (int i = 0; i < projectSetting.size(); i++) {
                if (projectSetting.get(i).getParentId().equals(reasonId)) {
                    ProjectSettingConfigVo.HouseQmIssueReason singleReason = new ProjectSettingConfigVo().new HouseQmIssueReason();
                    singleReason.setId(projectSetting.get(i).getId());
                    singleReason.setValue(projectSetting.get(i).getValue());
                    reason_list.add(singleReason);
                }
            }
        }
        vo.setReason_list(reason_list);
        response.setData(vo);
        return response;
    }


    /**
     * 删除问题
     *
     * @param projectId
     * @param issueUuid
     * @return
     */
    @PostMapping(value = "delete", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public TaskResponse delete(HttpServletRequest request,@RequestParam(value = "project_id", required = true) Integer projectId,
                               @RequestParam(value = "issue_uuid", required = true) String issueUuid) {
        TaskResponse response = new TaskResponse();
        try {
            ctrlTool.projPerm(request, "项目.工程检查.问题管理.查看");
            iIssueService.deleteHouseqmCheckTaskIssueByProjectAndUuid(projectId, issueUuid);
        } catch (Exception e) {
            e.printStackTrace();
            response.setResult(1);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    /**
     * 项目下问题详情修改整改责任人
     *
     * @param projectId
     * @param issueUuid
     * @param repairerId
     * @param repairFollowerIds
     * @return
     */
    @PostMapping(value = "edit_repairer", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse editRepairer(HttpServletRequest request,@RequestParam(value = "project_id", required = true) Integer projectId,
                                       @RequestParam(value = "issue_uuid", required = true) String issueUuid,
                                       @RequestParam(value = "repairer_id", required = false, defaultValue = "0") Integer repairerId,
                                       @RequestParam(value = "repair_follower_ids", required = false, defaultValue = "") String repairFollowerIds) {
        Integer userId = (Integer) sessionInfo.getBaseInfo("userId");
        try {
            ctrlTool.projPerm(request, "项目.工程检查.问题管理.查看");
        } catch (Exception e) {
            e.printStackTrace();
        }
        LjBaseResponse taskResponse = iIssueService.updateIssueRepairInfoByProjectAndUuid(userId, repairerId, repairFollowerIds, projectId, issueUuid);
        return taskResponse;
    }


    /**
     * 项目下问题详情追加描述
     *
     * @param projectId
     * @param issueUuid
     * @param content
     * @return
     */
    @PostMapping(value = "add_desc", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse addDesc(HttpServletRequest request,@RequestParam(value = "project_id", required = true) Integer projectId,
                                  @RequestParam(value = "issue_uuid", required = true) String issueUuid,
                                  @RequestParam(value = "content", required = true) String content) {
        Integer userId = (Integer) sessionInfo.getBaseInfo("userId");
        try {
            ctrlTool.projPerm(request, "项目.工程检查.问题管理.查看");
        } catch (Exception e) {
            e.printStackTrace();
        }
        LjBaseResponse taskResponse = iIssueService.updeteIssueDescByUuid(projectId, issueUuid, userId, content);
        return taskResponse;
    }

    /**
     * 更新issue计划整改完成时间
     *
     * @param projectId
     * @param issueUuid
     * @param plan_end_on
     * @return
     */
    @PostMapping(value = "edit_plan_end_on", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse editPlanEndOn(HttpServletRequest request,@RequestParam(value = "project_id", required = true) Integer projectId,
                                        @RequestParam(value = "issue_uuid", required = true) String issueUuid,
                                        @RequestParam(value = "plan_end_on", required = false, defaultValue = "0") Integer plan_end_on) {
        Integer userId = (Integer) sessionInfo.getBaseInfo("userId");
        try {
            ctrlTool.projPerm(request, "项目.工程检查.问题管理.查看");
        } catch (Exception e) {
            e.printStackTrace();
        }
        LjBaseResponse taskResponse = iIssueService.updateIssuePlanEndOnByProjectAndUuid(projectId, issueUuid, userId, plan_end_on);
        return taskResponse;
    }

    /**
     * 项目下问题详情销项问题
     *
     * @param projectId
     * @param issueUuid
     * @param status
     * @param content
     * @return
     */
    @PostMapping(value = "edit_approve", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse editApprove(HttpServletRequest request,@RequestParam(value = "project_id", required = true) Integer projectId,
                                      @RequestParam(value = "issue_uuid", required = true) String issueUuid,
                                      @RequestParam(value = "status", required = true) Integer status,
                                      @RequestParam(value = "content", required = true) String content) {
        Integer userId = (Integer) sessionInfo.getBaseInfo("userId");
        try {
            ctrlTool.projPerm(request, "项目.工程检查.问题管理.查看");
        } catch (Exception e) {
            e.printStackTrace();
        }
        LjBaseResponse taskResponse = iIssueService.updateIssueApproveStatusByUuid(projectId, issueUuid, userId, status, content);
        return taskResponse;
    }

    /**
     * 项目下问题修复记录
     *
     * @param projectId
     * @param issueUuid
     * @return
     */
    @GetMapping(value = "detail_repair_log", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<List<HouseQmCheckTaskIssueDetailRepairLogVo>> detailRepairLog(HttpServletRequest request,@RequestParam(value = "project_id", required = true) Integer projectId,
                                                                                        @RequestParam(value = "issue_uuid", required = true) String issueUuid) {
        Integer userId = (Integer) sessionInfo.getBaseInfo("userId");
        try {
            ctrlTool.projPerm(request, "项目.工程检查.问题管理.查看");
        } catch (Exception e) {
            e.printStackTrace();
        }
        LjBaseResponse<List<HouseQmCheckTaskIssueDetailRepairLogVo>> result = iIssueService.getDetailRepairLogByIssueUuid(issueUuid);

        return result;
    }

    /**
     * 项目下问题详情
     *
     * @param projectId
     * @param issueUuid
     * @return
     */
    @GetMapping(value = "detail_base", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<IssueInfoVo> detailBase(HttpServletRequest request,@RequestParam(value = "project_id", required = true) Integer projectId,
                                                  @RequestParam(value = "issue_uuid", required = true) String issueUuid) {
        Integer userId = (Integer) sessionInfo.getBaseInfo("userId");
        try {
            ctrlTool.projPerm(request, "项目.工程检查.问题管理.查看");
        } catch (Exception e) {
            e.printStackTrace();
        }
        LjBaseResponse<IssueInfoVo> result = iIssueService.getHouseQmCheckTaskIssueDetailBaseByProjectAndUuid(userId, projectId, issueUuid);

        return result;
    }

}
