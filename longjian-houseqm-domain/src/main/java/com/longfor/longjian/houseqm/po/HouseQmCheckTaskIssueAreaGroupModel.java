package com.longfor.longjian.houseqm.po;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * @author Houyan
 * @date 2018/12/18 0018 16:20
 */
@Table(name = "house_qm_check_task_issue")
public class HouseQmCheckTaskIssueAreaGroupModel {


    @Column(name = "area_id")
    private Integer areaId;
    @Column(name = "status")
    private Integer status;

    @Column(name = "area_path")
    private String areaPath;
    @Column(name = "extend_col")
    private Integer extendCol;

    public Integer getAreaId() {
        return areaId;
    }

    public void setAreaId(Integer areaId) {
        this.areaId = areaId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getAreaPath() {
        return areaPath;
    }

    public void setAreaPath(String areaPath) {
        this.areaPath = areaPath;
    }

    public Integer getExtendCol() {
        return extendCol;
    }

    public void setExtendCol(Integer extendCol) {
        this.extendCol = extendCol;
    }

}
