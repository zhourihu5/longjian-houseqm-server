package com.longfor.longjian.houseqm.po.zhijian2_setting;

import java.util.Date;
import javax.persistence.*;

@Table(name = "issue_field_setting")
public class IssueFieldSetting {
    @Id
    private Integer id;

    @Column(name = "project_id")
    private Integer projectId;

    @Column(name = "field_id")
    private Integer fieldId;

    private String name;

    private String alias;

    @Column(name = "display_status")
    private Integer displayStatus;

    @Column(name = "required_status")
    private Integer requiredStatus;

    @Column(name = "alias_status")
    private Integer aliasStatus;

    @Column(name = "module_id")
    private Integer moduleId;

    @Column(name = "modify_user_id")
    private Integer modifyUserId;

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
     * @return field_id
     */
    public Integer getFieldId() {
        return fieldId;
    }

    /**
     * @param fieldId
     */
    public void setFieldId(Integer fieldId) {
        this.fieldId = fieldId;
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
     * @return alias
     */
    public String getAlias() {
        return alias;
    }

    /**
     * @param alias
     */
    public void setAlias(String alias) {
        this.alias = alias == null ? null : alias.trim();
    }

    /**
     * @return display_status
     */
    public Integer getDisplayStatus() {
        return displayStatus;
    }

    /**
     * @param displayStatus
     */
    public void setDisplayStatus(Integer displayStatus) {
        this.displayStatus = displayStatus;
    }

    /**
     * @return required_status
     */
    public Integer getRequiredStatus() {
        return requiredStatus;
    }

    /**
     * @param requiredStatus
     */
    public void setRequiredStatus(Integer requiredStatus) {
        this.requiredStatus = requiredStatus;
    }

    /**
     * @return alias_status
     */
    public Integer getAliasStatus() {
        return aliasStatus;
    }

    /**
     * @param aliasStatus
     */
    public void setAliasStatus(Integer aliasStatus) {
        this.aliasStatus = aliasStatus;
    }

    /**
     * @return module_id
     */
    public Integer getModuleId() {
        return moduleId;
    }

    /**
     * @param moduleId
     */
    public void setModuleId(Integer moduleId) {
        this.moduleId = moduleId;
    }

    /**
     * @return modify_user_id
     */
    public Integer getModifyUserId() {
        return modifyUserId;
    }

    /**
     * @param modifyUserId
     */
    public void setModifyUserId(Integer modifyUserId) {
        this.modifyUserId = modifyUserId;
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