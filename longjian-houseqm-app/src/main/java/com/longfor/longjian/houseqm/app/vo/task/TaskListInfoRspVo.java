package com.longfor.longjian.houseqm.app.vo.task;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.omg.CORBA.INTERNAL;

import java.io.Serializable;
import java.util.List;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.app.vo.task
 * @ClassName: TaskListInfoRspVo
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/1/24 11:17
 */
@Data
@NoArgsConstructor
public class TaskListInfoRspVo implements Serializable {

    //TaskList []*HouseQmCheckTaskInfoRsp `json:"task_list" zpf_name:"task_list"`         // task_list
    //	Total    int                        `json:"total" zpf_reqd:"true" zpf_name:"total"` // 数量
    private List<HouseQmCheckTaskInfoRspVo> task_list;
    private Integer total;//数量

}
