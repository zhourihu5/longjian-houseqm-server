package com.longfor.longjian.houseqm.app.controller.papiv3;

import com.longfor.longjian.common.base.LjBaseResponse;
import com.longfor.longjian.houseqm.app.feginclient1.IBuildingqmExportFeignService;
import com.longfor.longjian.houseqm.app.req.bgtask.ExportBuildingExcelReq;
import com.longfor.longjian.houseqm.app.vo.bgtask.ExportRsp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;


/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.app.controller
 * @ClassName: BgTaskController
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/1/15 11:15
 */
@RestController
@RequestMapping("/papi/v3/bgtask/")
@Slf4j
public class BgTaskController {

    @Resource
    private IBuildingqmExportFeignService iBuildingqmExportFeignService;


    /**
     * @return com.longfor.longjian.common.base.LjBaseResponse<com.longfor.longjian.houseqm.app.vo.bgtask.ExportPicExcelRspVo>
     * @Author hy
     * @Description 导出问题列表带图片Excel
     * http://192.168.37.159:3000/project/8/interface/api/3364
     * @Date 11:36 2019/1/15
     * @Param [req]
     **/
    @RequestMapping(value = "buildingqm/export_pic_excel", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<ExportRsp> exportPicExcel(@Validated ExportBuildingExcelReq req) {
        return iBuildingqmExportFeignService.exportPicExcel(req);
    }

    /**
     * @return com.longfor.longjian.common.base.LjBaseResponse<com.longfor.longjian.houseqm.app.vo.bgtask.ExportPptRspVo>
     * @Author hy
     * @Description 导出问题报告PPT  先导出到任务记录中 缓存中redis
     * http://192.168.37.159:3000/project/8/interface/api/3360
     * @Date 11:36 2019/1/15
     * @Param [req]
     **/
    @RequestMapping(value = "buildingqm/export_ppt", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<ExportRsp> exportPpt(@Validated ExportBuildingExcelReq req) {
        return iBuildingqmExportFeignService.exportPpt(req);
    }

}
