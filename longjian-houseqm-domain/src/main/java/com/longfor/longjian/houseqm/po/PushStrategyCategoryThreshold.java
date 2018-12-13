package com.longfor.longjian.houseqm.po;

import java.util.Date;
import javax.persistence.*;

@Table(name = "push_strategy_category_threshold")
public class PushStrategyCategoryThreshold {
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
     * 检查项列表
     */
    @Column(name = "category_keys")
    private String categoryKeys;

    /**
     * 告警阈值
     */
    private Integer threshold;

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
     * 策略有效期
     */
    @Column(name = "scan_end_on")
    private Date scanEndOn;

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
     * 扫描统计信息
     */
    @Column(name = "stat_info")
    private String statInfo;

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
     * 获取检查项列表
     *
     * @return category_keys - 检查项列表
     */
    public String getCategoryKeys() {
        return categoryKeys;
    }

    /**
     * 设置检查项列表
     *
     * @param categoryKeys 检查项列表
     */
    public void setCategoryKeys(String categoryKeys) {
        this.categoryKeys = categoryKeys == null ? null : categoryKeys.trim();
    }

    /**
     * 获取告警阈值
     *
     * @return threshold - 告警阈值
     */
    public Integer getThreshold() {
        return threshold;
    }

    /**
     * 设置告警阈值
     *
     * @param threshold 告警阈值
     */
    public void setThreshold(Integer threshold) {
        this.threshold = threshold;
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
     * 获取策略有效期
     *
     * @return scan_end_on - 策略有效期
     */
    public Date getScanEndOn() {
        return scanEndOn;
    }

    /**
     * 设置策略有效期
     *
     * @param scanEndOn 策略有效期
     */
    public void setScanEndOn(Date scanEndOn) {
        this.scanEndOn = scanEndOn;
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
     * 获取扫描统计信息
     *
     * @return stat_info - 扫描统计信息
     */
    public String getStatInfo() {
        return statInfo;
    }

    /**
     * 设置扫描统计信息
     *
     * @param statInfo 扫描统计信息
     */
    public void setStatInfo(String statInfo) {
        this.statInfo = statInfo == null ? null : statInfo.trim();
    }
}