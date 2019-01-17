package com.longfor.longjian.houseqm.app.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.longfor.longjian.common.base.LjBaseResponse;
import com.longfor.longjian.common.exception.LjBaseRuntimeException;
import com.longfor.longjian.common.util.CtrlTool;
import com.longfor.longjian.houseqm.app.req.issue.IssueBatchAppointReq;
import com.longfor.longjian.houseqm.app.req.issue.IssueBatchApproveReq;
import com.longfor.longjian.houseqm.app.req.issue.IssueBatchDeleteReq;
import com.longfor.longjian.houseqm.app.req.issue.IssueExportPdfReq;
import com.longfor.longjian.houseqm.app.service.IHouseqmIssueService;
import com.longfor.longjian.houseqm.app.vo.IssueBatchAppointRspVo;
import com.longfor.longjian.houseqm.app.vo.issue.IssueBatchApproveRspVo;
import com.longfor.longjian.houseqm.app.vo.issue.IssueBatchDeleteRspVo;
import com.longfor.longjian.houseqm.consts.ErrorEnum;
import com.longfor.longjian.houseqm.consts.HouseQmCheckTaskIssueCheckStatusEnum;
import com.longfor.longjian.common.consts.HouseQmCheckTaskIssueStatusEnum;
import com.longfor.longjian.houseqm.domain.internalService.HouseQmCheckTaskIssueService;
import com.longfor.longjian.houseqm.domain.internalService.HouseQmCheckTaskService;
import com.longfor.longjian.houseqm.po.HouseQmCheckTask;
import com.longfor.longjian.houseqm.po.HouseQmCheckTaskIssue;
import com.longfor.longjian.houseqm.po.zj2db.Project;
import com.longfor.longjian.houseqm.util.DateUtil;
import com.longfor.longjian.houseqm.util.StringSplitToListUtil;
import com.longfor.longjian.houseqm.app.vo.url.UrlUtils;
import com.sun.jndi.toolkit.url.UrlUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

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
    @PostMapping(value = "export_pdf/", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse exportPdf(HttpServletRequest request, @Valid IssueExportPdfReq req) {
        //todo 鉴权 _, _, err = ctrl_tool.ProjPermMulti(c, []string{"项目.移动验房.问题管理.查看", "项目.工程检查.问题管理.查看"})
        /*try {
            ctrlTool.projPermMulti(request,new String[]{"项目.移动验房.问题管理.查看", "项目.工程检查.问题管理.查看"});
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }*/
        Project proj = iHouseqmIssueService.getProjectByProjId(req.getProject_id());
        if (proj == null)
            throw new LjBaseRuntimeException(ErrorEnum.DB_ITEM_UNFOUND.getCode(), ErrorEnum.DB_ITEM_UNFOUND.getMessage());

        List<String> uuids = null;
        if (req.getUuids().trim().length() > 0) {
            uuids = StringSplitToListUtil.splitToStringComma(req.getUuids(), ",");
        }

        String taskName = "";
        List<HouseQmCheckTask> taskList = Lists.newArrayList();
        //优先看有没有传task_id
        if (req.getTask_id() > 0) {
            HouseQmCheckTask houseQmCheckTask = houseQmCheckTaskService.getHouseQmCheckTaskByProjTaskId(req.getProject_id(), req.getTask_id());
            taskList.add(houseQmCheckTask);
        } else if (req.getUuids().length() > 0) {
            // 按uuid提取
            List<HouseQmCheckTaskIssue> issueList = null;
            try {
                issueList = iHouseqmIssueService.searchHouseQmIssueListByProjUuidIn(req.getProject_id(), uuids);
            } catch (Exception e) {
                e.printStackTrace();
            }
            HashMap<Integer, Boolean> taskMap = Maps.newHashMap();
            issueList.forEach(issue -> taskMap.put(issue.getTaskId(), true));
            List<Integer> taskIds = taskMap.keySet().stream().collect(Collectors.toList());
            if (taskIds.size() > 0) {
                taskList = houseQmCheckTaskService.searchHouseQmCheckTaskByTaskIdIn(taskIds);
            }
        } else {
            //全取
            taskList = houseQmCheckTaskService.selectByProjectIdAndCategoryCls(req.getProject_id(), req.getCategory_cls());
        }
        int taskLen = taskList.size();
        for (int i = 0; i < taskList.size(); i++) {
            taskName += taskList.get(i).getName();
            if (taskName.length() > 50) {
                taskName += "等" + taskLen + "个任务";
                break;
            }
            if (i != taskLen - 1) {
                taskName += "、";
            }
        }
        // 导出项配置
        UrlUtils.getUrl(statExportServerAddr + "/stat_export/houseqm_check_task_issue/index_json/");

        //todo  设置url参数 即要导出的数据


        // 获取userid //c.MustGet("user").(*zj3user_models.User).Id
        int userId = 1;
        Map<String, String> args = Maps.newHashMap();
        args.put("url", "");

        String nowTime = DateUtil.getNowTimeStr("yyyyMMddhhmm");
        String exportName = "【" + proj.getName() + "】整改报告." + nowTime + ".pdf";
        // todo 把导出的信息插入到数据库中
        iHouseqmIssueService.create(userId, proj.getTeamId(), req.getProject_id(), 0, args, exportName, new Date());


        LjBaseResponse<Object> response = new LjBaseResponse<>();
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
    @PostMapping(value = "batch_appoint/", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<IssueBatchAppointRspVo> batchAppoint(HttpServletRequest request, @Valid IssueBatchAppointReq req) throws Exception {
        //todo 鉴权 _, _, err := ctrl_tool.ProjPermMulti(c, []string{"项目.移动验房.问题管理.编辑", "项目.工程检查.问题管理.编辑"})
       /* try {
            ctrlTool.projPermMulti(request,new String[]{"项目.移动验房.问题管理.编辑", "项目.工程检查.问题管理.编辑"});
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }*/
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
        //todo 获取userid
        int uid = 7566;
        List<String> fails = iHouseqmIssueService.updateBatchIssueRepairInfoByUuids(uuids, req.getProject_id(), uid, req.getRepairer_id(), req.getRepair_follower_ids(), req.getPlan_end_on());
        LjBaseResponse<IssueBatchAppointRspVo> response = new LjBaseResponse<>();
        IssueBatchAppointRspVo data = new IssueBatchAppointRspVo();
        data.setFails(fails);
        response.setData(data);
        return response;
    }

    /**
     * @return com.longfor.longjian.common.base.LjBaseResponse<com.longfor.longjian.houseqm.app.vo.issue.IssueBatchApproveRspVo>
     * @Author hy
     * @Description 项目下我的问题批量销项
     * http://192.168.37.159:3000/project/8/interface/api/3376
     * @Date 18:45 2019/1/12
     * @Param [req]
     **/
    @PostMapping(value = "batch_approve/", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<IssueBatchApproveRspVo> batchApprove(HttpServletRequest request, IssueBatchApproveReq req) throws Exception {
        //todo 鉴权 String[] perms=new String[]{"项目.移动验房.问题管理.编辑","项目.工程检查.问题管理.编辑"};
        /*try {
            ctrlTool.projPermMulti(request,new String[]{"项目.移动验房.问题管理.编辑", "项目.工程检查.问题管理.编辑"});
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }*/

        // 过滤掉不同task下的问题，感觉有点多余，不过还是处理下
        List<String> uuids = filterIssueUuidByProjIdTaskIdUuids(req.getProject_id(), req.getTask_id(), req.getIssue_uuids());

        //todo uid  uId := getCurUid(c)
        int uid = 7556;

        List<String> fails = iHouseqmIssueService.updateBatchIssueApproveStatusByUuids(uuids, req.getProject_id(), uid, HouseQmCheckTaskIssueCheckStatusEnum.CheckYes.getId(), "", "");
        LjBaseResponse<IssueBatchApproveRspVo> response = new LjBaseResponse<>();
        IssueBatchApproveRspVo data = new IssueBatchApproveRspVo();
        data.setFails(fails);
        response.setData(data);
        return response;
    }

    /**
     * @return com.longfor.longjian.common.base.LjBaseResponse<com.longfor.longjian.houseqm.app.vo.issue.IssueBatchDeleteRspVo>
     * @Author hy
     * @Description 项目下问题管理批量删除
     * http://192.168.37.159:3000/project/8/interface/api/3348
     * @Date 18:49 2019/1/12
     * @Param [req]
     **/
    @PostMapping(value = "batch_delete/", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<IssueBatchDeleteRspVo> batchDelete(HttpServletRequest request, IssueBatchDeleteReq req) {
        //todo 鉴权 String[] perms=new String[]{"项目.移动验房.问题管理.编辑","项目.工程检查.问题管理.编辑"};
        /*try {
            ctrlTool.projPermMulti(request,new String[]{"项目.移动验房.问题管理.编辑", "项目.工程检查.问题管理.编辑"});
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }*/
        List<String> issueUuids = StringSplitToListUtil.splitToStringComma(req.getIssue_uuids(), ",");
        LjBaseResponse<IssueBatchDeleteRspVo> response = new LjBaseResponse<>();
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

    public List<String> filterIssueUuidByProjIdTaskIdUuids(int projId, int taskId, String uuidStr) {
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

}
