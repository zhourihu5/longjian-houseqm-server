package com.longfor.longjian.houseqm.po.zj2db;

import java.util.Date;
import javax.persistence.*;

@Table(name = "house_qm_check_task_issue_user")
public class HouseQmCheckTaskIssueUser {
    /**
     * 记录ID
     */
    @Id
    private Integer id;

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
     * 用户ID
     */
    @Column(name = "user_id")
    private Integer userId;

    /**
     * 用户角色类型（10=检查人 20=整改负责人 30=整改跟进人）
     */
    @Column(name = "role_type")
    private Integer roleType;

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
     * 获取用户ID
     *
     * @return user_id - 用户ID
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * 设置用户ID
     *
     * @param userId 用户ID
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * 获取用户角色类型（10=检查人 20=整改负责人 30=整改跟进人）
     *
     * @return role_type - 用户角色类型（10=检查人 20=整改负责人 30=整改跟进人）
     */
    public Integer getRoleType() {
        return roleType;
    }

    /**
     * 设置用户角色类型（10=检查人 20=整改负责人 30=整改跟进人）
     *
     * @param roleType 用户角色类型（10=检查人 20=整改负责人 30=整改跟进人）
     */
    public void setRoleType(Integer roleType) {
        this.roleType = roleType;
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