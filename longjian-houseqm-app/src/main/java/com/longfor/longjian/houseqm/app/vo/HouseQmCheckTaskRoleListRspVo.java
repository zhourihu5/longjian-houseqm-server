package com.longfor.longjian.houseqm.app.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.app.vo
 * @ClassName: HouseQmCheckTaskRoleListRspVo
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/1/9 11:39
 */
@Data
@NoArgsConstructor
public class HouseQmCheckTaskRoleListRspVo implements Serializable {

    //Id         int    `json:"id" zpf_reqd:"true" zpf_name:"id"`                   // ID
    private Integer id;
    //	UserId     int    `json:"user_id" zpf_reqd:"true" zpf_name:"user_id"`         // 用户id
    private Integer user_id;
    //	SquadId    int    `json:"squad_id" zpf_reqd:"true" zpf_name:"squad_id"`       // 检查小组id
    private Integer squad_id;
    //	RoleType   int    `json:"role_type" zpf_reqd:"true" zpf_name:"role_type"`     // 角色类型  10=检查人 20=整改人
    private Integer role_type;
    //	CanApprove int    `json:"can_approve" zpf_reqd:"true" zpf_name:"can_approve"` // 能否消项问题  10=可以消项 20=不可以消项
    private Integer can_approve;
    //	RealName   string `json:"real_name" zpf_reqd:"true" zpf_name:"real_name"`     // 用户名称
    private String real_name;
    //	TaskId     int    `json:"task_id" zpf_reqd:"true" zpf_name:"task_id"`         // 任务ID
    private Integer task_id;
}
