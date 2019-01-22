package com.longfor.longjian.houseqm.app.feginClient;

import com.longfor.gaia.gfs.web.feign.LFFeignClient;
import com.longfor.gaia.gfs.web.feign.config.LFFeignConfiguration;
import com.longfor.longjian.common.base.LjBaseResponse;
import com.longfor.longjian.houseqm.app.req.bgtask.ExportBuildingExcelReq;
import com.longfor.longjian.houseqm.app.vo.bgtask.ExportRsp;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.app.feginClient
 * @ClassName: IBuildqmExportFeignService
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/1/22 15:27
 */
@Service
@LFFeignClient(group = "longjian-basic-server",value = "buildingqmexport",configuration = LFFeignConfiguration.class)
public interface IBuildingqmExportFeignService {

    //导出问题列表带图片Excel
    @PostMapping(value = "export_pic_excel/", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    LjBaseResponse<ExportRsp> exportPicExcel(ExportBuildingExcelReq req);



}
