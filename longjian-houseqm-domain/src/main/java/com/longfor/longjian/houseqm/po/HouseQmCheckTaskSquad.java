package com.longfor.longjian.houseqm.po;

import java.util.Date;
import javax.persistence.*;

@Table(name = "house_qm_check_task_squad")
public class HouseQmCheckTaskSquad {
    /**
     * 记录ID（同时为组ID）
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
     * 组类型（10=检查人组 20=整改人组）
     */
    @Column(name = "squad_type")
    private Integer squadType;

    /**
     * 组名称
     */
    private String name;

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
     * 获取记录ID（同时为组ID）
     *
     * @return id - 记录ID（同时为组ID）
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置记录ID（同时为组ID）
     *
     * @param id 记录ID（同时为组ID）
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
     * 获取组类型（10=检查人组 20=整改人组）
     *
     * @return squad_type - 组类型（10=检查人组 20=整改人组）
     */
    public Integer getSquadType() {
        return squadType;
    }

    /**
     * 设置组类型（10=检查人组 20=整改人组）
     *
     * @param squadType 组类型（10=检查人组 20=整改人组）
     */
    public void setSquadType(Integer squadType) {
        this.squadType = squadType;
    }

    /**
     * 获取组名称
     *
     * @return name - 组名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置组名称
     *
     * @param name 组名称
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
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