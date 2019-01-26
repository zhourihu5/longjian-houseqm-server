package com.longfor.longjian.houseqm.app.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Dongshun on 2019/1/19.
 */
@Data
@NoArgsConstructor
public class HouseQmCheckTaskIssueVoRsp implements Serializable {
    private Integer total;
    private List<HouseQmCheckTaskIssueOnlineInfoVo>items;
}
