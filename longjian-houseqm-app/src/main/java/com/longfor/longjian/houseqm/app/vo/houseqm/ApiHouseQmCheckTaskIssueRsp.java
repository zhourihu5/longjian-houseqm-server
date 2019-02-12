package com.longfor.longjian.houseqm.app.vo.houseqm;

import com.longfor.longjian.houseqm.app.vo.ApiHouseQmCheckTaskIssueLogDetailRspVo;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.app.vo.houseqm
 * @ClassName: ApiHouseQmCheckTaskIssueRsp
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/1/21 15:04
 */
@Data
@NoArgsConstructor
public class ApiHouseQmCheckTaskIssueRsp implements Serializable {

    private Integer id;//id
    private Integer project_id;// 项目ID
    private Integer task_id;// 任务ID
    private String uuid;// Uuid
    private Integer sender_id;// 提交人user_id
    private Integer plan_end_on;             // 计划整改完成时间
    private Integer end_on;                 // 实际整改完成时间
    private Integer area_id;                // 区域ID
    private String area_path_and_id;//// 区域路径和id
    private Integer category_cls;           // 任务模块
    private String category_key;            // 任务类型key
    private String category_path_and_key;//// 任务类型路径和Key
    private String check_item_key;           // 检查项key
    private String check_item_path_and_key;//// 检查项路径和key
    private String drawing_md5;// // 图纸文件MD5
    private Integer pos_x;                  // 在图纸上的位置X
    private Integer pos_y;                  // 在图纸上的位置Y
    private String title;                  // 问题标题
    private Integer typ;                   // 0 普通记录；1 问题记录
    private String content;//// 问题描述
    private Integer condition;// // 问题严重程度  1 严重 2 较差 3 轻微
    private Integer status;// 整改状态：当前issue状态 0没有问题 1已记录未分配 2已分配未整改 3已整改未审核 4已审核（消项） 5已取消 6=整改中（介乎于2和3之间）
    private String attachment_md5_list; // 产生Issue时的现场照片，多个用半角都好“,”分隔
    private String audio_md5_list;// 录音
    private Integer repairer_id;            // 整改人负责人user_id
    private String repairer_follower_ids;    // 整改人跟进人user_ids
    private Integer client_create_at;// 客户端记录时间

    private Integer last_assigner;// 最后指派人
    private Integer last_assigner_at;// 最后指派时间
    private Integer last_repairer;// 最后整改人
    private Integer last_repairer_at;// 最后整改时间
    private Integer destroy_user;// 最后消项人
    private Integer destroy_at;// 最后消项时间
    private Integer delete_user;// 删除人
    private Integer delete_time;// 删除时间

    private ApiHouseQmCheckTaskIssueDetail detail;// 其它数据

    private Integer update_at;// 更新时间
    private Integer delete_at; // 删除时间(0表示未删除)
}
