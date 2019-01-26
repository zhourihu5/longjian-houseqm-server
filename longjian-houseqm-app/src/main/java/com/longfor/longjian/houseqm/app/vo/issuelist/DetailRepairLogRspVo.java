package com.longfor.longjian.houseqm.app.vo.issuelist;

import com.longfor.longjian.houseqm.app.vo.HouseQmCheckTaskIssueDetailRepairLogVo;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.app.vo.issuelist
 * @ClassName: DetailRepairLogRspVo
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/1/26 14:04
 */
@Data
@NoArgsConstructor
public class DetailRepairLogRspVo implements Serializable {

    private List<HouseQmCheckTaskIssueDetailRepairLogVo> items;
}
