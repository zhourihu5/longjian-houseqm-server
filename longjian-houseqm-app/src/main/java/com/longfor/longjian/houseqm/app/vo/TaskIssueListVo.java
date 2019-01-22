package com.longfor.longjian.houseqm.app.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author lipeishuai
 * @date 2018/11/17 17:23
 */
@Data
@NoArgsConstructor
public class TaskIssueListVo implements Serializable{

    private TaskIussueVo item;

    @Data
    @NoArgsConstructor
    public class TaskIussueVo implements Serializable {

        private Integer  task;
        private Integer issue;
        private Integer  issue_log;
        private Integer task_members;
        private Integer issue_members;
    }


}
