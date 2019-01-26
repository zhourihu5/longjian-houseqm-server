package com.longfor.longjian.houseqm.app.vo.issuelist;

import com.longfor.longjian.houseqm.app.vo.HouseQmCheckTaskIssueHistoryLogVo;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.app.vo.issuelist
 * @ClassName: DetailLogRspVo
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/1/26 11:35
 */
@Data
@NoArgsConstructor
public class DetailLogRspVo implements Serializable {

    private List<HouseQmCheckTaskIssueHistoryLogVo> items;
}
