package com.longfor.longjian.houseqm.po;

import java.util.Date;
import javax.persistence.*;

@Table(name = "project_setting_v2")
public class ProjectSettingV2 {
    @Id
    private Integer id;

    @Column(name = "project_id")
    private Integer projectId;

    @Column(name = "s_module")
    private String sModule;

    @Column(name = "parent_id")
    private Integer parentId;

    @Column(name = "main_type")
    private Integer mainType;

    @Column(name = "s_type")
    private Integer sType;

    @Column(name = "s_key")
    private String sKey;

    private String value;
    @Column(name = "`order`")
    private Integer order;

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
     * @return s_module
     */
    public String getsModule() {
        return sModule;
    }

    /**
     * @param sModule
     */
    public void setsModule(String sModule) {
        this.sModule = sModule == null ? null : sModule.trim();
    }

    /**
     * @return parent_id
     */
    public Integer getParentId() {
        return parentId;
    }

    /**
     * @param parentId
     */
    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    /**
     * @return main_type
     */
    public Integer getMainType() {
        return mainType;
    }

    /**
     * @param mainType
     */
    public void setMainType(Integer mainType) {
        this.mainType = mainType;
    }

    /**
     * @return s_type
     */
    public Integer getsType() {
        return sType;
    }

    /**
     * @param sType
     */
    public void setsType(Integer sType) {
        this.sType = sType;
    }

    /**
     * @return s_key
     */
    public String getsKey() {
        return sKey;
    }

    /**
     * @param sKey
     */
    public void setsKey(String sKey) {
        this.sKey = sKey == null ? null : sKey.trim();
    }

    /**
     * @return value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value
     */
    public void setValue(String value) {
        this.value = value == null ? null : value.trim();
    }

    /**
     * @return order
     */
    public Integer getOrder() {
        return order;
    }

    /**
     * @param order
     */
    public void setOrder(Integer order) {
        this.order = order;
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