package com.longfor.longjian.houseqm.app.vo;

import com.longfor.longjian.houseqm.po.zhijian2_notify.GcglIssueStatRecord;
import lombok.Data;

/**
 * Created by Wang on 2019/3/4.
 */
@Data
public class GcglIssueStatRecordVo {
    private GcglIssueStatRecord gcglIssueStatRecord;

    private String teamName;

    private String srcUserName;

    private String moduleName;

    private String projectName;

    private String desUserName;

}
