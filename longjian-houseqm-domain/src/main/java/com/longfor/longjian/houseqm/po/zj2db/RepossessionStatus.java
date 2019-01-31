package com.longfor.longjian.houseqm.po.zj2db;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by Dongshun on 2018/12/29.
 */
@Data
@Table(name = "repossession_status")
public class RepossessionStatus {
    @Id
    private Integer id;
    @Column(name = "project_id")
    private Integer projectId;
    @Column(name = "task_id")
    private Integer taskId;
    @Column(name = "area_id")
    private Integer areaId;
    @Column(name = "area_path_and_id")
    private String areaPathAndId;
    @Column(name = "status")
    private Integer status;
    @Column(name = "status_client_update_at")
    private Date statusClientUpdateAt;
    @Column(name = "has_take_key")
    private Integer hasTakeKey;
    @Column(name = "trust_key_count")
    private Integer trustKeyCount;
    @Column(name = "key_client_update_at")
    private Date keyClientUpdateAt;
    @Column(name = "accompany_sign_md5_list")
    private String accompanySignMd5List;
    @Column(name = "accompany_sign_client_update_at")
    private Date accompanySignClientUpdateAt;
    @Column(name = "sign_status")
    private Integer signStatus;
    @Column(name = "sign_comment")
    private String signComment;
    @Column(name = "sign_md5_list")
    private String signMd5List;
    @Column(name = "sign_client_update_at")
    private Date signClientUpdateAt;
    @Column(name = "remark")
    private String remark;
    @Column(name = "expect_date")
    private Date expectDate;
    @Column(name = "repossession_check_status")
    private Integer repossessionCheckStatus;
    @Column(name = "repossession_check_status_client_update_at")
    private Date repossessionCheckStatusClientUpdateAt;
    @Column(name = "repair_status")
    private Integer repairStatus;
    @Column(name = "repair_sign_md5_list")
    private String repairSignMd5List;
    @Column(name = "repair_client_update_at")
    private Date repairClientUpdateAt;
    @Column(name = "create_at")
    private Date createAt;
    @Column(name = "update_at")
    private Date updateAt;
    @Column(name = "delete_at")
    private Date deleteAt;
    @Column(name = "uuid")
    private String uuid;
    @Column(name = "modify_user_id")
    private Integer modifyUserId;
    @Column(name = "satisfaction_score")
    private Integer satisfactionScore;
}
