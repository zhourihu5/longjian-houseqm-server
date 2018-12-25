package com.longfor.longjian.houseqm.app.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by Dongshun on 2018/12/22.
 */
@Data
@NoArgsConstructor
public class HouseQmCheckTaskIssueOnlineInfoVo implements Serializable {
   private  Integer Id ;
    private  Integer ProjectId;
    private Integer TaskId;
    private String Uuid ;
    private String Title;
    private Integer Typ;
    private String Content;
    private Integer Condition;
    private  Integer Status;
    private Date PlanEndOn;
    private  String AttachmentMd5List;
    private Date ClientCreateAt;
    private Date UpdateAt;
    private Integer AreaId;
    private String CategoryKey;
    private String CategoryPathAndKey;
    private String CheckItemKey;
    private String CheckItemPathAndKey;
    private List<String> AreaPathName;
    private String AreaName;
    private List<String> CategoryPathName;
    private String CategoryName;
    private List<String> CheckItemPathName;
    private String CheckItemName;
    private List<String> AttachmentUrlList;

}
