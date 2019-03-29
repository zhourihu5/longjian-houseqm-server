package com.longfor.longjian.houseqm.app.controller.buildingqmv3papi;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.longfor.longjian.common.base.LjBaseResponse;
import com.longfor.longjian.common.util.CtrlTool;
import com.longfor.longjian.common.util.RequestContextHolderUtil;
import com.longfor.longjian.common.util.SessionInfo;
import com.longfor.longjian.houseqm.app.req.EditDetailReq;
import com.longfor.longjian.houseqm.app.req.IssueListDoActionReq;
import com.longfor.longjian.houseqm.app.req.bgtask.ExportBuildingExcelReq;
import com.longfor.longjian.houseqm.app.service.IIssueService;
import com.longfor.longjian.houseqm.app.utils.CtrlToolUtils;
import com.longfor.longjian.houseqm.app.utils.SessionUtil;
import com.longfor.longjian.houseqm.app.vo.HouseQmCheckTaskIssueDetailRepairLogVo;
import com.longfor.longjian.houseqm.app.vo.HouseQmCheckTaskIssueHistoryLogVo;
import com.longfor.longjian.houseqm.app.vo.IssueInfoVo;
import com.longfor.longjian.houseqm.app.vo.ProjectSettingConfigVo;
import com.longfor.longjian.houseqm.app.vo.issuelist.DetailLogRspVo;
import com.longfor.longjian.houseqm.app.vo.issuelist.DetailRepairLogRspVo;
import com.longfor.longjian.houseqm.app.vo.issuelist.IssueListRsp;
import com.longfor.longjian.houseqm.consts.CommonGlobalEnum;
import com.longfor.longjian.houseqm.po.zj2db.ProjectSettingV2;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Map;

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
    private static final String PROJECT_ID = "project_id";
    private static final String ISSUE_IDS = "issue_ids";
    private static final String UTF_8 = "utf-8";
    private static final String ARGS_ERROR="args error";
    private static final String DESC="项目.移动验房.问题管理.查看";
    private static final String DESC_EDIT="项目.移动验房.问题管理.编辑";

    @Resource
    private IIssueService iIssueService;
    @Resource
    private CtrlTool ctrlTool;
    @Resource
    private SessionInfo sessionInfo;

    @RequestMapping(value = "export_excel", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<Object> exportExcel(HttpServletRequest request, HttpServletResponse response, @Validated ExportBuildingExcelReq req) {
        LjBaseResponse<Object> ljBaseResponse = new LjBaseResponse<>();
        ServletOutputStream os = null;
        try {
            // 对参数进行非空判断
            log.info("export_excel," + JSON.toJSONString(req));
            Integer uid = SessionUtil.getUid(sessionInfo);
            ctrlTool.projPerm(request, DESC);
            // 导出execel
            os = response.getOutputStream();

            Map<String, Object> map = iIssueService.exportExcel(uid, req);
            String fileName = (String) map.get("fileName");
            SXSSFWorkbook wb = (SXSSFWorkbook) map.get("workbook");
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding(UTF_8);
            response.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes(UTF_8), "iso8859-1"));
            wb.write(os);
            os.flush();
        } catch (Exception e) {
            log.error("excel 导出异常", e);
            return CtrlToolUtils.errorReturn(ljBaseResponse,e);
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }
        return ljBaseResponse;
    }

    @RequestMapping(value = "list", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<IssueListRsp> doAction(HttpServletRequest request, @Valid IssueListDoActionReq req) {
        log.info("list, project_id=" + req.getProject_id() + ", category_cls=" + req.getCategory_cls() + ", task_id=" + req.getTask_id() + ", category_key=" + req.getCategory_key() + ", check_item_key=" + req.getCheck_item_key() + ", area_ids=" + req.getArea_ids() + ", status_in=" + req.getStatus_in() + ", checker_id=" + req.getChecker_id() + ", repairer_id=" + req.getRepairer_id() + "," +
                " type=" + req.getType() + ", condition=" + req.getCondition() + ", key_word=" + req.getKey_word() + ", create_on_begin=" + req.getCreate_on_begin() + ", create_on_end=" + req.getCreate_on_end() + ", is_overdue=" + req.getIs_overdue() + ", page=" + req.getPage() + ", page_size=" + req.getPage_size()
        );
        LjBaseResponse<IssueListRsp> response = new LjBaseResponse<>();
        try {
            ctrlTool.projPerm(request, DESC);
            IssueListRsp result = iIssueService.list(req);
            response.setData(result);
        } catch (Exception e) {
            log.error("问题检索异常:", e.getMessage());
            return CtrlToolUtils.errorReturn(response,e);
        }
        return response;
    }

    @RequestMapping(value = "detail_log", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<DetailLogRspVo> detailLog(@RequestParam(value = "project_id") Integer projectId,
                                                    @RequestParam(value = "issue_uuid") String issueUuid) {
        LjBaseResponse<DetailLogRspVo> response = new LjBaseResponse<>();
        try {
            ctrlTool.projPerm(RequestContextHolderUtil.getRequest(), "项目.工程检查.问题管理.查看");
            List<HouseQmCheckTaskIssueHistoryLogVo> result = iIssueService.getHouseQmCheckTaskIssueActionLogByIssueUuid(issueUuid);
            DetailLogRspVo data = new DetailLogRspVo();
            data.setItems(result);
            response.setData(data);
        } catch (Exception e) {
            log.error(e.getMessage());
            return CtrlToolUtils.errorReturn(response,e);
        }
        return response;
    }

    @RequestMapping(value = "repair_notify_export2/", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<Object> repairNotifyExport2(HttpServletRequest request, HttpServletResponse response) {
        String projectId = request.getParameter(PROJECT_ID);
        String issueUuid = request.getParameter(ISSUE_IDS);
        log.info("repair_notify_export2, PROJECT_ID=" + projectId + ", issue_ids=" + issueUuid + "");
        Integer userId = SessionUtil.getUid(sessionInfo);
        if (projectId == null || issueUuid == null) {
            LjBaseResponse<Object> objectTaskResponse = new LjBaseResponse<>();
            objectTaskResponse.setMessage(ARGS_ERROR);
            objectTaskResponse.setResult((Integer) CommonGlobalEnum.RES_ERROR.getId());
            return objectTaskResponse;

        }
        Boolean b = iIssueService.repairNotifyExport2(userId, Integer.parseInt(projectId), issueUuid, response);
        if (b) {
            LjBaseResponse<Object> objectTaskResponse = new LjBaseResponse<>();
            objectTaskResponse.setData(b);
            return objectTaskResponse;
        }

        return null;
    }

    @RequestMapping(value = "repair_notify_export", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<Object> repairNotifyExport(HttpServletRequest request, HttpServletResponse response) {
        String projectId = request.getParameter(PROJECT_ID);
        String issueUuid = request.getParameter(ISSUE_IDS);
        log.info("repair_notify_export, project_id=" + projectId + ", ISSUE_IDS=" + issueUuid + "");
        Integer userId = SessionUtil.getUid(sessionInfo);
        if (projectId == null || issueUuid == null) {
            LjBaseResponse<Object> objectTaskResponse = new LjBaseResponse<>();
            objectTaskResponse.setMessage(ARGS_ERROR);
            objectTaskResponse.setResult((Integer) CommonGlobalEnum.RES_ERROR.getId());
            return objectTaskResponse;

        }
        Boolean b = iIssueService.repairNotifyExport(userId, Integer.parseInt(projectId), issueUuid, response, request);
        if (b) {
            LjBaseResponse<Object> objectTaskResponse = new LjBaseResponse<>();
            objectTaskResponse.setData(b);
            return objectTaskResponse;
        }

        return null;
    }

    //导出整改回复单
    @RequestMapping(value = "repair_reply_export", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<Object> repairReplyExport(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String projectId = request.getParameter(PROJECT_ID);
        String issueIds = request.getParameter(ISSUE_IDS);
        log.info("repair_reply_export, project_id=" + projectId + ", issue_ids=" + issueIds);
        if (projectId == null || issueIds == null) {
            LjBaseResponse<Object> objectTaskResponse = new LjBaseResponse<>();
            objectTaskResponse.setMessage(ARGS_ERROR);
            objectTaskResponse.setResult((Integer) CommonGlobalEnum.RES_ERROR.getId());
            return objectTaskResponse;
        }
        try (ServletOutputStream os = response.getOutputStream()) {
            Map<String, Object> map = iIssueService.repairReplyExport(Integer.parseInt(projectId), issueIds);
            XWPFDocument doc = (XWPFDocument) map.get("doc");
            String filename = (String) map.get("filename");
            response.setCharacterEncoding(UTF_8);
            response.setHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes(UTF_8), "iso8859-1"));
            doc.write(os);
            os.flush();
        } catch (IOException e) {
            log.error("导出整改回复单:", e.getMessage());
        }

        return new LjBaseResponse<>();
    }

    @RequestMapping(value = "configs/", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @SuppressWarnings("squid:S3776")
    public LjBaseResponse<ProjectSettingConfigVo> configs(HttpServletRequest request, @RequestParam(value = "project_id") Integer projectId) {
        LjBaseResponse<ProjectSettingConfigVo> response = new LjBaseResponse<>();
        try {
            ctrlTool.projPerm(request, DESC);
        } catch (Exception e) {
            log.error("问题鉴权异常:", e.getMessage());
            return CtrlToolUtils.errorReturn(response,e);
        }
        List<ProjectSettingConfigVo.HouseQmIssueReason> reasonList = Lists.newArrayList();
        Integer reasonId = 0;

        List<ProjectSettingV2> projectSetting = iIssueService.getProjectSettingId(projectId);
        ProjectSettingConfigVo vo = new ProjectSettingConfigVo();
        reasonId = getInteger(reasonList, reasonId, projectSetting, vo);
        if (reasonId > 0) {
            for (ProjectSettingV2 projectSettingV2 : projectSetting) {
                if (projectSettingV2.getParentId()!=null&&projectSettingV2.getParentId().equals(reasonId)) {
                    ProjectSettingConfigVo.HouseQmIssueReason singleReason = new ProjectSettingConfigVo().new HouseQmIssueReason();
                    singleReason.setId(projectSettingV2.getId());
                    singleReason.setValue(projectSettingV2.getValue());
                    reasonList.add(singleReason);
                }
            }
        }
        vo.setReason_list(reasonList);
        response.setData(vo);
        return response;
    }

    private Integer getInteger(List<ProjectSettingConfigVo.HouseQmIssueReason> reasonList, Integer reasonId, List<ProjectSettingV2> projectSetting, ProjectSettingConfigVo vo) {
        for (ProjectSettingV2 projectSettingV2 : projectSetting) {

            if ("PROJ_ISSUE_REASON_SWITCH".equals(projectSettingV2.getsKey())) {
                vo.setHas_issue_reason(true);
            }
            if ("PROJ_ISSUE_SUGGEST_SWITCH".equals(projectSettingV2.getsKey())) {
                vo.setHas_issue_suggest(true);
            }
            if ("PROJ_POTENTIAL_RISK_SWITCH".equals(projectSettingV2.getsKey())) {
                vo.setHas_issue_potential_rist(true);
            }
            if ("PROJ_PREVENTIVE_ACTION_SWITCH".equals(projectSettingV2.getsKey())) {
                vo.setHas_issue_preventive_action(true);
            }
            if ("PROJ_ISSUE_REASON_NAME".equals(projectSettingV2.getsKey())) {
                reasonId = projectSettingV2.getId();
            }
            if ("PROJ_ISSUE_REASON_LIST".equals(projectSettingV2.getsKey())) {
                ProjectSettingConfigVo.HouseQmIssueReason singleReason = new ProjectSettingConfigVo().new HouseQmIssueReason();
                singleReason.setId(projectSettingV2.getId());
                singleReason.setValue(projectSettingV2.getValue());
                reasonList.add(singleReason);
            }
        }
        return reasonId;
    }

    @RequestMapping(value = "delete", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse delete(HttpServletRequest request, @RequestParam(value = "project_id") Integer projectId,
                                 @RequestParam(value = "issue_uuid") String issueUuid) {
        LjBaseResponse response = new LjBaseResponse();
        try {
            ctrlTool.projPerm(request, "项目.工程检查.问题管理.删除");
            iIssueService.deleteHouseqmCheckTaskIssueByProjectAndUuid(projectId, issueUuid);
        } catch (Exception e) {
            log.error("删除问题:", e.getMessage());
            return CtrlToolUtils.errorReturn(response,e);
        }
        return response;
    }

    @RequestMapping(value = "edit_repairer", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse editRepairer(HttpServletRequest request, @RequestParam(value = "project_id") Integer projectId,
                                       @RequestParam(value = "issue_uuid") String issueUuid,
                                       @RequestParam(value = "repairer_id", required = false, defaultValue = "0") Integer repairerId,
                                       @RequestParam(value = "repair_follower_ids", required = false, defaultValue = "") String repairFollowerIds) {
        Integer userId = SessionUtil.getUid(sessionInfo);
        try {
            ctrlTool.projPerm(request, DESC_EDIT);
        } catch (Exception e) {
            log.error("修改整改责任人异常:", e.getMessage());
            return CtrlToolUtils.errorReturn(new LjBaseResponse<>(),e);
        }
        return iIssueService.updateIssueRepairInfoByProjectAndUuid(userId, repairerId, repairFollowerIds, projectId, issueUuid);
    }

    @RequestMapping(value = "add_desc", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse addDesc(HttpServletRequest request, @RequestParam(value = "project_id") Integer projectId,
                                  @RequestParam(value = "issue_uuid") String issueUuid,
                                  @RequestParam(value = "content") String content) {
        Integer userId = SessionUtil.getUid(sessionInfo);
        try {
            ctrlTool.projPerm(request, DESC_EDIT);
        } catch (Exception e) {
            log.error("追加描述异常:", e.getMessage());
            return CtrlToolUtils.errorReturn(new LjBaseResponse<>(),e);
        }
        return iIssueService.updeteIssueDescByUuid(projectId, issueUuid, userId, content);
    }

    @RequestMapping(value = "edit_plan_end_on", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse editPlanEndOn(HttpServletRequest request, @RequestParam(value = "project_id") Integer projectId,
                                        @RequestParam(value = "issue_uuid") String issueUuid,
                                        @RequestParam(value = "plan_end_on", required = false, defaultValue = "0") Integer planEndOn) {
        Integer userId = SessionUtil.getUid(sessionInfo);
        try {
            ctrlTool.projPerm(request, DESC_EDIT);
        } catch (Exception e) {
            log.error("更新完成时间异常:", e.getMessage());
            return CtrlToolUtils.errorReturn(new LjBaseResponse<>(),e);
        }
        return iIssueService.updateIssuePlanEndOnByProjectAndUuid(projectId, issueUuid, userId, planEndOn);
    }

    @RequestMapping(value = "edit_approve", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse editApprove(HttpServletRequest request, @RequestParam(value = "project_id") Integer projectId,
                                      @RequestParam(value = "issue_uuid") String issueUuid,
                                      @RequestParam(value = "status") Integer status,
                                      @RequestParam(value = "content") String content) {
        Integer userId = SessionUtil.getUid(sessionInfo);
        try {
            ctrlTool.projPerm(request, DESC_EDIT);
        } catch (Exception e) {
            log.error("销项问题异常:", e.getMessage());
            return CtrlToolUtils.errorReturn(new LjBaseResponse<>(),e);
        }
        return iIssueService.updateIssueApproveStatusByUuid(projectId, issueUuid, userId, status, content);
    }

    @RequestMapping(value = "detail_repair_log", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<DetailRepairLogRspVo> detailRepairLog(HttpServletRequest request, @RequestParam(value = "project_id") Integer projectId,
                                                                @RequestParam(value = "issue_uuid") String issueUuid) {
        try {
            ctrlTool.projPerm(request, DESC);
        } catch (Exception e) {
            log.error("鉴权异常:", e.getMessage());
            return CtrlToolUtils.errorReturn(new LjBaseResponse<>(),e);
        }
        LjBaseResponse<List<HouseQmCheckTaskIssueDetailRepairLogVo>> result = iIssueService.getDetailRepairLogByIssueUuid(issueUuid);

        LjBaseResponse<DetailRepairLogRspVo> response = new LjBaseResponse<>();
        DetailRepairLogRspVo data = new DetailRepairLogRspVo();
        data.setItems(result.getData());
        response.setData(data);
        return response;
    }

    @RequestMapping(value = "detail_base", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<IssueInfoVo> detailBase(HttpServletRequest request, @RequestParam(value = "project_id") Integer projectId,
                                                  @RequestParam(value = "issue_uuid") String issueUuid) {
        Integer userId = SessionUtil.getUid(sessionInfo);
        try {
            ctrlTool.projPerm(request, DESC);
        } catch (Exception e) {
            log.error("问题详情鉴权异常:", e.getMessage());
            return CtrlToolUtils.errorReturn(new LjBaseResponse<>(),e);
        }

        return iIssueService.getHouseQmCheckTaskIssueDetailBaseByProjectAndUuid(userId, projectId, issueUuid);
    }

    @RequestMapping(value = "edit_detail", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<Object> editDetail(HttpServletRequest request, @Validated EditDetailReq req) {
        LjBaseResponse<Object> response = new LjBaseResponse<>();
        Integer userId = SessionUtil.getUid(sessionInfo);
        try {
            ctrlTool.projPerm(request, DESC_EDIT);
        } catch (Exception e) {
            log.error("信息编辑异常:", e.getMessage());
            return CtrlToolUtils.errorReturn(response,e);
        }

        return iIssueService.updateIssueDetailByProjectAndUuid(userId, req.getProject_id(), req.getIssue_uuid(), req.getTyp(), req.getData());
    }

}
