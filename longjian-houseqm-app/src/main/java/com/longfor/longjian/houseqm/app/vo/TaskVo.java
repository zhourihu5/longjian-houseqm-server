package com.longfor.longjian.houseqm.app.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author lipeishuai
 * @date 2018/11/19 18:29
 */
@Data
@NoArgsConstructor
public class TaskVo implements Serializable {

    private int  project_id = 0;
    private int task_id = 0;
    private String name="";//任务名
    private int status = 0;//任务状态：1=未完成 2=已完成 3=已取消
    private int category_cls = 0;//模块类型 参数常量表
    private String root_category_key="";//任务类型的根节点
    private String area_ids="";//区域ID(只含顶层区域ID)(多个ID用半角逗号分隔)
    private String  area_types="";//区域类型ID(楼层,户等)(多个ID用半角逗号分隔)
    private int repairer_refund_permission = 0;//整改负责人退单权限：10=能退单 20=不能退单
    private int repairer_follower_permission = 0;//整改参与人权限：10=只能提交描述 20=可以完成整改
    private int checker_approve_permission = 0;//具有销项权的人能否销项由自己整改的问题：10=能销项 20=不能销项
    private int repaired_picture_status = 0;//整改人完成整改时：10=不强制附加照片 20=必须附加整改照片
    private int  issue_desc_status = 0;//创建问题缺陷时：10=文字描述和缺陷图片任意必填一项 20=必填文字描述 30=必填缺陷图片 40=文字描述和缺陷图片都必填 50=文字描述和缺陷图片都不填
    private String issue_default_desc = "";//创建问题时默认描述(issue_desc_status=50时生效)
    private int  plan_begin_on= 0;//计划开始时间
    private int  plan_end_on= 0;//计划完成时间
    private int  create_at= 0;//创建时间
    private int  delete_at= 0;//更新时间
    private int update_at= 0;//删除时间(0表示未删除)

    //private String extra_ops;
}