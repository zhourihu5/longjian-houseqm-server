package com.longfor.longjian.houseqm.app.vo.houseqm;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.app.vo.houseqm
 * @ClassName: MyIssueLogListVo
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/1/21 15:05
 */
@Data
@NoArgsConstructor
public class HouseqmMyIssueLogListRspVo implements Serializable {
    private List<ApiHouseQmCheckTaskIssueLogRsp> issue_list;
    private Integer last_id;
}
