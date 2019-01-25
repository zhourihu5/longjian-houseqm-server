package com.longfor.longjian.houseqm.app.vo.houseqmstatistic;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.app.vo.houseqmstatistic
 * @ClassName: HouseqmStatisticProjectIssueRepairRspVo
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/1/24 10:58
 */
@Data
@NoArgsConstructor
public class HouseqmStatisticTaskIssueRepairRsp implements Serializable {

    private HouseqmStatisticProjectIssueRepairRsp.ApiHouseQmIssueRepairStat item;

}
