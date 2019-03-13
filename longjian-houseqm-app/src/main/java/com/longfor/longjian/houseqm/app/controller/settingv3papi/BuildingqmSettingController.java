package com.longfor.longjian.houseqm.app.controller.settingv3papi;

/**
 * @author lipeishuai
 * @date 2018/11/20 19:32
 */

import com.longfor.longjian.common.base.LjBaseResponse;
import com.longfor.longjian.common.util.SessionInfo;
import com.longfor.longjian.houseqm.app.service.BuildingqmSettingService;
import com.longfor.longjian.houseqm.app.vo.ApiIssueFiledSettingMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

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
    @Resource
    SessionInfo sessionInfo;
    private static final String USER_ID="userId";

    /**
     * @param projectIds
     * @param timestamp
     * @return
     */
    @RequestMapping(value = "get_issuefiled_setting", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<ApiIssueFiledSettingMsg.IssueFileds> getIssuefiledSetting(@RequestParam(value = "project_ids") String projectIds, @RequestParam(value = "timestamp", required = false, defaultValue = "0") Integer timestamp) {

        return buildingqmSettingService.getIssuefiledSetting(projectIds, timestamp);
    }
    /*
    * 设置(工程检查)问题字段配置列表
    * */
    @RequestMapping(value = "save_issuefiled_setting", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse saveIssuefiledSetting(@RequestParam(value = "project_id") Integer projectId,
                                                @RequestParam(value = "field_id") Integer fieldId,
                                                @RequestParam(value = "display_status") Integer displayStatus,
                                                @RequestParam(value = "alias", required = false, defaultValue = "") Integer alias) {
        log.info(String.format("save_issuefiled_setting, project_id=%d, field_id=%d, display_status=%d, alias=%s" ,projectId, fieldId, displayStatus, alias));
        Integer userId = (Integer) sessionInfo.getBaseInfo(USER_ID);
        return   buildingqmSettingService.saveIssuefiledSetting(userId,projectId,fieldId,displayStatus,alias);
    }
}