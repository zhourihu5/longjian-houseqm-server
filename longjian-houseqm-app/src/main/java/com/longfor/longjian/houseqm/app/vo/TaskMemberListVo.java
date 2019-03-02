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
public class TaskMemberListVo implements Serializable {

    private List<SquadVo> squad_list;
    private List<MemberVo> member_list;

    @Data
    @NoArgsConstructor
    public class SquadVo implements Serializable {

        private Integer id;
        private Integer project_id;
        private Integer task_id;
        private Integer squad_type;
        private String name;
        private Integer update_at;
        private Integer delete_at;
    }


    @Data
    @NoArgsConstructor
    public class MemberVo implements Serializable {

        private Integer id;
        private Integer squad_id;
        private Integer user_id;
        private Integer role_type;
        private Integer can_approve;
        private Integer can_direct_approve;
        private Integer can_reassign;
        private Integer task_id;
        private Integer update_at;
        private Integer delete_at;
    }

}
