package com.longfor.longjian.houseqm.app.controller;

import com.longfor.longjian.common.base.LjBaseResponse;
import com.longfor.longjian.houseqm.app.req.ProjectOrdersReq;
import com.longfor.longjian.houseqm.app.service.IHouseqmExportService;
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
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.app.controller
 * @ClassName: HouseqmExportController
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/1/12 19:36
 */
@RestController
@RequestMapping("oapi/v3/houseqm/export/")
@Slf4j
@Deprecated
public class HouseqmExportController {


    @Resource
    private IHouseqmExportService iHouseqmExportService;


    /**
     * @return com.longfor.longjian.common.base.LjBaseResponse
     * ////该接口不用了，废弃
     * @Author hy
     * @Description 工程处理单导出
     * @Date 10:08 2019/1/14
     * @Param [req]
     **/
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
        iHouseqmExportService.exportProjectOrdersByProjIdTaskIdAreaIdsRepairedIdBeginOnEndOn(req.getProject_id(), req.getTask_id(), areaIds, req.getRepairer_id(), beginOn, endOn, req.getCategory_cls(), false);

        resp.setHeader("Content-Type", "application/octet-stream; charset=utf-8");
        resp.setHeader("Content-Disposition", " attachment; filename=\" 工程处理单.zip\"");
        resp.setHeader("Expires", "0");

        return null;
    }


}
