package com.longfor.longjian.houseqm.po.zhijian2_notify;

import java.util.Date;
import javax.persistence.*;

@Table(name = "gcgl_issue_stat_record")
public class GcglIssueStatRecord {
    @Id
    private Integer id;

    private String uuid;

    private String title;

    private String description;

    @Column(name = "notice_type")
    private Integer noticeType;

    @Column(name = "source_type")
    private Integer sourceType;

    @Column(name = "team_id")
    private Integer teamId;

    @Column(name = "project_id")
    private Integer projectId;

    @Column(name = "src_user_id")
    private Integer srcUserId;

    @Column(name = "des_user_id")
    private Integer desUserId;

    @Column(name = "module_id")
    private Integer moduleId;

    private String content;

    @Column(name = "behavior_type")
    private Integer behaviorType;

    @Column(name = "behavior_name")
    private String behaviorName;

    @Column(name = "extra_info")
    private String extraInfo;

    @Column(name = "issue_ids")
    private String issueIds;

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
     * @return uuid
     */
    public String getUuid() {
        return uuid;
    }

    /**
     * @param uuid
     */
    public void setUuid(String uuid) {
        this.uuid = uuid == null ? null : uuid.trim();
    }

    /**
     * @return title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title
     */
    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    /**
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description
     */
    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    /**
     * @return notice_type
     */
    public Integer getNoticeType() {
        return noticeType;
    }

    /**
     * @param noticeType
     */
    public void setNoticeType(Integer noticeType) {
        this.noticeType = noticeType;
    }

    /**
     * @return source_type
     */
    public Integer getSourceType() {
        return sourceType;
    }

    /**
     * @param sourceType
     */
    public void setSourceType(Integer sourceType) {
        this.sourceType = sourceType;
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
     * @return src_user_id
     */
    public Integer getSrcUserId() {
        return srcUserId;
    }

    /**
     * @param srcUserId
     */
    public void setSrcUserId(Integer srcUserId) {
        this.srcUserId = srcUserId;
    }

    /**
     * @return des_user_id
     */
    public Integer getDesUserId() {
        return desUserId;
    }

    /**
     * @param desUserId
     */
    public void setDesUserId(Integer desUserId) {
        this.desUserId = desUserId;
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
     * @return content
     */
    public String getContent() {
        return content;
    }

    /**
     * @param content
     */
    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    /**
     * @return behavior_type
     */
    public Integer getBehaviorType() {
        return behaviorType;
    }

    /**
     * @param behaviorType
     */
    public void setBehaviorType(Integer behaviorType) {
        this.behaviorType = behaviorType;
    }

    /**
     * @return behavior_name
     */
    public String getBehaviorName() {
        return behaviorName;
    }

    /**
     * @param behaviorName
     */
    public void setBehaviorName(String behaviorName) {
        this.behaviorName = behaviorName == null ? null : behaviorName.trim();
    }

    /**
     * @return extra_info
     */
    public String getExtraInfo() {
        return extraInfo;
    }

    /**
     * @param extraInfo
     */
    public void setExtraInfo(String extraInfo) {
        this.extraInfo = extraInfo == null ? null : extraInfo.trim();
    }

    /**
     * @return issue_ids
     */
    public String getIssueIds() {
        return issueIds;
    }

    /**
     * @param issueIds
     */
    public void setIssueIds(String issueIds) {
        this.issueIds = issueIds == null ? null : issueIds.trim();
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