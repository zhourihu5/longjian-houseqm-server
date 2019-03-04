package com.longfor.longjian.houseqm.app.vo;

import com.google.common.collect.Lists;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author lipeishuai
 * @date 2018/11/17 15:54
 */
@Data
@NoArgsConstructor
public class IssueListVo implements Serializable {

    private Integer id = 0;//id
    private Integer project_id = 0;//项目名
    private Integer task_id = 0;//任务id
    private String uuid = "";//问题Uuid
    private Integer sender_id = 0;//提交人user_id
    private Integer plan_end_on = 0;//计划整改完成时间
    private Integer end_on = 0;//实际整改完成时间
    private Integer area_id = 0;//区域ID
    private String area_path_and_id = "";//区域路径和id
    private Integer category_cls = 0;//模块类型
    private String category_key = "";//任务类型key
    private String category_path_and_key = "";//任务类型路径和Key
    private String category_name = "";//任务类型名称
    private String check_item_key = "";//检查项key
    private String check_item_path_and_key = "";//检查项路径和key
    private String check_item_name = "";//检查项名字
    private String drawing_md5 = "";//图纸文件MD5
    private Integer pos_x = 0;//在图纸上的位置X
    private Integer pos_y = 0;//在图纸上的位置Y
    private String title = "";//问题标题
    private Integer typ = 0;//0 普通记录；1 问题记录
    private String content = "";//问题描述
    private Integer condition = 0;//问题严重程度  1 严重 2 较差 3 轻微
    private Integer status = 0;//整改状态：当前issue状态 0没有问题 1已记录未分配 2已分配未整改 3已整改未审核 4已审核（消项） 5已取消 6=整改中（介乎于2和3之间）
    private String attachment_md5_list = "";//产生Issue时的现场照片,多个用半角都好“,”分隔
    private Integer client_create_at = 0;//客户端记录时间
    private Integer last_assigner = 0;//最后指派人
    private Integer last_assigner_at = 0;//最后指派时间
    private Integer last_repairer = 0;//最后整改人
    private Integer last_repairer_at = 0;//最后整改时间
    private Integer destroy_user = 0;//最后消项人
    private Integer destroy_at = 0;//最后消项时间
    private Integer delete_user = 0;//删除人
    private Integer delete_time = 0;//删除时间
    private DetailVo detail;//其它数据
    private List<String> pictures = Lists.newArrayList();//issue图片列表
    private String area_path_name = "";//区域名称
    private String check_item_name_path = "";//检查项名称
    private String last_repairer_name = "";//最后整改人姓名


}
