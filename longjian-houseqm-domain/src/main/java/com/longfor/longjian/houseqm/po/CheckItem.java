package com.longfor.longjian.houseqm.po;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Dongshun on 2018/12/21.
 */
@Data
@Table(name = "check_item_v3")
public class CheckItem implements Serializable {
    @Id
    private Integer id;
    @Column(name = "father_key")
    private String fatherKey;
    @Column(name = "key")
    private String key;
    @Column(name = "path")
    private String path;
    @Column(name = "team_id")
    private Integer teamId;
    @Column(name = "root_category_id")
    private Integer rootCategoryId;
    @Column(name = "category_order")
    private String categoryOrder;
    @Column(name = "category_key")
    private String categoryKey;
    @Column(name = "order")
    private String order;
    @Column(name = "zj_std_key")
    private String zjStdKey;
    @Column(name = "custom_key")
    private String customKey;
    @Column(name = "name")
    private String name;
    @Column(name = "desc")
    private String desc;
    @Column(name = "common_issues")
    private String commonIssues;
    @Column(name = "required_type")
    private Integer requiredType;
    @Column(name = "create_at")
    private Date createAt;
    @Column(name = "update_at")
    private Date updateAt;
    @Column(name = "delete_at")
    private Date deleteAt;


}
