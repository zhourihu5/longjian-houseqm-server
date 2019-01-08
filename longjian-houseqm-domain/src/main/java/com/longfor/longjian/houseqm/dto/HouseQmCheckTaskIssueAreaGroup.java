package com.longfor.longjian.houseqm.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.dto
 * @ClassName: HouseQmCheckTaskIssueAreaGroup
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/1/8 17:44
 */
@Data
@NoArgsConstructor
public class HouseQmCheckTaskIssueAreaGroup {

    private String AreaPath;
    private Integer ExtendCol;

    /**
     * 记录ID（也用作问题ID）
     */
    private Integer id;

    /**
     * 项目ID（关联project表id字段）
     */
    private Integer projectId;

    /**
     * 任务ID（关联task表id字段
     */
    private Integer taskId;

    /**
     * 问题uuid
     */
    private String uuid;

    /**
     * 提交人ID
     */
    private Integer senderId;

    /**
     * 计划整改完成时间
     */
    private Date planEndOn;

    /**
     * 实际整改完成时间
     */
    private Date endOn;
    private int count;

    /**
     * 区域ID
     */
    private Integer areaId;

    /**
     * 区域路径和id
     */
    private String areaPathAndId;

    /**
     * 模块类型（23=日常检查, 24=月度检查, 25=季度检查, 26=入伙验房, 27=专项检查, 28=分户验收, 29=安全检查, 30=承接查验, 31=工地开放）
     */
    private Integer categoryCls;

    /**
     * 任务类型key
     */
    private String categoryKey;

    /**
     * 任务类型路径和Key
     */
    private String categoryPathAndKey;

    /**
     * 检查项key
     */
    private String checkItemKey;

    /**
     * 检查项路径和key
     */
    private String checkItemPathAndKey;

    /**
     * 图纸文件MD5
     */
    private String drawingMD5;

    /**
     * 在图纸上的位置X
     */
    private Integer posX;

    /**
     * 在图纸上的位置Y
     */
    private Integer posY;

    /**
     * 问题标题
     */
    private String title;

    /**
     * 99 普通记录；1 问题记录
     */
    private Integer typ;

    /**
     * 问题描述
     */
    private String content;

    /**
     * 问题严重程度  1 严重 2 较差 3 轻微
     */
    private Integer condition;

    /**
     * 整改状态：当前issue状态 10 没有问题 20 已记录未分配 30 已分配未整改 50 已整改未审核 60 已审核（消项） 70已取消 6=整改中（介乎于2和3之间）
     */
    private Integer status;

    /**
     * 整改负责人
     */
    private Integer repairerId;

    /**
     * 整改参与人多个用半角逗号“,”分隔
     */
    private String repairerFollowerIds;

    /**
     * 客户端记录时间
     */
    private Date clientCreateAt;

    /**
     * 最后指派人
     */
    private Integer lastAssigner;

    /**
     * 最后指派时间
     */
    private Date lastAssignAt;

    /**
     * 最后整改人
     */
    private Integer lastRepairer;

    /**
     * 最后整改时间
     */
    private Date lastRepairerAt;

    /**
     * 最后消项人
     */
    private Integer destroyUser;

    /**
     * 最后消项时间
     */
    private Date destroyAt;

    /**
     * 删除人
     */
    private Integer deleteUser;

    /**
     * 删除时间
     */
    private Date deleteTime;

    /**
     * 创建时间
     */
    private Date createAt;

    /**
     * 更新时间
     */
    private Date updateAt;

    /**
     * 删除时间
     */
    private Date deleteAt;

    /**
     * 产生Issue时的现场照片，多个用半角逗号“,”分隔
     */
    private String attachmentMd5List;

    /**
     * 产生Issue时的现场录音，多个用半角逗号“,”分隔
     */
    private String audioMd5List;

    /**
     * 其它数据
     */
    private String detail;

}
