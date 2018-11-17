package com.longfor.longjian.houseqm.po;

import java.util.Date;
import javax.persistence.*;

@Table(name = "house_qm_check_task_notify_record")
public class HouseQmCheckTaskNotifyRecord {
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
     * 问题来源用户ID
     */
    @Column(name = "src_user_id")
    private Integer srcUserId;

    /**
     * 问题目标用户ID列表
     */
    @Column(name = "des_user_ids")
    private String desUserIds;

    /**
     * 模块ID（1001=工程检查, 1002=移动验房, 1003=工序管理, 1004=实测实量, 1005=巡检, 1006=进度, 1007=分户验收, 1008=入伙验房, 1009=承接查验, 1010=工地开放）
     */
    @Column(name = "module_id")
    private Integer moduleId;

    /**
     * 问题ID（关联house_qm_check_task_issue表id字段）
     */
    @Column(name = "issue_id")
    private Integer issueId;

    /**
     * 问题状态（10=无问题, 20=待分配, 30=待整改, 50=待销项, 60=已销项, 70=已取消）
     */
    @Column(name = "issue_status")
    private Integer issueStatus;

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
     * 获取问题来源用户ID
     *
     * @return src_user_id - 问题来源用户ID
     */
    public Integer getSrcUserId() {
        return srcUserId;
    }

    /**
     * 设置问题来源用户ID
     *
     * @param srcUserId 问题来源用户ID
     */
    public void setSrcUserId(Integer srcUserId) {
        this.srcUserId = srcUserId;
    }

    /**
     * 获取问题目标用户ID列表
     *
     * @return des_user_ids - 问题目标用户ID列表
     */
    public String getDesUserIds() {
        return desUserIds;
    }

    /**
     * 设置问题目标用户ID列表
     *
     * @param desUserIds 问题目标用户ID列表
     */
    public void setDesUserIds(String desUserIds) {
        this.desUserIds = desUserIds == null ? null : desUserIds.trim();
    }

    /**
     * 获取模块ID（1001=工程检查, 1002=移动验房, 1003=工序管理, 1004=实测实量, 1005=巡检, 1006=进度, 1007=分户验收, 1008=入伙验房, 1009=承接查验, 1010=工地开放）
     *
     * @return module_id - 模块ID（1001=工程检查, 1002=移动验房, 1003=工序管理, 1004=实测实量, 1005=巡检, 1006=进度, 1007=分户验收, 1008=入伙验房, 1009=承接查验, 1010=工地开放）
     */
    public Integer getModuleId() {
        return moduleId;
    }

    /**
     * 设置模块ID（1001=工程检查, 1002=移动验房, 1003=工序管理, 1004=实测实量, 1005=巡检, 1006=进度, 1007=分户验收, 1008=入伙验房, 1009=承接查验, 1010=工地开放）
     *
     * @param moduleId 模块ID（1001=工程检查, 1002=移动验房, 1003=工序管理, 1004=实测实量, 1005=巡检, 1006=进度, 1007=分户验收, 1008=入伙验房, 1009=承接查验, 1010=工地开放）
     */
    public void setModuleId(Integer moduleId) {
        this.moduleId = moduleId;
    }

    /**
     * 获取问题ID（关联house_qm_check_task_issue表id字段）
     *
     * @return issue_id - 问题ID（关联house_qm_check_task_issue表id字段）
     */
    public Integer getIssueId() {
        return issueId;
    }

    /**
     * 设置问题ID（关联house_qm_check_task_issue表id字段）
     *
     * @param issueId 问题ID（关联house_qm_check_task_issue表id字段）
     */
    public void setIssueId(Integer issueId) {
        this.issueId = issueId;
    }

    /**
     * 获取问题状态（10=无问题, 20=待分配, 30=待整改, 50=待销项, 60=已销项, 70=已取消）
     *
     * @return issue_status - 问题状态（10=无问题, 20=待分配, 30=待整改, 50=待销项, 60=已销项, 70=已取消）
     */
    public Integer getIssueStatus() {
        return issueStatus;
    }

    /**
     * 设置问题状态（10=无问题, 20=待分配, 30=待整改, 50=待销项, 60=已销项, 70=已取消）
     *
     * @param issueStatus 问题状态（10=无问题, 20=待分配, 30=待整改, 50=待销项, 60=已销项, 70=已取消）
     */
    public void setIssueStatus(Integer issueStatus) {
        this.issueStatus = issueStatus;
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