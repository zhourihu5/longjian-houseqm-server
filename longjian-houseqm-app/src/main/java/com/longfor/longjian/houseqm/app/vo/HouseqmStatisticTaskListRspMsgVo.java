package com.longfor.longjian.houseqm.app.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created by Dongshun on 2019/1/17.
 */
@Data
@NoArgsConstructor
public class HouseqmStatisticTaskListRspMsgVo {
    List<ApiTaskInfo>items;
}
