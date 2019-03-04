package com.longfor.longjian.houseqm.po.zj2db;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.Date;

public class Project {
    @Id
    private Integer id;

    private String name;

    private Integer type;

    /**
     * 项目阶段对应id
     */
    @Column(name = "stage_id")
    private Integer stageId;

    @Column(name = "team_id")
    private Integer teamId;

    @Column(name = "create_at")
    private Date createAt;

    @Column(name = "update_at")
    private Date updateAt;

    @Column(name = "delete_at")
    private Date deleteAt;

    @Column(name = "summarize_info")
    private String summarizeInfo;

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
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * @return type
     */
    public Integer getType() {
        return type;
    }

    /**
     * @param type
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * 获取项目阶段对应id
     *
     * @return stage_id - 项目阶段对应id
     */
    public Integer getStageId() {
        return stageId;
    }

    /**
     * 设置项目阶段对应id
     *
     * @param stageId 项目阶段对应id
     */
    public void setStageId(Integer stageId) {
        this.stageId = stageId;
    }

    /**
     * @return team_id
     */
    public Integer getTeamId() {
        return teamId;
    }

    /**
     * @param teamId
     */
    public void setTeamId(Integer teamId) {
        this.teamId = teamId;
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
     * @return summarize_info
     */
    public String getSummarizeInfo() {
        return summarizeInfo;
    }

    /**
     * @param summarizeInfo
     */
    public void setSummarizeInfo(String summarizeInfo) {
        this.summarizeInfo = summarizeInfo == null ? null : summarizeInfo.trim();
    }
}