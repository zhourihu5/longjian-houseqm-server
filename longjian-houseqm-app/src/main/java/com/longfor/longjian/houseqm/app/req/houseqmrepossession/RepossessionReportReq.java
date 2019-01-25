package com.longfor.longjian.houseqm.app.req.houseqmrepossession;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.app.req.houseqmrepossession
 * @ClassName: Reposs
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/1/22 15:06
 */
@Data
public class RepossessionReportReq {

    @NotNull
    private String data;//报告内容
}
