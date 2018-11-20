package com.longfor.longjian.houseqm.app.controller;

import com.longfor.gaia.gfs.web.mock.MockOperation;
import com.longfor.longjian.common.base.LjBaseResponse;
import com.longfor.longjian.houseqm.app.vo.AppListFixingPresetVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * http://192.168.37.159:3000/project/8/interface/api/738  app端查看整改预设
 *
 * @author lipeishuai
 * @date 2018-11-11 18:34
 */
@RestController
@RequestMapping("core_srv_check_item/")
@Slf4j
public class FixingPresetAppController {

    /**
     * App端查看整改预设
     * <p>
     * http://192.168.37.159:3000/mock/8/core_srv_check_item/check_item/fixing_preset_app/app_list_fixing_preset1/?device_id=865297034852711&last_id=0&project_id=929&timestamp=0&token=RTc0OFFQRDBIM044TEI0QTFMVlpDQlFKRFRLRTdBWlA
     *
     * @return
     */
    @MockOperation
    @GetMapping(value = "check_item/fixing_preset_app/app_list_fixing_preset", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<AppListFixingPresetVo> appListFixingPreset(
            @RequestParam(value = "project_id", required = false, defaultValue = "1") Integer projectId,
            @RequestParam(value = "last_id", required = false, defaultValue = "1") Integer lastId,
            @RequestParam(value = "timestamp", required = true) Integer timestamp) {

        return null;
    }


}
