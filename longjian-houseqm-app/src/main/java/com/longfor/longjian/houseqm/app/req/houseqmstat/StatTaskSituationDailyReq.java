package com.longfor.longjian.houseqm.app.req.houseqmstat;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.app.req.houseqmstat
 * @ClassName: StatTaskSituationDailyReq
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/1/17 20:24
 */
@Data
@NoArgsConstructor
public class StatTaskSituationDailyReq implements Serializable {

    @NonNull
    private Integer project_id;// 项目id
    @NotNull
    private String task_ids;// 任务ID,多个请用半角逗号“,”分隔
    @NotNull
    private Integer page;// 页码
    @NotNull
    private Integer page_size;// 一页多少条


}
