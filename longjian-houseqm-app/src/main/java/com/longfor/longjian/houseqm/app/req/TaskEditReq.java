package com.longfor.longjian.houseqm.app.req;

import com.longfor.longjian.common.consts.checktask.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * Created by Dongshun on 2019/1/14.
 */
@Data
@NoArgsConstructor
public class TaskEditReq {
    @NotNull
    private Integer project_id;
    @NotNull
    private Integer task_id;
    @NotNull
    private String name;
    @NotNull
    private String area_ids;
    @NotNull
    private String area_types;
    @NotNull
    private String plan_begin_on;
    @NotNull
    private String plan_end_on;
    @NotNull
    private String repairer_ids;
    @NotNull
    private String checker_groups;
    private Integer repairer_refund_permission = CheckTaskRepairerRefundPermission.No.getValue();
    private Integer repairer_follower_permission = CheckTaskRepairerFollowerPermission.CompleteRepair.getValue();
    private Integer checker_approve_permission = CheckerApprovePermission.No.getValue();
    private Integer repaired_picture_status = CheckTaskRepairedPictureEnum.UnForcePicture.getValue();
    private Integer issue_desc_status = CheckTaskIssueDescEnum.Arbitrary.getValue();
    private String issue_default_desc = "该问题无文字描述";
    private String push_strategy_config = "";
}
