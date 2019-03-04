package com.longfor.longjian.houseqm.po.stat;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "stat_house_qm_project_user_workload_checker_daily_stat")
public class StatHouseQmProjectUserWorkloadCheckerDailyStat {
    @Id
    private Integer id;

    @Column(name = "project_id")
    private Integer projectId;

    @Column(name = "category_cls")
    private Integer categoryCls;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "issue_count")
    private Integer issueCount;

    @Column(name = "record_count")
    private Integer recordCount;

    @Column(name = "issue_check_yes_count")
    private Integer issueCheckYesCount;

    @Column(name = "issue_new_count")
    private Integer issueNewCount;

    @Column(name = "record_new_count")
    private Integer recordNewCount;

    @Column(name = "issue_new_check_yes_count")
    private Integer issueNewCheckYesCount;

    @Column(name = "date_day")
    private String dateDay;

    @Column(name = "date_week")
    private String dateWeek;

    @Column(name = "date_month")
    private String dateMonth;

    @Column(name = "date_quarter")
    private String dateQuarter;

    @Column(name = "date_year")
    private String dateYear;

    @Column(name = "update_at")
    private Date updateAt;

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
     * @return category_cls
     */
    public Integer getCategoryCls() {
        return categoryCls;
    }

    /**
     * @param categoryCls
     */
    public void setCategoryCls(Integer categoryCls) {
        this.categoryCls = categoryCls;
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
     * @return issue_count
     */
    public Integer getIssueCount() {
        return issueCount;
    }

    /**
     * @param issueCount
     */
    public void setIssueCount(Integer issueCount) {
        this.issueCount = issueCount;
    }

    /**
     * @return record_count
     */
    public Integer getRecordCount() {
        return recordCount;
    }

    /**
     * @param recordCount
     */
    public void setRecordCount(Integer recordCount) {
        this.recordCount = recordCount;
    }

    /**
     * @return issue_check_yes_count
     */
    public Integer getIssueCheckYesCount() {
        return issueCheckYesCount;
    }

    /**
     * @param issueCheckYesCount
     */
    public void setIssueCheckYesCount(Integer issueCheckYesCount) {
        this.issueCheckYesCount = issueCheckYesCount;
    }

    /**
     * @return issue_new_count
     */
    public Integer getIssueNewCount() {
        return issueNewCount;
    }

    /**
     * @param issueNewCount
     */
    public void setIssueNewCount(Integer issueNewCount) {
        this.issueNewCount = issueNewCount;
    }

    /**
     * @return record_new_count
     */
    public Integer getRecordNewCount() {
        return recordNewCount;
    }

    /**
     * @param recordNewCount
     */
    public void setRecordNewCount(Integer recordNewCount) {
        this.recordNewCount = recordNewCount;
    }

    /**
     * @return issue_new_check_yes_count
     */
    public Integer getIssueNewCheckYesCount() {
        return issueNewCheckYesCount;
    }

    /**
     * @param issueNewCheckYesCount
     */
    public void setIssueNewCheckYesCount(Integer issueNewCheckYesCount) {
        this.issueNewCheckYesCount = issueNewCheckYesCount;
    }

    /**
     * @return date_day
     */
    public String getDateDay() {
        return dateDay;
    }

    /**
     * @param dateDay
     */
    public void setDateDay(String dateDay) {
        this.dateDay = dateDay == null ? null : dateDay.trim();
    }

    /**
     * @return date_week
     */
    public String getDateWeek() {
        return dateWeek;
    }

    /**
     * @param dateWeek
     */
    public void setDateWeek(String dateWeek) {
        this.dateWeek = dateWeek == null ? null : dateWeek.trim();
    }

    /**
     * @return date_month
     */
    public String getDateMonth() {
        return dateMonth;
    }

    /**
     * @param dateMonth
     */
    public void setDateMonth(String dateMonth) {
        this.dateMonth = dateMonth == null ? null : dateMonth.trim();
    }

    /**
     * @return date_quarter
     */
    public String getDateQuarter() {
        return dateQuarter;
    }

    /**
     * @param dateQuarter
     */
    public void setDateQuarter(String dateQuarter) {
        this.dateQuarter = dateQuarter == null ? null : dateQuarter.trim();
    }

    /**
     * @return date_year
     */
    public String getDateYear() {
        return dateYear;
    }

    /**
     * @param dateYear
     */
    public void setDateYear(String dateYear) {
        this.dateYear = dateYear == null ? null : dateYear.trim();
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
}