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
import com.longfor.longjian.houseqm.app.utils.ExportUtils;
import com.longfor.longjian.houseqm.app.vo.*;
import com.longfor.longjian.houseqm.app.vo.issuelist.DetailLogRspVo;
import com.longfor.longjian.houseqm.app.vo.issuelist.DetailRepairLogRspVo;
import com.longfor.longjian.houseqm.app.vo.issuelist.IssueListRsp;
import com.longfor.longjian.houseqm.consts.CommonGlobalEnum;
import com.longfor.longjian.houseqm.po.zj2db.ProjectSettingV2;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    @Resource
    private IIssueService iIssueService;
    @Resource
    private CtrlTool ctrlTool;
    @Resource
    private SessionInfo sessionInfo;

    /**
     * @return com.longfor.longjian.common.base.LjBaseResponse<java.lang.Object>
     * @Author hy
     * @Description 问题管理--导出excel
     * @Date 20:51 2019/2/15
     * @Param [request, req]
     **/
    @RequestMapping(value = "export_excel", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<Object> exportExcel(HttpServletRequest request, HttpServletResponse response, @Validated ExportBuildingExcelReq req) throws Exception {
        // 对参数进行非空判断
        log.info("export_excel," + JSON.toJSONString(req));
        Integer uid = (Integer) sessionInfo.getBaseInfo("userId");
        ctrlTool.projPerm(request, "项目.工程检查.问题管理.查看");
        LjBaseResponse<Object> ljBaseResponse = new LjBaseResponse<>();
        // 导出execel
        ServletOutputStream os = response.getOutputStream();
        try {
            Map<String, Object> map = iIssueService.exportExcel(uid, req.getProject_id(), req.getCategory_cls(), req.getTask_id(), req.getCategory_key(), req.getCheck_item_key(),
                    req.getArea_ids(), req.getStatus_in(), req.getChecker_id(), req.getRepairer_id(), req.getType(), req.getCondition(), req.getKey_word(),
                    req.getCreate_on_begin(), req.getCreate_on_end(), req.getIs_overdue());
            String fileName = (String) map.get("fileName");
            SXSSFWorkbook wb = (SXSSFWorkbook) map.get("workbook");
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes("gbk"), "iso8859-1") + ".xlsx");
            wb.write(os);
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
            log.error("excel 导出异常");
            ljBaseResponse.setResult(1);
            ljBaseResponse.setMessage(e.getMessage());
        } finally {
            os.close();
        }
        return ljBaseResponse;
    }

    /**
     * 问题检索
     * http://192.168.37.159:3000/project/8/interface/api/286
     * //PageInfo<IssueListVo>
     *
     * @param req
     * @return
     */
    @RequestMapping(value = "list", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public TaskResponse<IssueListRsp> doAction(HttpServletRequest request, @Valid IssueListDoActionReq req) {
        log.info("list, project_id=" + req.getProject_id() + ", category_cls=" + req.getCategory_cls() + ", task_id=" + req.getTask_id() + ", category_key=" + req.getCategory_key() + ", check_item_key=" + req.getCheck_item_key() + ", area_ids=" + req.getArea_ids() + ", status_in=" + req.getStatus_in() + ", checker_id=" + req.getChecker_id() + ", repairer_id=" + req.getRepairer_id() + "," +
                " type=" + req.getType() + ", condition=" + req.getCondition() + ", key_word=" + req.getKey_word() + ", create_on_begin=" + req.getCreate_on_begin() + ", create_on_end=" + req.getCreate_on_end() + ", is_overdue=" + req.is_overdue() + ", page=" + req.getPage() + ", page_size=" + req.getPage_size()
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
            response.setMsg(e.getMessage());
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
    @RequestMapping(value = "detail_log", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<DetailLogRspVo> detailLog(@RequestParam(value = "project_id", required = true) Integer projectId,
                                                    @RequestParam(value = "issue_uuid", required = true) String issueUuid) throws Exception {
        LjBaseResponse<DetailLogRspVo> response = new LjBaseResponse<>();
       /* Integer userId = (Integer) sessionInfo.getBaseInfo("userId");
        ctrlTool.projPerm(RequestContextHolderUtil.getRequest(), "项目.工程检查.问题管理.查看");*/
        List<HouseQmCheckTaskIssueHistoryLogVo> result = iIssueService.getHouseQmCheckTaskIssueActionLogByIssueUuid(issueUuid);
        DetailLogRspVo data = new DetailLogRspVo();
        data.setItems(result);
        response.setData(data);

        return response;
    }

    @RequestMapping(value = "repair_notify_export2/", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<Object> repairNotifyExport2(HttpServletRequest request, HttpServletResponse response) {
        String projectId = request.getParameter("project_id");
        String issueUuid = request.getParameter("issue_ids");
        log.info("repair_notify_export2, project_id=" + projectId + ", issue_ids=" + issueUuid + "");
        Integer userId = (Integer) sessionInfo.getBaseInfo("userId");
        if (projectId == null || issueUuid == null) {
            LjBaseResponse<Object> objectTaskResponse = new LjBaseResponse<>();
            objectTaskResponse.setMessage("args error");
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
        String projectId = request.getParameter("project_id");
        String issueUuid=  request.getParameter("issue_ids");
        log.info("repair_notify_export, project_id=" + projectId + ", issue_ids=" + issueUuid + "");
        Integer userId = (Integer) sessionInfo.getBaseInfo("userId");
        if (projectId == null || issueUuid == null) {
            LjBaseResponse<Object> objectTaskResponse = new LjBaseResponse<>();
            objectTaskResponse.setMessage("args error");
            objectTaskResponse.setResult((Integer) CommonGlobalEnum.RES_ERROR.getId());
            return objectTaskResponse;

        }
        Boolean b = iIssueService.repairNotifyExport(userId, Integer.parseInt(projectId), issueUuid,response,request);
        if(b){
            LjBaseResponse<Object> objectTaskResponse = new LjBaseResponse<>();
            objectTaskResponse.setData(b);
            return objectTaskResponse;
        }

        return null;
    }









    //导出整改回复单
    @RequestMapping(value = "repair_reply_export", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<Object> repairReplyExport(HttpServletRequest request, HttpServletResponse response,
                                                    @RequestParam(value = "project_id", required = true) Integer projectId,
                                                    @RequestParam(value = "issue_ids", required = true) String issueIds) throws IOException {
        log.info("repair_reply_export, project_id=" + projectId + ", issue_ids=" + issueIds);
        if (projectId == null || issueIds == null) {
            LjBaseResponse<Object> objectTaskResponse = new LjBaseResponse<>();
            objectTaskResponse.setMessage("args error");
            objectTaskResponse.setResult((Integer) CommonGlobalEnum.RES_ERROR.getId());
            return objectTaskResponse;
        }
        ServletOutputStream os = response.getOutputStream();
        try {
            Map<String, Object> map = iIssueService.repairReplyExport(projectId, issueIds);
            XWPFDocument doc = (XWPFDocument) map.get("doc");
            String filename = (String) map.get("filename");
            //response.setContentType("application/vnd.ms-word");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes("gbk"), "iso8859-1"));
            doc.write(os);
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (os != null) {
                os.close();
            }
        }

        return new LjBaseResponse<>();
    }


    /**
     * 项目下问题详情
     *
     * @param projectId
     * @return
     */
    @RequestMapping(value = "configs/", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
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
    @RequestMapping(value = "delete", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse delete(HttpServletRequest request, @RequestParam(value = "project_id", required = true) Integer projectId,
                                 @RequestParam(value = "issue_uuid", required = true) String issueUuid) {
        LjBaseResponse response = new LjBaseResponse();
        try {
            ctrlTool.projPerm(request, "项目.工程检查.问题管理.删除");
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
    @RequestMapping(value = "edit_repairer", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse editRepairer(HttpServletRequest request, @RequestParam(value = "project_id", required = true) Integer projectId,
                                       @RequestParam(value = "issue_uuid", required = true) String issueUuid,
                                       @RequestParam(value = "repairer_id", required = false, defaultValue = "0") Integer repairerId,
                                       @RequestParam(value = "repair_follower_ids", required = false, defaultValue = "") String repairFollowerIds) {
        Integer userId = (Integer) sessionInfo.getBaseInfo("userId");
        try {
            ctrlTool.projPerm(request, "项目.移动验房.问题管理.编辑");
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
    @RequestMapping(value = "add_desc", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse addDesc(HttpServletRequest request, @RequestParam(value = "project_id", required = true) Integer projectId,
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
    @RequestMapping(value = "edit_plan_end_on", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse editPlanEndOn(HttpServletRequest request, @RequestParam(value = "project_id", required = true) Integer projectId,
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
    @RequestMapping(value = "edit_approve", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse editApprove(HttpServletRequest request, @RequestParam(value = "project_id", required = true) Integer projectId,
                                      @RequestParam(value = "issue_uuid", required = true) String issueUuid,
                                      @RequestParam(value = "status", required = true) Integer status,
                                      @RequestParam(value = "content", required = true) String content) {
        Integer userId = (Integer) sessionInfo.getBaseInfo("userId");
        try {
            ctrlTool.projPerm(request, "项目.移动验房.问题管理.编辑");
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
    @RequestMapping(value = "detail_repair_log", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<DetailRepairLogRspVo> detailRepairLog(HttpServletRequest request, @RequestParam(value = "project_id", required = true) Integer projectId,
                                                                @RequestParam(value = "issue_uuid", required = true) String issueUuid) {
        Integer userId = (Integer) sessionInfo.getBaseInfo("userId");
        try {
            ctrlTool.projPerm(request, "项目.工程检查.问题管理.查看");
        } catch (Exception e) {
            e.printStackTrace();
        }
        LjBaseResponse<List<HouseQmCheckTaskIssueDetailRepairLogVo>> result = iIssueService.getDetailRepairLogByIssueUuid(issueUuid);

        LjBaseResponse<DetailRepairLogRspVo> response = new LjBaseResponse<>();
        DetailRepairLogRspVo data = new DetailRepairLogRspVo();
        data.setItems(result.getData());
        response.setData(data);
        return response;
    }

    /**
     * 项目下问题详情
     *
     * @param projectId
     * @param issueUuid
     * @return
     */
    @RequestMapping(value = "detail_base", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<IssueInfoVo> detailBase(HttpServletRequest request, @RequestParam(value = "project_id", required = true) Integer projectId,
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

    //【项目-过程检查-问题管理-问题详情】其他信息编辑
    @RequestMapping(value = "edit_detail",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<Object> editDetail(HttpServletRequest request,@Validated EditDetailReq req){
        LjBaseResponse<Object> response = new LjBaseResponse<>();
        Integer userId = (Integer) sessionInfo.getBaseInfo("userId");
        try {
            ctrlTool.projPerm(request,"项目.工程检查.问题管理.编辑");
        } catch (Exception e) {
            e.printStackTrace();
            response.setResult(1);
            response.setMessage("PermissionDenied");
            return response;
        }

        return iIssueService.updateIssueDetailByProjectAndUuid(userId, req.getProject_id(), req.getIssue_uuid(), req.getTyp(), req.getData());
    }

}
