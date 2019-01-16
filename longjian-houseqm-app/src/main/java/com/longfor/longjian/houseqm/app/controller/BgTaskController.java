package com.longfor.longjian.houseqm.app.controller;

import com.longfor.longjian.common.base.LjBaseResponse;
import com.longfor.longjian.common.util.DateUtil;
import com.longfor.longjian.houseqm.app.req.bgtask.BgTaskExportReq;
import com.longfor.longjian.houseqm.app.vo.bgtask.ExportPicExcelRspVo;
import com.longfor.longjian.houseqm.app.vo.bgtask.ExportPptRspVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

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

    
    /**
     * @Author hy
     * @Description 导出问题报告PPT http://192.168.37.159:3000/project/8/interface/api/3360
     * @Date 11:36 2019/1/15
     * @Param [req]
     * @return com.longfor.longjian.common.base.LjBaseResponse<com.longfor.longjian.houseqm.app.vo.bgtask.ExportPicExcelRspVo>
     **/
    @GetMapping(value = "buildingqm/export_pic_excel/",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<ExportPicExcelRspVo> exportPicExcel(BgTaskExportReq req){
        String now = DateUtil.dateToString(new Date(), "yyyy-MM-dd hh:mm");

        String file_name="工程检查：导出带图片Excel报告 "+now;

        //调服务

        LjBaseResponse<ExportPicExcelRspVo> response = new LjBaseResponse<>();
        response.setResult(0);
        //response.setData();
        return response;
    }

    /**
     * @Author hy
     * @Description 导出问题列表带图片Excel  http://192.168.37.159:3000/project/8/interface/api/3364
     * @Date 11:36 2019/1/15
     * @Param [req]
     * @return com.longfor.longjian.common.base.LjBaseResponse<com.longfor.longjian.houseqm.app.vo.bgtask.ExportPptRspVo>
     **/
    @GetMapping(value = "buildingqm/export_ppt/",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<ExportPptRspVo> exportPpt(BgTaskExportReq req){
        String now = DateUtil.dateToString(new Date(), "yyyy-MM-dd hh:mm");

        String file_name="工程检查：导出带图片Excel报告 "+now;
        //调服务
        LjBaseResponse<ExportPptRspVo> response = new LjBaseResponse<>();
        response.setResult(0);
        //response.setData();
        return response;
    }

}
