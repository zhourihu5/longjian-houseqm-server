package com.longfor.longjian.houseqm.app.controller;

import com.longfor.gaia.gfs.web.mock.MockOperation;
import com.longfor.longjian.common.base.LjBaseResponse;
import com.longfor.longjian.houseqm.app.vo.AreaListVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * http://192.168.37.159:3000/project/8/interface/api/1580 项目统计分析问题分析获取项目区域统计数据
 * http://192.168.37.159:3000/project/8/interface/api/1604 项目统计分析区域对比获取区域信息
 * http://192.168.37.159:3000/project/8/interface/api/1628 项目统计分析分项详情获取区域信息列表
 *
 * @author lipeishuai
 * @date 2018/11/17 15:07
 */

@RestController
@RequestMapping("gapi/v3/res/")
@Slf4j
public class AreaController {


    /**
     *
     * @param projectId
     * @param groupId
     * @param teamId
     * @param tip
     * @return
     */
    @MockOperation
    @GetMapping(value = "area", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<AreaListVo> doAction(@RequestParam(value="project_id" ) Integer projectId,
                                           @RequestParam(value="group_id") String groupId,
                                           @RequestParam(value="team_id") String teamId,
                                           @RequestParam(value="tip") String tip){


        return null;
    }


}
