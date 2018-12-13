package com.longfor.longjian.houseqm.innervo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Houyan
 * @date 2018/12/13 0013 15:09
 */
@Data
@NoArgsConstructor
public class ApiBuildingQmCheckTaskMsg implements Serializable {
    /**
     * desc='项目ID'
     */
    private Integer project_id=0;

    /**
     * desc='任务ID'
     */
    private Integer task_id=0;

    /**
     *  desc='任务名'
     */
    private String name="";

    /**
     * desc='任务状态：1=未完成 2=已完成 3=已取消'
     */
    private Integer status=0;
    /**
     * desc='模块类型 参数常量表'
     */
    private Integer category_cls=0;
    /**
     * desc='任务类型的根节点'
     */
    private String root_category_key ="";
    /**
     * desc='区域ID(只含顶层区域ID)(多个ID用半角逗号分隔)'
     */
    private String area_ids ="";
    /**
     * desc='区域类型ID(楼层,户等)(多个ID用半角逗号分隔)'
     */
    private String area_types ="";
    /**
     * desc='整改负责人退单权限：10=能退单 20=不能退单'
     */
    private Integer repairer_refund_permission   =0;
    /**
     * desc='整改参与人权限：10=只能提交描述 20=可以完成整改'
     */
    private  Integer repairer_follower_permission =0;
    /**
     *desc='具有销项权的人能否销项由自己整改的问题：10=能销项 20=不能销项'
     */
    private Integer checker_approve_permission   =0;
    /**
     * desc='整改人完成整改时：10=不强制附加照片 20=必须附加整改照片'
     */
    private Integer repaired_picture_status      =0;
    /**
     * desc='创建问题缺陷时：10=文字描述和缺陷图片任意必填一项 20=必填文字描述 30=必填缺陷图片 40=文字描述和缺陷图片都必填 50=文字描述和缺陷图片都不填'
     */
    private Integer issue_desc_status=0;
    /**
     * desc='创建问题时默认描述(issue_desc_status=50时必填)'
     */
    private String issue_default_desc="";
    /**
     * desc='消息推送策略信息'
     */
    private String push_strategy_config="";
    /**
     * desc='计划开始时间'
     */
    private Integer plan_begin_on=0;
    /**
     * desc='计划完成时间'
     */
    private  Integer plan_end_on=0;
    /**
     * desc='创建时间'
     */
    private Integer create_at=0;
    /**
     * desc='更新时间'
     */
    private Integer update_at =0;
    /**
     * desc='删除时间(0表示未删除)'
     */
    private Integer delete_at=0;
    /**
     * desc='额外操作'
     */
    private ApiBuildingQmCheckTaskExtraOps extra_ops=new ApiBuildingQmCheckTaskExtraOps() ;

    @Data
    @NoArgsConstructor
    public class ApiBuildingQmCheckTaskExtraOps implements Serializable{
        /**
         *  desc='导出问题报告'
         */
        private String export_issue="";
    }


}
