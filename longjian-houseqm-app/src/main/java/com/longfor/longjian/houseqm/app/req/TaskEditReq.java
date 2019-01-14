package com.longfor.longjian.houseqm.app.req;

import com.longfor.longjian.common.consts.checktask.*;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Dongshun on 2019/1/14.
 */
@Data
@NoArgsConstructor
public class TaskEditReq {
    private  Integer project_id;
    private  Integer task_id;
    private  String name;
    private  String area_ids;
    private  String area_types;
    private  String plan_begin_on;
    private  String plan_end_on;
    private  String  repairer_ids;
    private  String checker_groups;
    private  Integer repairer_refund_permission=CheckTaskRepairerRefundPermission.No.getValue();
    private  Integer repairer_follower_permission=CheckTaskRepairerFollowerPermission.CompleteRepair.getValue();
    private  Integer checker_approve_permission=CheckerApprovePermission.No.getValue();
    private  Integer repaired_picture_status=CheckTaskRepairedPictureEnum.UnForcePicture.getValue();
    private  Integer issue_desc_status=CheckTaskIssueDescEnum.Arbitrary.getValue();
    private  String  issue_default_desc="该问题无文字描述";
    private  String  push_strategy_config="";
}
