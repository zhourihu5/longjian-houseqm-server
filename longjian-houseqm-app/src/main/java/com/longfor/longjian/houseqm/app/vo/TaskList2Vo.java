package com.longfor.longjian.houseqm.app.vo;

import com.longfor.longjian.houseqm.innervo.ApiBuildingQmCheckTaskMsg;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author Houyan
 * @date 2018/12/13 0013 20:45
 */
@Data
@NoArgsConstructor
public class TaskList2Vo implements Serializable {
    private List<ApiBuildingQmCheckTaskMsg> task_list;
}
