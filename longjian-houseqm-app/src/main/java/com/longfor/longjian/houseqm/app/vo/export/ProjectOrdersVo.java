package com.longfor.longjian.houseqm.app.vo.export;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.app.vo.export
 * @ClassName: ProjectOrdersVo
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/1/14 10:27
 */
@Data
@NoArgsConstructor
public class ProjectOrdersVo implements Serializable {

    private String title;
    private String projectName;
    private String areaNames;
    private String houseOwnerName;
    private String houseOwnerPhone;
    private boolean isViewHouseOwner;
    private List<ProjectIssueInfo> issueItems;


}
