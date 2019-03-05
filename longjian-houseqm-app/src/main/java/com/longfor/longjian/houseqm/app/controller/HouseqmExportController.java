package com.longfor.longjian.houseqm.app.controller;

import com.longfor.longjian.common.base.LjBaseResponse;
import com.longfor.longjian.houseqm.app.req.ProjectOrdersReq;
import com.longfor.longjian.houseqm.app.service.IHouseqmExportService;
import com.longfor.longjian.houseqm.app.vo.houseqm.HouseqmExportVo;
import com.longfor.longjian.houseqm.util.DateUtil;
import com.longfor.longjian.houseqm.util.StringSplitToListUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 * @deprecated  类废弃
 */
@RestController
@RequestMapping("oapi/v3/houseqm/export/")
@Slf4j
@Deprecated
public class HouseqmExportController {


    @Resource
    private IHouseqmExportService iHouseqmExportService;

    /**
     * @deprecated  接口废弃
     */
    @Deprecated
    @GetMapping(value = "project_orders", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse projectOrders(ProjectOrdersReq req, HttpServletResponse resp) {

        Date beginOn = null;
        Date endOn = null;
        if (!req.getBegin_on().equals("")) {
            beginOn = DateUtil.strToDate(req.getBegin_on(), "yyyy-MM-dd");
        }
        if (!req.getEnd_on().equals("")) {
            endOn = DateUtil.strToDate(req.getEnd_on(), "yyyy-MM-dd");
        }
        List<Integer> areaIds = StringSplitToListUtil.strToInts(req.getArea_ids(), ",");
        HouseqmExportVo hevo=new HouseqmExportVo();
        hevo.setAreaIds(areaIds);
        hevo.setBeginOn(beginOn);
        hevo.setCategoryCls(req.getCategory_cls());
        hevo.setWithRule(false);
        hevo.setTaskId(req.getTask_id());
        hevo.setProjectId(req.getProject_id());
        hevo.setEndOn(endOn);
        hevo.setRepairerId(req.getRepairer_id());
        iHouseqmExportService.exportProjectOrdersByProjIdTaskIdAreaIdsRepairedIdBeginOnEndOn(hevo);

        resp.setHeader("Content-Type", "application/octet-stream; charset=utf-8");
        resp.setHeader("Content-Disposition", " attachment; filename=\" 工程处理单.zip\"");
        resp.setHeader("Expires", "0");

        return null;
    }


}
