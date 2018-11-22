package com.longfor.longjian.houseqm.app.controller;

import com.longfor.gaia.gfs.web.mock.MockOperation;
import com.longfor.longjian.common.base.LjBaseResponse;
import com.longfor.longjian.houseqm.app.vo.StatListVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * http://192.168.37.159:3000/project/8/interface/api/1540 项目统计分析总览获取本周和上周统计数据
 * http://192.168.37.159:3000/project/8/interface/api/1548 项目下统计分析总览获取所选时段每天统计信息
 * http://192.168.37.159:3000/project/8/interface/api/1556 项目统计分析整改进度获取所有区域检查项数据和每个区域每个检查项的数据
 * http://192.168.37.159:3000/project/8/interface/api/1588 项目统计分析问题分析获取项目整体统计数据和每个检查项统计数据
 * http://192.168.37.159:3000/project/8/interface/api/1612 项目统计分析区域对比获取根据选定区域检查项的统计数据
 * http://192.168.37.159:3000/project/8/interface/api/1636 项目统计分析分项详情获取区域整体和所选区域时间检查项的数据
 * http://192.168.37.159:3000/project/8/interface/api/1652 项目统计分析人员对比获取合作伙伴人员列表
 * http://192.168.37.159:3000/project/8/interface/api/1660 项目统计分析人员对比获取选定条件的统计数据
 * http://192.168.37.159:3000/project/8/interface/api/1668 项目统计分析人员对比获取检查人列表
 * http://192.168.37.159:3000/project/8/interface/api/1676 项目统计分析人员对比获取人员工作量统计
 *
 * @author lipeishuai
 * @date 2018/11/17 15:07
 */

@RestController
@RequestMapping("gapi/v3/stat_houseqm/stat/")
@Slf4j
public class StatProjectController {


    /**
     *
     * @param projectId
     * @param groupId
     * @param teamId
     * @param tip
     * @return
     */
    @MockOperation
    @GetMapping(value = "project", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<StatListVo> doAction(@RequestParam(value="project_id" ) Integer projectId,
                                               @RequestParam(value="group_id") String groupId,
                                               @RequestParam(value="team_id") String teamId,
                                               @RequestParam(value="tip") String tip){


        return null;
    }
}
