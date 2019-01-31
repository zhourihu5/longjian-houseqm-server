package com.longfor.longjian.houseqm.po.zj2db;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Dongshun on 2018/12/25.
 */
@Data
@NoArgsConstructor
@Table(name = "fixing_preset")
public class FixingPreset implements Serializable {
    @Id
    private Integer id;
    @Column(name = "project_id")
    private Integer projectId;
    @Column(name = "area_id")
    private Integer areaId;
    @Column(name = "root_category_key")
    private String rootCategoryKey;
    @Column(name = "category_key")
    private String categoryKey;
    @Column(name = "check_item_key")
    private String checkItemKey;
    @Column(name = "user_ids")
    private String userIds;
    @Column(name = "days")
    private Integer days;
    @Column(name = "typ")
    private Integer typ;
    @Column(name = "create_at")
    private Date createAt;
    @Column(name = "update_at")
    private Date updateAt;
    @Column(name = "delete_at")
    private Date deleteAt;
    @Column(name = "minutes")
    private Integer minutes;
}
