package com.longfor.longjian.houseqm.app.vo.houseqmstatistic;

import com.longfor.longjian.houseqm.app.vo.HouseqmStatisticCategoryIssueListRspMsgVo;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.app.vo.houseqmstatisticapp
 * @ClassName: HouseqmStatisticTaskIssueRepairListRsp
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/1/18 16:38
 */
@Data
@NoArgsConstructor
public class HouseqmStatisticTaskIssueRepairListRsp implements Serializable {
    private List<HouseqmStatisticCategoryIssueListRspMsgVo.ApiTaskIssueRepairListRsp> issue_list;
    private Integer total;


}
