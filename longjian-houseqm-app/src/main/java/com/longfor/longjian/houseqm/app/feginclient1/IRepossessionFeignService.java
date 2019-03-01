package com.longfor.longjian.houseqm.app.feginclient1;

import com.longfor.gaia.gfs.web.feign.LFFeignClient;
import com.longfor.gaia.gfs.web.feign.config.LFFeignConfiguration;
import com.longfor.longjian.common.base.LjBaseResponse;
import com.longfor.longjian.houseqm.app.req.houseqmrepossession.RepossessionReportReq;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.app.feginclient
 * @ClassName: IRepossessionFeignService
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/1/22 15:08
 */
@Service
@LFFeignClient(group = "longjian-basic-server", value = "repossession", configuration = LFFeignConfiguration.class)
public interface IRepossessionFeignService {

    /**
     * 发布验房报告
     *
     * @param repossessionReportReq
     * @return
     */
    @RequestMapping(value = "report", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    LjBaseResponse<Object> report(@Validated @RequestBody RepossessionReportReq repossessionReportReq);


    /**
     * 获取验房报告
     *
     * @param
     * @return
     */
    @RequestMapping(value = "get", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    LjBaseResponse<Object> get(@RequestParam(required = true, name = "task_ids") String task_ids,
                               @RequestParam(required = true, name = "timestamp") Integer timestamp);

}
