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
 * http://192.168.37.159:3000/project/8/interface/api/932  获取集团本月和上月指标
 * http://192.168.37.159:3000/project/8/interface/api/972  获取集团下近七天每天的数据
 * http://192.168.37.159:3000/project/8/interface/api/1004 获取集团下搜索时段区域的趋势对比信息
 * http://192.168.37.159:3000/project/8/interface/api/996  获取集团下项目排名
 * http://192.168.37.159:3000/project/8/interface/api/1012 获取检查项的统计数据
 *
 * @author lipeishuai
 * @date 2018/11/17 15:07
 */

@RestController
@RequestMapping("gapi/v3/stat_houseqm/stat/")
@Slf4j
public class StatGroupController {


    /**
     *
     * @param pageLevel
     * @param groupId
     * @param tip
     * @return
     */
    @MockOperation
    @GetMapping(value = "group", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<StatListVo> doAction(@RequestParam(value="page_level") String pageLevel,
                                               @RequestParam(value="group_id") String groupId,
                                               @RequestParam(value="tip") String tip){


        return null;
    }
}
