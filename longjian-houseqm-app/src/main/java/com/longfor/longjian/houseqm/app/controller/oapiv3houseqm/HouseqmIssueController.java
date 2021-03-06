package com.longfor.longjian.houseqm.app.controller.oapiv3houseqm;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.longfor.longjian.common.base.LjBaseResponse;
import com.longfor.longjian.common.consts.HouseQmCheckTaskIssueStatusEnum;
import com.longfor.longjian.common.exception.LjBaseRuntimeException;
import com.longfor.longjian.common.util.CtrlTool;
import com.longfor.longjian.common.util.SessionInfo;
import com.longfor.longjian.houseqm.app.feginclient.IHouseqmCheckTaskIssueFeignService;
import com.longfor.longjian.houseqm.app.req.issue.IssueBatchAppointReq;
import com.longfor.longjian.houseqm.app.req.issue.IssueBatchApproveReq;
import com.longfor.longjian.houseqm.app.req.issue.IssueBatchDeleteReq;
import com.longfor.longjian.houseqm.app.req.issue.IssueExportPdfReq;
import com.longfor.longjian.houseqm.app.service.IHouseqmIssueService;
import com.longfor.longjian.houseqm.app.utils.CtrlToolUtils;
import com.longfor.longjian.houseqm.app.vo.IssueBatchAppointRspVo;
import com.longfor.longjian.houseqm.app.vo.houseqmissue.IssueBatchApproveRspVo;
import com.longfor.longjian.houseqm.app.vo.houseqmissue.IssueBatchDeleteRspVo;
import com.longfor.longjian.houseqm.consts.ErrorEnum;
import com.longfor.longjian.houseqm.consts.HouseQmCheckTaskIssueCheckStatusEnum;
import com.longfor.longjian.houseqm.domain.internalservice.HouseQmCheckTaskIssueService;
import com.longfor.longjian.houseqm.domain.internalservice.HouseQmCheckTaskService;
import com.longfor.longjian.houseqm.po.zj2db.HouseQmCheckTask;
import com.longfor.longjian.houseqm.po.zj2db.HouseQmCheckTaskIssue;
import com.longfor.longjian.houseqm.po.zj2db.Project;
import com.longfor.longjian.houseqm.util.DateUtil;
import com.longfor.longjian.houseqm.util.StringSplitToListUtil;
import com.longfor.longjian.houseqm.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

@RestController
@RequestMapping("oapi/v3/houseqm/issue/")
@Slf4j
public class HouseqmIssueController {

    @Resource
    private IHouseqmIssueService iHouseqmIssueService;
    @Resource
    private HouseQmCheckTaskService houseQmCheckTaskService;
    @Resource
    private HouseQmCheckTaskIssueService houseQmCheckTaskIssueService;
    @Resource
    private CtrlTool ctrlTool;
    @Resource
    private IHouseqmCheckTaskIssueFeignService iHouseqmCheckTaskIssueFeignService;
    @Resource
    private SessionInfo sessionInfo;
    @Value("${stat_export_server_addr}")
    private String statExportServerAddr;

    private static final String USER_ID="userId";
    private static final String AUTH_PROJECT_QUESTION_MANAGE_EDIT="??????.????????????.????????????.??????";
    private static final String PROJECT_ENGINEERING_QUESTION_MANAGE_EDIT="??????.????????????.????????????.??????";

    @RequestMapping(value = "export_pdf", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @SuppressWarnings("squid:S3776")
    public LjBaseResponse exportPdf(HttpServletRequest request, @Validated IssueExportPdfReq req) throws IOException {
        LjBaseResponse<Object> response = new LjBaseResponse<>();
        try {
            ctrlTool.projPermMulti(request, new String[]{"??????.????????????.????????????.??????", "??????.????????????.????????????.??????"});
        } catch (Exception e) {
            log.error(e.getMessage());
            return CtrlToolUtils.errorReturn(response,e);
        }
        if (req.getTask_id() == null) req.setTask_id(0);
        Project proj = iHouseqmIssueService.getProjectByProjId(req.getProject_id());
        if (proj == null) {
            throw new LjBaseRuntimeException(ErrorEnum.DB_ITEM_UNFOUND.getCode(), ErrorEnum.DB_ITEM_UNFOUND.getMessage());
        }

        List<String> uuids = null;

        if (req.getUuids().trim().length() > 0) {
            uuids = StringUtil.strToStrs(req.getUuids(), ",");
        }

        StringBuilder taskName = new StringBuilder();
        List<HouseQmCheckTask> taskList = Lists.newArrayList();
        //?????????????????????task_id
        if (req.getTask_id() > 0) {
            HouseQmCheckTask houseQmCheckTask = houseQmCheckTaskService.getHouseQmCheckTaskByProjTaskId(req.getProject_id(), req.getTask_id());
            taskList.add(houseQmCheckTask);
        } else if (req.getUuids().length() > 0) {
            // ???uuid??????
            List<HouseQmCheckTaskIssue> issueList = Lists.newArrayList();
            try {
                issueList = iHouseqmIssueService.searchHouseQmIssueListByProjUuidIn(req.getProject_id(), uuids);
            } catch (Exception e) {
                log.error(e.getMessage());
            }
            HashMap<Integer, Boolean> taskMap = Maps.newHashMap();
            issueList.forEach(issue -> taskMap.put(issue.getTaskId(), true));
            List<Integer> taskIds = new ArrayList<>(taskMap.keySet());
            if (CollectionUtils.isNotEmpty(taskIds)) {
                taskList = houseQmCheckTaskService.searchHouseQmCheckTaskByTaskIdIn(taskIds);
            }
        } else {
            //??????
            taskList = houseQmCheckTaskService.selectByProjectIdAndCategoryCls(req.getProject_id(), req.getCategory_cls());
        }
        int taskLen = taskList.size();
        for (int i = 0; i < taskList.size(); i++) {
            taskName.append(taskList.get(i).getName());
            if (taskName.length() > 50) {
                taskName.append("???").append(taskLen).append("?????????");
                break;
            }
            if (i != taskLen - 1) {
                taskName.append("???");
            }
        }
        String url = statExportServerAddr + "/stat_export/houseqm_check_task_issue/index_json/?";
        // ??????
        Map<String, String> urlargs = Maps.newHashMap();
        urlargs.put("safeCallKey", "268494d141d8054585ef5943e75e49f2");
        urlargs.put("project_id", String.valueOf(req.getProject_id()));
        urlargs.put("task_id", String.valueOf(req.getTask_id()==null?0:req.getTask_id()));
        urlargs.put("task_name", taskName.toString());
        urlargs.put("checker_id", String.valueOf(req.getChecker_id()==null?0:req.getChecker_id()));
        urlargs.put("repairer_id", String.valueOf(req.getRepairer_id()==null?0:req.getRepairer_id()));
        urlargs.put("create_on_begin", req.getCreate_on_begin()==null?"":req.getCreate_on_begin());
        urlargs.put("create_on_end", req.getCreate_on_end()==null?"":req.getCreate_on_end());
        urlargs.put("category_key", req.getCategory_key()==null?"":req.getCategory_key());
        urlargs.put("check_item_key", req.getCheck_item_key()==null?"":req.getCheck_item_key());
        urlargs.put("type", String.valueOf(req.getType()==null?0:req.getType()));
        urlargs.put("condition", String.valueOf(req.getCondition()==null?0:req.getCondition()));
        urlargs.put("is_overdue", String.valueOf(req.getIs_overdue()==null?false:req.getIs_overdue()));
        urlargs.put("category_cls", String.valueOf(req.getCategory_cls()));

        if (CollectionUtils.isNotEmpty(req.getStatus_in())) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < req.getStatus_in().size(); i++) {
                if (i == 0) {
                    sb.append(req.getStatus_in().get(i));
                } else {
                    sb.append("status_in=").append(req.getStatus_in().get(i));
                }
                if (i < req.getStatus_in().size() - 1) {
                    sb.append("&");
                }
            }
            urlargs.put("status_in", sb.toString());
        } else {
            urlargs.put("status_in", "");
        }

        if (CollectionUtils.isNotEmpty(req.getArea_ids())) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < req.getArea_ids().size(); i++) {
                if (i == 0) {
                    sb.append(req.getArea_ids().get(i));
                } else {
                    sb.append("area_ids=").append(req.getArea_ids().get(i));
                }
                if (i < req.getArea_ids().size() - 1) {
                    sb.append("&");
                }
            }
            urlargs.put("area_ids", sb.toString());
        } else {
            urlargs.put("area_ids", "");
        }
        List<String> uuidList = StringUtil.strToStrs(req.getUuids(), ",");
        if (CollectionUtils.isNotEmpty(uuidList)) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < uuidList.size(); i++) {
                if (i == 0) {
                    sb.append(uuidList.get(i));
                } else {
                    sb.append("uuids=").append(uuidList.get(i));
                }
                if (i < uuidList.size() - 1) {
                    sb.append("&");
                }
            }
            urlargs.put("uuids", sb.toString());
        } else {
            urlargs.put("uuids", "");
        }

        String urlargsStr = buildMap(urlargs);
        url += urlargsStr;
        Integer userId = (Integer) sessionInfo.getBaseInfo(USER_ID);

        Map<String, String> args = Maps.newHashMap();
        args.put("url", url);
        String nowTime = DateUtil.getNowTimeStr("yyyyMMddhhmm");
        String exportName = "???" + proj.getName() + "???????????????." + nowTime + ".pdf";
        // ???????????????????????????????????????
        iHouseqmIssueService.create(userId, proj.getTeamId(), req.getProject_id(), 0, args, exportName, new Date());
        return response;
    }


    @RequestMapping(value = "batch_appoint", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<IssueBatchAppointRspVo> batchAppoint(HttpServletRequest request, @Valid IssueBatchAppointReq req) {
        LjBaseResponse<IssueBatchAppointRspVo> response = new LjBaseResponse<>();
        try {
            ctrlTool.projPermMulti(request, new String[]{AUTH_PROJECT_QUESTION_MANAGE_EDIT, PROJECT_ENGINEERING_QUESTION_MANAGE_EDIT});
            if (req.getRepairer_id() == null) req.setRepairer_id(0);
            if (req.getPlan_end_on() == null) req.setPlan_end_on(0);
            // ???????????????task?????????????????????????????????????????????????????????
            List<String> issueUuids = StringSplitToListUtil.splitToStringComma(req.getIssue_uuids(), ",");
            List<HouseQmCheckTaskIssue> issues = houseQmCheckTaskIssueService.searchHouseQmCheckTaskIssueByTaskIdUuidIn(req.getTask_id(), issueUuids);
            List<String> uuids = Lists.newArrayList();
            for (HouseQmCheckTaskIssue issue : issues) {
                if (issue.getStatus().equals(HouseQmCheckTaskIssueStatusEnum.CheckYes.getId())) {
                    throw new LjBaseRuntimeException(-1,"????????????????????????????????????");
                }
                if (req.getProject_id().equals(issue.getProjectId())) {
                    uuids.add(issue.getUuid());
                }
            }
            Integer userId = (Integer) sessionInfo.getBaseInfo(USER_ID);
            List<String> fails = iHouseqmIssueService.updateBatchIssueRepairInfoByUuids(uuids, req.getProject_id(), userId, req.getRepairer_id(), req.getRepair_follower_ids(), req.getPlan_end_on());
            IssueBatchAppointRspVo data = new IssueBatchAppointRspVo();
            data.setFails(fails);
            response.setData(data);
        } catch (Exception e) {
            log.error(e.getMessage());
            response.setResult(1);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    @RequestMapping(value = "batch_approve", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<IssueBatchApproveRspVo> batchApprove(HttpServletRequest request, @Validated IssueBatchApproveReq req) {
        LjBaseResponse<IssueBatchApproveRspVo> response = new LjBaseResponse<>();
        try {
            ctrlTool.projPermMulti(request, new String[]{AUTH_PROJECT_QUESTION_MANAGE_EDIT, PROJECT_ENGINEERING_QUESTION_MANAGE_EDIT});
            // ???????????????task?????????????????????????????????????????????????????????
            List<String> uuids = filterIssueUuidByProjIdTaskIdUuids(req.getProject_id(), req.getTask_id(), req.getIssue_uuids());
            Integer userId = (Integer) sessionInfo.getBaseInfo(USER_ID);
            List<String> fails = iHouseqmIssueService.updateBatchIssueApproveStatusByUuids(uuids, req.getProject_id(), userId, HouseQmCheckTaskIssueCheckStatusEnum.CheckYes.getId(), "", "");
            IssueBatchApproveRspVo data = new IssueBatchApproveRspVo();
            data.setFails(fails);
            response.setData(data);
        } catch (Exception e) {
            log.error(e.getMessage());
            response.setResult(1);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    @RequestMapping(value = "batch_delete", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<IssueBatchDeleteRspVo> batchDelete(HttpServletRequest request, @Validated IssueBatchDeleteReq req) {
        LjBaseResponse<IssueBatchDeleteRspVo> response = new LjBaseResponse<>();
        try {
            ctrlTool.projPermMulti(request, new String[]{AUTH_PROJECT_QUESTION_MANAGE_EDIT, PROJECT_ENGINEERING_QUESTION_MANAGE_EDIT});
        } catch (Exception e) {
            log.error(e.getMessage());
            log.error(e.getMessage());
            return CtrlToolUtils.errorReturn(response,e);
        }
            List<String> issueUuids = StringUtil.strToStrs(req.getIssue_uuids(), ",");
            IssueBatchDeleteRspVo data = new IssueBatchDeleteRspVo();
            List<String> fails = Lists.newArrayList();
            for (String issueUuid : issueUuids) {
                try {
                    iHouseqmIssueService.deleteHouseQmCheckTaskIssueByProjUuid(req.getProject_id(), issueUuid);
                } catch (Exception e) {
                    fails.add(issueUuid);
                }
            }
            data.setFails(fails);
            response.setData(data);
        return response;
    }

    private List<String> filterIssueUuidByProjIdTaskIdUuids(int projId, int taskId, String uuidStr) {
        List<String> issueUuids = StringUtil.strToStrs(uuidStr, ",");
        if (issueUuids.isEmpty()) return Lists.newArrayList();
        List<HouseQmCheckTaskIssue> issues = houseQmCheckTaskIssueService.searchHouseQmCheckTaskIssueByTaskIdUuidIn(taskId, issueUuids);
        List<String> uuids = Lists.newArrayList();
        for (HouseQmCheckTaskIssue issue : issues) {
            if (issue.getProjectId().equals(projId)) {
                uuids.add(issue.getUuid());
            }
        }
        return uuids;
    }

    // url????????????
    private String buildMap(Map<String, String> map) {
        StringBuilder sb = new StringBuilder();
        if (map.size() > 0) {
            for (String key : map.keySet()) {
                sb.append(key).append("=");
                if (StringUtils.isEmpty(map.get(key))) {
                    sb.append("&");
                } else {
                    String value = map.get(key);
                    try {
                        value = URLEncoder.encode(value, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        log.error(e.getMessage());
                    }
                    sb.append(value).append("&");
                }
            }
        }
        return sb.toString();
    }


}
