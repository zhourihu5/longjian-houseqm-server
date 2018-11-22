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
 * http://192.168.37.159:3000/project/8/interface/api/1220 获取公司本月和上月度统计数据
 * http://192.168.37.159:3000/project/8/interface/api/1236 获取公司下近7天（时间范围选项）统计数据
 * http://192.168.37.159:3000/project/8/interface/api/1268 获取公司统计分析组织对比项目对比排名信息
 * http://192.168.37.159:3000/project/8/interface/api/1276 公司统计分析组织对比项目对比横向对比查询数据
 * http://192.168.37.159:3000/project/8/interface/api/1284 公司统计分析组织对比总包对比获取总包公司列表
 * http://192.168.37.159:3000/project/8/interface/api/1300 公司统计分析组织对比总包对比获取总包公司排名
 * http://192.168.37.159:3000/project/8/interface/api/1308 公司统计分析组织对比总包对比获取条件查询数据
 * http://192.168.37.159:3000/project/8/interface/api/1484 公司统计分析分项详情获取查询检查项的数据
 *
 * @author lipeishuai
 * @date 2018/11/17 15:07
 */

@RestController
@RequestMapping("gapi/v3/stat_houseqm/stat/")
@Slf4j
public class StatTeamController {


    /**
     *
     * @param projectId
     * @param groupId
     * @param teamId
     * @param tip
     * @return
     */
    @MockOperation
    @GetMapping(value = "team", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<StatListVo> doAction(@RequestParam(value="project_id" ) Integer projectId,
                                               @RequestParam(value="group_id") String groupId,
                                               @RequestParam(value="team_id") String teamId,
                                               @RequestParam(value="tip") String tip){


        return null;
    }
}
