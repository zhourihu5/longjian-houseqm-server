package com.longfor.longjian.houseqm.app.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Dongshun on 2018/12/14.
 */
@Data
@NoArgsConstructor
public class HouseQmCheckTaskVo implements Serializable {
    private Integer id;
    private int projectId;
    private Integer taskId;
    private String name;
    private Integer status;
    private Integer categoryCls;
    private String rootCategoryKey;
    private String areaIds;
    private String areaTypes;
    private Date planBeginOn;
    private Date planEndOn;
    private Date endOn;
    private Integer creator;
    private Integer editor;
    private Date createAt;
    private Date updateAt;
    private Date deleteAt;
    private String configInfo;
    private Date deliveryBeginOn;
    private Date deliveryEndOn;
}
