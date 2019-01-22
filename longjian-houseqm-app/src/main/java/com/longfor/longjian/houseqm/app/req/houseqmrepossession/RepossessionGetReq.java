package com.longfor.longjian.houseqm.app.req.houseqmrepossession;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.io.Serializable;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.app.req.houseqmrepossession
 * @ClassName: RepossessionGetReq
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/1/22 15:16
 */
@Data
public class RepossessionGetReq implements Serializable {

    @Max(1024)
    @Min(1)
    private String task_ids;
    private Integer timestamp;
}
