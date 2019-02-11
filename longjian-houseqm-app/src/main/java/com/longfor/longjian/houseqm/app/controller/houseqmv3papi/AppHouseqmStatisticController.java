package com.longfor.longjian.houseqm.app.controller.houseqmv3papi;

import com.longfor.longjian.common.base.LjBaseResponse;
import com.longfor.longjian.common.util.SessionInfo;
import com.longfor.longjian.houseqm.app.req.houseqmstatisticapp.ProjectCheckerStatReq;
import com.longfor.longjian.houseqm.app.req.houseqmstatisticapp.ProjectIssueStatReq;
import com.longfor.longjian.houseqm.app.req.houseqmstatisticapp.ProjectListReq;
import com.longfor.longjian.houseqm.app.req.houseqmstatisticapp.ProjectRepairerStatReq;
import com.longfor.longjian.houseqm.app.service.IHouseqmStatisticService;
import com.longfor.longjian.houseqm.app.vo.houseqmstatisticapp.ProjectCheckerStatRspVo;
import com.longfor.longjian.houseqm.app.vo.houseqmstatisticapp.ProjectIssueStatRspVo;
import com.longfor.longjian.houseqm.app.vo.houseqmstatisticapp.ProjectListRspVo;
import com.longfor.longjian.houseqm.app.vo.houseqmstatisticapp.ProjectRepairerStatRspVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.app.controller.houseqmstatisticapp
 * @ClassName: AppHouseqmStatisticController
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/1/21 17:48
 */
@RestController
@RequestMapping("/houseqm/v3/papi/houseqm_statistic/")
@Slf4j
public class AppHouseqmStatisticController {

    @Resource
    private IHouseqmStatisticService iHouseqmStatisticService;
    @Resource
    private SessionInfo sessionInfo;

    /**
     * @return com.longfor.longjian.common.base.LjBaseResponse
     * @Author hy
     * @Description 获取项目列表
     * http://192.168.37.159:3000/project/8/interface/api/3264
     * @Date 17:53 2019/1/21
     * @Param [request, req]
     **/
    @RequestMapping(value = "project_list", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<ProjectListRspVo> projectList(HttpServletRequest request, @Valid ProjectListReq req) {
        LjBaseResponse<ProjectListRspVo> response = new LjBaseResponse<>();
        log.info("project_list, source=" + req.getSource() + ", timestamp=" + req.getTimestamp());
        Integer userId = (Integer) sessionInfo.getBaseInfo("userId");
        try {
            ProjectListRspVo items = iHouseqmStatisticService.projectList(userId, req.getSource(), req.getTimestamp());
            response.setData(items);
        } catch (Exception e) {
            e.printStackTrace();
            response.setResult(1);
            response.setMessage(e.getMessage());
        }
        return response;
    }


    /**
     * @return com.longfor.longjian.common.base.LjBaseResponse<com.longfor.longjian.houseqm.app.vo.houseqmstatisticapp.ProjectIssueStatRspVo>
     * @Author hy
     * @Description 项目汇总状态
     * http://192.168.37.159:3000/project/8/interface/api/3280
     * @Date 20:28 2019/1/21
     * @Param [request, req]
     **/
    @RequestMapping(value = "project_issue_stat", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<ProjectIssueStatRspVo> projectIssueStat(HttpServletRequest request, @Valid ProjectIssueStatReq req) {
        LjBaseResponse<ProjectIssueStatRspVo> response = new LjBaseResponse<>();
        log.info("project_issue_stat, project_id=" + req.getProject_id() + ", source=" + req.getSource() + ", area_id=" + req.getArea_id() + ", timestamp=" + req.getTimestamp());
        Integer userId = (Integer) sessionInfo.getBaseInfo("userId");
        try {
            ProjectIssueStatRspVo item = iHouseqmStatisticService.projectIssueStat(userId, req.getProject_id(), req.getSource(), req.getArea_id(), req.getTimestamp());
            response.setData(item);
        } catch (Exception e) {
            e.printStackTrace();
            response.setResult(1);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    /**
     * @return com.longfor.longjian.common.base.LjBaseResponse<com.longfor.longjian.houseqm.app.vo.houseqmstatisticapp.ProjectCheckerStatRspVo>
     * @Author hy
     * @Description 项目/任务检查人员统计
     * http://192.168.37.159:3000/project/8/interface/api/3268
     * @Date 10:27 2019/1/22
     * @Param [request, req]
     **/
    @RequestMapping(value = "project_checker_stat", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<ProjectCheckerStatRspVo> projectCheckerStat(HttpServletRequest request, @Valid ProjectCheckerStatReq req) {
        LjBaseResponse<ProjectCheckerStatRspVo> response = new LjBaseResponse<>();
        log.info("project_checker_stat, project_id=" + req.getProject_id() + ", task_id=" + req.getTask_id() + ", source=" + req.getSource() + ", stat_begin=" + req.getStat_begin() + ", stat_end=" + req.getStat_end() + ", timestamp=" + req.getTimestamp());
        Integer userId = (Integer) sessionInfo.getBaseInfo("userId");
        try {
            ProjectCheckerStatRspVo item = iHouseqmStatisticService.projectCheckerStat(userId, req.getProject_id(), req.getTask_id(), req.getSource(), req.getStat_begin(), req.getStat_end(), req.getTimestamp());
            response.setData(item);
        } catch (Exception e) {
            e.printStackTrace();
            response.setResult(1);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    /**
     * @return com.longfor.longjian.common.base.LjBaseResponse<com.longfor.longjian.houseqm.app.vo.houseqmstatisticapp.ProjectRepairerStatRspVo>
     * @Author hy
     * @Description 项目/任务整改人员统计
     * http://192.168.37.159:3000/project/8/interface/api/3272
     * @Date 12:16 2019/1/22
     * @Param [request, req]
     **/
    @RequestMapping(value = "project_repairer_stat", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<ProjectRepairerStatRspVo> projectRepairerStat(HttpServletRequest request, @Valid ProjectRepairerStatReq req) {
        LjBaseResponse<ProjectRepairerStatRspVo> response = new LjBaseResponse<>();
        log.info("project_repairer_stat, project_id=" + req.getProject_id() + ", task_id=" + req.getTask_id() + ", source=" + req.getSource() + ", stat_begin=" + req.getStat_begin() + ", stat_end=" + req.getStat_end() + ", timestamp=" + req.getTimestamp());
        Integer userId = (Integer) sessionInfo.getBaseInfo("userId");
        try {
            ProjectRepairerStatRspVo items = iHouseqmStatisticService.projectRepairerStat(userId, req.getProject_id(), req.getTask_id(), req.getSource(), req.getStat_begin(), req.getStat_end(), req.getTimestamp());
            response.setData(items);
        } catch (Exception e) {
            e.printStackTrace();
            response.setResult(1);
            response.setMessage(e.getMessage());
        }
        return response;
    }


}
