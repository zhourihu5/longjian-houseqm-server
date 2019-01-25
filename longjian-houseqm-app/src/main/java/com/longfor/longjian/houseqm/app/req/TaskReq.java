package com.longfor.longjian.houseqm.app.req;

import com.longfor.longjian.common.consts.checktask.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * Created by Dongshun on 2019/1/8.
 */
@Data
@NoArgsConstructor
public class TaskReq {
    @NotNull
    private Integer project_id;
    @NotNull
    private String name;
    @NotNull
    private Integer category_cls;//模块类型
    @NotNull
    private String root_category_key;//任务类型的根节点
    @NotNull
    private String area_ids;//区域IDs,多个用半角逗号“,”分隔
    @NotNull
    private String area_types;//区域类型IDs,多个用半角逗号“,”分隔
    @NotNull
    private String plan_begin_on;
    @NotNull
    private String plan_end_on;
    @NotNull
    private String repairer_ids;
    @NotNull
    private String checker_groups;
    private Integer repairer_refund_permission= CheckTaskRepairerRefundPermission.No.getValue();//非必须
    private Integer repairer_follower_permission= CheckTaskRepairerFollowerPermission.CompleteRepair.getValue();//非必须
    private Integer checker_approve_permission= CheckerApprovePermission.No.getValue();//非必须
    private Integer repaired_picture_status=CheckTaskRepairedPictureEnum.UnForcePicture.getValue();//非必须
    private Integer issue_desc_status=CheckTaskIssueDescEnum.Arbitrary.getValue();//非必须
    private String issue_default_desc="该问题无文字描述";//非必须
    private String push_strategy_config="";//非必须
}
