package com.longfor.longjian.houseqm.app.controller.oapiv3houseqm;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.longfor.longjian.common.base.LjBaseResponse;
import com.longfor.longjian.common.exception.LjBaseRuntimeException;
import com.longfor.longjian.common.util.CtrlTool;
import com.longfor.longjian.common.util.SessionInfo;
import com.longfor.longjian.houseqm.app.feginClient.IHouseqmCheckTaskIssueFeignService;
import com.longfor.longjian.houseqm.app.req.issue.IssueBatchAppointReq;
import com.longfor.longjian.houseqm.app.req.issue.IssueBatchApproveReq;
import com.longfor.longjian.houseqm.app.req.issue.IssueBatchDeleteReq;
import com.longfor.longjian.houseqm.app.req.issue.IssueExportPdfReq;
import com.longfor.longjian.houseqm.app.service.IHouseqmIssueService;
import com.longfor.longjian.houseqm.app.vo.IssueBatchAppointRspVo;
import com.longfor.longjian.houseqm.app.vo.houseqmissue.IssueBatchApproveRspVo;
import com.longfor.longjian.houseqm.app.vo.houseqmissue.IssueBatchDeleteRspVo;
import com.longfor.longjian.houseqm.consts.ErrorEnum;
import com.longfor.longjian.houseqm.consts.HouseQmCheckTaskIssueCheckStatusEnum;
import com.longfor.longjian.common.consts.HouseQmCheckTaskIssueStatusEnum;
import com.longfor.longjian.houseqm.domain.internalService.HouseQmCheckTaskIssueService;
import com.longfor.longjian.houseqm.domain.internalService.HouseQmCheckTaskService;
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
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.app.controller
 * @ClassName: HouseqmIssueController
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/1/10 17:02
 */
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

    /**
     * @return com.longfor.longjian.common.base.LjBaseResponse
     * @Author hy
     * @Description 项目下问题导出PDF到任务
     * http://192.168.37.159:3000/project/8/interface/api/3356
     * @Date 17:13 2019/1/10
     * @Param [req]
     **/
    @RequestMapping(value = "export_pdf", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse exportPdf(HttpServletRequest request, @Validated IssueExportPdfReq req) throws Exception {
        LjBaseResponse<Object> response = new LjBaseResponse<>();
        try {
            ctrlTool.projPermMulti(request, new String[]{"项目.移动验房.问题管理.查看", "项目.工程检查.问题管理.查看"});
        } catch (Exception e) {
            log.error(e.getMessage());
            response.setResult(1);
            response.setMessage(e.getMessage());
        }
        if (req.getTask_id()==null)req.setTask_id(0);
        Project proj = iHouseqmIssueService.getProjectByProjId(req.getProject_id());
        if (proj == null) {
            throw new LjBaseRuntimeException(ErrorEnum.DB_ITEM_UNFOUND.getCode(), ErrorEnum.DB_ITEM_UNFOUND.getMessage());
        }

        List<String> uuids = null;

        if (req.getUuids().trim().length() > 0) {
            uuids = StringSplitToListUtil.splitToStringComma(req.getUuids(), ",");
        }

        StringBuilder taskName = new StringBuilder();
        List<HouseQmCheckTask> taskList = Lists.newArrayList();
        //优先看有没有传task_id
        if (req.getTask_id() > 0) {
            HouseQmCheckTask houseQmCheckTask = houseQmCheckTaskService.getHouseQmCheckTaskByProjTaskId(req.getProject_id(), req.getTask_id());
            taskList.add(houseQmCheckTask);
        } else if (req.getUuids().length() > 0) {
            // 按uuid提取
            List<HouseQmCheckTaskIssue> issueList = Lists.newArrayList();
            try {
                issueList = iHouseqmIssueService.searchHouseQmIssueListByProjUuidIn(req.getProject_id(), uuids);
            } catch (Exception e) {
                log.error(e.getMessage());
            }
            HashMap<Integer, Boolean> taskMap = Maps.newHashMap();
            issueList.forEach(issue -> taskMap.put(issue.getTaskId(), true));
            List<Integer> taskIds = new ArrayList<>(taskMap.keySet());
            if (taskIds.size() > 0) {
                taskList = houseQmCheckTaskService.searchHouseQmCheckTaskByTaskIdIn(taskIds);
            }
        } else {
            //全取
            taskList = houseQmCheckTaskService.selectByProjectIdAndCategoryCls(req.getProject_id(), req.getCategory_cls());
        }
        int taskLen = taskList.size();
        for (int i = 0; i < taskList.size(); i++) {
            taskName.append(taskList.get(i).getName());
            if (taskName.length() > 50) {
                taskName.append("等").append(taskLen).append("个任务");
                break;
            }
            if (i != taskLen - 1) {
                taskName.append("、");
            }
        }
        String url = statExportServerAddr + "/stat_export/houseqm_check_task_issue/index_json/?";
        // 参数
        Map<String, String> urlargs = Maps.newHashMap();
        urlargs.put("safeCallKey", "268494d141d8054585ef5943e75e49f2");
        urlargs.put("project_id", String.valueOf(req.getProject_id()));
        urlargs.put("task_id", String.valueOf(req.getTask_id()));
        urlargs.put("task_name", taskName.toString());
        urlargs.put("checker_id", String.valueOf(req.getChecker_id()));
        urlargs.put("repairer_id", String.valueOf(req.getRepairer_id()));
        urlargs.put("create_on_begin", req.getCreate_on_begin());
        urlargs.put("create_on_end", req.getCreate_on_end());
        urlargs.put("category_key", req.getCategory_key());
        urlargs.put("check_item_key", req.getCheck_item_key());
        urlargs.put("type", String.valueOf(req.getType()));
        urlargs.put("condition", String.valueOf(req.getCondition()));
        urlargs.put("is_overdue", String.valueOf(req.getIs_overdue()));
        urlargs.put("category_cls", String.valueOf(req.getCategory_cls()));

        if (CollectionUtils.isNotEmpty(req.getStatus_in())) {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < req.getStatus_in().size(); i++) {
                if (i==0){
                    sb.append(req.getStatus_in().get(i));
                }else {
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
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < req.getArea_ids().size(); i++) {
                if (i==0){
                    sb.append(req.getArea_ids().get(i));
                }else {
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
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < uuidList.size(); i++) {
                if (i==0){
                    sb.append(uuidList.get(i));
                }else {
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
        Integer userId = (Integer) sessionInfo.getBaseInfo("userId");

        Map<String, String> args = Maps.newHashMap();
        args.put("url", url);
        String nowTime = DateUtil.getNowTimeStr("yyyyMMddhhmm");
        String exportName = "【" + proj.getName() + "】整改报告." + nowTime + ".pdf";
        // 把导出的信息插入到数据库中
        iHouseqmIssueService.create(userId, proj.getTeamId(), req.getProject_id(), 0, args, exportName, new Date());
        return response;
    }

    /**
     * @return com.longfor.longjian.common.base.LjBaseResponse<com.longfor.longjian.houseqm.app.vo.IssueBatchAppointRspVo>
     * @Author hy
     * @Description 批量指派issue
     * http://192.168.37.159:3000/project/8/interface/api/3320
     * @Date 14:03 2019/1/11
     * @Param [req]
     **/
    @RequestMapping(value = "batch_appoint", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<IssueBatchAppointRspVo> batchAppoint(HttpServletRequest request, @Valid IssueBatchAppointReq req) throws Exception {
        LjBaseResponse<IssueBatchAppointRspVo> response = new LjBaseResponse<>();
        try {
           ctrlTool.projPermMulti(request, new String[]{"项目.移动验房.问题管理.编辑", "项目.工程检查.问题管理.编辑"});
            if (req.getRepairer_id() == null) req.setRepairer_id(0);
            if (req.getPlan_end_on() == null) req.setPlan_end_on(0);
            // 过滤掉不同task下的问题，感觉有点多余，不过还是处理下
            List<String> issueUuids = StringSplitToListUtil.splitToStringComma(req.getIssue_uuids(), ",");
            List<HouseQmCheckTaskIssue> issues = houseQmCheckTaskIssueService.searchHouseQmCheckTaskIssueByTaskIdUuidIn(req.getTask_id(), issueUuids);
            List<String> uuids = Lists.newArrayList();
            for (HouseQmCheckTaskIssue issue : issues) {
                if (issue.getStatus().equals(HouseQmCheckTaskIssueStatusEnum.CheckYes.getId())) {
                    throw new Exception("有问题已销项，不能被指派");
                }
                if (req.getProject_id().equals(issue.getProjectId())) {
                    uuids.add(issue.getUuid());
                }
            }
            Integer userId = (Integer) sessionInfo.getBaseInfo("userId");
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

    /**
     * @return com.longfor.longjian.common.base.LjBaseResponse<com.longfor.longjian.houseqm.app.vo.houseqmissue.IssueBatchApproveRspVo>
     * @Author hy
     * @Description 项目下我的问题批量销项
     * http://192.168.37.159:3000/project/8/interface/api/3376
     * @Date 18:45 2019/1/12
     * @Param [req]
     **/
    @RequestMapping(value = "batch_approve", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<IssueBatchApproveRspVo> batchApprove(HttpServletRequest request, @Validated IssueBatchApproveReq req) throws Exception {
        LjBaseResponse<IssueBatchApproveRspVo> response = new LjBaseResponse<>();
        try {
            ctrlTool.projPermMulti(request, new String[]{"项目.移动验房.问题管理.编辑", "项目.工程检查.问题管理.编辑"});
            // 过滤掉不同task下的问题，感觉有点多余，不过还是处理下
            List<String> uuids = filterIssueUuidByProjIdTaskIdUuids(req.getProject_id(), req.getTask_id(), req.getIssue_uuids());
            Integer userId = (Integer) sessionInfo.getBaseInfo("userId");
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

    /**
     * @return com.longfor.longjian.common.base.LjBaseResponse<com.longfor.longjian.houseqm.app.vo.houseqmissue.IssueBatchDeleteRspVo>
     * @Author hy
     * @Description 项目下问题管理批量删除
     * http://192.168.37.159:3000/project/8/interface/api/3348
     * @Date 18:49 2019/1/12
     * @Param [req]
     **/
    @RequestMapping(value = "batch_delete", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<IssueBatchDeleteRspVo> batchDelete(HttpServletRequest request, @Validated IssueBatchDeleteReq req) {
        LjBaseResponse<IssueBatchDeleteRspVo> response = new LjBaseResponse<>();
        try {
            ctrlTool.projPermMulti(request, new String[]{"项目.移动验房.问题管理.编辑", "项目.工程检查.问题管理.编辑"});
            List<String> issueUuids = StringSplitToListUtil.splitToStringComma(req.getIssue_uuids(), ",");
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
        } catch (Exception e) {
            log.error(e.getMessage());
            log.error(e.getMessage());
            response.setResult(1);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    private List<String> filterIssueUuidByProjIdTaskIdUuids(int projId, int taskId, String uuidStr) {
        List<String> issueUuids = StringSplitToListUtil.splitToStringComma(uuidStr, ",");
        if (issueUuids.size() == 0) return null;
        List<HouseQmCheckTaskIssue> issues = houseQmCheckTaskIssueService.searchHouseQmCheckTaskIssueByTaskIdUuidIn(taskId, issueUuids);
        List<String> uuids = Lists.newArrayList();
        for (HouseQmCheckTaskIssue issue : issues) {
            if (issue.getProjectId().equals(projId)) {
                uuids.add(issue.getUuid());
            }
        }
        return uuids;
    }

    // url参数拼接
    public String buildMap(Map<String, String> map) {
        StringBuffer sb = new StringBuffer();
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
                    sb.append(value + "&");
                }
            }
        }
        return sb.toString();
    }


}
