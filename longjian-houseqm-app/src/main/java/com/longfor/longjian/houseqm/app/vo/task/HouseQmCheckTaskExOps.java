package com.longfor.longjian.houseqm.app.vo.task;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.app.vo.task
 * @ClassName: HouseQmCheckTaskExOps
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/1/24 14:33
 */
@Data
@NoArgsConstructor
public class HouseQmCheckTaskExOps implements Serializable {
    private String export_issue;// 导出问题报告
}
