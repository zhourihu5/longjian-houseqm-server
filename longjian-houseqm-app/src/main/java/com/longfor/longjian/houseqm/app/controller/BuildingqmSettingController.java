package com.longfor.longjian.houseqm.app.controller;

/**
 * @author lipeishuai
 * @date 2018/11/20 19:32
 */

import com.longfor.gaia.gfs.web.mock.MockOperation;
import com.longfor.longjian.common.base.LjBaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
     *
     * @return
     */
    @MockOperation
    @GetMapping(value = "get_issuefiled_setting", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<String> getIssuefiledSetting() {

        return null;
    }
}
