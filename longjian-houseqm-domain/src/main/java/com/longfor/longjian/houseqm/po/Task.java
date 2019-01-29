package com.longfor.longjian.houseqm.po;

import java.util.Date;
import javax.persistence.*;

public class Task {
    @Id
    @GeneratedValue(generator="JDBC")
    private Integer id;

    /**
     * 项目ID project.id
     */
    @Column(name = "project_id")
    private Integer projectId;

    private String name;

    @Column(name = "creator_id")
    private Integer creatorId;

    private Integer typ;

    @Column(name = "config_info")
    private String configInfo;

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
     * 获取项目ID project.id
     *
     * @return project_id - 项目ID project.id
     */
    public Integer getProjectId() {
        return projectId;
    }

    /**
     * 设置项目ID project.id
     *
     * @param projectId 项目ID project.id
     */
    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
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
     * @return creator_id
     */
    public Integer getCreatorId() {
        return creatorId;
    }

    /**
     * @param creatorId
     */
    public void setCreatorId(Integer creatorId) {
        this.creatorId = creatorId;
    }

    /**
     * @return typ
     */
    public Integer getTyp() {
        return typ;
    }

    /**
     * @param typ
     */
    public void setTyp(Integer typ) {
        this.typ = typ;
    }

    /**
     * @return config_info
     */
    public String getConfigInfo() {
        return configInfo;
    }

    /**
     * @param configInfo
     */
    public void setConfigInfo(String configInfo) {
        this.configInfo = configInfo == null ? null : configInfo.trim();
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