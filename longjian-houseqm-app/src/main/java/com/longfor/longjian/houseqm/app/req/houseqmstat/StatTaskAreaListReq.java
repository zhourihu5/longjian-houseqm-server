package com.longfor.longjian.houseqm.app.req.houseqmstat;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.app.req.houseqmstat
 * @ClassName: StatTaskAreaListReq
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/1/17 20:36
 */
@Data
@NoArgsConstructor
public class StatTaskAreaListReq implements Serializable {

    @NotNull
    private Integer project_id;// 项目id
    @NotNull
    private Integer task_id;// 任务ID

    private Integer typ;// 区域类型
}
