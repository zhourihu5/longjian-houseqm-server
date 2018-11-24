package com.longfor.longjian.houseqm.po;

import java.util.Date;
import javax.persistence.*;

@Table(name = "user_in_house_qm_check_task")
public class UserInHouseQmCheckTask {
    /**
     * 记录ID
     */
    @Id
    private Integer id;

    /**
     * 任务组ID
     */
    @Column(name = "squad_id")
    private Integer squadId;

    /**
     * 用户ID
     */
    @Column(name = "user_id")
    private Integer userId;

    /**
     * 角色类型（10=检查人 20=整改人）
     */
    @Column(name = "role_type")
    private Integer roleType;

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
     * 能否销项
     */
    @Column(name = "can_approve")
    private Integer canApprove;

    /**
     * 能否直接销项
     */
    @Column(name = "can_direct_approve")
    private Integer canDirectApprove;

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
     * 获取任务组ID
     *
     * @return squad_id - 任务组ID
     */
    public Integer getSquadId() {
        return squadId;
    }

    /**
     * 设置任务组ID
     *
     * @param squadId 任务组ID
     */
    public void setSquadId(Integer squadId) {
        this.squadId = squadId;
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
     * 获取角色类型（10=检查人 20=整改人）
     *
     * @return role_type - 角色类型（10=检查人 20=整改人）
     */
    public Integer getRoleType() {
        return roleType;
    }

    /**
     * 设置角色类型（10=检查人 20=整改人）
     *
     * @param roleType 角色类型（10=检查人 20=整改人）
     */
    public void setRoleType(Integer roleType) {
        this.roleType = roleType;
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
     * 获取能否销项
     *
     * @return can_approve - 能否销项
     */
    public Integer getCanApprove() {
        return canApprove;
    }

    /**
     * 设置能否销项
     *
     * @param canApprove 能否销项
     */
    public void setCanApprove(Integer canApprove) {
        this.canApprove = canApprove;
    }

    /**
     * 获取能否直接销项
     *
     * @return can_direct_approve - 能否直接销项
     */
    public Integer getCanDirectApprove() {
        return canDirectApprove;
    }

    /**
     * 设置能否直接销项
     *
     * @param canDirectApprove 能否直接销项
     */
    public void setCanDirectApprove(Integer canDirectApprove) {
        this.canDirectApprove = canDirectApprove;
    }
}