package com.longfor.longjian.houseqm.app.controller.v3houseqmstat;

import com.google.common.collect.Lists;
import com.longfor.longjian.common.base.LjBaseResponse;
import com.longfor.longjian.common.util.CtrlTool;
import com.longfor.longjian.houseqm.app.req.v3houseqmstat.InspectionSituationExportReq;
import com.longfor.longjian.houseqm.app.service.IHouseqmStatService;
import com.longfor.longjian.houseqm.app.utils.CtrlToolUtils;
import com.longfor.longjian.houseqm.app.utils.ExportUtils;
import com.longfor.longjian.houseqm.app.vo.houseqmstat.InspectionHouseStatusInfoVo;
import com.longfor.longjian.houseqm.consts.RepossessionStatusEnum;
import com.longfor.longjian.houseqm.consts.StatisticFormInspectionIssueStatusEnum;
import com.longfor.longjian.houseqm.consts.StatisticFormInspectionStatusEnum;
import com.longfor.longjian.houseqm.domain.internalservice.AreaService;
import com.longfor.longjian.houseqm.domain.internalservice.HouseQmCheckTaskService;
import com.longfor.longjian.houseqm.po.zj2db.Area;
import com.longfor.longjian.houseqm.po.zj2db.HouseQmCheckTask;
import com.longfor.longjian.houseqm.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

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

    // ???????????? -???????????? -???????????? ??????excel
    @RequestMapping(value = "inspection_situation_export", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<Object> inspectionSituationExport(HttpServletRequest request, HttpServletResponse response, @Validated InspectionSituationExportReq req){
        LjBaseResponse<Object> ljBaseResponse = new LjBaseResponse<>();
        try {
            ctrlTool.projPermMulti(request, new String[]{"??????.????????????.??????.??????", "??????.????????????.??????.??????"});
        } catch (Exception e) {
            log.error(e.getMessage());
            return CtrlToolUtils.errorReturn(ljBaseResponse,e);
        }

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
        List<InspectionHouseStatusInfoVo> details = iHouseqmStatService.formatFenhuHouseInspectionStatusInfoByAreaIds(req.getIssue_status(),req.getTask_id(), areaIds);
        // ?????? ?????? ???????????????
        String title = "????????????";
        HouseQmCheckTask task = houseQmCheckTaskService.getHouseQmCheckTaskByProjTaskId(req.getProject_id(), req.getTask_id());
        if (task != null) {
            title += "???" + task.getName();
        }
        if (req.getArea_id() > 0) {
            Area area = areaService.selectById(req.getArea_id());
            if (area != null) {
                title += "???" + area.getName();
            }
        }
        title += "???" + StatisticFormInspectionStatusEnum.getName(req.getStatus());
        title += "???" + StatisticFormInspectionIssueStatusEnum.getName(req.getIssue_status());

        response.setHeader("Content-Type", "application/vnd.ms-excel; charset=utf-8");
        try {
            response.setHeader("Content-Disposition", " attachment; filename=" + new String(title.getBytes("utf-8"), "iso8859-1") + ".xlsx"); //File name extension was wrong
        } catch (UnsupportedEncodingException e) {
          log.error("????????????",e.getMessage());
        }
        response.setHeader("Expires", " 0");

        try (ServletOutputStream os = response.getOutputStream()) {
            SXSSFWorkbook wb = ExportUtils.exportInspectionSituationExcel(details);
            wb.write(os);
            os.flush();
        } catch (IOException e) {
            log.error("???????????? excel ????????????");
            ljBaseResponse.setResult(1);
            ljBaseResponse.setMessage(e.getMessage());
        }
        return ljBaseResponse;
    }
}
