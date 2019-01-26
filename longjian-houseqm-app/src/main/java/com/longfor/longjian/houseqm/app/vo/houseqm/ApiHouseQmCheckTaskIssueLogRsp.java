package com.longfor.longjian.houseqm.app.vo.houseqm;

import com.longfor.longjian.houseqm.app.vo.ApiHouseQmCheckTaskIssueLogDetailRspVo;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.app.vo.houseqm
 * @ClassName: ApiHouseQmCheckTaskIssueLogRsp
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/1/21 14:28
 */
@Data
@NoArgsConstructor
public class ApiHouseQmCheckTaskIssueLogRsp implements Serializable {

    private Integer id;//id
    private Integer project_id;// 项目ID
    private Integer task_id;// 任务ID
    private String uuid;// Uuid
    private String issue_uuid;// 问题Uuid
    private Integer sender_id;// 提交人user_id
    private String desc;// 问题描述
    private Integer status;// 整改状态：当前issue状态 0没有问题 1已记录未分配 2已分配未整改 3已整改未审核 4已审核（消项） 5已取消 6=整改中（介乎于2和3之间）
    private String attachment_md5_list; // 产生Issue时的现场照片，多个用半角都好“,”分隔
    private String audio_md5_list;// 录音
    private String memo_audio_md5_list;// 录音备忘录
    private Integer client_create_at;// 客户端记录时间
    private ApiHouseQmCheckTaskIssueLogDetailRspVo detail;// 其它数据
    private Integer update_at;// 更新时间
    private Integer delete_at; // 删除时间(0表示未删除)


}
