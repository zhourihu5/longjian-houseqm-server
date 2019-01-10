package com.longfor.longjian.houseqm.app.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.app.vo
 * @ClassName: StatTaskSituationMembersRepairerRspVo
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/1/10 14:57
 */
@Data
@NoArgsConstructor
public class StatTaskSituationMembersRepairerRspVo implements Serializable {

    private List<HouseQmStatTaskDetailMemberRepairerRspVo> items;//items
}
