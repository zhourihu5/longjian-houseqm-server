package com.longfor.longjian.houseqm.app.controller.v3houseqmstat;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.longfor.longjian.common.base.LjBaseResponse;
import com.longfor.longjian.common.util.CtrlTool;
import com.longfor.longjian.houseqm.app.req.v3houseqmstat.InspectionSituationExportReq;
import com.longfor.longjian.houseqm.app.service.IHouseqmStatService;
import com.longfor.longjian.houseqm.app.service.IHouseqmStatisticService;
import com.longfor.longjian.houseqm.app.utils.ExportUtils;
import com.longfor.longjian.houseqm.app.vo.houseqmstat.InspectionHouseStatusInfoVo;
import com.longfor.longjian.houseqm.consts.RepossessionStatusEnum;
import com.longfor.longjian.houseqm.consts.StatisticFormInspectionIssueStatusEnum;
import com.longfor.longjian.houseqm.consts.StatisticFormInspectionStatusEnum;
import com.longfor.longjian.houseqm.domain.internalService.AreaService;
import com.longfor.longjian.houseqm.domain.internalService.HouseQmCheckTaskService;
import com.longfor.longjian.houseqm.po.zj2db.Area;
import com.longfor.longjian.houseqm.po.zj2db.HouseQmCheckTask;
import com.longfor.longjian.houseqm.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.app.controller.v3houseqmstat
 * @ClassName: V3HouseqmStatController
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/2/19 16:44
 */
@RestController
@RequestMapping("v3/houseqm/stat/")
@Slf4j
public class V3HouseqmStatController {

    @Resource
    private CtrlTool ctrlTool;
    @Resource
    private IHouseqmStatService iHouseqmStatService;
    @Resource
    private HouseQmCheckTaskService houseQmCheckTaskService;
    @Resource
    private AreaService areaService;

    // 统计报告 -任务概况 -验房详情 导出excel
    @RequestMapping(value = "inspection_situation_export", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<Object> inspectionSituationExport(HttpServletRequest request, HttpServletResponse response, @Validated InspectionSituationExportReq req) throws Exception {
        ctrlTool.projPermMulti(request, new String[]{"项目.移动验房.统计.查看", "项目.工程检查.统计.查看"});
        if (req.getArea_id() == null) req.setArea_id(0);
        if (req.getIssue_status() == null) req.setIssue_status(0);
        if (req.getStatus() == null) req.setStatus(0);

        if (!req.getStatus().equals(RepossessionStatusEnum.Accept.getId())) {
            req.setStart_time("");
            req.setEnd_time("");
        }
        List<Integer> areaIds = Lists.newArrayList();
        if (StringUtils.isNotEmpty(req.getArea_ids())) {
            areaIds = StringUtil.strToInts(req.getArea_ids(), ",");
        } else {
            areaIds = iHouseqmStatService.searchInspectionAreaIdsByConditions(req.getProject_id(), req.getTask_id(), req.getArea_id(), req.getStatus(), req.getIssue_status());
        }
        List<InspectionHouseStatusInfoVo> details = iHouseqmStatService.formatFenhuHouseInspectionStatusInfoByAreaIds(req.getTask_id(), areaIds);
        // 拼接 名字 导出文件名
        String title = "验房详情";
        HouseQmCheckTask task = houseQmCheckTaskService.getHouseQmCheckTaskByProjTaskId(req.getProject_id(), req.getTask_id());
        if (task != null) title += "－" + task.getName();
        if (req.getArea_id() > 0) {
            Area area = areaService.selectById(req.getArea_id());
            if (area != null) title += "－" + area.getName();
        }
        title += "－" + StatisticFormInspectionStatusEnum.getName(req.getStatus());
        title += "－" + StatisticFormInspectionIssueStatusEnum.getName(req.getIssue_status());

        response.setHeader("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet; charset=utf-8");
        response.setHeader("Content-Disposition", " attachment; filename=" + new String(title.getBytes("gbk"), "iso8859-1") + ".xls"); //File name extension was wrong
        response.setHeader("Expires", " 0");
        // 导出
        Map<String, Object> map = Maps.newHashMap();
        map.put("details", details);
        ExportUtils.exportStatExcel("inspection_situation_excel_fhys.ftl", map, response, request);

        return new LjBaseResponse<>();
    }
}
