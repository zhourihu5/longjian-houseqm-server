package com.longfor.longjian.houseqm.app.req.houseqmstat;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.app.req.houseqmstat
 * @ClassName: StatTaskSituationOverallReq
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/1/18 10:16
 */
@Data
@NoArgsConstructor
public class StatTaskSituationOverallReq implements Serializable {
    @NotNull
    private Integer project_id;// 项目id

    @NotNull
    private String task_ids;// 任务ID,多个请用半角逗号“,”分隔
}
