package com.longfor.longjian.houseqm.app.req.tasklist;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.app.req.tasklist
 * @ClassName: TaskListListReq
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/1/18 13:38
 */
@Data
@NoArgsConstructor
public class TaskListListReq implements Serializable {

    @NotNull
    private Integer team_id;//公司ID
    @NotNull
    private Integer project_id;//项目ID
    @NotNull
    private Integer category_cls;//模块类型
    @NotNull
    private Integer status;//任务状态


}
