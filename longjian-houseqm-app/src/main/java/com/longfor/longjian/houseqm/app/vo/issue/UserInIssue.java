package com.longfor.longjian.houseqm.app.vo.issue;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.app.vo.issue
 * @ClassName: UserInIssueVo
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/1/11 15:31
 */
@Data
@NoArgsConstructor
public class UserInIssue implements Serializable {
    //UserRole map[int]int
    //	Modified bool
    //	TaskId   int
    private Map<Integer,Integer> userRole;
    private boolean modified;
    private int taskId;
}
