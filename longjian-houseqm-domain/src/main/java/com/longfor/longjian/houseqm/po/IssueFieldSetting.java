package com.longfor.longjian.houseqm.po;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Dongshun on 2018/12/11.
 */
@Data
@Table(name = "issue_field_setting")
public class IssueFieldSetting implements Serializable {
    @Id
    private Integer id;
    @Column(name = "project_id")
    private Integer projectId;
    @Column(name = "field_id")
    private Integer fieldId;
    @Column(name = "name")
    private String name;
    @Column(name = "alias")
    private String alias;
    @Column(name = "display_status")
    private Integer displayStatus;
    @Column(name = "required_status")
    private Integer requiredStatus;
    @Column(name = "alias_status")
    private Integer aliasStatus;
    @Column(name = "module_id")
    private Integer moduleId;
    @Column(name = "modify_user_id")
    private Integer modifyUserId;
    @Column(name = "create_at")
    private Date createAt;
    @Column(name = "update_at")
    private Date updateAt;
    @Column(name = "delete_at")
    private Date deleteAt;
}
