package com.longfor.longjian.houseqm.app.vo.issue;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.app.vo.issue
 * @ClassName: IssueBatchDeleteRspVo
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/1/12 18:48
 */
@Data
@NoArgsConstructor
public class IssueBatchDeleteRspVo implements Serializable {
    private List<String> fails;// 操作失败问题Uuids
}
