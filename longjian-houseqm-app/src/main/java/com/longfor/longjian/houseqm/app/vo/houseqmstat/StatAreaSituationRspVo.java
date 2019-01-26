package com.longfor.longjian.houseqm.app.vo.houseqmstat;

import com.longfor.longjian.houseqm.app.vo.HouseQmStatAreaSituationIssueRspVo;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.app.vo.houseqmstat
 * @ClassName: StatAreaSituationRspVo
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/1/25 14:04
 */
@Data
@NoArgsConstructor
public class StatAreaSituationRspVo implements Serializable {

    private HouseQmStatAreaSituationIssueRspVo issue;
}
