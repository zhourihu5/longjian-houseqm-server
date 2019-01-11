package com.longfor.longjian.houseqm.app.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.app.vo
 * @ClassName: IssueBatchAppointRspVo
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/1/11 14:02
 */
@Data
@NoArgsConstructor
public class IssueBatchAppointRspVo implements Serializable {

    private List<String> fails;// 操作失败问题Uuids
}
