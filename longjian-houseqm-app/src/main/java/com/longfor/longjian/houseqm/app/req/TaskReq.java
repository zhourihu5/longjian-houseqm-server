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
    private String repairer_ids;//整改人ids
    @NotNull
    private String checker_groups;//检查人组信息
    private Integer repairer_refund_permission= CheckTaskRepairerRefundPermission.No.getValue();//整改负责人退单权限：10=能退单 20=不能退单
    private Integer repairer_follower_permission= CheckTaskRepairerFollowerPermission.CompleteRepair.getValue();//整改参与人权限：10=只能提交描述 20=可以完成整改
    private Integer checker_approve_permission= CheckerApprovePermission.No.getValue();//具有销项权的人能否销项由自己整改的问题：10=能销项 20=不能销项
    private Integer repaired_picture_status=CheckTaskRepairedPictureEnum.UnForcePicture.getValue();//整改人完成整改时：10=不强制附加照片 20=必须附加整改照片
    private Integer issue_desc_status=CheckTaskIssueDescEnum.Arbitrary.getValue();//创建问题缺陷时：10=文字描述和缺陷图片任意必填一项 20=必填文字描述 30=必填缺陷图片 40=文字描述和缺陷图片都必填 50=文字描述和缺陷图片都不填
    private String issue_default_desc="该问题无文字描述";//创建问题时默认描述(issue_desc_status=50时必填)
    private String push_strategy_config="";//消息推送策略信息
}
