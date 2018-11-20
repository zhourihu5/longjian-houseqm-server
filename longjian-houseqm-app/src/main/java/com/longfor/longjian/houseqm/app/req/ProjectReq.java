package com.longfor.longjian.houseqm.app.req;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author lipeishuai
 * @date 2018/11/17 17:23
 */
@Data
@NoArgsConstructor
public class ProjectReq implements Serializable {

    private Integer group_id;
    private Integer project_id;
    private Integer team_id;
    private Integer page_level;
    private String category_cls;
    private Integer task_id;

    private String tip;

}
