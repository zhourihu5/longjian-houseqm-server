package com.longfor.longjian.houseqm.app.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lipeishuai on 2018/11/11.
 */
@Data
@NoArgsConstructor
public class TaskRoleListVo implements Serializable{

    private List<TaskRoleVo> role_list;

    @Data
    @NoArgsConstructor
    public class TaskRoleVo implements Serializable {

        private Integer id;
        private Integer user_id;
        private Integer squad_id;
        private Integer role_type;
        private Integer  can_approve;
        private Integer can_direct_approve;
        private Integer can_reassign;
        private String real_name;
        private Integer  task_id;
    }

}
