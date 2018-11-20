package com.longfor.longjian.houseqm.app.controller;

import com.longfor.gaia.gfs.web.mock.MockOperation;
import com.longfor.longjian.common.base.LjBaseResponse;
import com.longfor.longjian.houseqm.app.req.ProjectReq;
import com.longfor.longjian.houseqm.app.vo.ProjectDailyListVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * http://192.168.37.159:3000/project/8/interface/api/368
 * http://192.168.37.159:3000/project/8/interface/api/374
 * http://192.168.37.159:3000/project/8/interface/api/380
 * http://192.168.37.159:3000/project/8/interface/api/384
 * http://192.168.37.159:3000/project/8/interface/api/388
 * http://192.168.37.159:3000/project/8/interface/api/392
 * http://192.168.37.159:3000/project/8/interface/api/394
 * http://192.168.37.159:3000/project/8/interface/api/396
 * http://192.168.37.159:3000/project/8/interface/api/400
 * http://192.168.37.159:3000/project/8/interface/api/402
 *
 * @author lipeishuai
 * @date 2018/11/20 18:15
 */

@RestController
@RequestMapping("v3/api/houseqm_statistic/")
@Slf4j
public class HouseqmStatisticController {

    /**
     *
     * @param projectReq
     * @return
     */
    @MockOperation
    @GetMapping(value = "task_stat", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<ProjectDailyListVo> taskStat(ProjectReq projectReq){
        return null;
    }

    /**
     *
     * @param projectReq
     * @return
     */
    @MockOperation
    @GetMapping(value = "get_daterange_options", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<ProjectDailyListVo> getDaterangeOptions(ProjectReq projectReq){
        return null;
    }

    /**
     *
     * @param projectReq
     * @return
     */
    @MockOperation
    @GetMapping(value = "task_list", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<ProjectDailyListVo> taskList(ProjectReq projectReq){
        return null;
    }

    /**
     *
     * @param projectReq
     * @return
     */
    @MockOperation
    @GetMapping(value = "project_issue_repair", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<ProjectDailyListVo> projectIssueRepair(ProjectReq projectReq){
        return null;
    }

    /**
     *
     * @param projectReq
     * @return
     */
    @MockOperation
    @GetMapping(value = "task_checkitem_stat", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<ProjectDailyListVo> taskCheckitemStat(ProjectReq projectReq){
        return null;
    }

    /**
     *
     * @param projectReq
     * @return
     */
    @MockOperation
    @GetMapping(value = "task_issue_repair", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<ProjectDailyListVo> taskIssueRepair(ProjectReq projectReq){
        return null;
    }

    /**
     *
     * @param projectReq
     * @return
     */
    @MockOperation
    @GetMapping(value = "task_issue_repair_list", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<ProjectDailyListVo> taskIssueRepairList(ProjectReq projectReq){
        return null;
    }

    /**
     *
     * @param projectReq
     * @return
     */
    @MockOperation
    @GetMapping(value = "category_issue_list", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<ProjectDailyListVo> categoryIssueList(ProjectReq projectReq){
        return null;
    }

    /**
     *
     * @param projectReq
     * @return
     */
    @MockOperation
    @GetMapping(value = "project_building_list", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<ProjectDailyListVo> projectBuildingList(ProjectReq projectReq){
        return null;
    }

    /**
     *
     * @param projectReq
     * @return
     */
    @MockOperation
    @GetMapping(value = "task_building_list", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<ProjectDailyListVo> taskBuildingList(ProjectReq projectReq){
        return null;
    }

}
