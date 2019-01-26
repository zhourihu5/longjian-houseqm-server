package com.longfor.longjian.houseqm.po.stat;

import tk.mybatis.mapper.entity.IDynamicTableName;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "stat_house_qm_project_weekly_stat")
public class StatHouseQmProjectWeeklyStat implements IDynamicTableName {
    @Id
    private Integer id;

    /**
     * 项目id
     */
    @Column(name = "project_id")
    private Integer projectId;

    /**
     * 检查项分类
     */
    @Column(name = "category_cls")
    private Integer categoryCls;

    /**
     * 区域id
     */
    @Column(name = "area_id")
    private Integer areaId;

    /**
     * 父级id
     */
    @Column(name = "area_father_id")
    private Integer areaFatherId;

    /**
     * 区域名称
     */
    @Column(name = "area_name")
    private String areaName;

    /**
     * 区域path和id
     */
    @Column(name = "area_path_and_id")
    private String areaPathAndId;

    /**
     * 检查项key
     */
    @Column(name = "category_key")
    private String categoryKey;

    /**
     * 父级检查项key
     */
    @Column(name = "category_father_key")
    private String categoryFatherKey;

    /**
     * 检查项名称
     */
    @Column(name = "category_name")
    private String categoryName;

    /**
     * 父级检查项path和key
     */
    @Column(name = "category_path_and_key")
    private String categoryPathAndKey;

    /**
     * 是否拥有子节点
     */
    @Column(name = "has_sub")
    private Integer hasSub;

    /**
     * 根检查项id
     */
    @Column(name = "root_category_id")
    private Integer rootCategoryId;

    /**
     * 检查项排序
     */
    @Column(name = "category_order")
    private String categoryOrder;

    /**
     * 问题数量
     */
    @Column(name = "issue_count")
    private Integer issueCount;

    /**
     * 记录数量
     */
    @Column(name = "record_count")
    private Integer recordCount;

    /**
     * 新增问题数量
     */
    @Column(name = "issue_new_count")
    private Integer issueNewCount;

    /**
     * 新增记录数量
     */
    @Column(name = "record_new_count")
    private Integer recordNewCount;

    /**
     * 待分配问题数
     */
    @Column(name = "issue_note_no_assign_count")
    private Integer issueNoteNoAssignCount;

    /**
     * 已分配待整改问题数
     */
    @Column(name = "issue_assign_no_reform_count")
    private Integer issueAssignNoReformCount;

    /**
     * 已整改待销项问题数
     */
    @Column(name = "issue_reform_no_check_count")
    private Integer issueReformNoCheckCount;

    /**
     * 已销项问题数
     */
    @Column(name = "issue_check_yes_count")
    private Integer issueCheckYesCount;

    /**
     * 超期待指派问题数
     */
    @Column(name = "issue_overdue_to_assign_count")
    private Integer issueOverdueToAssignCount;

    /**
     * 超期待整改问题数
     */
    @Column(name = "issue_overdue_to_reform_count")
    private Integer issueOverdueToReformCount;

    /**
     * 超期待销项问题数
     */
    @Column(name = "issue_overdue_to_check_count")
    private Integer issueOverdueToCheckCount;

    /**
     * 超期待销项问题数
     */
    @Column(name = "issue_overdue_checked_count")
    private Integer issueOverdueCheckedCount;

    /**
     * 未超期待指派问题数
     */
    @Column(name = "issue_intime_to_assign_count")
    private Integer issueIntimeToAssignCount;

    /**
     * 未超期待整改问题数
     */
    @Column(name = "issue_intime_to_reform_count")
    private Integer issueIntimeToReformCount;

    /**
     * 未超期待销项问题数
     */
    @Column(name = "issue_intime_to_check_count")
    private Integer issueIntimeToCheckCount;

    /**
     * 未超期销项问题数
     */
    @Column(name = "issue_intime_checked_count")
    private Integer issueIntimeCheckedCount;

    /**
     * 未设置待指派问题数
     */
    @Column(name = "issue_notset_to_assign_count")
    private Integer issueNotsetToAssignCount;

    /**
     * 未设置待整改问题数
     */
    @Column(name = "issue_notset_to_reform_count")
    private Integer issueNotsetToReformCount;

    /**
     * 未设置待销项问题数
     */
    @Column(name = "issue_notset_to_check_count")
    private Integer issueNotsetToCheckCount;

    /**
     * 未设置销项问题数
     */
    @Column(name = "issue_notset_checked_count")
    private Integer issueNotsetCheckedCount;

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
     * 获取项目id
     *
     * @return project_id - 项目id
     */
    public Integer getProjectId() {
        return projectId;
    }

    /**
     * 设置项目id
     *
     * @param projectId 项目id
     */
    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    /**
     * 获取检查项分类
     *
     * @return category_cls - 检查项分类
     */
    public Integer getCategoryCls() {
        return categoryCls;
    }

    /**
     * 设置检查项分类
     *
     * @param categoryCls 检查项分类
     */
    public void setCategoryCls(Integer categoryCls) {
        this.categoryCls = categoryCls;
    }

    /**
     * 获取区域id
     *
     * @return area_id - 区域id
     */
    public Integer getAreaId() {
        return areaId;
    }

    /**
     * 设置区域id
     *
     * @param areaId 区域id
     */
    public void setAreaId(Integer areaId) {
        this.areaId = areaId;
    }

    /**
     * 获取父级id
     *
     * @return area_father_id - 父级id
     */
    public Integer getAreaFatherId() {
        return areaFatherId;
    }

    /**
     * 设置父级id
     *
     * @param areaFatherId 父级id
     */
    public void setAreaFatherId(Integer areaFatherId) {
        this.areaFatherId = areaFatherId;
    }

    /**
     * 获取区域名称
     *
     * @return area_name - 区域名称
     */
    public String getAreaName() {
        return areaName;
    }

    /**
     * 设置区域名称
     *
     * @param areaName 区域名称
     */
    public void setAreaName(String areaName) {
        this.areaName = areaName == null ? null : areaName.trim();
    }

    /**
     * 获取区域path和id
     *
     * @return area_path_and_id - 区域path和id
     */
    public String getAreaPathAndId() {
        return areaPathAndId;
    }

    /**
     * 设置区域path和id
     *
     * @param areaPathAndId 区域path和id
     */
    public void setAreaPathAndId(String areaPathAndId) {
        this.areaPathAndId = areaPathAndId == null ? null : areaPathAndId.trim();
    }

    /**
     * 获取检查项key
     *
     * @return category_key - 检查项key
     */
    public String getCategoryKey() {
        return categoryKey;
    }

    /**
     * 设置检查项key
     *
     * @param categoryKey 检查项key
     */
    public void setCategoryKey(String categoryKey) {
        this.categoryKey = categoryKey == null ? null : categoryKey.trim();
    }

    /**
     * 获取父级检查项key
     *
     * @return category_father_key - 父级检查项key
     */
    public String getCategoryFatherKey() {
        return categoryFatherKey;
    }

    /**
     * 设置父级检查项key
     *
     * @param categoryFatherKey 父级检查项key
     */
    public void setCategoryFatherKey(String categoryFatherKey) {
        this.categoryFatherKey = categoryFatherKey == null ? null : categoryFatherKey.trim();
    }

    /**
     * 获取检查项名称
     *
     * @return category_name - 检查项名称
     */
    public String getCategoryName() {
        return categoryName;
    }

    /**
     * 设置检查项名称
     *
     * @param categoryName 检查项名称
     */
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName == null ? null : categoryName.trim();
    }

    /**
     * 获取父级检查项path和key
     *
     * @return category_path_and_key - 父级检查项path和key
     */
    public String getCategoryPathAndKey() {
        return categoryPathAndKey;
    }

    /**
     * 设置父级检查项path和key
     *
     * @param categoryPathAndKey 父级检查项path和key
     */
    public void setCategoryPathAndKey(String categoryPathAndKey) {
        this.categoryPathAndKey = categoryPathAndKey == null ? null : categoryPathAndKey.trim();
    }

    /**
     * 获取是否拥有子节点
     *
     * @return has_sub - 是否拥有子节点
     */
    public Integer getHasSub() {
        return hasSub;
    }

    /**
     * 设置是否拥有子节点
     *
     * @param hasSub 是否拥有子节点
     */
    public void setHasSub(Integer hasSub) {
        this.hasSub = hasSub;
    }

    /**
     * 获取根检查项id
     *
     * @return root_category_id - 根检查项id
     */
    public Integer getRootCategoryId() {
        return rootCategoryId;
    }

    /**
     * 设置根检查项id
     *
     * @param rootCategoryId 根检查项id
     */
    public void setRootCategoryId(Integer rootCategoryId) {
        this.rootCategoryId = rootCategoryId;
    }

    /**
     * 获取检查项排序
     *
     * @return category_order - 检查项排序
     */
    public String getCategoryOrder() {
        return categoryOrder;
    }

    /**
     * 设置检查项排序
     *
     * @param categoryOrder 检查项排序
     */
    public void setCategoryOrder(String categoryOrder) {
        this.categoryOrder = categoryOrder == null ? null : categoryOrder.trim();
    }

    /**
     * 获取问题数量
     *
     * @return issue_count - 问题数量
     */
    public Integer getIssueCount() {
        return issueCount;
    }

    /**
     * 设置问题数量
     *
     * @param issueCount 问题数量
     */
    public void setIssueCount(Integer issueCount) {
        this.issueCount = issueCount;
    }

    /**
     * 获取记录数量
     *
     * @return record_count - 记录数量
     */
    public Integer getRecordCount() {
        return recordCount;
    }

    /**
     * 设置记录数量
     *
     * @param recordCount 记录数量
     */
    public void setRecordCount(Integer recordCount) {
        this.recordCount = recordCount;
    }

    /**
     * 获取新增问题数量
     *
     * @return issue_new_count - 新增问题数量
     */
    public Integer getIssueNewCount() {
        return issueNewCount;
    }

    /**
     * 设置新增问题数量
     *
     * @param issueNewCount 新增问题数量
     */
    public void setIssueNewCount(Integer issueNewCount) {
        this.issueNewCount = issueNewCount;
    }

    /**
     * 获取新增记录数量
     *
     * @return record_new_count - 新增记录数量
     */
    public Integer getRecordNewCount() {
        return recordNewCount;
    }

    /**
     * 设置新增记录数量
     *
     * @param recordNewCount 新增记录数量
     */
    public void setRecordNewCount(Integer recordNewCount) {
        this.recordNewCount = recordNewCount;
    }

    /**
     * 获取待分配问题数
     *
     * @return issue_note_no_assign_count - 待分配问题数
     */
    public Integer getIssueNoteNoAssignCount() {
        return issueNoteNoAssignCount;
    }

    /**
     * 设置待分配问题数
     *
     * @param issueNoteNoAssignCount 待分配问题数
     */
    public void setIssueNoteNoAssignCount(Integer issueNoteNoAssignCount) {
        this.issueNoteNoAssignCount = issueNoteNoAssignCount;
    }

    /**
     * 获取已分配待整改问题数
     *
     * @return issue_assign_no_reform_count - 已分配待整改问题数
     */
    public Integer getIssueAssignNoReformCount() {
        return issueAssignNoReformCount;
    }

    /**
     * 设置已分配待整改问题数
     *
     * @param issueAssignNoReformCount 已分配待整改问题数
     */
    public void setIssueAssignNoReformCount(Integer issueAssignNoReformCount) {
        this.issueAssignNoReformCount = issueAssignNoReformCount;
    }

    /**
     * 获取已整改待销项问题数
     *
     * @return issue_reform_no_check_count - 已整改待销项问题数
     */
    public Integer getIssueReformNoCheckCount() {
        return issueReformNoCheckCount;
    }

    /**
     * 设置已整改待销项问题数
     *
     * @param issueReformNoCheckCount 已整改待销项问题数
     */
    public void setIssueReformNoCheckCount(Integer issueReformNoCheckCount) {
        this.issueReformNoCheckCount = issueReformNoCheckCount;
    }

    /**
     * 获取已销项问题数
     *
     * @return issue_check_yes_count - 已销项问题数
     */
    public Integer getIssueCheckYesCount() {
        return issueCheckYesCount;
    }

    /**
     * 设置已销项问题数
     *
     * @param issueCheckYesCount 已销项问题数
     */
    public void setIssueCheckYesCount(Integer issueCheckYesCount) {
        this.issueCheckYesCount = issueCheckYesCount;
    }

    /**
     * 获取超期待指派问题数
     *
     * @return issue_overdue_to_assign_count - 超期待指派问题数
     */
    public Integer getIssueOverdueToAssignCount() {
        return issueOverdueToAssignCount;
    }

    /**
     * 设置超期待指派问题数
     *
     * @param issueOverdueToAssignCount 超期待指派问题数
     */
    public void setIssueOverdueToAssignCount(Integer issueOverdueToAssignCount) {
        this.issueOverdueToAssignCount = issueOverdueToAssignCount;
    }

    /**
     * 获取超期待整改问题数
     *
     * @return issue_overdue_to_reform_count - 超期待整改问题数
     */
    public Integer getIssueOverdueToReformCount() {
        return issueOverdueToReformCount;
    }

    /**
     * 设置超期待整改问题数
     *
     * @param issueOverdueToReformCount 超期待整改问题数
     */
    public void setIssueOverdueToReformCount(Integer issueOverdueToReformCount) {
        this.issueOverdueToReformCount = issueOverdueToReformCount;
    }

    /**
     * 获取超期待销项问题数
     *
     * @return issue_overdue_to_check_count - 超期待销项问题数
     */
    public Integer getIssueOverdueToCheckCount() {
        return issueOverdueToCheckCount;
    }

    /**
     * 设置超期待销项问题数
     *
     * @param issueOverdueToCheckCount 超期待销项问题数
     */
    public void setIssueOverdueToCheckCount(Integer issueOverdueToCheckCount) {
        this.issueOverdueToCheckCount = issueOverdueToCheckCount;
    }

    /**
     * 获取超期待销项问题数
     *
     * @return issue_overdue_checked_count - 超期待销项问题数
     */
    public Integer getIssueOverdueCheckedCount() {
        return issueOverdueCheckedCount;
    }

    /**
     * 设置超期待销项问题数
     *
     * @param issueOverdueCheckedCount 超期待销项问题数
     */
    public void setIssueOverdueCheckedCount(Integer issueOverdueCheckedCount) {
        this.issueOverdueCheckedCount = issueOverdueCheckedCount;
    }

    /**
     * 获取未超期待指派问题数
     *
     * @return issue_intime_to_assign_count - 未超期待指派问题数
     */
    public Integer getIssueIntimeToAssignCount() {
        return issueIntimeToAssignCount;
    }

    /**
     * 设置未超期待指派问题数
     *
     * @param issueIntimeToAssignCount 未超期待指派问题数
     */
    public void setIssueIntimeToAssignCount(Integer issueIntimeToAssignCount) {
        this.issueIntimeToAssignCount = issueIntimeToAssignCount;
    }

    /**
     * 获取未超期待整改问题数
     *
     * @return issue_intime_to_reform_count - 未超期待整改问题数
     */
    public Integer getIssueIntimeToReformCount() {
        return issueIntimeToReformCount;
    }

    /**
     * 设置未超期待整改问题数
     *
     * @param issueIntimeToReformCount 未超期待整改问题数
     */
    public void setIssueIntimeToReformCount(Integer issueIntimeToReformCount) {
        this.issueIntimeToReformCount = issueIntimeToReformCount;
    }

    /**
     * 获取未超期待销项问题数
     *
     * @return issue_intime_to_check_count - 未超期待销项问题数
     */
    public Integer getIssueIntimeToCheckCount() {
        return issueIntimeToCheckCount;
    }

    /**
     * 设置未超期待销项问题数
     *
     * @param issueIntimeToCheckCount 未超期待销项问题数
     */
    public void setIssueIntimeToCheckCount(Integer issueIntimeToCheckCount) {
        this.issueIntimeToCheckCount = issueIntimeToCheckCount;
    }

    /**
     * 获取未超期销项问题数
     *
     * @return issue_intime_checked_count - 未超期销项问题数
     */
    public Integer getIssueIntimeCheckedCount() {
        return issueIntimeCheckedCount;
    }

    /**
     * 设置未超期销项问题数
     *
     * @param issueIntimeCheckedCount 未超期销项问题数
     */
    public void setIssueIntimeCheckedCount(Integer issueIntimeCheckedCount) {
        this.issueIntimeCheckedCount = issueIntimeCheckedCount;
    }

    /**
     * 获取未设置待指派问题数
     *
     * @return issue_notset_to_assign_count - 未设置待指派问题数
     */
    public Integer getIssueNotsetToAssignCount() {
        return issueNotsetToAssignCount;
    }

    /**
     * 设置未设置待指派问题数
     *
     * @param issueNotsetToAssignCount 未设置待指派问题数
     */
    public void setIssueNotsetToAssignCount(Integer issueNotsetToAssignCount) {
        this.issueNotsetToAssignCount = issueNotsetToAssignCount;
    }

    /**
     * 获取未设置待整改问题数
     *
     * @return issue_notset_to_reform_count - 未设置待整改问题数
     */
    public Integer getIssueNotsetToReformCount() {
        return issueNotsetToReformCount;
    }

    /**
     * 设置未设置待整改问题数
     *
     * @param issueNotsetToReformCount 未设置待整改问题数
     */
    public void setIssueNotsetToReformCount(Integer issueNotsetToReformCount) {
        this.issueNotsetToReformCount = issueNotsetToReformCount;
    }

    /**
     * 获取未设置待销项问题数
     *
     * @return issue_notset_to_check_count - 未设置待销项问题数
     */
    public Integer getIssueNotsetToCheckCount() {
        return issueNotsetToCheckCount;
    }

    /**
     * 设置未设置待销项问题数
     *
     * @param issueNotsetToCheckCount 未设置待销项问题数
     */
    public void setIssueNotsetToCheckCount(Integer issueNotsetToCheckCount) {
        this.issueNotsetToCheckCount = issueNotsetToCheckCount;
    }

    /**
     * 获取未设置销项问题数
     *
     * @return issue_notset_checked_count - 未设置销项问题数
     */
    public Integer getIssueNotsetCheckedCount() {
        return issueNotsetCheckedCount;
    }

    /**
     * 设置未设置销项问题数
     *
     * @param issueNotsetCheckedCount 未设置销项问题数
     */
    public void setIssueNotsetCheckedCount(Integer issueNotsetCheckedCount) {
        this.issueNotsetCheckedCount = issueNotsetCheckedCount;
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

    @Override
    public String getDynamicTableName() {
        return null;
    }
}