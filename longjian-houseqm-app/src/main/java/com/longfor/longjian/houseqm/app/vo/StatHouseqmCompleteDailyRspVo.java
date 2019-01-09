package com.longfor.longjian.houseqm.app.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.app.vo
 * @ClassName: StatHouseqmCompleteDailyRsp
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/1/8 20:23
 */
@Data
@NoArgsConstructor
public class StatHouseqmCompleteDailyRspVo implements Serializable {

    private List<HouseQmHouseQmStatCompleteDailyRspVo> items;// items
    private Integer total;// 总数

}
