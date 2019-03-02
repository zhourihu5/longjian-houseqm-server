package com.longfor.longjian.houseqm.app.req.houseqmstat;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.app.req.houseqmstat
 * @ClassName: StatTaskDetailReq
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/1/17 21:02
 */
@Data
@NoArgsConstructor
public class StatTaskDetailReq implements Serializable {

    @NotNull
    private Integer project_id;
    @NotNull
    private Integer task_id;

    private Integer area_id; // 区域ID
}
