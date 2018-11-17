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

        private int id;
        private int user_id;
        private int squad_id;
        private int role_type;
        private int  can_approve;
        private int can_direct_approve;
        private String real_name;
        private int  task_id;
    }

}
