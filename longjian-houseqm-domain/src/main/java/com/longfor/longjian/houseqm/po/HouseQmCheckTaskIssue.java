package com.longfor.longjian.houseqm.po;

import java.util.Date;
import javax.persistence.*;

@Table(name = "house_qm_check_task_issue")
public class HouseQmCheckTaskIssue {
    /**
     * 记录ID（也用作问题ID）
     */
    @Id
    private Integer id;

    /**
     * 项目ID（关联project表id字段）
     */
    @Column(name = "project_id")
    private Integer projectId;

    /**
     * 任务ID（关联task表id字段
     */
    @Column(name = "task_id")
    private Integer taskId;

    /**
     * 问题uuid
     */
    private String uuid;

    /**
     * 提交人ID
     */
    @Column(name = "sender_id")
    private Integer senderId;

    /**
     * 计划整改完成时间
     */
    @Column(name = "plan_end_on")
    private Date planEndOn;

    /**
     * 实际整改完成时间
     */
    @Column(name = "end_on")
    private Date endOn;
    private int count;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    /**
     * 区域ID
     */
    @Column(name = "area_id")
    private Integer areaId;

    /**
     * 区域路径和id
     */
    @Column(name = "area_path_and_id")
    private String areaPathAndId;

    /**
     * 模块类型（23=日常检查, 24=月度检查, 25=季度检查, 26=入伙验房, 27=专项检查, 28=分户验收, 29=安全检查, 30=承接查验, 31=工地开放）
     */
    @Column(name = "category_cls")
    private Integer categoryCls;

    /**
     * 任务类型key
     */
    @Column(name = "category_key")
    private String categoryKey;

    /**
     * 任务类型路径和Key
     */
    @Column(name = "category_path_and_key")
    private String categoryPathAndKey;

    /**
     * 检查项key
     */
    @Column(name = "check_item_key")
    private String checkItemKey;

    /**
     * 检查项路径和key
     */
    @Column(name = "check_item_path_and_key")
    private String checkItemPathAndKey;

    /**
     * 图纸文件MD5
     */
    @Column(name = "drawing_m_d5")
    private String drawingMD5;

    /**
     * 在图纸上的位置X
     */
    @Column(name = "pos_x")
    private Integer posX;

    /**
     * 在图纸上的位置Y
     */
    @Column(name = "pos_y")
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
    @Column(name = "`condition`")
    private Integer condition;

    /**
     * 整改状态：当前issue状态 10 没有问题 20 已记录未分配 30 已分配未整改 50 已整改未审核 60 已审核（消项） 70已取消 6=整改中（介乎于2和3之间）
     */
    @Column(name = "`status`")
    private Integer status;

    /**
     * 整改负责人
     */
    @Column(name = "repairer_id")
    private Integer repairerId;

    /**
     * 整改参与人多个用半角逗号“,”分隔
     */
    @Column(name = "repairer_follower_ids")
    private String repairerFollowerIds;

    /**
     * 客户端记录时间
     */
    @Column(name = "client_create_at")
    private Date clientCreateAt;

    /**
     * 最后指派人
     */
    @Column(name = "last_assigner")
    private Integer lastAssigner;

    /**
     * 最后指派时间
     */
    @Column(name = "last_assign_at")
    private Date lastAssignAt;

    /**
     * 最后整改人
     */
    @Column(name = "last_repairer")
    private Integer lastRepairer;

    /**
     * 最后整改时间
     */
    @Column(name = "last_repairer_at")
    private Date lastRepairerAt;

    /**
     * 最后消项人
     */
    @Column(name = "destroy_user")
    private Integer destroyUser;

    /**
     * 最后消项时间
     */
    @Column(name = "destroy_at")
    private Date destroyAt;

    /**
     * 删除人
     */
    @Column(name = "delete_user")
    private Integer deleteUser;

    /**
     * 删除时间
     */
    @Column(name = "delete_time")
    private Date deleteTime;

    /**
     * 创建时间
     */
    @Column(name = "create_at")
    private Date createAt;

    /**
     * 更新时间
     */
    @Column(name = "update_at")
    private Date updateAt;

    /**
     * 删除时间
     */
    @Column(name = "delete_at")
    private Date deleteAt;

    /**
     * 产生Issue时的现场照片，多个用半角逗号“,”分隔
     */
    @Column(name = "attachment_md5_list")
    private String attachmentMd5List;

    /**
     * 产生Issue时的现场录音，多个用半角逗号“,”分隔
     */
    @Column(name = "audio_md5_list")
    private String audioMd5List;

    /**
     * 其它数据
     */
    private String detail;

    /**
     * 获取记录ID（也用作问题ID）
     *
     * @return id - 记录ID（也用作问题ID）
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置记录ID（也用作问题ID）
     *
     * @param id 记录ID（也用作问题ID）
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取项目ID（关联project表id字段）
     *
     * @return project_id - 项目ID（关联project表id字段）
     */
    public Integer getProjectId() {
        return projectId;
    }

    /**
     * 设置项目ID（关联project表id字段）
     *
     * @param projectId 项目ID（关联project表id字段）
     */
    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    /**
     * 获取任务ID（关联task表id字段
     *
     * @return task_id - 任务ID（关联task表id字段
     */
    public Integer getTaskId() {
        return taskId;
    }

    /**
     * 设置任务ID（关联task表id字段
     *
     * @param taskId 任务ID（关联task表id字段
     */
    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    /**
     * 获取问题uuid
     *
     * @return uuid - 问题uuid
     */
    public String getUuid() {
        return uuid;
    }

    /**
     * 设置问题uuid
     *
     * @param uuid 问题uuid
     */
    public void setUuid(String uuid) {
        this.uuid = uuid == null ? null : uuid.trim();
    }

    /**
     * 获取提交人ID
     *
     * @return sender_id - 提交人ID
     */
    public Integer getSenderId() {
        return senderId;
    }

    /**
     * 设置提交人ID
     *
     * @param senderId 提交人ID
     */
    public void setSenderId(Integer senderId) {
        this.senderId = senderId;
    }

    /**
     * 获取计划整改完成时间
     *
     * @return plan_end_on - 计划整改完成时间
     */
    public Date getPlanEndOn() {
        return planEndOn;
    }

    /**
     * 设置计划整改完成时间
     *
     * @param planEndOn 计划整改完成时间
     */
    public void setPlanEndOn(Date planEndOn) {
        this.planEndOn = planEndOn;
    }

    /**
     * 获取实际整改完成时间
     *
     * @return end_on - 实际整改完成时间
     */
    public Date getEndOn() {
        return endOn;
    }

    /**
     * 设置实际整改完成时间
     *
     * @param endOn 实际整改完成时间
     */
    public void setEndOn(Date endOn) {
        this.endOn = endOn;
    }

    /**
     * 获取区域ID
     *
     * @return area_id - 区域ID
     */
    public Integer getAreaId() {
        return areaId;
    }

    /**
     * 设置区域ID
     *
     * @param areaId 区域ID
     */
    public void setAreaId(Integer areaId) {
        this.areaId = areaId;
    }

    /**
     * 获取区域路径和id
     *
     * @return area_path_and_id - 区域路径和id
     */
    public String getAreaPathAndId() {
        return areaPathAndId;
    }

    /**
     * 设置区域路径和id
     *
     * @param areaPathAndId 区域路径和id
     */
    public void setAreaPathAndId(String areaPathAndId) {
        this.areaPathAndId = areaPathAndId == null ? null : areaPathAndId.trim();
    }

    /**
     * 获取模块类型（23=日常检查, 24=月度检查, 25=季度检查, 26=入伙验房, 27=专项检查, 28=分户验收, 29=安全检查, 30=承接查验, 31=工地开放）
     *
     * @return category_cls - 模块类型（23=日常检查, 24=月度检查, 25=季度检查, 26=入伙验房, 27=专项检查, 28=分户验收, 29=安全检查, 30=承接查验, 31=工地开放）
     */
    public Integer getCategoryCls() {
        return categoryCls;
    }

    /**
     * 设置模块类型（23=日常检查, 24=月度检查, 25=季度检查, 26=入伙验房, 27=专项检查, 28=分户验收, 29=安全检查, 30=承接查验, 31=工地开放）
     *
     * @param categoryCls 模块类型（23=日常检查, 24=月度检查, 25=季度检查, 26=入伙验房, 27=专项检查, 28=分户验收, 29=安全检查, 30=承接查验, 31=工地开放）
     */
    public void setCategoryCls(Integer categoryCls) {
        this.categoryCls = categoryCls;
    }

    /**
     * 获取任务类型key
     *
     * @return category_key - 任务类型key
     */
    public String getCategoryKey() {
        return categoryKey;
    }

    /**
     * 设置任务类型key
     *
     * @param categoryKey 任务类型key
     */
    public void setCategoryKey(String categoryKey) {
        this.categoryKey = categoryKey == null ? null : categoryKey.trim();
    }

    /**
     * 获取任务类型路径和Key
     *
     * @return category_path_and_key - 任务类型路径和Key
     */
    public String getCategoryPathAndKey() {
        return categoryPathAndKey;
    }

    /**
     * 设置任务类型路径和Key
     *
     * @param categoryPathAndKey 任务类型路径和Key
     */
    public void setCategoryPathAndKey(String categoryPathAndKey) {
        this.categoryPathAndKey = categoryPathAndKey == null ? null : categoryPathAndKey.trim();
    }

    /**
     * 获取检查项key
     *
     * @return check_item_key - 检查项key
     */
    public String getCheckItemKey() {
        return checkItemKey;
    }

    /**
     * 设置检查项key
     *
     * @param checkItemKey 检查项key
     */
    public void setCheckItemKey(String checkItemKey) {
        this.checkItemKey = checkItemKey == null ? null : checkItemKey.trim();
    }

    /**
     * 获取检查项路径和key
     *
     * @return check_item_path_and_key - 检查项路径和key
     */
    public String getCheckItemPathAndKey() {
        return checkItemPathAndKey;
    }

    /**
     * 设置检查项路径和key
     *
     * @param checkItemPathAndKey 检查项路径和key
     */
    public void setCheckItemPathAndKey(String checkItemPathAndKey) {
        this.checkItemPathAndKey = checkItemPathAndKey == null ? null : checkItemPathAndKey.trim();
    }

    /**
     * 获取图纸文件MD5
     *
     * @return drawing_m_d5 - 图纸文件MD5
     */
    public String getDrawingMD5() {
        return drawingMD5;
    }

    /**
     * 设置图纸文件MD5
     *
     * @param drawingMD5 图纸文件MD5
     */
    public void setDrawingMD5(String drawingMD5) {
        this.drawingMD5 = drawingMD5 == null ? null : drawingMD5.trim();
    }

    /**
     * 获取在图纸上的位置X
     *
     * @return pos_x - 在图纸上的位置X
     */
    public Integer getPosX() {
        return posX;
    }

    /**
     * 设置在图纸上的位置X
     *
     * @param posX 在图纸上的位置X
     */
    public void setPosX(Integer posX) {
        this.posX = posX;
    }

    /**
     * 获取在图纸上的位置Y
     *
     * @return pos_y - 在图纸上的位置Y
     */
    public Integer getPosY() {
        return posY;
    }

    /**
     * 设置在图纸上的位置Y
     *
     * @param posY 在图纸上的位置Y
     */
    public void setPosY(Integer posY) {
        this.posY = posY;
    }

    /**
     * 获取问题标题
     *
     * @return title - 问题标题
     */
    public String getTitle() {
        return title;
    }

    /**
     * 设置问题标题
     *
     * @param title 问题标题
     */
    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    /**
     * 获取99 普通记录；1 问题记录
     *
     * @return typ - 99 普通记录；1 问题记录
     */
    public Integer getTyp() {
        return typ;
    }

    /**
     * 设置99 普通记录；1 问题记录
     *
     * @param typ 99 普通记录；1 问题记录
     */
    public void setTyp(Integer typ) {
        this.typ = typ;
    }

    /**
     * 获取问题描述
     *
     * @return content - 问题描述
     */
    public String getContent() {
        return content;
    }

    /**
     * 设置问题描述
     *
     * @param content 问题描述
     */
    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    /**
     * 获取问题严重程度  1 严重 2 较差 3 轻微
     *
     * @return condition - 问题严重程度  1 严重 2 较差 3 轻微
     */
    public Integer getCondition() {
        return condition;
    }

    /**
     * 设置问题严重程度  1 严重 2 较差 3 轻微
     *
     * @param condition 问题严重程度  1 严重 2 较差 3 轻微
     */
    public void setCondition(Integer condition) {
        this.condition = condition;
    }

    /**
     * 获取整改状态：当前issue状态 10 没有问题 20 已记录未分配 30 已分配未整改 50 已整改未审核 60 已审核（消项） 70已取消 6=整改中（介乎于2和3之间）
     *
     * @return status - 整改状态：当前issue状态 10 没有问题 20 已记录未分配 30 已分配未整改 50 已整改未审核 60 已审核（消项） 70已取消 6=整改中（介乎于2和3之间）
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置整改状态：当前issue状态 10 没有问题 20 已记录未分配 30 已分配未整改 50 已整改未审核 60 已审核（消项） 70已取消 6=整改中（介乎于2和3之间）
     *
     * @param status 整改状态：当前issue状态 10 没有问题 20 已记录未分配 30 已分配未整改 50 已整改未审核 60 已审核（消项） 70已取消 6=整改中（介乎于2和3之间）
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 获取整改负责人
     *
     * @return repairer_id - 整改负责人
     */
    public Integer getRepairerId() {
        return repairerId;
    }

    /**
     * 设置整改负责人
     *
     * @param repairerId 整改负责人
     */
    public void setRepairerId(Integer repairerId) {
        this.repairerId = repairerId;
    }

    /**
     * 获取整改参与人多个用半角逗号“,”分隔
     *
     * @return repairer_follower_ids - 整改参与人多个用半角逗号“,”分隔
     */
    public String getRepairerFollowerIds() {
        return repairerFollowerIds;
    }

    /**
     * 设置整改参与人多个用半角逗号“,”分隔
     *
     * @param repairerFollowerIds 整改参与人多个用半角逗号“,”分隔
     */
    public void setRepairerFollowerIds(String repairerFollowerIds) {
        this.repairerFollowerIds = repairerFollowerIds == null ? null : repairerFollowerIds.trim();
    }

    /**
     * 获取客户端记录时间
     *
     * @return client_create_at - 客户端记录时间
     */
    public Date getClientCreateAt() {
        return clientCreateAt;
    }

    /**
     * 设置客户端记录时间
     *
     * @param clientCreateAt 客户端记录时间
     */
    public void setClientCreateAt(Date clientCreateAt) {
        this.clientCreateAt = clientCreateAt;
    }

    /**
     * 获取最后指派人
     *
     * @return last_assigner - 最后指派人
     */
    public Integer getLastAssigner() {
        return lastAssigner;
    }

    /**
     * 设置最后指派人
     *
     * @param lastAssigner 最后指派人
     */
    public void setLastAssigner(Integer lastAssigner) {
        this.lastAssigner = lastAssigner;
    }

    /**
     * 获取最后指派时间
     *
     * @return last_assign_at - 最后指派时间
     */
    public Date getLastAssignAt() {
        return lastAssignAt;
    }

    /**
     * 设置最后指派时间
     *
     * @param lastAssignAt 最后指派时间
     */
    public void setLastAssignAt(Date lastAssignAt) {
        this.lastAssignAt = lastAssignAt;
    }

    /**
     * 获取最后整改人
     *
     * @return last_repairer - 最后整改人
     */
    public Integer getLastRepairer() {
        return lastRepairer;
    }

    /**
     * 设置最后整改人
     *
     * @param lastRepairer 最后整改人
     */
    public void setLastRepairer(Integer lastRepairer) {
        this.lastRepairer = lastRepairer;
    }

    /**
     * 获取最后整改时间
     *
     * @return last_repairer_at - 最后整改时间
     */
    public Date getLastRepairerAt() {
        return lastRepairerAt;
    }

    /**
     * 设置最后整改时间
     *
     * @param lastRepairerAt 最后整改时间
     */
    public void setLastRepairerAt(Date lastRepairerAt) {
        this.lastRepairerAt = lastRepairerAt;
    }

    /**
     * 获取最后消项人
     *
     * @return destroy_user - 最后消项人
     */
    public Integer getDestroyUser() {
        return destroyUser;
    }

    /**
     * 设置最后消项人
     *
     * @param destroyUser 最后消项人
     */
    public void setDestroyUser(Integer destroyUser) {
        this.destroyUser = destroyUser;
    }

    /**
     * 获取最后消项时间
     *
     * @return destroy_at - 最后消项时间
     */
    public Date getDestroyAt() {
        return destroyAt;
    }

    /**
     * 设置最后消项时间
     *
     * @param destroyAt 最后消项时间
     */
    public void setDestroyAt(Date destroyAt) {
        this.destroyAt = destroyAt;
    }

    /**
     * 获取删除人
     *
     * @return delete_user - 删除人
     */
    public Integer getDeleteUser() {
        return deleteUser;
    }

    /**
     * 设置删除人
     *
     * @param deleteUser 删除人
     */
    public void setDeleteUser(Integer deleteUser) {
        this.deleteUser = deleteUser;
    }

    /**
     * 获取删除时间
     *
     * @return delete_time - 删除时间
     */
    public Date getDeleteTime() {
        return deleteTime;
    }

    /**
     * 设置删除时间
     *
     * @param deleteTime 删除时间
     */
    public void setDeleteTime(Date deleteTime) {
        this.deleteTime = deleteTime;
    }

    /**
     * 获取创建时间
     *
     * @return create_at - 创建时间
     */
    public Date getCreateAt() {
        return createAt;
    }

    /**
     * 设置创建时间
     *
     * @param createAt 创建时间
     */
    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    /**
     * 获取更新时间
     *
     * @return update_at - 更新时间
     */
    public Date getUpdateAt() {
        return updateAt;
    }

    /**
     * 设置更新时间
     *
     * @param updateAt 更新时间
     */
    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }

    /**
     * 获取删除时间
     *
     * @return delete_at - 删除时间
     */
    public Date getDeleteAt() {
        return deleteAt;
    }

    /**
     * 设置删除时间
     *
     * @param deleteAt 删除时间
     */
    public void setDeleteAt(Date deleteAt) {
        this.deleteAt = deleteAt;
    }

    /**
     * 获取产生Issue时的现场照片，多个用半角逗号“,”分隔
     *
     * @return attachment_md5_list - 产生Issue时的现场照片，多个用半角逗号“,”分隔
     */
    public String getAttachmentMd5List() {
        return attachmentMd5List;
    }

    /**
     * 设置产生Issue时的现场照片，多个用半角逗号“,”分隔
     *
     * @param attachmentMd5List 产生Issue时的现场照片，多个用半角逗号“,”分隔
     */
    public void setAttachmentMd5List(String attachmentMd5List) {
        this.attachmentMd5List = attachmentMd5List == null ? null : attachmentMd5List.trim();
    }

    /**
     * 获取产生Issue时的现场录音，多个用半角逗号“,”分隔
     *
     * @return audio_md5_list - 产生Issue时的现场录音，多个用半角逗号“,”分隔
     */
    public String getAudioMd5List() {
        return audioMd5List;
    }

    /**
     * 设置产生Issue时的现场录音，多个用半角逗号“,”分隔
     *
     * @param audioMd5List 产生Issue时的现场录音，多个用半角逗号“,”分隔
     */
    public void setAudioMd5List(String audioMd5List) {
        this.audioMd5List = audioMd5List == null ? null : audioMd5List.trim();
    }

    /**
     * 获取其它数据
     *
     * @return detail - 其它数据
     */
    public String getDetail() {
        return detail;
    }

    /**
     * 设置其它数据
     *
     * @param detail 其它数据
     */
    public void setDetail(String detail) {
        this.detail = detail == null ? null : detail.trim();
    }
}