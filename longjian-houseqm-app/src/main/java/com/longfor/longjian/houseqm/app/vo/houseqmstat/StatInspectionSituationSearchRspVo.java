package com.longfor.longjian.houseqm.app.vo.houseqmstat;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.app.vo.houseqmstat
 * @ClassName: StatInspectionSituationSearchRspVo
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/1/29 10:46
 */
@Data
@NoArgsConstructor
public class StatInspectionSituationSearchRspVo implements Serializable {
    private Integer total;//总数
    private List<HouseQmStatInspectionSituationRspVo> items;
}
