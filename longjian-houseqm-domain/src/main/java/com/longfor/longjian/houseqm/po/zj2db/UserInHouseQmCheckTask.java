package com.longfor.longjian.houseqm.po.zj2db;

import java.util.Date;
import javax.persistence.*;

@Table(name = "user_in_house_qm_check_task")
public class UserInHouseQmCheckTask {
    @Id
    private Integer id;

    @Column(name = "squad_id")
    private Integer squadId;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "role_type")
    private Integer roleType;

    @Column(name = "project_id")
    private Integer projectId;

    @Column(name = "task_id")
    private Integer taskId;

    @Column(name = "create_at")
    private Date createAt;

    @Column(name = "update_at")
    private Date updateAt;

    @Column(name = "delete_at")
    private Date deleteAt;

    @Column(name = "can_approve")
    private Integer canApprove;

    @Column(name = "can_direct_approve")
    private Integer canDirectApprove;

    /**
     * 能否修改检查项
     */
    @Column(name = "can_reassign")
    private Integer canReassign;

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
     * @return squad_id
     */
    public Integer getSquadId() {
        return squadId;
    }

    /**
     * @param squadId
     */
    public void setSquadId(Integer squadId) {
        this.squadId = squadId;
    }

    /**
     * @return user_id
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * @param userId
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * @return role_type
     */
    public Integer getRoleType() {
        return roleType;
    }

    /**
     * @param roleType
     */
    public void setRoleType(Integer roleType) {
        this.roleType = roleType;
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

    /**
     * @return can_approve
     */
    public Integer getCanApprove() {
        return canApprove;
    }

    /**
     * @param canApprove
     */
    public void setCanApprove(Integer canApprove) {
        this.canApprove = canApprove;
    }

    /**
     * @return can_direct_approve
     */
    public Integer getCanDirectApprove() {
        return canDirectApprove;
    }

    /**
     * @param canDirectApprove
     */
    public void setCanDirectApprove(Integer canDirectApprove) {
        this.canDirectApprove = canDirectApprove;
    }

    /**
     * 获取能否修改检查项
     *
     * @return can_reassign - 能否修改检查项
     */
    public Integer getCanReassign() {
        return canReassign;
    }

    /**
     * 设置能否修改检查项
     *
     * @param canReassign 能否修改检查项
     */
    public void setCanReassign(Integer canReassign) {
        this.canReassign = canReassign;
    }
}