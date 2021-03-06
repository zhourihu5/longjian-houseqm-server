package com.longfor.longjian.houseqm.app.controller.houseqmv3papi;

import com.longfor.longjian.common.base.LjBaseResponse;
import com.longfor.longjian.common.util.SessionInfo;
import com.longfor.longjian.houseqm.app.req.houseqmstatisticapp.ProjectCheckerStatReq;
import com.longfor.longjian.houseqm.app.req.houseqmstatisticapp.ProjectIssueStatReq;
import com.longfor.longjian.houseqm.app.req.houseqmstatisticapp.ProjectListReq;
import com.longfor.longjian.houseqm.app.service.IHouseqmStatisticService;
import com.longfor.longjian.houseqm.app.utils.SessionUtil;
import com.longfor.longjian.houseqm.app.vo.houseqmstatisticapp.ProjectCheckerStatRspVo;
import com.longfor.longjian.houseqm.app.vo.houseqmstatisticapp.ProjectIssueStatRspVo;
import com.longfor.longjian.houseqm.app.vo.houseqmstatisticapp.ProjectListRspVo;
import com.longfor.longjian.houseqm.app.vo.houseqmstatisticapp.ProjectRepairerStatRspVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;


@RestController
@RequestMapping("/houseqm/v3/papi/houseqm_statistic/")
@Slf4j
public class AppHouseqmStatisticController {

    @Resource
    private IHouseqmStatisticService iHouseqmStatisticService;
    @Resource
    private SessionInfo sessionInfo;

    @RequestMapping(value = "project_list", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<ProjectListRspVo> projectList(HttpServletRequest request, @Valid ProjectListReq req) {
        LjBaseResponse<ProjectListRspVo> response = new LjBaseResponse<>();
        log.info(String.format("project_list, source=%s, timestamp=%d",req.getSource(), req.getTimestamp())) ;
        Integer userId = SessionUtil.getUid(sessionInfo);
        try {
            ProjectListRspVo items = iHouseqmStatisticService.projectList(userId, req.getSource(), req.getTimestamp());
            response.setData(items);
        } catch (Exception e) {
            log.error("??????????????????error:", e.getMessage());
            response.setResult(1);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    @RequestMapping(value = "project_issue_stat", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<ProjectIssueStatRspVo> projectIssueStat(HttpServletRequest request, @Valid ProjectIssueStatReq req) {
        LjBaseResponse<ProjectIssueStatRspVo> response = new LjBaseResponse<>();
        log.info(String.format("project_issue_stat, project_id=%d, source=%s, area_id=%d, timestamp=%d", req.getProject_id(),req.getSource(),req.getArea_id(), req.getTimestamp()));
        Integer userId = SessionUtil.getUid(sessionInfo);
        try {
            if (req.getArea_id() == null) req.setArea_id(0);
            if (req.getTimestamp() == null) req.setTimestamp(0);
            ProjectIssueStatRspVo item = iHouseqmStatisticService.projectIssueStat(userId, req.getProject_id(), req.getSource(), req.getArea_id(), req.getTimestamp());
            response.setData(item);
        } catch (Exception e) {
            log.error("??????????????????error:", e.getMessage());
            response.setResult(1);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    @RequestMapping(value = "project_checker_stat", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<ProjectCheckerStatRspVo> projectCheckerStat(HttpServletRequest request, @Valid ProjectCheckerStatReq req) {
        LjBaseResponse<ProjectCheckerStatRspVo> response = new LjBaseResponse<>();
        log.info("project_checker_stat, project_id=" + req.getProject_id() + ", task_id=" + req.getTask_id() + ", source=" + req.getSource() + ", stat_begin=" + req.getStat_begin() + ", stat_end=" + req.getStat_end() + ", timestamp=" + req.getTimestamp());
        Integer userId = SessionUtil.getUid(sessionInfo);
        try {
            requestParamInitProjChecker(req);
            ProjectCheckerStatRspVo item = iHouseqmStatisticService.projectCheckerStat(userId, req.getProject_id(), req.getTask_id(), req.getSource(), req.getStat_begin(), req.getStat_end(), req.getTimestamp());
            response.setData(item);
        } catch (Exception e) {
            log.error("??????/????????????????????????error:", e.getMessage());
            response.setResult(1);
            response.setMessage(e.getMessage());
        }
        return response;
    }


    @RequestMapping(value = "project_repairer_stat", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<ProjectRepairerStatRspVo> projectRepairerStat(HttpServletRequest request, @Valid ProjectCheckerStatReq req) {
        LjBaseResponse<ProjectRepairerStatRspVo> response = new LjBaseResponse<>();
        log.info("project_repairer_stat, project_id=" + req.getProject_id() + ", task_id=" + req.getTask_id() + ", source=" + req.getSource() + ", stat_begin=" + req.getStat_begin() + ", stat_end=" + req.getStat_end() + ", timestamp=" + req.getTimestamp());
        Integer userId = SessionUtil.getUid(sessionInfo);
        try {
            requestParamInitProjChecker(req);
            ProjectRepairerStatRspVo items = iHouseqmStatisticService.projectRepairerStat(userId, req.getProject_id(), req.getTask_id(), req.getSource(), req.getStat_begin(), req.getStat_end(), req.getTimestamp());
            response.setData(items);
        } catch (Exception e) {
            log.error("??????/????????????????????????error:", e.getMessage());
            response.setResult(1);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    private void requestParamInitProjChecker(@Valid ProjectCheckerStatReq req) {
        if (req.getTask_id() == null) req.setTask_id(0);
        if (req.getStat_begin() == null) req.setStat_begin(0);
        if (req.getStat_end() == null) req.setStat_end(0);
        if (req.getTimestamp() == null) req.setTimestamp(0);
    }

}
