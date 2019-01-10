package com.longfor.longjian.houseqm.app.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author Houyan
 * @date 2018/12/22 0022 16:16
import java.util.List;

/**
 * Created by Dongshun on 2018/12/22.
 */
@Data
@NoArgsConstructor
public class HouseqmStatisticCategoryIssueListRspMsgVo implements Serializable {
    List<ApiTaskIssueRepairListRsp> issue_list; // issue_list
   private Integer total;  // 总数
    @Data
    @NoArgsConstructor
    public class  ApiTaskIssueRepairListRsp   implements Serializable{
        private   Integer id; // ID
        private  Integer project_id; // 项目ID
        private  Integer task_id; // 任务ID
        private  String uuid; // 问题Uuid
        private   List<String> area_path_name;// 区域完整路径名称
        private   List<String> category_path_name;// 任务类型路径名称
        private    List<String> check_item_path_name;// 检查项路径名称
        private  Integer plan_end_on;  // 计划整改完成时间
        private  String title;  // 问题标题
        private Integer typ;  // 0 普通记录；1 问题记录
        private String content; // 问题描述
        private Integer condition;  // 问题严重程度  1 严重 2 较差 3 轻微
        private  Integer status;   // 整改状态：当前issue状态 0没有问题 1已记录未分配 2已分配未整改 3已整改未审核 4已审核（消项） 5已取消 6=整改中（介乎于2和3之间）
        private  List<String> attachment_url_list;// 产生Issue时的现场照片
        private  String attachment_md5_list ;// 产生Issue时的现场照片，多个用半角逗号“,”分隔
        private Integer client_create_at; // 客户端记录时间
        private Integer update_at;// 更新时间
    }

}
