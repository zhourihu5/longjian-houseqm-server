package com.longfor.longjian.houseqm.app.controller;

import com.longfor.gaia.gfs.web.mock.MockOperation;
import com.longfor.longjian.common.base.LjBaseResponse;
import com.longfor.longjian.houseqm.app.vo.OrgListVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * http://192.168.37.159:3000/project/8/interface/api/988  获取集团下所有项目
 * http://192.168.37.159:3000/project/8/interface/api/1252 公司统计分析组织对比项目对比/获取公司下所有项目列表
 * http://192.168.37.159:3000/project/8/interface/api/1500 公司功能使用统计横向对比获取所有公司及其下所有项目列表信息
 *
 * @author lipeishuai
 * @date 2018/11/17 15:07
 */

@RestController
@RequestMapping("gapi/v3/res/")
@Slf4j
public class OrgController {

    /**
     *
     * @param pageLevel
     * @param groupId
     * @param tip
     * @return
     */
    @MockOperation
    @GetMapping(value = "org", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<OrgListVo> doAction(@RequestParam(value="page_level") String pageLevel,
                                              @RequestParam(value="group_id") String groupId,
                                              @RequestParam(value="tip") String tip){


        return null;
    }
}
