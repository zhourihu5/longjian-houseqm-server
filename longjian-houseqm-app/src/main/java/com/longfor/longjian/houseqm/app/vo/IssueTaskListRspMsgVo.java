package com.longfor.longjian.houseqm.app.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Dongshun on 2018/12/17.
 */
@Data
@NoArgsConstructor
public class IssueTaskListRspMsgVo implements Serializable {
    private List<HouseQmCheckTaskSimpleRspVo> taskList;
}
