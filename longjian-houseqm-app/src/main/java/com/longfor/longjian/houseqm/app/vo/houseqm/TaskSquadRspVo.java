package com.longfor.longjian.houseqm.app.vo.houseqm;

import com.longfor.longjian.houseqm.app.vo.HouseQmCheckTaskSquadListRspVo;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.app.vo.houseqm
 * @ClassName: TaskSquadRspVo
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/1/26 11:30
 */
@Data
@NoArgsConstructor
public class TaskSquadRspVo implements Serializable {

    private List<HouseQmCheckTaskSquadListRspVo> squad_list;
}
