package com.longfor.longjian.houseqm.po;

import javax.persistence.Table;

/**
 * @author Houyan
 * @date 2018/12/19 0019 11:23
 */
@Table(name = "house_qm_check_task_issue")
public class IssueRepairCount {

    private Integer total;
    private Integer noPlanEndOn;
    private Integer overtimeUnfinish;
    private Integer initimeUnfinish;
    private Integer overtimeFinish;
    private Integer initimeFinish;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getNoPlanEndOn() {
        return noPlanEndOn;
    }

    public void setNoPlanEndOn(Integer noPlanEndOn) {
        this.noPlanEndOn = noPlanEndOn;
    }

    public Integer getOvertimeUnfinish() {
        return overtimeUnfinish;
    }

    public void setOvertimeUnfinish(Integer overtimeUnfinish) {
        this.overtimeUnfinish = overtimeUnfinish;
    }

    public Integer getInitimeUnfinish() {
        return initimeUnfinish;
    }

    public void setInitimeUnfinish(Integer initimeUnfinish) {
        this.initimeUnfinish = initimeUnfinish;
    }

    public Integer getOvertimeFinish() {
        return overtimeFinish;
    }

    public void setOvertimeFinish(Integer overtimeFinish) {
        this.overtimeFinish = overtimeFinish;
    }

    public Integer getInitimeFinish() {
        return initimeFinish;
    }

    public void setInitimeFinish(Integer initimeFinish) {
        this.initimeFinish = initimeFinish;
    }
}
