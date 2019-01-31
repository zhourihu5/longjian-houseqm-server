package com.longfor.longjian.houseqm.po.zj2db;

import com.longfor.longjian.houseqm.util.StringSplitToListUtil;

import java.util.Date;
import java.util.List;
import javax.persistence.*;

@Table(name = "category_v3")
public class CategoryV3 {
    @Id
    private Integer id;

    @Column(name = "father_key")
    private String fatherKey;
    @Column(name = "`key`")
    private String key;

    private String path;

    private Integer cls;

    @Column(name = "root_category_id")
    private Integer rootCategoryId;

    @Column(name = "team_id")
    private Integer teamId;
    @Column(name = "`order`")
    private String order;

    @Column(name = "zj_std_key")
    private String zjStdKey;

    @Column(name = "custom_key")
    private String customKey;
    @Column(name = "`name`")
    private String name;
    @Column(name = "`desc`")
    private String desc;

    @Column(name = "file_md5")
    private String fileMd5;

    @Column(name = "create_at")
    private Date createAt;

    @Column(name = "update_at")
    private Date updateAt;

    @Column(name = "delete_at")
    private Date deleteAt;

    @Column(name = "node_status")
    private Integer nodeStatus;

    public List<String> getPathSlice(){
        List<String> keys = StringSplitToListUtil.removeStartAndEndStrAndSplit(this.path, "/", "/");
        keys.add(this.key);
        return keys;
    }

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
     * @return father_key
     */
    public String getFatherKey() {
        return fatherKey;
    }

    /**
     * @param fatherKey
     */
    public void setFatherKey(String fatherKey) {
        this.fatherKey = fatherKey == null ? null : fatherKey.trim();
    }

    /**
     * @return key
     */
    public String getKey() {
        return key;
    }

    /**
     * @param key
     */
    public void setKey(String key) {
        this.key = key == null ? null : key.trim();
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
     * @return cls
     */
    public Integer getCls() {
        return cls;
    }

    /**
     * @param cls
     */
    public void setCls(Integer cls) {
        this.cls = cls;
    }

    /**
     * @return root_category_id
     */
    public Integer getRootCategoryId() {
        return rootCategoryId;
    }

    /**
     * @param rootCategoryId
     */
    public void setRootCategoryId(Integer rootCategoryId) {
        this.rootCategoryId = rootCategoryId;
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
     * @return order
     */
    public String getOrder() {
        return order;
    }

    /**
     * @param order
     */
    public void setOrder(String order) {
        this.order = order == null ? null : order.trim();
    }

    /**
     * @return zj_std_key
     */
    public String getZjStdKey() {
        return zjStdKey;
    }

    /**
     * @param zjStdKey
     */
    public void setZjStdKey(String zjStdKey) {
        this.zjStdKey = zjStdKey == null ? null : zjStdKey.trim();
    }

    /**
     * @return custom_key
     */
    public String getCustomKey() {
        return customKey;
    }

    /**
     * @param customKey
     */
    public void setCustomKey(String customKey) {
        this.customKey = customKey == null ? null : customKey.trim();
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
     * @return node_status
     */
    public Integer getNodeStatus() {
        return nodeStatus;
    }

    /**
     * @param nodeStatus
     */
    public void setNodeStatus(Integer nodeStatus) {
        this.nodeStatus = nodeStatus;
    }
}