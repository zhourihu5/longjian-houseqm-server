package com.longfor.longjian.houseqm.po.zj2db;

import java.util.Date;
import javax.persistence.*;

@Table(name = "house_qm_check_task_notify_record")
public class HouseQmCheckTaskNotifyRecord {
    @Id
    private Integer id;

    @Column(name = "project_id")
    private Integer projectId;

    @Column(name = "task_id")
    private Integer taskId;

    @Column(name = "src_user_id")
    private Integer srcUserId;

    @Column(name = "des_user_ids")
    private String desUserIds;

    @Column(name = "module_id")
    private Integer moduleId;

    @Column(name = "issue_id")
    private Integer issueId;

    @Column(name = "issue_status")
    private Integer issueStatus;

    @Column(name = "extra_info")
    private String extraInfo;

    @Column(name = "create_at")
    private Date createAt;

    @Column(name = "update_at")
    private Date updateAt;

    @Column(name = "delete_at")
    private Date deleteAt;

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return project_id
     */
    public Integer getProjectId() {
        return projectId;
    }

    /**
     * @param projectId
     */
    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    /**
     * @return task_id
     */
    public Integer getTaskId() {
        return taskId;
    }

    /**
     * @param taskId
     */
    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    /**
     * @return src_user_id
     */
    public Integer getSrcUserId() {
        return srcUserId;
    }

    /**
     * @param srcUserId
     */
    public void setSrcUserId(Integer srcUserId) {
        this.srcUserId = srcUserId;
    }

    /**
     * @return des_user_ids
     */
    public String getDesUserIds() {
        return desUserIds;
    }

    /**
     * @param desUserIds
     */
    public void setDesUserIds(String desUserIds) {
        this.desUserIds = desUserIds == null ? null : desUserIds.trim();
    }

    /**
     * @return module_id
     */
    public Integer getModuleId() {
        return moduleId;
    }

    /**
     * @param moduleId
     */
    public void setModuleId(Integer moduleId) {
        this.moduleId = moduleId;
    }

    /**
     * @return issue_id
     */
    public Integer getIssueId() {
        return issueId;
    }

    /**
     * @param issueId
     */
    public void setIssueId(Integer issueId) {
        this.issueId = issueId;
    }

    /**
     * @return issue_status
     */
    public Integer getIssueStatus() {
        return issueStatus;
    }

    /**
     * @param issueStatus
     */
    public void setIssueStatus(Integer issueStatus) {
        this.issueStatus = issueStatus;
    }

    /**
     * @return extra_info
     */
    public String getExtraInfo() {
        return extraInfo;
    }

    /**
     * @param extraInfo
     */
    public void setExtraInfo(String extraInfo) {
        this.extraInfo = extraInfo == null ? null : extraInfo.trim();
    }

    /**
     * @return create_at
     */
    public Date getCreateAt() {
        return createAt;
    }

    /**
     * @param createAt
     */
    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    /**
     * @return update_at
     */
    public Date getUpdateAt() {
        return updateAt;
    }

    /**
     * @param updateAt
     */
    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }

    /**
     * @return delete_at
     */
    public Date getDeleteAt() {
        return deleteAt;
    }

    /**
     * @param deleteAt
     */
    public void setDeleteAt(Date deleteAt) {
        this.deleteAt = deleteAt;
    }
}