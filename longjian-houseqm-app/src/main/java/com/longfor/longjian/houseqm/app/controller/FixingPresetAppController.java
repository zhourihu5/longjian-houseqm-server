package com.longfor.longjian.houseqm.app.controller;

import com.google.common.collect.Lists;
import com.longfor.gaia.gfs.web.mock.MockOperation;
import com.longfor.longjian.common.base.LjBaseResponse;
import com.longfor.longjian.houseqm.app.service.FixingPresetAppService;
import com.longfor.longjian.houseqm.app.vo.AppFixingPresetProtoVo;
import com.longfor.longjian.houseqm.app.vo.AppListFixingPresetVo;
import com.longfor.longjian.houseqm.po.FixingPreset;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

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
    @Resource
    FixingPresetAppService fixingPresetService;

    /**
     * App端查看整改预设
     * <p>
     * http://192.168.37.159:3000/mock/8/core_srv_check_item/check_item/fixing_preset_app/app_list_fixing_preset1/?device_id=865297034852711&last_id=0&project_id=929&timestamp=0&token=RTc0OFFQRDBIM044TEI0QTFMVlpDQlFKRFRLRTdBWlA
     *
     * @return
     */

    @GetMapping(value = "check_item/fixing_preset_app/app_list_fixing_preset", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<AppListFixingPresetVo> appListFixingPreset(
            @RequestParam(value = "project_id", required = true, defaultValue = "0") Integer projectId,
            @RequestParam(value = "last_id", required = true, defaultValue = "0") Integer lastId,
            @RequestParam(value = "timestamp", required = true) Integer timestamp,
            @RequestParam(value = "limit", required = false, defaultValue = "2000") Integer limit
    ) {
        List<FixingPreset> fixingPresetData = fixingPresetService.appGetFixingPreset(projectId, lastId, timestamp, limit);

        if (fixingPresetData != null && fixingPresetData.size() > 0) {
            lastId = fixingPresetData.get(fixingPresetData.size() - 1).getId();
        } else {
            lastId = -1;
        }

        List<AppFixingPresetProtoVo> items = Lists.newArrayList();
        LjBaseResponse<AppListFixingPresetVo> response = new LjBaseResponse<>();
        for (int i = 0; i < fixingPresetData.size(); i++) {
            AppFixingPresetProtoVo vo = new AppFixingPresetProtoVo();
            vo.setId(String.valueOf(fixingPresetData.get(i).getId()));
            vo.setProject_id(fixingPresetData.get(i).getProjectId());
            vo.setArea_id(fixingPresetData.get(i).getAreaId());
            vo.setRoot_category_key(fixingPresetData.get(i).getRootCategoryKey());
            vo.setCategory_key(fixingPresetData.get(i).getCategoryKey());
            vo.setCheck_item_key(fixingPresetData.get(i).getCheckItemKey());
            vo.setUser_ids(fixingPresetData.get(i).getUserIds());
            vo.setMinutes(fixingPresetData.get(i).getMinutes());
            if (fixingPresetData.get(i).getMinutes() > 0) {
                vo.setDays(fixingPresetData.get(i).getMinutes() / 1440);
            } else {
                vo.setDays(fixingPresetData.get(i).getMinutes());
            }
            items.add(vo);
        }
        AppListFixingPresetVo presetVo = new AppListFixingPresetVo();
        presetVo.setItems(items);
        presetVo.setLast_id(lastId);
        response.setData(presetVo);

        return response;
    }


}
