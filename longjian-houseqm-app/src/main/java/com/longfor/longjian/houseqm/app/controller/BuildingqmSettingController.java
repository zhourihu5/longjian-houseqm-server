package com.longfor.longjian.houseqm.app.controller;

/**
 * @author lipeishuai
 * @date 2018/11/20 19:32
 */

import com.longfor.gaia.gfs.web.mock.MockOperation;
import com.longfor.longjian.common.base.LjBaseResponse;
import com.longfor.longjian.houseqm.app.service.BuildingqmSettingService;
import com.longfor.longjian.houseqm.app.vo.ApiIssueFiledSettingMsg;
import com.longfor.longjian.houseqm.po.IssueFieldSetting;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * http://192.168.37.159:3000/project/8/interface/api/752  获取(工程检查)问题字段配置列表
 *
 * @author lipeishuai
 * @date 2018/11/20 18:56
 */
@RestController
@RequestMapping("setting/v3/papi/buildingqm/")
@Slf4j
public class BuildingqmSettingController {



    @Resource
    private BuildingqmSettingService buildingqmSettingService;

    /**
     *
     * @param projectIds
     * @param timestamp
     * @return
     */
    @GetMapping(value = "get_issuefiled_setting", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<ApiIssueFiledSettingMsg.IssueFileds> getIssuefiledSetting(@RequestParam(value = "project_ids")   String  projectIds, @RequestParam(value = "timestamp" ,required = false,defaultValue = "0") Integer timestamp) {

        LjBaseResponse<ApiIssueFiledSettingMsg.IssueFileds> response = buildingqmSettingService.getIssuefiledSetting(projectIds, timestamp);

        return response;
    }
}