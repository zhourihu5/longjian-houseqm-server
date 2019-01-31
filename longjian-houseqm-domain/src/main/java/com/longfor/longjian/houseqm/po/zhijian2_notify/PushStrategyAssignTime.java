package com.longfor.longjian.houseqm.po.zhijian2_notify;

import java.util.Date;
import javax.persistence.*;

@Table(name = "push_strategy_assign_time")
public class PushStrategyAssignTime {
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
     * 模块ID
     */
    @Column(name = "module_id")
    private Integer moduleId;

    /**
     * 策略类型
     */
    private Integer typ;

    /**
     * 指定推送时间
     */
    @Column(name = "push_time")
    private Date pushTime;

    /**
     * 推送用户列表
     */
    @Column(name = "user_ids")
    private String userIds;

    /**
     * 扩展信息
     */
    @Column(name = "extra_info")
    private String extraInfo;

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
     * 获取模块ID
     *
     * @return module_id - 模块ID
     */
    public Integer getModuleId() {
        return moduleId;
    }

    /**
     * 设置模块ID
     *
     * @param moduleId 模块ID
     */
    public void setModuleId(Integer moduleId) {
        this.moduleId = moduleId;
    }

    /**
     * 获取策略类型
     *
     * @return typ - 策略类型
     */
    public Integer getTyp() {
        return typ;
    }

    /**
     * 设置策略类型
     *
     * @param typ 策略类型
     */
    public void setTyp(Integer typ) {
        this.typ = typ;
    }

    /**
     * 获取指定推送时间
     *
     * @return push_time - 指定推送时间
     */
    public Date getPushTime() {
        return pushTime;
    }

    /**
     * 设置指定推送时间
     *
     * @param pushTime 指定推送时间
     */
    public void setPushTime(Date pushTime) {
        this.pushTime = pushTime;
    }

    /**
     * 获取推送用户列表
     *
     * @return user_ids - 推送用户列表
     */
    public String getUserIds() {
        return userIds;
    }

    /**
     * 设置推送用户列表
     *
     * @param userIds 推送用户列表
     */
    public void setUserIds(String userIds) {
        this.userIds = userIds == null ? null : userIds.trim();
    }

    /**
     * 获取扩展信息
     *
     * @return extra_info - 扩展信息
     */
    public String getExtraInfo() {
        return extraInfo;
    }

    /**
     * 设置扩展信息
     *
     * @param extraInfo 扩展信息
     */
    public void setExtraInfo(String extraInfo) {
        this.extraInfo = extraInfo == null ? null : extraInfo.trim();
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