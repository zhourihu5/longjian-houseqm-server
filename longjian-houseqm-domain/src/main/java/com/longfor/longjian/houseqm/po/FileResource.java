package com.longfor.longjian.houseqm.po;

import java.util.Date;
import javax.persistence.*;

@Table(name = "file_resource")
public class FileResource {
    @Id
    private Integer id;

    @Column(name = "mime_type")
    private String mimeType;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_ext")
    private String fileExt;

    @Column(name = "file_size")
    private Integer fileSize;

    @Column(name = "file_md5")
    private String fileMd5;

    private String desc;

    @Column(name = "store_key")
    private String storeKey;

    @Column(name = "sender_id")
    private Integer senderId;

    @Column(name = "create_at")
    private Date createAt;

    @Column(name = "update_at")
    private Date updateAt;

    @Column(name = "delete_at")
    private Date deleteAt;

    @Column(name = "project_id")
    private Integer projectId;

    private Integer typ;

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
     * @return mime_type
     */
    public String getMimeType() {
        return mimeType;
    }

    /**
     * @param mimeType
     */
    public void setMimeType(String mimeType) {
        this.mimeType = mimeType == null ? null : mimeType.trim();
    }

    /**
     * @return file_name
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * @param fileName
     */
    public void setFileName(String fileName) {
        this.fileName = fileName == null ? null : fileName.trim();
    }

    /**
     * @return file_ext
     */
    public String getFileExt() {
        return fileExt;
    }

    /**
     * @param fileExt
     */
    public void setFileExt(String fileExt) {
        this.fileExt = fileExt == null ? null : fileExt.trim();
    }

    /**
     * @return file_size
     */
    public Integer getFileSize() {
        return fileSize;
    }

    /**
     * @param fileSize
     */
    public void setFileSize(Integer fileSize) {
        this.fileSize = fileSize;
    }

    /**
     * @return file_md5
     */
    public String getFileMd5() {
        return fileMd5;
    }

    /**
     * @param fileMd5
     */
    public void setFileMd5(String fileMd5) {
        this.fileMd5 = fileMd5 == null ? null : fileMd5.trim();
    }

    /**
     * @return desc
     */
    public String getDesc() {
        return desc;
    }

    /**
     * @param desc
     */
    public void setDesc(String desc) {
        this.desc = desc == null ? null : desc.trim();
    }

    /**
     * @return store_key
     */
    public String getStoreKey() {
        return storeKey;
    }

    /**
     * @param storeKey
     */
    public void setStoreKey(String storeKey) {
        this.storeKey = storeKey == null ? null : storeKey.trim();
    }

    /**
     * @return sender_id
     */
    public Integer getSenderId() {
        return senderId;
    }

    /**
     * @param senderId
     */
    public void setSenderId(Integer senderId) {
        this.senderId = senderId;
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
}