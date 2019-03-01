package com.longfor.longjian.houseqm.po.zhijian2_notify;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "notice_user_record")
public class NoticeUserRecord {
    @Id
    private Integer id;

    /**
     * 推送用户ID
     */
    @Column(name = "user_id")
    private Integer userId;

    /**
     * 消息ID
     */
    @Column(name = "notice_id")
    private Integer noticeId;

    /**
     * 消息类型
     */
    @Column(name = "notice_type")
    private Integer noticeType;

    /**
     * 模块ID
     */
    @Column(name = "module_id")
    private Integer moduleId;

    /**
     * 读取状态
     */
    @Column(name = "read_status")
    private Integer readStatus;

    /**
     * 读取时间
     */
    @Column(name = "read_at")
    private Date readAt;

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
     * 获取推送用户ID
     *
     * @return user_id - 推送用户ID
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * 设置推送用户ID
     *
     * @param userId 推送用户ID
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * 获取消息ID
     *
     * @return notice_id - 消息ID
     */
    public Integer getNoticeId() {
        return noticeId;
    }

    /**
     * 设置消息ID
     *
     * @param noticeId 消息ID
     */
    public void setNoticeId(Integer noticeId) {
        this.noticeId = noticeId;
    }

    /**
     * 获取消息类型
     *
     * @return notice_type - 消息类型
     */
    public Integer getNoticeType() {
        return noticeType;
    }

    /**
     * 设置消息类型
     *
     * @param noticeType 消息类型
     */
    public void setNoticeType(Integer noticeType) {
        this.noticeType = noticeType;
    }

    /**
     * 获取模块ID
     *
     * @return module_id - 模块ID
     */
    public Integer getModuleId() {
        return moduleId;
    }

    /**
     * 设置模块ID
     *
     * @param moduleId 模块ID
     */
    public void setModuleId(Integer moduleId) {
        this.moduleId = moduleId;
    }

    /**
     * 获取读取状态
     *
     * @return read_status - 读取状态
     */
    public Integer getReadStatus() {
        return readStatus;
    }

    /**
     * 设置读取状态
     *
     * @param readStatus 读取状态
     */
    public void setReadStatus(Integer readStatus) {
        this.readStatus = readStatus;
    }

    /**
     * 获取读取时间
     *
     * @return read_at - 读取时间
     */
    public Date getReadAt() {
        return readAt;
    }

    /**
     * 设置读取时间
     *
     * @param readAt 读取时间
     */
    public void setReadAt(Date readAt) {
        this.readAt = readAt;
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