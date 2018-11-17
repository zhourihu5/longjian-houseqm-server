package com.longfor.longjian.houseqm.po;

import java.util.Date;
import javax.persistence.*;

@Table(name = "house_qm_check_task_issue_log")
public class HouseQmCheckTaskIssueLog {
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
     * 日志uuid
     */
    private String uuid;

    /**
     * 所属问题uuid（关联house_qm_check_task_issue表uuid字段）
     */
    @Column(name = "issue_uuid")
    private String issueUuid;

    /**
     * 日志提交人员ID
     */
    @Column(name = "sender_id")
    private Integer senderId;

    /**
     * 整改状态：当前issue状态 10 没有问题 20 已记录未分配 30 已分配未整改 40=整改中 50 已整改未审核 60已审核（消项） 70已取消
     */
    private Integer status;

    /**
     * 客户端记录时间
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
     * 问题描述
     */
    private String desc;

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
     * 备忘录录音md5，多个用半角逗号“,”分隔
     */
    @Column(name = "memo_audio_md5_list")
    private String memoAudioMd5List;

    /**
     * 其它数据
     */
    private String detail;

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
     * 获取日志uuid
     *
     * @return uuid - 日志uuid
     */
    public String getUuid() {
        return uuid;
    }

    /**
     * 设置日志uuid
     *
     * @param uuid 日志uuid
     */
    public void setUuid(String uuid) {
        this.uuid = uuid == null ? null : uuid.trim();
    }

    /**
     * 获取所属问题uuid（关联house_qm_check_task_issue表uuid字段）
     *
     * @return issue_uuid - 所属问题uuid（关联house_qm_check_task_issue表uuid字段）
     */
    public String getIssueUuid() {
        return issueUuid;
    }

    /**
     * 设置所属问题uuid（关联house_qm_check_task_issue表uuid字段）
     *
     * @param issueUuid 所属问题uuid（关联house_qm_check_task_issue表uuid字段）
     */
    public void setIssueUuid(String issueUuid) {
        this.issueUuid = issueUuid == null ? null : issueUuid.trim();
    }

    /**
     * 获取日志提交人员ID
     *
     * @return sender_id - 日志提交人员ID
     */
    public Integer getSenderId() {
        return senderId;
    }

    /**
     * 设置日志提交人员ID
     *
     * @param senderId 日志提交人员ID
     */
    public void setSenderId(Integer senderId) {
        this.senderId = senderId;
    }

    /**
     * 获取整改状态：当前issue状态 10 没有问题 20 已记录未分配 30 已分配未整改 40=整改中 50 已整改未审核 60已审核（消项） 70已取消
     *
     * @return status - 整改状态：当前issue状态 10 没有问题 20 已记录未分配 30 已分配未整改 40=整改中 50 已整改未审核 60已审核（消项） 70已取消
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置整改状态：当前issue状态 10 没有问题 20 已记录未分配 30 已分配未整改 40=整改中 50 已整改未审核 60已审核（消项） 70已取消
     *
     * @param status 整改状态：当前issue状态 10 没有问题 20 已记录未分配 30 已分配未整改 40=整改中 50 已整改未审核 60已审核（消项） 70已取消
     */
    public void setStatus(Integer status) {
        this.status = status;
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
     * 获取问题描述
     *
     * @return desc - 问题描述
     */
    public String getDesc() {
        return desc;
    }

    /**
     * 设置问题描述
     *
     * @param desc 问题描述
     */
    public void setDesc(String desc) {
        this.desc = desc == null ? null : desc.trim();
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
     * 获取备忘录录音md5，多个用半角逗号“,”分隔
     *
     * @return memo_audio_md5_list - 备忘录录音md5，多个用半角逗号“,”分隔
     */
    public String getMemoAudioMd5List() {
        return memoAudioMd5List;
    }

    /**
     * 设置备忘录录音md5，多个用半角逗号“,”分隔
     *
     * @param memoAudioMd5List 备忘录录音md5，多个用半角逗号“,”分隔
     */
    public void setMemoAudioMd5List(String memoAudioMd5List) {
        this.memoAudioMd5List = memoAudioMd5List == null ? null : memoAudioMd5List.trim();
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