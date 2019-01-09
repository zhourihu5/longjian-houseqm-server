package com.longfor.longjian.houseqm.app.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.app.vo
 * @ClassName: TaskTaskRoleRspVo
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/1/9 11:38
 */
@Data
@NoArgsConstructor
public class TaskTaskRoleRspVo implements Serializable {
    private List<HouseQmCheckTaskRoleListRspVo> role_list;
}
