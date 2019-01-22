package com.longfor.longjian.houseqm.app.req.buildingqm;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.app.req.buildingqm
 * @ClassName: MyIssuePatchListReq
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/1/21 11:36
 */
@Data
@NoArgsConstructor
public class MyIssuePatchListReq implements Serializable {
    //private Integer task_ids;
    //private Long device_id;
    private Integer last_id;
    @NotNull
    private Integer task_id;//任务ID
    private Integer timestamp = 0;//上次更新时间
    //private String token;
}
