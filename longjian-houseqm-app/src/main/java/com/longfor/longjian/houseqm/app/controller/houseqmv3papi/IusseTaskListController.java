package com.longfor.longjian.houseqm.app.controller.houseqmv3papi;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.longfor.longjian.common.base.LjBaseResponse;
import com.longfor.longjian.common.util.CtrlTool;
import com.longfor.longjian.common.util.RequestContextHolderUtil;
import com.longfor.longjian.common.util.SessionInfo;
import com.longfor.longjian.houseqm.app.controller.buildingqmv3papi.IssueListController;
import com.longfor.longjian.houseqm.app.req.EditDetailReq;
import com.longfor.longjian.houseqm.app.req.IssueListDoActionReq;
import com.longfor.longjian.houseqm.app.service.IIssueService;
import com.longfor.longjian.houseqm.app.service.IusseTaskListService;
import com.longfor.longjian.houseqm.app.utils.SessionUtil;
import com.longfor.longjian.houseqm.app.vo.*;
import com.longfor.longjian.houseqm.app.vo.issuelist.DetailLogRspVo;
import com.longfor.longjian.houseqm.app.vo.issuelist.DetailRepairLogRspVo;
import com.longfor.longjian.houseqm.app.vo.issuelist.IssueListRsp;
import com.longfor.longjian.houseqm.po.zj2db.ProjectSettingV2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * http://192.168.37.159:3000/project/8/interface/api/282  获取可用于检索的任务列表
 * http://192.168.37.159:3000/project/8/interface/api/1524 项目下季度检查获取任务列表
 * post请求，路径与get请求一致 参数不同 未找到py代码 搁置
 *
 * @author lipeishuai
 * @date 2018/11/17 15:07
 */

@RestController
@RequestMapping("houseqm/v3/papi/")
@Slf4j
public class IusseTaskListController {

    @Resource
    IssueListController issueListController;
    @Resource
    private IusseTaskListService iusseTaskListService;
    @Resource
    private CtrlTool ctrlTool;
    @Resource
    private SessionInfo sessionInfo;
    @Resource
    private IIssueService iIssueService;

    /**
     * 获取可用于检索的任务列表
     *
     * @param projectId
     * @param categoryCls
     * @param
     * @return
     */
    @RequestMapping(value = "issue/task_list", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public TaskResponse<HouseQmCheckTaskSimpleRspVo.TaskList> doAction(HttpServletRequest request, @RequestParam(value = "project_id") Integer projectId,
                                                                       @RequestParam(value = "category_cls") Integer categoryCls
    ) {
        try {
            ctrlTool.projPerm(request, "项目.移动验房.问题管理.查看");
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        //通过id projectId 判断
        TaskResponse<HouseQmCheckTaskSimpleRspVo.TaskList> response = new TaskResponse<>();
        List<HouseQmCheckTaskSimpleRspVo> vos = iusseTaskListService.selectByProjectIdAndCategoryCls(projectId, categoryCls);
        HouseQmCheckTaskSimpleRspVo.TaskList taskList = new HouseQmCheckTaskSimpleRspVo().new TaskList();
        taskList.setTask_list(vos);
        response.setData(taskList);
        return response;

    }

    /**
     * http://192.168.37.159:3000/project/8/interface/api/3304  获取验房验收项目配置列表
     */

    @RequestMapping(value = "stat_houseqm/get_acceptanceitems_setting", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<ApiStatHouseqmMeterSettingMsgVo.HouseqmMeterSetting> getAcceptanceitemsSetting(@RequestParam(name = "project_ids", required = true) String projectIds,
                                                                                                         @RequestParam(name = "timestamp", required = false, defaultValue = "0") Integer timestamp) {
        log.info("get_acceptanceitems_setting, project_ids=" + projectIds + ", timestamp=" + timestamp + "");
        List<ApiStatHouseqmMeterSettingMsgVo> acceptanceItems = iusseTaskListService.getAcceptanceitemsSetting(projectIds, timestamp);
        LjBaseResponse<ApiStatHouseqmMeterSettingMsgVo.HouseqmMeterSetting> response = new LjBaseResponse<>();
        ApiStatHouseqmMeterSettingMsgVo.HouseqmMeterSetting setting = new ApiStatHouseqmMeterSettingMsgVo().new HouseqmMeterSetting();
        setting.setAcceptance_items(acceptanceItems);
        response.setData(setting);
        return response;
    }

    /**
     * http://192.168.37.159:3000/project/8/interface/api/3308 获取“我”的公司与项目列表
     *
     * @param categorys
     * @return
     */
    @RequestMapping(value = "mine/teams_and_projects", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<ApiMineMsg> teamsAndProjects(@RequestParam(name = "categorys", required = false, defaultValue = "26,28") String categorys) {
        log.info("teams_and_projects, categorys=" + categorys + "");
        Integer userId = SessionUtil.getUid(sessionInfo);
        ApiMineMsg apiMineMsg = iusseTaskListService.teamsAndProjects(userId, categorys);
        LjBaseResponse<ApiMineMsg> response = new LjBaseResponse<>();
        response.setData(apiMineMsg);
        return response;
    }

    // 工程检查- 我的问题
    @RequestMapping(value = "issue/list", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<IssueListRsp> list(HttpServletRequest request, @Validated IssueListDoActionReq req) {
        LjBaseResponse<IssueListRsp> response = new LjBaseResponse<>();

        log.info("list," + JSON.toJSONString(req));
        try {
            ctrlTool.projPerm(request, "项目.移动验房.问题管理.查看");
        } catch (Exception e) {
            log.error("我的问题 鉴权异常:", e.getMessage());
            response.setResult(1);
            response.setCode(1);
            response.setMessage("PermissionDenied");
            return response;
        }
        IssueListRsp result = iIssueService.list(req);

        response.setData(result);
        return response;
    }

    // 工程检查- 我的问题 -问题详情
    @RequestMapping(value = "issue/configs", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<ProjectSettingConfigVo> configs(HttpServletRequest request, @RequestParam(value = "project_id", required = true) Integer projectId) {
        LjBaseResponse<ProjectSettingConfigVo> response = new LjBaseResponse<>();
        try {
            ctrlTool.projPerm(request, "项目.工程检查.问题管理.查看");
        } catch (Exception e) {
            log.error(e.getMessage());
            response.setResult(1);
            response.setMessage(e.getMessage());
        }
        List<ProjectSettingConfigVo.HouseQmIssueReason> reasonList = Lists.newArrayList();
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
                ProjectSettingConfigVo.HouseQmIssueReason singleReason = new ProjectSettingConfigVo().new HouseQmIssueReason();
                singleReason.setId(projectSetting.get(i).getId());
                singleReason.setValue(projectSetting.get(i).getValue());
                reasonList.add(singleReason);
            }
        }
        if (reasonId > 0) {
            for (int i = 0; i < projectSetting.size(); i++) {
                if (projectSetting.get(i).getParentId().equals(reasonId)) {
                    ProjectSettingConfigVo.HouseQmIssueReason singleReason = new ProjectSettingConfigVo().new HouseQmIssueReason();
                    singleReason.setId(projectSetting.get(i).getId());
                    singleReason.setValue(projectSetting.get(i).getValue());
                    reasonList.add(singleReason);
                }
            }
        }
        vo.setReason_list(reasonList);
        response.setData(vo);
        return response;
    }

    //工程检查- 我的问题 项目下问题详情
    @RequestMapping(value = "issue/detail_base", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<IssueInfoVo> detailBase(HttpServletRequest request, @RequestParam(value = "project_id", required = true) Integer projectId,
                                                  @RequestParam(value = "issue_uuid", required = true) String issueUuid) {
        Integer userId = SessionUtil.getUid(sessionInfo);
        try {
            ctrlTool.projPerm(request, "项目.工程检查.问题管理.查看");
        } catch (Exception e) {
            log.error("我的问题-项目下问题详情鉴权:", e.getMessage());
        }

        return iIssueService.getHouseQmCheckTaskIssueDetailBaseByProjectAndUuid(userId, projectId, issueUuid);
    }

    //工程检查- 我的问题 -项目下问题详情追加描述
    @RequestMapping(value = "issue/add_desc", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse addDesc(HttpServletRequest request, @RequestParam(value = "project_id", required = true) Integer projectId,
                                  @RequestParam(value = "issue_uuid", required = true) String issueUuid,
                                  @RequestParam(value = "content", required = true) String content) {
        Integer userId = SessionUtil.getUid(sessionInfo);
        try {
            ctrlTool.projPerm(request, "项目.工程检查.问题管理.查看");
        } catch (Exception e) {
            log.error("我的问题 -项目下问题详情追加描述 鉴权异常:", e.getMessage());
        }
        return iIssueService.updeteIssueDescByUuid(projectId, issueUuid, userId, content);
    }

    //工程检查- 我的问题 问题详情
    @RequestMapping(value = "issue/detail_log", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<DetailLogRspVo> detailLog(@RequestParam(value = "project_id", required = true) Integer projectId,
                                                    @RequestParam(value = "issue_uuid", required = true) String issueUuid) throws Exception {
        LjBaseResponse<DetailLogRspVo> response = new LjBaseResponse<>();
        ctrlTool.projPerm(RequestContextHolderUtil.getRequest(), "项目.工程检查.问题管理.查看");
        List<HouseQmCheckTaskIssueHistoryLogVo> result = iIssueService.getHouseQmCheckTaskIssueActionLogByIssueUuid(issueUuid);
        DetailLogRspVo data = new DetailLogRspVo();
        data.setItems(result);
        response.setData(data);

        return response;
    }

    //工程检查- 我的问题 项目下问题修复记录
    @RequestMapping(value = "issue/detail_repair_log", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<DetailRepairLogRspVo> detailRepairLog(HttpServletRequest request, @RequestParam(value = "project_id", required = true) Integer projectId,
                                                                @RequestParam(value = "issue_uuid", required = true) String issueUuid) {
        try {
            ctrlTool.projPerm(request, "项目.工程检查.问题管理.查看");
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        LjBaseResponse<List<HouseQmCheckTaskIssueDetailRepairLogVo>> result = iIssueService.getDetailRepairLogByIssueUuid(issueUuid);

        LjBaseResponse<DetailRepairLogRspVo> response = new LjBaseResponse<>();
        DetailRepairLogRspVo data = new DetailRepairLogRspVo();
        data.setItems(result.getData());
        response.setData(data);
        return response;
    }

    //【项目-过程检查-问题管理-问题详情】其他信息编辑
    @RequestMapping(value = "issue/edit_detail", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<Object> editDetail(HttpServletRequest request, @Validated EditDetailReq req) {
        LjBaseResponse<Object> response = new LjBaseResponse<>();
        Integer userId = SessionUtil.getUid(sessionInfo);
        try {
            ctrlTool.projPerm(request, "项目.工程检查.问题管理.编辑");
        } catch (Exception e) {
            log.error("【项目-过程检查-问题管理-问题详情】其他信息编辑 鉴权异常:", e.getMessage());
            response.setResult(1);
            response.setMessage("PermissionDenied");
            return response;
        }

        return iIssueService.updateIssueDetailByProjectAndUuid(userId, req.getProject_id(), req.getIssue_uuid(), req.getTyp(), req.getData());
    }

    //【项目-过程检查-问题管理-问题详情】删除问题
    @RequestMapping(value = "issue/delete", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse delete(HttpServletRequest request, @RequestParam(value = "project_id", required = true) Integer projectId,
                                 @RequestParam(value = "issue_uuid", required = true) String issueUuid) {
        LjBaseResponse response = new LjBaseResponse();
        try {
            ctrlTool.projPerm(request, "项目.工程检查.问题管理.删除");
            iIssueService.deleteHouseqmCheckTaskIssueByProjectAndUuid(projectId, issueUuid);
        } catch (Exception e) {
            log.error("【项目-过程检查-问题管理-问题详情】删除问题 鉴权异常:", e.getMessage());
            response.setResult(1);
            response.setMessage(e.getMessage());
        }
        return response;
    }

}
