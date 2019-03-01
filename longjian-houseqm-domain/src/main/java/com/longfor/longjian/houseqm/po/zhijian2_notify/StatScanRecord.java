package com.longfor.longjian.houseqm.po.zhijian2_notify;

import java.util.Date;
import javax.persistence.*;

@Table(name = "stat_scan_record")
public class StatScanRecord {
    @Id
    private Integer id;

    @Column(name = "module_id")
    private Integer moduleId;

    @Column(name = "module_name")
    private String moduleName;

    @Column(name = "prev_scan_at")
    private Date prevScanAt;

    @Column(name = "last_scan_at")
    private Date lastScanAt;

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
     * @return module_name
     */
    public String getModuleName() {
        return moduleName;
    }

    /**
     * @param moduleName
     */
    public void setModuleName(String moduleName) {
        this.moduleName = moduleName == null ? null : moduleName.trim();
    }

    /**
     * @return prev_scan_at
     */
    public Date getPrevScanAt() {
        return prevScanAt;
    }

    /**
     * @param prevScanAt
     */
    public void setPrevScanAt(Date prevScanAt) {
        this.prevScanAt = prevScanAt;
    }

    /**
     * @return last_scan_at
     */
    public Date getLastScanAt() {
        return lastScanAt;
    }

    /**
     * @param lastScanAt
     */
    public void setLastScanAt(Date lastScanAt) {
        this.lastScanAt = lastScanAt;
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