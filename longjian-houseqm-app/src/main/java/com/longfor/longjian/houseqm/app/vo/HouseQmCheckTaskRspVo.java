package com.longfor.longjian.houseqm.app.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Created by Dongshun on 2018/12/15.
 */
@Data
@NoArgsConstructor
public class HouseQmCheckTaskRspVo implements Serializable {
    private Integer project_id; // 项目ID
    private Integer task_id;    // 任务ID
    private String name;      // 任务名
    private Integer status;   // 任务状态：1=未完成 2=已完成 3=已取消
    private Integer category_cls;      // 模块类型 参数常量表
    private String root_category_key;  // 任务类型的根节点
    private String area_ids;         // 区域ID(只含顶层区域ID)(多个ID用半角逗号分隔)
    private String area_types;      // 区域类型ID(楼层,户等)(多个ID用半角逗号分隔)
    private Integer plan_begin_on;  // 计划开始时间
    private Integer plan_end_on;   // 计划完成时间
    private Integer create_at;   // 创建时间
    private Integer update_at;  // 更新时间
    private Integer delete_at; // 删除时间(0表示未删除)
    private HouseQmCheckTaskExOpsVo extra_ops;        // 额外操作
}
