package com.longfor.longjian.houseqm.app.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by Dongshun on 2018/12/17.
 */
@Data
@NoArgsConstructor
public class HouseqmStatisticGetDaterangeOptionsRspMsgVo implements Serializable {

    private List<ApiDateRangeOption> items;
}