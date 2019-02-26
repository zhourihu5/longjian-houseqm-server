package com.longfor.longjian.houseqm.app.vo.buildingqm;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.omg.CORBA.INTERNAL;

import java.io.Serializable;
import java.util.List;

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
    private Integer project_id;
    private String data;

}
