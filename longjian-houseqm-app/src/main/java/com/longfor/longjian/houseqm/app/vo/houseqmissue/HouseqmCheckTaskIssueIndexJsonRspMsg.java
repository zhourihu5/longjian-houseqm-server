package com.longfor.longjian.houseqm.app.vo.houseqmissue;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.app.vo.houseqmissue
 * @ClassName: HouseqmCheckTaskIssueIndexJsonRspMsg
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/1/22 16:24
 */
@Data
@NoArgsConstructor
public class HouseqmCheckTaskIssueIndexJsonRspMsg implements Serializable {
    private String html;
    private List<StatExportPic> pics;
    private List<ChartImage> charts;
}
