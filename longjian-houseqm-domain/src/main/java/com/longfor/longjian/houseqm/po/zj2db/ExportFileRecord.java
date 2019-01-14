package com.longfor.longjian.houseqm.po.zj2db;

import java.util.Date;
import javax.persistence.*;

@Table(name = "export_file_record")
public class ExportFileRecord {
    @Id
    private Integer id;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "team_id")
    private Integer teamId;

    @Column(name = "project_id")
    private Integer projectId;

    @Column(name = "export_type")
    private Integer exportType;

    private String params;

    @Column(name = "result_file_path")
    private String resultFilePath;

    @Column(name = "result_name")
    private String resultName;

    private Integer status;

    @Column(name = "execute_at")
    private Date executeAt;

    @Column(name = "create_at")
    private Date createAt;

    @Column(name = "update_at")
    private Date updateAt;

    @Column(name = "delete_at")
    private Date deleteAt;

    @Column(name = "error_msg")
    private String errorMsg;

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
     * @return user_id
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * @param userId
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
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
     * @return export_type
     */
    public Integer getExportType() {
        return exportType;
    }

    /**
     * @param exportType
     */
    public void setExportType(Integer exportType) {
        this.exportType = exportType;
    }

    /**
     * @return params
     */
    public String getParams() {
        return params;
    }

    /**
     * @param params
     */
    public void setParams(String params) {
        this.params = params == null ? null : params.trim();
    }

    /**
     * @return result_file_path
     */
    public String getResultFilePath() {
        return resultFilePath;
    }

    /**
     * @param resultFilePath
     */
    public void setResultFilePath(String resultFilePath) {
        this.resultFilePath = resultFilePath == null ? null : resultFilePath.trim();
    }

    /**
     * @return result_name
     */
    public String getResultName() {
        return resultName;
    }

    /**
     * @param resultName
     */
    public void setResultName(String resultName) {
        this.resultName = resultName == null ? null : resultName.trim();
    }

    /**
     * @return status
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * @param status
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * @return execute_at
     */
    public Date getExecuteAt() {
        return executeAt;
    }

    /**
     * @param executeAt
     */
    public void setExecuteAt(Date executeAt) {
        this.executeAt = executeAt;
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
     * @return error_msg
     */
    public String getErrorMsg() {
        return errorMsg;
    }

    /**
     * @param errorMsg
     */
    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg == null ? null : errorMsg.trim();
    }
}