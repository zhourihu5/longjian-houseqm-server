package com.longfor.longjian.houseqm.app.req;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.app.req
 * @ClassName: ProjectOrdersReq
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/1/14 9:55
 */
@Data
@NoArgsConstructor
public class ProjectOrdersReq implements Serializable {

    //ProjectId   int    `zpf_label:"项目Id" zpf_reqd:"true"`
    //		TaskId      int    `zpf_label:"任务Id" zpf_reqd:"true"`
    //		AreaIds     string `zpf_label:"区域Id" zpf_reqd:"true"`
    //		CategoryCls int    `zpf_label:"类型："`
    //		RepairerId  int    `zpf_label:"整改人id"`
    //		BeginOn     string `zpf_label:"开始日期"`
    //		EndOn       string `zpf_label:"结束日期"`
    @NotNull
    private Integer project_id;
    @NotNull
    private Integer task_id;
    @NotBlank
    private String area_ids;
    private Integer category_cls;
    private Integer repairer_id;
    private String begin_on;
    private String end_on;

}
