package com.longfor.longjian.houseqm.app.req.taskcheckedareas;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.app.req.taskcheckedareas
 * @ClassName: CheckedAreasReq
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/1/18 13:54
 */
@Data
@NoArgsConstructor
public class CheckedAreasReq implements Serializable {

    @NotNull
    private Integer project_id;//项目ID
    @NotNull
    private Integer task_id;//任务ID

}
