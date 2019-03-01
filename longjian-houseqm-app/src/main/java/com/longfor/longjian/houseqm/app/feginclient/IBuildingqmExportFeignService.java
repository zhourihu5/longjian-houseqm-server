package com.longfor.longjian.houseqm.app.feginclient;

import com.longfor.gaia.gfs.web.feign.LFFeignClient;
import com.longfor.gaia.gfs.web.feign.config.LFFeignConfiguration;
import com.longfor.longjian.common.base.LjBaseResponse;
import com.longfor.longjian.houseqm.app.req.bgtask.ExportBuildingExcelReq;
import com.longfor.longjian.houseqm.app.vo.bgtask.ExportRsp;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.app.feginclient
 * @ClassName: IBuildqmExportFeignService
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/1/22 15:27
 */
@Service
@LFFeignClient(group = "longjian-basic-server",value = "buildingqmexport",configuration = LFFeignConfiguration.class)
public interface IBuildingqmExportFeignService {

    //导出问题列表带图片Excel
    @RequestMapping(value = "export_pic_excel", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    LjBaseResponse<ExportRsp> exportPicExcel(@RequestBody ExportBuildingExcelReq req);

    // 工程检查-问题列表- 导出ppt 存入redis中 用户从任务记录中下载
    @RequestMapping(value = "export_ppt",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<ExportRsp> exportPpt(@RequestBody ExportBuildingExcelReq req);

}
