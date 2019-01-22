package com.longfor.longjian.houseqm.app.controller.houseqmrepossession;

import com.longfor.longjian.common.base.LjBaseResponse;
import com.longfor.longjian.houseqm.app.feginClient.IRepossessionFeignService;
import com.longfor.longjian.houseqm.app.req.houseqmrepossession.RepossessionGetReq;
import com.longfor.longjian.houseqm.app.req.houseqmrepossession.RepossessionReportReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.app.controller.houseqmrepossession
 * @ClassName: HouseqmRepossessionController
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/1/22 14:52
 */
@RestController
@RequestMapping(value = "/houseqm/v3/papi/repossession/")
@Slf4j
public class HouseqmRepossessionController {

    @Resource
    private IRepossessionFeignService iRepossessionFeignService;

    /**
     * @Author hy
     * @Description 提交验房报告
     * http://192.168.37.159:3000/project/8/interface/api/3292
     * @Date 15:18 2019/1/22
     * @Param [repossessionReportReq]
     * @return com.longfor.longjian.common.base.LjBaseResponse<java.lang.Object>
     **/
    @GetMapping(value= "report/", produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<Object> report(@Validated RepossessionReportReq repossessionReportReq){
        return iRepossessionFeignService.report(repossessionReportReq);
    }

    /**
     * @Author hy
     * @Description 获取验房报告
     * http://192.168.37.159:3000/project/8/interface/api/3300
     * @Date 15:18 2019/1/22
     * @Param
     * @return
     **/
    @GetMapping(value= "get/", produces= MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<Object> get(@Validated RepossessionGetReq repossessionGetReq){
        return iRepossessionFeignService.get(repossessionGetReq);
    }



}
