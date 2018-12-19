package com.longfor.longjian.houseqm.app.controller;

/**
 * @author lipeishuai
 * @date 2018/11/20 19:32
 */

import com.longfor.gaia.gfs.web.mock.MockOperation;
import com.longfor.longjian.common.base.LjBaseResponse;
import com.longfor.longjian.houseqm.app.service.BuildingqmSettingService;
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
 *
 * http://192.168.37.159:3000/project/8/interface/api/752  获取(工程检查)问题字段配置列表
 *
 * @author lipeishuai
 * @date 2018/11/20 18:56
 */
@RestController
@RequestMapping("setting/v3/papi/buildingqm/")
@Slf4j
public class BuildingqmSettingController {


    /**
     * @return
     */

    @Resource
    private BuildingqmSettingService buildingqmSettingService;


    @GetMapping(value = "get_issuefiled_setting", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<IssueFieldSetting> getIssuefiledSetting(@RequestParam List<String> projectIds, @RequestParam(required = false) Integer timestamp) {

        LjBaseResponse<IssueFieldSetting> esponse= null;
        try {
            esponse = buildingqmSettingService.get_issuefiled_setting(projectIds, timestamp);
        } catch (ParseException e) {
            e.printStackTrace();
        }
return esponse;
    }
}