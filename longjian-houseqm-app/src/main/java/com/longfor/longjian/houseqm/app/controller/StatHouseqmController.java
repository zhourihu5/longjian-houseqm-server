package com.longfor.longjian.houseqm.app.controller;

import com.longfor.gaia.gfs.web.mock.MockOperation;
import com.longfor.longjian.common.base.LjBaseResponse;
import com.longfor.longjian.houseqm.app.vo.CheckerStatListVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * http://192.168.37.159:3000/project/8/interface/api/298  项目/任务检查人员统计
 *
 * @author lipeishuai
 * @date 2018/11/17 15:07
 */

@RestController
@RequestMapping("oapi/v3/houseqm/stat_houseqm/")
@Slf4j
public class StatHouseqmController {


    /**
     *
     * @param projectId
     * @param categoryCls
     * @param pageLevel
     * @param groupId
     * @param teamId
     * @param taskIds
     * @return
     */
    @MockOperation
    @GetMapping(value = "checker_stat", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<CheckerStatListVo> checkerStat(@RequestParam(value="project_id" ) Integer projectId,
                                                         @RequestParam(value="category_cls") String categoryCls,
                                                         @RequestParam(value="page_level") String pageLevel,
                                                         @RequestParam(value="group_id") String groupId,
                                                         @RequestParam(value="team_id") String teamId,
                                                         @RequestParam(value="task_id") String taskIds){


        return null;
    }
}
