package com.longfor.longjian.houseqm.po.zj2db;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by Dongshun on 2018/12/17.
 */
@Table(name = "area")
public class Area {
    @Id
    private Integer id;

    @Column(name = "project_id")
    private Integer projectId;

    private String name;

    @Column(name = "custom_code")
    private String customCode;

    private String path;

    private Integer type;

    @Column(name = "area_class_id")
    private Integer areaClassId;

    @Column(name = "is_lock")
    private Short isLock;

    @Column(name = "order_by")
    private Integer orderBy;

    @Column(name = "drawing_md5")
    private String drawingMd5;

    @Column(name = "father_id")
    private Integer fatherId;

    @Column(name = "create_at")
    private Date createAt;

    @Column(name = "update_at")
    private Date updateAt;

    @Column(name = "delete_at")
    private Date deleteAt;

    private String location;

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
     * @return custom_code
     */
    public String getCustomCode() {
        return customCode;
    }

    /**
     * @param customCode
     */
    public void setCustomCode(String customCode) {
        this.customCode = customCode == null ? null : customCode.trim();
    }

    /**
     * @return path
     */
    public String getPath() {
        return path;
    }

    /**
     * @param path
     */
    public void setPath(String path) {
        this.path = path == null ? null : path.trim();
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
     * @return area_class_id
     */
    public Integer getAreaClassId() {
        return areaClassId;
    }

    /**
     * @param areaClassId
     */
    public void setAreaClassId(Integer areaClassId) {
        this.areaClassId = areaClassId;
    }

    /**
     * @return is_lock
     */
    public Short getIsLock() {
        return isLock;
    }

    /**
     * @param isLock
     */
    public void setIsLock(Short isLock) {
        this.isLock = isLock;
    }

    /**
     * @return order_by
     */
    public Integer getOrderBy() {
        return orderBy;
    }

    /**
     * @param orderBy
     */
    public void setOrderBy(Integer orderBy) {
        this.orderBy = orderBy;
    }

    /**
     * @return drawing_md5
     */
    public String getDrawingMd5() {
        return drawingMd5;
    }

    /**
     * @param drawingMd5
     */
    public void setDrawingMd5(String drawingMd5) {
        this.drawingMd5 = drawingMd5 == null ? null : drawingMd5.trim();
    }

    /**
     * @return father_id
     */
    public Integer getFatherId() {
        return fatherId;
    }

    /**
     * @param fatherId
     */
    public void setFatherId(Integer fatherId) {
        this.fatherId = fatherId;
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
     * @return location
     */
    public String getLocation() {
        return location;
    }

    /**
     * @param location
     */
    public void setLocation(String location) {
        this.location = location == null ? null : location.trim();
    }
}

