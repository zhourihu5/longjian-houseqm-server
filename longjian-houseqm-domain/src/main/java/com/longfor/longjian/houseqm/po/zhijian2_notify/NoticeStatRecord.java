package com.longfor.longjian.houseqm.po.zhijian2_notify;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "notice_stat_record")
public class NoticeStatRecord {
    @Id
    @GeneratedValue(generator="JDBC")
    private Integer id;

    private String title;

    private String description;

    @Column(name = "notice_type")
    private Integer noticeType;

    @Column(name = "team_id")
    private Integer teamId;

    @Column(name = "team_name")
    private String teamName;

    @Column(name = "project_id")
    private Integer projectId;

    @Column(name = "project_name")
    private String projectName;

    @Column(name = "src_user_id")
    private Integer srcUserId;

    @Column(name = "src_user_name")
    private String srcUserName;

    @Column(name = "des_user_id")
    private Integer desUserId;

    @Column(name = "des_user_name")
    private String desUserName;

    @Column(name = "module_id")
    private Integer moduleId;

    @Column(name = "module_name")
    private String moduleName;

    private String content;

    @Column(name = "behavior_type")
    private Integer behaviorType;

    @Column(name = "behavior_name")
    private String behaviorName;

    @Column(name = "extra_info")
    private String extraInfo;

    @Column(name = "read_status")
    private Integer readStatus;

    @Column(name = "read_at")
    private Date readAt;

    @Column(name = "create_at")
    private Date createAt;

    @Column(name = "update_at")
    private Date updateAt;

    @Column(name = "delete_at")
    private Date deleteAt;

    @Column(name = "des_user_ids")
    private String desUserIds;

    @Column(name = "des_user_names")
    private String desUserNames;

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
     * @return team_name
     */
    public String getTeamName() {
        return teamName;
    }

    /**
     * @param teamName
     */
    public void setTeamName(String teamName) {
        this.teamName = teamName == null ? null : teamName.trim();
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
     * @return project_name
     */
    public String getProjectName() {
        return projectName;
    }

    /**
     * @param projectName
     */
    public void setProjectName(String projectName) {
        this.projectName = projectName == null ? null : projectName.trim();
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
     * @return src_user_name
     */
    public String getSrcUserName() {
        return srcUserName;
    }

    /**
     * @param srcUserName
     */
    public void setSrcUserName(String srcUserName) {
        this.srcUserName = srcUserName == null ? null : srcUserName.trim();
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
     * @return des_user_name
     */
    public String getDesUserName() {
        return desUserName;
    }

    /**
     * @param desUserName
     */
    public void setDesUserName(String desUserName) {
        this.desUserName = desUserName == null ? null : desUserName.trim();
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
     * @return read_status
     */
    public Integer getReadStatus() {
        return readStatus;
    }

    /**
     * @param readStatus
     */
    public void setReadStatus(Integer readStatus) {
        this.readStatus = readStatus;
    }

    /**
     * @return read_at
     */
    public Date getReadAt() {
        return readAt;
    }

    /**
     * @param readAt
     */
    public void setReadAt(Date readAt) {
        this.readAt = readAt;
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
     * @return des_user_ids
     */
    public String getDesUserIds() {
        return desUserIds;
    }

    /**
     * @param desUserIds
     */
    public void setDesUserIds(String desUserIds) {
        this.desUserIds = desUserIds == null ? null : desUserIds.trim();
    }

    /**
     * @return des_user_names
     */
    public String getDesUserNames() {
        return desUserNames;
    }

    /**
     * @param desUserNames
     */
    public void setDesUserNames(String desUserNames) {
        this.desUserNames = desUserNames == null ? null : desUserNames.trim();
    }
}