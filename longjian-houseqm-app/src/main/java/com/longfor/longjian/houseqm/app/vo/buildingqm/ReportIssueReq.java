package com.longfor.longjian.houseqm.app.vo.buildingqm;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.app.vo.buildingqm
 * @ClassName: ReportIssueReq
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/2/26 20:27
 */
@Data
@NoArgsConstructor
public class ReportIssueReq implements Serializable {
    @NotNull
    private Integer project_id;
    @NotNull
    private String data;

}
