package com.longfor.longjian.houseqm.app.req;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.app.req
 * @ClassName: StatHouseqmCompleteDailyReq
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/1/8 20:18
 */
@Data
@NoArgsConstructor
public class StatHouseqmCompleteDailyReq implements Serializable {
    @NotNull
    private Integer project_id;// 项目ID
    @NotBlank
    private String task_ids;// 任务ID,多个请用半角逗号“,”分隔
    private String begin_on; // 开始时间
    private String end_on;// 结束时间
    @NotNull
    private Integer page;// 页码
    @NotNull
    private Integer page_size; // 一页多少条

}
