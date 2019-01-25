package com.longfor.longjian.houseqm.app.vo.task;

import com.longfor.longjian.houseqm.po.HouseQmCheckTask;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.app.vo.task
 * @ClassName: HouseQmCheckTaskVo
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/1/24 11:40
 */
@Data
@NoArgsConstructor
public class HouseQmCheckTaskListAndTotalVo implements Serializable {

    private List<HouseQmCheckTask> list;
    private Integer total;
}
