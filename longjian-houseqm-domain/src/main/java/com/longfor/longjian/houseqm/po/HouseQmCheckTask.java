package com.longfor.longjian.houseqm.po;

import java.util.Date;
import javax.persistence.*;

@Table(name = "house_qm_check_task")
public class HouseQmCheckTask {
    /**
     * 记录ID
     */
    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    /**
     * 项目ID（关联project表id字段）
     */
    @Column(name = "project_id")
    private Integer projectId;

    /**
     * 任务ID（关联task表id字段）
     */
    @Column(name = "task_id")
    private Integer taskId;

    /**
     * 任务名称
     */
    @Column(name = "`name`")
    private String name;

    /**
     * 任务状态
     */
    private Integer status;

    /**
     * 任务类型（23=日常检查, 24=月度检查, 25=季度检查, 26=入伙验房, 27=专项检查, 28=分户验收, 29=安全检查, 30=承接查验, 31=工地开放）
     */
    @Column(name = "category_cls")
    private Integer categoryCls;

    /**
     * 根检查项
     */
    @Column(name = "root_category_key")
    private String rootCategoryKey;

    /**
     * 任务区域类型（1=公共区域, 2=楼栋, 3=层, 4=户, 5=房间, 6=楼层公区, 7=别墅, 8=其他）
     */
    @Column(name = "area_types")
    private String areaTypes;

    /**
     * 任务计划开始时间
     */
    @Column(name = "plan_begin_on")
    private Date planBeginOn;

    /**
     * 任务计划结束时间
     */
    @Column(name = "plan_end_on")
    private Date planEndOn;

    /**
     * 任务结束时间
     */
    @Column(name = "end_on")
    private Date endOn;

    /**
     * 创建人
     */
    private Integer creator;

    /**
     * 最后编辑人
     */
    private Integer editor;

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
     * 任务额外配置信息
     */
    @Column(name = "config_info")
    private String configInfo;

    /**
     * 集中交付起始时间（仅针对入伙验房）
     */
    @Column(name = "delivery_begin_on")
    private Date deliveryBeginOn;

    /**
     * 集中交付结束时间（仅针对入伙验房）
     */
    @Column(name = "delivery_end_on")
    private Date deliveryEndOn;

    /**
     * 任务区域范围
     */
    @Column(name = "area_ids")
    private String areaIds;

    /**
     * 获取记录ID
     *
     * @return id - 记录ID
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置记录ID
     *
     * @param id 记录ID
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取项目ID（关联project表id字段）
     *
     * @return project_id - 项目ID（关联project表id字段）
     */
    public Integer getProjectId() {
        return projectId;
    }

    /**
     * 设置项目ID（关联project表id字段）
     *
     * @param projectId 项目ID（关联project表id字段）
     */
    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    /**
     * 获取任务ID（关联task表id字段）
     *
     * @return task_id - 任务ID（关联task表id字段）
     */
    public Integer getTaskId() {
        return taskId;
    }

    /**
     * 设置任务ID（关联task表id字段）
     *
     * @param taskId 任务ID（关联task表id字段）
     */
    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    /**
     * 获取任务名称
     *
     * @return name - 任务名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置任务名称
     *
     * @param name 任务名称
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * 获取任务状态
     *
     * @return status - 任务状态
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置任务状态
     *
     * @param status 任务状态
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 获取任务类型（23=日常检查, 24=月度检查, 25=季度检查, 26=入伙验房, 27=专项检查, 28=分户验收, 29=安全检查, 30=承接查验, 31=工地开放）
     *
     * @return category_cls - 任务类型（23=日常检查, 24=月度检查, 25=季度检查, 26=入伙验房, 27=专项检查, 28=分户验收, 29=安全检查, 30=承接查验, 31=工地开放）
     */
    public Integer getCategoryCls() {
        return categoryCls;
    }

    /**
     * 设置任务类型（23=日常检查, 24=月度检查, 25=季度检查, 26=入伙验房, 27=专项检查, 28=分户验收, 29=安全检查, 30=承接查验, 31=工地开放）
     *
     * @param categoryCls 任务类型（23=日常检查, 24=月度检查, 25=季度检查, 26=入伙验房, 27=专项检查, 28=分户验收, 29=安全检查, 30=承接查验, 31=工地开放）
     */
    public void setCategoryCls(Integer categoryCls) {
        this.categoryCls = categoryCls;
    }

    /**
     * 获取根检查项
     *
     * @return root_category_key - 根检查项
     */
    public String getRootCategoryKey() {
        return rootCategoryKey;
    }

    /**
     * 设置根检查项
     *
     * @param rootCategoryKey 根检查项
     */
    public void setRootCategoryKey(String rootCategoryKey) {
        this.rootCategoryKey = rootCategoryKey == null ? null : rootCategoryKey.trim();
    }

    /**
     * 获取任务区域类型（1=公共区域, 2=楼栋, 3=层, 4=户, 5=房间, 6=楼层公区, 7=别墅, 8=其他）
     *
     * @return area_types - 任务区域类型（1=公共区域, 2=楼栋, 3=层, 4=户, 5=房间, 6=楼层公区, 7=别墅, 8=其他）
     */
    public String getAreaTypes() {
        return areaTypes;
    }

    /**
     * 设置任务区域类型（1=公共区域, 2=楼栋, 3=层, 4=户, 5=房间, 6=楼层公区, 7=别墅, 8=其他）
     *
     * @param areaTypes 任务区域类型（1=公共区域, 2=楼栋, 3=层, 4=户, 5=房间, 6=楼层公区, 7=别墅, 8=其他）
     */
    public void setAreaTypes(String areaTypes) {
        this.areaTypes = areaTypes == null ? null : areaTypes.trim();
    }

    /**
     * 获取任务计划开始时间
     *
     * @return plan_begin_on - 任务计划开始时间
     */
    public Date getPlanBeginOn() {
        return planBeginOn;
    }

    /**
     * 设置任务计划开始时间
     *
     * @param planBeginOn 任务计划开始时间
     */
    public void setPlanBeginOn(Date planBeginOn) {
        this.planBeginOn = planBeginOn;
    }

    /**
     * 获取任务计划结束时间
     *
     * @return plan_end_on - 任务计划结束时间
     */
    public Date getPlanEndOn() {
        return planEndOn;
    }

    /**
     * 设置任务计划结束时间
     *
     * @param planEndOn 任务计划结束时间
     */
    public void setPlanEndOn(Date planEndOn) {
        this.planEndOn = planEndOn;
    }

    /**
     * 获取任务结束时间
     *
     * @return end_on - 任务结束时间
     */
    public Date getEndOn() {
        return endOn;
    }

    /**
     * 设置任务结束时间
     *
     * @param endOn 任务结束时间
     */
    public void setEndOn(Date endOn) {
        this.endOn = endOn;
    }

    /**
     * 获取创建人
     *
     * @return creator - 创建人
     */
    public Integer getCreator() {
        return creator;
    }

    /**
     * 设置创建人
     *
     * @param creator 创建人
     */
    public void setCreator(Integer creator) {
        this.creator = creator;
    }

    /**
     * 获取最后编辑人
     *
     * @return editor - 最后编辑人
     */
    public Integer getEditor() {
        return editor;
    }

    /**
     * 设置最后编辑人
     *
     * @param editor 最后编辑人
     */
    public void setEditor(Integer editor) {
        this.editor = editor;
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

    /**
     * 获取任务额外配置信息
     *
     * @return config_info - 任务额外配置信息
     */
    public String getConfigInfo() {
        return configInfo;
    }

    /**
     * 设置任务额外配置信息
     *
     * @param configInfo 任务额外配置信息
     */
    public void setConfigInfo(String configInfo) {
        this.configInfo = configInfo == null ? null : configInfo.trim();
    }

    /**
     * 获取集中交付起始时间（仅针对入伙验房）
     *
     * @return delivery_begin_on - 集中交付起始时间（仅针对入伙验房）
     */
    public Date getDeliveryBeginOn() {
        return deliveryBeginOn;
    }

    /**
     * 设置集中交付起始时间（仅针对入伙验房）
     *
     * @param deliveryBeginOn 集中交付起始时间（仅针对入伙验房）
     */
    public void setDeliveryBeginOn(Date deliveryBeginOn) {
        this.deliveryBeginOn = deliveryBeginOn;
    }

    /**
     * 获取集中交付结束时间（仅针对入伙验房）
     *
     * @return delivery_end_on - 集中交付结束时间（仅针对入伙验房）
     */
    public Date getDeliveryEndOn() {
        return deliveryEndOn;
    }

    /**
     * 设置集中交付结束时间（仅针对入伙验房）
     *
     * @param deliveryEndOn 集中交付结束时间（仅针对入伙验房）
     */
    public void setDeliveryEndOn(Date deliveryEndOn) {
        this.deliveryEndOn = deliveryEndOn;
    }

    /**
     * 获取任务区域范围
     *
     * @return area_ids - 任务区域范围
     */
    public String getAreaIds() {
        return areaIds;
    }

    /**
     * 设置任务区域范围
     *
     * @param areaIds 任务区域范围
     */
    public void setAreaIds(String areaIds) {
        this.areaIds = areaIds == null ? null : areaIds.trim();
    }
}