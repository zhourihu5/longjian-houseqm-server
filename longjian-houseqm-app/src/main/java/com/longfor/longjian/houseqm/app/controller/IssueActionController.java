package com.longfor.longjian.houseqm.app.controller;

import com.longfor.gaia.gfs.web.mock.MockOperation;
import com.longfor.longjian.houseqm.app.vo.IssueActionVo;
import com.longfor.longjian.houseqm.app.vo.TaskResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * http://192.168.37.159:3000/project/8/interface/api/1092  集团问题动作统计
 * http://192.168.37.159:3000/project/8/interface/api/1116 集团功能统计横向对比获取公司数据
 * http://192.168.37.159:3000/project/8/interface/api/1148 集团下功能统计趋势对比获取数据
 * http://192.168.37.159:3000/project/8/interface/api/1492 公司功能使用统计总览获取公司和公司下每个项目（查询时间）的统计数据
 * http://192.168.37.159:3000/project/8/interface/api/1508 公司功能使用统计横向对比分析查询条件获取公司及所选项目数据
 * http://192.168.37.159:3000/project/8/interface/api/1516 公司功能使用统计趋势对比根据查询条件获取数据
 * http://192.168.37.159:3000/project/8/interface/api/1684 项目功能使用统计总览获取集团公司项目统计信息
 * http://192.168.37.159:3000/project/8/interface/api/1692 项目功能使用统计趋势对比获取选定时间公司和项目的人员工作量统计
 *
 * @author lipeishuai
 * @date 2018/11/17 15:07
 */

@RestController
@RequestMapping("/gapi/v3/stat_houseqm/")
@Slf4j
public class IssueActionController {

    /**
     * @param groupId
     * @param tip
     * @return
     */
    @MockOperation
    @GetMapping(value = "issue_action", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public TaskResponse<IssueActionVo> doAction(@RequestParam(value = "group_id") String groupId,
                                                @RequestParam(value = "tip") String tip) {


        return null;
    }


}
