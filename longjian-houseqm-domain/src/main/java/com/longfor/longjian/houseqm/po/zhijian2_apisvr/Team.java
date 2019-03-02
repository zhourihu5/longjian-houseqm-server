package com.longfor.longjian.houseqm.po.zhijian2_apisvr;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "team")
public class Team {
    @Id
    @Column(name = "team_id")
    private Integer teamId;

    @Column(name = "team_name")
    private String teamName;

    @Column(name = "parent_team_id")
    private Integer parentTeamId;

    private String path;

    private Integer level;

    @Column(name = "display_index")
    private Integer displayIndex;

    /**
     * # 用户支付情况 10=试用 20=付费用户
     */
    @Column(name = "payment_type")
    private Integer paymentType;

    /**
     * # 集团归属用户id，只有集团才会有，其它情况为0
     */
    @Column(name = "owner_user_id")
    private Integer ownerUserId;

    @Column(name = "group_code")
    private String groupCode;

    @Column(name = "create_at")
    private Date createAt;

    @Column(name = "update_at")
    private Date updateAt;

    @Column(name = "delete_at")
    private Date deleteAt;

    /**
     * 客户系统唯一识别码
     */
    @Column(name = "custom_id")
    private String customId;

    private String setting;

    /**
     * 客户系统扩展信息
     */
    @Column(name = "custom_extra")
    private String customExtra;

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
     * @return parent_team_id
     */
    public Integer getParentTeamId() {
        return parentTeamId;
    }

    /**
     * @param parentTeamId
     */
    public void setParentTeamId(Integer parentTeamId) {
        this.parentTeamId = parentTeamId;
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
     * @return level
     */
    public Integer getLevel() {
        return level;
    }

    /**
     * @param level
     */
    public void setLevel(Integer level) {
        this.level = level;
    }

    /**
     * @return display_index
     */
    public Integer getDisplayIndex() {
        return displayIndex;
    }

    /**
     * @param displayIndex
     */
    public void setDisplayIndex(Integer displayIndex) {
        this.displayIndex = displayIndex;
    }

    /**
     * 获取# 用户支付情况 10=试用 20=付费用户
     *
     * @return payment_type - # 用户支付情况 10=试用 20=付费用户
     */
    public Integer getPaymentType() {
        return paymentType;
    }

    /**
     * 设置# 用户支付情况 10=试用 20=付费用户
     *
     * @param paymentType # 用户支付情况 10=试用 20=付费用户
     */
    public void setPaymentType(Integer paymentType) {
        this.paymentType = paymentType;
    }

    /**
     * 获取# 集团归属用户id，只有集团才会有，其它情况为0
     *
     * @return owner_user_id - # 集团归属用户id，只有集团才会有，其它情况为0
     */
    public Integer getOwnerUserId() {
        return ownerUserId;
    }

    /**
     * 设置# 集团归属用户id，只有集团才会有，其它情况为0
     *
     * @param ownerUserId # 集团归属用户id，只有集团才会有，其它情况为0
     */
    public void setOwnerUserId(Integer ownerUserId) {
        this.ownerUserId = ownerUserId;
    }

    /**
     * @return group_code
     */
    public String getGroupCode() {
        return groupCode;
    }

    /**
     * @param groupCode
     */
    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode == null ? null : groupCode.trim();
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
     * 获取客户系统唯一识别码
     *
     * @return custom_id - 客户系统唯一识别码
     */
    public String getCustomId() {
        return customId;
    }

    /**
     * 设置客户系统唯一识别码
     *
     * @param customId 客户系统唯一识别码
     */
    public void setCustomId(String customId) {
        this.customId = customId == null ? null : customId.trim();
    }

    /**
     * @return setting
     */
    public String getSetting() {
        return setting;
    }

    /**
     * @param setting
     */
    public void setSetting(String setting) {
        this.setting = setting == null ? null : setting.trim();
    }

    /**
     * 获取客户系统扩展信息
     *
     * @return custom_extra - 客户系统扩展信息
     */
    public String getCustomExtra() {
        return customExtra;
    }

    /**
     * 设置客户系统扩展信息
     *
     * @param customExtra 客户系统扩展信息
     */
    public void setCustomExtra(String customExtra) {
        this.customExtra = customExtra == null ? null : customExtra.trim();
    }
}