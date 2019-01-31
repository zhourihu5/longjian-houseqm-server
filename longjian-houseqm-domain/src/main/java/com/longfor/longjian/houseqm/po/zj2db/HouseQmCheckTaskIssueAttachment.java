package com.longfor.longjian.houseqm.po.zj2db;

import java.util.Date;
import javax.persistence.*;

@Table(name = "house_qm_check_task_issue_attachment")
public class HouseQmCheckTaskIssueAttachment {
    /**
     * 记录ID
     */
    @Id
    private Integer id;

    /**
     * 项目ID
     */
    @Column(name = "project_id")
    private Integer projectId;

    /**
     * 任务ID
     */
    @Column(name = "task_id")
    private Integer taskId;

    /**
     * 问题uuid（关联house_qm_check_task_issue表uuid字段）
     */
    @Column(name = "issue_uuid")
    private String issueUuid;

    /**
     * 提交用户ID
     */
    @Column(name = "user_id")
    private Integer userId;

    /**
     * 公开类型 10=公有 20=私有
     */
    @Column(name = "public_type")
    private Integer publicType;

    /**
     * 附件类型 10=音频
     */
    @Column(name = "attachment_type")
    private Integer attachmentType;

    /**
     * 附件md5
     */
    private String md5;

    /**
     * 状态 1=可用 2=删除
     */
    private Integer status;

    /**
     * 客户端创建时间
     */
    @Column(name = "client_create_at")
    private Date clientCreateAt;

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
     * 获取记录ID
     *
     * @return id - 记录ID
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置记录ID
     *
     * @param id 记录ID
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取项目ID
     *
     * @return project_id - 项目ID
     */
    public Integer getProjectId() {
        return projectId;
    }

    /**
     * 设置项目ID
     *
     * @param projectId 项目ID
     */
    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    /**
     * 获取任务ID
     *
     * @return task_id - 任务ID
     */
    public Integer getTaskId() {
        return taskId;
    }

    /**
     * 设置任务ID
     *
     * @param taskId 任务ID
     */
    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    /**
     * 获取问题uuid（关联house_qm_check_task_issue表uuid字段）
     *
     * @return issue_uuid - 问题uuid（关联house_qm_check_task_issue表uuid字段）
     */
    public String getIssueUuid() {
        return issueUuid;
    }

    /**
     * 设置问题uuid（关联house_qm_check_task_issue表uuid字段）
     *
     * @param issueUuid 问题uuid（关联house_qm_check_task_issue表uuid字段）
     */
    public void setIssueUuid(String issueUuid) {
        this.issueUuid = issueUuid == null ? null : issueUuid.trim();
    }

    /**
     * 获取提交用户ID
     *
     * @return user_id - 提交用户ID
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * 设置提交用户ID
     *
     * @param userId 提交用户ID
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * 获取公开类型 10=公有 20=私有
     *
     * @return public_type - 公开类型 10=公有 20=私有
     */
    public Integer getPublicType() {
        return publicType;
    }

    /**
     * 设置公开类型 10=公有 20=私有
     *
     * @param publicType 公开类型 10=公有 20=私有
     */
    public void setPublicType(Integer publicType) {
        this.publicType = publicType;
    }

    /**
     * 获取附件类型 10=音频
     *
     * @return attachment_type - 附件类型 10=音频
     */
    public Integer getAttachmentType() {
        return attachmentType;
    }

    /**
     * 设置附件类型 10=音频
     *
     * @param attachmentType 附件类型 10=音频
     */
    public void setAttachmentType(Integer attachmentType) {
        this.attachmentType = attachmentType;
    }

    /**
     * 获取附件md5
     *
     * @return md5 - 附件md5
     */
    public String getMd5() {
        return md5;
    }

    /**
     * 设置附件md5
     *
     * @param md5 附件md5
     */
    public void setMd5(String md5) {
        this.md5 = md5 == null ? null : md5.trim();
    }

    /**
     * 获取状态 1=可用 2=删除
     *
     * @return status - 状态 1=可用 2=删除
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置状态 1=可用 2=删除
     *
     * @param status 状态 1=可用 2=删除
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 获取客户端创建时间
     *
     * @return client_create_at - 客户端创建时间
     */
    public Date getClientCreateAt() {
        return clientCreateAt;
    }

    /**
     * 设置客户端创建时间
     *
     * @param clientCreateAt 客户端创建时间
     */
    public void setClientCreateAt(Date clientCreateAt) {
        this.clientCreateAt = clientCreateAt;
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
}