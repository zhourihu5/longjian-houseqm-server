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
    private Integer ProjectId; // 项目ID
    private Integer TaskId;    // 任务ID
    private String Name;      // 任务名
    private Integer Status;   // 任务状态：1=未完成 2=已完成 3=已取消
    private Integer CategoryCls;      // 模块类型 参数常量表
    private String RootCategoryKey;  // 任务类型的根节点
    private String AreaIds;         // 区域ID(只含顶层区域ID)(多个ID用半角逗号分隔)
    private String AreaTypes;      // 区域类型ID(楼层,户等)(多个ID用半角逗号分隔)
    private Integer PlanBeginOn;  // 计划开始时间
    private Integer PlanEndOn;   // 计划完成时间
    private Integer CreateAt;   // 创建时间
    private Integer UpdateAt;  // 更新时间
    private Integer DeleteAt; // 删除时间(0表示未删除)
    private HouseQmCheckTaskExOpsVo ExtraOps;        // 额外操作
}
