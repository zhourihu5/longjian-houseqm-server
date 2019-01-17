package com.longfor.longjian.houseqm.app.controller;

import com.google.common.collect.Lists;
import com.longfor.gaia.gfs.core.bean.PageInfo;
import com.longfor.gaia.gfs.web.mock.MockOperation;
import com.longfor.longjian.common.base.LjBaseResponse;
import com.longfor.longjian.common.consts.CommonGlobal;
import com.longfor.longjian.common.util.CtrlTool;
import com.longfor.longjian.houseqm.app.req.IssueListDoActionReq;
import com.longfor.longjian.houseqm.app.service.IIssueService;
import com.longfor.longjian.houseqm.app.vo.*;
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

    /**
     * 问题检索
     * http://192.168.37.159:3000/project/8/interface/api/286
     * @param req
     * @return
     */
    @GetMapping(value = "list", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public TaskResponse<PageInfo<IssueListVo>> doAction(HttpServletRequest request,@Valid IssueListDoActionReq req) {
        log.info("list, project_id=%d, category_cls=%d, task_id=%d, category_key=%s, check_item_key=%s, area_ids=%s, status_in=%s, checker_id=%d, repairer_id=%d," +
                        " type=%d, condition=%d, key_word=%s, create_on_begin=%s, create_on_end=%s, is_overdue=%d, page=%d, page_size=%d"
                       );
        ////todo session 取uid 权限 uid = session['uid']
        try {
            ctrlTool.projPerm(request,"项目.工程检查.问题管理.查看");
        } catch (Exception e) {
            e.printStackTrace();
        }
        /*
        has_per = ucenter_api.check_project_permission(uid, req.project_id, '项目.工程检查.问题管理.查看')
        if not has_per:
        rsp = errors_utils.err(rsp, 'PermissionDenied')*/

        PageInfo<IssueListVo> pageInfo = iIssueService.list(req.getProject_id(), req.getCategory_cls(),req.getTask_id(),req.getCategory_key(),req.getCheck_item_key(),
                                                            req.getArea_ids(),req.getStatus_in(),req.getChecker_id(),req.getRepairer_id(),req.getType(),req.getCondition(),req.getKey_word(),
                                                            req.getCreate_on_begin(),req.getCreate_on_end(),req.is_overdue(),req.getPage(),req.getPage_size());
        TaskResponse<PageInfo<IssueListVo>> taskResponse = new TaskResponse<>();
        taskResponse.setData(pageInfo);
        return taskResponse;
    }

    @GetMapping(value = "detail_log", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public TaskResponse< ArrayList<HouseQmCheckTaskIssueHistoryLogVo>> detailLog(@RequestParam(value = "project_id",required = true) Integer projectId,
                                                                     @RequestParam(value = "issue_uuid",required =true) String issueUuid){
        ////todo session 取uid 权限
        /*uid = session['uid']
        has_per = ucenter_api.check_project_permission(uid, req.project_id, '项目.工程检查.问题管理.查看')
        if not has_per:
        rsp = errors_utils.err(rsp, 'PermissionDenied')*/
        List<HouseQmCheckTaskIssueHistoryLogVo> result=  iIssueService.getHouseQmCheckTaskIssueActionLogByIssueUuid(issueUuid);
        TaskResponse<ArrayList<HouseQmCheckTaskIssueHistoryLogVo>> response = new TaskResponse<>();
        response.setData(result);
        return response;
    }

    @GetMapping(value = "repair_notify_export2", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse< String> repairNotifyExport2(HttpServletRequest request, HttpServletResponse response,@RequestParam(value = "project_id",required = true) Integer projectId,
                                                                                           @RequestParam(value = "issue_uuid",required =true) String issueUuid){
            if(request.getMethod()=="POST"){
                String project_id = request.getParameter("project_id");
                String issue_ids = request.getParameter("issue_ids");
            }
        log.info("repair_notify_export2, project_id="+projectId+", issue_ids="+issueUuid+"");

                //// todo 获取uid
               /* uid = session['uid']*/
        if(projectId==null||issueUuid==null){
            LjBaseResponse<String> objectTaskResponse = new LjBaseResponse<>();
            objectTaskResponse.setMessage("args error");
            objectTaskResponse.setResult((Integer) CommonGlobalEnum.RES_ERROR.getId());
            return objectTaskResponse;

        }
        Integer uid=1;
        RepairNotifyExportVo repairNotifyExportVo= iIssueService.repairNotifyExport2(uid,projectId,issueUuid);
        log.info("export repair notify, result="+repairNotifyExportVo.getResult()+", message="+repairNotifyExportVo.getMessage()+", path="+repairNotifyExportVo.getPath()+"");
        if(repairNotifyExportVo.getResult()!=0){
            LjBaseResponse<String> objectTaskResponse = new LjBaseResponse<>();
            objectTaskResponse.setMessage(repairNotifyExportVo.getMessage());
            objectTaskResponse.setResult(repairNotifyExportVo.getResult());
            return objectTaskResponse;
        }
        return null;
    }
    @GetMapping(value = "configs", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<ProjectSettingConfigVo> configs(@RequestParam(value = "project_id",required = true) Integer projectId){
        ////todo session 取uid 权限
        /*uid = session['uid']
        has_per = ucenter_api.check_project_permission(uid, req.project_id, '项目.工程检查.问题管理.查看')
        if not has_per:
        rsp = errors_utils.err(rsp, 'PermissionDenied')*/
        List<ProjectSettingConfigVo.HouseQmIssueReason> reason_list = Lists.newArrayList();
        Integer reasonId=0;

      List<ProjectSettingV2>projectSetting=  iIssueService.getProjectSettingId(projectId);
        ProjectSettingConfigVo vo = new ProjectSettingConfigVo();
        for (int i = 0; i < projectSetting.size(); i++) {
            if(projectSetting.get(i).getsKey().equals("PROJ_ISSUE_REASON_SWITCH")){
                vo.setHas_issue_reason(true);
            }
            if(projectSetting.get(i).getsKey().equals("PROJ_ISSUE_SUGGEST_SWITCH")){
                vo.setHas_issue_suggest(true);
            }
            if(projectSetting.get(i).getsKey().equals("PROJ_POTENTIAL_RISK_SWITCH")){
                vo.setHas_issue_potential_rist(true);
            }
            if(projectSetting.get(i).getsKey().equals("PROJ_PREVENTIVE_ACTION_SWITCH")){
                vo.setHas_issue_preventive_action(true);
            }
            if(projectSetting.get(i).getsKey().equals("PROJ_ISSUE_REASON_NAME")){
              reasonId=projectSetting.get(i).getId();
            }
            if(projectSetting.get(i).getsKey().equals("PROJ_ISSUE_REASON_LIST")){
                ProjectSettingConfigVo.HouseQmIssueReason  single_reason= new ProjectSettingConfigVo(). new HouseQmIssueReason();
                single_reason.setId(projectSetting.get(i).getId());
                single_reason.setValue(projectSetting.get(i).getValue());
                reason_list.add(single_reason);
            }
        }
        if(reasonId>0){
            for (int i = 0; i < projectSetting.size(); i++) {
                if(projectSetting.get(i).getParentId().equals(reasonId)){
                    ProjectSettingConfigVo.HouseQmIssueReason singleReason = new ProjectSettingConfigVo().new HouseQmIssueReason();
                    singleReason.setId(projectSetting.get(i).getId());
                    singleReason.setValue(projectSetting.get(i).getValue());
                    reason_list.add(singleReason);
                }
            }
        }
        vo.setReason_list(reason_list);
        LjBaseResponse<ProjectSettingConfigVo> response = new LjBaseResponse<>();
        response.setData(vo);
        return response;
    }




    @PostMapping(value = "delete", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public TaskResponse delete(@RequestParam(value = "project_id",required = true) Integer projectId,
                               @RequestParam(value = "issue_uuid",required =true) String issueUuid){
        ////todo session 取uid 权限
        /*uid = session['uid']
        has_per = ucenter_api.check_project_permission(uid, req.project_id, '项目.工程检查.问题管理.查看')
        if not has_per:
        rsp = errors_utils.err(rsp, 'PermissionDenied')*/
        iIssueService.deleteHouseqmCheckTaskIssueByProjectAndUuid(projectId,issueUuid);
        return new TaskResponse();
    }

    @PostMapping(value = "edit_repairer", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse editRepairer(@RequestParam(value = "project_id",required = true) Integer projectId,
                               @RequestParam(value = "issue_uuid",required =true) String issueUuid,
                                    @RequestParam(value = "repairer_id",required = false,defaultValue = "0") Integer repairerId,
                                     @RequestParam(value = "repair_follower_ids",required =false,defaultValue = "") String repairFollowerIds ){
        ////todo session 取uid 权限
        /*uid = session['uid']
        has_per = ucenter_api.check_project_permission(uid, req.project_id, '项目.工程检查.问题管理.查看')
        if not has_per:
        rsp = errors_utils.err(rsp, 'PermissionDenied')*/
        Integer uid=1;
        LjBaseResponse taskResponse = iIssueService.updateIssueRepairInfoByProjectAndUuid(uid,repairerId,repairFollowerIds,projectId,issueUuid);
        return taskResponse;
    }




    @PostMapping(value = "add_desc", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse addDesc(@RequestParam(value = "project_id",required = true) Integer projectId,
                               @RequestParam(value = "issue_uuid",required =true) String issueUuid,
                                @RequestParam(value = "content",required =true) String content){
        ////todo session 取uid 权限
        /*uid = session['uid']
        has_per = ucenter_api.check_project_permission(uid, req.project_id, '项目.工程检查.问题管理.查看')
        if not has_per:
        rsp = errors_utils.err(rsp, 'PermissionDenied')*/

        Integer uid=1;
        LjBaseResponse taskResponse=  iIssueService.updeteIssueDescByUuid(projectId,issueUuid,uid,content);
        return  taskResponse;
    }
    @PostMapping(value = "edit_plan_end_on", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse editPlanEndOn(@RequestParam(value = "project_id",required = true) Integer projectId,
                                  @RequestParam(value = "issue_uuid",required =true) String issueUuid,
                                  @RequestParam(value = "plan_end_on",required =false, defaultValue = "0") Integer plan_end_on){
        ////todo session 取uid 权限
        /*uid = session['uid']
        has_per = ucenter_api.check_project_permission(uid, req.project_id, '项目.工程检查.问题管理.查看')
        if not has_per:
        rsp = errors_utils.err(rsp, 'PermissionDenied')*/

        Integer uid=1;
        LjBaseResponse taskResponse=  iIssueService.updateIssuePlanEndOnByProjectAndUuid(projectId,issueUuid,uid,plan_end_on);
        return  taskResponse;
    }
    @PostMapping(value = "edit_approve", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse editApprove(@RequestParam(value = "project_id",required = true) Integer projectId,
                                        @RequestParam(value = "issue_uuid",required =true) String issueUuid,
                                      @RequestParam(value = "status",required =true) Integer status,
                                      @RequestParam(value = "content",required =true) String content){
        ////todo session 取uid 权限
        /*uid = session['uid']
        has_per = ucenter_api.check_project_permission(uid, req.project_id, '项目.工程检查.问题管理.查看')
        if not has_per:
        rsp = errors_utils.err(rsp, 'PermissionDenied')*/

        Integer uid=1;
        LjBaseResponse taskResponse=  iIssueService.updateIssueApproveStatusByUuid(projectId,issueUuid,uid,status,content);
        return  taskResponse;
    }

    @GetMapping(value = "detail_repair_log", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<List<HouseQmCheckTaskIssueDetailRepairLogVo>> detailRepairLog(@RequestParam(value = "project_id",required = true) Integer projectId,
                                                                                 @RequestParam(value = "issue_uuid",required =true) String issueUuid){
        ////todo session 取uid 权限
        /*uid = session['uid']
        has_per = ucenter_api.check_project_permission(uid, req.project_id, '项目.工程检查.问题管理.查看')
        if not has_per:
        rsp = errors_utils.err(rsp, 'PermissionDenied')*/
        LjBaseResponse<List<HouseQmCheckTaskIssueDetailRepairLogVo>>  result=  iIssueService.getDetailRepairLogByIssueUuid(issueUuid);

        return result;
    }


    @GetMapping(value = "detail_base", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<IssueInfoVo> detailBase(@RequestParam(value = "project_id",required = true) Integer projectId,
                                                                                        @RequestParam(value = "issue_uuid",required =true) String issueUuid){
        ////todo session 取uid 权限
        /*uid = session['uid']
        has_per = ucenter_api.check_project_permission(uid, req.project_id, '项目.工程检查.问题管理.查看')
        if not has_per:
        rsp = errors_utils.err(rsp, 'PermissionDenied')*/
        Integer  uid=1;
        LjBaseResponse<IssueInfoVo>  result=  iIssueService.getHouseQmCheckTaskIssueDetailBaseByProjectAndUuid(uid,projectId,issueUuid);

        return result;
    }

}
