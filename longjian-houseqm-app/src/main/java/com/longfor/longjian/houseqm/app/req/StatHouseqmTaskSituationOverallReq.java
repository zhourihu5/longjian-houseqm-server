package com.longfor.longjian.houseqm.app.req;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.app.req
 * @ClassName: StatHouseqmTaskSituationOverallReq
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/1/8 16:39
 */
@Data
@NoArgsConstructor
public class StatHouseqmTaskSituationOverallReq implements Serializable {
    //ProjectId int    `json:"project_id" zpf_reqd:"true" zpf_name:"project_id"` // 项目ID
    //	TaskIds   string `json:"task_ids" zpf_reqd:"true" zpf_name:"task_ids"`     // 任务ID,多个请用半角逗号“,”分隔
    @NotNull
    private Integer project_id;
    @NotBlank
    private String task_ids;

}
