package com.longfor.longjian.houseqm.po;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

/**
 * 用于检查人员的统计 映射
 *
 * @author Houyan
 * @date 2018/12/15 0015 12:45
 */
@Table(name = "house_qm_check_task_issue")
public class CheckerIssueStat {
    /**
     * 统计提交人Id的数量
     */
    @Column(name = "count")
    private Integer count;

    /**
     * 项目ID（关联project表id字段）
     */
    @Column(name = "project_id")
    private Integer projectId;

    /**
     * 提交人ID
     */
    @Column(name = "sender_id")
    private Integer userId;

    /**
     * 任务ID（关联task表id字段
     */
    @Column(name = "task_id")
    private Integer taskId;
    /**
     * 区域ID
     */
    @Column(name = "area_id")
    private Integer areaId;

    /**
     * 区域路径和id
     */
    @Column(name = "area_path_and_id")
    private String areaPathAndId;
    /**
     * 99 普通记录；1 问题记录
     */
    private Integer typ;
    /**
     * 删除时间
     */
    @Column(name = "delete_at")
    private Date deleteAt;

    /**
     * create_at 时间格式 转换
     */
    @Column(name = "date")
    private Date date;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public Integer getAreaId() {
        return areaId;
    }

    public void setAreaId(Integer areaId) {
        this.areaId = areaId;
    }

    public String getAreaPathAndId() {
        return areaPathAndId;
    }

    public void setAreaPathAndId(String areaPathAndId) {
        this.areaPathAndId = areaPathAndId;
    }

    public Integer getTyp() {
        return typ;
    }

    public void setTyp(Integer typ) {
        this.typ = typ;
    }

    public Date getDeleteAt() {
        return deleteAt;
    }

    public void setDeleteAt(Date deleteAt) {
        this.deleteAt = deleteAt;
    }
}
