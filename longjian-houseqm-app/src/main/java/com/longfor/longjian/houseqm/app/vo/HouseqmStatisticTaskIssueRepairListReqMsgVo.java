package com.longfor.longjian.houseqm.app.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Houyan
 * @date 2018/12/22 0022 13:46
 */
@Data
@NoArgsConstructor
public class HouseqmStatisticTaskIssueRepairListReqMsgVo implements Serializable {
    private Integer projectId;// 项目ID
    private Integer taskId;// 任务ID
    private String source;// app名称，如：ydyf、gcjc
    private Integer areaId;// 区域id
    private Integer beginOn;   // 开始时间戳
    private Integer endOn;   // 结束时间戳
    private Integer planStatus;  // 计划状态，（1、按期完成 2、未超期未完成 3、未设置期限 4、超期完成 5、超期未完成 ）
    private Integer timestamp;  // 上次更新时间
    private Integer page;     // 第几页
    private Integer pageSize;

}
