package com.longfor.longjian.houseqm.app.controller;

import com.longfor.gaia.gfs.web.mock.MockOperation;
import com.longfor.longjian.common.base.LjBaseResponse;
import com.longfor.longjian.houseqm.app.vo.SearchListVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * http://192.168.37.159:3000/project/8/interface/api/1172 集团下功能统计项目系统设置获取公司下所有项目及统计状态
 *
 * @author lipeishuai
 * @date 2018/11/17 15:07
 */

@RestController
@RequestMapping("/papi/v3/stat_houseqm/in_stat_proj_status/")
@Slf4j
public class InStatProjStatusController {


    /**
     * @param teamId
     * @return
     */
    @MockOperation
    @GetMapping(value = "search", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<SearchListVo> doAction(@RequestParam(value = "team_id") String teamId) {

        return null;
    }
}
