package com.longfor.longjian.houseqm.app.controller;

import com.longfor.longjian.common.base.LjBaseResponse;
import com.longfor.longjian.houseqm.app.req.issue.IssueExportPdfReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.awt.*;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.app.controller
 * @ClassName: HouseqmIssueController
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/1/10 17:02
 */
@RestController
@RequestMapping("oapi/v3/houseqm/issue/")
@Slf4j
public class HouseqmIssueController {

    
    /**
     * @Author hy
     * @Description  项目下问题导出PDF到任务
     * http://192.168.37.159:3000/project/8/interface/api/3356
     * @Date 17:13 2019/1/10
     * @Param [req]
     * @return com.longfor.longjian.common.base.LjBaseResponse
     **/
    @PostMapping(value = "export_pdf/", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse exportPdf(@Valid IssueExportPdfReq req) {
        //todo 鉴权 _, _, err = ctrl_tool.ProjPermMulti(c, []string{"项目.移动验房.问题管理.查看", "项目.工程检查.问题管理.查看"})



        return null;
    }

    public LjBaseResponse batch_appoint() {

        return null;
    }

    public LjBaseResponse batch_approve() {

        return null;
    }

    public LjBaseResponse batch_delete() {

        return null;
    }


}
